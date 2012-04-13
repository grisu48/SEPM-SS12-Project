package org.smartsnip.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

public class GUI implements EntryPoint {

	// Create the common used Panels

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
	
	

	@Override
	public void onModuleLoad() {
	
		// Make CSS-Classes
		searchButton.addStyleName("searchButton");
		searchSnippet.addStyleName("searchSnippet");
		
		createBasicPage();
		//showSearchPage();
		//showSnipPage();
		showPersonalPage();
	}
	
	
	
	
	public void createBasicPage() {
		// Fill userPanel
		userPanel.add(user);	
		
		// Fill searchPanel
		searchPanel.add(searchSnippet);
		searchPanel.add(searchButton);
		
		// Fix Panels to divs
		RootPanel.get("user").add(userPanel);
		RootPanel.get("search").add(searchPanel);
		RootPanel.get("data").add(dataPanel);
		
		// cursor?
		// z.B. ewSymbolTextBox.setFocus(true);
	}
	
	
	public void showSearchPage() {
		
		TabArea myTabArea = new TabArea();
		VerticalPanel rightPanel = new VerticalPanel();
		FlowPanel catsPanel = new FlowPanel();
		TagArea myTagArea = new TagArea();
		Button test1 = new Button("test");
		Button test2 = new Button("test");
		
		dataPanel.add(myTabArea);
		dataPanel.add(rightPanel);
		rightPanel.add(catsPanel);
		rightPanel.add(myTagArea);
		catsPanel.add(test1);
		catsPanel.add(test2);
		
	}
	
	public void showSnipPage() {
		SnipArea mySnipArea = new SnipArea();
		CommentArea myCommentArea = new CommentArea();
		dataPanel.add(mySnipArea);
		dataPanel.add(myCommentArea);
		
	}
	
	public void showPersonalPage() {
		PersonalArea myPersonalArea = new PersonalArea();
		VerticalPanel vertPanel = new VerticalPanel();
		dataPanel.add(myPersonalArea);
		dataPanel.add(vertPanel);
		
	}

}
