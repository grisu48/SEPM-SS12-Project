package org.smartsnip.client;

import org.smartsnip.server.Uploader;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Version 2 of the Upload Popup to upload a source file to a code object.
 * 
 * The request is sent to the {@link Uploader} servlet "upload" with the
 * argument "codeID". This is hard-coded an defined as the protocol for the
 * uploader
 * 
 * @author Felix Niederwanger
 * 
 */
public class UploadCode extends PopupPanel {
	/** Associated code ID */
	private final long codeID;

	/* Controls */
	private final FormPanel form = new FormPanel();

	private final VerticalPanel vertMain;
	private final HorizontalPanel horToolbar;

	private final Label lblTitle;

	private final Label lblStatus;
	private final Button btnUpload;
	private final Button btnCancel;

	private final FileUpload upload;

	/* End controls */

	@SuppressWarnings("deprecation")
	public UploadCode(final long codeID) {
		super();

		this.codeID = codeID;

		setTitle("Upload code source");
		lblTitle = new Label("Upload file");
		vertMain = new VerticalPanel();
		horToolbar = new HorizontalPanel();
		upload = new FileUpload();
		lblStatus = new Label("");
		btnUpload = new Button("Upload");
		btnCancel = new Button("Close");

		upload.setName("file_upload");

		form.addFormHandler(new FormHandler() {

			@Override
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				vertMain.clear();
				vertMain.add(new HTML(event.getResults()));
				vertMain.add(btnCancel);
				btnCancel.setText("Close");
				btnCancel.setEnabled(true);
			}

			@Override
			public void onSubmit(FormSubmitEvent event) {
				String file = upload.getFilename();
				if (file.isEmpty()) {
					Window.alert("Select a file first!");
					event.setCancelled(true);
				}
			}
		});

		btnUpload.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnUpload.setEnabled(false);
				btnCancel.setEnabled(false);
				lblStatus.setText("Uploading file ... ");

				/* This part is there for the transmission */
				form.setEncoding(FormPanel.ENCODING_MULTIPART);
				form.setMethod(FormPanel.METHOD_POST);
				form.setAction("upload?codeID=" + codeID + "&session="
						+ Control.getSessionCookie());
				form.submit();

			}
		});
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});

		horToolbar.add(btnUpload);
		horToolbar.add(btnCancel);

		vertMain.add(lblTitle);
		vertMain.add(upload);
		vertMain.add(lblStatus);
		vertMain.add(horToolbar);

		form.add(vertMain);
		setWidget(form);

		applyStyle();

	}

	/** Apply style */
	private void applyStyle() {
		Window.scrollTo(0, 0);
		setStyleName("uploadForm");
		setGlassEnabled(true);
		setPopupPosition(90, 104);
		setWidth("250px");
	}

	private void close() {
		this.hide();
	}

	/**
	 * Shows the upload dialog for a code object identified by its id
	 * 
	 * @param codeID
	 *            ID of the code, the upload belongs to
	 */
	public static void show(long codeID) {
		UploadCode upload = new UploadCode(codeID);
		upload.show();
	}
}
