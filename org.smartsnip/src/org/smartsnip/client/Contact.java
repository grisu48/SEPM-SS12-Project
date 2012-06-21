package org.smartsnip.client;

import org.smartsnip.shared.IAdministrator;
import org.smartsnip.shared.NoAccessException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * 
 * @author Paul
 * 
 * 
 *         A composed Widget to display the contact window
 * 
 */
public class Contact extends Composite {

	private final PopupPanel parent;

	private final HorizontalPanel buttonPanel = new HorizontalPanel();
	private final VerticalPanel vertPanel;
	private final Label lTitle;
	private final Label lMail;
	private final TextBox tbMail;
	private final Label lMessage;
	private final TextArea taMessage;
	private final Button btnSend;
	private final Button btnClose;
	private final Label lStatus = new Label("");

	/**
	 * Initializes the contact popup
	 * 
	 * @param parent
	 *            - the parent window popup
	 */
	public Contact(PopupPanel parent) {

		this.parent = parent;

		vertPanel = new VerticalPanel();
		lTitle = new Label("Contact");
		lTitle.setStyleName("h3");
		lMail = new Label("E-Mail");
		tbMail = new TextBox();
		lMessage = new Label("Your Message");
		taMessage = new TextArea();
		taMessage.setVisibleLines(5);
		btnSend = new Button("Send");
		btnSend.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				send();
			}

		});
		btnClose = new Button("Cancel");
		btnClose.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Contact.this.parent.hide();
			}
		});

		vertPanel.add(lTitle);
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

	/** Submits the message to the server */
	private void send() {
		String message = taMessage.getText();
		String email = tbMail.getText();
		if (message.length() < 5)
			return;
		if (email.length() < 5)
			return;

		setStatus("Sending ... ", false);
		IAdministrator.Util.getInstance().messageToAdmin(message, email, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				setStatus("Sent successfully");
				close();
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught == null)
					setStatus("Unknown error occured", true);
				else if (caught instanceof NoAccessException)
					setStatus("Access denied", true);
				else if (caught instanceof IllegalArgumentException)
					setStatus("Illegal argument: " + caught.getMessage(), true);
				else
					setStatus("Error: " + caught.getCause(), true);
			}
		});
	}

	/** Close popup */
	private void close() {
		parent.hide();
	}

	/**
	 * Set status message
	 * 
	 * @param message
	 *            to be displayed
	 */
	private void setStatus(String message) {
		lStatus.setText(message);
	}

	/**
	 * Set status message
	 * 
	 * @param message
	 *            to be displayed
	 * @param controlsEnabled
	 *            controls enabled or disabled
	 */
	private void setStatus(String message, boolean controlsEnabled) {
		lStatus.setText(message);
		btnSend.setEnabled(controlsEnabled);
	}

}
