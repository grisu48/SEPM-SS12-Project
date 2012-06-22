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
import org.smartsnip.core.File;
import org.smartsnip.core.Notification;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.shared.Pair;

import sun.reflect.Reflection;

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
 * 	if (Reflection.getCallerClass(&lt;packageDepth&gt;) == null
 * 			|| Reflection.getCallerClass(&lt;packageDepth&gt;) != PersistenceFactory.class) {
 * 		throw new IllegalAccessException(
 * 				&quot;Singleton pattern: caller must be PersistenceFactory class.&quot;);
 * 	}
 * }
 * </pre>
 * 
 * The {@code int} value {@code <packageDepth>} is to set to
 * {@code (2 + [depth of subpackage-tree])} for more details see
 * {@link Reflection#getCallerClass(int)}
 * 
 * @author littlelion
 * 
 */
public interface IPersistence {

	/**
	 * Constant for the parameter {@code flags} of the writeXxx() and
	 * removeXxx() methods: use the default conditions. This constant is
	 * overridden by all other flags, so it isn't necessary to set this flag
	 * combined with any additional flag. If no deviating behavior is described
	 * at the corresponding method the default is:
	 * <p>
	 * <blockquote>
	 * <nl>
	 * <li>to create a new entry if not already present</li>
	 * <li>to update an existing entry</li>
	 * <li>to keep database entries (don't use DELETE statements)</li>
	 * <li>to ignore null entries on updates (don't overwrite existing entries
	 * of columns with "null")
	 * </nl>
	 * </blockquote>
	 * <p>
	 * The values of the flags may change in future revisions, so to insert a
	 * hardcoded integer value instead of using this constant is discouraged.
	 * 
	 * @see #DB_NEW_ONLY
	 * @see #DB_UPDATE_ONLY
	 * @see #DB_FORCE_NULL_VALUES
	 * @see #DB_NO_DELETE
	 * @see #DB_FORCE_DELETE
	 */
	public static final int DB_DEFAULT = 0;

	/**
	 * Constant for the parameter {@code flags} of the writeXxx() and
	 * removeXxx() methods: reject to write if the element already exists in the
	 * database. This constant overrides {@link #DB_UPDATE_ONLY}.
	 */
	public static final int DB_NEW_ONLY = 1;

	/**
	 * Constant for the parameter {@code flags} of the writeXxx() and
	 * removeXxx() methods: reject the request if the element to update doesn't
	 * exist. This constant has no effect if {@link #DB_NEW_ONLY} is set.
	 */
	public static final int DB_UPDATE_ONLY = 2;

	/**
	 * Constant for the parameter {@code flags} of the writeXxx() and
	 * removeXxx() methods: overwrite existing values in the addressed columns
	 * with null values if they are null in the corresponding DB-object. This
	 * flag only has effect to object(s) which are currently present in the
	 * database. The database will reject all attempts to insert a null value
	 * into a not-nullable column, so be careful if using this option.
	 */
	public static final int DB_FORCE_NULL_VALUES = 4;

	/**
	 * Constant for the parameter {@code flags} of the writeXxx() and
	 * removeXxx() methods: don't delete contents of the database. This constant
	 * has exclusively effect on queries which remove contents from the
	 * database. It overrides {@link #DB_FORCE_DELETE}.
	 */
	public static final int DB_NO_DELETE = 8;

	/**
	 * Constant for the parameter {@code flags} of the writeXxx() and
	 * removeXxx() methods: don't delete contents of the database. This constant
	 * has exclusively effect on queries which remove contents from the
	 * database.
	 */
	public static final int DB_FORCE_DELETE = 16;

	/**
	 * Sorting constant for the search. Doesn't sort the results.
	 * 
	 * @see #SORT_LATEST
	 * @see #SORT_MOSTVIEWED
	 * @see #SORT_BEST_RATED
	 */
	public static final int SORT_UNSORTED = 0;

	/**
	 * Sorting constant for the search. Sorts the searching items by the lastest
	 * modified date.
	 */
	public static final int SORT_LATEST = 1;

	/**
	 * Sorting constant for the search. Sorts the search results by the view
	 * count.
	 */
	public static final int SORT_MOSTVIEWED = 2;

	/**
	 * Sorting constant for the search. Sorts the search results by the best
	 * ratings.
	 */
	public static final int SORT_BEST_RATED = 3;

	/**
	 * Modifiers for the {@link #getLanguages(int)} method. Fetch all available
	 * languages.
	 */
	public static final int LANGUAGE_GET_ALL = 0;

	/**
	 * Modifiers for the {@link #getLanguages(int)} method. Fetch only the
	 * default flagged languages.
	 */
	public static final int LANGUAGE_GET_DEFAULTS = 1;

	/**
	 * Modifiers for the {@link #getLanguages(int)} method. Fetch only the
	 * non-default flagged languages.
	 */
	public static final int LANGUAGE_GET_OTHERS = 2;

	/**
	 * Persist a single User-dataset.
	 * 
	 * @param user
	 *            the user to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeUser(User user, int flags) throws IOException;

	/**
	 * Persist multiple User-datasets.
	 * 
	 * @param users
	 *            the list of users to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeUser(List<User> users, int flags) throws IOException;

	/**
	 * Persist a new password into the database Using this method is deprecated
	 * because the grant_login flag isn't considered. Use
	 * {@link #writeLogin(User, String, Boolean, int)} instead.
	 * 
	 * @param user
	 *            the owner of the password
	 * @param password
	 *            to set for the user
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	@Deprecated
	public void writePassword(User user, String password, int flags)
			throws IOException;

	/**
	 * Persist a new password into the database. This method is deprecated
	 * because the grant_login flag isn't considered.
	 * 
	 * @param user
	 *            the owner of the login
	 * @param password
	 *            to set for the user
	 * @param grantLogin
	 *            set this flag to true if the user should be able to login
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeLogin(User user, String password, Boolean grantLogin,
			int flags) throws IOException;

	/**
	 * Persist a single Snippet-dataset.
	 * 
	 * @param snippet
	 *            the snippet to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection. To delete orphaned
	 *            tags in the same query use the flag {@link #DB_FORCE_DELETE}.
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Long writeSnippet(Snippet snippet, int flags) throws IOException;

	/**
	 * Persist multiple Snippet-datasets.
	 * 
	 * @param snippets
	 *            the list of snippets to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection. To delete orphaned
	 *            tags in the same query use the flag {@link #DB_FORCE_DELETE}.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeSnippet(List<Snippet> snippets, int flags)
			throws IOException;

	/**
	 * Persist a single Comment-dataset.
	 * 
	 * @param comment
	 *            the comment to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Long writeComment(Comment comment, int flags) throws IOException;

	/**
	 * Persist multiple Comment-datasets.
	 * 
	 * @param comments
	 *            the list of comments to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeComment(List<Comment> comments, int flags)
			throws IOException;

	/**
	 * Persist a single Tag-dataset.
	 * 
	 * @param tag
	 *            the tag to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeTag(Tag tag, int flags) throws IOException;

	/**
	 * Persist multiple Tag-datasets.
	 * 
	 * @param tags
	 *            the list of tags to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeTag(List<Tag> tags, int flags) throws IOException;

	/**
	 * Persist a single Notification-dataset.
	 * 
	 * @param notification
	 *            the notification to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Long writeNotification(Notification notification, int flags)
			throws IOException;

	/**
	 * Persist multiple Notification-datasets.
	 * 
	 * @param notifications
	 *            the list of notifications to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeNotification(List<Notification> notifications, int flags)
			throws IOException;

	/**
	 * Persist a single Code-dataset.
	 * 
	 * @param code
	 *            the code to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Long writeCode(Code code, int flags) throws IOException;

	/**
	 * Persist multiple Code-datasets.
	 * 
	 * @param codes
	 *            the list of code fragments to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeCode(List<Code> codes, int flags) throws IOException;

	/**
	 * Persist a file with the source code. The code with the given
	 * {@code codeId} must exist in the database.
	 * 
	 * @param codeId
	 *            the Id
	 * @param file
	 *            the file to persist
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeCodeFile(Long codeId, File file, int flags)
			throws IOException;

	/**
	 * Persist a single Category-dataset.
	 * 
	 * @param category
	 *            the category to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection. To make a category
	 *            parent it is necessary to set the
	 *            {@link #DB_FORCE_NULL_VALUES} flag.
	 * @return the id
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Long writeCategory(Category category, int flags) throws IOException;

	/**
	 * Persist multiple Category-datasets.
	 * 
	 * @param categories
	 *            the list of categories to write
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection. To make a category
	 *            parent it is necessary to set the
	 *            {@link #DB_FORCE_NULL_VALUES} flag.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeCategory(List<Category> categories, int flags)
			throws IOException;

	/**
	 * Persist a language.
	 * 
	 * @param language
	 *            the language to write
	 * @param highlighter
	 *            the calling string for invoking the syntax highlighter
	 * @param isDefault
	 *            Set this flag to {@code true} to assign this language to the
	 *            default set.
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeLanguage(String language, String highlighter,
			boolean isDefault, int flags) throws IOException;

	/**
	 * Persist a License.
	 * 
	 * @param shortDescription
	 *            short license content
	 * @param fullText
	 *            the license document
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void writeLicense(String shortDescription, String fullText, int flags)
			throws IOException;

	/**
	 * Persist a rating. This operation updates an existing rating if the user
	 * has rated already for the given snippet. This operation will be rejected
	 * in {@link #DB_NEW_ONLY} flag if an update on an existing rating should be
	 * performed.
	 * 
	 * @param rating
	 *            the rating to write
	 * 
	 * @param snippet
	 *            the snippet the rating belongs to
	 * @param user
	 *            the user who rated
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @return the average rating of the snippet
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Float writeRating(Integer rating, Snippet snippet, User user,
			int flags) throws IOException;

	/**
	 * Remove a rating. This operation updates an existing rating to '0' in
	 * default mode.
	 * 
	 * @param user
	 *            the user who rated
	 * @param snippet
	 *            the snippet to rate for
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection. The default is
	 *            {@link #DB_NO_DELETE} which sets the rating to {@code 0}. To
	 *            force the removal the rating entry set the
	 *            {@link #DB_FORCE_DELETE} flag.
	 * @return the average rating of the snippet
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Float unRate(User user, Snippet snippet, int flags)
			throws IOException;

	/**
	 * Persist a vote. This operation updates an existing vote if the user has
	 * voted already for the given comment. This operation will be rejected in
	 * {@link #DB_NEW_ONLY} flag if an update on an existing vote should be
	 * performed.
	 * 
	 * @param vote
	 *            the vote to write
	 * 
	 * @param comment
	 *            the comment to vote for
	 * @param user
	 *            the user who voted
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @return the actual values of the votes<br>
	 *         The first parameter gives the positive votes, the seconds
	 *         parameter of the pair gives the number of negative votes.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Pair<Integer, Integer> writeVote(Integer vote, Comment comment,
			User user, int flags) throws IOException;

	/**
	 * Persist a positive vote. This operation updates an existing vote if the
	 * user has voted already for the given comment. This operation will be
	 * rejected in {@link #DB_NEW_ONLY} flag if an update on an existing vote
	 * should be performed.
	 * 
	 * @param user
	 *            the user who voted
	 * @param comment
	 *            the comment to vote for
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @return the actual values of the votes<br>
	 *         The first parameter gives the positive votes, the seconds
	 *         parameter of the pair gives the number of negative votes.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Pair<Integer, Integer> votePositive(User user, Comment comment,
			int flags) throws IOException;

	/**
	 * Persist a negative vote. This operation updates an existing vote if the
	 * user has voted already for the given comment. This operation will be
	 * rejected in {@link #DB_NEW_ONLY} flag if an update on an existing vote
	 * should be performed.
	 * 
	 * @param user
	 *            the user who voted
	 * @param comment
	 *            the comment to vote for
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @return the actual values of the votes<br>
	 *         The first parameter gives the positive votes, the seconds
	 *         parameter of the pair gives the number of negative votes.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Pair<Integer, Integer> voteNegative(User user, Comment comment,
			int flags) throws IOException;

	/**
	 * Remove a vote. This operation updates an existing vote to 'none' in
	 * default mode.
	 * 
	 * @param user
	 *            the user who voted
	 * @param comment
	 *            the comment to vote for
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection. The default is
	 *            {@link #DB_NO_DELETE} which sets the vote to 'none'. To force
	 *            the removal the rating entry set the {@link #DB_FORCE_DELETE}
	 *            flag.
	 * @return the actual values of the votes<br>
	 *         The first parameter gives the positive votes, the seconds
	 *         parameter of the pair gives the number of negative votes.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public Pair<Integer, Integer> unVote(User user, Comment comment, int flags)
			throws IOException;

	/**
	 * Sets the favourite-state of the snippet.
	 * 
	 * @param snippet
	 *            the snippet to change state
	 * @param user
	 *            the owner of the favorite list
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void addFavourite(Snippet snippet, User user, int flags)
			throws IOException;

	/**
	 * Clears the favourite-state of the snippet.
	 * 
	 * @param snippet
	 *            the snippet to change state
	 * @param user
	 *            the owner of the favorite list
	 * @param flags
	 *            the constraints for the write access. more than one constraint
	 *            can be added by a bitwise or connection. Set
	 *            {@link #DB_FORCE_DELETE} to delete the entry from database.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeFavourite(Snippet snippet, User user, int flags)
			throws IOException;

	/**
	 * Remove the User from the database.
	 * 
	 * @param user
	 *            the user to remove
	 * @param flags
	 *            the constraints for the write access. The default is
	 *            {@link #DB_FORCE_DELETE}. Due to database constraints, the
	 *            Password-, Favourite- and Notification-entries are removed
	 *            also.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeUser(User user, int flags) throws IOException;

	/**
	 * Remove the Password from the database.
	 * <p>
	 * This method removes the ability to login with the given user account. The
	 * default behavior of this method is to disable the {@code grantLogin} flag
	 * but to keep the password entry.
	 * 
	 * @param user
	 *            the user account to lock
	 * @param flags
	 *            the constraints for the write access. The default is
	 *            {@link #DB_NO_DELETE}. To force the removal the password entry
	 *            set the {@link #DB_FORCE_DELETE} flag.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeLogin(User user, int flags) throws IOException;

	/**
	 * Remove the Snippet from the database.
	 * <p>
	 * Due to database constraints, all Comment-, Code-, Rating- and
	 * Vote-entries are removed also.
	 * 
	 * @param snippet
	 *            the object to remove
	 * @param flags
	 *            the constraints for the write access. The default is to delete
	 *            the snippet with it's comments including the votes, codes,
	 *            ratings, and the targeting favourites. To delete orphaned tags
	 *            in the same query use the flag {@link #DB_FORCE_DELETE}.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeSnippet(Snippet snippet, int flags) throws IOException;

	/**
	 * Remove the Comment from the database.
	 * <p>
	 * Due to database constraints, all Vote-entrys are removed also.
	 * 
	 * @param comment
	 *            the object to remove
	 * @param flags
	 *            the constraints for the write access. The default is to delete
	 *            the comment with it's votes.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeComment(Comment comment, int flags) throws IOException;

	/**
	 * Remove the Tag from the database.
	 * <p>
	 * An attempt to delete a {@code Tag} entry used by a {@link Snippet} is
	 * restricted by database constraints.
	 * 
	 * @param tag
	 *            the tag to remove
	 * @param flags
	 *            the constraints for the write access. The default is
	 *            {@link #DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeTag(Tag tag, int flags) throws IOException;

	// TODO add method: method to cleanup the database (if needed)
	// /**
	// * Remove all unused tags.
	// * @param flags
	// * the constraints for the write access. The default is
	// * {@link #DB_FORCE_DELETE}
	// * @throws IOException
	// * at a problem committing the data
	// */
	// public void removeUnusedTags(int flags) throws IOException;

	/**
	 * Remove the Notification from the database.
	 * 
	 * @param notification
	 *            the object to remove
	 * @param flags
	 *            the constraints for the write access. The default is
	 *            {@link #DB_FORCE_DELETE}
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeNotification(Notification notification, int flags)
			throws IOException;

	/**
	 * Remove the Code from the database.
	 * 
	 * @param code
	 *            the object to remove
	 * @param flags
	 *            the constraints for the write access. The default is
	 *            {@link #DB_FORCE_DELETE}.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeCode(Code code, int flags) throws IOException;

	/**
	 * Remove the Category from the database.
	 * <p>
	 * The parent category inherits all subcategories of the deleted category.
	 * <p>
	 * An attempt to delete a {@code Category} entry used by a {@link Snippet}
	 * is restricted by the database constraints.
	 * 
	 * @param category
	 *            the object to remove
	 * @param flags
	 *            the constraints for the write access. The default is
	 *            {@link #DB_FORCE_DELETE}.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeCategory(Category category, int flags) throws IOException;

	/**
	 * Remove the Language from the database.
	 * <p>
	 * An attempt to delete a {@code Language} entry used by a {@link Code} is
	 * restricted by database constraints.
	 * 
	 * @param language
	 *            the language to remove
	 * @param flags
	 *            the constraints for the write access. The default is
	 *            {@link #DB_FORCE_DELETE}.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeLanguage(String language, int flags) throws IOException;

	/**
	 * Remove the License file from the database. The snippets which refer to
	 * this object loose their license.
	 * 
	 * @param shortDescription
	 *            the short description matching to the license
	 * @param flags
	 *            the constraints for the write access. The default is
	 *            {@link #DB_FORCE_DELETE}.
	 * @throws IOException
	 *             at a problem committing the data
	 */
	public void removeLicense(String shortDescription, int flags)
			throws IOException;

	/**
	 * Get a user by his nickname.
	 * 
	 * @param nick
	 *            the nickname of the user as key
	 * @return the user object
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public User getUser(String nick) throws IOException;

	/**
	 * Get a user by his email address.
	 * 
	 * @param email
	 *            the email address
	 * @return the user object
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public User getUserByEmail(String email) throws IOException;

	/**
	 * Get all users.
	 * 
	 * @param start
	 *            the starting index of the results set, null or 0 for start at
	 *            the first index
	 * @param count
	 *            the number of entries, null if no upper limit is wanted
	 * @return a list of all users
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<User> getAllUsers(Integer start, Integer count)
			throws IOException;

	/**
	 * Get the password string related to the user. This method is deprecated
	 * because of security reasons. It violates the one-way stream of password
	 * strings to the database.
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
	@Deprecated
	public String getPassword(User user) throws IOException,
			UnsupportedOperationException;

	/**
	 * Verifies the password and the permission to login of the given user. This
	 * method returns true if the grant_login flag is set and the given password
	 * matches to the stored password. Due to security reasons the password
	 * can't be read out of the database.
	 * 
	 * @param user
	 * @param password
	 * @return true if the user is granted to login
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public boolean verifyPassword(User user, String password)
			throws IOException;

	/**
	 * get the state of the grant_login flag. Only if this flag is true the user
	 * is permitted to login.
	 * 
	 * @param user
	 * @return true if the user's grantLogin flag is set
	 * @throws IOException
	 */
	public boolean isLoginGranted(User user) throws IOException;

	/**
	 * Find all users with matching names.
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
	 * Get all snippets where the user is the owner.
	 * 
	 * @param owner
	 *            a user owning the snippets
	 * @return all matching snippets
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Snippet> getUserSnippets(User owner) throws IOException;

	/**
	 * Get all snippets which are tagged with the favourite attribute of the
	 * given user.
	 * 
	 * @param user
	 *            the user who attached the favourite tags
	 * @return all matching snippets
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Snippet> getFavorited(User user) throws IOException;

	/**
	 * Get all snippets.
	 * 
	 * @param start
	 *            the starting index of the results set, null or 0 for start at
	 *            the first index
	 * @param count
	 *            the number of entries, null if no upper limit is wanted
	 * @param sortingOrder
	 *            the result is sorted by the given order. Available are the
	 *            sorting constants {@link #SORT_UNSORTED}, {@link #SORT_LATEST}
	 *            , {@link #SORT_MOSTVIEWED} and {@link #SORT_BEST_RATED}.
	 * @return all snippets of the given range
	 * @throws IOException
	 */
	public List<Snippet> getAllSnippets(Integer start, Integer count,
			int sortingOrder) throws IOException;

	/**
	 * Get the snippet with the given id or null, if not existing.
	 * 
	 * @param id
	 *            id of the snippet
	 * @return the snippet with the given tag or null, if not existing
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Snippet getSnippet(Long id) throws IOException;

	/**
	 * get a random snippet
	 * 
	 * @param random
	 *            a normalized random number (0 <= random <= 1)
	 * @return the snippet with the given tag or null, if not existing
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Snippet getRandomSnippet(double random) throws IOException;

	/**
	 * Get all snippets which contain all of the given tags in their tag-list.
	 * 
	 * @param matchingTags
	 *            a list of tags
	 * @return all matching snippets
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	@Deprecated
	public List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException;

	/**
	 * Get all snippets of a given category.
	 * 
	 * @param category
	 *            the category
	 * @return all matching snippets
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	@Deprecated
	public List<Snippet> getSnippets(Category category) throws IOException;

	/**
	 * Get all snippets of a given category.
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
	public List<Snippet> getSnippets(Category category, Integer start,
			Integer count) throws IOException;

	/**
	 * Get the comment by it's identifier.
	 * 
	 * @param id
	 * @return the comment
	 * @throws IOException
	 *             at a problem retrieving the data at a problem retrieving the
	 *             data, e.g. if the comment doesn't exist
	 */
	public Comment getComment(Long id) throws IOException;

	/**
	 * Get all comments attached to the given snippet.
	 * 
	 * @param snippet
	 * @return all matching comments
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Comment> getComments(Snippet snippet) throws IOException;

	/**
	 * Get all tags attached to the given snippet.
	 * 
	 * @param snippet
	 * @return the list of tags
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	@Deprecated
	public List<Tag> getTags(Snippet snippet) throws IOException;

	/**
	 * Get all tags.
	 * 
	 * @param start
	 *            the starting index of the results set, null or 0 for start at
	 *            the first index
	 * @param count
	 *            the number of entries, null if no upper limit is wanted
	 * @return a list of all tags
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Tag> getAllTags(Integer start, Integer count)
			throws IOException;

	/**
	 * Get the notification by id.
	 * 
	 * @param id
	 *            the id
	 * @return the notification
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Notification getNotification(Long id) throws IOException;

	/**
	 * Get all notifications belonging to the given user.
	 * 
	 * @param userName
	 *            the name of the user to notify
	 * @param unreadOnly
	 *            return only the notifications which are viewed the first time
	 *            now
	 * @return all (new) notifications
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Notification> getNotifications(String userName,
			boolean unreadOnly) throws IOException;

	/**
	 * Get all code fragments of a given snippet.
	 * 
	 * @param snippet
	 * @return all code fragments
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Code> getCodes(Snippet snippet) throws IOException;

	/**
	 * Get a code object by it's id.
	 * 
	 * @param codeId
	 *            the id
	 * @return the code object
	 * @throws IOException
	 */
	public Code getCode(Long codeId) throws IOException;

	/**
	 * Get a file with the source code.
	 * 
	 * @param codeId
	 *            the id
	 * @return the file
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public File getCodeFile(Long codeId) throws IOException;

	/**
	 * Get all categories.
	 * 
	 * @return all categories
	 * @throws IOException
	 */
	public List<Category> getAllCategories() throws IOException;

	/**
	 * Get the category the snippet belongs to.
	 * 
	 * @param snippet
	 * @return the category
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Category getCategory(Snippet snippet) throws IOException;

	/**
	 * Get the category by name.
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
	 * Get the immediate parent of a category.
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
	 * Get the immediate subcategories.
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
	 * Get all available programming-languages.
	 * 
	 * @param toFetch
	 *            Defines the subset of languages to fetch. Use the constants
	 *            {@link #LANGUAGE_GET_ALL}, {@link #LANGUAGE_GET_DEFAULTS} or
	 *            {@link #LANGUAGE_GET_OTHERS}.
	 * 
	 * @return a list of all languages
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<String> getLanguages(int toFetch) throws IOException;

	/**
	 * Get the properties assigned to the given language.
	 * 
	 * @param language
	 *            the language to fetch the properties
	 * 
	 * @return the properties: <br>
	 *         <ul>
	 *         <li>a string argument to pass to the syntax highlighter</li>
	 *         <li>a flag assigning this language as default</li>
	 *         </ul>
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Pair<String, Boolean> getLanguageProperties(String language)
			throws IOException;

	/**
	 * Get the license document by it's shortDescription.
	 * 
	 * @param shortDescription
	 *            the short description
	 * @return the full text document
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public String getLicense(String shortDescription) throws IOException;

	/**
	 * Get all ratings belonging to the given snippet.
	 * 
	 * @param snippet
	 * @return a list of pairs of user objects and ratings as integer
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Pair<User, Integer>> getRatings(Snippet snippet)
			throws IOException;

	/**
	 * Get the average rating belonging to the given snippet.
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
	 * Get the state of a user's vote according to a comment.
	 * 
	 * @param user
	 * @param comment
	 * @return the vote, 0 if no vote exists or it has been undone.
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public Integer getVote(User user, Comment comment) throws IOException;

	/**
	 * Search for the arguments of the given search-string.
	 * 
	 * @param searchString
	 *            the string to search for
	 * @param start
	 *            the starting index of the results set, null or 0 for start at
	 *            the first index
	 * @param count
	 *            the number of entries, null if no upper limit is wanted
	 * @param sortingOrder
	 *            the result is sorted by the given order. Available are the
	 *            sorting constants {@link #SORT_UNSORTED}, {@link #SORT_LATEST}
	 *            , {@link #SORT_MOSTVIEWED} and {@link #SORT_BEST_RATED}.
	 * @return a list of snippets which contain the given arguments
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public List<Snippet> search(String searchString, Integer start,
			Integer count, int sortingOrder) throws IOException;

	/**
	 * Get the number of users which are currently in the database.
	 * 
	 * @return the count
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public int getUserCount() throws IOException;

	/**
	 * Get the number of categories which are currently in the database.
	 * 
	 * @return the count
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public int getCategoryCount() throws IOException;

	/**
	 * Get the number of snippets which are currently in the database.
	 * 
	 * @return the count
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public int getSnippetsCount() throws IOException;

	/**
	 * Get the number of tags which are currently in the database.
	 * 
	 * @return the count
	 * @throws IOException
	 *             at a problem retrieving the data
	 */
	public int getTagsCount() throws IOException;

	/**
	 * Close the implementing factory. If some logging is needed use
	 * {@link PersistenceFactory#closeFactory()} instead.
	 * 
	 * @throws IOException
	 *             on a problem closing the factory
	 */
	void close() throws IOException;
}