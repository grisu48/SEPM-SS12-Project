package org.smartsnip.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.smartsnip.core.Category;
import org.smartsnip.core.Code;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Notification;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.Tree;
import org.smartsnip.core.User;
import org.smartsnip.shared.Pair;

import sun.reflect.Reflection;

/**
 * This persistence layer lifes completely in the memory and should be used for
 * testing purposes and as failback-layer, if the SQL layer is down.
 * 
 * Currently it does <b>NOT</b> support any saving mechanisms, so after you
 * restart the server, all changes are gone
 * 
 * 
 */
public class MemPersistence implements IPersistence {

	/**
	 * This constructor is protected against multiple instantiation to
	 * accomplish a singleton pattern. It rejects any attempt to build an
	 * instance except it is called by the
	 * {@link PersistenceFactory#getInstance(int)} method.
	 */
	MemPersistence() throws IllegalAccessException {
		super();
		if (Reflection.getCallerClass(2) == null || Reflection.getCallerClass(2) != PersistenceFactory.class)
			throw new IllegalAccessException("Singleton pattern: caller must be PersistenceFactory class.");
	}

	private final HashMap<String, User> allUsers = new HashMap<String, User>();
	private final HashMap<Long, Snippet> allSnippets = new HashMap<Long, Snippet>();

	private final HashMap<Snippet, HashMap<Integer, Comment>> allComments = new HashMap<Snippet, HashMap<Integer, Comment>>();
	private final HashMap<Long, Comment> commentMap = new HashMap<Long, Comment>();

	private final List<Tag> allTags = new ArrayList<Tag>();
	private final List<String> allLanguages = new ArrayList<String>();

	private final HashMap<Snippet, List<Tag>> snippetTags = new HashMap<Snippet, List<Tag>>();

	private final HashMap<User, List<Notification>> notifications = new HashMap<User, List<Notification>>();

	private final HashMap<Snippet, HashMap<Integer, Code>> allCodes = new HashMap<Snippet, HashMap<Integer, Code>>();

	private final Tree<Category> categoryTree = new Tree<Category>();

	private final HashMap<Snippet, HashMap<User, Integer>> ratings = new HashMap<Snippet, HashMap<User, Integer>>();
	private final HashMap<Comment, HashMap<User, Integer>> votings = new HashMap<Comment, HashMap<User, Integer>>();

	private final HashMap<User, List<Snippet>> favorites = new HashMap<User, List<Snippet>>();

	private final HashMap<User, String> passwords = new HashMap<User, String>();

	private String toKey(String key) {
		if (key == null)
			return "";
		return key.trim().toLowerCase();
	}

	@Override
	public void writeUser(User user, int mode) throws IOException {
		if (user == null)
			return;
		String key = toKey(user.username);
		allUsers.put(key, user);
	}

	@Override
	public void writeUser(List<User> users, int mode) throws IOException {
		if (users == null)
			return;

		for (User user : users)
			writeUser(user, mode);
	}

	@Override
	public Long writeSnippet(Snippet snippet, int mode) throws IOException {
		if (snippet == null)
			return null;

		Long id = snippet.getHashId();
		if (id == null) {
			id = createNewSnippetId();
			snippet.id = id;
		}

		allSnippets.put(id, snippet);
		// TODO snippetTags.put(snippet, );
		return id;
	}

	@Override
	public void writeSnippet(List<Snippet> snippets, int mode) throws IOException {
		if (snippets == null)
			return;

		for (Snippet snippet : snippets)
			writeSnippet(snippet, mode);
	}

	@Override
	public Long writeComment(Comment comment, int mode) throws IOException {
		if (comment == null)
			return null;
		int key = comment.hashCode();

		HashMap<Integer, Comment> comments = allComments.get(comment.snippet);
		if (comments == null) {
			comments = new HashMap<Integer, Comment>();
			allComments.put(comment.snippet, comments);
		}

		comments.put(key, comment);
		return (long) key;
	}

	@Override
	public void writeComment(List<Comment> comments, int mode) throws IOException {
		if (comments == null)
			return;

		for (Comment comment : comments)
			writeComment(comment, mode);
	}

