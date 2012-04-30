package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface handles the interactions of the GUI on a concrete snippet. It
 * is given by the session to the GUI
 * 
 */
@RemoteServiceRelativePath("snippet")
public interface ISnippet extends RemoteService, IsSerializable {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static ISnippetAsync instance = null;

		/** Get the proxy object instance */
		public static ISnippetAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(ISnippet.class);
			}
			return instance;
		}
	}

	/**
	 * Gets a list of the comments of the snippet. The resulting list is sorted
	 * chronological.
	 * 
	 * If the given snippet hash doesn't exists, null is the result
	 * 
	 * The parameter start gives the start index of the total list of comments,
	 * the parameter count the maximum objects to pull.
	 * 
	 * @param snippet
	 *            Hash code of the source snippet
	 * @param start
	 *            Starting index of the list of total comments
	 * @param count
	 *            Maximum number of comments to pull
	 * @return a list containing at most count number of comments of the snippet
	 *         or null, if the snippet hash is not found
	 */
	public List<XComment> getComments(int snippet, int start, int count);

	/**
	 * @param snippet
	 *            Hash code of the source snippet
	 * @return the total number of comments or -1, if the given snippet hash is
	 *         not found
	 */
	public int getCommentCount(int snippet);

	/**
	 * Gets the snippet given with the hash, or null, if no such snippet exists
	 * 
	 * @param snippet
	 *            hash of the snippet to get
	 * @return a new {@link XSnippet} object with the attributes of the snippet,
	 *         or null, if no such snippet exists
	 */
	public XSnippet getSnippet(int snippet);
}
