package org.smartsnip.client;

import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GUI {

	// visible in package, Control can modify
	ResultArea myResultArea = null;
	SortArea mySortArea = null;
	CatArea myCatArea = null;
	TagArea myTagArea = null;
	CommentArea myCommentArea = null;
	Meta myMeta = null;
	Footer myFooter = null;
	SnipArea mySnipArea = null;
	PersonalArea myPersonalArea = null;
	SearchArea mySearchArea = null;

	// Create userPanel
	SimplePanel userPanel = new SimplePanel();
	// Create searchPanel
	SimplePanel searchPanel = new SimplePanel();
	// Create dataPanel
	HorizontalPanel dataPanel = new HorizontalPanel();
	// Create footerPanel
	SimplePanel footerPanel = new SimplePanel();

	public void getReady() {
		// Adds a personalized CSS-File
		Resources.INSTANCE.css().ensureInjected();

		// Get the updated userdata
		Control control = Control.getInstance();
		control.refresh();

		// Create the Page
		createBasicPage();
		showSearchPage();
		// showImpressum();
		// showSnipPage(SNIPPET);

		// showPersonalPage();
		// showLoginPopup();
		// showRegisterPopup();
		// showTestPopup();

	}

	public void createBasicPage() {

		// Fill userPanel
		myMeta = new Meta();
		userPanel.add(myMeta);

		// Fill searchPanel
		mySearchArea = new SearchArea();
		searchPanel.add(mySearchArea);

		// Fill footerPanel
		myFooter = new Footer();
		footerPanel.setWidth("100%");
		footerPanel.add(myFooter);

		// Fix Panels to divs
		RootPanel.get("user").add(userPanel);
		RootPanel.get("search").add(searchPanel);
		RootPanel.get("data").add(dataPanel);
		RootPanel.get("footer").add(footerPanel);

		// Sets Cursor
		// z.B. ewSymbolTextBox.setFocus(true);

	}

	public void showSearchPage() {

		dataPanel.clear();

		myResultArea = new ResultArea();
		mySortArea = new SortArea();
		myCatArea = new CatArea();
		myTagArea = new TagArea();

		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setStyleName("leftPanel");

		VerticalPanel rightPanel = new VerticalPanel();
		rightPanel.setStyleName("rightPanel");

		leftPanel.add(mySortArea);
		leftPanel.add(myResultArea);
		rightPanel.add(myCatArea);
		rightPanel.add(myTagArea);

		dataPanel.add(leftPanel);
		dataPanel.add(rightPanel);

	}

	public void showSnipPage(XSnippet snip) {
		dataPanel.clear();
		mySnipArea = new SnipArea(snip);
		myCommentArea = new CommentArea(snip);
		dataPanel.add(mySnipArea);
		dataPanel.add(myCommentArea);
	}

	public void showPersonalPage() {
		dataPanel.clear();
		myPersonalArea = new PersonalArea();
		VerticalPanel vertPanel = new VerticalPanel();
		dataPanel.add(myPersonalArea);
		dataPanel.add(vertPanel);

	}

	public void showLoginPopup() {

		PopupPanel loginPanel = new PopupPanel(false);
		loginPanel.setStyleName("Login");
		loginPanel.setTitle("Login");
		Login login = new Login(loginPanel);
		loginPanel.setWidget(login);
		loginPanel.setGlassEnabled(true);
		loginPanel.setPopupPosition(90, 104);
		loginPanel.setWidth("250px");
		loginPanel.show();
	}

	public void showTestPopup(String test) {

		final PopupPanel loginPanel = new PopupPanel(true, true);
		Button close = new Button("<b>Close</b>");

		loginPanel.setTitle(test);
		VerticalPanel vertPanel = new VerticalPanel();
		Label lname = new Label(test);
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

	public void showErrorPopup(String message, Throwable cause) {
		if (message == null)
			message = "";
		if (cause != null) {
			message = message + "\n" + cause.getMessage();
		}
		showErrorPopup(message);
	}

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

	public void showImpressum() {

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

	public void updateSearchPage(XSearch result, String status) {
		showSearchPage();

		mySortArea.update(status);
		if (result != null) {
			myResultArea.update(result.snippets);
			myCatArea.update(result.categories);
			myTagArea.update(result.tagsAppearingInSearchString);
		}
	}

	public void showContactForm() {

		Window.scrollTo(0, 0);
		PopupPanel ppnlContact = new PopupPanel(false);
		ppnlContact.setStyleName("contactForm");
		ppnlContact.setTitle("Contact");
		Contact contact = new Contact(ppnlContact);
		ppnlContact.setWidget(contact);
		ppnlContact.setGlassEnabled(true);
		ppnlContact.setPopupPosition(110, 100);
		ppnlContact.setWidth("340px");
		ppnlContact.show();
	}

	public void showCreateSnippetForm() {
		PopupPanel ppnlSnippet = new PopupPanel(true, true);
		// TODO CSS Style
		ppnlSnippet.setStyleName("contactForm");
		ppnlSnippet.setTitle("Create snippet");
		CreateSnippet newSnippet = new CreateSnippet(ppnlSnippet, "Create");
		ppnlSnippet.setWidget(newSnippet);
		ppnlSnippet.setGlassEnabled(true);
		ppnlSnippet.setPopupPosition(90, 104);
		ppnlSnippet.setWidth("250px");
		ppnlSnippet.show();

	}

	public void startSearch() {
		mySortArea.update("Searching ... ");
	}

	/**
	 * Creates a popup window with a link
	 * 
	 * @param convertToLink
	 *            link to be created
	 */
	public void showDownloadPopup(String message, String convertToLink) {
		if (message == null)
			message = "";
		if (convertToLink == null || convertToLink.isEmpty())
			return;

		final PopupPanel popup = new PopupPanel(true, true);
		Button close = new Button("<b>Close</b>");
		Anchor link = new Anchor(convertToLink);
		link.setHref(convertToLink);

		popup.setTitle("Link ");
		VerticalPanel vertPanel = new VerticalPanel();
		Label lname = new Label(message);
		vertPanel.add(lname);
		vertPanel.add(close);
		popup.setWidget(vertPanel);
		popup.setGlassEnabled(true);
		popup.setPopupPosition(110, 100);
		popup.setWidth("340px");
		popup.show();
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		});
	}

}
