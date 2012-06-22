/**
 * File: SqlPersistenceImplTest.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.smartsnip.core.Category;
import org.smartsnip.core.Code;
import org.smartsnip.core.File;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Notification;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.core.User.UserState;
import org.smartsnip.persistence.IPersistence;
import org.smartsnip.persistence.PersistenceFactory;
import org.smartsnip.security.MD5;
import org.smartsnip.shared.Pair;

/**
 * Test cases for the database methods provided by the
 * {@link org.smartsnip.persistence.IPersistence} interface and implemented by
 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl}. To drive the
 * tests an empty MySQL database is needed. The setup of the table structure
 * should be done with the DB_setup.sql script. Once set up the database the
 * following lines in the hibernate.cfg.test_db.xml are to update with the
 * connection data of the new created database.
 * <p>
 * &lt;!-- Local Database connection settings --&gt;<br>
 * &lt;property
 * name="connection.url"&gt;jdbc:mysql://test_host:port/test_database
 * &lt;/property&gt;<br>
 * &lt;property name="connection.username"&gt;test_user&lt;/property&gt;<br>
 * &lt;property name="connection.password"&gt;test_password&lt;/property&gt;
 * </p>
 * 
 * @author littlelion
 * 
 */
public class SqlPersistenceImplTest {

	private static IPersistence instance;
	private static SqlPersistenceHelper helper;
	private static Validator validator;
	private static Logger log = Logger.getLogger(SqlPersistenceImplTest.class);

	private static Snippet test_snip1;
	private static Snippet test_snip2;
	private static User test_user1;
	private static User test_user2;
	private static Comment test_comment1;

	/**
	 * set up the database connection before unit tests
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// change the log level
		LogManager.getRootLogger().setLevel(Level.ALL);
		log.info("SqlPersistence test cases - begin tests");
		log.info("changed LOG-Level to ALL");
		System.out.println("See log file for detailed debug messages.");
		System.out.println();

		// get a bean validator instance
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		String dbConfig = "/hibernate.cfg.test_db.xml"; // the config file

		log.info("Use local database defined in " + dbConfig);
		PersistenceFactory.closeFactory();
		PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_SQL_DB);
		DBSessionFactory.getInstance(dbConfig);
		instance = PersistenceFactory.getInstance();
		assertTrue(
				"persistence type not PERSIST_SQL_DB",
				PersistenceFactory.getPersistenceType() == PersistenceFactory.PERSIST_SQL_DB);

		// mass index the database
		Maintainance.fullTextMassIndexer();

		// get a helper to create some core objects
		helper = new SqlPersistenceHelper();

		// write some objects into the DB
		log.trace("Preparing some persistent test objects");
		User user1 = helper.createUser("_test_user_1", "a test user",
				"one@test.org", UserState.unvalidated, new Date());
		User user2 = helper.createUser("_test_user_2", "another test user",
				"two@test.org", UserState.validated, new Date());
		User user3 = helper.createUser("_test_user_3", "third test user",
				"three@test.org", UserState.validated, new Date());
		instance.writeUser(user1, IPersistence.DB_DEFAULT);
		instance.writeUser(user2, IPersistence.DB_DEFAULT);
		instance.writeUser(user3, IPersistence.DB_DEFAULT);

		instance.writeLogin(user1, MD5.md5("_test_pass"), true,
				IPersistence.DB_DEFAULT);
		instance.writeLogin(user2, MD5.md5("_test_login"), false,
				IPersistence.DB_DEFAULT);
		instance.writeLogin(user3, MD5.md5("_test_code"), true,
				IPersistence.DB_DEFAULT);

		Category par = helper.createCategory("_test_root",
				"a test category as root", null);
		Category cat = helper.createCategory("_test_sub",
				"another test category", "_test_root");
		instance.writeCategory(par, IPersistence.DB_FORCE_NULL_VALUES);
		instance.writeCategory(cat, IPersistence.DB_DEFAULT);

		instance.writeLanguage("_test_java", "java", true,
				IPersistence.DB_DEFAULT);
		instance.writeLanguage("_test_c", "c", true, IPersistence.DB_DEFAULT);
		instance.writeLanguage("_test_sql", "sql", false,
				IPersistence.DB_DEFAULT);

		instance.writeLicense(
				"_test_license",
				"This is a test license file. It is generated by the SqlPersistence unit test",
				IPersistence.DB_DEFAULT);

		List<Tag> tags1 = new ArrayList<Tag>();
		tags1.add(helper.createTag("_test_one"));
		tags1.add(helper.createTag("_test_two"));
		tags1.add(helper.createTag("_test_three"));
		tags1.add(helper.createTag("_test_four"));
		tags1.add(helper.createTag("_test_five"));

		Snippet snip1 = helper.createSnippet(1L, user1.getUsername(),
				"_test_snippet_1", "this is a snippet", par.getName(), tags1,
				null, "_test_license", 0, 0F);
		Long snipId1 = instance.writeSnippet(snip1, IPersistence.DB_DEFAULT);
		snip1.id = snipId1; // fetch the generated snippet id from the database

		tags1.remove(2);
		tags1.remove(0);
		tags1.add(helper.createTag("_test_one_more"));
		tags1.add(helper.createTag("_test_another_one"));

		Snippet snip2 = helper.createSnippet(2L, user1.getUsername(),
				"_test_snippet_2", "this is another snippet", cat.getName(),
				tags1, null, "_test_license", 0, 0F);
		Long snipId2 = instance.writeSnippet(snip2, IPersistence.DB_DEFAULT);
		snip2.id = snipId2;

		List<Tag> tags2 = new ArrayList<Tag>();
		tags2.add(helper.createTag("_test_something"));

		Snippet snip3 = helper.createSnippet(3L, user2.getUsername(),
				"_test_snippet_3", "this is a third snippet", par.getName(),
				tags2, null, "_test_license", 0, 0F);
		Long snipId3 = instance.writeSnippet(snip3, IPersistence.DB_DEFAULT);
		snip3.id = snipId3;

		StringBuilder builder = new StringBuilder("/* test code in java */\n");
		builder.append("public class Test {\n\tpublic static void main() {\n");
		builder.append("\tfor(int i = 0; i < 10; ++i) ");
		builder.append("{\n\t\tSystem.out.println(\"Number = \" + i);\n\t}\n}\n");

