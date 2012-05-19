package org.smartsnip.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.params.HttpAbstractParamBean;
import org.smartsnip.shared.ISession;
import org.smartsnip.shared.XServerStatus;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This class should be used as supercalas for RemoteServiceServlets, if they
 * need to get the session cookie
 * 
 */
public class SessionServlet extends RemoteServiceServlet {

	/** Serialisation ID */
	private static final long serialVersionUID = -5691219932423526109L;

	/** Assosciated session for this servlet call */
	private Session session = null;

	/**
	 * Default constructor handles the session initialisation
	 */
	public SessionServlet() {
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

		HttpServletResponse response = getThreadLocalResponse();
		if (response == null) {
			System.err.println("getThreadLocalResponse() delivered NULL");
			return;
		}

		response.addCookie(cookie);
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

		HttpServletRequest request = getThreadLocalRequest();
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
	protected final Session getSession() {
		if (session != null)
			return session;

		synchronized (this) {
			/*
			 * Thread-Saftey: This call MUST be executed once again in the
			 * synchronized block!!
			 */
			if (session != null)
				return session;

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
					session = Session.getSession(sid);
				}
			}

			// Does a activity on the running session
			session.doActivity();
			return session;
		}
	}

	public XServerStatus getServerStatus() {
		HttpServletRequest request = getThreadLocalRequest();
		Runtime runtime = Runtime.getRuntime();

		XServerStatus result = new XServerStatus();
		result.servername = request.getServerName();
		result.totalMemory = runtime.totalMemory();
		result.freeMemory = runtime.freeMemory();
		result.maxMemory = runtime.maxMemory();

		return result;
	}
}
