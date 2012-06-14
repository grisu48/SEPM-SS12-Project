package org.smartsnip.server;

import org.smartsnip.core.User;
import org.smartsnip.shared.IModerator;

public class IModeratorImpl extends GWTSessionServlet implements IModerator {

	/** Serialisation ID */
	private static final long serialVersionUID = -3154461839994229670L;

	@Override
	public boolean isModerator() {
		Session session = getSession();
		if (!session.isLoggedIn()) return false;
		User user = session.getUser();
		if (user == null) return false;
		return user.canModerate();
	}

}
