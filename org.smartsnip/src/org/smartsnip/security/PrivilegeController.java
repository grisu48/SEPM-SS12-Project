package org.smartsnip.security;

import org.smartsnip.core.Category;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.server.Session;

public class PrivilegeController {

	/** Default hard coded internal guest access policy */
	private final static IAccessPolicy guestAccessPolicy = new IAccessPolicy() {

		@Override
		public boolean canSearch(Session session) {
			return true;
		}

		@Override
		public boolean canRegister(Session session) {
			return true;
		}

		@Override
		public boolean canLogin(Session session) {
			return true;
		}

		@Override
		public boolean canEditUserData(Session session, User user) {
			return false;
		}

		@Override
		public boolean canEditSnippet(Session session, Snippet snippet) {
			return false;
		}

		@Override
		public boolean canDeleteSnippet(Session session, Snippet snippet) {
			return false;
		}

		@Override
		public boolean canCreateSnippet(Session session, Category category) {
			return false;
		}

		@Override
		public boolean canTagSnippet(Session session, Snippet snippet) {
			return false;
		}

		@Override
		public boolean canRateSnippet(Session session, Snippet snippet) {
			return false;
		}

		@Override
		public boolean canComment(Session session) {
			return false;
		}

		@Override
		public boolean canEditComment(Session session, Comment comment) {
			return false;
		}

		@Override
		public boolean canEditCategory(Session session, Category category) {
			return false;
		}

		@Override
		public boolean canDeleteComment(Session session, Comment comment) {
			return false;
		}
	};

	/** Hard coded user access policy */
	private final static IAccessPolicy userAccessPolicy = new IAccessPolicy() {

		@Override
		public boolean canTagSnippet(Session session, Snippet snippet) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canSearch(Session session) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canRegister(Session session) {
			return !session.isLoggedIn();
		}

		@Override
		public boolean canRateSnippet(Session session, Snippet snippet) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canLogin(Session session) {
			return !session.isLoggedIn();
		}

		@Override
		public boolean canEditUserData(Session session, User user) {
			return session.isLoggedInUser(user);
		}

		@Override
		public boolean canEditSnippet(Session session, Snippet snippet) {
			if (!session.isLoggedIn())
				return false;
			return session.isLoggedInUser(snippet.getOwner());
		}

		@Override
		public boolean canDeleteSnippet(Session session, Snippet snippet) {
			if (!session.isLoggedIn())
				return false;
			return session.isLoggedInUser(snippet.getOwner());
		}

		@Override
		public boolean canCreateSnippet(Session session, Category category) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canComment(Session session) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canEditComment(Session session, Comment comment) {
			if (!session.isLoggedIn())
				return false;
			return session.isLoggedInUser(comment.owner);
		}

		@Override
		public boolean canEditCategory(Session session, Category category) {
			// Not available for user
			return false;
		}

		@Override
		public boolean canDeleteComment(Session session, Comment comment) {
			if (!session.isLoggedIn())
				return false;
			return session.isLoggedInUser(comment.owner);
		}
	};

	/** Access policy for the moderator */
	private static IAccessPolicy moderatorAccessPolicy = new IAccessPolicy() {

		@Override
		public boolean canTagSnippet(Session session, Snippet snippet) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canSearch(Session session) {
			return true;
		}

		@Override
		public boolean canRegister(Session session) {
			return true;
		}

		@Override
		public boolean canRateSnippet(Session session, Snippet snippet) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canLogin(Session session) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canEditUserData(Session session, User user) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canEditSnippet(Session session, Snippet snippet) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canEditComment(Session session, Comment comment) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canEditCategory(Session session, Category category) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canDeleteSnippet(Session session, Snippet snippet) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canCreateSnippet(Session session, Category category) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canComment(Session session) {
			return session.isLoggedIn();
		}

		@Override
		public boolean canDeleteComment(Session session, Comment comment) {
			return session.isLoggedIn();
		}
	};

	/**
	 * Generate the access policy for a session
	 * 
	 * @param session
	 *            for that the access policy should be generated
	 * @return the generated access policy
	 */
	public static IAccessPolicy getAccessPolicty(Session session) {
		if (session == null)
			return guestAccessPolicy;
		if (!session.isLoggedIn())
			return guestAccessPolicy;

		User user = session.getUser();
		if (user.isModerator())
			return moderatorAccessPolicy;

		return userAccessPolicy;
	}

	/**
	 * @return the default guest access policy
	 */
	public static IAccessPolicy getGuestAccessPolicty() {
		return guestAccessPolicy;
	}

	/**
	 * Checks if a password is secure enought
	 * 
	 * @param password
	 *            to be checked
	 * @return true if secured enought false if considered harmfull
	 */
	public static boolean acceptPassword(String password) {
		if (password == null)
			return false;
		return password.length() > 3;
	}

	/**
	 * Checks a given message to the administrators for spam
	 * 
	 * @param message
	 *            to be checked
	 * @param email
	 *            to be checked
	 * @return true if spam
	 */
	public static boolean checkSpam(String message, String email) {
		if (message.length() < 10)
			return true;
		return false;
	}
}
