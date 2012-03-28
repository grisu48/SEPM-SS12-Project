package org.smartsnip.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GUI implements EntryPoint {

	// hier kommen alle benötigen gtw-widgets und panels rein
	//Label label = new Label("Hello GWT !!!");
	//Button button = new Button("Say something");
	
	//Create searchPanel
	HorizontalPanel searchPanel = new HorizontalPanel();
	TextBox searchSnippet = new TextBox();
	ListBox searchLanguage = new ListBox();
	Button searchButton = new Button("Search");
	//Create leftSide
	TabPanel leftPanel = new TabPanel();
	VerticalPanel contentPanel0 = new VerticalPanel();
	VerticalPanel contentPanel1 = new VerticalPanel();
	VerticalPanel contentPanel2 = new VerticalPanel();
	VerticalPanel contentPanel3 = new VerticalPanel();
	VerticalPanel contentPanel4 = new VerticalPanel();
	//Create rightSide
	VerticalPanel rightPanel = new VerticalPanel();
	Button cats = new Button("Categories");
	Button tags = new Button("Tags");
	
	
	
	@Override
	public void onModuleLoad() {
		
		//hier kann eine initialisierung vorgenommen werden
		//z.b. stocksFlexTable.setText(0, 1, "Price");
		
		//hier werden die widgets in einander gebaut
		//z.b.  addPanel.add(addStockButton);
		
		//Fill searchPanel
		searchPanel.add(searchSnippet);
		searchPanel.add(searchLanguage);
		searchPanel.add(searchButton);
		//Fill leftPanel
		leftPanel.add(contentPanel0, "All");
		leftPanel.add(contentPanel1, "Highest Rated");
		leftPanel.add(contentPanel2, "Mostly Viewed");
		leftPanel.add(contentPanel3, "Recently Viewd");
		leftPanel.add(contentPanel4, "Favourites"); 
		leftPanel.selectTab(0);
		//Fill rightPanel
		rightPanel.add(cats);
		rightPanel.add(tags);
		

	    //hier wird das hauptpanel oder widget zum rootpanel (div oder body) hinzugefuegt
		//z.b.
		//RootPanel.get().add(label);
		//RootPanel.get().add(button);
		
		//Fix Panels to Divs
		RootPanel.get("search").add(searchPanel);
		RootPanel.get("left").add(leftPanel);
		RootPanel.get("right").add(rightPanel);
		
		//hier wird der cursor gesetzt
		//newSymbolTextBox.setFocus(true);
		
		

		
	}
}