package org.smartsnip.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The GUI
 * 
 * Putting together different Widgets
 * 
 * 
 */
public class GUI {

	private static final GUI instance = new GUI();

	/**
	 * Enumeration of all available pages
	 * 
	 */
	public enum Page {
		PAGE_Impressum, PAGE_User, PAGE_SnippetOfDay, PAGE_Search, PAGE_Snippet, PAGE_Blank, PAGE_Notifications, PAGE_Moderator, PAGE_CodeHistory
	}

	/**
	 * This inner class is used for return results in instantiated popup dialogs
	 * 
	 * @param <E>
	 *            Generic type of the return value
	 */
	private static class ReturnValue<E> {
		/** Value that is returned */
		private E value = null;

		/**
		 * Set return value
		 * 
		 * @param value
		 *            value to be set
		 */
		public void setValue(E value) {
			this.value = value;
		}

		/**
		 * @return the previously set return value, or null, if not yet set
		 */
		public E getValue() {
			return value;
		}
	}

	/* Components of the GUI */
	// visible in package, Control can modify
	ResultArea myResultArea = null;
	StatusArea myStatusArea = null;
	CatArea myCatArea = null;
	TagArea myTagArea = null;
	CommentArea myCommentArea = null;
	final Meta myMeta;
	final Footer myFooter;
	SnipArea mySnipArea = null;
	PersonalArea myPersonalArea = null;
	final SearchArea mySearchArea;
	ModeratorArea myModeratorArea = null;
	final SearchToolbar mySearchToolbar;
	NotificationsArea myNotifications = null;
	CodeHistoryPage myCodeHistory = null;

	// Create userPanel
	SimplePanel userPanel = new SimplePanel();
	// Create searchPanel
	SimplePanel searchPanel = new SimplePanel();
	// Create dataPanel
	HorizontalPanel dataPanel = new HorizontalPanel();
	// Create footerPanel
	SimplePanel footerPanel = new SimplePanel();

	/* END of Components of the GUI */

	/** For easy access to the control */
	private final Control control = Control.getInstance();

	/** The current displayed page */
	private Page currentPage = Page.PAGE_SnippetOfDay;

	/**
	 * This callback registers itself in the constructor and handles the update
	 * of the search page. It is called, when the search results come to the
	 * client.
	 * 
	 * This is the callback for {@link Search#search()}, when an arbitary GUI
	 * entity fires a search
	 */
	private final AsyncCallback<XSearch> searchAsynCallback = new AsyncCallback<XSearch>() {

		@Override
		public void onSuccess(XSearch result) {
			String status = result.totalresults + " results in " + convertSearchTime(Control.search.getSearchTime());
			updateSearchPage(result, status);
			onSearchDone(true);
		}

		@Override
		public void onFailure(Throwable caught) {
			String status = "Search failed";
			if (caught != null)
				status += ": " + caught.getMessage();

			updateSearchPage(null, status);
			onSearchDone(false);
		}

		/**
		 * This call converts a given time interval given in milliseconds into a
		 * string, that is readable for humans
		 * 
		 * @param millis
		 *            milliseconds to be converted
		 * @return a human readable format with possible less precision than the
		 *         milliseconds
		 */
		private String convertSearchTime(long millis) {
			if (millis < 0)
				return "- " + convertSearchTime(-millis);

			if (millis < 1000)
				return millis + " ms";
			int tenthSeconds = (int) (millis / 100); // 10th-seconds
														// ("Zehntelsekunden")
			if (tenthSeconds < 100) {
				float result = tenthSeconds / (float) 10.0;
				return result + " s";
			}

			return (millis / 1000) + " s";
		}
	};

