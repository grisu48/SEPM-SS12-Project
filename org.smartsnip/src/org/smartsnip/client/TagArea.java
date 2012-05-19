package org.smartsnip.client;



import java.util.List;

import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;


public class TagArea extends Composite {
	
	private FlowPanel myPanel;
	private Label title;
	
	public TagArea() {
		myPanel = new FlowPanel();
		title = new Label("Tags");
		myPanel.add(title);
		initWidget(myPanel);
	    // Give the overall composite a style name.
	    setStyleName("tagArea");
	}

	public void update(List<String> tagsAppearingInSearchString) {
		for (String i : tagsAppearingInSearchString) {
				myPanel.add(new Button(i));
		}
		
	}
	

}
