package org.smartsnip.client;


import org.smartsnip.shared.ISnippet;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TagArea extends Composite {
	
	private FlowPanel myPanel;
	private Label title;
	
	public TagArea() {
		myPanel = new FlowPanel();
		title = new Label("Tags");
		myPanel.add(title);
		myPanel.add(new Button("So ein Tag"));
		myPanel.add(new Button("so wun"));
		myPanel.add(new Button("derschön"));
		myPanel.add(new Button("wie heute"));
	
		initWidget(myPanel);
	    // Give the overall composite a style name.
	    setStyleName("tagArea");
	}
	
	/*
	public TagArea(ISnippet mySnip) {
		vertPanel = new VerticalPanel();
		title = new Label("Tags");
		vertPanel.add(title);
		for (String i : mySnip.getTags()) {
			vertPanel.add(new Button(i));
		}
		initWidget(vertPanel);
	    // Give the overall composite a style name.
	    setStyleName("tagArea");
	}*/

}
