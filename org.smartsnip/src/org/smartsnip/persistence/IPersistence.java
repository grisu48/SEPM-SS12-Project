/**
 * File: IPersistence.java
 * Date: 22.04.2012
 */
package org.smartsnip.persistence;

import java.io.IOException;
import java.util.List;

import org.smartsnip.core.*;
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
	 * overrides {@link IPersistence#DB_UPDATE_ONLY}.
	 */
	public static int DB_NEW_ONLY = 1;

	/**
	 * constant for the argument 'mode' of the writeXxx() methods: reject the
	 * request if the element to update doesn't exist. This constant has no
	 * effect if {@link IPersistence#DB_NEW_ONLY} is set.
	 */
	public static int DB_UPDATE_ONLY = 2;

	/**
	 * constant for the argument 'mode' of the writeXxx() methods: don't delete
	 * contents of the database. This constant overrides
	 * {@link IPersistence#DB_FORCE_DELETE}.
	 */
	public static int DB_NO_DELETE = 4;

	/**
	 * constant for the argument 'mode' of the writeXxx() methods: don't delete
	 * contents of the database. This constant has no effect if
	 * {@link IPersistence#DB_NEW_ONLY} is set.
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
	 *             at a problem committing the data
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
	 *             at a problem committing the data
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
	 *             at a problem committing the data
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
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public long writeSnippet(Snippet snippet, int mode) throws IOException;

	/**
	 * Persist multiple Snippet-datasets.
	 * 
	 * @param snippets
	 *            the list of snippets to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
	 *             at a problem committing the data
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
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public long writeComment(Comment comment, int mode) throws IOException;

	/**
	 * Persist multiple Comment-datasets.
	 * 
	 * @param comments
	 *            the list of comments to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
	 *             at a problem committing the data
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
	 *             at a problem committing the data
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
	 *             at a problem committing the data
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
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public long writeNotification(Notification notification, int mode)
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
	 *             at a problem committing the data
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
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public long writeCode(Code code, int mode) throws IOException;

	/**
	 * Persist multiple Code-datasets.
	 * 
	 * @param codes
	 *            the list of code fragments to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
	 *             at a problem committing the data
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
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public long writeCategory(Category category, int mode) throws IOException;

	/**
	 * Persist multiple Category-datasets.
	 * 
	 * @param categories
	 *            the list of categories to write
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
	 *             at a problem committing the data
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
	 *             at a problem committing the data
	 */
	public void writeLanguage(String language, int mode) throws IOException;

	/**
	 * Persist a rating. This operation updates an existing rating if the user
	 * has rated already for the given snippet. This operation will be rejected
	 * in {@link IPersistence#DB_NEW_ONLY} mode if an update on an existing
	 * rating should be performed.
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
	 *             at a problem committing the data
	 */
	public void writeRating(Integer rating, Snippet snippet, User user, int mode)
			throws IOException;

	/**
	 * Remove a rating. This operation updates an existing rating to '0' in
	 * {@link IPersistence#DB_NO_DELETE} mode which is currently the default
	 * behavior. In {@link IPersistence#DB_FORCE_DELETE} mode the given database
	 * entry is deleted.
	 * 
	 * @param user
	 *            the user who rated
	 * @param snippet
	 *            the snippet to rate for
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void unRate(User user, Snippet snippet, int mode) throws IOException;

	/**
	 * Persist a vote. This operation updates an existing vote if the user has
	 * voted already for the given comment. This operation will be rejected in
	 * {@link IPersistence#DB_NEW_ONLY} mode if an update on an existing vote
	 * should be performed.
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
	 *             at a problem committing the data
	 */
	public void writeVote(Integer vote, Comment comment, User user, int mode)
			throws IOException;

	/**
	 * Persist a positive vote. This operation updates an existing vote if the
	 * user has voted already for the given comment. This operation will be
	 * rejected in {@link IPersistence#DB_NEW_ONLY} mode if an update on an
	 * existing vote should be performed.
	 * 
	 * @param user
	 *            the user who voted
	 * @param comment
	 *            the comment to vote for
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @return the actual value of {@code Comment.vote_sum}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void votePositive(User user, Comment comment, int mode)
			throws IOException;

	/**
	 * Persist a negative vote. This operation updates an existing vote if the
	 * user has voted already for the given comment. This operation will be
	 * rejected in {@link IPersistence#DB_NEW_ONLY} mode if an update on an
	 * existing vote should be performed.
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
	 *             at a problem committing the data
	 */
	public void voteNegative(User user, Comment comment, int mode)
			throws IOException;

	/**
	 * Remove a vote. This operation updates an existing vote to 'none' in
	 * {@link IPersistence#DB_NO_DELETE} mode which is currently the default
	 * behavior. In {@link IPersistence#DB_FORCE_DELETE} mode the given database
	 * entry is deleted.
	 * 
	 * @param user
	 *            the user who voted
	 * @param comment
	 *            the comment to vote for
	 * @param mode
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a logical or connection.
	 * @throws IOException
	 *             at a problem committing the data
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
	 *             at a problem committing the data
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
	 *             at a problem committing the data
	 */
	public void removeFavourite(Snippet snippet, User user, int mode)
			throws IOException;

	/**
	 * remove the User from the database
	 * <p>
	 * Due to database constraints, the Password-, Favourite- and
	 * Notification-entries are removed also.
	 * 
	 * @param nickname
	 *            the user's nickname as key of the object to remove
	 * @param mode
	 *            the constraints for the write access. The default is
	 *            {@link IPersistence#DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeUser(String nickname, int mode) throws IOException;

	/**
	 * remove the Snippet from the database
	 * <p>
	 * Due to database constraints, all Comment-, Code-, Rating- and
	 * Vote-entries are removed also.
	 * 
	 * @param snippetId
	 *            the id of the object to remove
	 * @param mode
	 *            the constraints for the write access. The default is
	 *            {@link IPersistence#DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeSnippet(Long snippetId, int mode) throws IOException;

	/**
	 * remove the Comment from the database
	 * <p>
	 * Due to database constraints, all Vote-entrys are removed also.
	 * 
	 * @param commentId
	 *            the id of the object to remove
	 * @param mode
	 *            the constraints for the write access. The default is
	 *            {@link IPersistence#DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeComment(Long commentId, int mode) throws IOException;

	/**
	 * remove the Tag from the database
	 * 
	 * @param tag
	 *            the tag to remove
	 * @param mode
	 *            the constraints for the write access. The default is
	 *            {@link IPersistence#DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeTag(Tag tag, int mode) throws IOException;

	/**
	 * remove the Notification from the database
	 * 
	 * @param notificationId
	 *            the id of the object to remove
	 * @param mode
	 *            the constraints for the write access. The default is
	 *            {@link IPersistence#DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeNotification(Long notificationId, int mode)
			throws IOException;

	/**
	 * remove all read notifications owned by a user
	 * 
	 * @param user
	 *            the user's nickname as owner of the objects to remove
	 * @param mode
	 *            the constraints for the write access. The default is
	 *            {@link IPersistence#DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeReadNotifications(User user, int mode) throws IOException;

	/**
	 * remove the Code from the database
	 * 
	 * @param codeId
	 *            the id of the object to remove
	 * @param mode
	 *            the constraints for the write access. The default is
	 *            {@link IPersistence#DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeCode(Long codeId, int mode) throws IOException;

	/**
	 * remove the Category from the database
	 * <p>
	 * The parent category inherits all subcategories of the deleted category.
	 * <p>
	 * An attempt to delete a {@code Category} entry used by a {@link Snippet}
	 * is restricted by the database constraints.
	 * 
	 * @param categoryId
	 *            the id of the object to remove
	 * @param mode
	 *            the constraints for the write access. The default is
	 *            {@link IPersistence#DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeCategory(Long categoryId, int mode) throws IOException;

	/**
	 * remove the Language from the database
	 * <p>
	 * An attempt to delete a {@code Language} entry used by a {@link Code} is
	 * restricted by the database constraints.
	 * 
	 * @param language
	 *            the language to remove
	 * @param mode
	 *            the constraints for the write access. The default is
	 *            {@link IPersistence#DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeLanguage(String language, int mode) throws IOException;

	/**
	 * get a user by his nickname
	 * 
	 * @param nick
	 *            the nickname of the user as key
	 * @return the user object
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public User getUser(String nick) throws IOException;

	/**
	 * get a user by his email address
	 * 
	 * @param email
	 *            the email address
	 * @return the user object
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public User getUserByEmail(String email) throws IOException;

	/**
	 * get the password string related to the user
	 * 
	 * @param user
	 *            the user
	 * @return the password string
	 * @throws IOException
	 *             at a problem retrieving the data
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
	 *             at a problem retrieving the data
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
	 *             at a problem retrieving the data
	 */
	public List<User> findUser(String realName) throws IOException;

	/**
	 * get all snippets where the user is the owner
	 * 
	 * @param owner
	 *            a user owning the snippets
	 * @return all matching snippets
	 * @throws IOException
	 *             at a problem retrieving the data
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
	 *             at a problem retrieving the data
	 */
	public List<Snippet> getFavorited(User user) throws IOException;

	/**
	 * get all snippets which contain all of the given tags in their tag-list
	 * 
	 * @param matchingTags
	 *            a list of tags
	 * @return all matching snippets
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException;

	/**
	 * get all snippets of a given category
	 * 
	 * @param category
	 *            the category
	 * @return all matching snippets
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Snippet> getSnippets(Category category) throws IOException;

	/**
	 * get all snippets of a given category
	 * 
	 * @param category
	 *            the category
	 * @param start
	 *            the starting index of the results set, null or 0 for start at
	 *            the first index
	 * @param count
	 *            the number of entries, null if no upper limit is wanted
	 * @return all matching snippets
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Snippet> getSnippets(Category category, int start, int count)
			throws IOException;

	/**
	 * get the comment by it's identifier
	 * 
	 * @param id
	 * @return the comment
	 * @throws IOException
	 *             at a problem retrieving the data at a problem retrieving the
	 *             data, e.g. if the comment doesn't exist
	 */
	public Comment getComment(long id) throws IOException;

	/**
	 * get all comments attached to the given snippet
	 * 
	 * @param snippet
	 * @return all matching comments
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Comment> getComments(Snippet snippet) throws IOException;

	/**
	 * get all tags attached to the given snippet
	 * 
	 * @param snippet
	 * @return the list of tags
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Tag> getTags(Snippet snippet) throws IOException;

	/**
	 * get all tags
	 * 
	 * @return a list of all tags
	 * @throws IOException
	 *             at a problem retrieving the data
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
	 *             at a problem retrieving the data
	 */
	public List<Notification> getNotifications(User user, boolean unreadOnly)
			throws IOException;

	/**
	 * get all code fragments of a given snippet
	 * 
	 * @param snippet
	 * @return all code fragments
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Code> getCodes(Snippet snippet) throws IOException;

	/**
	 * get all category names
	 * 
	 * @return all categories by name
	 * @throws IOException
	 */
	public List<String> getAllCategories() throws IOException;

	/**
	 * get the category the snippet belongs to
	 * 
	 * @param snippet
	 * @return the category
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Category getCategory(Snippet snippet) throws IOException;

	/**
	 * get the category by name
	 * <p>
	 * This method returns a Category with the parent field set to null.
	 * 
	 * @param name
	 *            the name of the category as key
	 * @return the category object
	 * @throws IOException
	 */
	public Category getCategory(String name) throws IOException;

	/**
	 * get the immediate parent of a category
	 * <p>
	 * This method returns a generated Category object if the parameter is root.
	 * The marker contains the string "_you_asked_for_root_parent" in the name
	 * field.
	 * 
	 * @param category
	 *            the child category
	 * @return the parent category or a generated marker object if the given
	 *         category is a root
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Category getParentCategory(Category category) throws IOException;

	/**
	 * get the immediate subcategories
	 * 
	 * @param category
	 *            the parent category
	 * @return the direct child categories in first order. If the given category
	 *         is a leaf an empty list is returned.
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Category> getSubcategories(Category category)
			throws IOException;

	/**
	 * get the immediate subcategories as string
	 * 
	 * @param category
	 *            the parent category
	 * @return the direct child categories in first order. If the given category
	 *         is a leaf an empty list is returned.
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<String> getSubcategoryNames(Category category)
			throws IOException;

	/**
	 * get all available programming-languages
	 * 
	 * @return a list of all languages
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<String> getAllLanguages() throws IOException;

	/**
	 * get all ratings belonging to the given snippet
	 * 
	 * @param snippet
	 * @return a list of pairs of user objects and ratings as integer
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Pair<User, Integer>> getRatings(Snippet snippet)
			throws IOException;

	/**
	 * get the average rating belonging to the given snippet
	 * 
	 * @param snippet
	 * @return the average of all ratings
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Float getAverageRating(Snippet snippet) throws IOException;

	/**
	 * Creates a pair with the votes for a comment.
	 * <p>
	 * The first parameter gives the positive votes, the seconds parameter of
	 * the pair gives the number of negative votes.
	 * 
	 * @param comment
	 * @return a pair of two integers
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Pair<Integer, Integer> getVotes(Comment comment) throws IOException;

	/**
	 * get the state of a user's vote according to a comment
	 * 
	 * @param user
	 * @param comment
	 * @return the vote, 0 if no vote exists or it has been undone.
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Integer getVote(User user, Comment comment) throws IOException;

	/**
	 * search for the arguments of the given search-string.
	 * 
	 * @param searchString
	 *            the string to search for
	 * @param start
	 *            the starting index of the results set, null or 0 for start at
	 *            the first index
	 * @param count
	 *            the number of entries, null if no upper limit is wanted
	 * @return a list of snippets which contain the given arguments
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Snippet> search(String searchString, int start, int count)
			throws IOException;

	/**
	 * get the number of users which are currently in the database
	 * 
	 * @return the count
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public int getUserCount() throws IOException;

	/**
	 * get the number of categories which are currently in the database
	 * 
	 * @return the count
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public int getCategoryCount() throws IOException;

	/**
	 * get the number of snippets which are currently in the database
	 * 
	 * @return the count
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public int getSnippetsCount() throws IOException;

	/**
	 * get the number of tags which are currently in the database
	 * 
	 * @return the count
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public int getTagsCount() throws IOException;

	/**
	 * get the number of entries where the tag is used
	 * 
	 * @param tag
	 * @return the number the tag is used. 0 if the tag is unused or doesn't
	 *         exist.
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public int getTagFrequency(Tag tag) throws IOException;
}