package org.smartsnip.client;

import org.smartsnip.shared.XSearch.SearchSorting;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * 
 * 
 * @author Paul
 * 
 * 
 *         A composed Widget to display the status area
 * 
 */
public class StatusArea extends Composite {

	private final DockPanel pnlBasis;
	private final Label lblStatus;
	private final ListBox lbSelectSort;

	/**
	 * Initializes the status area
	 * 
	 */
	public StatusArea() {
		pnlBasis = new DockPanel();
		// pnlBasis.setWidth("400px");
		lblStatus = new Label("");

		lbSelectSort = new ListBox();
		lbSelectSort.addItem("Unsorted");
		lbSelectSort.addItem("Latest Snippets");
		lbSelectSort.addItem("Mostly Viewed");
		lbSelectSort.addItem("Best Rated");
		lbSelectSort.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				switch (lbSelectSort.getSelectedIndex()) {
				case 1: // Latest Snippets
					Control.search.setSorting(SearchSorting.time);
					break;
				case 2: // Mostly Viewed
					Control.search.setSorting(SearchSorting.mostViewed);
					break;
				case 3: // Best Rated
					Control.search.setSorting(SearchSorting.highestRated);
					break;
				default: // Unsorted
					Control.search.setSorting(SearchSorting.unsorted);
					break;
				}

				Control.search.search();

			}
		});

		pnlBasis.add(lblStatus, DockPanel.WEST);
		pnlBasis.add(lbSelectSort, DockPanel.EAST);

		initWidget(pnlBasis);
		// Give the overall composite a style name.
		setStyleName("statusArea");
	}

	/**
	 * Sets the current status
	 * 
	 * @param status
	 *            - a string
	 */
	public void setStatus(String status) {
		lblStatus.setText(status);
	}

}
