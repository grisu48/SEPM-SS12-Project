package org.smartsnip.client;

import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class TagArea extends Composite {

	private final FlowPanel myPanel;
	private final Label title;

	public TagArea() {
		myPanel = new FlowPanel();
		title = new Label("Tags");
		myPanel.add(title);
		initWidget(myPanel);
		// Give the overall composite a style name.
		setStyleName("tagArea");
	}

	public void update(List<String> tagsAppearingInSearchString) {
		if (tagsAppearingInSearchString == null)
			return;

		for (String i : tagsAppearingInSearchString) {
			myPanel.add(new Button(i));
		}

	}

}
