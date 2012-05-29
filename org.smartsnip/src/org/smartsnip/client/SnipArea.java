package org.smartsnip.client;


import org.smartsnip.shared.XSnippet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

import com.google.gwt.user.client.ui.Composite;
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
	private EditableLabel title;
	private EditableLabel description;
	private EditableLabel language;
	private EditableLabel license;
	private HTMLPanel snipFull;
	private Button btnFav;
	private Button btnRate;

	
	SnipArea(final XSnippet mySnip) {
	
		vertPanel = new VerticalPanel();
		horPanel = new HorizontalPanel();
		scrPanel = new ScrollPanel();
		properties = new Grid(4,1);
		title = new EditableLabel("titel");
		description = new EditableLabel(mySnip.description);
		language = new EditableLabel(mySnip.language);
		license = new EditableLabel(mySnip.license);
		snipFull = new HTMLPanel(mySnip.codeHTML);
		
		properties.setWidget(0, 0, title );
		properties.setWidget(1, 0, description);
		properties.setWidget(2, 0, language);
		properties.setWidget(3, 0, license);
		
		
		btnFav = new Button("Add to Favourites");
		btnFav.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				control.toFav(mySnip.hash);
			}
		});
		
		
		
		
		btnRate = new Button("Rate this Snippet");
		btnRate.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				int rate = 3;
				control.rateSnippet(rate, mySnip.hash);
			}
		});
		

		
		
	
		
		
		
		
		vertPanel.add(properties);
		vertPanel.add(scrPanel);
		vertPanel.add(horPanel);
		scrPanel.add(snipFull);
		horPanel.add(btnRate);
		horPanel.add(btnFav);

		
		initWidget(vertPanel);
	    // Give the overall composite a style name.
	    setStyleName("snipArea");
	}


	public void update() {
		//if necessary
	}
	
}
