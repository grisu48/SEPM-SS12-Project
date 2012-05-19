package org.smartsnip.client;


import java.util.List;

import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSnippet;


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
		initWidget(myPanel);
	    // Give the overall composite a style name.
	    setStyleName("catArea");
	}

	
	public void update(List<String> categories) {
		for (String i : categories) {
			myPanel.add(new Button(i));
	}
		
	}
	
	


}
