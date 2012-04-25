package org.smartsnip.client;

import org.smartsnip.shared.ISnippet;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShortSnipField extends Composite {

	private VerticalPanel vertPanel;
	private FlexTable properties;
	private HTMLPanel snipPreview;
	

	public ShortSnipField(ISnippet mysnip) {
		vertPanel = new VerticalPanel();
		properties = new FlexTable();
		snipPreview = new HTMLPanel("Test");
		vertPanel.add(properties);
		vertPanel.add(snipPreview);
		
		initWidget(vertPanel);
	    // Give the overall composite a style name.
	    setStyleName("shortSnipField");
		
	}

	
	
	
}
