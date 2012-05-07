package org.smartsnip.client;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Login extends Composite {
	
	private VerticalPanel vertPanel = new VerticalPanel();
	private Label lname = new Label("Username");
	private TextBox name = new TextBox();
	private Label lpw = new Label("Password");
	private PasswordTextBox pw = new PasswordTextBox();
	private Button login = new Button("Login");
	
	public Login() {
		
	    vertPanel = new VerticalPanel();
	    lname = new Label("Username");
	    name = new TextBox();
	    lpw = new Label("Password");
	    pw = new PasswordTextBox();
	    login = new Button("Login");
	    login.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		          Control control = Control.getInstance();
		          control.login(name.getText(), pw.getText());
		        }

		      });
	    
	    vertPanel.add(lname);
	    vertPanel.add(name);
	    vertPanel.add(lpw);
	    vertPanel.add(pw);
	    vertPanel.add(login);
	   
	
		initWidget(vertPanel);
	    // Give the overall composite a style name.
	    setStyleName("login");
	}

	


}
