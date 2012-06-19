package org.smartsnip.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XSnippet implements IsSerializable, Cloneable {
	public String owner;
	public long hash;
	public String title;
	public String description;
	public String category;
	public List<String> tags;
	public String code;
	public String codeHTML;
	public String language;
	public String license;
	public int viewcount;
	public boolean isFavorite;
	public boolean canEdit;
	public boolean canDelete;
	public float rating;
	public int myRating;
	public boolean isOwn;
	public boolean canRate;

	/** Used by GWT */
	private XSnippet() {
		// this("", 0, "", "", null, null, null, "", "", "", "", 0);
	}

	public XSnippet(String owner, long hash, String title, String description,
			String category, ArrayList<String> tags, String code,
			String codeHTML, String language, String license, int viewcount) {
		this.owner = owner;
		this.hash = hash;
		this.title = title;
		this.description = description;
		this.category = category;
		this.tags = tags;
		this.code = code;
		this.codeHTML = codeHTML;
		this.language = language;
		this.license = license;
		this.viewcount = viewcount;
		this.isFavorite = false;
	}

	public XSnippet(String owner, long hash, String title, String description,
			String category, ArrayList<String> tags, String code,
			String codeHTML, String language, String license, int viewcount,
			boolean isFavorite) {
		this.owner = owner;
		this.hash = hash;
		this.title = title;
		this.description = description;
		this.category = category;
		this.tags = tags;
		this.code = code;
		this.codeHTML = codeHTML;
		this.language = language;
		this.license = license;
		this.viewcount = viewcount;
		this.isFavorite = isFavorite;
	}

	@SuppressWarnings("unchecked")
	@Override
	public XSnippet clone() {
		XSnippet clone = new XSnippet();

		clone.canDelete = canDelete;
		clone.canEdit = canEdit;
		clone.canRate = canRate;
		clone.category = category;
		clone.code = code;
		clone.codeHTML = codeHTML;
		clone.description = description;
		clone.hash = hash;
		clone.isFavorite = isFavorite;
		clone.isOwn = isOwn;
		clone.language = language;
		clone.license = license;
		clone.myRating = myRating;
		clone.owner = owner;
		clone.rating = rating;
		clone.tags = cloneTags();
		clone.title = title;
		clone.viewcount = viewcount;

		return clone;
	}

	/**
	 * @return cloned tag list
	 */
	private List<String> cloneTags() {
		List<String> clone = new ArrayList<String>(tags.size());
		for (String tag : tags)
			clone.add(tag);
		return clone;
	}
}
