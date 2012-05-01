/**
 * File: PersistenceHelper.java
 * Date: 01.05.2012
 */
package org.smartsnip.persistence;

import java.util.Date;
import java.util.List;

import org.smartsnip.core.*;

/**
 * @author littlelion
 *
 */
public class PersistenceHelper extends org.smartsnip.core.PersistenceHelper {

	
	/**
	 * 
	 */
	PersistenceHelper() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createSnippet(org.smartsnip.core.User, java.lang.String, org.smartsnip.core.Category, java.util.List, java.util.List, org.smartsnip.core.Code, java.lang.String, int)
	 */
	@Override
	protected Snippet createSnippet(User owner, String name, Category category,
			List<Tag> tags, List<Comment> comments, Code code, String license,
			int viewcount) {
		// TODO Auto-generated method stub
		return super.createSnippet(owner, name, category, tags, comments, code,
				license, viewcount);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createTag(java.lang.String)
	 */
	@Override
	protected Tag createTag(String tag) {
		// TODO Auto-generated method stub
		return super.createTag(tag);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createNotification(java.lang.String, java.lang.Boolean, java.lang.String, java.lang.String)
	 */
	@Override
	protected Notification createNotification(String message, Boolean read,
			String time, String source) {
		// TODO Auto-generated method stub
		return super.createNotification(message, read, time, source);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createComment(org.smartsnip.core.User, org.smartsnip.core.Snippet, java.lang.String, long, java.util.Date, int, int)
	 */
	@Override
	protected Comment createComment(User owner, Snippet snippet,
			String message, long id, Date time, int posVotes, int negVotes) {
		// TODO Auto-generated method stub
		return super.createComment(owner, snippet, message, id, time, posVotes,
				negVotes);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createCode(java.lang.String, java.lang.String, org.smartsnip.core.Snippet, int)
	 */
	@Override
	protected Code createCode(String code, String language, Snippet snippet,
			int version) {
		// TODO Auto-generated method stub
		return super.createCode(code, language, snippet, version);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createUser(java.lang.String, java.lang.String, java.lang.String, org.smartsnip.core.User.UserState, java.util.List)
	 */
	@Override
	protected User createUser(String username, String realName, String email,
			User.UserState state, List<Snippet> favourites) {
		// TODO Auto-generated method stub
		return super.createUser(username, realName, email, state, favourites);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createCategory(java.lang.String, java.lang.String, long)
	 */
	@Override
	protected Category createCategory(String name, String description,
			long parentId) {
		// TODO Auto-generated method stub
		return super.createCategory(name, description, parentId);
	}

}
