package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ICommentAsync {

	void getServerStatus(AsyncCallback<XServerStatus> callback);

}
