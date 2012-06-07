package org.smartsnip.client;

import org.smartsnip.shared.ISnippet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateSnippet extends Composite {

	private final PopupPanel parent;

	private final VerticalPanel pnlRootPanel;

	private final HorizontalPanel pnlControl;
	private final Label lblTitle;
	private final Label lblName;
	private final Label lblDesc;
	private final Label lblCode;
	private final Label lblLanguage;
	private final TextBox txtName;
	private final TextArea txtDescription;
	private final TextArea txtCode;
	private final ListBox lstLanguage;

	private final Label lblStatus;

	private final Button btCreate;
	private final Button btCancel;

	public CreateSnippet(PopupPanel parent, String buttonname) {
		this.parent = parent;

		pnlRootPanel = new VerticalPanel();
		pnlControl = new HorizontalPanel();

		lblTitle = new Label("Create Snippet");
		lblTitle.setStyleName("h3");
		
		lblName = new Label();
		lblDesc = new Label();
		lblCode = new Label();
		lblLanguage = new Label();
		txtName = new TextBox();
		txtDescription = new TextArea();
		txtCode = new TextArea();
		lstLanguage = new ListBox();
		lstLanguage.addItem("Java");

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

	
		

		pnlControl.add(btCreate);
		pnlControl.add(btCancel);
		pnlControl.add(lblStatus);

		pnlRootPanel.add(lblTitle);
		pnlRootPanel.add(lblName);
		pnlRootPanel.add(txtName);
		pnlRootPanel.add(lblDesc);
		pnlRootPanel.add(txtDescription);
		pnlRootPanel.add(lblCode);
		pnlRootPanel.add(txtCode);

		pnlRootPanel.add(lblCode);
		pnlRootPanel.add(txtCode);
		pnlRootPanel.add(lstLanguage);

		pnlRootPanel.add(pnlControl);

		initWidget(pnlRootPanel);
	}

	private void createSnippet() {
		String name = txtName.getText();
		String desc = txtDescription.getText();
		String language;
		String code = txtCode.getText();

		if (lstLanguage.getSelectedIndex() == -1) {
			language = "";
		} else {
			language = lstLanguage.getItemText(lstLanguage.getSelectedIndex());
		}

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
