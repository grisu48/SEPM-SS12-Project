/**
 * File: SqlPersistenceImpl.java
 * Date: 24.04.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.List;

import org.smartsnip.core.*;
import org.smartsnip.persistence.*;
import org.smartsnip.shared.Pair;

import sun.reflect.Reflection;

/**
 * @author littlelion
 * 
 */
public class SqlPersistenceImpl implements IPersistence {

	/**
	 * This constructor is protected against multiple instantiation to
	 * accomplish a singleton pattern. It rejects any attempt to build an
	 * instance except it is called by the
	 * {@link PersistenceFactory#getInstance(int)} method.
	 * 
	 * @throws IllegalAccessException
	 */
	protected SqlPersistenceImpl() throws IllegalAccessException {
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
		UserFactory.writeUser(user, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeUser(java.util.List,
	 *      int)
	 */
	@Override
	public void writeUser(List<User> users, int flags) throws IOException {
		UserFactory.writeUser(users, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writePassword(org.smartsnip.core.User,
	 *      String, int)
	 */
	@Override
	@Deprecated
	public void writePassword(User user, String password, int flags)
			throws IOException {
		writeLogin(user, password, false, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeLogin(org.smartsnip.core.User,
	 *      java.lang.String, java.lang.Boolean, int)
	 */
	@Override
	public void writeLogin(User user, String password, Boolean grantLogin,
			int flags) throws IOException {
		UserFactory.writeLogin(user, password, grantLogin, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	@Override
	public Long writeSnippet(Snippet snippet, int flags) throws IOException {
		return SnippetFactory.writeSnippet(snippet, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeSnippet(java.util.List,
	 *      int)
	 */
	@Override
	public void writeSnippet(List<Snippet> snippets, int flags)
			throws IOException {
		SnippetFactory.writeSnippet(snippets, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeComment(org.smartsnip.core.Comment,
	 *      int)
	 */
	@Override
	public Long writeComment(Comment comment, int flags) throws IOException {
		return CommentFactory.writeComment(comment, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeComment(java.util.List,
	 *      int)
	 */
	@Override
	public void writeComment(List<Comment> comments, int flags)
			throws IOException {
		CommentFactory.writeComment(comments, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(org.smartsnip.core.Tag,
	 *      int)
	 */
	@Override
	public void writeTag(Tag tag, int flags) throws IOException {
		TagFactory.writeTag(tag, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(java.util.List, int)
	 */
	@Override
	public void writeTag(List<Tag> tags, int flags) throws IOException {
		TagFactory.writeTag(tags, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeNotification(org.smartsnip.core.Notification,
	 *      int)
	 */
	@Override
	public Long writeNotification(Notification notification, int flags)
			throws IOException {
		return UserFactory.writeNotification(notification, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeNotification(java.util.List,
	 *      int)
	 */
	@Override
	public void writeNotification(List<Notification> notifications, int flags)
			throws IOException {
		UserFactory.writeNotification(notifications, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	@Override
	public Long writeCode(Code code, int flags) throws IOException {
		return SnippetFactory.writeCode(code, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCode(java.util.List,
	 *      int)
	 */
	@Override
	public void writeCode(List<Code> codes, int flags) throws IOException {
		SnippetFactory.writeCode(codes, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCategory(org.smartsnip.core.Category,
	 *      int)
	 */
	@Override
	public Long writeCategory(Category category, int flags) throws IOException {
		return CategoryFactory.writeCategory(category, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCategory(java.util.List,
	 *      int)
	 */
	@Override
	public void writeCategory(List<Category> categories, int flags)
			throws IOException {
		CategoryFactory.writeCategory(categories, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeLanguage(java.lang.String,
	 *      int)
	 */
	@Override
	public void writeLanguage(String language, int flags) throws IOException {
		SnippetFactory.writeLanguage(language, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeRating(java.lang.Integer,
	 *      org.smartsnip.core.Snippet, org.smartsnip.core.User, int)
	 */
	@Override
	public void writeRating(Integer rating, Snippet snippet, User user,
			int flags) throws IOException {
		SnippetFactory.writeRating(rating, snippet, user, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#unRate(org.smartsnip.core.User,
	 *      org.smartsnip.core.Snippet, int)
	 */
	@Override
	public void unRate(User user, Snippet snippet, int flags)
			throws IOException {
		SnippetFactory.unRate(user, snippet, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeVote(java.lang.Integer,
	 *      org.smartsnip.core.Comment, org.smartsnip.core.User, int)
	 */
	@Override
	public void writeVote(Integer vote, Comment comment, User user, int flags)
			throws IOException {
		CommentFactory.writeVote(vote, comment, user, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#votePositive(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void votePositive(User user, Comment comment, int flags)
			throws IOException {
		CommentFactory.votePositive(user, comment, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#voteNegative(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void voteNegative(User user, Comment comment, int flags)
			throws IOException {
		CommentFactory.voteNegative(user, comment, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#unVote(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void unVote(User user, Comment comment, int flags)
			throws IOException {
		CommentFactory.unVote(user, comment, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#addFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	@Override
	public void addFavourite(Snippet snippet, User user, int flags)
			throws IOException {
		SnippetFactory.addFavourite(snippet, user, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	@Override
	public void removeFavourite(Snippet snippet, User user, int flags)
			throws IOException {
		SnippetFactory.removeFavourite(snippet, user, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeUser(org.smartsnip.core.User,
	 *      int)
	 */
	@Override
	public void removeUser(User user, int flags) throws IOException {
		UserFactory.removeUser(user, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeLogin(org.smartsnip.core.User,
	 *      int)
	 */
	@Override
	public void removeLogin(User user, int flags) throws IOException {
		UserFactory.removeLogin(user, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	@Override
	public void removeSnippet(Snippet snippet, int flags) throws IOException {
		SnippetFactory.removeSnippet(snippet, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeComment(org.smartsnip.core.Comment,
	 *      int)
	 */
	@Override
	public void removeComment(Comment comment, int flags) throws IOException {
		CommentFactory.removeComment(comment, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeTag(org.smartsnip.core.Tag,
	 *      int)
	 */
	@Override
	public void removeTag(Tag tag, int flags) throws IOException {
		TagFactory.removeTag(tag, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeNotification(org.smartsnip.core.Notification,
	 *      int)
	 */
	@Override
	public void removeNotification(Notification notification, int flags)
			throws IOException {
		UserFactory.removeNotification(notification, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	@Override
	public void removeCode(Code code, int flags) throws IOException {
		SnippetFactory.removeCode(code, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeCategory(org.smartsnip.core.Category,
	 *      int)
	 */
	@Override
	public void removeCategory(Category category, int flags) throws IOException {
		CategoryFactory.removeCategory(category, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeLanguage(java.lang.String,
	 *      int)
	 */
	@Override
	public void removeLanguage(String language, int flags) throws IOException {
		SnippetFactory.removeLanguage(language, flags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String nick) throws IOException {
		return UserFactory.getUser(nick);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserByEmail(java.lang.String)
	 */
	@Override
	public User getUserByEmail(String email) throws IOException {
		return UserFactory.getUserByEmail(email);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getPassword(org.smartsnip.core.User)
	 */
	@Override
	@Deprecated
	public String getPassword(User user) throws IOException,
			UnsupportedOperationException {
		throw new UnsupportedOperationException(
				"This operation is deprecated, use verifyPassword instead.");
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#verifyPassword(org.smartsnip.core.User,
	 *      java.lang.String)
	 */
	@Override
	public boolean verifyPassword(User user, String password)
			throws IOException {
		return UserFactory.verifyPassword(user, password);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#isLoginGranted(org.smartsnip.core.User)
	 */
	// @Override
	public boolean isLoginGranted(User user) throws IOException {
		return UserFactory.isLoginGranted(user);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#findUser(java.lang.String)
	 */
	@Override
	public List<User> findUser(String realName) throws IOException {
		return UserFactory.findUser(realName);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserSnippets(org.smartsnip.core.User)
	 */
	@Override
	public List<Snippet> getUserSnippets(User owner) throws IOException {
		return SnippetFactory.getUserSnippets(owner);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getFavorited(org.smartsnip.core.User)
	 */
	@Override
	public List<Snippet> getFavorited(User user) throws IOException {
		return SnippetFactory.getFavorited(user);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(java.util.List)
	 */
	@Override
	public List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException {
		return SnippetFactory.getSnippets(matchingTags);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(org.smartsnip.core.Category)
	 */
	@Override
	public List<Snippet> getSnippets(Category category) throws IOException {
		return SnippetFactory.getSnippets(category, null, null);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(org.smartsnip.core.Category,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Snippet> getSnippets(Category category, Integer start,
			Integer count) throws IOException {
		return SnippetFactory.getSnippets(category, start, count);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippet(java.lang.Long)
	 */
	@Override
	public Snippet getSnippet(Long id) throws IOException {
		return SnippetFactory.getSnippet(id);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getComment(java.lang.Long)
	 */
	@Override
	public Comment getComment(Long id) throws IOException {
		return CommentFactory.getComment(id);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getComments(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Comment> getComments(Snippet snippet) throws IOException {
		return CommentFactory.getComments(snippet);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTags(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Tag> getTags(Snippet snippet) throws IOException {
		return TagFactory.getTags(snippet);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllTags(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public List<Tag> getAllTags(Integer start, Integer count)
			throws IOException {
		return TagFactory.getAllTags(start, count);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getNotifications(org.smartsnip.core.User,
	 *      boolean)
	 */
	@Override
	public List<Notification> getNotifications(User user, boolean unreadOnly)
			throws IOException {
		return UserFactory.getNotifications(user, unreadOnly);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCodes(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Code> getCodes(Snippet snippet) throws IOException {
		return SnippetFactory.getCodes(snippet);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllCategories()
	 */
	@Override
	public List<Category> getAllCategories() throws IOException {
		return CategoryFactory.getAllCategories();
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategory(org.smartsnip.core.Snippet)
	 */
	@Override
	public Category getCategory(Snippet snippet) throws IOException {
		return CategoryFactory.getCategory(snippet);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategory(java.lang.String)
	 */
	@Override
	public Category getCategory(String name) throws IOException {
		return CategoryFactory.getCategory(name);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getParentCategory(org.smartsnip.core.Category)
	 */
	@Override
	public Category getParentCategory(Category category) throws IOException {
		return CategoryFactory.getParentCategory(category);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSubcategories(org.smartsnip.core.Category)
	 */
	@Override
	public List<Category> getSubcategories(Category category)
			throws IOException {
		return CategoryFactory.getSubcategories(category);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllLanguages()
	 */
	@Override
	public List<String> getAllLanguages() throws IOException {
		return SnippetFactory.getAllLanguages();
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getRatings(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Pair<User, Integer>> getRatings(Snippet snippet)
			throws IOException {
		return SnippetFactory.getRatings(snippet);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAverageRating(org.smartsnip.core.Snippet)
	 */
	@Override
	public Float getAverageRating(Snippet snippet) throws IOException {
		return SnippetFactory.getAverageRating(snippet);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getVotes(org.smartsnip.core.Comment)
	 */
	@Override
	public Pair<Integer, Integer> getVotes(Comment comment) throws IOException {
		return CommentFactory.getVotes(comment);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getVote(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment)
	 */
	@Override
	public Integer getVote(User user, Comment comment) throws IOException {
		return CommentFactory.getVote(user, comment);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#search(java.lang.String,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Snippet> search(String searchString, Integer start,
			Integer count) throws IOException {
		return SnippetFactory.search(searchString, start, count);
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserCount()
	 */
	@Override
	public int getUserCount() throws IOException {
		return UserFactory.getUserCount();
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategoryCount()
	 */
	@Override
	public int getCategoryCount() throws IOException {
		return CategoryFactory.getCategoryCount();
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippetsCount()
	 */
	@Override
	public int getSnippetsCount() throws IOException {
		return SnippetFactory.getSnippetsCount();
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTagsCount()
	 */
	@Override
	public int getTagsCount() throws IOException {
		return TagFactory.getTagsCount();
	}

	@Override
	public void close() throws IOException {
		try {
			DBSessionFactory.closeFactory();
		} catch (RuntimeException e) {
			throw new IOException(e);
		}
	}
}
