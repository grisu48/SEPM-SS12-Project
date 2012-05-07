package org.smartsnip.client;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchArea extends Composite {
	
	private HorizontalPanel horPanel;
	private SuggestBox searchSnippet;
	private Button searchButton;
	public SearchArea() {
		
	    horPanel = new HorizontalPanel();
		searchSnippet = new SuggestBox();
		searchButton = new Button("Search Snippet");
		searchButton.addStyleName("searchButton");
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		          Control control = Control.getInstance();
		          control.search(searchSnippet.getText());
		        }

		      });
		horPanel.add(searchSnippet);
		horPanel.add(searchButton);
	
		initWidget(horPanel);
	    // Give the overall composite a style name.
	    setStyleName("searchArea");
	}

	


}
