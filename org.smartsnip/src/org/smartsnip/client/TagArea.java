package org.smartsnip.client;



import java.util.List;

import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;


public class TagArea extends Composite {
	
	private FlowPanel myPanel;
	private Label title;
	
	public TagArea(List <XSnippet> mySnipList) {
		myPanel = new FlowPanel();
		title = new Label("Tags");
		myPanel.add(title);
		
		for (XSnippet i : mySnipList) {
			for (String j: i.tags) {
				myPanel.add(new Button(j));
			}
		}

	
		initWidget(myPanel);
	    // Give the overall composite a style name.
	    setStyleName("tagArea");
	}
	

}
