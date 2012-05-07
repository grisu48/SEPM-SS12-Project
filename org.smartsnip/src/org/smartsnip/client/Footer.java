package org.smartsnip.client;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	private Label name3;
	private Label links;
	private Label link1;
	private Label about;
	private Anchor impressum;
	
	
	public Footer() {
		footerGrid = new Grid(1,3);
		vertPanel1 = new VerticalPanel();
		vertPanel2 = new VerticalPanel();
		vertPanel3 = new VerticalPanel();
		copyright = new Label("Copyright");
		copyright.addStyleName("h3");
		name1 = new Label("Gerhard Aigner");
		name2 = new Label("Felix Niederwanger");
		name3 = new Label("Paul Opitz");
		links = new Label("Links");
		links.addStyleName("h3");
		link1 = new Label("Example link");
		about = new Label("About this Website");
		about.addStyleName("h3");
		impressum = new Anchor("Impressum");
		impressum.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		          Control control = Control.getInstance();
		          control.changeSite('i');
		        }

		      });
		
		
		
		
		vertPanel1.add(copyright);
		vertPanel1.add(name1);
		vertPanel1.add(name2);
		vertPanel1.add(name3);
		vertPanel2.add(about);
		vertPanel2.add(impressum);
		vertPanel3.add(links);
		vertPanel3.add(link1);
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
