package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Contact extends Composite {

	private final PopupPanel parent;

	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private VerticalPanel vertPanel;
	private Label lMail;
	private TextBox tbMail;
	private Label lMessage;
	private TextArea taMessage;
	private Button btnSend;
	private Button btnClose;
	private Label lStatus = new Label("");

	public Contact(PopupPanel parent) {

		this.parent = parent;

		vertPanel = new VerticalPanel();
		lMail = new Label("E-Mail");
		tbMail = new TextBox();
		lMessage = new Label("Your Message");
		taMessage = new TextArea();
		taMessage.setVisibleLines(5);
		btnSend = new Button("Send");
		btnSend.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// XXX todo
			}

		});
		btnClose = new Button("Cancel");
		btnClose.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Contact.this.parent.hide();
			}
		});

		vertPanel.add(lMail);
		vertPanel.add(tbMail);
		vertPanel.add(lMessage);
		vertPanel.add(taMessage);
		buttonPanel.add(btnSend);
		buttonPanel.add(btnClose);

		vertPanel.add(buttonPanel);
		vertPanel.add(lStatus);
		initWidget(vertPanel);
		// Give the overall composite a style name.
		setStyleName("contact");
	}



}
