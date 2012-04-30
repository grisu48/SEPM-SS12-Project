package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XUser implements IsSerializable {
	public final String username;
	public final String realname;
	public final String email;

	public XUser(String username, String realname, String email) {
		this.username = username;
		this.realname = realname;
		this.email = email;
	}

}
