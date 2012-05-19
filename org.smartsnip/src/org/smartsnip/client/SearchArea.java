package org.smartsnip.client;

import org.smartsnip.shared.XSearch;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;

public class SearchArea extends Composite {

	private final HorizontalPanel horPanel;
	private final SuggestBox searchSnippet;
	private final Button searchButton;
	private final Label lblStatus;

	/** Search duration timer */
	private long searchDuration = 0L;

	public SearchArea() {

		horPanel = new HorizontalPanel();
		
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();  
		   oracle.add("Cat");
		   oracle.add("Dog");
		   oracle.add("Horse");
		   oracle.add("Canary");
		searchSnippet = new SuggestBox(oracle);
		searchButton = new Button("Search Snippet");
		searchButton.addStyleName("searchButton");
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				searchDuration = System.currentTimeMillis();
				control.search(searchSnippet.getText(), null, null,
						XSearch.SearchSorting.highestRated, 0, 10,
						SearchArea.this);
			}
		});
		lblStatus = new Label("");
		horPanel.add(searchSnippet);
		horPanel.add(searchButton);
		horPanel.add(lblStatus);

		initWidget(horPanel);
		// Give the overall composite a style name.
		setStyleName("searchArea");
	}

	/**
	 * Callback if the search failed with an exception
	 * 
	 * @param caught
	 *            Cause why the search failed
	 */
	void seachFailed(Throwable caught) {
		lblStatus.setText("Search failed: " + caught.getMessage());
	}

	/**
	 * Callback if the search succeeds
	 * 
	 * @param result
	 *            Of the search
	 */
	void searchDone(XSearch result) {
		searchDuration = System.currentTimeMillis() - searchDuration;
		searchDuration = searchDuration / 10;
		double time = searchDuration / 100.0D;
		lblStatus.setText(result.totalresults + " results in " + time + " ms");

	}

}
