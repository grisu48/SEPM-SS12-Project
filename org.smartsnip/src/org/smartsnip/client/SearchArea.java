package org.smartsnip.client;

import org.smartsnip.shared.XSearch;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class SearchArea extends Composite {

	private final HorizontalPanel horPanel;
	private final SuggestBox searchSnippet;
	private final Button searchButton;
	private final Button btCreateSnippet;
	private Control control;
	private String status;

	/** Search duration timer */
	private long searchDuration = 0L;

	public SearchArea() {
		
		control = Control.getInstance();

		horPanel = new HorizontalPanel();

		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.add("Java");
		oracle.add("Snippet");
		oracle.add("Quicksort");
		oracle.add("Smartsnip");
		searchSnippet = new SuggestBox(oracle);
		searchSnippet.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
					fireSearch();
				}
			}
		});

		searchButton = new Button("Search Snippet");
		searchButton.addStyleName("searchButton");
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				fireSearch();
			}
		});
		
		btCreateSnippet = new Button("Create Snippet");
		btCreateSnippet.setVisible(true);
		btCreateSnippet.addStyleName("createButton");
		btCreateSnippet.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Control.getInstance().changeSite('n');
			}
		});
		
		

		horPanel.add(searchSnippet);
		horPanel.add(searchButton);
		horPanel.add(btCreateSnippet);

		initWidget(horPanel);
		// Give the overall composite a style name.
		setStyleName("searchArea");
		
		
		update();
	}

	public void fireSearch() {
		Control control = Control.getInstance();
		searchDuration = System.currentTimeMillis();
		control.search(searchSnippet.getText(), null, null,
				XSearch.SearchSorting.highestRated, 0, 10, SearchArea.this);
	}
	
public void update() {
		if (control.isLoggedIn()) {
			btCreateSnippet.setVisible(true);
		} 
		else {
			btCreateSnippet.setVisible(false);
		} 
		
	}
	
	

	/**
	 * Callback if the search failed with an exception
	 * 
	 * @param caught
	 *            Cause why the search failed
	 */
	String searchFailed(Throwable caught) {
		status = "Search failed: " + caught.getMessage();
		return status;
	}

	/**
	 * Callback if the search succeeds
	 * 
	 * @param result
	 *            Of the search
	 */
	String searchDone(XSearch result) {
		searchDuration = System.currentTimeMillis() - searchDuration;
		searchDuration = searchDuration / 10;
		double time = searchDuration / 100.0D;
		status = result.totalresults + " results in " + time + " ms";
		return status;
	}
	
	

}
