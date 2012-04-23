/**
 * File: IPersistence.java
 * Date: 22.04.2012
 */
package org.smartsnip.persistence;

import java.util.List;
import org.smartsnip.core.*;

/**
 * Wrapper interface which contains all methods to persist or load data from the
 * underlying database.
 * 
 * @author littlelion
 * 
 */
public interface IPersistence {

	/**
	 * <p>
	 * constant for the argument 'mode' of the writeXxx() methods: use the
	 * default conditions. In common the default behavior is:
	 * </p>
	 * <blockquote>
	 * <nl>
	 * <li>to create a new entry if not already present</li>
	 * <li>to update an existing entry</li>
	 * <li>to keep database entries (don't use DELETE statements)</li>
	 * </nl>
	 * </blockquote>
	 * <p>
	 * See the description of the corresponding methods for possible deviating
	 * behavior.
	 * </p>
	 */
	public static int DB_DEFAULT = 0;

	/**
	 * constant for the argument 'mode' of the writeXxx() methods: reject to
	 * write if the element already exists in the database. This constant
	 * overrides DB_UPDATE_ONLY.
	 */
	public static int DB_NEW_ONLY = 1;

	/**
	 * constant for the argument 'mode' of the writeXxx() methods: reject the
	 * request if the element to update doesn't exist. This constant has no
	 * effect if DB_NEW_ONLY is set.
	 */
	public static int DB_UPDATE_ONLY = 2;

	/**
	 * constant for the argument 'mode' of the writeXxx() methods: don't delete
	 * contents of the database. This constant overrides DB_FORCE_DELETE.
	 */
	public static int DB_NO_DELETE = 4;

	/**
	 * constant for the argument 'mode' of the writeXxx() methods: don't delete
	 * contents of the database. This constant has no effect if DB_NEW_ONLY is
	 * set.
	 */
	public static int DB_FORCE_DELETE = 8;

	// TODO: specialize Exception

	/**
	 * Persist a single User-dataset.
	 * 
	 * @param user
	 *            the user to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeUser(User user, int mode) throws Exception;

	/**
	 * Persist multiple User-datasets.
	 * 
	 * @param users
	 *            the list of users to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeUser(List<User> users, int mode) throws Exception;

	/**
	 * Persist a single Snippet-dataset.
	 * 
	 * @param snippet
	 *            the snippet to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeSnippet(Snippet snippet, int mode) throws Exception;

	/**
	 * Persist multiple Snippet-datasets.
	 * 
	 * @param snippets
	 *            the list of snippets to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeSnippet(List<Snippet> snippets, int mode) throws Exception;

	/**
	 * Persist a single Comment-dataset.
	 * 
	 * @param comment
	 *            the comment to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeComment(Comment comment, int mode) throws Exception;

	/**
	 * Persist multiple Comment-datasets.
	 * 
	 * @param comments
	 *            the list of comments to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeComment(List<Comment> comments, int mode) throws Exception;

	/**
	 * Persist a single Tag-dataset.
	 * 
	 * @param tag
	 *            the tag to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeTag(Tag tag, int mode) throws Exception;

	/**
	 * Persist multiple Tag-datasets.
	 * 
	 * @param tags
	 *            the list of tags to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeTag(List<Tag> tags, int mode) throws Exception;

	/**
	 * Persist a single Notification-dataset.
	 * 
	 * @param notification
	 *            the notification to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeNotification(Notification notification, int mode)
			throws Exception;

	/**
	 * Persist multiple Notification-datasets.
	 * 
	 * @param notifications
	 *            the list of notifications to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeNotification(List<Notification> notifications, int mode)
			throws Exception;

	/**
	 * Persist a single Code-dataset.
	 * 
	 * @param code
	 *            the code to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeCode(Code code, int mode) throws Exception;

	/**
	 * Persist multiple Code-datasets.
	 * 
	 * @param codes
	 *            the list of code fragments to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeCode(List<Code> codes, int mode) throws Exception;

	/**
	 * Persist a single Category-dataset.
	 * 
	 * @param category
	 *            the category to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeCategory(Category category, int mode) throws Exception;

	/**
	 * Persist multiple Category-datasets.
	 * 
	 * @param categories
	 *            the list of categories to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeCategory(List<Category> categories, int mode)
			throws Exception;

	// XXX Language not implemented yet.
	/**
	 * Persist a language.
	 * 
	 * @param language
	 *            the language to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeLanguage(Language language, int mode) throws Exception;

	// XXX Rating not implemented yet.
	/**
	 * Persist a rating. This operation updates an existing rating if the user
	 * has rated already for the given snippet. This operation will be rejected
	 * in DB_NEW_ONLY mode if an update on an existing rating should be
	 * performed.
	 * 
	 * @param rating
	 *            the rating to write
	 * 
	 * @param snippet
	 *            the snippet the rating belongs to
	 * @param user
	 *            the user who rated
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws Exception
	 */
	public void writeRating(Rating rating, Snippet snippet, User user, int mode)
			throws Exception;

