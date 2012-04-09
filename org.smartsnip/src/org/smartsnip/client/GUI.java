package org.smartsnip.client;

import java.util.Date;

import org.smartsnip.client.MessageBox.OptionPaneResult;
import org.smartsnip.core.Session;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;

public class GUI implements EntryPoint {

	// hier kommen alle benötigen gtw-widgets und panels rein
	// Label label = new Label("Hello GWT !!!");
	// Button button = new Button("Say something");

	// Create loginPanel
	HorizontalPanel loginPanel = new HorizontalPanel();
	Label user = new Label("guest");
	TextBox txtLoginUsername = new TextBox();
	PasswordTextBox txtPassword = new PasswordTextBox();
	Button btLogin = new Button("Login");
	// Create searchPanel
	HorizontalPanel searchPanel = new HorizontalPanel();
	SuggestBox searchSnippet = new SuggestBox();
	ListBox searchLanguage = new ListBox();
	Button searchButton = new Button("Search");
	// Create leftSide
	TabLayoutPanel leftPanel = new TabLayoutPanel(3, Unit.EM);
	VerticalPanel contentPanel0 = new VerticalPanel();
	VerticalPanel contentPanel1 = new VerticalPanel();
	VerticalPanel contentPanel2 = new VerticalPanel();
	VerticalPanel contentPanel3 = new VerticalPanel();
	VerticalPanel contentPanel4 = new VerticalPanel();
	// Create rightSide
	VerticalPanel rightPanel = new VerticalPanel();
	Button cats = new Button("Categories");
	Button tags = new Button("Tags");

	@Override
	public void onModuleLoad() {
		boolean guest = true; // UGLY, remove this

		// hier kann eine initialisierung vorgenommen werden
		// z.b. stocksFlexTable.setText(0, 1, "Price");

		// hier werden die widgets in einander gebaut
		// z.b. addPanel.add(addStockButton);

		// Fill loginPanel
		loginPanel.add(user);
		if (guest) {
			loginPanel.add(txtLoginUsername);
			loginPanel.add(txtPassword);
			loginPanel.add(btLogin);
		}
		btLogin.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// XXX The access to the controller fails!
				/*
				 * [ERROR] [org_smartsnip] - Errors in
				 * 'file:.../SEPM-SS12-Project/org.smartsnip/src/org/smartsnip/client/Controller.java'
				 * [ERROR] [org_smartsnip] - Line 23: No source code is
				 * available for type org.smartsnip.core.ISessionObserver; did
				 * you forget to inherit a required module? [ERROR]
				 * [org_smartsnip] - Line 26: No source code is available for
				 * type org.smartsnip.core.Session; did you forget to inherit a
				 * required module? [ERROR] [org_smartsnip] - Line 26: No source
				 * code is available for type org.smartsnip.core.Notification;
				 * did you forget to inherit a required module? [ERROR]
				 * [org_smartsnip] - Uncaught exception escaped
				 */
				if (Controller.isLoggedIn()) {
					if (MessageBox.showOptionPane("You are already logged in as " + Controller.getUsername()
							+ "\nYou want to log out and re-login?", "Question", txtLoginUsername.getAbsoluteLeft(),
							txtLoginUsername.getAbsoluteTop() + 20) != OptionPaneResult.yes) return;

					// Log out
					Controller.logout();
				}

				String username = txtLoginUsername.getText();
				String password = txtLoginUsername.getText();
				if (username.length() == 0 || password.length() == 0) {
					MessageBox.showMessageBox("Login credentials missing", "Login failure",
							txtLoginUsername.getAbsoluteLeft(), txtLoginUsername.getAbsoluteTop() + 20);
				} else {
					MessageBox.showMessageBox("Loggin in ... ", "DEBUG message", txtLoginUsername.getAbsoluteLeft(),
							txtLoginUsername.getAbsoluteTop() + 40);

				}
			}
		});

		// Fill searchPanel
		searchPanel.add(searchSnippet);
		searchPanel.add(searchLanguage);
		searchPanel.add(searchButton);
		// Fill leftPanel
		leftPanel.add(contentPanel0, "All");
		leftPanel.add(contentPanel1, "Highest Rated");
		leftPanel.add(contentPanel2, "Mostly Viewed");
		leftPanel.add(contentPanel3, "Recently Viewd");
		leftPanel.add(contentPanel4, "Favourites");
		leftPanel.selectTab(0);
		// Fill rightPanel
		rightPanel.add(cats);
		rightPanel.add(tags);

		// hier wird das hauptpanel oder widget zum rootpanel (div oder body)
		// hinzugefuegt
		// z.b.
		// RootPanel.get().add(label);
		// RootPanel.get().add(button);

		// Fix Panels to Divs
		RootPanel.get("login").add(loginPanel);
		RootPanel.get("search").add(searchPanel);
		RootPanel.get("left").add(leftPanel);
		RootPanel.get("right").add(rightPanel);

		// hier wird der cursor gesetzt
		// newSymbolTextBox.setFocus(true);

	}

}