	@Override
	public void writeTag(Tag tag, int mode) throws IOException {
		if (tag == null)
			return;

		if (allTags.contains(tag))
			return;
		allTags.add(tag);
	}

	@Override
	public void writeTag(List<Tag> tags, int mode) throws IOException {
		if (tags == null)
			return;

		for (Tag tag : tags)
			writeTag(tag, mode);
	}

	@Override
	public Long writeNotification(Notification notification, int mode) throws IOException {
		// XXX In current version not used, and therefore not yet tested
		if (notification == null)
			return null;

		User owner = notification.getOwner();
		List<Notification> notifications = this.notifications.get(owner);
		if (notifications == null) {
			notifications = new ArrayList<Notification>();
			this.notifications.put(owner, notifications);
		}
		return new Long(this.notifications.get(owner).indexOf(notification));
	}

	@Override
	public void writeNotification(List<Notification> notifications, int mode) throws IOException {
		// XXX In current version not used, and therefore not yet tested
		if (notifications == null)
			return;

		for (Notification notification : notifications)
			writeNotification(notification, mode);
	}

	@Override
	public Long writeCode(Code code, int mode) throws IOException {
		if (code == null)
			return null;
		int key = code.hashCode();

		Snippet snippet = code.snippet;
		HashMap<Integer, Code> codes = allCodes.get(snippet);
		if (codes == null) {
			codes = new HashMap<Integer, Code>();
			allCodes.put(snippet, codes);
		} else
			codes.put(key, code);

		return (long) key;
	}

	@Override
	public void writeCode(List<Code> codes, int mode) throws IOException {
		if (codes == null)
			return;

		for (Code code : codes)
			writeCode(code, mode);
	}

	@Override
	public Long writeCategory(Category category, int mode) throws IOException {
		if (category == null)
			return null;

		Category parent = category.getParent();
		return (long) categoryTree.put(category, parent).hashCode();
	}

	@Override
	public void writeCategory(List<Category> categories, int mode) throws IOException {
		if (categories == null)
			return;

		for (Category category : categories)
			writeCategory(category, mode);
	}

	@Override
	public void writeLanguage(String language, int mode) throws IOException {
		if (language == null)
			return;
		language = language.trim();
		if (language.isEmpty())
			return;

		// Check for existings
		for (String lang : allLanguages)
			if (lang.equalsIgnoreCase(language))
				return;

		allLanguages.add(language);
	}

	@Override
	public void writeRating(Integer rating, Snippet snippet, User user, int mode) throws IOException {
		if (rating == null || snippet == null || user == null)
			return;

		HashMap<User, Integer> list = ratings.get(snippet);
		if (list == null) {
			list = new HashMap<User, Integer>();
			ratings.put(snippet, list);
		}
		list.put(user, rating);
	}

	@Override
	public void writeVote(Integer vote, Comment comment, User user, int mode) throws IOException {
		if (vote == null || comment == null || user == null)
			return;

		HashMap<User, Integer> list = votings.get(comment);
		if (list == null) {
			list = new HashMap<User, Integer>();
			votings.put(comment, list);
		}
		list.put(user, vote);

	}

	@Override
	public void votePositive(User user, Comment comment, int mode) throws IOException {
		writeVote(+1, comment, user, mode);
	}

	@Override
	public void voteNegative(User user, Comment comment, int mode) throws IOException {
		writeVote(-1, comment, user, mode);
	}

	@Override
	public void unVote(User user, Comment comment, int mode) throws IOException {
		writeVote(0, comment, user, mode);
	}

	@Override
	public void addFavourite(Snippet snippet, User user, int mode) throws IOException {
		if (snippet == null || user == null)
			return;

		List<Snippet> userFav = favorites.get(user);
		if (userFav == null) {
			userFav = new ArrayList<Snippet>();
			favorites.put(user, userFav);
		} else if (userFav.contains(snippet))
			return;
		userFav.add(snippet);
	}

