package org.smartsnip.client;

import org.smartsnip.core.IComment;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class CommentField extends Composite {

	private HorizontalPanel horPanel;
	private HTMLPanel commentText;
	
	public CommentField(String comment) {
		
		horPanel = new HorizontalPanel();
		commentText = new HTMLPanel(comment);
		horPanel.add(commentText);
		
		
		initWidget(horPanel);
	    // Give the overall composite a style name.
	    setStyleName("commentField");
	}
	
	
}
