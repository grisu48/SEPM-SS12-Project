package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.shared.ICategory;
import org.smartsnip.shared.IModerator;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;
import org.smartsnip.shared.XCategory;
import org.smartsnip.shared.XSession;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter;

/**
 * This panel is for the moderator only and provides access to the moderator
 * functions
 * 
 * @author Felix Niederwanger
 * 
 */
public class ModeratorArea extends Composite {

	/** Selected page */
	private enum Page {
		sessions(1), user(2), categories(3);

		/** Tabindex */
		public final int tabindex;

		private Page(int tabindex) {
			this.tabindex = tabindex;
		}

		/** @return the tab index of the selected page */
		public int getTabIndex() {
			return tabindex;
		}
	}

	/** Current selected page */
	private Page currentPage = Page.sessions;

	private final TabPanel tabPanel;
	private final VerticalPanel pnlVertSessions;
	private final Label lblSessions;

	private final VerticalPanel pnlVertUsers;
	private final Label lblUsers;
	private final VerticalPanel verticalPanel;

	private final Panel categoryPanel = new VerticalPanel();
	private final CategoryTree categoryTree;

	public ModeratorArea() {

		tabPanel = new TabPanel();
		tabPanel.setAnimationEnabled(true);

		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				switch (event.getSelectedItem()) {
				case 0: // Sessions
					selectPage(Page.sessions);
					break;
				case 1: // Users
					selectPage(Page.user);
					break;
				case 2: // Categories
					selectPage(Page.categories);
					break;
				}
			}
		});

		pnlVertSessions = new VerticalPanel();
		lblSessions = new Label("Sessions: 0");
		pnlVertSessions.add(lblSessions);
		tabPanel.add(pnlVertSessions, "Sessions", false);

		verticalPanel = new VerticalPanel();
		pnlVertSessions.add(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		pnlVertUsers = new VerticalPanel();
		tabPanel.add(pnlVertUsers, "Users", false);
		pnlVertUsers.setSize("5cm", "3cm");

		lblUsers = new Label("Users: 0");
		pnlVertUsers.add(lblUsers);

		categoryTree = new CategoryTree();
		categoryPanel.add(categoryTree);

		tabPanel.add(categoryPanel, "Categories", false);

		initWidget(tabPanel);
		// Give the overall composite a style name.
		setStyleName("moderatorArea");

		selectPage(Page.sessions);
	}

	/**
	 * Occurs when a page selection change happend
	 * 
	 * @param newPage
	 *            new page to be displayed
	 */
	private void selectPage(Page newPage) {
		currentPage = newPage;

		switch (currentPage) {
		case sessions:
			updateSession();
			break;
		case user:
			updateUsers();
			break;
		case categories:
			updateCategories();
			break;
		}
	}

	/**
	 * 
	 * @return the currently selected page
	 */
	public Page getPage() {
		return currentPage;
	}

	/**
	 * Updates the components and the current selected page
	 */
	public void update() {
		// XXX Ugly workaround
		selectPage(currentPage);
	}

	/**
	 * Updates the session page
	 */
	private void updateSession() {
		lblSessions.setText("Updating ... ");
		pnlVertSessions.clear();
		pnlVertSessions.add(lblSessions);

		IModerator.Util.getInstance().getSessions(new AsyncCallback<List<XSession>>() {

			/** Guest session counter for createSessionPanel(XSession) */
			int guest = 0;

			@Override
			public void onSuccess(final List<XSession> result) {
				if (result == null) {
					onFailure(new IllegalArgumentException("Returned null"));
					return;
				}

				guest = 0;
				int size = result.size();
				lblSessions.setText((size == 1 ? "1 open session" : size + " open session"));
				for (XSession session : result)
					pnlVertSessions.add(createSessionPanel(session));
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught == null) lblSessions.setText("Unknown error while updating");
				else if (caught instanceof NoAccessException) lblSessions.setText("Update failed: Access denied");
				else
					lblSessions.setText("Update failed: " + caught.getMessage());

			}

			/**
			 * Creates a new session panel
			 * 
			 * @param session
			 *            for the panel
			 * @return the created session panel
			 */
			private Panel createSessionPanel(final XSession session) {
				final HorizontalPanel result = new HorizontalPanel();
				final VerticalPanel vertPanel = new VerticalPanel();
				final HorizontalPanel infoPanel = new HorizontalPanel();
				final String sessionString = session.user == null ? "Guest session #" + (++guest) : session.user;
				final Label lblSession = new Label(sessionString);
				final Label lblStatus = new Label("Status: Active");

				final Button btnClose = new Button("Close");
				btnClose.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						btnClose.setEnabled(false);
						lblStatus.setText("Closing ... ");
						IModerator.Util.getInstance().closeSession(session.key, new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								lblStatus.setText("Session closed");
							}

							@Override
							public void onFailure(Throwable caught) {
								if (caught == null) lblStatus.setText("Closing failed");
								else if (caught instanceof NoAccessException) lblStatus
										.setText("Closing failed: Access denial");
								else if (caught instanceof NotFoundException) lblStatus
										.setText("Closing failed: Session not found");
								else
									lblStatus.setText("Closing failed: " + caught.getMessage());

								btnClose.setText("Retry closing");
								btnClose.setEnabled(true);
							}
						});
					}
				});

				infoPanel.add(lblStatus);

				vertPanel.add(lblSession);
				vertPanel.add(infoPanel);

				result.add(vertPanel);
				result.add(btnClose);

				return result;
			}
		});
	}

	/**
	 * Updates the categories page
	 */
	private void updateCategories() {
		categoryTree.update();
	}

	/**
	 * Updates the users page
	 */
	private void updateUsers() {
		// TODO Auto-generated method stub

	}
}