	@Override
	public void removeFavourite(Snippet snippet, User user, int mode) throws IOException {
		if (snippet == null || user == null)
			return;

		List<Snippet> userFav = favorites.get(user);
		if (userFav == null)
			return;
		if (!userFav.contains(snippet))
			return;
		userFav.remove(snippet);
	}

	@Override
	public User getUser(String nick) throws IOException {
		if (nick == null || nick.isEmpty())
			return null;
		nick = toKey(nick);
		return allUsers.get(nick);
	}

	@Override
	public User getUserByEmail(String email) throws IOException {
		if (email == null || email.isEmpty())
			return null;

		List<User> users = new ArrayList<User>(allUsers.values());
		for (User user : users)
			if (user.getEmail().equalsIgnoreCase(email))
				return user;
		return null;
	}

	@Override
	public List<User> findUser(String realName) throws IOException {
		if (realName == null || realName.isEmpty())
			return null;

		realName = realName.toLowerCase();
		List<User> users = new ArrayList<User>(allUsers.values());
		List<User> results = new ArrayList<User>();
		for (User user : users)
			if (user.getRealName().contains(realName))
				results.add(user);
		return results;
	}

	@Override
	public List<Snippet> getUserSnippets(User owner) throws IOException {
		if (owner == null)
			return null;

		List<Snippet> snippets = new ArrayList<Snippet>(allSnippets.values());
		List<Snippet> result = new ArrayList<Snippet>();
		for (Snippet snippet : snippets)
			if (snippet.owner == owner)
				result.add(snippet);
		return result;
	}

	@Override
	public List<Snippet> getFavorited(User owner) throws IOException {
		if (owner == null)
			return null;

		List<Snippet> result = favorites.get(owner);
		if (result == null) {
			result = new ArrayList<Snippet>();
			favorites.put(owner, result);
		}
		return result;
	}

	@Override
	public List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException {
		if (matchingTags == null)
			return null;

		List<Snippet> result = new ArrayList<Snippet>();
		List<Snippet> snippets = new ArrayList<Snippet>(allSnippets.values());

		for (Snippet snippet : snippets)
			if (snippetMatchesTags(snippet, matchingTags))
				result.add(snippet);
		return result;

	}

	private boolean snippetMatchesTags(Snippet snippet, List<Tag> matchingTags) {
		List<Tag> tags = snippet.getTags();

		for (Tag tag : matchingTags)
			if (!tags.contains(tag))
				return false;
		return true;
	}

	@Override
	public List<Snippet> getSnippets(Category category) throws IOException {
		if (category == null)
			return null;

		List<Snippet> result = new ArrayList<Snippet>();
		List<Snippet> snippets = new ArrayList<Snippet>(allSnippets.values());

		for (Snippet snippet : snippets)
			if (snippet.getCategory().equals(category))
				result.add(snippet);
		return result;
	}

	@Override
	public List<Comment> getComments(Snippet snippet) throws IOException {
		if (snippet == null)
			return null;

		HashMap<Integer, Comment> comments = allComments.get(snippet);
		if (comments == null) {
			comments = new HashMap<Integer, Comment>();
			allComments.put(snippet, comments);
		}
		return new ArrayList<Comment>(comments.values());
	}

	@Override
	public List<Tag> getTags(Snippet snippet) throws IOException {
		if (snippet == null)
			return null;

		List<Tag> tags = snippetTags.get(snippet);
		if (tags == null) {
			tags = new ArrayList<Tag>();
			snippetTags.put(snippet, tags);
		}
		return tags;
	}

	@Override
	public List<Notification> getNotifications(User user, boolean unreadOnly) throws IOException {
		// XXX This method is not yet needed and therefore not tested

		if (user == null)
			return null;

		List<Notification> list = notifications.get(user);
		if (list == null) {
			list = new ArrayList<Notification>();
			notifications.put(user, list);
		}
		if (unreadOnly) {
			List<Notification> result = new ArrayList<Notification>();
			for (Notification item : list)
				if (!item.isRead())
					result.add(item);
			return result;
		} else
			return list;
	}

