package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.shared.ISnippet;
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

// TODO: Die Comment list in pages unterteilen!

public class CommentArea extends Composite {

	private final XSnippet snippet;

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
		vertPanel.add(lblComments);
		vertPanel.add(vertComments);

		myComment = new TextArea();
		// myComment.setStyleName("commentTxt");
		btnSend = new Button("Send");
		// btnSend.setStyleName("commentBtn");
		btnSend.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (!Control.getInstance().isLoggedIn()) {
					lblComments.setText("Login first!");
					return;
				}
				Control control = Control.getInstance();
				String comment = myComment.getText();
				if (comment.isEmpty()) return;

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

		update();

	}

	public void update() {
		lblComments.setText("Refreshing ... ");
		vertComments.clear();

		ISnippet.Util.getInstance().getComments(snippet.hash, 0, 50, new AsyncCallback<List<XComment>>() {

			@Override
			public void onSuccess(List<XComment> result) {
				if (result == null) result = new ArrayList<XComment>();

				for (XComment i : result) {
					vertComments.add(new CommentField(i));
				}
				int count = result.size();
				lblComments.setText(count + " comment" + (count == 1 ? "" : "s"));
			}

			@Override
			public void onFailure(Throwable caught) {
				vertComments.add(new Label("CommentList is null"));
				lblComments.setText("No comments");
			}
		});
	}

}
