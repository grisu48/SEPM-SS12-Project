/**
 * File: SqlPersistenceTest.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.smartsnip.core.Category;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.persistence.IPersistence;
import org.smartsnip.persistence.PersistenceFactory;

/**
 * @author littlelion
 *
 */
public class SqlPersistenceTest {

	private static IPersistence instance;
	private static SqlPersistenceHelper helper;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_SQL_DB);
		instance = PersistenceFactory.getInstance();
		if (PersistenceFactory.getPersistenceType() != PersistenceFactory.PERSIST_SQL_DB) {
			throw new InitializationError("persistence type not PERSIST_SQL_DB");
		}
		helper = new SqlPersistenceHelper();
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeUser(org.smartsnip.core.User, int)}.
	 * @throws Throwable 
	 */
	@Test
	public void testWriteUserUserInt() throws Throwable {
		User user = helper.createUser("flex", "Felix Niederwanger", "flex@feldspaten.org", User.UserState.validated, null);
		instance.writeUser(user, IPersistence.DB_NEW_ONLY);
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeUser(java.util.List, int)}.
	 */
	@Ignore
	public void testWriteUserListOfUserInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writePassword(org.smartsnip.core.User, java.lang.String, int)}.
	 */
	@Ignore
	public void testWritePassword() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeSnippet(org.smartsnip.core.Snippet, int)}.
	 */
	@Ignore
	public void testWriteSnippetSnippetInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeSnippet(java.util.List, int)}.
	 */
	@Ignore
	public void testWriteSnippetListOfSnippetInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeComment(org.smartsnip.core.Comment, int)}.
	 */
	@Ignore
	public void testWriteCommentCommentInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeComment(java.util.List, int)}.
	 */
	@Ignore
	public void testWriteCommentListOfCommentInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeTag(org.smartsnip.core.Tag, int)}.
	 */
	@Ignore
	public void testWriteTagTagInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeTag(java.util.List, int)}.
	 */
	@Ignore
	public void testWriteTagListOfTagInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeNotification(org.smartsnip.core.Notification, int)}.
	 */
	@Ignore
	public void testWriteNotificationNotificationInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeNotification(java.util.List, int)}.
	 */
	@Ignore
	public void testWriteNotificationListOfNotificationInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(org.smartsnip.core.Code, int)}.
	 */
	@Ignore
	public void testWriteCodeCodeInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(java.util.List, int)}.
	 */
	@Ignore
	public void testWriteCodeListOfCodeInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCategory(org.smartsnip.core.Category, int)}.
	 */
	@Ignore
	public void testWriteCategoryCategoryInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCategory(java.util.List, int)}.
	 */
	@Ignore
	public void testWriteCategoryListOfCategoryInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeLanguage(java.lang.String, int)}.
	 */
	@Ignore
	public void testWriteLanguage() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeRating(java.lang.Integer, org.smartsnip.core.Snippet, org.smartsnip.core.User, int)}.
	 */
	@Ignore
	public void testWriteRating() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#unRate(org.smartsnip.core.User, org.smartsnip.core.Snippet, int)}.
	 */
	@Ignore
	public void testUnRate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeVote(java.lang.Integer, org.smartsnip.core.Comment, org.smartsnip.core.User, int)}.
	 */
	@Ignore
	public void testWriteVote() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#votePositive(org.smartsnip.core.User, org.smartsnip.core.Comment, int)}.
	 */
	@Ignore
	public void testVotePositive() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#voteNegative(org.smartsnip.core.User, org.smartsnip.core.Comment, int)}.
	 */
	@Ignore
	public void testVoteNegative() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#unVote(org.smartsnip.core.User, org.smartsnip.core.Comment, int)}.
	 */
	@Ignore
	public void testUnVote() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#addFavourite(org.smartsnip.core.Snippet, org.smartsnip.core.User, int)}.
	 */
	@Ignore
	public void testAddFavourite() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeFavourite(org.smartsnip.core.Snippet, org.smartsnip.core.User, int)}.
	 */
	@Ignore
	public void testRemoveFavourite() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeUser(org.smartsnip.core.User, int)}.
	 */
	@Ignore
	public void testRemoveUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeSnippet(org.smartsnip.core.Snippet, int)}.
	 */
	@Ignore
	public void testRemoveSnippet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeComment(org.smartsnip.core.Comment, int)}.
	 */
	@Ignore
	public void testRemoveComment() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeTag(org.smartsnip.core.Tag, int)}.
	 */
	@Ignore
	public void testRemoveTag() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeNotification(org.smartsnip.core.Notification, int)}.
	 */
	@Ignore
	public void testRemoveNotification() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeCode(org.smartsnip.core.Code, int)}.
	 */
	@Ignore
	public void testRemoveCode() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeCategory(org.smartsnip.core.Category, int)}.
	 */
	@Ignore
	public void testRemoveCategory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeLanguage(java.lang.String, int)}.
	 */
	@Ignore
	public void testRemoveLanguage() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUser(java.lang.String)}.
	 */
	@Ignore
	public void testGetUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserByEmail(java.lang.String)}.
	 */
	@Ignore
	public void testGetUserByEmail() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getPassword(org.smartsnip.core.User)}.
	 */
	@Ignore
	public void testGetPassword() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#verifyPassword(org.smartsnip.core.User, java.lang.String)}.
	 */
	@Ignore
	public void testVerifyPassword() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#findUser(java.lang.String)}.
	 */
	@Ignore
	public void testFindUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserSnippets(org.smartsnip.core.User)}.
	 */
	@Ignore
	public void testGetUserSnippets() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getFavorited(org.smartsnip.core.User)}.
	 */
	@Ignore
	public void testGetFavorited() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(java.util.List)}.
	 */
	@Ignore
	public void testGetSnippetsListOfTag() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(org.smartsnip.core.Category)}.
	 */
	@Ignore
	public void testGetSnippetsCategory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(org.smartsnip.core.Category, java.lang.Integer, java.lang.Integer)}.
	 * @throws Throwable 
	 */
	@Test
	public void testGetSnippetsCategoryIntegerInteger() throws Throwable {
		List<Snippet> snips = instance.getSnippets(null, null, null);
		int i = 0;
		for (Snippet s: snips) {
			System.out.println(s);
			++i;
		}
		System.out.println("total: " + i);
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippet(java.lang.Long)}.
	 */
	@Ignore
	public void testGetSnippet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getComment(java.lang.Long)}.
	 */
	@Ignore
	public void testGetComment() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getComments(org.smartsnip.core.Snippet)}.
	 */
	@Ignore
	public void testGetComments() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getTags(org.smartsnip.core.Snippet)}.
	 */
	@Ignore
	public void testGetTags() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllTags(java.lang.Integer, java.lang.Integer)}.
	 * @throws Throwable 
	 */
	@Ignore
	public void testGetAllTags() throws Throwable {
		List<Tag> tags = instance.getAllTags(null, null);
		int i = 0;
		for (Tag t: tags) {
			System.out.println(t);
			++i;
		}
		System.out.println("total: " + i);
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getNotifications(org.smartsnip.core.User, boolean)}.
	 */
	@Ignore
	public void testGetNotifications() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCodes(org.smartsnip.core.Snippet)}.
	 */
	@Ignore
	public void testGetCodes() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllCategories()}.
	 */
	@Ignore
	public void testGetAllCategories() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCategory(org.smartsnip.core.Snippet)}.
	 */
	@Ignore
	public void testGetCategorySnippet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCategory(java.lang.String)}.
	 */
	@Ignore
	public void testGetCategoryString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getParentCategory(org.smartsnip.core.Category)}.
	 */
	@Ignore
	public void testGetParentCategory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSubcategories(org.smartsnip.core.Category)}.
	 */
	@Ignore
	public void testGetSubcategories() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllLanguages()}.
	 */
	@Ignore
	public void testGetAllLanguages() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getRatings(org.smartsnip.core.Snippet)}.
	 */
	@Ignore
	public void testGetRatings() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAverageRating(org.smartsnip.core.Snippet)}.
	 */
	@Ignore
	public void testGetAverageRating() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getVotes(org.smartsnip.core.Comment)}.
	 */
	@Ignore
	public void testGetVotes() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getVote(org.smartsnip.core.User, org.smartsnip.core.Comment)}.
	 */
	@Ignore
	public void testGetVote() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#search(java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	@Ignore
	public void testSearch() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserCount()}.
	 */
	@Ignore
	public void testGetUserCount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCategoryCount()}.
	 */
	@Ignore
	public void testGetCategoryCount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippetsCount()}.
	 */
	@Ignore
	public void testGetSnippetsCount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getTagsCount()}.
	 */
	@Ignore
	public void testGetTagsCount() {
		fail("Not yet implemented");
	}

}
