package org.smartsnip.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class GUI {

	// Create userPanel
	HorizontalPanel userPanel = new HorizontalPanel();
	// Create searchPanel
	HorizontalPanel searchPanel = new HorizontalPanel();
	// Create dataPanel
	HorizontalPanel dataPanel = new HorizontalPanel();
	// Create footerPanel
	SimplePanel footerPanel = new SimplePanel();
	
	
	public void getReady() {
		// Adds a personalized CSS-File
		Resources.INSTANCE.css().ensureInjected(); 
		
		//Create the Page
		createBasicPage();
		showSearchPage();
		//showImpressum();
		//showSnipPage();
		//showPersonalPage();
		//showLoginPopup();
		//showRegisterPopup();
		//showTestPopup();
	}

	
	
	
	
	
	
	public void createBasicPage() {
		
		// Fill userPanel
		Label user = new Label("Guest");
		Label login = new Label("Login");
		Label register = new Label("Register");
		userPanel.add(user);
		userPanel.add(login);
		userPanel.add(register);
		
		// Fill searchPanel
		SuggestBox searchSnippet = new SuggestBox();
		Button searchButton = new Button("Search Snippet");
		searchButton.addStyleName("searchButton");
		searchPanel.add(searchSnippet);
		searchPanel.add(searchButton);
		
		//Fill footerPanel
		Footer footer = new Footer();
		footerPanel.setWidth("100%");
		footerPanel.add(footer);

		
		
		// Fix Panels to divs
		RootPanel.get("user").add(userPanel);
		RootPanel.get("search").add(searchPanel);
		RootPanel.get("data").add(dataPanel);
		RootPanel.get("footer").add(footerPanel);
		
		// Sets Cursor
		// z.B. ewSymbolTextBox.setFocus(true);
		
		
		
	}
	
	
	public void showSearchPage() {
		
		TabArea myTabArea = new TabArea();
		VerticalPanel rightPanel = new VerticalPanel();
		rightPanel.setStyleName("rightPanel");
		CatArea myCatArea = new CatArea();
		TagArea myTagArea = new TagArea();
	
		Button test1 = new Button("test");
		Button test2 = new Button("test");
		
		dataPanel.add(myTabArea);
		dataPanel.add(rightPanel);
		rightPanel.add(myCatArea);
		rightPanel.add(myTagArea);
	
	}
	
	
	
	
	public void showSnipPage() {
		SnipArea mySnipArea = new SnipArea();
		CommentArea myCommentArea = new CommentArea();
		dataPanel.add(mySnipArea);
		dataPanel.add(myCommentArea);
		
	}
	
	public void showPersonalPage() {
		PersonalArea myPersonalArea = new PersonalArea();
		VerticalPanel vertPanel = new VerticalPanel();
		dataPanel.add(myPersonalArea);
		dataPanel.add(vertPanel);
		
	}
	
	public void showLoginPopup() {
		
		
		PopupPanel loginPanel = new PopupPanel(false);
		loginPanel.setStyleName("Login");
	    loginPanel.setTitle("Login");
	    VerticalPanel vertPanel = new VerticalPanel();
	    Label lname = new Label("Username");
	    TextBox name = new TextBox();
	    Label lpw= new Label("Password");
	    PasswordTextBox pw = new PasswordTextBox();
	    Button login = new Button("Login");
	    vertPanel.add(lname);
	    vertPanel.add(name);
	    vertPanel.add(lpw);
	    vertPanel.add(pw);
	    vertPanel.add(login);
	    loginPanel.setWidget(vertPanel);
	    loginPanel.setGlassEnabled(true);
	    loginPanel.setPopupPosition(110,100);
	    loginPanel.setWidth("340px");
	    loginPanel.show();
	}
	
public void showTestPopup(String test) {
		
		PopupPanel loginPanel = new PopupPanel(true);
	    loginPanel.setTitle(test);
	    VerticalPanel vertPanel = new VerticalPanel();
	    Label lname = new Label(test);
	    vertPanel.add(lname);
	    loginPanel.setWidget(vertPanel);
	    loginPanel.setGlassEnabled(true);
	    loginPanel.setPopupPosition(110,100);
	    loginPanel.setWidth("340px");
	    loginPanel.show();
	}
	
	
	public void showRegisterPopup() {
		PopupPanel registerPanel = new PopupPanel(false);
		registerPanel.setStyleName("Register");
		registerPanel.setTitle("Register");
	    VerticalPanel vertPanel = new VerticalPanel();
	    Label lname = new Label("Username");
	    TextBox name = new TextBox();
	    name.setText("myusername");
	    TextBox mail = new TextBox();
	    Label lmail= new Label("E-Mail");
	    mail.setText("user@provider.com");
	    Label lpw1= new Label("Create Password");
	    PasswordTextBox pw1 = new PasswordTextBox();
	    Label lpw2= new Label("Confirm Password");
	    PasswordTextBox pw2 = new PasswordTextBox();
	    Button login = new Button("Register");
	    vertPanel.add(lname);
	    vertPanel.add(name);
	    vertPanel.add(lmail);
	    vertPanel.add(mail);
	    vertPanel.add(lpw1);
	    vertPanel.add(pw1);
	    vertPanel.add(lpw2);
	    vertPanel.add(pw2);
	    vertPanel.add(login);
	    registerPanel.setWidget(vertPanel);
	    registerPanel.setGlassEnabled(true);
	    registerPanel.setPopupPosition(110,100);
	    registerPanel.setWidth("340px");
	    registerPanel.show();
	}
	
	public void showImpressum() {
		HTML impressum = new HTML("<p><br /><strong>Verantwortlich für die Inhalte<br /></strong>Paul Opitz <br />"+
	"Roveretoplatz 2 <br />6330 Kufstein <br />Österreich</p><p><br /><strong>Haftung für die Inhalte</strong><br />"+
	"Die Inhalte unserer Seiten wurden mit größter Sorgfalt erstellt. Für die Richtigkeit, Vollständigkeit und Aktualität der Inhalte kann ich jedoch keine Gewähr übernehmen.</p><p><br /><strong>Haftung für Links</strong><br />Unser Angebot enthält Links zu externen Webseiten Dritter, auf deren Inhalte ich keinen Einfluss habe. Deshalb kann ich für diese fremden Inhalte auch keine Gewähr übernehmen. Für die Inhalte der verlinkten Seiten ist stets der jeweilige Anbieter oder Betreiber der Seiten verantwortlich. Die verlinkten Seiten wurden zum Zeitpunkt der Verlinkung auf mögliche Rechtsverstöße überprüft. Rechtswidrige Inhalte waren zum Zeitpunkt der Verlinkung nicht erkennbar. Eine permanente inhaltliche Kontrolle der verlinkten Seiten ist jedoch ohne konkrete Anhaltspunkte einer Rechtsverletzung nicht zumutbar. Bei Bekanntwerden von Rechtsverletzungen werde ich derartige Links umgehend entfernen.</p><br /><strong>Rechte</strong><br />Die Inhalte stehen unter einer <a href='http://creativecommons.org/licenses/by-nc-sa/3.0/at/' rel='license'>Creative Commons Namensnennung-Nicht-kommerziell-Weitergabe unter gleichen Bedingungen 3.0 Österreich Lizenz</a>.<p><br /><strong>Datenschutz</strong><br />Die Nutzung unserer Webseite ist in der Regel ohne Angabe personenbezogener Daten möglich. Soweit auf meinen Seiten personenbezogene Daten (beispielsweise Name, Anschrift oder E-Mail-Adressen) erhoben werden, erfolgt dies, soweit möglich, stets auf freiwilliger Basis. Diese Daten werden ohne Ihre ausdrückliche Zustimmung nicht an Dritte weitergegeben.Ich weise darauf hin, dass die Datenübertragung im Internet (z.B. bei der Kommunikation per E-Mail) Sicherheitslücken aufweisen kann. Ein lückenloser Schutz der Daten vor dem Zugriff durch Dritte ist nicht möglich.Der Nutzung von im Rahmen der Impressumspflicht veröffentlichten Kontaktdaten durch Dritte zur Übersendung von nicht ausdrücklich angeforderter Werbung und Informationsmaterialien wird hiermit ausdrücklich widersprochen. Der Betreiber der Seiten behaltet sich ausdrücklich rechtliche Schritte im Falle der unverlangten Zusendung von Werbeinformationen, etwa durch Spam-Mails, vor.</p>");
		dataPanel.add(impressum);
	}

}
