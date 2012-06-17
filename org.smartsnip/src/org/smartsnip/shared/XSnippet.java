package org.smartsnip.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XSnippet implements IsSerializable {
	public String owner;
	public long hash;
	public String title;
	public String description;
	public String category;
	public ArrayList<String> tags;
	public String code;
	public String codeHTML;
	public String language;
	public String license;
	public int viewcount;
	public boolean isFavorite;
	public boolean canEdit;
	public boolean canDelete;
	public float rating;

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
}
