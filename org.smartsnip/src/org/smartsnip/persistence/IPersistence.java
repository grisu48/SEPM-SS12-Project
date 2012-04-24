/**
 * File: IPersistence.java
 * Date: 22.04.2012
 */
package org.smartsnip.persistence;

import java.io.IOException;
import java.util.List;

import org.smartsnip.core.Category;
import org.smartsnip.core.Code;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Notification;
import org.smartsnip.core.Pair;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;

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
	public void writeUser(User user, int mode) throws IOException;

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
	public void writeUser(List<User> users, int mode) throws IOException;

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
	public void writeSnippet(Snippet snippet, int mode) throws IOException;

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
	public void writeSnippet(List<Snippet> snippets, int mode) throws IOException;

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
	public void writeComment(Comment comment, int mode) throws IOException;

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
	public void writeComment(List<Comment> comments, int mode) throws IOException;

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
	public void writeTag(Tag tag, int mode) throws IOException;

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
	public void writeTag(List<Tag> tags, int mode) throws IOException;

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
	public void writeNotification(Notification notification, int mode) throws IOException;

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
	public void writeNotification(List<Notification> notifications, int mode) throws IOException;

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
	public void writeCode(Code code, int mode) throws IOException;

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
	public void writeCode(List<Code> codes, int mode) throws IOException;

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
	public void writeCategory(Category category, int mode) throws IOException;

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
	public void writeCategory(List<Category> categories, int mode) throws IOException;

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
	public void writeLanguage(String language, int mode) throws IOException;

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
	public void writeRating(Integer rating, Snippet snippet, User user, int mode) throws IOException;

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
	public int writeVote(Integer vote, Comment comment, User user, int mode) throws IOException;

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
	public int votePositive(User user, Comment comment, int mode) throws IOException;

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
	public int voteNegative(User user, Comment comment, int mode) throws IOException;

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
	public int unVote(User user, Comment comment, int mode) throws IOException;

	/**
	 * Sets the favourite-state of the snippet.
	 * 
	 * @param snippet
	 *            the snippet to change state
	 * @param user
	 *            the owner of the favorite list
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @return true if the state has changed
	 * @throws Exception
	 */
	public boolean addFavourite(Snippet snippet, User user, int mode) throws IOException;

	/**
	 * Clears the favourite-state of the snippet.
	 * 
	 * @param snippet
	 *            the snippet to change state
	 * @param user
	 *            the owner of the favorite list
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @return true if the state has changed
	 * @throws Exception
	 */
	public boolean removeFavourite(Snippet snippet, User user, int mode) throws IOException;

	/**
	 * get a user by his nickname
	 * 
	 * @param nick
	 * @return
	 * @throws Exception
	 */
	public User getUser(String nick) throws IOException;

	public User getUserByEmail(String email) throws IOException;

	public List<User> findUser(String realName) throws IOException;

	public List<Snippet> getUserSnippets(User owner) throws IOException;

	public List<Snippet> getFavorited(User owner) throws IOException;

	public List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException;

	public List<Snippet> getSnippets(Category category) throws IOException;

	public List<Comment> getComments(Snippet snippet) throws IOException;

	public List<Tag> getTags(Snippet snippet) throws IOException;

	public List<Tag> getAllTags() throws IOException;

	public List<Notification> getNotifications(User user, boolean unreadOnly) throws IOException;

	public List<Code> getCodes(Snippet snippet) throws IOException;

	public Category getCategory(Snippet snippet) throws IOException;

	public Category getParentCategory(Category category) throws IOException;

	/**
	 * 
	 * @param category
	 * @return the child categories in first order
	 * @throws Exception
	 */
	public List<Category> getSubcategories(Category category) throws IOException;

	public List<String> getAllLanguages() throws IOException;

	public List<Pair<User, Integer>> getRatings(Snippet snippet) throws IOException;

	public List<Pair<User, Integer>> getVotes(Comment comment) throws IOException;

	public List<Snippet> search(String searchString) throws IOException;

}