package org.smartsnip.client;

import org.smartsnip.shared.IUser;
import org.smartsnip.shared.XUser;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * 
 * @author Paul
 * 
 * 
 *         A composed Widget to display the personal data
 * 
 */
public class PersonalField extends Composite {

	/* Components */
	private final VerticalPanel rootPanel;
	private final Grid grid;

	private final Label lblTitle;

	private final Label[] lblDescs;
	private final Label lblName;
	private final Label lblRealname;
	private final Label lblEmail;
	private final Label lblUserState;

	private final HorizontalPanel pnlToolbar;
	private final Button btnSetPassword;
	private final Button btnEditData;

	/* End of Components */

	/**
	 * 
	 * Initalizes the field with data
	 * 
	 */
	public PersonalField() {

		rootPanel = new VerticalPanel();
		lblTitle = new Label("Personal user data");

		grid = new Grid(4, 2);
		lblDescs = new Label[] { new Label("Username"), new Label("Real name"), new Label("Email address"), new Label("Status") };
		lblName = new Label("");
		lblRealname = new Label("");
		lblEmail = new Label("");
		lblUserState = new Label("");
		btnSetPassword = new Button("Set password");
		btnEditData = new Button("Edit my data");

		for (int i = 0; i < lblDescs.length; i++)
			grid.setWidget(i, 0, lblDescs[i]);

		// Disable buttons till verified by update()
		btnSetPassword.setEnabled(false);
		btnEditData.setEnabled(false);

		btnSetPassword.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				User.showChangePasswordPopup();
			}
		});
		btnEditData.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				User.showChangeUserData();
			}
		});

		grid.setWidget(0, 1, lblName);
		grid.setWidget(1, 1, lblRealname);
		grid.setWidget(2, 1, lblEmail);
		grid.setWidget(3, 1, lblUserState);

		pnlToolbar = new HorizontalPanel();
		pnlToolbar.add(btnEditData);
		pnlToolbar.add(btnSetPassword);

		rootPanel.add(lblTitle);
		rootPanel.add(grid);
		rootPanel.add(pnlToolbar);
		initWidget(rootPanel);
		applyStyles();

		update();
	}

	/** Applies all the styles */
	private void applyStyles() {

		lblTitle.setStyleName("personalTitle");
		// Give the overall composite a style name.
		setStyleName("personalField");
	}

	/** Updates the component */
	public void update() {
		lblName.setText("Loading ...");
		lblRealname.setText("Loading ...");
		lblEmail.setText("Loading ...");
		lblUserState.setText("Loading ...");

		IUser.Util.getInstance().getMe(new AsyncCallback<XUser>() {

			@Override
			public void onSuccess(XUser result) {
				if (result == null)
					setGuestData();
				else
					setUserData(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				lblName.setText("An error occured");
				lblRealname.setText("");
				lblEmail.setText("");
				lblUserState.setText("");
			}
		});
	}

	/**
	 * Sets the components to data of a guest
	 */
	private void setGuestData() {
		lblName.setText("Guest");
		lblRealname.setText("Guest session");
		lblEmail.setText("");
		lblUserState.setText("Guest");

		btnEditData.setEnabled(false);
		btnSetPassword.setEnabled(false);

	}

	/**
	 * Sets the components to the data of a user
	 * 
	 * @param user
	 *            {@link XUser} userdata the components are set to
	 */
	private void setUserData(XUser user) {
		if (user == null)
			return;

		lblName.setText(user.username);
		lblRealname.setText(user.realname);
		lblEmail.setText(user.email);
		switch (user.state) {
		case administrator:
			lblUserState.setText("Administrator");
			break;
		case deleted:
			// This should never occur
			lblUserState.setText("Deleted user");
			break;
		case moderator:
			lblUserState.setText("Moderator");
			break;
		case unvalidated:
			lblUserState.setText("User (unvalidated)");
			break;
		case validated:
			lblUserState.setText("User");
			break;
		}

		btnEditData.setEnabled(true);
		btnSetPassword.setEnabled(true);
	}

}