		List<Code> codes = new ArrayList<Code>();
		codes.add(helper.createCode(1L, "/* test code incomplete */\n",
				"_test_java", snip1.getHashId(), 1, null));
		codes.add(helper.createCode(2L, builder.toString(), "_test_java",
				snip1.getHashId(), 2, null));
		codes.add(helper.createCode(3L, "/* test code to snippet 2 */",
				"_test_java", snip2.getHashId(), 0, null));
		codes.add(helper.createCode(4L, "/* test code to snippet 3 */",
				"_test_java", snip3.getHashId(), 7, null));
		instance.writeCode(codes, IPersistence.DB_DEFAULT);

		Notification notif = helper
				.createNotification(1L, user1.getUsername(), "a test notification", false,
						new Date(), "source is unknown", snip1.getHashId());
		instance.writeNotification(notif, IPersistence.DB_DEFAULT);

		List<Comment> comments = new ArrayList<Comment>();
		comments.add(helper.createComment(user2.getUsername(), snipId1,
				"first comment to user1's snippet", 1L, new Date(), 0, 0));
		comments.add(helper.createComment(user3.getUsername(), snipId1,
				"second comment to user1's snippet", 2L, new Date(), 0, 0));
		comments.add(helper.createComment(user1.getUsername(), snipId2,
				"third comment to user1's snippet", 3L, new Date(), 0, 0));
		comments.add(helper.createComment(user1.getUsername(), snipId3,
				"fourth comment to user2's snippet", 4L, new Date(), 0, 0));
		Comment comm1 = helper.createComment(user2.getUsername(), snipId2,
				"some comments with no message", 5L, new Date(), 0, 0);
		Comment comm2 = helper.createComment(user3.getUsername(), snipId2,
				"one more comment with no message", 6L, new Date(), 0, 0);
		comments.add(comm1);
		comments.add(comm2);
		instance.writeComment(comments, IPersistence.DB_DEFAULT);

