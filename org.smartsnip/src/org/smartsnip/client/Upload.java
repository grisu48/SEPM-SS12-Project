package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Upload extends Composite {

	private final PopupPanel parent;

	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private VerticalPanel vertPanel;
	private Label lTitle;
	private Label lText;
	private Button btnSend;
	private Button btnClose;
	private FileUpload fuUpload;
	private Label lStatus = new Label("");

	public Upload(PopupPanel parent, String send) {

		this.parent = parent;

		vertPanel = new VerticalPanel();
		lTitle = new Label("Upload");
		lTitle.setStyleName("h3");
		lText = new Label("Upload your Snippet");
		fuUpload = new FileUpload();
		
		
		btnSend = new Button(send);
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
		initWidget(vertPanel);
		// Give the overall composite a style name.
		setStyleName("upload");
	}



}
