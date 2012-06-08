package org.smartsnip.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XComment implements IsSerializable {
	public String owner;
	public long snippet;
	public String message;
	public int positiveVotes;
	public int negativeVotes;
	public Date time;
	public long id;

	private XComment() {

	}

	public XComment(String owner, long id, long snippet, String message,
			int positiveVotes, int negativeVotes, Date time) {
		this.owner = owner;
		this.id = id;
		this.snippet = snippet;
		this.message = message;
		this.positiveVotes = positiveVotes;
		this.negativeVotes = negativeVotes;
		this.time = time;
	}

}