		instance.addFavourite(snip3, user1, IPersistence.DB_DEFAULT);

		instance.writeRating(3, snip2, user3, IPersistence.DB_DEFAULT);
		instance.writeRating(1, snip2, user1, IPersistence.DB_DEFAULT);

		instance.votePositive(user1, comm1, IPersistence.DB_DEFAULT);
		instance.voteNegative(user2, comm1, IPersistence.DB_DEFAULT);
		instance.voteNegative(user3, comm1, IPersistence.DB_DEFAULT);
		instance.votePositive(user1, comm2, IPersistence.DB_DEFAULT);
		instance.votePositive(user2, comm2, IPersistence.DB_DEFAULT);
		instance.votePositive(user3, comm2, IPersistence.DB_DEFAULT);

		test_snip1 = snip1;
		test_snip2 = snip2;
		test_user1 = user1;
		test_user2 = user2;
		test_comment1 = comm1;
	}

	/**
	 * tear down the database connection after unit tests
	 * 
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		PersistenceFactory.closeFactory();
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
		User user = helper.createUser("_test_somebody", "the test user",
				"sbd@test.org", UserState.validated, new Date());
		instance.writeUser(user, IPersistence.DB_DEFAULT);

		Session session = DBSessionFactory.open();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBUser entity = new DBUser();
			entity.setUserName("_test_somebody");

			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}

		// test on constraint-violations
		boolean violatedConstraits = false;
		Set<ConstraintViolation<User>> constraintViolations = validator
				.validate(user);
		for (Iterator<ConstraintViolation<User>> itr = constraintViolations
				.iterator(); itr.hasNext();) {
			log.error("Constraint Violation: " + itr.next().getMessage());
			violatedConstraits = true;
		}
		if (violatedConstraits) {
			fail("Constraint Violations occured! See log file");
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
		users.add(helper.createUser("sick", "sick guy", "sick@guy.org", null, new Date()));
		users.add(helper.createUser("sie", "she bang", "she@bang", null, new Date()));
		users.add(helper.createUser("he", "he man", "er@bla", null, new Date()));
		users.add(helper.createUser("samson", "samie blu", "heini@bla", null, new Date()));
		users.add(helper.createUser("xxx", "anonymus", "anon@ym", null, new Date()));
		instance.writeUser(users, IPersistence.DB_DEFAULT);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writePassword(org.smartsnip.core.User, java.lang.String, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testWritePassword() throws Throwable {
		// deprecated
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
		User user = helper.createUser("sulu", "su lu", "su@bla", null, new Date());
		Category par = helper.createCategory("bla", "viel bla bla", null);
		Category cat = helper.createCategory("blabla", "noch mehr bla bla",
				"bla");
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(helper.createTag("xxx"));
		tags.add(helper.createTag("bbb"));
		tags.add(helper.createTag("new"));
		tags.add(helper.createTag("ddd"));
		tags.add(helper.createTag("old"));
		Snippet snip = helper.createSnippet(7L, user.getUsername(),
				"more stupid", "something else stupid stuff", cat.getName(),
				tags, null, null, 0, 0F);
		Code code = helper.createCode(1L, "code", "language", snip.getHashId(), 0, null);
		helper.setCodeOfSnippet(snip, code);
		instance.writeUser(user, IPersistence.DB_DEFAULT);
		instance.writeTag(tags, IPersistence.DB_DEFAULT);
		instance.writeCategory(par, IPersistence.DB_DEFAULT);
		instance.writeCategory(cat, IPersistence.DB_DEFAULT);
		Long snipId = instance.writeSnippet(snip, IPersistence.DB_DEFAULT);
		snip.id = snipId;
		Notification notif = helper.createNotification(22L, user.getUsername(), "notif",
				false, new Date(), "source", snip.getHashId());
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
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeLanguage(java.lang.String, java.lang.String, boolean, int)}
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
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#unRate(org.smartsnip.core.User, org.smartsnip.core.Snippet, int)}
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getRatings(org.smartsnip.core.Snippet)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testWriteRating() throws Throwable {
		// try{
		// instance.unRate(null, test_snip1, IPersistence.DB_FORCE_DELETE);
		// } catch (IOException ignore){}
		double rating = instance.getSnippet(test_snip1.getHashId())
				.getAverageRating();
		// assertEquals("Expected rating 0", 0, rating, 0.01);
		instance.writeRating(4, test_snip1, test_user1, IPersistence.DB_DEFAULT);
		rating = instance.getSnippet(test_snip1.getHashId()).getAverageRating();
		System.err.println(rating);
		// assertEquals("Expected rating 4", 4, rating, 0.01);
		List<Pair<User, Integer>> ratings = instance.getRatings(test_snip1);
		// assertEquals(1, ratings.size());
		// instance.unRate(null, test_snip1, IPersistence.DB_FORCE_DELETE);
		rating = instance.getSnippet(test_snip1.getHashId()).getAverageRating();
		// assertEquals(0, rating, 0.01);

		ratings = instance.getRatings(test_snip1);
		// assertEquals(0, ratings.size());
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeVote(java.lang.Integer, org.smartsnip.core.Comment, org.smartsnip.core.User, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testWriteVote() throws Throwable {
		instance.writeVote(1, test_comment1, test_user1, IPersistence.DB_DEFAULT);
		instance.writeVote(-1, test_comment1, test_user2, IPersistence.DB_DEFAULT);
		Comment comm = instance.getComment(test_comment1.getHashID());
		System.err.println(comm);
		Pair<Integer, Integer> votes = instance.getVotes(test_comment1);
		assertEquals(1, votes.first.intValue());
		assertEquals(2, votes.second.intValue());
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
	@Test
	@SuppressWarnings("unchecked")
	public void testWriteAndRemoveCategory() throws Throwable {
		Category midCat = helper.createCategory("_test_middle",
				"intermedate categorty", "_test_parent");
		List<Category> testCat = new ArrayList<Category>();
		testCat.add(helper.createCategory("_test_parent", "parent categorty",
				null));
		testCat.add(midCat);
		testCat.add(helper.createCategory("_test_child", "child categorty",
				"_test_middle"));
		instance.writeCategory(testCat, IPersistence.DB_FORCE_NULL_VALUES);

		Session session = null;
		List<DBCategory> entities;
		DBQuery query;
		try {
			session = DBSessionFactory.open();
			query = new DBQuery(session);
			query.addParameter("name1", "_test_parent");
			query.addParameter("name2", "_test_middle");
			query.addParameter("name3", "_test_child");
			query.initialize();
			Query hibQuery = query
					.buildCustomQuery(
							"from DBCategory where name = :name1 or name = :name2 or name = :name3",
							IPersistence.DB_DEFAULT);
			entities = hibQuery.list();
		} finally {
			DBSessionFactory.close(session);
		}
		assertTrue("3 objects expected, but there are " + entities.size(),
				entities.size() == 3);
		for (DBCategory ca : entities) {
			if (ca.getName() == "_test_parent") {
				assertNull("parent of _test_parent mus be null.",
						ca.getParentId());
			}
		}
		// test on constraint-violations
		boolean violatedConstraits = false;
		for (DBCategory c : entities) {
			Set<ConstraintViolation<DBCategory>> constraintViolations = validator
					.validate(c);
			for (Iterator<ConstraintViolation<DBCategory>> itr = constraintViolations
					.iterator(); itr.hasNext();) {
				log.error("Constraint Violation: " + itr.next().getMessage());
				violatedConstraits = true;
			}
		}
		if (violatedConstraits) {
			fail("Constraint Violations occured! See log file");
		}

		// remove middle Category
		instance.removeCategory(midCat, IPersistence.DB_DEFAULT);

		entities = new ArrayList<DBCategory>();
		try {
			session = DBSessionFactory.open();
			query = new DBQuery(session);
			query.addParameter("name1", "_test_parent");
			query.addParameter("name2", "_test_middle");
			query.addParameter("name3", "_test_child");
			query.initialize();
			Query hibQuery = query
					.buildCustomQuery(
							"from DBCategory where name = :name1 or name = :name2 or name = :name3",
							IPersistence.DB_DEFAULT);
			entities = hibQuery.list();
		} finally {
			DBSessionFactory.close(session);
		}

		assertTrue("2 objects expected, but there are " + entities.size(),
				entities.size() == 2);

		// test on constraint-violations
		violatedConstraits = false;
		for (DBCategory c : entities) {
			Set<ConstraintViolation<DBCategory>> constraintViolations = validator
					.validate(c);
			for (Iterator<ConstraintViolation<DBCategory>> itr = constraintViolations
					.iterator(); itr.hasNext();) {
				log.error("Constraint Violation: " + itr.next().getMessage());
				violatedConstraits = true;
			}
		}
		if (violatedConstraits) {
			fail("Constraint Violations occured! See log file");
		}
		DBCategory p = null;
		DBCategory c = null;
		for (DBCategory ca : entities) {
			if (ca.getName().equals("_test_parent")) {
				p = ca;
			} else if (ca.getName().equals("_test_child")) {
				c = ca;
			}
		}
		System.err.println("Categories: p: " + p + ", " + c);
		assertEquals(p.getCategoryId(), c.getParentId());
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
	@Ignore
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
		try {
			instance.getPassword(null);
			fail("UnsupportedOperationException expected");
		} catch (UnsupportedOperationException ignore) {
		}
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#verifyPassword(org.smartsnip.core.User, java.lang.String)}
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeLogin(User, String, Boolean, int)}
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#isLoginGranted(User)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testVerifyPassword() throws Throwable {
		// write password
		User user = helper.createUser("pwdTester", "aaa", "bbb@ccc.dd",
				User.UserState.validated, new Date());
		instance.writeUser(user, IPersistence.DB_DEFAULT);
		instance.writeLogin(user, "blabla", true, IPersistence.DB_DEFAULT);

		// read it ...
		assertTrue(instance.verifyPassword(user, "blabla"));
		assertFalse(instance.verifyPassword(user, " blabla"));
		assertFalse(instance.verifyPassword(user, "blabla "));
		assertFalse(instance.verifyPassword(user, "blabaa"));
		assertTrue(instance.isLoginGranted(user));

		Session session = DBSessionFactory.open();
		DBQuery query = new DBQuery(session);
		DBLogin entity = new DBLogin();
		entity.setUser("pwdTester");
		entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
				| DBQuery.QUERY_UNIQUE_RESULT);
		assertEquals(entity.getPassword(), "blabla");

		// test on constraint-violations
		boolean violatedConstraits = false;
		Set<ConstraintViolation<User>> constraintViolations = validator
				.validate(user);
		for (Iterator<ConstraintViolation<User>> itr = constraintViolations
				.iterator(); itr.hasNext();) {
			log.error("Constraint Violation: " + itr.next().getMessage());
			violatedConstraits = true;
		}
		if (violatedConstraits) {
			fail("Constraint Violations occured! See log file");
		}
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#isLoginGranted(User)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testUpdateLogin() throws Throwable {
		// write password
		User user = helper.createUser("pwdUpdateTester", "bbb", "aaa@ccc.dd",
				User.UserState.validated, new Date());
		instance.writeUser(user, IPersistence.DB_DEFAULT);
		instance.writeLogin(user, "old_pass", true, IPersistence.DB_DEFAULT);
		// update the written password
		instance.writeLogin(user, "new_pass", true, IPersistence.DB_DEFAULT);

		// read it ...
//		assertTrue(instance.verifyPassword(user, "new_pass"));

		Session session = DBSessionFactory.open();
		DBQuery query = new DBQuery(session);
		DBLogin entity = new DBLogin();
		entity.setUser("pwdTester");
		entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
				| DBQuery.QUERY_UNIQUE_RESULT);
//		assertEquals(entity.getPassword(), "blabla");
		
		// test on constraint-violations
		boolean violatedConstraits = false;
		Set<ConstraintViolation<User>> constraintViolations = validator
				.validate(user);
		for (Iterator<ConstraintViolation<User>> itr = constraintViolations
				.iterator(); itr.hasNext();) {
			log.error("Constraint Violation: " + itr.next().getMessage());
			violatedConstraits = true;
		}
		if (violatedConstraits) {
			fail("Constraint Violations occured! See log file");
		}

		// test the update query
//		entity.setPassword("changed_password");
//		query.reset();
//		query.update(entity, IPersistence.DB_DEFAULT);
//		query.addParameter("user_name", "pwdUpdateTester");
//		query.addParameter("password", "bla");
//		query.initialize();
//		org.hibernate.Query hQuery = query.buildCustomQuery("select aes_decrypt(password, 'd2aefeac9dc661bc98eebd6cc12f0b82') from DBLogin where user_name = :user_name", IPersistence.DB_DEFAULT);
//		List<String> str = hQuery.list();
//		query.reset();
		System.out.println("///////////////////////////////////////////");
		Transaction tx = session.beginTransaction();
		org.hibernate.Query uQuery = session.createQuery("update DBLogin set password = aes_encrypt(:pwd, 'd2aefeac9dc661bc98eebd6cc12f0b82') where user_name = :usr");
		uQuery.setParameter("pwd", "newer_pass");
		uQuery.setParameter("usr", "pwdUpdateTester");
		uQuery.executeUpdate();
		
//		session.update(entity);
		tx.commit();
		System.out.println("///////////////////////////////////////////");
		session.close();
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
	@Test
	public void testGetSnippetsCategoryIntegerInteger() throws Throwable {
		Category cat = helper.createCategory("_test_sub", "a test category",
				null);
		List<Snippet> snips = instance.getSnippets(cat, null, null);
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
	@Test
	public void testGetSnippet() throws Throwable {
		Snippet snip = instance.getRandomSnippet(0);
		Snippet snip2 = instance.getRandomSnippet(0.345);
		Snippet snip3 = instance.getRandomSnippet(0.99999999999);
		Snippet snip4 = instance.getRandomSnippet(1);
		System.out.println(snip);
		System.out.println(snip2);
		System.out.println(snip3);
		System.out.println(snip4);

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
	@Test
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
	@Test
	public void testGetCodes() throws Throwable {
		List<Code> cod = instance.getCodes(test_snip1);
		for (Code c : cod) {
			System.err.println("Code: " + c.getHashID() + " SnippetId: " + c.getSnippetId() + " Version = "
					+ c.getVersion() + " Data: " + c.getCode());
		}
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCodeFile(Long)}
	 * and
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCodeFile(Long, org.smartsnip.core.File, int)}
	 * .
	 * 
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testWriteAndGetCodeFile() throws Throwable {
		String name = "hibernate.cfg.test_db.xml";
		java.io.File file = new java.io.File("./test/" + name);
		FileInputStream in = new FileInputStream(file);
		long length = file.length();
		assertTrue("File size > 0 expected. Size = " + length, length > 0);

		if (length > Integer.MAX_VALUE) {
			throw new IOException("file too large");
		}
		byte[] buffer = new byte[(int) length];
		Byte[] content;
		try {
			in.read(buffer);
			content = ArrayUtils.toObject(buffer);
		} finally {
			try {
				in.close();
			} catch (IOException ignored) {
			}
		}
		File codeFile = helper.createCodeFile(name, content);
		Code code = helper.createCode(1L, "to test", "java", test_snip2.getHashId(), 1,
				null);
		Long codeId = instance.writeCode(code, IPersistence.DB_DEFAULT);
		instance.writeCodeFile(codeId, codeFile, IPersistence.DB_DEFAULT);

		File result = instance.getCodeFile(codeId);

		// Tests on the object
		assertEquals(name, result.getName());
		assertEquals(content.length, result.getContent().length);
		assertArrayEquals(content, result.getContent());

	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllCategories()}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetAllCategories() throws Throwable {
		List<Category> cat = instance.getAllCategories();
		for (Category c : cat) {
			System.out.println("Category: " + c.getName() + " Parent: "
					+ c.getParentName());
		}
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
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getLanguages(int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetLanguages() throws Throwable {
		Set<String> allLanguages = new TreeSet<String>(
				instance.getLanguages(IPersistence.LANGUAGE_GET_ALL));
		Set<String> defaultSubset = new TreeSet<String>(
				instance.getLanguages(IPersistence.LANGUAGE_GET_DEFAULTS));
		Set<String> othersSubset = new TreeSet<String>(
				instance.getLanguages(IPersistence.LANGUAGE_GET_OTHERS));

		// tests on all set
		assertTrue("Expected language not present: _test_java",
				allLanguages.contains("_test_java"));
		assertTrue("Expected language not present: _test_c",
				allLanguages.contains("_test_c"));
		assertTrue("Expected language not present: _test_sql",
				allLanguages.contains("_test_sql"));
		assertTrue(
				"Number of expected languages > 2, but is "
						+ allLanguages.size(), allLanguages.size() > 2);

		// tests on default subset
		assertTrue("Expected language not present: _test_java",
				defaultSubset.contains("_test_java"));
		assertTrue("Expected language not present: _test_c",
				defaultSubset.contains("_test_c"));
		assertFalse(
				"Language expected as not default is in defaults-list: _test_sql",
				defaultSubset.contains("_test_sql"));
		assertTrue(
				"Number of expected languages > 1, but is "
						+ defaultSubset.size(), defaultSubset.size() > 1);
		assertTrue("Number of expected defaults < all languages, but is "
				+ defaultSubset.size() + " < " + allLanguages.size(),
				defaultSubset.size() < allLanguages.size());

		// tests on non-default subset
		assertFalse(
				"Language expected as default is in non-defaults-list: _test_java",
				othersSubset.contains("_test_java"));
		assertFalse(
				"Language expected as default is in non-defaults-list: _test_c",
				othersSubset.contains("_test_c"));
		assertTrue("Expected language not present: _test_sql",
				othersSubset.contains("_test_sql"));
		assertTrue(
				"Number of expected languages > 0, but is "
						+ defaultSubset.size(), defaultSubset.size() > 0);
		assertTrue("Number of expected non-defaults < all languages, but is "
				+ othersSubset.size() + " < " + allLanguages.size(),
				othersSubset.size() < allLanguages.size());
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getLanguageProperties(String)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetLanguageProperties() throws Throwable {
		Pair<String, Boolean> result = instance
				.getLanguageProperties("_test_java");
		assertEquals("java", result.first);
		assertTrue("Expected property: isDefault == true", result.second);
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
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#search(java.lang.String, java.lang.Integer, java.lang.Integer, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testSearch() throws Throwable {
		List<Snippet> snippets = instance.search("snippet", null, null,
				IPersistence.SORT_MOSTVIEWED);
		System.err.println("Search:");
		for (Snippet s : snippets) {
			System.out.println("Search result: snippetId = " + s.getHashId()
					+ " Headline: " + s.getName() + " Data: "
					+ s.getDescription() + " Tags: " + s.getTags()
					+ " Category: " + s.getCategoryName() + " License: " + s.getLicense()
					+ " Viewcount: " + s.getViewcount() + " Rating: "
					+ s.getAverageRating() + " Owner: " + s.getOwnerUsername());
		}
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllSnippets(java.lang.Integer, java.lang.Integer, int)}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetAllSnippets() throws Throwable {
		List<Snippet> snippets = instance.getAllSnippets(null, null,
				IPersistence.SORT_MOSTVIEWED);
		System.err.println("All Snippets:");
		for (Snippet s : snippets) {
			System.out.println("Result: snippetId = " + s.getHashId()
					+ " Headline: " + s.getName() + " Data: "
					+ s.getDescription() + " Tags: " + s.getTags()
					+ " Category: " + s.getCategoryName() + " License: " + s.getLicense()
					+ " Viewcount: " + s.getViewcount() + " Rating: "
					+ s.getAverageRating() + " Owner: " + s.getOwnerUsername());
		}
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
