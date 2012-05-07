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

public class Meta extends Composite {
	
	private HorizontalPanel metaPanel;
	private Label user;
	private Anchor login;
	private Anchor register;
	
	
	public Meta() {
		
		metaPanel = new HorizontalPanel();
		user = new Label("Guest");
		login = new Anchor("Login");
		login.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		          Control control = Control.getInstance();
		          control.changeSite('l');
		        }

		      });
		register = new Anchor("Register");
		register.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		          Control control = Control.getInstance();
		          control.changeSite('r');
		        }

		      });
		metaPanel.add(user);
		metaPanel.add(login);
		metaPanel.add(register);

		
	
		initWidget(metaPanel);
	    // Give the overall composite a style name.
	    setStyleName("meta");
	}
	


}
