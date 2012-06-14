package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;

public class SearchToolbar extends Composite {

	/** Main panel */
	private final HorizontalPanel pnlHorizontal = new HorizontalPanel();

	/* Tools */
	private final Button btnClear = new Button("Clear");
	private final Button btnApply = new Button("Apply categories and tags");
	private final ToggleButton tglAutoApply = new ToggleButton("Auto-Apply");

	public SearchToolbar() {
		btnClear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onClear_Click();
			}
		});
		btnApply.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onApply_Click();
			}
		});
		tglAutoApply.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onAutoApply_Check(tglAutoApply.isDown());
			}
		});

		pnlHorizontal.add(btnClear);
		pnlHorizontal.add(btnApply);
		pnlHorizontal.add(tglAutoApply);

		initWidget(pnlHorizontal);
	}

	/**
	 * Occurs when the user clicks on the clear button
	 */
	private void onClear_Click() {
		Control.myGUI.myCatArea.clearCategories();
		Control.myGUI.myTagArea.clearTags();
	}

	/**
	 * Occurs when the user clicks on the apply button
	 */
	private void onApply_Click() {
		// Apply in this case equals a click on search.
		Control.search.search();
	}

	/**
	 * Occurs when the user enabled/disables the auto-apply button
	 */
	private void onAutoApply_Check(boolean isDown) {
		// Apply if down, otherwise do nothing
		if (isDown) onApply_Click();
	}

	/**
	 * If auto apply is selected or not
	 * 
	 * @return true if auto apply is selected, otherwise false
	 */
	public boolean autoApplySelected() {
		return tglAutoApply.isDown();
	}

	/**
	 * Enables or disables the toolbar
	 * 
	 * @param enabled
	 *            true if enabled, false if disabled
	 */
	public void setEnabled(boolean enabled) {
		btnClear.setEnabled(enabled);
		btnApply.setEnabled(enabled);
		tglAutoApply.setEnabled(enabled);
	}
}
