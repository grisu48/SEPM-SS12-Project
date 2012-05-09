package org.smartsnip.client;


import java.util.List;

import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.XSnippet;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CatArea extends Composite {
	
	private FlowPanel myPanel;
	private Label title;
	
	public CatArea(List<XSnippet> mySnipList) {
		myPanel = new FlowPanel();
		title = new Label("Categories");
		myPanel.add(title);
		
		for (XSnippet i : mySnipList) {
				myPanel.add(new Button(i.category.toString()));
		}
	
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
