package org.smartsnip.client;

import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;



public class SnipArea extends Composite {

	private VerticalPanel vertPanel;
	private HorizontalPanel horPanel;
	private ScrollPanel scrPanel;
	private Grid properties;
	private Label title;
	private Label description;
	private Label language;
	private Label license;
	private HTMLPanel snipFull;
	private Button fav;
	private Button del;
	private Button edit;
	private Button rate;
	
	
	SnipArea(XSnippet mySnip) {
	
		vertPanel = new VerticalPanel();
		horPanel = new HorizontalPanel();
		scrPanel = new ScrollPanel();
		properties = new Grid(4,1);
		title = new Label("titel");
		description = new Label(mySnip.description);
		language = new Label(mySnip.language);
		license = new Label(mySnip.license);
		snipFull = new HTMLPanel(mySnip.codeHTML);
		
		properties.setWidget(0, 0, title );
		properties.setWidget(1, 0, description);
		properties.setWidget(2, 0, language);
		properties.setWidget(3, 0, license);
		
		
		fav = new Button("Add to Favourites");
		rate = new Button("Rate this Snippet");
		del = new Button("Delete");
		edit = new Button("Edit");
		
		vertPanel.add(properties);
		vertPanel.add(scrPanel);
		vertPanel.add(horPanel);
		scrPanel.add(snipFull);
		horPanel.add(rate);
		horPanel.add(fav);
		horPanel.add(del);
		horPanel.add(edit);

		
		initWidget(vertPanel);
	    // Give the overall composite a style name.
	    setStyleName("snipArea");
	}
	
}
