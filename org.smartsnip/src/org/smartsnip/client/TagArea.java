package org.smartsnip.client;


import org.smartsnip.core.ISnippet;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TagArea extends Composite {
	
	private VerticalPanel vertPanel;
	private Label title;
	
	public TagArea() {
		vertPanel = new VerticalPanel();
		title = new Label("Tags");
		vertPanel.add(title);
		vertPanel.add(new Button("Test1"));
		vertPanel.add(new Button("Test2"));
		vertPanel.add(new Button("Test3"));
	
		initWidget(vertPanel);
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
