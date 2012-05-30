package org.smartsnip.client;

import org.smartsnip.shared.XComment;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentArea extends Composite {

	private XSnippet snippet;

	private VerticalPanel vertPanel;
	private Label lblComments;
	private VerticalPanel vertComments;
	private HorizontalPanel horPanel;
	private TextArea myComment;
	private Button btnSend;

	public CommentArea(final XSnippet snip) {

		this.snippet = snip;

		vertPanel = new VerticalPanel();
		vertComments = new VerticalPanel();
		horPanel = new HorizontalPanel();

		lblComments = new Label("");
		populateCommentField();
		vertPanel.add(lblComments);
		vertPanel.add(vertComments);

		myComment = new TextArea();
		btnSend = new Button("Send");
		btnSend.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (!Control.getInstance().isLoggedIn()) {
					lblComments.setText("Login first!");
					return;
				}
				Control control = Control.getInstance();
				String comment = myComment.getText();
				if (comment.isEmpty())
					return;

				lblComments.setText("Commenting ... ");
				control.writeComment(comment, snip.hash);
			}
		});

		horPanel.add(myComment);
		horPanel.add(btnSend);
		vertPanel.add(horPanel);

		initWidget(vertPanel);
		// Give the overall composite a style name.
		setStyleName("commentArea");

	}

	public void update() {
		lblComments.setText("Refreshing ... ");
		Control.proxySnippet.getSnippet(snippet.hash,
				new AsyncCallback<XSnippet>() {

					@Override
					public void onSuccess(XSnippet result) {
						lblComments.setText("Refresh done");
						if (result == null)
							return;
						snippet = result;
						populateCommentField();
					}

					@Override
					public void onFailure(Throwable caught) {
						lblComments.setText("Refresh failed: "
								+ caught.getMessage());
					}
				});
	}

	private void populateCommentField() {
		vertComments.clear();

		if (snippet.comments != null) {
			for (XComment i : snippet.comments) {
				vertComments.add(new CommentField(i));
			}
			int count = snippet.comments.size();
			lblComments.setText(count + " comment" + (count == 1 ? "" : "s"));
		} else {
			vertComments.add(new Label("CommentList is null"));
			lblComments.setText("No comments");
		}
	}

	/*
	 * public CommentArea(ArrayList<IComment> comments) {
	 * 
	 * vertPanel = new VerticalPanel(); horPanel = new HorizontalPanel(); for
	 * (IComment i : comments) { vertPanel.add(new CommentField(i)); } myComment
	 * = new TextArea(); send = new Button("Send"); horPanel.add(myComment);
	 * horPanel.add(send); vertPanel.add(horPanel);
	 * 
	 * 
	 * initWidget(vertPanel); // Give the overall composite a style name.
	 * setStyleName("commentArea");
	 * 
	 * }
	 */

}
