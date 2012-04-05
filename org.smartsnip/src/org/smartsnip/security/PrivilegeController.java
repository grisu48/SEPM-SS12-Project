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
			return true;
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
		
		// TODO Write me!
		return null;
	}

	/**
	 * @return the default guest access policy
	 */
	public static IAccessPolicy getGuestAccessPolicty() {
		return guestAccessPolicy;
	}
}
