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
	 * This constructor is protected against multiple instantiation to accomplish a singleton pattern.
	 * It rejects any attempt to build an instance except it is called by the {@link PersistenceFactory#getInstance(int)} method.
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
	public void writeUser(User user, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeUser(java.util.List,
	 *      int)
	 */
	@Override
	public void writeUser(List<User> users, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writePassword(org.smartsnip.core.Pair,
	 *      int)
	 */
	@Override
	public void writePassword(User user, String password, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	@Override
	public Long writeSnippet(Snippet snippet, int mode) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeSnippet(java.util.List,
	 *      int)
	 */
	@Override
	public void writeSnippet(List<Snippet> snippets, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeComment(org.smartsnip.core.Comment,
	 *      int)
	 */
	@Override
	public Long writeComment(Comment comment, int mode) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeComment(java.util.List,
	 *      int)
	 */
	@Override
	public void writeComment(List<Comment> comments, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(org.smartsnip.core.Tag,
	 *      int)
	 */
	@Override
	public void writeTag(Tag tag, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(java.util.List, int)
	 */
	@Override
	public void writeTag(List<Tag> tags, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeNotification(org.smartsnip.core.Notification,
	 *      int)
	 */
	@Override
	public Long writeNotification(Notification notification, int mode) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeNotification(java.util.List,
	 *      int)
	 */
	@Override
	public void writeNotification(List<Notification> notifications, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	@Override
	public Long writeCode(Code code, int mode) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCode(java.util.List,
	 *      int)
	 */
	@Override
	public void writeCode(List<Code> codes, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCategory(org.smartsnip.core.Category,
	 *      int)
	 */
	@Override
	public Long writeCategory(Category category, int mode) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeCategory(java.util.List,
	 *      int)
	 */
	@Override
	public void writeCategory(List<Category> categories, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeLanguage(java.lang.String,
	 *      int)
	 */
	@Override
	public void writeLanguage(String language, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeRating(java.lang.Integer,
	 *      org.smartsnip.core.Snippet, org.smartsnip.core.User, int)
	 */
	@Override
	public void writeRating(Integer rating, Snippet snippet, User user, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#unRate(org.smartsnip.core.User, org.smartsnip.core.Snippet, int)
	 */
	@Override
	public void unRate(User user, Snippet snippet, int mode) throws IOException {
		// TODO Auto-generated method stub
	}
	
	/**
	 * @see org.smartsnip.persistence.IPersistence#writeVote(java.lang.Integer,
	 *      org.smartsnip.core.Comment, org.smartsnip.core.User, int)
	 */
	@Override
	public void writeVote(Integer vote, Comment comment, User user, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#votePositive(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void votePositive(User user, Comment comment, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#voteNegative(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void voteNegative(User user, Comment comment, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#unVote(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	@Override
	public void unVote(User user, Comment comment, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#addFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	@Override
	public void addFavourite(Snippet snippet, User user, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	@Override
	public void removeFavourite(Snippet snippet, User user, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeUser(java.lang.String, int)
	 */
	@Override
	public void removeUser(String nickname, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeSnippet(java.lang.Long, int)
	 */
	@Override
	public void removeSnippet(Long snippetId, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeComment(java.lang.Long, int)
	 */
	@Override
	public void removeComment(Long commentId, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeTag(org.smartsnip.core.Tag, int)
	 */
	@Override
	public void removeTag(Tag tag, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeNotification(java.lang.Long, int)
	 */
	@Override
	public void removeNotification(Long notificationId, int mode)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeReadNotifications(org.smartsnip.core.User, int)
	 */
	@Override
	public void removeReadNotifications(User user, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeCode(java.lang.Long, int)
	 */
	@Override
	public void removeCode(Long codeId, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeCategory(java.lang.Long, int)
	 */
	@Override
	public void removeCategory(Long categoryId, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeLanguage(java.lang.String, int)
	 */
	@Override
	public void removeLanguage(String language, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String nick) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserByEmail(java.lang.String)
	 */
	@Override
	public User getUserByEmail(String email) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getPassword(org.smartsnip.core.User)
	 */
	@Override
	public String getPassword(User user) throws IOException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#verifyPassword(org.smartsnip.core.User,
	 *      java.lang.String)
	 */
	@Override
	public boolean verifyPassword(User user, String password) throws IOException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#findUser(java.lang.String)
	 */
	@Override
	public List<User> findUser(String realName) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserSnippets(org.smartsnip.core.User)
	 */
	@Override
	public List<Snippet> getUserSnippets(User owner) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getFavorited(org.smartsnip.core.User)
	 */
	@Override
	public List<Snippet> getFavorited(User user) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(java.util.List)
	 */
	@Override
	public List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(org.smartsnip.core.Category)
	 */
	@Override
	public List<Snippet> getSnippets(Category category) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippets(org.smartsnip.core.Category, int, int)
	 */
	@Override
	public List<Snippet> getSnippets(Category category, int start, int count)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getComment(long)
	 */
	@Override
	public Comment getComment(Long id) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getComments(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Comment> getComments(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTags(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Tag> getTags(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllTags()
	 */
	@Override
	public List<Tag> getAllTags() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getNotifications(org.smartsnip.core.User,
	 *      boolean)
	 */
	@Override
	public List<Notification> getNotifications(User user, boolean unreadOnly) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCodes(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Code> getCodes(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllCategories()
	 */
	@Override
	public List<String> getAllCategories() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategory(org.smartsnip.core.Snippet)
	 */
	@Override
	public Category getCategory(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategory(java.lang.String)
	 */
	@Override
	public Category getCategory(String name) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getParentCategory(org.smartsnip.core.Category)
	 */
	@Override
	public Category getParentCategory(Category category) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSubcategories(org.smartsnip.core.Category)
	 */
	@Override
	public List<Category> getSubcategories(Category category) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSubcategoryNames(org.smartsnip.core.Category)
	 */
	@Override
	public List<String> getSubcategoryNames(Category category)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllLanguages()
	 */
	@Override
	public List<String> getAllLanguages() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getRatings(org.smartsnip.core.Snippet)
	 */
	@Override
	public List<Pair<User, Integer>> getRatings(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAverageRating(org.smartsnip.core.Snippet)
	 */
	@Override
	public Float getAverageRating(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getVotes(org.smartsnip.core.Comment)
	 */
	@Override
	public Pair<Integer, Integer> getVotes(Comment comment) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getVote(org.smartsnip.core.User, org.smartsnip.core.Comment)
	 */
	@Override
	public Integer getVote(User user, Comment comment) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#search(java.lang.String, int,
	 *      int)
	 */
	@Override
	public List<Snippet> search(String searchString, int min, int max) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getUserCount()
	 */
	@Override
	public int getUserCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getCategoryCount()
	 */
	@Override
	public int getCategoryCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getSnippetsCount()
	 */
	@Override
	public int getSnippetsCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTagsCount()
	 */
	@Override
	public int getTagsCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTagFrequency(org.smartsnip.core.Tag)
	 */
	@Override
	public int getTagFrequency(Tag tag) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
}