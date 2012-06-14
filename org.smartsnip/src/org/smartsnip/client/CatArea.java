package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.shared.XSearch;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class CatArea extends Composite {

	private final FlowPanel myPanel;
	private final Label title;
	private final Control control;

	public CatArea() {
		control = Control.getInstance();
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

		for (final String i : categories) {
			Button catButton = new Button(i);
			catButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					ArrayList<String> catList = new ArrayList<String>();
					catList.add(i);
					control.search(Control.myGUI.mySearchArea.getSearchText(), null, catList,
							XSearch.SearchSorting.highestRated, 0, 10, Control.myGUI.mySearchArea);
				}
			});
			myPanel.add(catButton);
		}

	}

}
