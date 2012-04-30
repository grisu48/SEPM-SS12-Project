/**
 * File: BlackholePersistence.java
 * Date: 26.04.2012
 */
package org.smartsnip.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.*;

import sun.reflect.Reflection;

/**
 * Implementation of the persistence layer. This class provides minimal database
 * functionality. It stores no data and provides only predefined datasets.
 * 
 * @author littlelion
 * 
 */
public class BlackholePersistence implements IPersistence {

	private static User staticUser1 = User.createNewUser("nobody", "blabla", "nobody@anonymus.org");
	private static User staticUser2 = User.createNewUser("bin_da", "asdfgh", "bd@finger.net");
	private static Code staticCode = Code.createCode("/* There's nothing interesting to know.*/", "java", null);
			
	/**
	 * This constructor is protected against multiple instantiation to accomplish a singleton pattern.
	 * It rejects any attempt to build an instance except it is called by the {@link PersistenceFactory#getInstance(int)} method.
	 */
	BlackholePersistence() throws IllegalAccessException {
		super();
		if (Reflection.getCallerClass(2) == null
				|| Reflection.getCallerClass(2) != PersistenceFactory.class) {
			throw new IllegalAccessException(
					"Singleton pattern: caller must be PersistenceFactory class.");
		}
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeUser(org.smartsnip.core.User,
	 *      int)
	 */
	@Override
	public void writeUser(User user, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeUser(java.util.List,
	 *      int)
	 */
	@Override
	public void writeUser(List<User> users, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writePassword(org.smartsnip.core.User,
	 *      java.lang.String, int)
	 */
	@Override
	public void writePassword(User user, String password, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	@Override
	public void writeSnippet(Snippet snippet, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeSnippet(java.util.List,
	 *      int)
	 */
	@Override
	public void writeSnippet(List<Snippet> snippets, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeComment(org.smartsnip.core.Comment,
	 *      int)
	 */
	@Override
	public void writeComment(Comment comment, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeComment(java.util.List,
	 *      int)
	 */
	@Override
	public void writeComment(List<Comment> comments, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(org.smartsnip.core.Tag,
	 *      int)
	 */
	@Override
	public void writeTag(Tag tag, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(java.util.List, int)
	 */
	@Override
	public void writeTag(List<Tag> tags, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeNotification(org.smartsnip.core.Notification,
	 *      int)
	 */
	@Override
	public void writeNotification(Notification notification, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeNotification(java.util.List,
	 *      int)
	 */
	@Override
	public void writeNotification(List<Notification> notifications, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	@Override
	public void writeCode(Code code, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCode(java.util.List,
	 *      int)
	 */
	@Override
	public void writeCode(List<Code> codes, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCategory(org.smartsnip.core.Category,
	 *      int)
	 */
	@Override
	public void writeCategory(Category category, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCategory(java.util.List,
	 *      int)
	 */
	@Override
	public void writeCategory(List<Category> categories, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeLanguage(java.lang.String,
	 *      int)
	 */
	@Override
	public void writeLanguage(String language, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeRating(java.lang.Integer,
	 *      org.smartsnip.core.Snippet, org.smartsnip.core.User, int)
	 */
	@Override
	public void writeRating(Integer rating, Snippet snippet, User user, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeVote(java.lang.Integer,
	 *      org.smartsnip.core.Comment, org.smartsnip.core.User, int)
	 */
	@Override
	public void writeVote(Integer vote, Comment comment, User user, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#votePositive(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void votePositive(User user, Comment comment, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#voteNegative(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void voteNegative(User user, Comment comment, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#unVote(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void unVote(User user, Comment comment, int mode) throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#addFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	@Override
	public void addFavourite(Snippet snippet, User user, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	@Override
	public void removeFavourite(Snippet snippet, User user, int mode)
			throws IOException {
		// do nothing -> data vanish in the black hole!
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String nick) throws IOException {
		User result = User.createNewUser(nick, "blabla", nick + "@anonymus.org");
		return result;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserByEmail(java.lang.String)
	 */
	@Override
	public User getUserByEmail(String email) throws IOException {
		User result = User.createNewUser("nobody", "blabla", email);
		return result;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getPassword(org.smartsnip.core.User)
	 */
	@Override
	public String getPassword(User user) throws IOException,
			UnsupportedOperationException {
		throw new UnsupportedOperationException("Use method verifyPassword(User, String) instead.");
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
		list.add(User.createNewUser("nobody", "blabla", "nobody@anonymus.org"));
		list.add(User.createNewUser("bin_da", "asdfgh", "bd@finger.net"));
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserSnippets(org.smartsnip.core.User)
	 */
	@Override
	public List<Snippet> getUserSnippets(User owner) throws IOException {
		List<Snippet> snips = new ArrayList<Snippet>();
		Snippet snip = Snippet.createSnippet(owner, "Some Content", "This is an example entry", null, 0);
		Code code = Code.createCode("/* There's nothing interesting to know.*/", "java", snip);
		snip.setCode(code);
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
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(org.smartsnip.core.Category, int, int)
	 */
	@Override
	public List<Snippet> getSnippets(Category category, int start, int count)
			throws IOException {
		return getSnippets(category);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getComments(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Comment> getComments(Snippet snippet) throws IOException {
		List<Comment> list = new ArrayList<Comment>();
		Comment comm1 = Comment.createComment(staticUser1, snippet, "commented by nobody");
		Comment comm2 = Comment.createComment(staticUser2, snippet, "commented by bin_da");
		list.add(comm1);
		list.add(comm2);
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getComment(long)
	 */
	@Override
	public Comment getComment(long id) throws IOException {
		return getComments(null).get(0);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTags(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Tag> getTags(Snippet snippet) throws IOException {
		List<Tag> list = new ArrayList<Tag>();
		Tag tag1 = Tag.createTag("java");
		Tag tag2 = Tag.createTag("sort");
		list.add(tag1);
		list.add(tag2);
		return list;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllTags()
	 */
	@Override
	public List<Tag> getAllTags() throws IOException {
		List<Tag> list = getTags(null);
		list.add(Tag.createTag("merge"));
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
		Notification not1 = new Notification();
		not1.markUnread();
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
	public List<String> getAllCategories() throws IOException {
		List<String> result = new ArrayList<String>();
		result.add(getCategory("0").getName());
		for(Category cat: getSubcategories(getCategory("0"))) {
			result.add(cat.getName());
		}
		return result;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategory(java.lang.String)
	 */
	@Override
	public Category getCategory(String name) throws IOException {
		return Category.createCategory("search", "Searching algorithms", null);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSubcategoryNames(org.smartsnip.core.Category)
	 */
	@Override
	public List<String> getSubcategoryNames(Category category)
			throws IOException {
		List<String> result = new ArrayList<String>();
		for (Category cat: getSubcategories(category)) {
			result.add(cat.getName());
		}
		return result;
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
		return Category.createCategory("search", "Searching algorithms", null);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSubcategories(org.smartsnip.core.Category)
	 */
	@Override
	public List<Category> getSubcategories(Category category)
			throws IOException {
		List<Category> list = new ArrayList<Category>();
		Category cat1 = Category.createCategory("arraysearch", "Searching algorithms for arrays", category);
		Category cat2 = Category.createCategory("listsearch", "Searching algorithms for list", category);
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
		List<Pair<User, Integer>> list = new ArrayList<Pair<User,Integer>>();
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
	public List<Snippet> search(String searchString, int min, int max)
			throws IOException {
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
}
