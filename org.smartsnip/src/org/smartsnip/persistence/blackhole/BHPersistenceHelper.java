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
import org.smartsnip.core.File;
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
	 * 
	 * @see PersistenceHelper
	 */
	BHPersistenceHelper() {
		super();
		// Auto-generated constructor stub
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createSnippet(java.lang.Long,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.util.List, java.util.List, java.lang.String,
	 *      int, java.lang.Float)
	 */
	@Override
	protected Snippet createSnippet(Long id, String owner, String name,
			String description, String category, List<Tag> tags,
			List<Long> comments, String license, int viewcount,
			Float ratingAverage) {
		// Auto-generated method stub
		return super.createSnippet(id, owner, name, description, category,
				tags, comments, license, viewcount, ratingAverage);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#setCodeOfSnippet(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.Code)
	 */
	@Override
	protected void setCodeOfSnippet(Snippet snippet, Code code) {
		// Auto-generated method stub
		super.setCodeOfSnippet(snippet, code);
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
	 * @see org.smartsnip.core.PersistenceHelper#createNotification(java.lang.Long,
	 *      org.smartsnip.core.User, java.lang.String, java.lang.Boolean,
	 *      java.lang.String, java.lang.String, org.smartsnip.core.Snippet)
	 */
	@Override
	protected Notification createNotification(Long id, User owner,
			String message, Boolean read, String time, String source,
			Snippet target) {
		// Auto-generated method stub
		return super.createNotification(id, owner, message, read, time, source,
				target);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createComment(java.lang.String,
	 *      java.lang.Long, java.lang.String, java.lang.Long, java.util.Date,
	 *      int, int)
	 */
	@Override
	protected Comment createComment(String owner, Long snippetId,
			String message, Long id, Date time, int posVotes, int negVotes) {
		// Auto-generated method stub
		return super.createComment(owner, snippetId, message, id, time,
				posVotes, negVotes);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createCode(java.lang.Long,
	 *      java.lang.String, java.lang.String, org.smartsnip.core.Snippet, int)
	 */
	@Override
	protected Code createCode(Long id, String code, String language,
			Snippet snippet, int version) {
		// Auto-generated method stub
		return super.createCode(id, code, language, snippet, version);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createCodeFile(java.lang.String,
	 *      java.lang.Byte[])
	 */
	@Override
	protected File createCodeFile(String name, Byte[] content) {
		// Auto-generated method stub
		return super.createCodeFile(name, content);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createUser(java.lang.String,
	 *      java.lang.String, java.lang.String,
	 *      org.smartsnip.core.User.UserState)
	 */
	@Override
	protected User createUser(String username, String realName, String email,
			UserState state) {
		// Auto-generated method stub
		return super.createUser(username, realName, email, state);
	}

	/**
	 * @see org.smartsnip.core.PersistenceHelper#createCategory(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	protected Category createCategory(String name, String description,
			String parent) {
		// Auto-generated method stub
		return super.createCategory(name, description, parent);
	}
}