	// XXX Vote not implemented yet.
	/**
	 * Persist a vote. This operation updates an existing vote if the user has
	 * voted already for the given comment. This operation will be rejected in
	 * DB_NEW_ONLY mode if an update on an existing vote should be performed.
	 * 
	 * @param vote
	 *            the vote to write
	 * 
	 * @param comment
	 *            the comment to vote for
	 * @param user
	 *            the user who voted
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @return the actual value of Comment.vote_sum
	 * @throws Exception
	 */
	public int writeVote(Vote vote, Comment comment, User user, int mode)
			throws Exception;

	/**
	 * Persist a positive vote. This operation updates an existing vote if the
	 * user has voted already for the given comment. This operation will be
	 * rejected in DB_NEW_ONLY mode if an update on an existing vote should be
	 * performed.
	 * 
	 * @param user
	 *            the user who voted
	 * @param comment
	 *            the comment to vote for
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @return the actual value of Comment.vote_sum
	 * @throws Exception
	 */
	public int votePositive(User user, Comment comment, int mode)
			throws Exception;

	/**
	 * Persist a negative vote. This operation updates an existing vote if the
	 * user has voted already for the given comment. This operation will be
	 * rejected in DB_NEW_ONLY mode if an update on an existing vote should be
	 * performed.
	 * 
	 * @param user
	 *            the user who voted
	 * @param comment
	 *            the comment to vote for
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @return the actual value of Comment.vote_sum
	 * @throws Exception
	 */
	public int voteNegative(User user, Comment comment, int mode)
			throws Exception;

	/**
	 * Remove a vote. This operation updates an existing vote to 'none' in
	 * DB_NO_DELETE mode which is currently the default behavior. In
	 * DB_FORCE_DELETE mode the given database entry is deleted.
	 * 
	 * @param user
	 *            the user who voted
	 * @param comment
	 *            the comment to vote for
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @return the actual value of Comment.vote_sum
	 * @throws Exception
	 */
	public int unVote(User user, Comment comment, int mode) throws Exception;

	/**
	 * Toggles the actual favourite-state of the snippet.
	 * 
	 * @param snippet
	 *            the snippet to change state
	 * @throws Exception
	 */
	public void toggleFavouriteState(Snippet snippet) throws Exception;

	/**
	 * Sets the favourite-state of the snippet.
	 * 
	 * @param snippet
	 *            the snippet to change state
	 * @return true if the state has changed
	 * @throws Exception
	 */
	public boolean setFavouriteState(Snippet snippet) throws Exception;

	/**
	 * Clears the favourite-state of the snippet.
	 * 
	 * @param snippet
	 *            the snippet to change state
	 * @return true if the state has changed
	 * @throws Exception
	 */
	public boolean clearFavouriteState(Snippet snippet) throws Exception;

	// TODO comments
	public User getUser(String nick) throws Exception;

	public List<User> getUser(String first, String last) throws Exception;

	public boolean loginUser(String nick, String password) throws Exception;

	public List<Snippet> getSnippets(User owner) throws Exception;

	public List<Snippet> getSnippets(User owner, Boolean favouritesOnly)
			throws Exception;

	public List<Snippet> getSnippets(List<Tag> matchingTags) throws Exception;

	public List<Snippet> getSnippets(Category category) throws Exception;

	public List<Comment> getComments(Snippet snippet) throws Exception;

	public List<Tag> getTags(Snippet snippet) throws Exception;

	public List<Tag> getAllTags() throws Exception;

	public List<Notification> getNotifications(User user, boolean viewedOnly)
			throws Exception;

	public List<Code> getCode(Snippet snippet) throws Exception;

	public Category getCategory(Snippet snippet) throws Exception;

	public Category getParentCategory(Category category) throws Exception;

	public List<Category> getAllSubcategories(Category category)
			throws Exception;

	public Language getLanguage(Snippet snippet) throws Exception;

	public List<Language> getAllLanguages() throws Exception;

	public List<Rating> getRatings(Snippet snippet) throws Exception;

	public List<Vote> getVotes(Comment comment) throws Exception;
	
	public List<Snippet> search (Search search) throws Exception;
}
