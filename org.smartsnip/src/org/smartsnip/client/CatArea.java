package org.smartsnip.client;


import org.smartsnip.shared.ISnippet;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CatArea extends Composite {
	
	private FlowPanel myPanel;
	private Label title;
	
	public CatArea() {
		myPanel = new FlowPanel();
		title = new Label("Categories");
		myPanel.add(title);
		myPanel.add(new Button("Test1"));
		myPanel.add(new Button("Test2"));
		myPanel.add(new Button("Test3"));
	
		initWidget(myPanel);
	    // Give the overall composite a style name.
	    setStyleName("catArea");
	}
	
	/*
	public CatArea(ISnippet mySnip) {
		vertPanel = new VerticalPanel();
		title = new Label("Categories");
		vertPanel.add(title);
		for (String i : mySnip.getTags()) {
			vertPanel.add(new Button(i));
		}
		initWidget(vertPanel);
	    // Give the overall composite a style name.
	    setStyleName("catArea");
	}*/

}