	@Override
	public List<Code> getCodes(Snippet snippet) throws IOException {
		if (snippet == null)
			return null;

		HashMap<Integer, Code> codes = allCodes.get(snippet);
		if (codes == null) {
			codes = new HashMap<Integer, Code>();
			allCodes.put(snippet, codes);
		}
		return new ArrayList<Code>(codes.values());
	}

	@Override
	public Category getCategory(Snippet snippet) throws IOException {
		if (snippet == null)
			return null;
		return snippet.getCategory();
	}

	@Override
	public Category getParentCategory(Category category) throws IOException {
		if (categoryTree.getParent(category) == null)
			return null;
		return categoryTree.getParent(category).value();
	}

	@Override
	public List<Category> getSubcategories(Category category) throws IOException {
		if (category == null)
			return null;

		return categoryTree.getChildren(category);
	}

	@Override
	public List<String> getAllLanguages() throws IOException {
		return allLanguages;
	}

	@Override
	public List<Pair<User, Integer>> getRatings(Snippet snippet) throws IOException {
		if (snippet == null)
			return null;

		HashMap<User, Integer> list = ratings.get(snippet);
		if (list == null)
			return new ArrayList<Pair<User, Integer>>();
		List<Pair<User, Integer>> result = new ArrayList<Pair<User, Integer>>();
		List<Entry<User, Integer>> set = new ArrayList<Entry<User, Integer>>(list.entrySet());
		for (Entry<User, Integer> entry : set)
			result.add(new Pair<User, Integer>(entry.getKey(), entry.getValue()));
		return result;
	}

	@Override
	public Pair<Integer, Integer> getVotes(Comment comment) throws IOException {
		// Pair<positiveVotes, negativeVotes>
		if (comment == null)
			return null;

		HashMap<User, Integer> votes = votings.get(comment);
		if (votes == null)
			return new Pair<Integer, Integer>(0, 0);
		int chocolates = 0;
		int lemons = 0;
		List<Integer> iVotes = new ArrayList<Integer>(votes.values());
		for (Integer i : iVotes)
			if (i < 0)
				lemons++;
			else if (i > 0)
				chocolates++;

		return new Pair<Integer, Integer>(chocolates, lemons);
	}

	@Override
	public List<Snippet> search(String searchString, Integer min, Integer max) throws IOException {
		// TODO Eliminate min and max, that are ignored
		
		/*
		 * NOTE: Search string is NOT case senstitive
		 */
		
		if (searchString == null) return null;
		searchString = searchString.trim().toLowerCase();
		if (searchString.isEmpty()) return null;
		
		List<Snippet> results = new ArrayList<Snippet>();
		List<Snippet> snippets = new ArrayList<Snippet>(allSnippets.values());
		
		for (Snippet snippet : snippets) {
			String name = snippet.getName().trim().toLowerCase();
			
			if (searchString.contains(name) || name.contains(searchString))
				results.add(snippet);
		}
		
		return results;
	}

	@Override
	public int getUserCount() throws IOException {
		return allUsers.size();
	}

	@Override
	public int getCategoryCount() throws IOException {
		return categoryTree.size();
	}

	@Override
	public int getSnippetsCount() throws IOException {
		return allSnippets.size();
	}

	@Override
	public int getTagsCount() throws IOException {
		return allTags.size();
	}

	@Override
	public void writePassword(User user, String password, int mode) throws IOException {
		if (user == null || password == null)
			return;

		passwords.put(user, password);
	}

	@Override
	public String getPassword(User user) throws IOException, UnsupportedOperationException {
		throw new UnsupportedOperationException("getPassword unsupported. Use verifyPassword");
	}

	@Override
	public boolean verifyPassword(User user, String password) throws IOException {
		if (user == null || password == null || password.isEmpty())
			return false;

		String pass = passwords.get(user);
		if (pass == null)
			return false;
		return password.equals(pass);
	}

	@Override
	public Float getAverageRating(Snippet snippet) throws IOException {
		if (snippet == null)
			return null;

		HashMap<User, Integer> list = ratings.get(snippet);
		if (list == null)
			return new Float(0);
		int sum = 0;
		int num = 0;
		List<Entry<User, Integer>> set = new ArrayList<Entry<User, Integer>>(list.entrySet());
		if (set.size() == 0)
			return new Float(0);
		for (Entry<User, Integer> entry : set) {
			sum += entry.getValue();
			++num;
		}
		return ((float) sum / (float) num);
	}

