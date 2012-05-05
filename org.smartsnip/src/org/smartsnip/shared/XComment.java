package org.smartsnip.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XComment implements IsSerializable {
	public final String owner;
	public final long snippet;
	public final String message;
	public final int positiveVotes;
	public final int negativeVotes;
	public final Date time;

	public XComment(String owner, long snippet, String message, int positiveVotes, int negativeVotes, Date time) {
		this.owner = owner;
		this.snippet = snippet;
		this.message = message;
		this.positiveVotes = positiveVotes;
		this.negativeVotes = negativeVotes;
		this.time = time;
	}

}
