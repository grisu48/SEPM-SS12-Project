package org.smartsnip.client;

import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class CatArea extends Composite {

	private final FlowPanel myPanel;
	private final Label title;

	public CatArea() {
		myPanel = new FlowPanel();
		title = new Label("Categories");
		myPanel.add(title);
		initWidget(myPanel);
		// Give the overall composite a style name.
		setStyleName("catArea");
	}

	public void update(List<String> categories) {
		if (categories == null)
			return;

		for (String i : categories) {
			myPanel.add(new Button(i));
		}

	}

}
