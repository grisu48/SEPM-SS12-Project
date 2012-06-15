package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;

import org.smartsnip.shared.XSearch;

public class TagArea extends Composite {

	private final FlowPanel myPanel;
	private final Label title;
	private final List<Button> tagButtons = new ArrayList<Button>();

	public TagArea() {
		myPanel = new FlowPanel();
		title = new Label("Tags - (*) indicates enabled tags");
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

		if (tagsAppearingInSearchString == null) return;

		// TODO Better representation - Maybe with ToggleButton?!?
		for (final String tag : tagsAppearingInSearchString) {
			final Button tagButton = new Button(getTagDescription(tag, Control.search.containsTag(tag)));
			tagButtons.add(tagButton);
			tagButton.setTitle(tag); // DO NOT MODIFY - Used for each button to
										// associate it with a tag!
			tagButton.addClickHandler(new ClickHandler() {
				private boolean enabled = false;

				@Override
				public void onClick(ClickEvent event) {

					tagButton.setText(getTagDescription(tag, enabled));
					if (enabled) Control.search.removeTag(tag);
					else
						Control.search.addTag(tag);

					// Switch enabled state (tag can be ENABLED or DISABLED)
					enabled = !enabled;

					// Do a search, if auto apply is selected in the search
					// toolbar
					if (Control.myGUI.mySearchToolbar.autoApplySelected()) Control.search.search();

				}
			});
			myPanel.add(tagButton);
		}

	}

	/**
	 * The result is used for the tag buttons. The return value is dependent on
	 * the enabled status of the tag
	 * 
	 * @param tag
	 *            Tag to be checked
	 * @param enabled
	 *            true if the tag is enabled, otherwise false
	 * @return the button description
	 */
	private String getTagDescription(String tag, boolean enabled) {
		if (tag == null || tag.isEmpty()) return "";

		if (enabled) return "(*) " + tag + " (*)";
		else
			return tag;
	}

	/**
	 * Clears the field and removes all tag buttons. This method also removes
	 * all tags from the search
	 */
	private void clear() {
		myPanel.clear();
		tagButtons.clear();
		myPanel.add(title);
		Control.search.clearTags();
	}

	/**
	 * Disables all tags
	 */
	public void clearTags() {
		Control.search.clearTags();
		for (final Button tagButton : tagButtons) {
			tagButton.setText(tagButton.getTitle());
		}
	}

	/**
	 * Enables or disables the category area
	 * 
	 * @param enabled
	 *            true if enabled, false if disabled
	 */
	public void setEnabled(boolean enabled) {
		for (final Button tagButton : tagButtons) {
			tagButton.setEnabled(enabled);
		}
	}

}
