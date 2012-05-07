/**
 * File: PersistenceHelper.java
 * Date: 01.05.2012
 */
package org.smartsnip.core;

import java.util.Date;
import java.util.List;

/**
 * A helper class to pass on the needed core objects to the persistence package.
 * 
 * @author littlelion
 * 
 */
public class PersistenceHelper {

	/**
	 * A helper class to pass on the needed core objects to the persistence
	 * package. No instances needed here, but in the persistence package.
	 */
	protected PersistenceHelper() {
		super();
	}

	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createSnippet(org.smartsnip.core.User,
	 *      java.lang.String, org.smartsnip.core.Category, java.util.List,
	 *      java.util.List, org.smartsnip.core.Code, java.lang.String, int)
	 */
	protected Snippet createSnippet(Long id, User owner, String name, String description, Category category, List<Tag> tags,
			List<Comment> comments, Code code, String license, int viewcount) {
		return new Snippet(owner, name, description, id, code, category, license, tags, comments, viewcount);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createTag(java.lang.String)
	 */
	protected Tag createTag(String tag) {
		return new Tag(tag);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createNotification(java.lang.String,
	 *      java.lang.Boolean, java.lang.String, java.lang.String)
	 */
	protected Notification createNotification(String message, Boolean read, String time, String source) {
		// TODO getters missing in Notification class
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createComment(org.smartsnip.core.User,
	 *      org.smartsnip.core.Snippet, java.lang.String, long, java.util.Date,
	 *      int, int)
	 */
	protected Comment createComment(User owner, Snippet snippet, String message, long id, Date time, int posVotes,
			int negVotes) {
		Comment result = new Comment(owner, snippet, message, id, time, posVotes, negVotes);
		return result;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistenceFactory#createCode(java.lang.String,
	 *      java.lang.String, org.smartsnip.core.Snippet, int)
	 */
	protected Code createCode(Long id, String code, String language, Snippet snippet, int version) {
		Code result = Code.createCodeDB(code, language, snippet, id);
		// TODO add unimplemented method version
		return result;
	}

	protected User createUser(String username, String realName, String email, User.UserState state,
			List<Snippet> favourites) {
		User result = new User(username, realName, email, state, favourites);
		return result;
	}

	protected Category createCategory(String name, String description, Category parent) {
		return new Category(name, description, parent);
	}
}
