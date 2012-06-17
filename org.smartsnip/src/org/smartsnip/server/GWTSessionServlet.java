package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smartsnip.core.Logging;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.shared.ISession;
import org.smartsnip.shared.XServerStatus;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This class should be used as supercalas for RemoteServiceServlets, if they
 * need to get the session cookie
 * 
 */
public class GWTSessionServlet extends RemoteServiceServlet {

	/** Serialisation ID */
	private static final long serialVersionUID = -5691219932423526109L;

	/** Assosciated session for this servlet call */
	private Session session = null;

	/**
	 * Default constructor handles the session initialisation
	 */
	public GWTSessionServlet() {
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
				cookie = new Cookie(ISession.cookie_Session_ID,
						session.getCookie());
				addCookie(cookie);
				Logging.printInfo("New Session (SID=" + cookie
						+ ") attached. Host: " + getRemoteHost());
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

	/**
	 * Prints a session specific log message. Additional information will be
	 * attached to the message
	 * 
	 * @param message
	 *            to be printed
	 */
	protected void logInfo(String message) {
		if (message == null || message.isEmpty())
			return;

		String cookie = getSession().getCookie();
		if (cookie == null) {
			Logging.printInfo("(" + getRemoteHost() + "): " + message);
		} else {
			String user = session.getUsername();
			if (user.equalsIgnoreCase("guest"))
				Logging.printInfo("(SID=" + cookie + "): " + message);
			else
				Logging.printInfo("(SID=" + cookie + ", USER=" + user + "): "
						+ message);
		}
	}

	/**
	 * Prints a session specific log message that is an error. Additional
	 * information will be attached to the message
	 * 
	 * @param message
	 *            to be printed
	 */
	protected void logError(String message) {
		if (message == null || message.isEmpty())
			return;

		String cookie = getSession().getCookie();
		if (cookie == null) {
			Logging.printInfo("(" + getRemoteHost() + "): " + message);
		} else {
			String user = session.getUsername();
			if (user.equalsIgnoreCase("guest"))
				Logging.printError("(SID=" + cookie + "): " + message);
			else
				Logging.printError("(SID=" + cookie + ", USER=" + user + "): "
						+ message);
		}
	}

	/**
	 * Prints a session specific log message that is an error. Additional
	 * information will be attached to the message
	 * 
	 * @param cause
	 *            to be printed
	 */
	protected void logError(Throwable cause) {
		if (cause == null)
			return;
		logError(cause.getMessage());
	}

	protected String getRemoteHost() {
		HttpServletRequest request = getThreadLocalRequest();
		if (request == null)
			return "0.0.0.0";
		return request.getRemoteHost();
	}

	/**
	 * Converts a list of snippets to a list of XSnippets
	 * 
	 * @param snippets
	 *            Source list
	 * @return a list of {@link XSnippet}
	 */
	protected final List<XSnippet> toXSnippets(List<Snippet> snippets) {
		if (snippets == null)
			return null;

		Session session = getSession();
		User user = session.getUser();

		List<XSnippet> result = new ArrayList<XSnippet>();
		for (Snippet snippet : snippets)
			result.add(toXSnippet(snippet, session, user));

		return result;
	}

	/**
	 * Converts a snippet to a eXchange Snippet ({@link XSnippet} object.
	 * 
	 * Use this method instance of {@link Snippet#toXSnippet()} to initialise
	 * also the session- and user specific options like
	 * {@link XSnippet#isFavorite}, like {@link XSnippet#canDelete} and like
	 * {@link XSnippet#canEdit}
	 * 
	 * @param snippet
	 *            to be converted
	 * @return the converted {@link XSnippet} object
	 */
	protected final XSnippet toXSnippet(Snippet snippet) {
		Session session = getSession();
		return toXSnippet(snippet, session, session.getUser());
	}

	/**
	 * Converts a snippet to a eXchange Snippet ({@link XSnippet} object.
	 * 
	 * Use this method instance of {@link Snippet#toXSnippet()} to initialise
	 * also the session- and user specific options like
	 * {@link XSnippet#isFavorite}, like {@link XSnippet#canDelete} and like
	 * {@link XSnippet#canEdit}
	 * 
	 * This call should only be used internally, because no checks for session
	 * and user are done. The calling method must be sure, that the given
	 * parameters are right, otherwise the behaviour can be unpredictable
	 * 
	 * @param snippet
	 *            to be converted
	 * @param session
	 *            Session to be user
	 * @param user
	 *            User to be used
	 * @return the converted {@link XSnippet} object
	 */
	private final XSnippet toXSnippet(Snippet snippet, Session session,
			User user) {
		if (snippet == null)
			return null;

		XSnippet result = snippet.toXSnippet();

		if (user == null) {
			result.isFavorite = session.isFavourite(snippet);
			result.myRating = 0;
			result.isOwn = false;
		} else {
			result.isFavorite = user.isFavourite(snippet);
			result.myRating = user.getSnippetRating(snippet.getHashId());
			result.isOwn = user.getUsername().equalsIgnoreCase(
					snippet.getOwnerUsername());
		}
		result.canRate = session.getPolicy().canRateSnippet(session, snippet);
		result.canDelete = session.getPolicy().canDeleteSnippet(session,
				snippet);
		result.canEdit = session.getPolicy().canEditSnippet(session, snippet);

		return result;
	}
}
