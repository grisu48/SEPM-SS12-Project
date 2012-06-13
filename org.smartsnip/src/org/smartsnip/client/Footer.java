package org.smartsnip.client;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Footer extends Composite {
	
	private Grid footerGrid;
	private VerticalPanel vertPanel1;
	private VerticalPanel vertPanel2;
	private VerticalPanel vertPanel3;
	private Label copyright;
	private Label name1;
	private Label name2;
	private Anchor aName3;
	private Label links;
	private Label about;
	private Anchor impressum;
	private Anchor register;
	private Anchor aContact;
	private Anchor aLink1;
	private Anchor aLink2;
	private Anchor aSource;
	private Anchor aAndroid;
	
	
	public Footer() {
		footerGrid = new Grid(1,3);
		vertPanel1 = new VerticalPanel();
		vertPanel2 = new VerticalPanel();
		vertPanel3 = new VerticalPanel();
		copyright = new Label("Copyright");
		copyright.addStyleName("h4");
		name1 = new Label("Gerhard Aigner");
		name2 = new Label("Felix Niederwanger");
		aName3 = new Anchor("Paul Opitz", false, "http://perdix.at");
		aSource = new Anchor("Download Source Code", false, "");
		aAndroid = new Anchor("Download Android Client", false, "");
		links = new Label("Links");
		links.addStyleName("h4");
		aLink1 = new Anchor("Universität Innsbruck", false ,"http://uibk.ac.at");
		aLink2 = new Anchor("You can make IT", false ,"http://youcanmakeit.at");
		about = new Label("About this Website");
		about.addStyleName("h4");
		impressum = new Anchor("Impressum");
		impressum.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		          Control control = Control.getInstance();
		          control.changeSite('i');
		        }

		      });
		register = new Anchor("Registration");
		register.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		          Control control = Control.getInstance();
		          control.changeSite('r');
		        }

		      });
		aContact = new Anchor("Contact");
		aContact.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		          Control control = Control.getInstance();
		          control.changeSite('c');
		        }

		      });
		
		
		
		
		vertPanel1.add(copyright);
		vertPanel1.add(name1);
		vertPanel1.add(name2);
		vertPanel1.add(aName3);
		vertPanel2.add(about);
		vertPanel2.add(impressum);
		vertPanel2.add(register);
		vertPanel2.add(aContact);
		vertPanel2.add(aSource);
		vertPanel2.add(aAndroid);
		vertPanel3.add(links);
		vertPanel3.add(aLink1);
		vertPanel3.add(aLink2);
		footerGrid.setWidget(0,0,vertPanel1);
		footerGrid.setWidget(0,1,vertPanel2);
		footerGrid.setWidget(0,2,vertPanel3);
		footerGrid.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		footerGrid.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		footerGrid.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
		footerGrid.setWidth("100%");
		

		
	
		initWidget(footerGrid);
	    // Give the overall composite a style name.
	    setStyleName("footerGrid");
	}
	


}
