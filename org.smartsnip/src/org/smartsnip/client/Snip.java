package org.smartsnip.client;

import org.smartsnip.core.ISnippet;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;



public class Snip extends Composite {

	private VerticalPanel vertPanel;
	private HorizontalPanel horPanel;
	private ScrollPanel scrPanel;
	private FlexTable properties;
	private HTMLPanel snipFull;
	private Button fav;
	private Button del;
	private Button edit;
	private Button rate;
	
	
	Snip(ISnippet mySnip) {
	
		vertPanel = new VerticalPanel();
		horPanel = new HorizontalPanel();
		scrPanel = new ScrollPanel();
		properties = new FlexTable();
		snipFull = new HTMLPanel("Test");
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
	    setStyleName("snip");
	}
	
}
