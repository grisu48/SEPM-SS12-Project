package org.smartsnip.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TabArea extends Composite {

	private TabPanel tabPanel;
	private VerticalPanel allPanel;
	private VerticalPanel highPanel;
	private VerticalPanel mostPanel;
	private VerticalPanel recePanel;
	private VerticalPanel favPanel;
	
	public TabArea() {
		
		tabPanel = new TabPanel();
		allPanel = new VerticalPanel();
		highPanel = new VerticalPanel();
		mostPanel = new VerticalPanel();
		recePanel = new VerticalPanel();
		favPanel = new VerticalPanel();
		tabPanel.add(allPanel,"All Snippets");
		tabPanel.add(highPanel,"Highest Rated");
		tabPanel.add(mostPanel,"Mostly Viewed");
		tabPanel.add(recePanel,"Recenty Viewed");
		tabPanel.add(favPanel,"My Favorites");

		
		
		
		initWidget(tabPanel);
	    // Give the overall composite a style name.
	    setStyleName("tabArea");
	}
	
	
}
