package org.smartsnip.shared;

import java.util.List;

import org.smartsnip.shared.XComment;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XSnippet implements IsSerializable {
	public final String owner;
	public final long hash;
	public final String title;
	public final String description;
	public final XCategory category;
	public final List<String> tags;
	public final List<XComment> comments;
	public final String code;
	public final String codeHTML;
	public final String language;
	public final String license;
	public final int viewcount;

	public XSnippet(String owner, long hash, String title, String description, XCategory category, List<String> tags, List<XComment> comments, String code,
			String codeHTML, String language, String license, int viewcount) {
		this.owner = owner;
		this.hash = hash;
		this.title = title;
		this.description = description;
		this.category = category;
		this.tags = tags;
		this.comments = comments;
		this.code = code;
		this.codeHTML = codeHTML;
		this.language = language;
		this.license = license;
		this.viewcount = viewcount;
	}
}
