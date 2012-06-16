package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;


public class SearchToolbar extends Composite {

	/** Main panel */
	private final HorizontalPanel pnlHorizontal = new HorizontalPanel();

	/* Tools */
	private final Button btnRestrict = new Button("Restrict all");
	private final Button btnUnrestrict = new Button("Unrestrict all");
	private final Button btnApply = new Button("Apply restricted search");


	public SearchToolbar() {
		
		btnRestrict.setStyleName("btnRestrict");
		btnUnrestrict.setStyleName("btnUnrestrict");
		btnApply.setStyleName("btnApply");
		
		btnRestrict.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onRestrict_Click();
			}
		});
		
		btnUnrestrict.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onUnrestrict_Click();
			}
		});
		
		btnApply.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onApply_Click();
			}
		});
		

		pnlHorizontal.add(btnRestrict);
		pnlHorizontal.add(btnUnrestrict);
		pnlHorizontal.add(btnApply);
		


		initWidget(pnlHorizontal);
	}

	/**
	 * Occurs when the user clicks on the restrict button
	 */
	private void onRestrict_Click() {
		Control.myGUI.myCatArea.removeAll();
		Control.myGUI.myTagArea.removeAll();
	}
	
	/**
	 * Occurs when the user clicks on the unrestrict button
	 */
	private void onUnrestrict_Click() {
		Control.myGUI.myCatArea.addAll();
		Control.myGUI.myTagArea.addAll();
	}

	/**
	 * Occurs when the user clicks on the apply button
	 */
	private void onApply_Click() {
		// Apply in this case equals a click on search.
		Control.search.search();
	}

	/**
	 * Enables/Disables the buttons (while searching)
	 */
	public void setEnabled(boolean b) {
		btnRestrict.setEnabled(b);
		btnUnrestrict.setEnabled(b);
		btnApply.setEnabled(b);
		
	}


}
