/**
 * File: IPersistence.java
 * Date: 22.04.2012
 */
package org.smartsnip.persistence;

import java.io.IOException;
import java.util.List;

import org.smartsnip.*;
import org.smartsnip.shared.Pair;

/**
 * Interface which contains all methods to persist or load data from the
 * underlying database.
 * <p>
 * Implementing classes e. g. {@code MyPersistence} should provide an exclusive
 * constructor of the shape:
 * 
 * <pre>
 * MyPersistence() throws IllegalAccessException {
 * 	super();
 * 	if (Reflection.getCallerClass(2) == null
 * 			|| Reflection.getCallerClass(2) != PersistenceFactory.class) {
 * 		throw new IllegalAccessException(
 * 				&quot;Singleton pattern: caller must be PersistenceFactory class.&quot;);
 * 	}
 * }
 * </pre>
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

	/**
	 * Persist a single User-dataset.
	 * 
	 * @param user
	 *            the user to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
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
	 * @throws IOException
	 */
	public void writeUser(List<User> users, int mode) throws IOException;

	/**
	 * Persist a new password into the database
	 * 
	 * @param user
	 *            the owner of the password
	 * @param password
	 *            to set for the user
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
	 */
	public void writePassword(User user, String password, int mode)
			throws IOException;

	/**
	 * Persist a single Snippet-dataset.
	 * 
	 * @param snippet
	 *            the snippet to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
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
	 * @throws IOException
	 */
	public void writeSnippet(List<Snippet> snippets, int mode)
			throws IOException;

	/**
	 * Persist a single Comment-dataset.
	 * 
	 * @param comment
	 *            the comment to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
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
	 * @throws IOException
	 */
	public void writeComment(List<Comment> comments, int mode)
			throws IOException;

	/**
	 * Persist a single Tag-dataset.
	 * 
	 * @param tag
	 *            the tag to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
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
	 * @throws IOException
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
	 * @throws IOException
	 */
	public void writeNotification(Notification notification, int mode)
			throws IOException;

	/**
	 * Persist multiple Notification-datasets.
	 * 
	 * @param notifications
	 *            the list of notifications to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
	 */
	public void writeNotification(List<Notification> notifications, int mode)
			throws IOException;

	/**
	 * Persist a single Code-dataset.
	 * 
	 * @param code
	 *            the code to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
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
	 * @throws IOException
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
	 * @throws IOException
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
	 * @throws IOException
	 */
	public void writeCategory(List<Category> categories, int mode)
			throws IOException;

	/**
	 * Persist a language.
	 * 
	 * @param language
	 *            the language to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
	 */
	public void writeLanguage(String language, int mode) throws IOException;

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
	 * @throws IOException
	 */
	public void writeRating(Integer rating, Snippet snippet, User user, int mode)
			throws IOException;

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
	 * @throws IOException
	 */
	public void writeVote(Integer vote, Comment comment, User user, int mode)
			throws IOException;

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
	 * @throws IOException
	 */
	public void votePositive(User user, Comment comment, int mode)
			throws IOException;

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
	 * @throws IOException
	 */
	public void voteNegative(User user, Comment comment, int mode)
			throws IOException;

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
	 * @throws IOException
	 */
	public void unVote(User user, Comment comment, int mode) throws IOException;

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
	 * @throws IOException
	 */
	public void addFavourite(Snippet snippet, User user, int mode)
			throws IOException;

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
	 * @throws IOException
	 */
	public void removeFavourite(Snippet snippet, User user, int mode)
			throws IOException;

	/**
	 * get a user by his nickname
	 * 
	 * @param nick
	 *            the nickname of the user as key
	 * @return the user object
	 * @throws IOException
	 */
	public User getUser(String nick) throws IOException;

	/**
	 * get a user by his email address
	 * 
	 * @param email
	 *            the email address
	 * @return the user object
	 * @throws IOException
	 */
	public User getUserByEmail(String email) throws IOException;

	/**
	 * get the password string related to the user
	 * 
	 * @param user
	 *            the user
	 * @return the password string
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 *             if the method is not supported by the persistence framework.
	 *             If this happens, use {@link #verifyPassword(User, String)}
	 *             instant, that has to be implemented
	 */
	public String getPassword(User user) throws IOException,
			UnsupportedOperationException;

	/**
	 * verifies the password of the given user
	 * 
	 * @param user
	 * @param password
	 * @return true if the password is correct
	 * @throws IOException
	 */
	public boolean verifyPassword(User user, String password)
			throws IOException;

	/**
	 * find all users with matching names
	 * 
	 * @param realName
	 *            a string containing all parts of the searched user's name. The
	 *            word order is unimportant.
	 * @return the users where all given words match with the name entry
	 * @throws IOException
	 */
	public List<User> findUser(String realName) throws IOException;

	/**
	 * get all snippets where the user is the owner
	 * 
	 * @param owner
	 *            a user owning the snippets
	 * @return all matching snippets
	 * @throws IOException
	 */
	public List<Snippet> getUserSnippets(User owner) throws IOException;

	/**
	 * get all snippets which are tagged with the favourite attribute of the
	 * given user
	 * 
	 * @param user
	 *            the user who attached the favourite tags
	 * @return all matching snippets
	 * @throws IOException
	 */
	public List<Snippet> getFavorited(User user) throws IOException;

	/**
	 * get all snippets which contain all of the given tags in their tag-list
	 * 
	 * @param matchingTags
	 *            a list of tags
	 * @return all matching snippets
	 * @throws IOException
	 */
	public List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException;

	/**
	 * get all snippets of a given category
	 * 
	 * @param category
	 *            the category
	 * @return all matching snippets
	 * @throws IOException
	 */
	public List<Snippet> getSnippets(Category category) throws IOException;

	/**
	 * get all comments attached to the given snippet
	 * 
	 * @param snippet
	 * @return all matching comments
	 * @throws IOException
	 */
	public List<Comment> getComments(Snippet snippet) throws IOException;

	/**
	 * get all tags attached to the given snippet
	 * 
	 * @param snippet
	 * @return the list of tags
	 * @throws IOException
	 */
	public List<Tag> getTags(Snippet snippet) throws IOException;

	/**
	 * get all tags
	 * 
	 * @return a list of all tags
	 * @throws IOException
	 */
	public List<Tag> getAllTags() throws IOException;

	/**
	 * get all notifications belonging to the given user
	 * 
	 * @param user
	 *            the user to notify
	 * @param unreadOnly
	 *            return only the notifications which are viewed the first time
	 *            now
	 * @return all (new) notifications
	 * @throws IOException
	 */
	public List<Notification> getNotifications(User user, boolean unreadOnly)
			throws IOException;

	/**
	 * get all code fragments of a given snippet
	 * 
	 * @param snippet
	 * @return all code fragments
	 * @throws IOException
	 */
	public List<Code> getCodes(Snippet snippet) throws IOException;

	/**
	 * get the category the snippet belongs to
	 * 
	 * @param snippet
	 * @return the category
	 * @throws IOException
	 */
	public Category getCategory(Snippet snippet) throws IOException;

	/**
	 * get the immediate parent of a category
	 * 
	 * @param category
	 * @return the parent category or null if the given category is a root
	 * @throws IOException
	 */
	public Category getParentCategory(Category category) throws IOException;

	/**
	 * get the immediate subcategories
	 * 
	 * @param category
	 * @return the direct child categories in first order. If the given category
	 *         is a leaf an empty list is returned.
	 * @throws IOException
	 */
	public List<Category> getSubcategories(Category category)
			throws IOException;

	/**
	 * get all available programming-languages
	 * 
	 * @return a list of all languages
	 * @throws IOException
	 */
	public List<String> getAllLanguages() throws IOException;

	/**
	 * get all ratings belonging to the given snippet
	 * 
	 * @param snippet
	 * @return a list of pairs of user objects and ratings as integer
	 * @throws IOException
	 */
	public List<Pair<User, Integer>> getRatings(Snippet snippet)
			throws IOException;

	/**
	 * get the average rating belonging to the given snippet
	 * 
	 * @param snippet
	 * @return the average of all ratings
	 * @throws IOException
	 */
	public Float getAverageRating(Snippet snippet)
			throws IOException;

	/**
	 * Creates a pair with the votes for a comment.
	 * <p>
	 * The first parameter gives the positive votes, the seconds parameter of
	 * the pair gives the number of negative votes.
	 * 
	 * @param comment
	 * @return a pair of two integers
	 * @throws IOException
	 */
	public Pair<Integer, Integer> getVotes(Comment comment) throws IOException;

	/**
	 * search for the arguments of the given search-string.
	 * 
	 * @param searchString
	 *            the string to search for
	 * @param min
	 *            the index of the first result, null or 0 if no lower limit is
	 *            wanted
	 * @param max
	 *            the index of the last result, null if no upper limit is wanted
	 * @return a list of snippets which contain the given arguments
	 * @throws IOException
	 */
	public List<Snippet> search(String searchString, int min, int max)
			throws IOException;

	/**
	 * get the number of users which are currently in the database
	 * 
	 * @return the count
	 * @throws IOException
	 */
	public int getUserCount() throws IOException;

	/**
	 * get the number of categories which are currently in the database
	 * 
	 * @return the count
	 * @throws IOException
	 */
	public int getCategoryCount() throws IOException;

	/**
	 * get the number of snippets which are currently in the database
	 * 
	 * @return the count
	 * @throws IOException
	 */
	public int getSnippetsCount() throws IOException;

	/**
	 * get the number of tags which are currently in the database
	 * 
	 * @return the count
	 * @throws IOException
	 */
	public int getTagsCount() throws IOException;

	public List<String> getCategories() throws IOException;

	public Category getCategory(String name) throws IOException;

	public List<String> getCategories(Category prnt) throws IOException;

	public List<Snippet> getSnippets(Category category, int start, int count) throws IOException;

	public Comment getComment(long hash) throws IOException;

}