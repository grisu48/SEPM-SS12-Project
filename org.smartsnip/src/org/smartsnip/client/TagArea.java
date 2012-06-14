package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import org.smartsnip.shared.XSearch;

public class TagArea extends Composite {

	private final FlowPanel myPanel;
	private final Label title;
	private Control control;

	public TagArea() {
		control = Control.getInstance();
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
			
		for (final String i : tagsAppearingInSearchString) {
			Button tagButton = new Button(i);
			tagButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					ArrayList<String> taglist = new ArrayList<String>();
					taglist.add(i);
					control.search(Control.myGUI.mySearchArea.getSearchText(), taglist, null,
							XSearch.SearchSorting.highestRated, 0, 10, Control.myGUI.mySearchArea);
				}
			});
			myPanel.add(tagButton);
		}
		
		
		
	}

}
