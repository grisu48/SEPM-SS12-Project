package org.smartsnip.client;

import org.smartsnip.shared.XComment;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentArea extends Composite {

	private VerticalPanel vertPanel;
	private HorizontalPanel horPanel;
	private TextArea myComment;
	private Button btnSend;

	public CommentArea(final XSnippet snip) {

		vertPanel = new VerticalPanel();
		horPanel = new HorizontalPanel();

		if (snip.comments != null) {
			for (XComment i : snip.comments) {
				vertPanel.add(new CommentField(i));
			}
		} else
			vertPanel.add(new Label("CommentList is null"));

		myComment = new TextArea();
		btnSend = new Button("Send");
		btnSend.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				String comment = myComment.getText();
				if (comment.isEmpty())
					return;

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
