package org.smartsnip.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class SortArea extends Composite {

	private final HorizontalPanel pnlBasis;
	private final Label lblStatus;
	private final Label lblSort;
	private final ListBox lbSelectSort;

	public SortArea() {
		pnlBasis = new HorizontalPanel();
		lblStatus = new Label("Dummy-Text");
		lblSort = new Label("Sorting:");

		lbSelectSort = new ListBox();
		lbSelectSort.addItem("Latest Snippets");
		lbSelectSort.addItem("Mostly Viewed");
		lbSelectSort.addItem("Best Rated");
		lbSelectSort.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				switch (lbSelectSort.getSelectedIndex()) {
				case 0:
					break;
				case 1:
					break;
				case 2:
					break;
				}

			}
		});

		pnlBasis.add(lblStatus);
		pnlBasis.add(lblSort);
		pnlBasis.add(lbSelectSort);

		initWidget(pnlBasis);
		// Give the overall composite a style name.
		setStyleName("sortArea");
	}

	public void update(String status) {
		lblStatus.setText(status);
	}

}
