/**
 * File: PersistenceHelper.java
 * Date: 01.05.2012
 */
package org.smartsnip.core;

import java.util.Date;
import java.util.List;

/**
 * @author littlelion
 *
 */
public class PersistenceHelper {

	/**
	 * 
	 */
	protected PersistenceHelper() {
		super();
		// a helper class - no instances in this package needed
	}


	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createSnippet(org.smartsnip.core.User, java.lang.String, org.smartsnip.core.Category, java.util.List, java.util.List, org.smartsnip.core.Code, java.lang.String, int)
	 */
	protected Snippet createSnippet(User owner, String name, Category category, 
			List<Tag> tags, List<Comment> comments, Code code, String license, int viewcount) {
		return null;
	}

	
	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createTag(java.lang.String)
	 */
	protected Tag createTag(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createNotification(java.lang.String, java.lang.Boolean, java.lang.String, java.lang.String)
	 */
	protected Notification createNotification(String message, Boolean read,
			String time, String source) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createComment(org.smartsnip.core.User, org.smartsnip.core.Snippet, java.lang.String, long, java.util.Date, int, int)
	 */
	protected Comment createComment(User owner, Snippet snippet, String message,
			long id, Date time, int posVotes, int negVotes) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createCode(java.lang.String, java.lang.String, org.smartsnip.core.Snippet, int)
	 */
	protected Code createCode(String code, String language, Snippet snippet,
			int version) {
		// TODO Auto-generated method stub
		return null;
	}


	protected User createUser(String username, String realName, String email,
			User.UserState state, List<Snippet> favourites) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Category createCategory(String name, String description, long parentId) {
		// TODO Auto-generated method stub
		return null;		
	}
}
