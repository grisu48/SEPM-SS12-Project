package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CatArea extends Composite {

	private final VerticalPanel myPanel;
	private final Label title;
	private final List<Button> catButtons = new ArrayList<Button>();

	public CatArea() {
		myPanel = new VerticalPanel();
		title = new Label("Categories");
		myPanel.add(title);

		initWidget(myPanel);
		// Give the overall composite a style name.
		setStyleName("catArea");
	}

	public void update(List<String> categories) {
		clear();

		if (categories == null)
			return;

		for (final String category : categories) {

			final Button catButton = new Button(category);
			final boolean categoryEnabled = Control.search
					.containsCategory(category);

			catButton.setEnabled(true);

			if (categoryEnabled)
				catButton.setStyleName("btDisCat");
			else
				catButton.setStyleName("btEnCat");

			catButtons.add(catButton);
			catButton.addClickHandler(new ClickHandler() {
				private boolean enabled = categoryEnabled;

				@Override
				public void onClick(ClickEvent event) {
					enabled = !enabled;

					if (enabled) {
						Control.search.addCategory(category);
						catButton.setStyleName("btDisCat");
					} else {
						Control.search.removeCategory(category);
						catButton.setStyleName("btEnCat");
					}

				}
			});
			myPanel.add(catButton);
		}

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
	 * Disables all tags
	 */
	public void removeAll() {
		Control.search.clearCategories();
		for (final Button catButton : catButtons) {
			catButton.setStyleName("btDisCat");
		}

	}

	/**
	 * Enable all tags
	 */
	public void addAll() {
		for (final Button catButton : catButtons) {
			Control.search.addTag(catButton.getText());
			catButton.setStyleName("btEnCat");
		}

	}

	/**
	 * Enables the buttons
	 */
	public void setEnabled(boolean b) {
		for (final Button catButton : catButtons) {
			catButton.setEnabled(b);
		}

	}

}
