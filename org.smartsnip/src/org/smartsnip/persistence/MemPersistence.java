package org.smartsnip.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.smartsnip.core.Category;
import org.smartsnip.core.Code;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Notification;
import org.smartsnip.core.Pair;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;

/**
 * Testing persistence layer that is completely in the memory
 * 
 * @author phoenix
 * 
 */
public class MemPersistence implements IPersistence {

	private final HashMap<String, User> allUsers = new HashMap<String, User>();
	private final HashMap<Integer, Snippet> allSippets = new HashMap<Integer, Snippet>();
	private final HashMap<Snippet, Comment> allComments = new HashMap<Snippet, Comment>();
	private final List<Tag> allTags = new ArrayList<Tag>();
	private final HashMap<User, List<Notification>> notifications = new HashMap<User, List<Notification>>();

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
	public void writeSnippet(Snippet snippet, int mode) throws IOException {
		if (snippet == null)
			return;

		allSippets.put(snippet.hash, snippet);
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
	public void writeComment(Comment comment, int mode) throws IOException {
		if (comment == null)
			return;

		allComments.put(comment.snippet, comment);
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
	public void writeNotification(Notification notification, int mode) throws IOException {
		if (notification == null)
			return;

		User owner = notification.getOwner();
		List<Notification> notifications = this.notifications.get(owner);
		if (notifications == null) {
			notifications = new ArrayList<Notification>();
			this.notifications.put(owner, notifications);
		}
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
	public void writeCode(Code code, int mode) throws IOException {

	}

	@Override
	public void writeCode(List<Code> codes, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeCategory(Category category, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeCategory(List<Category> categories, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeLanguage(String language, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeRating(Integer rating, Snippet snippet, User user, int mode) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeVote(Integer vote, Comment comment, User user, int mode) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void votePositive(User user, Comment comment, int mode) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void voteNegative(User user, Comment comment, int mode) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void unVote(User user, Comment comment, int mode) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void addFavourite(Snippet snippet, User user, int mode) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeFavourite(Snippet snippet, User user, int mode) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public User getUser(String nick) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByEmail(String email) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findUser(String realName) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Snippet> getUserSnippets(User owner) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Snippet> getFavorited(User owner) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Snippet> getSnippets(Category category) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> getComments(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tag> getTags(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tag> getAllTags() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Notification> getNotifications(User user, boolean unreadOnly) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Code> getCodes(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pair<User, Integer>> getRatings(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Integer, Integer> getVotes(Comment comment) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Snippet> search(String searchString) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUserCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCategoryCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSnippetsCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTagsCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
