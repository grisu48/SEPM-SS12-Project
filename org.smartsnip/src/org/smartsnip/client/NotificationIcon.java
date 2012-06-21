package org.smartsnip.client;

import org.smartsnip.client.GUI.Page;
import org.smartsnip.shared.ISession;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * An icon used for displaying notifications
 * 
 * @author Felix Niederwanger
 * 
 */
public class NotificationIcon extends Composite {

	/** Number of new notifications */
	private int newNotifications = 0;

	/** Notification image URL */
	private final String imageURL_NONEW = Control.baseURL + "/images/notification.png";
	private final String imageURL_NEW = Control.baseURL + "/images/notification_new.png";

	/** This component is based on a button with text and a image */
	private final Button btnNotifications;

	/** Root panel of the component */
	private final Panel rootPanel = new FlowPanel();

	/** Initialises a new NotififactionIcon */
	public NotificationIcon() {
		btnNotifications = new Button("");

		btnNotifications.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Control.getInstance().changeSite(Page.PAGE_Notifications);
			}
		});

		/* CSS hack, so that we have a button with text and graphics */
		btnNotifications.setPixelSize(10, 10);
		DOM.setStyleAttribute(btnNotifications.getElement(), "background", "transparent url('" + imageURL_NONEW + "')");
		DOM.setStyleAttribute(btnNotifications.getElement(), "border", "solid 0px white");
		DOM.setStyleAttribute(btnNotifications.getElement(), "textAlign", "center");
		rootPanel.add(btnNotifications);

		initWidget(rootPanel);
	}

	/** Update the component */
	public void update() {
		ISession.Util.getInstance().getNotificationCount(true, new AsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				if (result == null) {
					onFailure(new IllegalArgumentException("NUll returned"));
					return;
				}

				long temp = result;
				newNotifications = (int) temp;
				refreshComponent();
			}

			@Override
			public void onFailure(Throwable caught) {
				// Ignore
				newNotifications = 0;
				refreshComponent();
			}
		});
	}

	/**
	 * Refreshes the component based on the parameters that are set Here no
	 * update takes place!!
	 * */
	private void refreshComponent() {
		boolean newArrived = (newNotifications > 0);
		if (newArrived) {
			// notification_new.png
			btnNotifications.setText(newNotifications + "");
			DOM.setStyleAttribute(btnNotifications.getElement(), "background", "transparent url('" + imageURL_NEW + "')");
		} else {
			// notification.png
			btnNotifications.setText("");
			DOM.setStyleAttribute(btnNotifications.getElement(), "background", "transparent url('" + imageURL_NONEW + "')");
		}
	}
}