	/**
	 * Instantiates a GUI element. Singleton, therefore private constructor
	 */
	private GUI() {
		Resources.INSTANCE.css().ensureInjected();
		Resources.INSTANCE.cssPrettify().ensureInjected();
		// Add search callback
		Control.search.addCallback(searchAsynCallback);

		mySearchToolbar = new SearchToolbar();
		myCatArea = new CatArea();
		myTagArea = new TagArea();
		myMeta = new Meta();
		mySearchArea = new SearchArea();
		myFooter = new Footer();

		footerPanel.setWidth("100%");
		footerPanel.add(myFooter);

		// Fill userPanel
		userPanel.add(myMeta);

		// Fill searchPanel
		searchPanel.add(mySearchArea);

		// Fix Panels to divs
		RootPanel.get("user").add(userPanel);
		RootPanel.get("search").add(searchPanel);
		RootPanel.get("data").add(dataPanel);
		RootPanel.get("footer").add(footerPanel);

		control.showSnippetOfDay();
	}

	/**
	 * shows the search page
	 * 
	 */
	public void showSearchPage() {
		currentPage = Page.PAGE_Search;

		dataPanel.clear();

		myResultArea = new ResultArea();
		myStatusArea = new StatusArea();

		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setStyleName("leftPanel");

		VerticalPanel rightPanel = new VerticalPanel();
		rightPanel.setStyleName("rightPanel");

		leftPanel.add(myStatusArea);
		leftPanel.add(myResultArea);

		rightPanel.add(myCatArea);
		rightPanel.add(myTagArea);
		rightPanel.add(mySearchToolbar);
		dataPanel.add(leftPanel);

		dataPanel.add(rightPanel);

	}

	/**
	 * shows the snippet page
	 * 
	 */
	public void showSnipPage(final XSnippet snip) {
		currentPage = Page.PAGE_Snippet;

		dataPanel.clear();
		mySnipArea = new SnipArea(snip);
		mySnipArea.update(); // Updade increases viewcounter
		dataPanel.add(mySnipArea);
		myCommentArea = new CommentArea(snip);
		dataPanel.add(myCommentArea);
	}

	/**
	 * shows the Personal page
	 * 
	 */
	public void showPersonalPage() {
		currentPage = Page.PAGE_User;

		dataPanel.clear();
		myPersonalArea = new PersonalArea();
		VerticalPanel vertPanel = new VerticalPanel();
		dataPanel.add(myPersonalArea);
		dataPanel.add(vertPanel);
		myPersonalArea.updateSnippets();
	}

	/**
	 * shows the login popup
	 * 
	 */
	public void showLoginPopup() {
		// Does not change current page
		Login.showLoginPopup();
	}

