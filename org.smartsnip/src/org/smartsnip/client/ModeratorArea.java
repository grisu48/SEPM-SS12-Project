package org.smartsnip.client;

import java.util.List;

import org.smartsnip.shared.IModerator;
import org.smartsnip.shared.XSession;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;

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
		sessions, user, categories
	}

	/** Current selected page */
	private Page currentPage = Page.sessions;

	private final TabPanel tabPanel;
	private final VerticalPanel pnlVertSessions;
	private final Label lblSessions;

	private final VerticalPanel pnlVertUsers;
	private final Label lblUsers;

	private final VerticalPanel pnlVertCategories;
	private final Label lblCategories;
	private final Tree treeCategories;

	public ModeratorArea() {

		tabPanel = new TabPanel();

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

		pnlVertUsers = new VerticalPanel();
		tabPanel.add(pnlVertUsers, "Users", false);
		pnlVertUsers.setSize("5cm", "3cm");

		lblUsers = new Label("Users: 0");
		pnlVertUsers.add(lblUsers);

		pnlVertCategories = new VerticalPanel();
		tabPanel.add(pnlVertCategories, "Categories", false);
		pnlVertCategories.setSize("5cm", "3cm");

		lblCategories = new Label("Categories: 0");
		pnlVertCategories.add(lblCategories);

		treeCategories = new Tree();
		pnlVertCategories.add(treeCategories);

		initWidget(tabPanel);
		// Give the overall composite a style name.
		setStyleName("moderatorArea");

		update();
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

		IModerator.Util.getInstance().getSessions(
				new AsyncCallback<List<XSession>>() {

					/** Guest session counter for createSessionPanel(XSession) */
					int guest = 0;

					@Override
					public void onSuccess(final List<XSession> result) {
						if (result == null) {
							onFailure(new IllegalArgumentException(
									"Returned null"));
							return;
						}

						guest = 0;
						lblSessions.setText(result.size() + " open sessions");
						for (XSession session : result)
							pnlVertSessions.add(createSessionPanel(session));
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

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
						final String sessionString = session.user == null ? "Guest session #"
								+ (++guest)
								: session.user;
						final Label lblSession = new Label(sessionString);

						final Button btnClose = new Button("Close");
						btnClose.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								btnClose.setEnabled(false);
								lblSession.setText(sessionString
										+ "(Closing ... )");
								IModerator.Util.getInstance().closeSession(
										session.key, new AsyncCallback<Void>() {

											@Override
											public void onSuccess(Void result) {
												lblSession
														.setText(sessionString
																+ " (closed)");
											}

											@Override
											public void onFailure(
													Throwable caught) {
												lblSession
														.setText(sessionString
																+ "(Closing failed)");
												lblSession.setTitle(caught
														.getMessage());
											}
										});
							}
						});

						result.add(lblSession);
						result.add(btnClose);

						return result;
					}
				});
	}

	/**
	 * Updates the categories page
	 */
	private void updateCategories() {
		// TODO Auto-generated method stub

	}

	/**
	 * Updates the users page
	 */
	private void updateUsers() {
		// TODO Auto-generated method stub

	}
}
