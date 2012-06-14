package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Category;
import org.smartsnip.core.Search;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.shared.ISession;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSearch.SearchSorting;
import org.smartsnip.shared.XSnippet;
import org.smartsnip.shared.XUser;

/**
 * This class acts as the servlet that coordiantes the transactions between the
 * session and the client
 * 
 */
public class ISessionImpl extends GWTSessionServlet implements ISession {

	/** Serialisation ID */
	private static final long serialVersionUID = 51299L;

	/** Max. number of search items, hard-coded */
	private static int maxSearchItems = 100;

	@Override
	public String getUsername() {
		Session session = getSession();
		if (!session.isLoggedIn()) return null;

		return session.getUsername();
	}

	@Override
	public int getActiveSessionCount() {
		return Session.activeCount();
	}

	@Override
	public int getGuestSessionCount() {
		return Session.guestSessions();
	}

	@Override
	public int getUserCount() {
		return User.totalCount();
	}

	@Override
	public int getCategoryCount() {
		return Category.totalCount();
	}

	@Override
	public int getSnippetCount() {
		return Snippet.totalCount();
	}

	@Override
	public boolean login(String username, String password) throws NoAccessException {

		Session session = getSession();

		if (session.isLoggedIn()) return false;

		try {
			session.login(username, password);
			logInfo("Login success: USERNAME=" + username);
			return true;
		} catch (NoAccessException e) {
			logInfo("Login failure: USERNAME=" + username);
			throw e;
		}
	}

	@Override
	public void logout() {
		logInfo("Logout");

		Session session = getSession();
		session.logout();
	}

	@Override
	public boolean isLoggedIn() {
		Session session = getSession();
		return session.isLoggedIn();
	}

	@Override
	public boolean registerNewUser(String username, String password, String email) throws NoAccessException {
		if (username == null) return false;
		if (password == null || password.isEmpty()) return false;
		if (email == null || email.isEmpty()) return false;

		username = User.trimUsername(username);
		if (username.isEmpty()) return false;

		Session session = getSession();
		if (!session.getPolicy().canRegister(session)) throw new NoAccessException();

		if (User.exists(username)) return false;
		try {
			logInfo("Requesting create new user (USER=" + username + "; MAIL=" + email + ")");
			if (User.createNewUser(username, password, email) == null) return false;

			// Success
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public String getSessionCookie() {
		Session session = getSession();
		return session.getCookie();
	}

	@Override
	public XSearch doSearch(String searchString, List<String> tags, List<String> categories, SearchSorting sorting,
			int start, int count) {

		XSearch result = new XSearch();
		result.snippets = new ArrayList<XSnippet>();
		result.searchString = searchString;

		if (start < 0) start = 0;
		if (count <= 0) count = 1;
		if (count >= maxSearchItems) count = maxSearchItems;

		result.start = start;
		result.count = count;

		Search search = Search.createSearch(searchString);
		if (tags != null) {
			for (String tag : tags) {
				search.addTag(tag);
				result.tags.add(tag);
			}
		}
		if (categories != null) for (String category : categories) {
			search.addCategory(category);
			result.categories.add(category);
		}

		List<Snippet> snippets = search.getResults(sorting, start, count);
		List<String> resultCategories = new ArrayList<String>();
		List<String> resultTags = new ArrayList<String>();
		for (Snippet snippet : snippets) {
			result.snippets.add(snippet.toXSnippet());

			String category = snippet.getCategoryName();
			if (!resultCategories.contains(category)) resultCategories.add(category);
			for (Tag tag : snippet.getTags()) {
				if (!resultTags.contains(tag.name)) resultTags.add(tag.name);
			}
		}

		result.categories = resultCategories;
		result.tags = resultTags;

		List<Tag> allTagsMatchingSearchCriteria = search.getAllTagsMatchingSearchCriteria();
		for (Tag tag : allTagsMatchingSearchCriteria) {
			result.tagsAppearingInSearchString.add(tag.name);
		}

		result.totalresults = search.getTotalResults();
		if (result.totalresults > 0) {
			logInfo("Search for: \"" + searchString + "\". " + result.totalresults + " results total");
		}

		return result;
	}

	@Override
	public XUser getUser(String username) {

		Session session = getSession();
		if (!session.isLoggedIn()) return null;

		XUser user = new XUser(session.getUsername(), session.getRealname(), session.getMail());

		return user;
	}
}
