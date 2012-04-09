package org.smartsnip.client;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This class is used for a static access to message and option dialogs from
 * accross the project.
 * 
 * The class itself can not be created (private constructor). All methods must
 * be access in a static way
 * 
 */
public class MessageBox {

	/**
	 * This enumeration contains all results from an OptionPane
	 * 
	 */
	public enum OptionPaneResult {
		yes, no, cancel
	}

	/** No outer instances are allowed. */
	private MessageBox() {
	}

	/**
	 * Creates a new message box with a given message at the given position
	 * 
	 * If the given message is null or empty, nothing happens
	 * 
	 * @param message
	 *            to be displayed
	 * @param title
	 *            Title of the message to be displayed
	 * @param left
	 *            Position from the left (x coordiante)
	 * @param top
	 *            Position from the top (y coordiante)
	 */
	static void showMessageBox(final String message, final String title, int left, int top) {
		if (message == null || message.length() == 0) return;

		DialogBox box = new DialogBox() {
			Label lblMessage = new Label();

			/** Constructor for the anonymous inner class */
			{
				// Set the dialog box's caption.
				lblMessage.setText(message);
				if (title != null && title.length() > 0) {
					setText(title);
				} else {
					setText("Message");
				}

				// DialogBox is a SimplePanel, so you have to set its widget
				// property to
				// whatever you want its contents to be.
				Button ok = new Button("Ok");
				ok.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						hide();
					}
				});

				add(lblMessage);
				setWidget(ok);
			}
		};
		box.setPopupPosition(left, top);
		box.show();
	}

	/**
	 * Creates a new message box with a given message at the given position
	 * 
	 * If the given message is null or empty, nothing happens
	 * 
	 * @param message
	 *            to be displayed
	 * @param left
	 *            Position from the left (x coordiante)
	 * @param top
	 *            Position from the top (y coordiante)
	 */
	static void showMessageBox(final String message, int left, int top) {
		showMessageBox(message, "Message", left, top);
	}

	/**
	 * Creates a new message box with a given message and a default title
	 * 
	 * If the given message is null or empty, nothing happens
	 * 
	 * @param message
	 *            to be displayed
	 */
	static void showMessageBox(final String message) {
		showMessageBox(message, "Message");
	}

	/**
	 * Creates a new message box with a given message
	 * 
	 * If the given message is null or empty, nothing happens
	 * 
	 * @param message
	 *            to be displayed
	 * @param title
	 *            Title of the message to be displayed
	 */
	static void showMessageBox(final String message, final String title) {
		if (message == null || message.length() == 0) return;

		DialogBox box = new DialogBox() {
			Label lblMessage = new Label();

			/** Constructor for the anonymous inner class */
			{
				// Set the dialog box's caption.
				lblMessage.setText(message);
				if (title != null && title.length() > 0) {
					setText(title);
				} else {
					setText("Message");
				}

				// DialogBox is a SimplePanel, so you have to set its widget
				// property to
				// whatever you want its contents to be.
				Button ok = new Button("Ok");
				ok.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						hide();
					}
				});

				add(lblMessage);
				setWidget(ok);
			}
		};
		box.center();
		box.show();
	}

	/**
	 * Shows an option pane (currently only yes-no) and returns the result of
	 * the user interaction.
	 * 
	 * The message must not be null, otherwise the method returns null and does
	 * not display any message dialog.
	 * 
	 * @param message
	 *            to be displayed
	 * @param title
	 *            Title of the message to be displayed
	 * @param left
	 *            Position from the left (x coordiante)
	 * @param top
	 *            Position from the top (y coordiante)
	 * @return The button that the user clicked
	 */
	public static OptionPaneResult showOptionPane(final String message, final String title, int left, int top) {
		if (message == null || message.length() == 0) return null;

		final AtomicInteger result = new AtomicInteger(0); // Result of the
															// dialog
		DialogBox box = new DialogBox() {
			final Label lblMessage = new Label(message);
			final Button btYes = new Button("Yes");
			final Button btNo = new Button("No");

			/** Constructor for the anonymous inner class */
			{
				// Set the dialog box's caption.
				if (title != null && title.length() > 0) {
					setText(title);
				} else {
					setText("Message");
				}

				// DialogBox is a SimplePanel, so you have to set its widget
				// property to
				// whatever you want its contents to be.
				btYes.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						result.set(1);
						hide();
					}
				});
				btNo.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						result.set(0);
						hide();
					}
				});

				RootPanel.get().add(lblMessage);
				RootPanel.get().add(btYes);
				RootPanel.get().add(btNo);
			}
		};
		box.show();
		switch (result.get()) {
		case 1: // YES
			return OptionPaneResult.yes;
		case 2: // NO
			return OptionPaneResult.no;
		default: // CANCEL
			return OptionPaneResult.cancel;
		}
	}
}