	@Override
	public List<Snippet> getSnippets(Category category, Integer start, Integer count) throws IOException {
		if (category == null || start < 0 || count < 0)
			return null;

		List<Snippet> snippets = getSnippets(category);
		if (start > snippets.size())
			return new ArrayList<Snippet>();

		/* Cut list to desired length */
		while (start > 0) {
			snippets.remove(0);
			start--;
		}

		while (count > snippets.size())
			snippets.remove(count);

		return snippets;
	}

	@Override
	public Comment getComment(Long id) throws IOException {
		if (id == null)
			return null;
		return commentMap.get(id);
	}

	@Override
	public List<Category> getAllCategories() throws IOException {
		return new ArrayList<Category>(categoryTree.flatten());
	}

	@Override
	public Category getCategory(String name) throws IOException {
		Iterator<Category> iterator = categoryTree.iterator();
		while (iterator.hasNext()) {
			Category category = iterator.next();
			if (category.getName().equalsIgnoreCase(name))
				return category;
		}
		return null;
	}

	@Override
	public void unRate(User user, Snippet snippet, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTag(Tag tag, int mode) throws IOException {
		if (tag == null)
			return;

		// TODO Search for tags and remove them

		allTags.remove(tag);
	}

	@Override
	public void removeLanguage(String language, int mode) throws IOException {
		if (language == null || language.isEmpty())
			return;
		language = language.trim();

		for (String lang : allLanguages) {
			if (lang.trim().equalsIgnoreCase(language)) {
				allLanguages.remove(lang);
			}
		}
	}

	@Override
	public Integer getVote(User user, Comment comment) throws IOException {
		if (user == null || comment == null)
			return null;

		HashMap<User, Integer> votes = votings.get(comment);
		if (votes == null)
			return null;
		return votes.get(user);
	}

	@Override
	public Snippet getSnippet(Long hashid) throws IOException {
		if (hashid == null)
			return null;
		return allSnippets.get(hashid);
	}

	@Override
	public void removeUser(User user, int mode) throws IOException {
		if (user == null)
			return;

		allUsers.remove(user.username);
		// TODO: Cross references should also be removed some day ...
	}

	@Override
	public void removeSnippet(Snippet snippet, int mode) throws IOException {
		if (snippet == null)
			return;

		allSnippets.remove(snippet.getHashId());
	}

	@Override
	public void removeComment(Comment comment, int mode) throws IOException {
		if (comment == null)
			return;

		HashMap<Integer, Comment> comments = allComments.get(comment.snippet);
		if (comment != null)
			comments.remove(comment.getHashID());
		commentMap.remove(comment.getHashID());
	}

	@Override
	public void removeNotification(Notification notification, int mode) throws IOException {
		// TODO Not implemented because not used currently

	}

	@Override
	public void removeCode(Code code, int mode) throws IOException {
		if (code == null)
			return;

		Snippet snippet = code.snippet;
		HashMap<Integer, Code> codeHistory = allCodes.get(snippet);
		codeHistory.remove(code.getHashID());
	}

	@Override
	public void removeCategory(Category category, int mode) throws IOException {
		if (category == null)
			return;
		categoryTree.deleteItem(category);
	}

	@Override
	public List<Tag> getAllTags(Integer start, Integer count) throws IOException {
		if (start < 0 || count < 0)
			return null;
		if (start > allTags.size())
			return new ArrayList<Tag>();

		List<Tag> result = new ArrayList<Tag>(allTags);
		while (start > 0) {
			result.remove(0);
			start--;
		}
		while (count > result.size()) {
			result.remove(0);
		}

		return result;
	}

	// YEAH!! Keyword WAR!!!!
	protected synchronized final long createNewSnippetId() {
		long id;
		do {
			id = nextInteger();
		} while (allSnippets.containsKey(id));
		return id;
	}

	private static long nextInteger() {
		return (long) (Math.random() * Integer.MAX_VALUE);
	}
}
