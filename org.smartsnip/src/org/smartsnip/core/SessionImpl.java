package org.smartsnip.core;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.smartsnip.shared.ISession;

/**
 * This class acts as the servlet that coordiantes the transactions between the
 * session and the client
 * 
 */
public class SessionImpl extends SessionServlet implements ISession {

	/** Serialisation ID */
	private static final long serialVersionUID = 51299L;

	/** Gets the username that is currenlt logged in, or null if a guest session */
	@Override
	public String getUsername() {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HttpSession getSession() {
		return getThreadLocalRequest().getSession(true);
	}

	@Override
	protected void addCookie(Cookie cookie) {
		getThreadLocalResponse().addCookie(cookie);
	}

	@Override
	protected void addCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		addCookie(cookie);
	}

	@Override
	protected void removeCookie(String name) {
		Cookie cookie = this.getCookie(name);
		if (cookie != null) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			addCookie(cookie);
		}
	}

	@Override
	protected Cookie getCookie(String name) {
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
