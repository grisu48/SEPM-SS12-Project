package org.smartsnip.client;


import org.smartsnip.shared.ISnippet;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SortArea extends Composite {
	
	private FlowPanel myPanel;
	private Label title;
	
	public SortArea() {
		myPanel = new FlowPanel();
		title = new Label("Sorting");
		myPanel.add(title);
		myPanel.add(new Button("All"));
		myPanel.add(new Button("Highest Rated"));
		myPanel.add(new Button("Mostly Viewed"));
		myPanel.add(new Button("Recently Viewed"));
		myPanel.add(new Button("My Favourites"));
	
		initWidget(myPanel);
	    // Give the overall composite a style name.
	    setStyleName("sortArea");
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
