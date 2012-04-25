package org.smartsnip.client;

import java.util.ArrayList;

import org.smartsnip.shared.IComment;

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
	
	
	public CommentArea() {
		
		vertPanel = new VerticalPanel();
		horPanel = new HorizontalPanel();
		vertPanel.add(new CommentField("Test1"));
		vertPanel.add(new CommentField("Test2"));
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
