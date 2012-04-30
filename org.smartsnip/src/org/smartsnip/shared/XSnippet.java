package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XSnippet implements IsSerializable {
	public final String owner;
	public final int hash;
	public final String description;
	public final List<String> tags;
	public final List<Integer> comment;
	public final String code;
	public final String codeHTML;
	public final String language;
	public final String license;
	public final int viewcount;

	public XSnippet(String owner, int hash, String description, List<String> tags, List<Integer> comment, String code,
			String codeHTML, String language, String license, int viewcount) {
		this.owner = owner;
		this.hash = hash;
		this.description = description;
		this.tags = tags;
		this.comment = comment;
		this.code = code;
		this.codeHTML = codeHTML;
		this.language = language;
		this.license = license;
		this.viewcount = viewcount;
	}
}
