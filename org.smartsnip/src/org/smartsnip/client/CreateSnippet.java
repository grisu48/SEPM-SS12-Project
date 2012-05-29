package org.smartsnip.client;

import org.smartsnip.shared.ISnippet;

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

public class CreateSnippet extends Composite {

	private final PopupPanel parent;

	private final VerticalPanel pnlRootPanel;

	private final HorizontalPanel pnlVertName;
	private final HorizontalPanel pnlVertDesc;
	private final HorizontalPanel pnlLanguage;
	private final HorizontalPanel pnlControl;

	private final Label lblName;
	private final Label lblDesc;
	private final Label lblCode;
	private final Label lblLanguage;
	private final TextBox txtName;
	private final TextArea txtDescription;
	private final TextArea txtCode;
	private final TextBox txtLanguage;
	private final Label lblStatus;

	private final Button btCreate;
	private final Button btCancel;

	public CreateSnippet(PopupPanel parent, String buttonname) {
		this.parent = parent;

		pnlRootPanel = new VerticalPanel();
		pnlVertName = new HorizontalPanel();
		pnlVertDesc = new HorizontalPanel();
		pnlLanguage = new HorizontalPanel();
		pnlControl = new HorizontalPanel();

		lblName = new Label();
		lblDesc = new Label();
		lblCode = new Label();
		lblLanguage = new Label();
		txtName = new TextBox();
		txtDescription = new TextArea();
		txtCode = new TextArea();
		txtLanguage = new TextBox();

		btCreate = new Button(buttonname);
		btCreate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createSnippet();
			}
		});
		btCancel = new Button("Cancel");
		btCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});

		lblStatus = new Label("");

		lblName.setText("Snippet name");
		lblDesc.setText("Description");
		lblLanguage.setText("Language");
		lblCode.setText("Snippet's code");

		pnlVertName.add(lblName);
		pnlVertName.add(txtName);

		pnlVertDesc.add(lblDesc);
		pnlVertDesc.add(txtDescription);

		pnlControl.add(btCreate);
		pnlControl.add(btCancel);
		pnlControl.add(lblStatus);

		pnlLanguage.add(lblLanguage);
		pnlLanguage.add(txtLanguage);

		pnlRootPanel.add(pnlVertName);
		pnlRootPanel.add(pnlVertDesc);
		pnlRootPanel.add(lblCode);
		pnlRootPanel.add(txtCode);
		pnlRootPanel.add(pnlLanguage);
		pnlRootPanel.add(pnlControl);

		initWidget(pnlRootPanel);
	}

	private void createSnippet() {
		String name = txtName.getText();
		String desc = txtDescription.getText();
		String language = txtLanguage.getText();
		String code = txtCode.getText();

		// TODO Error messages
		lblStatus.setText("");
		if (name.isEmpty() || desc.isEmpty() || language.isEmpty()
				|| code.isEmpty()) {
			lblStatus.setText("Some arguments are missing");
			return;
		}

		// TODO Category and Tags
		btCreate.setEnabled(false);
		lblStatus.setText("Creating snippet ... ");
		ISnippet.Util.getInstance().create(name, desc, code, language, null,
				null, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						lblStatus.setText("Success");
						close();
					}

					@Override
					public void onFailure(Throwable caught) {
						btCreate.setEnabled(true);
						lblStatus.setText("Error creating snippet: "
								+ caught.getMessage());
					}
				});
	}

	private void close() {
		this.parent.hide();
	}
}
