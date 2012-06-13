package org.smartsnip.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class StatusArea extends Composite {

	private final DockPanel pnlBasis;
	private final Label lblStatus;
	private final ListBox lbSelectSort;

	public StatusArea() {
		pnlBasis = new DockPanel();
		//pnlBasis.setWidth("400px");
		lblStatus = new Label("Dummy-Text");

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

		pnlBasis.add(lblStatus, DockPanel.WEST);
		pnlBasis.add(lbSelectSort, DockPanel.EAST);

		initWidget(pnlBasis);
		// Give the overall composite a style name.
		setStyleName("statusArea");
	}

	public void update(String status) {
		lblStatus.setText(status);
	}

}
