package org.smartsnip.server;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.smartsnip.shared.ISession;

public class SessionServlet extends HttpServlet {

	/** Associated session for this servlet call */
	private Session session = null;

	/**
	 * Gets a cookie from the HTTP servlet session.
	 * 
	 * If the name is null or enmpty, null is returned.
	 * 
	 * @param name
	 *            name of the cookie
	 * @return the stored cookie, or null if not existing. Returns also null, if
	 *         the given name is null or empty
	 */
	protected Cookie getCookie(String name, HttpServletRequest request) {
		if (name == null || name.isEmpty())
			return null;

		if (request == null) {
			System.err.println("getThreadLocalRequest() delivered NULL");
			return null;
		}

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name))
					return cookie;
			}
		}
		return null;
	}

	/**
	 * Gets the session for this servlet object
	 * 
	 * @return the session for this revlet object
	 */
	protected final Session getSession(HttpServletRequest request) {
		if (session != null)
			return session;

		synchronized (this) {
			/*
			 * Thread-Saftey: This call MUST be executed once again in the
			 * synchronized block!!
			 */
			if (session != null)
				return session;

			Cookie cookie = getCookie(ISession.cookie_Session_ID, request);

			if (cookie == null)
				return null;
			else {
				String sid = cookie.getValue();
				if (sid == null) {
					session = Session.getStaticGuestSession();
				} else {
					session = Session.getSession(sid);
				}
			}

			// Does a activity on the running session
			session.doActivity();
			return session;
		}
	}

}
