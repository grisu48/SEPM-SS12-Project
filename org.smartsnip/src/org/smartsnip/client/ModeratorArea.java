package org.smartsnip.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;

/**
 * This panel is for the moderator only and provides access to the moderator
 * functions
 * 
 * 
 */
public class ModeratorArea extends Composite {

	public ModeratorArea() {

		TabPanel tabPanel = new TabPanel();


		VerticalPanel pnlVertUsers = new VerticalPanel();
		tabPanel.add(pnlVertUsers, "Users", false);
		pnlVertUsers.setSize("5cm", "3cm");

		Label lblUsers = new Label("Users: 0");
		pnlVertUsers.add(lblUsers);

		VerticalPanel pnlVertCategories = new VerticalPanel();
		tabPanel.add(pnlVertCategories, "Categories", false);
		pnlVertCategories.setSize("5cm", "3cm");

		Label lblCategories = new Label("Categories: 0");
		pnlVertCategories.add(lblCategories);

		Tree treeCategories = new Tree();
		pnlVertCategories.add(treeCategories);
		
		initWidget(tabPanel);
		// Give the overall composite a style name.
		setStyleName("moderatorArea");
	}

}
