/**
 * File: BHPersistenceHelper.java
 * Date: 07.05.2012
 */
package org.smartsnip.persistence.blackhole;

import java.util.Date;
import java.util.List;

import org.smartsnip.core.Category;
import org.smartsnip.core.Code;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Notification;
import org.smartsnip.core.PersistenceHelper;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.core.User.UserState;

/**
 * @author littlelion
 *
 */
public class BHPersistenceHelper extends PersistenceHelper {

	/**
	 * Creates a helper object
	 * @see PersistenceHelper
	 */
	BHPersistenceHelper() {
		super();
		// Auto-generated constructor stub
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createSnippet(java.lang.Long, org.smartsnip.core.User, java.lang.String, java.lang.String, org.smartsnip.core.Category, java.util.List, java.util.List, org.smartsnip.core.Code, java.lang.String, int)
	 */
	@Override
	protected Snippet createSnippet(Long id, User owner, String name,
			String description, Category category, List<Tag> tags,
			List<Comment> comments, Code code, String license, int viewcount) {
		// Auto-generated method stub
		return super.createSnippet(id, owner, name, description, category, tags,
				comments, code, license, viewcount);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createTag(java.lang.String)
	 */
	@Override
	protected Tag createTag(String tag) {
		// Auto-generated method stub
		return super.createTag(tag);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createNotification(java.lang.Long, org.smartsnip.core.User, java.lang.String, java.lang.Boolean, java.lang.String, java.lang.String, org.smartsnip.core.Snippet)
	 */
	@Override
	protected Notification createNotification(Long id, User owner,
			String message, Boolean read, String time, String source,
			Snippet target) {
		// Auto-generated method stub
		return super.createNotification(id, owner, message, read, time, source, target);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createComment(org.smartsnip.core.User, org.smartsnip.core.Snippet, java.lang.String, long, java.util.Date, int, int)
	 */
	@Override
	protected Comment createComment(User owner, Snippet snippet,
			String message, long id, Date time, int posVotes, int negVotes) {
		// Auto-generated method stub
		return super.createComment(owner, snippet, message, id, time, posVotes,
				negVotes);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createCode(java.lang.Long, java.lang.String, java.lang.String, org.smartsnip.core.Snippet, int)
	 */
	@Override
	protected Code createCode(Long id, String code, String language,
			Snippet snippet, int version) {
		// Auto-generated method stub
		return super.createCode(id, code, language, snippet, version);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createUser(java.lang.String, java.lang.String, java.lang.String, org.smartsnip.core.User.UserState, java.util.List)
	 */
	@Override
	protected User createUser(String username, String realName, String email,
			UserState state, List<Snippet> favourites) {
		// Auto-generated method stub
		return super.createUser(username, realName, email, state, favourites);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createCategory(java.lang.String, java.lang.String, org.smartsnip.core.Category)
	 */
	@Override
	protected Category createCategory(String name, String description,
			Category parent) {
		// Auto-generated method stub
		return super.createCategory(name, description, parent);
	}
}
