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
	 * Factory method for the persistence layer
	 * 
	 * @param id
	 * @param owner
	 * @param name
	 * @param description
	 * @param category
	 * @param tags
	 * @param comments
	 * @param code
	 * @param license
	 * @param viewcount
	 * @return an initialized Snippet object
	 */
	protected Snippet createSnippet(Long id, User owner, String name,
			String description, Category category, List<Tag> tags,
			List<Long> comments, Code code, String license, int viewcount) {
		return new Snippet(owner.getUsername(), name, description, id,
				category.getName(), license, tags, comments, viewcount);
	}

	/**
	 * Factory method for the persistence layer
	 * 
	 * @param tag
	 * @return an initialized Tag object
	 */
	protected Tag createTag(String tag) {
		return new Tag(tag);
	}

	/**
	 * Factory method for the persistence layer
	 * 
	 * @param id
	 * @param owner
	 * @param message
	 * @param read
	 * @param time
	 * @param source
	 * @param target
	 * @return an initialized Notification object
	 */
	protected Notification createNotification(Long id, User owner,
			String message, Boolean read, String time, String source,
			Snippet target) {
		return new Notification(id, owner, message, read, time, source, target);
	}

	/**
	 * Factory method for the persistence layer
	 * 
	 * @param owner
	 * @param snippet
	 * @param message
	 * @param id
	 * @param time
	 * @param posVotes
	 * @param negVotes
	 * @return an initialized Comment object
	 */
	protected Comment createComment(User owner, Snippet snippet,
			String message, long id, Date time, int posVotes, int negVotes) {
		return new Comment(owner.getUsername(), snippet.getHashId(), message,
				id, time, posVotes, negVotes);
	}

	/**
	 * Factory method for the persistence layer
	 * 
	 * @param id
	 * @param code
	 * @param language
	 * @param snippet
	 * @param version
	 * @return an initialized Code object
	 */
	protected Code createCode(Long id, String code, String language,
			Snippet snippet, int version) {
		return Code.createCodeDB(code, language, snippet, id, version);
	}

	/**
	 * Factory method for the persistence layer
	 * 
	 * @param username
	 * @param realName
	 * @param email
	 * @param state
	 * @param favourites
	 * @return an initialized User object
	 */
	protected User createUser(String username, String realName, String email,
			User.UserState state) {

		return new User(username, realName, email, state);
	}

	/**
	 * Factory method for the persistence layer
	 * 
	 * @param name
	 * @param description
	 * @param parent
	 * @return an initialized Category object
	 */
	protected Category createCategory(String name, String description,
			String parent) {
		return new Category(name, description, parent);
	}

	/**
	 * evaluates the flags
	 * 
	 * @param flags
	 *            the value containing some flags
	 * @param flag
	 *            the flag to test
	 * @return true if the flag is present
	 */
	public static boolean hasFlag(int flags, int flag) {
		return (flags & flag) == flag;
	}
}
