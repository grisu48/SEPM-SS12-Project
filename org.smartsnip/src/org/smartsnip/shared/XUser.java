package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XUser implements IsSerializable {
	public enum UserState {
		unvalidated, validated, deleted, moderator, administrator;
	}

	public String username;
	public String realname;
	public String email;

	public XUser() {

	}

	public XUser(String username, String realname, String email) {
		this.username = username;
		this.realname = realname;
		this.email = email;
	}

}
