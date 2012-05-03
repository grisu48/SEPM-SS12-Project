package org.smartsnip.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.smartsnip.core.Category;
import org.smartsnip.core.Code;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Notification;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.shared.Pair;

import sun.reflect.Reflection;

/**
 * Testing persistence layer that is completely in the memory
 * 
 * @author phoenix
 * 
 */
public class MemPersistence implements IPersistence {

	/**
	 * This constructor is protected against multiple instantiation to accomplish a singleton pattern.
	 * It rejects any attempt to build an instance except it is called by the {@link PersistenceFactory#getInstance(int)} method.
	 */
	MemPersistence() throws IllegalAccessException {
		super();
		if (Reflection.getCallerClass(2) == null
				|| Reflection.getCallerClass(2) != PersistenceFactory.class) {
			throw new IllegalAccessException(
					"Singleton pattern: caller must be PersistenceFactory class.");
		}
	}

	private class TreeItem<E> {
		private final List<E> children = new ArrayList<E>();
		private final List<TreeItem<E>> subTree = new ArrayList<TreeItem<E>>();

		public boolean contains(E e) {
			if (e == null)
				return false;

			if (children.contains(e))
				return true;
			for (TreeItem<E> item : subTree) {
				if (item.contains(e))
					return true;
			}

			return false;
		}

		public E get(E item) {
			if (item == null)
				return null;

			// If one of my children, then get it!
			int index = children.indexOf(item);
			if (index >= 0)
				return children.get(index);

			// Recursive call in all subtrees
			for (TreeItem<E> current : subTree) {
				E result = current.get(item);
				if (result != null)
					return result;
			}

			return null;

		}

		public int size() {
			int size = children.size();
			for (TreeItem<E> current : subTree) {
				size += current.size();
			}
			return size;
		}
	}

	private final HashMap<String, User> allUsers = new HashMap<String, User>();
	private final HashMap<Integer, Snippet> allSippets = new HashMap<Integer, Snippet>();
	private final HashMap<Snippet, List<Comment>> allComments = new HashMap<Snippet, List<Comment>>();
	private final List<Tag> allTags = new ArrayList<Tag>();
	private final List<String> allLanguages = new ArrayList<String>();
	private final HashMap<Snippet, List<Tag>> snippetTags = new HashMap<Snippet, List<Tag>>();
	private final HashMap<User, List<Notification>> notifications = new HashMap<User, List<Notification>>();
	private final HashMap<Snippet, List<Code>> allCodes = new HashMap<Snippet, List<Code>>();
	private final TreeItem<Category> categoryTree = new TreeItem<Category>();
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

