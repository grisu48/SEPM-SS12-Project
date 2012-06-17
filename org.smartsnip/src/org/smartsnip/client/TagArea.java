package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class TagArea extends Composite {

	private final FlowPanel myPanel;
	private final ArrayList<Button> tagButtons;
	private final Label title;

	public TagArea() {
		myPanel = new FlowPanel();
		tagButtons = new ArrayList<Button>();
		title = new Label("Tags");
		myPanel.add(title);
		initWidget(myPanel);
		// Give the overall composite a style name.
		setStyleName("tagArea");
	}

	/**
	 * Updates the GUI control according to the tag list. If the tag list is
	 * null, the panel is cleared.
	 * 
	 * @param tagsAppearingInSearchString
	 *            tag list to be applied
	 */
	public void update(final List<String> tagsAppearingInSearchString) {
		clear();

		if (tagsAppearingInSearchString == null) {
			return;
		}

		for (final String tag : tagsAppearingInSearchString) {

			final Button tagButton = new Button(tag);
			final boolean tagEnabled = Control.search.containsTag(tag);
			tagButton.setEnabled(true);
			if (tagEnabled)
				tagButton.setStyleName("btDis");
			else
				tagButton.setStyleName("btEn");

			tagButtons.add(tagButton);
			tagButton.setTitle(tag); // DO NOT MODIFY - Used for each button to
										// associate it with a tag!
			tagButton.addClickHandler(new ClickHandler() {
				private boolean enabled = tagEnabled;

				@Override
				public void onClick(ClickEvent event) {

					// Switch enabled state (tag can be ENABLED or DISABLED)
					enabled = !enabled;

					if (enabled) {
						Control.search.addTag(tag);
						tagButton.setStyleName("btDis");
					}

					else {
						Control.search.removeTag(tag);
						tagButton.setStyleName("btEn");
					}

				}
			});
			myPanel.add(tagButton);
		}

	}

	/**
	 * Clears the field and removes all tag buttons.
	 */
	private void clear() {
		myPanel.clear();
		tagButtons.clear();
		myPanel.add(title);
	}

	/**
	 * Disables all tags
	 */
	public void removeAll() {
		Control.search.clearTags();
		for (final Button tagButton : tagButtons) {
			tagButton.setStyleName("btDis");
		}

	}

	/**
	 * Enables all tags
	 */
	public void addAll() {
		for (final Button tagButton : tagButtons) {
			Control.search.addTag(tagButton.getText());
			tagButton.setStyleName("btEn");
		}

	}

	/**
	 * Enables the buttons
	 */
	public void setEnabled(boolean b) {
		for (final Button tagButton : tagButtons) {
			tagButton.setEnabled(b);
		}

	}

}
