package org.smartsnip.security;

import org.smartsnip.core.*;

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
			if (!session.isLoggedIn()) return false;
			return session.isLoggedInUser(snippet.getOwner());
		}

		@Override
		public boolean canDeleteSnippet(Session session, Snippet snippet) {
			if (!session.isLoggedIn()) return false;
			return session.isLoggedInUser(snippet.getOwner());
		}

		@Override
		public boolean canCreateSnippet(Session session, Category category) {
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
		if (session == null) return guestAccessPolicy;
		if (!session.isLoggedIn()) return guestAccessPolicy;

		return userAccessPolicy;
	}

	/**
	 * @return the default guest access policy
	 */
	public static IAccessPolicy getGuestAccessPolicty() {
		return guestAccessPolicy;
	}
}