		for (User user : users) {
			writeUser(user, mode);
		}
	}

	@Override
	public Long writeSnippet(Snippet snippet, int mode) throws IOException {
		if (snippet == null)
			return null;

		allSippets.put(snippet.hash, snippet);
		return new Long(snippet.hash);
	}

	@Override
	public void writeSnippet(List<Snippet> snippets, int mode) throws IOException {
		if (snippets == null)
			return;

		for (Snippet snippet : snippets) {
			writeSnippet(snippet, mode);
		}
	}

	@Override
	public Long writeComment(Comment comment, int mode) throws IOException {
		if (comment == null)
			return null;

		List<Comment> list = allComments.get(comment.snippet);
		if (list == null) {
			list = new ArrayList<Comment>();
			allComments.put(comment.snippet, list);
		}
		if (list.contains(comment))
			return new Long(list.indexOf(comment));

		list.add(comment);
		return new Long(list.indexOf(comment));
	}

	@Override
	public void writeComment(List<Comment> comments, int mode) throws IOException {
		if (comments == null)
			return;

		for (Comment comment : comments) {
			writeComment(comment, mode);
		}
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

		for (Tag tag : tags) {
			writeTag(tag, mode);
		}
	}

	@Override
	public Long writeNotification(Notification notification, int mode) throws IOException {
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
		if (notifications == null)
			return;

		for (Notification notification : notifications) {
			writeNotification(notification, mode);
		}
	}

	@Override
	public Long writeCode(Code code, int mode) throws IOException {
		if (code == null)
			return null;

		Snippet snippet = code.snippet;
		List<Code> codes = allCodes.get(snippet);
		if (codes == null) {
			codes = new ArrayList<Code>();
			allCodes.put(snippet, codes);
		} else {
			if (codes.contains(code))
				return new Long(codes.indexOf(code));
		}

		codes.add(code);
		return new Long(codes.indexOf(code));
	}

	@Override
	public void writeCode(List<Code> codes, int mode) throws IOException {
		if (codes == null)
			return;

		for (Code code : codes) {
			writeCode(code, mode);
		}
	}

	@Override
	public Long writeCategory(Category category, int mode) throws IOException {
		if (category == null)
			return null;

		if (categoryTree.contains(category))
			// TODO Implement an indexer
			return 0L;
		// TODO Implement me
		return 0L;
	}

	@Override
	public void writeCategory(List<Category> categories, int mode) throws IOException {
		// TODO Auto-generated method stub

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
			if (user.getRealName().contains(realName)) {
				results.add(user);
			}
		return results;
	}

	@Override
	public List<Snippet> getUserSnippets(User owner) throws IOException {
		if (owner == null)
			return null;

		List<Snippet> snippets = new ArrayList<Snippet>(allSippets.values());
		List<Snippet> result = new ArrayList<Snippet>();
		for (Snippet snippet : snippets)
			if (snippet.owner == owner) {
				result.add(snippet);
			}
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
		// TODO Auto-generated method stub
		return new ArrayList<Snippet>();
	}

	@Override
	public List<Snippet> getSnippets(Category category) throws IOException {
		// TODO Auto-generated method stub
		return new ArrayList<Snippet>();
	}

	@Override
	public List<Comment> getComments(Snippet snippet) throws IOException {
		if (snippet == null)
			return null;

		List<Comment> comments = allComments.get(snippet);
		if (comments == null) {
			comments = new ArrayList<Comment>();
			allComments.put(snippet, comments);
		}
		return comments;
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
	public List<Tag> getAllTags() throws IOException {
		return allTags;
	}

	@Override
	public List<Notification> getNotifications(User user, boolean unreadOnly) throws IOException {
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
				if (!item.isRead()) {
					result.add(item);
				}
			return result;
		} else
			return list;
	}

	@Override
	public List<Code> getCodes(Snippet snippet) throws IOException {
		if (snippet == null)
			return null;

		List<Code> codes = allCodes.get(snippet);
		if (codes == null) {
			codes = new ArrayList<Code>();
			allCodes.put(snippet, codes);
		}
		return codes;
	}

	@Override
	public Category getCategory(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category getParentCategory(Category category) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> getSubcategories(Category category) throws IOException {
		// TODO Auto-generated method stub
		return null;
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
		for (Entry<User, Integer> entry : set) {
			result.add(new Pair<User, Integer>(entry.getKey(), entry.getValue()));
		}
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
			if (i < 0) {
				lemons++;
			} else if (i > 0) {
				chocolates++;
			}

		return new Pair<Integer, Integer>(chocolates, lemons);
	}

	@Override
	public List<Snippet> search(String searchString, int min, int max) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUserCount() throws IOException {
		return allUsers.size();
	}

	@Override
	public int getCategoryCount() throws IOException {
		// TODO Auto-generated method stub
		return categoryTree.size();
	}

	@Override
	public int getSnippetsCount() throws IOException {
		return allSippets.size();
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
		if (set.size() == 0 ) return new Float(0);
		for (Entry<User, Integer> entry : set) {
			sum += entry.getValue();
			++num;
		}
		return ((float)sum / (float)num);
	}

	@Override
	public List<Snippet> getSnippets(Category category, int start, int count)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment getComment(Long id) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllCategories() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category getCategory(String name) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSubcategoryNames(Category category)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unRate(User user, Snippet snippet, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUser(String nickname, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSnippet(Long snippetId, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeComment(Long commentId, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTag(Tag tag, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeNotification(Long notificationId, int mode)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeReadNotifications(User user, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCode(Long codeId, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCategory(Long categoryId, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLanguage(String language, int mode) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getVote(User user, Comment comment) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTagFrequency(Tag tag) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Snippet getSnippet(int hash) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
