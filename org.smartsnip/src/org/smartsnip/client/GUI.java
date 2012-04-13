package org.smartsnip.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

public class GUI implements EntryPoint {

	// All necessary panels and widgets

	// Create userPanel
	HorizontalPanel userPanel = new HorizontalPanel();
	Label user = new Label("guest");
	
	// Create loginPanel
	PopupPanel loginPanel = new PopupPanel();
	
	
	// Create searchPanel
	HorizontalPanel searchPanel = new HorizontalPanel();
	SuggestBox searchSnippet = new SuggestBox();
	Button searchButton = new Button("Search");
	
	
	// Create dataPanel
	HorizontalPanel dataPanel = new HorizontalPanel();
	
	TabPanel tabPanel = new TabPanel();
	
	VerticalPanel rightPanel = new VerticalPanel();
	FlowPanel catsPanel = new FlowPanel();
	FlowPanel tagsPanel = new FlowPanel();
	
	Button test1 = new Button("test");
	Button test2 = new Button("test");
	Button test3 = new Button("test");
	Button test4 = new Button("test");
	
	VerticalPanel allPanel = new VerticalPanel();
	VerticalPanel highPanel = new VerticalPanel();
	VerticalPanel mostPanel = new VerticalPanel();
	VerticalPanel recePanel = new VerticalPanel();
	VerticalPanel favPanel = new VerticalPanel();
	
	
	
	
	


	@Override
	public void onModuleLoad() {
	

		// hier kann eine initialisierung vorgenommen werden
		// z.b. stocksFlexTable.setText(0, 1, "Price");

		// hier werden die widgets in einander gebaut
		// z.b. addPanel.add(addStockButton);

		// Make CSS-Classes
		searchButton.addStyleName("searchButton");
		searchSnippet.addStyleName("searchSnippet");
		tabPanel.addStyleName("tabPanel");
		
		
		// Fill userPanel
		userPanel.add(user);
		
		// Fill searchPanel
		searchPanel.add(searchSnippet);
		searchPanel.add(searchButton);
		

		// Fill dataPanel
		dataPanel.add(tabPanel);
		dataPanel.add(rightPanel);
		tabPanel.add(allPanel, "All");
		tabPanel.add(highPanel, "Highest Rated");
		tabPanel.add(mostPanel, "Mostly Viewed");
		tabPanel.add(recePanel, "Recently Viewed");
		tabPanel.add(favPanel, "Favorites");
		rightPanel.add(catsPanel);
		rightPanel.add(tagsPanel);
		catsPanel.add(test1);
		catsPanel.add(test2);
		tagsPanel.add(test3);
		tagsPanel.add(test4);
		


		
		

		// Fix Panels to Divs
		RootPanel.get("user").add(userPanel);
		RootPanel.get("search").add(searchPanel);
		RootPanel.get("data").add(dataPanel);
		

		// hier wird der cursor gesetzt
		// z.B. ewSymbolTextBox.setFocus(true);

	}

}
