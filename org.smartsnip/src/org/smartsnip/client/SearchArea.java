package org.smartsnip.client;

import java.util.List;

import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.XSearch;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class SearchArea extends Composite {

	private final HorizontalPanel horPanel;
	private final SuggestBox searchSnippet;
	final MultiWordSuggestOracle oracle;
	private final Button btSnippetOfDay;
	private final Button searchButton;
	private final Button btCreateSnippet;
	private final Control control;
	private String status;

	/** Search duration timer */
	private long searchDuration = 0L;

	public SearchArea() {

		control = Control.getInstance();

		horPanel = new HorizontalPanel();

		oracle = new MultiWordSuggestOracle();

		searchSnippet = new SuggestBox(oracle);
		searchSnippet.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
					fireSearch();
				}
			}
		});

		btSnippetOfDay = new Button("Snippet of the Day");
		btSnippetOfDay.addStyleName("snippetOfDayButton");
		btSnippetOfDay.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.showSnippetOfDay();
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
		horPanel.add(btSnippetOfDay);
		horPanel.add(btCreateSnippet);

		initWidget(horPanel);
		// Give the overall composite a style name.
		setStyleName("searchArea");

		update();
		updateSuggestions();
	}

	public void fireSearch() {
		searchButton.setEnabled(false);
		Control control = Control.getInstance();
		searchDuration = System.currentTimeMillis();
		control.search(searchSnippet.getText(), null, null, XSearch.SearchSorting.highestRated, 0, 10, SearchArea.this);
	}

	public void update() {
		if (control.isLoggedIn()) {
			btCreateSnippet.setVisible(true);
		} else {
			btCreateSnippet.setVisible(false);
		}
		searchButton.setEnabled(true);
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
		int time = (int) Math.floor(searchDuration);

		status = result.totalresults + " results in " + time + " ms";
		// status = "test";
		System.out.println(status);
		return status;
	}

	/**
	 * Refreshes the suggestions in the search field
	 */
	public void updateSuggestions() {
		ISnippet.Util.getInstance().getSearchSuggestions(new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				final int maxSuggestions = 10;
				int count = 0;

				oracle.clear();
				for (String suggestion : result) {
					oracle.add(suggestion);
					if (count++ > maxSuggestions) break;
				}

			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});

	}

	public String getSearchText() {
		return searchSnippet.getText();
	}
}
