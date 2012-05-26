package org.smartsnip.client;

import java.util.ArrayList;
import java.util.Date;

import org.smartsnip.shared.IComment;
import org.smartsnip.shared.XComment;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentArea extends Composite {
	
	private VerticalPanel vertPanel;
	private HorizontalPanel horPanel;
	private TextArea myComment;
	private Button send;
	
	
	public CommentArea(XSnippet mySnippet) {
		
		vertPanel = new VerticalPanel();
		horPanel = new HorizontalPanel();
		
		if (mySnippet.comments != null) {
			for (XComment i: mySnippet.comments) {
				vertPanel.add(new CommentField(i));
			}
		}
		
		
		myComment = new TextArea();
		send = new Button("Send");
		horPanel.add(myComment);
		horPanel.add(send);
		vertPanel.add(horPanel);
		
		
		initWidget(vertPanel);
	    // Give the overall composite a style name.
	    setStyleName("commentArea");
		
	}
	
	
	
	/*
	public CommentArea(ArrayList<IComment> comments) {
		
		vertPanel = new VerticalPanel();
		horPanel = new HorizontalPanel();
		for (IComment i : comments) {
			vertPanel.add(new CommentField(i));
		}
		myComment = new TextArea();
		send = new Button("Send");
		horPanel.add(myComment);
		horPanel.add(send);
		vertPanel.add(horPanel);
		
		
		initWidget(vertPanel);
	    // Give the overall composite a style name.
	    setStyleName("commentArea");
		
	}*/
	
	

}
