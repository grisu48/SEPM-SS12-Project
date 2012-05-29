/**
 * File: SqlPersistenceImplTest.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.smartsnip.core.Category;
import org.smartsnip.core.Code;
import org.smartsnip.core.Notification;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.persistence.IPersistence;
import org.smartsnip.persistence.PersistenceFactory;

/**
 * @author littlelion
 * 
 */
public class SqlPersistenceImplTest {

	private static IPersistence instance;
	private static SqlPersistenceHelper helper;
	private static Validator validator;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
		
		PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_SQL_DB);
		instance = PersistenceFactory.getInstance();
		if (PersistenceFactory.getPersistenceType() != PersistenceFactory.PERSIST_SQL_DB) {
			throw new InitializationError("persistence type not PERSIST_SQL_DB");
		}
		helper = new SqlPersistenceHelper();
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeUser(org.smartsnip.core.User, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testWriteUserUserInt() throws Throwable {
		User user = helper.createUser("si", "she ra", "sie@bla", null);
		instance.writeUser(user, IPersistence.DB_DEFAULT);
		
		Set<ConstraintViolation<User>> constraintViolations =
	            validator.validate(user);
		for(Iterator<ConstraintViolation<User>> itr = constraintViolations.iterator(); itr.hasNext();) {
			System.out.println("Constraint Violation: " + itr.next().getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeUser(java.util.List, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testWriteUserListOfUserInt() throws Throwable {
		List<User> users = new ArrayList<User>();
		users.add(helper.createUser("sick", "sick guy", "sick@guy.org", null));
		users.add(helper.createUser("sie", "she bang", "she@bang", null));
		users.add(helper.createUser("he", "he man", "er@bla", null));
		users.add(helper.createUser("er", "erbie", "he@bla", null));
		users.add(helper.createUser("xxx", "anonymus", "anon@ym", null));
		instance.writeUser(users, IPersistence.DB_DEFAULT);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writePassword(org.smartsnip.core.User, java.lang.String, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWritePassword() throws Throwable {
		// deprecated
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeLogin(User, String, Boolean, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testWriteLogin() throws Throwable {
		User user = helper.createUser("pwdTester", "aaa", "bbb@ccc.dd", User.UserState.validated);
		instance.writeUser(user, IPersistence.DB_DEFAULT);	
		instance.writeLogin(user, "blabla", true, IPersistence.DB_DEFAULT);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeSnippet(org.smartsnip.core.Snippet, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteSnippetSnippetInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeSnippet(java.util.List, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteSnippetListOfSnippetInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeComment(org.smartsnip.core.Comment, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteCommentCommentInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeComment(java.util.List, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteCommentListOfCommentInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeTag(org.smartsnip.core.Tag, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteTagTagInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeTag(java.util.List, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteTagListOfTagInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeNotification(org.smartsnip.core.Notification, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testWriteNotificationNotificationInt() throws Throwable {
		User user = helper.createUser("si", "she ra", "sie@bla", null);
		Category par = helper.createCategory("bla", "viel bla bla", null);
		Category cat = helper.createCategory("blabla", "noch mehr bla bla", "bla");
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(helper.createTag("xxx"));
		tags.add(helper.createTag("bbb"));
		tags.add(helper.createTag("new"));
		tags.add(helper.createTag("ddd"));
		tags.add(helper.createTag("old"));
		Snippet snip = helper.createSnippet(5L, user.getUsername(), "something", "something stupid stuff", cat.getName(), tags, null, null, 0);
		Code code = helper.createCode(1L, "code", "language", snip, 0);
		snip.setCodeWithoutWriting(code);
		instance.writeTag(tags, IPersistence.DB_DEFAULT);
		instance.writeCategory(par, IPersistence.DB_DEFAULT);
		instance.writeCategory(cat, IPersistence.DB_DEFAULT);
		Long snipId = instance.writeSnippet(snip, IPersistence.DB_DEFAULT);
		snip.id = snipId;
		Notification notif = helper.createNotification(22L, user, "notif", false, "now", "source", snip);
		instance.writeNotification(notif, IPersistence.DB_DEFAULT);
		System.out.println("Snippet Id = " + snipId);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeNotification(java.util.List, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteNotificationListOfNotificationInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(org.smartsnip.core.Code, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteCodeCodeInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(java.util.List, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteCodeListOfCodeInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCategory(org.smartsnip.core.Category, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteCategoryCategoryInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCategory(java.util.List, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteCategoryListOfCategoryInt() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeLanguage(java.lang.String, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteLanguage() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeRating(java.lang.Integer, org.smartsnip.core.Snippet, org.smartsnip.core.User, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteRating() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#unRate(org.smartsnip.core.User, org.smartsnip.core.Snippet, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testUnRate() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeVote(java.lang.Integer, org.smartsnip.core.Comment, org.smartsnip.core.User, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testWriteVote() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#votePositive(org.smartsnip.core.User, org.smartsnip.core.Comment, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testVotePositive() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#voteNegative(org.smartsnip.core.User, org.smartsnip.core.Comment, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testVoteNegative() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#unVote(org.smartsnip.core.User, org.smartsnip.core.Comment, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testUnVote() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#addFavourite(org.smartsnip.core.Snippet, org.smartsnip.core.User, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testAddFavourite() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeFavourite(org.smartsnip.core.Snippet, org.smartsnip.core.User, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveFavourite() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeUser(org.smartsnip.core.User, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveUser() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeLogin(User, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveLogin() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeSnippet(org.smartsnip.core.Snippet, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveSnippet() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeComment(org.smartsnip.core.Comment, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveComment() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeTag(org.smartsnip.core.Tag, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveTag() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeNotification(org.smartsnip.core.Notification, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveNotification() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeCode(org.smartsnip.core.Code, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveCode() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeCategory(org.smartsnip.core.Category, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveCategory() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeLanguage(java.lang.String, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testRemoveLanguage() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUser(java.lang.String)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetUser() throws Throwable {
		User user = instance.getUser("si");
		System.out.println(user.getUsername());
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserByEmail(java.lang.String)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetUserByEmail() throws Throwable {
		User user = instance.getUserByEmail("sie@bla");
		System.out.println(user.getUsername());
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getPassword(org.smartsnip.core.User)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testGetPassword() throws Throwable {
		try{
			instance.getPassword(null);
			fail("UnsupportedOperationException expected");
		} catch (UnsupportedOperationException ignore) {
		}
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#verifyPassword(org.smartsnip.core.User, java.lang.String)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testVerifyPassword() throws Throwable {
		User user = helper.createUser("pwdTester", "aaa", "bbb@ccc.dd", User.UserState.validated);
		assertTrue(instance.verifyPassword(user, "blabla"));
		assertFalse(instance.verifyPassword(user, "blabaa"));
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#isLoginGranted(User)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testIsLoginGranted() throws Throwable {
		User user = helper.createUser("pwdTester", "aaa", "bbb@ccc.dd", User.UserState.validated);
		assertTrue(instance.isLoginGranted(user));
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#findUser(java.lang.String)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testFindUser() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserSnippets(org.smartsnip.core.User)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetUserSnippets() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getFavorited(org.smartsnip.core.User)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetFavorited() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(java.util.List)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetSnippetsListOfTag() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(org.smartsnip.core.Category)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetSnippetsCategory() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(org.smartsnip.core.Category, java.lang.Integer, java.lang.Integer)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetSnippetsCategoryIntegerInteger() throws Throwable {
		List<Snippet> snips = instance.getSnippets(null, null, null);
		int i = 0;
		for (Snippet s : snips) {
			System.out.println(s);
			++i;
		}
		System.out.println("total: " + i);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippet(java.lang.Long)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetSnippet() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getComment(java.lang.Long)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetComment() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getComments(org.smartsnip.core.Snippet)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetComments() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getTags(org.smartsnip.core.Snippet)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetTags() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllTags(java.lang.Integer, java.lang.Integer)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetAllTags() throws Throwable {
		List<Tag> tags = instance.getAllTags(null, null);
		int i = 0;
		for (Tag t : tags) {
			System.out.println(t);
			++i;
		}
		System.out.println("total: " + i);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getNotifications(org.smartsnip.core.User, boolean)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetNotifications() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCodes(org.smartsnip.core.Snippet)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetCodes() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllCategories()}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetAllCategories() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCategory(org.smartsnip.core.Snippet)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetCategorySnippet() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCategory(java.lang.String)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetCategoryString() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getParentCategory(org.smartsnip.core.Category)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetParentCategory() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSubcategories(org.smartsnip.core.Category)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetSubcategories() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllLanguages()}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetAllLanguages() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getRatings(org.smartsnip.core.Snippet)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetRatings() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAverageRating(org.smartsnip.core.Snippet)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetAverageRating() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getVotes(org.smartsnip.core.Comment)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetVotes() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getVote(org.smartsnip.core.User, org.smartsnip.core.Comment)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testGetVote() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#search(java.lang.String, java.lang.Integer, java.lang.Integer)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Ignore
	public void testSearch() throws Throwable {
		fail("Not yet implemented"); // TODO implement test case
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserCount()}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetUserCount() throws Throwable {
		int count = instance.getUserCount();
		System.out.println("user count = " + count);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCategoryCount()}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetCategoryCount() throws Throwable {
		int count = instance.getCategoryCount();
		System.out.println("category count = " + count);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippetsCount()}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetSnippetsCount() throws Throwable {
		int count = instance.getSnippetsCount();
		System.out.println("snippets count = " + count);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getTagsCount()}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetTagsCount() throws Throwable {
		int count = instance.getTagsCount();
		System.out.println("tags count = " + count);
	}

}
