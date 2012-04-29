package org.smartsnip.core;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.smartsnip.shared.ISession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This class should be used as supercalas for RemoteServiceServlets, if they
 * need to get the session cookie
 * 
 */
public class SessionServlet extends RemoteServiceServlet {

	/** Assosciated session for this servlet call */
	protected final Session session;

	/**
	 * Default constructor handles the session initialisation
	 */
	public SessionServlet() {
		Cookie cookie = getCookie(ISession.cookie_Session_ID);

		if (cookie == null) {
			session = Session.createNewSession();
			cookie = new Cookie(ISession.cookie_Session_ID, session.getCookie());
			addCookie(cookie);
		} else {
			/** TODO: Check handling if cookies are disabled in browser */
			String sid = cookie.getValue();
			if (sid == null) {
				session = Session.getStaticGuestSession();
			} else {
				session = Session.getSession(cookie.getValue());
			}
		}

		// Does a activity on the running session
		session.doActivity();
	}

	/**
	 * @return gets the HTTP session of the servlet
	 */
	protected HttpSession getSession() {
		return getThreadLocalRequest().getSession(true);
	}

	/**
	 * Adds a cookie to the http servlet session.
	 * 
	 * If the cookie is null nothing will be done
	 * 
	 * @param cookie
	 *            to be added
	 */
	protected void addCookie(Cookie cookie) {
		if (cookie == null)
			return;
		getThreadLocalResponse().addCookie(cookie);
	}

	/**
	 * Adds a cookie to the http servlet session.
	 * 
	 * If the name or the value is null, or if the name is empty, nothing will
	 * be done
	 * 
	 * @param name
	 *            Name of the cookie to be added
	 * @param value
	 *            Value of the cookie
	 */
	protected void addCookie(String name, String value) {
		if (name == null || value == null || name.isEmpty())
			return;

		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		addCookie(cookie);
	}

	/**
	 * Remoes a given cookie from the http servlet session. If the name is null,
	 * nothing is done. If the cookie doesn't exists, nothing is done
	 * 
	 * @param name
	 *            of the cookie to be removed
	 */
	protected void removeCookie(String name) {
		if (name == null)
			return;
		Cookie cookie = this.getCookie(name);
		if (cookie != null) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			addCookie(cookie);
		}
	}

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
	protected Cookie getCookie(String name) {
		if (name == null || name.isEmpty())
			return null;

		Cookie[] cookies = getThreadLocalRequest().getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name))
					return cookie;
			}
		}
		return null;
	}

}
