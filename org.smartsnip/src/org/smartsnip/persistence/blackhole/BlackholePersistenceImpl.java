/**
 * File: BlackholePersistenceImpl.java
 * Date: 26.04.2012
 */
package org.smartsnip.persistence.blackhole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.smartsnip.core.*;
import org.smartsnip.persistence.IPersistence;
import org.smartsnip.persistence.PersistenceFactory;
import org.smartsnip.shared.Pair;

import sun.reflect.Reflection;

/**
 * Implementation of the persistence layer. This class provides minimal database
 * functionality. It stores no data and provides only predefined datasets.
 * 
 * @author littlelion
 * 
 */
public class BlackholePersistenceImpl implements IPersistence {

	private BHPersistenceHelper helper = new BHPersistenceHelper();

	private User staticUser1 = this.helper.createUser("nobody", "blabla",
			"nobody@anonymus.org", User.UserState.validated, null);
	private User staticUser2 = this.helper.createUser("bin_da", "asdfgh",
			"bd@finger.net", User.UserState.validated, null);
	private Code staticCode = this.helper.createCode(1L,
			"/* There's nothing interesting to know.*/", "java", null, 0);

	/**
	 * This constructor is protected against multiple instantiation to
	 * accomplish a singleton pattern. It rejects any attempt to build an
	 * instance except it is called by the
	 * {@link PersistenceFactory#getInstance(int)} method.
	 */
	protected BlackholePersistenceImpl() throws IllegalAccessException {
		super();
		if (Reflection.getCallerClass(3) == null
				|| Reflection.getCallerClass(3) != PersistenceFactory.class) {
			throw new IllegalAccessException(
					"Singleton pattern: caller must be PersistenceFactory class.");
		}
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeUser(org.smartsnip.core.User,
	 *      int)
	 */
	@Override
	public void writeUser(User user, int flags) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeUser(java.util.List,
	 *      int)
	 */
	@Override
	public void writeUser(List<User> users, int flags) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writePassword(org.smartsnip.core.User,
	 *      java.lang.String, int)
	 */
	@Override
	public void writePassword(User user, String password, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	@Override
	public Long writeSnippet(Snippet snippet, int flags) throws IOException {
		return 1L;
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeSnippet(java.util.List,
	 *      int)
	 */
	@Override
	public void writeSnippet(List<Snippet> snippets, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @return
	 * @see org.smartsnip.persistence.IPersistence#writeComment(org.smartsnip.core.Comment,
	 *      int)
	 */
	@Override
	public Long writeComment(Comment comment, int flags) throws IOException {
		return 1L;
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeComment(java.util.List,
	 *      int)
	 */
	@Override
	public void writeComment(List<Comment> comments, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(org.smartsnip.core.Tag,
	 *      int)
	 */
	@Override
	public void writeTag(Tag tag, int flags) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(java.util.List, int)
	 */
	@Override
	public void writeTag(List<Tag> tags, int flags) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @return
	 * @see org.smartsnip.persistence.IPersistence#writeNotification(org.smartsnip.core.Notification,
	 *      int)
	 */
	@Override
	public Long writeNotification(Notification notification, int flags)
			throws IOException {
		return 1L;
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeNotification(java.util.List,
	 *      int)
	 */
	@Override
	public void writeNotification(List<Notification> notifications, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @return
	 * @see org.smartsnip.persistence.IPersistence#writeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	@Override
	public Long writeCode(Code code, int flags) throws IOException {
		return 1L;
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCode(java.util.List,
	 *      int)
	 */
	@Override
	public void writeCode(List<Code> codes, int flags) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @return
	 * @see org.smartsnip.persistence.IPersistence#writeCategory(org.smartsnip.core.Category,
	 *      int)
	 */
	@Override
	public Long writeCategory(Category category, int flags) throws IOException {
		return 1L;
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCategory(java.util.List,
	 *      int)
	 */
	@Override
	public void writeCategory(List<Category> categories, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeLanguage(java.lang.String,
	 *      int)
	 */
	@Override
	public void writeLanguage(String language, int flags) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeRating(java.lang.Integer,
	 *      org.smartsnip.core.Snippet, org.smartsnip.core.User, int)
	 */
	@Override
	public void writeRating(Integer rating, Snippet snippet, User user, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeVote(java.lang.Integer,
	 *      org.smartsnip.core.Comment, org.smartsnip.core.User, int)
	 */
	@Override
	public void writeVote(Integer vote, Comment comment, User user, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#votePositive(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void votePositive(User user, Comment comment, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#voteNegative(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void voteNegative(User user, Comment comment, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#unVote(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void unVote(User user, Comment comment, int flags) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#addFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	@Override
	public void addFavourite(Snippet snippet, User user, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	@Override
	public void removeFavourite(Snippet snippet, User user, int flags)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String nick) throws IOException {
		User result = this.helper.createUser(nick, "blabla", nick
				+ "@anonymus.org", User.UserState.validated, null);
		return result;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserByEmail(java.lang.String)
	 */
	@Override
	public User getUserByEmail(String email) throws IOException {
		User result = this.helper.createUser("nobody", "some anonymous writer",
				email, User.UserState.validated, null);
		return result;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getPassword(org.smartsnip.core.User)
	 */
	@Override
	public String getPassword(User user) throws IOException,
			UnsupportedOperationException {
		throw new UnsupportedOperationException(
				"Use method verifyPassword(User, String) instead.");
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#verifyPassword(org.smartsnip.core.User,
	 *      java.lang.String)
	 */
	@Override
	public boolean verifyPassword(User user, String password)
			throws IOException {
		return true;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#findUser(java.lang.String)
	 */
	@Override
	public List<User> findUser(String realName) throws IOException {
		List<User> list = new ArrayList<User>();
		list.add(this.helper.createUser("nobody", "blabla",
				"nobody@anonymus.org", User.UserState.validated, null));
		list.add(this.helper.createUser("bin_da", "asdfgh", "bd@finger.net",
				User.UserState.validated, null));
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserSnippets(org.smartsnip.core.User)
	 */
	@Override
	public List<Snippet> getUserSnippets(User owner) throws IOException {
		List<Snippet> snips = new ArrayList<Snippet>();
		Snippet snip = this.helper.createSnippet(1L, owner, "The Header",
				"Some Content", this.helper.createCategory("undefined",
						"Undefined Content", null), new ArrayList<Tag>(),
				new ArrayList<Comment>(), this.helper.createCode(1L,
						"/* There's nothing interesting to know.*/", "java",
						null, 0), "license free", 0);
		snips.add(snip);
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getFavorited(org.smartsnip.core.User)
	 */
	@Override
	public List<Snippet> getFavorited(User user) throws IOException {
		return getUserSnippets(user);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(java.util.List)
	 */
	@Override
	public List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException {
		return getUserSnippets(staticUser1);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(org.smartsnip.core.Category)
	 */
	@Override
	public List<Snippet> getSnippets(Category category) throws IOException {
		return getUserSnippets(staticUser1);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(org.smartsnip.core.Category,
	 *      int, int)
	 */
	@Override
	public List<Snippet> getSnippets(Category category, Integer start,
			Integer count) throws IOException {
		return getSnippets(category);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getComments(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Comment> getComments(Snippet snippet) throws IOException {
		List<Comment> list = new ArrayList<Comment>();
		Comment comm1 = this.helper.createComment(staticUser1, snippet,
				"commented by nobody", 1L, new Date(
						System.currentTimeMillis() - 86400000), 5, 3);
		Comment comm2 = this.helper.createComment(staticUser2, snippet,
				"commented by bin_da", 2L, new Date(
						System.currentTimeMillis() - 3600000), 1, 0);
		list.add(comm1);
		list.add(comm2);
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getComment(Long)
	 */
	@Override
	public Comment getComment(Long id) throws IOException {
		return getComments(null).get(0);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTags(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Tag> getTags(Snippet snippet) throws IOException {
		List<Tag> list = new ArrayList<Tag>();
		Tag tag1 = this.helper.createTag("java");
		Tag tag2 = this.helper.createTag("sort");
		list.add(tag1);
		list.add(tag2);
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllTags()
	 */
	@Override
	public List<Tag> getAllTags(Integer start, Integer count)
			throws IOException {
		List<Tag> list = getTags(null);
		list.add(this.helper.createTag("merge"));
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getNotifications(org.smartsnip.core.User,
	 *      boolean)
	 */
	@Override
	public List<Notification> getNotifications(User user, boolean unreadOnly)
			throws IOException {
		List<Notification> list = new ArrayList<Notification>();
		Notification not1 = this.helper.createNotification(1L, user,
				"Some obscure things happened.", false, "12. 3. 2012, 13.00",
				"unknown", null);
		list.add(not1);
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCodes(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Code> getCodes(Snippet snippet) throws IOException {
		List<Code> list = new ArrayList<Code>();
		list.add(staticCode);
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllCategories()
	 */
	@Override
	public List<Category> getAllCategories() throws IOException {
		List<Category> result = new ArrayList<Category>();
		result.add(getCategory("0"));
		for (Category cat : getSubcategories(getCategory("0"))) {
			result.add(cat);
		}
		return result;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategory(java.lang.String)
	 */
	@Override
	public Category getCategory(String name) throws IOException {
		return this.helper.createCategory("search", "Searching algorithms",
				null);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategory(org.smartsnip.core.Snippet)
	 */
	@Override
	public Category getCategory(Snippet snippet) throws IOException {
		return getCategory("0");
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getParentCategory(org.smartsnip.core.Category)
	 */
	@Override
	public Category getParentCategory(Category category) throws IOException {
		return this.helper.createCategory("search", "Searching algorithms",
				null);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSubcategories(org.smartsnip.core.Category)
	 */
	@Override
	public List<Category> getSubcategories(Category category)
			throws IOException {
		List<Category> list = new ArrayList<Category>();
		Category cat1 = this.helper.createCategory("arraysearch",
				"Searching algorithms for arrays", category);
		Category cat2 = this.helper.createCategory("listsearch",
				"Searching algorithms for list", category);
		list.add(cat1);
		list.add(cat2);
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllLanguages()
	 */
	@Override
	public List<String> getAllLanguages() throws IOException {
		List<String> list = new ArrayList<String>();
		list.add(new String("c"));
		list.add(new String("java"));
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getRatings(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Pair<User, Integer>> getRatings(Snippet snippet)
			throws IOException {
		List<Pair<User, Integer>> list = new ArrayList<Pair<User, Integer>>();
		list.add(new Pair<User, Integer>(staticUser1, 4));
		list.add(new Pair<User, Integer>(staticUser2, 3));
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAverageRating(org.smartsnip.core.Snippet)
	 */
	@Override
	public Float getAverageRating(Snippet snippet) throws IOException {
		return new Float(3.5);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getVotes(org.smartsnip.core.Comment)
	 */
	@Override
	public Pair<Integer, Integer> getVotes(Comment comment) throws IOException {
		return new Pair<Integer, Integer>(new Integer(7), new Integer(4));
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#search(java.lang.String, int,
	 *      int)
	 */
	@Override
	public List<Snippet> search(String searchString, Integer start,
			Integer count) throws IOException {
		return getUserSnippets(staticUser1);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserCount()
	 */
	@Override
	public int getUserCount() throws IOException {
		return 3;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategoryCount()
	 */
	@Override
	public int getCategoryCount() throws IOException {
		return 2;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippetsCount()
	 */
	@Override
	public int getSnippetsCount() throws IOException {
		return 4;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTagsCount()
	 */
	@Override
	public int getTagsCount() throws IOException {
		return 3;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#unRate(org.smartsnip.core.User, org.smartsnip.core.Snippet, int)
	 */
	@Override
	public void unRate(User user, Snippet snippet, int flags) throws IOException {
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeUser(org.smartsnip.core.User, int)
	 */
	@Override
	public void removeUser(User user, int flags) throws IOException {
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeSnippet(org.smartsnip.core.Snippet, int)
	 */
	@Override
	public void removeSnippet(Snippet snippet, int flags) throws IOException {
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeComment(org.smartsnip.core.Comment, int)
	 */
	@Override
	public void removeComment(Comment comment, int flags) throws IOException {
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeTag(org.smartsnip.core.Tag, int)
	 */
	@Override
	public void removeTag(Tag tag, int flags) throws IOException {
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeNotification(org.smartsnip.core.Notification, int)
	 */
	@Override
	public void removeNotification(Notification notification, int flags)
			throws IOException {
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeCode(org.smartsnip.core.Code, int)
	 */
	@Override
	public void removeCode(Code code, int flags) throws IOException {
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeCategory(org.smartsnip.core.Category, int)
	 */
	@Override
	public void removeCategory(Category category, int flags) throws IOException {
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeLanguage(java.lang.String, int)
	 */
	@Override
	public void removeLanguage(String language, int flags) throws IOException {
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getVote(org.smartsnip.core.User, org.smartsnip.core.Comment)
	 */
	@Override
	public Integer getVote(User user, Comment comment) throws IOException {
		return null; // no vote exists
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippet(java.lang.Long)
	 */
	@Override
	public Snippet getSnippet(Long id) throws IOException {
		return this.helper.createSnippet(id, this.staticUser1, "The Header",
				"Some Content", this.helper.createCategory("undefined",
						"Undefined Content", null), new ArrayList<Tag>(),
				new ArrayList<Comment>(), this.helper.createCode(1L,
						"/* There's nothing interesting to know.*/", "java",
						null, 0), "license free", 0);
	}
}
