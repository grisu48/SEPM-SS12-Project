package org.smartsnip.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Upload extends Composite {

	private final PopupPanel parent;

	private final FormPanel form = new FormPanel();

	private final HorizontalPanel buttonPanel = new HorizontalPanel();
	private final VerticalPanel vertPanel;
	private final Label lTitle;
	private final Label lText;
	private final Button btnSend;
	private final Button btnClose;
	private final FileUpload fuUpload;
	private final Label lStatus = new Label("");

	public Upload(PopupPanel parent) {
		this(parent, "upload", null);
	}

	/**
	 * Generates a new upload process in a PopupPanel. The given action is the
	 * destination, where the POST request with the file is sent to
	 * 
	 * @param parent
	 *            Parent PopupPanel
	 * @param action
	 *            Destination of the POST sequence
	 */
	@SuppressWarnings("deprecation")
	public Upload(final PopupPanel parent, final String action,
			final String parameter) {

		this.parent = parent;

		vertPanel = new VerticalPanel();
		lTitle = new Label("Upload");
		lTitle.setStyleName("h3");
		lText = new Label("Upload your Snippet");
		fuUpload = new FileUpload();
		fuUpload.setName("snippet");

		form.addFormHandler(new FormHandler() {

			@Override
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				lStatus.setText("Upload complete.");
				close();
			}

			@Override
			public void onSubmit(FormSubmitEvent event) {
				String file = fuUpload.getFilename();
				if (file.isEmpty()) {
					Window.alert("Select a file first!");
					event.setCancelled(true);
					return;
				}

				// POST it
				btnSend.setEnabled(false);
				btnClose.setEnabled(false);
				lStatus.setText("Uploading file ... ");
				fuUpload.setEnabled(false);
				Upload.this.parent.setAutoHideEnabled(false);
			}
		});

		btnSend = new Button("Upload");
		btnSend.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/* This code block is responsible for the code upload */
				form.setEncoding(FormPanel.ENCODING_MULTIPART);
				form.setMethod(FormPanel.METHOD_POST);
				String strAction;
				if (parameter != null && parameter.length() > 0)
					strAction = action + "?" + parameter;
				else
					strAction = action;

				strAction = GWT.getModuleBaseURL() + strAction;
				form.setAction(strAction);
				form.submit();
				/* End of upload code block */

			}

		});
		btnClose = new Button("Close");
		btnClose.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Upload.this.parent.hide();
			}
		});

		vertPanel.add(lTitle);
		vertPanel.add(lText);
		vertPanel.add(fuUpload);
		buttonPanel.add(btnSend);
		buttonPanel.add(btnClose);

		vertPanel.add(buttonPanel);
		vertPanel.add(lStatus);
		form.setWidget(vertPanel);
		initWidget(form);
		// Give the overall composite a style name.
		setStyleName("upload");
	}

	/** Closes this popup panel */
	public void close() {
		parent.hide();
	}
}