package org.smartsnip.client;

import org.smartsnip.shared.IComment;
import org.smartsnip.shared.XComment;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class CommentField extends Composite {

	private HorizontalPanel horPanel;
	private HTMLPanel commentText;
	
	public CommentField(XComment myComment) {
		
		horPanel = new HorizontalPanel();
		commentText = new HTMLPanel(myComment.message);
		horPanel.add(commentText);
		
		
		initWidget(horPanel);
	    // Give the overall composite a style name.
	    setStyleName("commentField");
	}
	
	
}