	/**
	 * Show a primitive {@link PopupPanel} with a given message
	 * 
	 * @param message
	 *            Message to be displayed
	 * 
	 */
	public void showMessagePopup(String message) {

		final PopupPanel loginPanel = new PopupPanel(true, true);
		Button close = new Button("<b>Close</b>");

		loginPanel.setTitle(message);
		VerticalPanel vertPanel = new VerticalPanel();
		Label lname = new Label(message);
		vertPanel.add(lname);
		vertPanel.add(close);
		loginPanel.setWidget(vertPanel);
		loginPanel.setGlassEnabled(true);
		loginPanel.setPopupPosition(110, 100);
		loginPanel.setWidth("340px");
		loginPanel.show();
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loginPanel.hide();
			}
		});
	}

	/**
	 * Shows a simple confirmation dialog, with the YES/NO Option.
	 * 
	 * @param message
	 *            Message to be displayed
	 * @param callback
	 *            Callback for results. true if the user clicked YES, false if
	 *            the user clicked NO
	 */
	public void showConfirmPopup(String message, final ConfirmCallback callback) {
		showConfirmPopup(message, "Confirmation", callback);
	}

	/**
	 * Shows a simple confirmation dialog, with the YES/NO Option.
	 * 
	 * @param message
	 *            Message to be displayed
	 * @param title
	 *            Title of the message to be displayed
	 * @param callback
	 *            Callback for results. true if the user clicked YES, false if
	 *            the user clicked NO
	 */
	public void showConfirmPopup(String message, String title, final ConfirmCallback callback) {

		Window.scrollTo(0, 0);

		if (message == null)
			message = "";
		if (title == null)
			title = "";

		final PopupPanel confirmPopup = new PopupPanel(true, true);
		Button btnYes = new Button("<b>Yes</b>");
		Button btnNo = new Button("<b>No</b>");

		confirmPopup.setTitle("Error");
		VerticalPanel vertPanel = new VerticalPanel();
		HorizontalPanel pnlButtons = new HorizontalPanel();
		Label lname = new Label(message);
		vertPanel.add(lname);
		pnlButtons.add(btnYes);
		pnlButtons.add(btnNo);
		vertPanel.add(pnlButtons);
		confirmPopup.setWidget(vertPanel);
		confirmPopup.setGlassEnabled(true);
		confirmPopup.setPopupPosition(110, 100);
		confirmPopup.setWidth("340px");

		btnNo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				confirmPopup.hide();
				callback.onNo();
			}
		});
		btnYes.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				confirmPopup.hide();
				callback.onYes();
			}
		});
		confirmPopup.show();
	}

	/**
	 * shows a error popup
	 * 
	 * @param String
	 *            the error message
	 * @deprecated Deprecated because it uses less fuctionality than
	 *             {@link #showErrorPopup(String, Throwable)} Use this method
	 *             instant
	 * 
	 */
	@Deprecated
	public void showErrorPopup(String message) {
		if (message == null || message.isEmpty())
			return;

		final PopupPanel errorPopup = new PopupPanel(true, true);
		Button close = new Button("<b>Close</b>");

		errorPopup.setTitle("Error");
		VerticalPanel vertPanel = new VerticalPanel();
		Label lname = new Label(message);
		vertPanel.add(lname);
		vertPanel.add(close);
		errorPopup.setWidget(vertPanel);
		errorPopup.setGlassEnabled(true);
		errorPopup.setPopupPosition(110, 100);
		errorPopup.setWidth("340px");
		errorPopup.show();
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				errorPopup.hide();
			}
		});
	}

	/**
	 * Shows a error popup. This error popup is more detailed than
	 * {@link #showErrorPopup(String)} and gives the user some more tools to
	 * investigate the error
	 * 
	 * @param String
	 *            the error message
	 * @param Throwable
	 *            the error
	 * 
	 */
	public void showErrorPopup(String message, final Throwable cause) {

		Window.scrollTo(0, 0);

		if (message == null)
			message = "";
		if (cause != null) {
			String causeMessage = cause.getMessage();
			if (causeMessage != null)
				message = message + "\n" + cause.getMessage();
		}

		final PopupPanel errorPopup = new PopupPanel(true, true);
		Button close = new Button("<b>Close</b>");
		final Anchor anchDetails = new Anchor("Show me more details ... ");

		errorPopup.setTitle("Error");
		final VerticalPanel vertPanel = new VerticalPanel();
		Label lname = new Label(message);
		final Label lblErrorTrace = new Label("");
		vertPanel.add(lname);
		vertPanel.add(close);
		vertPanel.add(lblErrorTrace);
		vertPanel.add(anchDetails);
		lblErrorTrace.setVisible(false);
		errorPopup.setWidget(vertPanel);
		errorPopup.setGlassEnabled(true);
		errorPopup.setPopupPosition(110, 100);
		errorPopup.setWidth("340px");
		errorPopup.show();
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				errorPopup.hide();
			}
		});
		anchDetails.addClickHandler(new ClickHandler() {

			boolean enabled = false;

			@Override
			public void onClick(ClickEvent event) {
				if (enabled)
					return;
				enabled = true;

				// Show more details
				lblErrorTrace.setVisible(true);
				anchDetails.setEnabled(false);
				printErrorTrace();
			}

			private void printErrorTrace() {
				final StringBuffer buffer = new StringBuffer();
				cause.printStackTrace(new PrintStream(new OutputStream() {

					// CAUTION: Here NO OVERRIDE annotiation!
					// Will cause GWT to not work!
					public void write(int character) throws IOException {
						buffer.append((char) character);
					}
				}));
				String trace = buffer.toString();

				lblErrorTrace.setText(trace);

			}
		});
	}

	/**
	 * shows the register popup
	 * 
	 * 
	 */
	public void showRegisterPopup() {
		Window.scrollTo(0, 0);
		PopupPanel registerPanel = new PopupPanel(true);

		registerPanel.setStyleName("Register");
		registerPanel.setTitle("Register");
		Register register = new Register(registerPanel);
		registerPanel.setWidget(register);
		registerPanel.setGlassEnabled(true);
		registerPanel.setPopupPosition(90, 104);
		registerPanel.setWidth("250px");
		registerPanel.show();
	}

	/**
	 * shows the impressum
	 * 
	 * 
	 */
	public void showImpressum() {
		currentPage = Page.PAGE_Impressum;

		Window.scrollTo(0, 0);
		dataPanel.clear();
		HTML impressum = new HTML(
				"<p><br /><strong>Verantwortlich für den Inhalt<br /></strong>Paul Opitz <br />"
						+ "Roveretoplatz 2 <br />6330 Kufstein <br />Österreich</p><p><br /><strong>Haftung</strong><br />"
						+ "Die Inhalte unserer Seiten wurden mit größter Sorgfalt erstellt. Für die Richtigkeit, Vollständigkeit und Aktualität der Inhalte kann ich jedoch keine Gewähr übernehmen.</p><p><br /><strong>Haftung für Links</strong><br />Unser Angebot enthält Links zu externen Webseiten Dritter, auf deren Inhalte ich keinen Einfluss habe. Deshalb kann ich für diese fremden Inhalte auch keine Gewähr übernehmen. "
						+ "Für die Inhalte der verlinkten Seiten ist stets der jeweilige Anbieter "
						+ "oder Betreiber der Seiten verantwortlich. Die verlinkten Seiten wurden zum Zeitpunkt der Verlinkung auf mögliche Rechtsverstöße überprüft. Rechtswidrige Inhalte waren zum Zeitpunkt der Verlinkung nicht erkennbar. Eine permanente inhaltliche Kontrolle der verlinkten Seiten ist jedoch ohne konkrete Anhaltspunkte einer Rechtsverletzung nicht zumutbar. Bei Bekanntwerden von Rechtsverletzungen werde ich derartige Links umgehend entfernen.</p><br /><strong>Rechte</strong><br />"
						+ "Die Inhalte stehen unter einer Creative Commons Namensnennung-Nicht-kommerziell-Weitergabe unter gleichen Bedingungen 3.0 Österreich Lizenz.<p><br /><strong>Datenschutz</strong><br />Die Nutzung unserer Webseite ist in der Regel ohne Angabe personenbezogener Daten möglich. Soweit auf meinen Seiten personenbezogene Daten (beispielsweise Name, Anschrift oder E-Mail-Adressen) erhoben werden, erfolgt dies, soweit möglich, stets auf freiwilliger Basis. Diese Daten werden ohne Ihre ausdrückliche Zustimmung nicht an Dritte weitergegeben.Ich weise darauf hin, dass die Datenübertragung im Internet (z.B. bei der Kommunikation per E-Mail) Sicherheitslücken aufweisen kann. Ein lückenloser Schutz der Daten vor dem Zugriff durch Dritte ist nicht möglich.Der Nutzung von im Rahmen der Impressumspflicht veröffentlichten Kontaktdaten durch Dritte zur Übersendung von nicht ausdrücklich angeforderter Werbung und Informationsmaterialien wird hiermit ausdrücklich widersprochen. Der Betreiber der Seiten behaltet sich ausdrücklich rechtliche Schritte im Falle der unverlangten Zusendung von Werbeinformationen, etwa durch Spam-Mails, vor.</p>");
		impressum.setWidth("800px");
		dataPanel.add(impressum);
	}

	/**
	 * updates the search pagel. Internal call for
	 * {@link GUI#searchAsynCallback}
	 * 
	 * @param XSearch
	 *            the search result
	 * @param String
	 *            the current search status
	 * 
	 * 
	 */
	private void updateSearchPage(XSearch result, String status) {
		showSearchPage();

		myStatusArea.setStatus(status);
		if (result != null) {
			myResultArea.update(result.snippets);
			myCatArea.update(result.categories);
			myTagArea.update(result.tagsAppearingInSearchString);
			mySearchArea.update();
		}

		mySearchArea.updateSuggestions();
	}

	/**
	 * Show new blank page without any contents
	 * 
	 * @return The created {@link BlankPage}
	 */
	public BlankPage showBlankPage() {
		currentPage = Page.PAGE_Blank;

		dataPanel.clear();
		BlankPage page = new BlankPage();
		dataPanel.add(page);
		return page;
	}

	/**
	 * Shows a page created from an error
	 * 
	 * The page itself is a creates {@link BlankPage}, so that also the current
	 * page is set to {@link Page#PAGE_Blank}
	 * 
	 * @param message
	 *            Message to be displayed
	 * @param cause
	 *            Throwable of the error to be displayed
	 */
	public void showErrorPage(final String message, final Throwable cause) {
		final BlankPage page = showBlankPage();

		final Label lblTitle = new Label((message == null || message.isEmpty() ? "An error occured" : message));
		final HTML html = new HTML("<p>Well, this is embrassing</p>\n<hr>");
		final Label lblMessage = new Label((cause == null ? "Unknown error" : cause.getMessage()));
		final Anchor anchTrace = new Anchor("Print traceback");
		anchTrace.addClickHandler(new ClickHandler() {

			/** If the link is enabled */
			boolean enabled = true;

			@Override
			public void onClick(ClickEvent event) {
				if (!enabled)
					return;
				enabled = true;
				anchTrace.setText("Getting traceback ... ");
				anchTrace.setEnabled(false);

				final StringBuffer buffer = new StringBuffer();
				cause.printStackTrace(new PrintStream(new OutputStream() {

					// NO @OVERRIDE annotation here !!!
					// (carefull with save actions)
					public void write(int character) throws IOException {
						// TODO Format (newlines, ecc)

						buffer.append((char) character);
					}
				}));
				String trace = buffer.toString();

				final HTML traceHTML = new HTML("<p>" + trace + "</p>");
				page.add(traceHTML);
			}
		});

		page.add(lblTitle);
		page.add(html);
		page.add(lblMessage);
		page.add(anchTrace);

	}

	/**
	 * shows the contact form
	 * 
	 * 
	 */
	public void showContactForm() {
		Window.scrollTo(0, 0);
		PopupPanel ppnlContact = new PopupPanel(false);
		ppnlContact.setStyleName("contactForm");
		ppnlContact.setTitle("Contact");
		Contact contact = new Contact(ppnlContact);
		ppnlContact.setWidget(contact);
		ppnlContact.setGlassEnabled(true);
		ppnlContact.setPopupPosition(90, 104);
		ppnlContact.setWidth("250px");
		ppnlContact.show();
	}

	/**
	 * shows the edit snippet popup
	 * 
	 * 
	 */
	public void showEditSnippetForm(final XSnippet snippet) {
		if (snippet == null)
			return;

		Window.scrollTo(0, 0);
		PopupPanel ppnlSnippet = new PopupPanel(true, true);
		ppnlSnippet.setStyleName("contactForm");
		ppnlSnippet.setTitle("Edit snippet");
		EditSnippet newSnippet = new EditSnippet(ppnlSnippet, snippet);
		ppnlSnippet.setWidget(newSnippet);
		ppnlSnippet.setGlassEnabled(true);
		ppnlSnippet.setPopupPosition(90, 104);
		ppnlSnippet.setWidth("450px");
		ppnlSnippet.show();
	}

	/**
	 * shows the create snippet popup
	 * 
	 * 
	 */
	public void showCreateSnippetForm() {
		Window.scrollTo(0, 0);
		PopupPanel ppnlSnippet = new PopupPanel(true, true);
		ppnlSnippet.setStyleName("contactForm");
		ppnlSnippet.setTitle("Create snippet");
		CreateSnippet newSnippet = new CreateSnippet(ppnlSnippet, "Create");
		ppnlSnippet.setWidget(newSnippet);
		ppnlSnippet.setGlassEnabled(true);
		ppnlSnippet.setPopupPosition(90, 104);
		ppnlSnippet.setWidth("450px");
		ppnlSnippet.show();
	}

	/**
	 * Shows thte code history page for a given snippet. If the snippet is null,
	 * nothing happens
	 * 
	 * @param owner
	 *            Snippet the code history is shown from
	 */
	public void showCodeHistoryPage(XSnippet owner) {
		if (owner == null)
			return;

		currentPage = Page.PAGE_CodeHistory;
		myCodeHistory = new CodeHistoryPage(owner);

		dataPanel.clear();
		dataPanel.add(myCodeHistory);
	}

	/**
	 * Creates a popup window with a link
	 * 
	 * @param String
	 *            the download message
	 * @param convertToLink
	 *            link to be created
	 */
	public void showDownloadPopup(String message, String convertToLink) {
		Window.scrollTo(0, 0);

		if (message == null)
			message = "";
		if (convertToLink == null || convertToLink.isEmpty())
			return;

		final PopupPanel popup = new PopupPanel(true, true);
		Button close = new Button("<b>Close</b>");
		Anchor link = new Anchor("Download the source now");
		link.setHref(convertToLink);

		popup.setTitle("Link ");
		VerticalPanel vertPanel = new VerticalPanel();
		Label lname = new Label(message);
		vertPanel.add(lname);
		vertPanel.add(link);
		vertPanel.add(close);
		popup.setWidget(vertPanel);
		popup.setGlassEnabled(true);
		popup.setPopupPosition(90, 104);
		popup.setWidth("250px");
		popup.show();
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		});
	}

	/**
	 * Shows the notifications page
	 */
	public void showNotificationPage() {
		dataPanel.clear();
		myNotifications = new NotificationsArea();
		dataPanel.add(myNotifications);
	}

	/**
	 * shows the moderator page
	 * 
	 * 
	 */
	public void showModeratorPage() {
		dataPanel.clear();
		myModeratorArea = new ModeratorArea();
		dataPanel.add(myModeratorArea);
	}

	/**
	 * This method is called by {@link Search#search()} when a new search has
	 * started
	 */
	void onSearchStart() {
		showSearchPage();
		myCatArea.setVisible(false);
		myTagArea.setVisible(false);
		mySearchToolbar.setVisible(false);
		myStatusArea.setStatus("Searching ... ");
		mySearchArea.disableSearchButton();

		mySearchToolbar.setEnabled(false);
		myCatArea.setEnabled(false);
		myTagArea.setEnabled(false);

	}

	/**
	 * This method is called by a search callback initialised in
	 * {@link GUI#initComponents}
	 */
	void onSearchDone(boolean success) {
		myCatArea.setVisible(true);
		myTagArea.setVisible(true);
		mySearchToolbar.setVisible(true);

		mySearchToolbar.setEnabled(success);
		myCatArea.setEnabled(success);
		myTagArea.setEnabled(success);
	}

	/**
	 * Refreshes the whole GUI
	 */
	public void refresh() {
		switch (currentPage) {
		case PAGE_Blank:
		case PAGE_Impressum:
			break;
		case PAGE_Snippet:
		case PAGE_SnippetOfDay:
			// May occur during loading of first page!
			if (mySnipArea != null)
				mySnipArea.update();
			if (myCommentArea != null)
				myCommentArea.update();
			break;
		case PAGE_User:
			// Workaround to fix the personal area problem
			showPersonalPage();
			break;
		case PAGE_Moderator:
			myModeratorArea.update();
			break;
		case PAGE_Notifications:
			myNotifications.update();
			break;
		case PAGE_CodeHistory:
			myCodeHistory.update();
			break;
		case PAGE_Search:
			// Ignore, we don't do a new search
			break;
		}
	}

	/**
	 * Gets the current page
	 * 
	 * @return the current displayed page
	 */
	public Page getCurrentPage() {
		return currentPage;
	}

	/** Makes a refresh on the meta */
	public void refreshMeta() {
		myMeta.update();
	}

	/**
	 * @return the singleton instance
	 */
	public static GUI getInstance() {
		return instance;
	}
}
