package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.shared.XSearch;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;

public class CatArea extends Composite {

	private final FlowPanel myPanel;
	private final Label title;

	private final List<Button> catButtons = new ArrayList<Button>();

	public CatArea() {
		myPanel = new FlowPanel();
		title = new Label("Categories - (*) indicates enabled categories");
		myPanel.add(title);
		initWidget(myPanel);
		// Give the overall composite a style name.
		setStyleName("catArea");
	}

	public void update(List<String> categories) {
		clear();

		if (categories == null) return;

		for (final String category : categories) {
			final boolean isEnabled = Control.search.containsCategory(category);
			final Button catButton = new Button(getCatDescription(category, isEnabled));
			catButtons.add(catButton);
			catButton.addClickHandler(new ClickHandler() {
				private boolean enabled = isEnabled;

				@Override
				public void onClick(ClickEvent event) {
					enabled = !enabled;

					if (enabled) Control.search.addCategory(category);
					else
						Control.search.removeCategory(category);
					catButton.setText(getCatDescription(category, enabled));

					// Do a search, if auto apply is selected in the search
					// toolbar
					if (Control.myGUI.mySearchToolbar.autoApplySelected()) Control.search.search();
				}
			});
			myPanel.add(catButton);
		}

	}

	/**
	 * The result is used for the category buttons. The return value is
	 * dependent on the enabled status of the category
	 * 
	 * @param category
	 *            Category to be checked
	 * @param enabled
	 *            true if the tag is enabled, otherwise false
	 * @return the button description
	 */
	private String getCatDescription(String category, boolean enabled) {
		if (category == null || category.isEmpty()) return "";

		if (enabled) return "(*) " + category + " (*)";
		else
			return category;
	}

	/**
	 * Clears the component
	 */
	private void clear() {
		myPanel.clear();
		myPanel.add(title);
		catButtons.clear();
	}

	/**
	 * Clears all selected categories
	 */
	public void clearCategories() {
		// Obsolete, because currently only one category is supported
	}

	/**
	 * Enables or disables the category area
	 * 
	 * @param enabled
	 *            true if enabled, false if disabled
	 */
	public void setEnabled(boolean enabled) {
		for (final Button catButton : catButtons) {
			catButton.setEnabled(enabled);
		}
	}

}
