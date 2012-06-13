/**
 * File: CodeFactory.java
 * Date: 06.06.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.smartsnip.core.Code;
import org.smartsnip.core.File;
import org.smartsnip.core.Snippet;
import org.smartsnip.persistence.IPersistence;

/**
 * @author littlelion
 * 
 */
public class CodeFactory {

	private CodeFactory() {
		// no instances
	}

	/**
	 * Implementation of {@link IPersistence#writeCode(Code, int)}
	 * 
	 * @param code
	 * @param flags
	 * @return the Id
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	static Long writeCode(Code code, int flags) throws IOException {
		Session session = DBSessionFactory.open();
		Long result;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBCode entity = new DBCode();
			entity.setCodeId(code.getHashID()); // codeId is read-only
			entity.setSnippetId(code.getSnippet().getHashId());
			entity.setLanguage(code.getLanguage());
			entity.setFile(code.getCode());
			entity.setVersion(code.getVersion());

			result = (Long) query.write(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return result;
	}

	/**
	 * Implementation of {@link IPersistence#writeCode(List, int)}
	 * 
	 * @param codes
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(java.util.List,
	 *      int)
	 */
	static void writeCode(List<Code> codes, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query;

			DBCode entity;

			for (Code code : codes) {
				query = new DBQuery(session);
				entity = new DBCode();
				entity.setCodeId(code.getHashID()); // codeId is read-only
				entity.setSnippetId(code.getSnippet().getHashId());
				entity.setLanguage(code.getLanguage());
				entity.setFile(code.getCode());
				entity.setVersion(code.getVersion());

				query.write(entity, flags);
			}
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of {@link IPersistence#writeCodeFile}
	 * 
	 * @param codeId
	 * @param file
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCodeFile(Long,
	 *      File, int)
	 */
	static void writeCodeFile(Long codeId, File file, int flags)
			throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBCodeFile entity = new DBCodeFile();
			entity.setCodeId(codeId); // codeId is read-only
			entity.setFileName(file.getName());
			entity.setFileContent(file.getContent());

			query.update(entity, IPersistence.DB_FORCE_NULL_VALUES);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of {@link IPersistence#writeLanguage(String, int)}
	 * 
	 * @param language
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeLanguage(java.lang.String,
	 *      int)
	 */
	static void writeLanguage(String language, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBLanguage entity = new DBLanguage();
			entity.setLanguage(language);

			query.write(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of {@link IPersistence#removeCode(Code, int)}
	 * 
	 * @param code
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	static void removeCode(Code code, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBCode entity = new DBCode();
			entity.setCodeId(code.getHashID());

			query.remove(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of {@link IPersistence#removeLanguage(String, int)}
	 * 
	 * @param language
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeLanguage(java.lang.String,
	 *      int)
	 */
	static void removeLanguage(String language, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBLanguage entity = new DBLanguage();
			entity.setLanguage(language);

			query.remove(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of {@link IPersistence#getCodes(Snippet)}
	 * 
	 * @param snippet
	 * @return a list of code fragments
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCodes(org.smartsnip.core.Snippet)
	 */
	static List<Code> getCodes(Snippet snippet) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<Code> result = new ArrayList<Code>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			result = fetchCode(helper, session, snippet);

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return result;
	}

	/**
	 * Implementation of {@link IPersistence#getCodeFile(Long)}
	 * 
	 * @param codeId
	 * @return the file
	 * @throws IOException 
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCodeFile(java.lang.Long)
	 */
	static File getCodeFile(Long codeId) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBCodeFile entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			DBQuery query = new DBQuery(session);
			entity = new DBCodeFile();
			entity.setCodeId(codeId);
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
			

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return helper.createCodeFile(entity.getFileName(), entity.getFileContent());
	}

	/**
	 * Implementation of {@link IPersistence#getAllLanguages()}
	 * 
	 * @return a list of languages
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllLanguages()
	 */
	static List<String> getAllLanguages() throws IOException {
		Session session = DBSessionFactory.open();
		List<String> result = new ArrayList<String>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBLanguage entity = new DBLanguage();

			for (Iterator<DBLanguage> iterator = query.iterate(entity); iterator
					.hasNext();) {
				entity = iterator.next();
				result.add(entity.getLanguage());
			}

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return result;
	}

	/**
	 * Helper method to fetch all code fragments from a snippet.
	 * 
	 * @param helper
	 *            the PersisteceHelper object to create the tags
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippet
	 *            the snippet as source of the code
	 * @return a list of code fragments
	 */
	static List<Code> fetchCode(SqlPersistenceHelper helper, Session session,
			Snippet snippet) {
		DBQuery query = new DBQuery(session);
		DBCode entity = new DBCode();
		List<Code> result = new ArrayList<Code>();
		entity.setSnippetId(snippet.getHashId());
		System.err.println("entity: " + entity.getSnippetId());
		for (Iterator<DBCode> itr = query.iterate(entity); itr.hasNext();) {
			entity = itr.next();
			result.add(helper.createCode(entity.getCodeId(), entity.getFile(),
					entity.getLanguage(), snippet, entity.getVersion()));
		}
		return result;
	}

	/**
	 * Helper method to fetch the latest code from a snippet.
	 * 
	 * @param helper
	 *            the PersisteceHelper object to create the tags
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippet
	 *            the snippet as source of the code
	 * @return the code fragment with highest version number
	 */
	static Code fetchNewestCode(SqlPersistenceHelper helper, Session session,
			Snippet snippet) {
		Criteria criteria = session.createCriteria(DBCode.class);
		criteria.addOrder(Order.desc("version"));
		DBQuery query = new DBQuery(session);
		DBCode entity = new DBCode();
		entity.setSnippetId(snippet.getHashId());
		entity = query.fromSingle(entity, DBQuery.QUERY_NULLABLE);
		Code result = null;
		if (entity != null) {
			result = helper.createCode(entity.getCodeId(), entity.getFile(),
					entity.getLanguage(), snippet, entity.getVersion());
		}
		return result;
	}
}
