package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.shared.ICategory;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.XCategory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
	private final HorizontalPanel pnlProp;
	private final HorizontalPanel pnlTags;
	private final HorizontalPanel pnlAddedTags;
	private final Label lblTitle;
	private final Label lblName;
	private final Label lblDesc;
	private final Label lblCode;
	private final Label lblLanguage;
	private final TextBox txtName;
	private final TextArea txtDescription;
	private final TextArea txtCode;
	private final ListBox lstLanguage;
	private final ListBox lstCategory;
	private final TextBox txtTag;
	private final Label lblTags;
	private final Button btnTag;
	private final ArrayList<String> taglist;
	private final Label lblStatus;

	private final Button btCreate;
	private final Button btCancel;

	public CreateSnippet(PopupPanel parent, String buttonname) {
		this.parent = parent;

		pnlRootPanel = new VerticalPanel();
		pnlProp = new HorizontalPanel();
		pnlControl = new HorizontalPanel();
		pnlTags = new HorizontalPanel();
		pnlAddedTags = new HorizontalPanel();

		taglist = new ArrayList<String>();

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
		lstCategory = new ListBox();

		ISnippet.Util.getInstance().getSupportedLanguages(
				new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						lblStatus
								.setText("Error retrieving supported languages");
					}

					@Override
					public void onSuccess(List<String> result) {
						if (result == null)
							return;
						for (String lang : result)
							lstLanguage.addItem(lang);
					}
				});

		ICategory.Util.getInstance().getCategories(null,
				new AsyncCallback<List<XCategory>>() {

					@Override
					public void onSuccess(List<XCategory> result) {
						if (result == null)
							return;
						for (XCategory category : result) {
							lstCategory.addItem(category.name);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Error handling
						lblStatus.setText("Error fetching categories list");
					}
				});

		lstCategory.addItem("Category");

		txtTag = new TextBox();
		txtTag.setStyleName("txtTag");
		txtTag.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
					addTag(txtTag.getText());
				}
			}
		});

		lblTags = new Label("Add Tags");
		btnTag = new Button("Add");
		btnTag.setStyleName("add");
		btnTag.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				addTag(txtTag.getText());
			}
		});

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

		pnlProp.add(lstLanguage);
		pnlProp.add(lstCategory);

		pnlTags.add(txtTag);
		pnlTags.add(btnTag);

		pnlRootPanel.add(lblTitle);
		pnlRootPanel.add(lblName);
		pnlRootPanel.add(txtName);
		pnlRootPanel.add(lblDesc);
		pnlRootPanel.add(txtDescription);
		pnlRootPanel.add(lblCode);
		pnlRootPanel.add(txtCode);

		pnlRootPanel.add(pnlProp);
		pnlRootPanel.add(lblTags);
		pnlRootPanel.add(pnlTags);
		pnlRootPanel.add(pnlAddedTags);
		pnlRootPanel.add(pnlControl);

		initWidget(pnlRootPanel);
	}

	private void createSnippet() {

		String name = txtName.getText();
		String desc = txtDescription.getText();
		String language;
		String code = txtCode.getText();
		String cat;

		if (lstLanguage.getSelectedIndex() == -1) {
			language = "";
		} else {
			language = lstLanguage.getItemText(lstLanguage.getSelectedIndex());
		}

		if (lstCategory.getSelectedIndex() == -1) {
			cat = "";
		} else {
			cat = lstCategory.getItemText(lstCategory.getSelectedIndex());
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
		ISnippet.Util.getInstance().create(name, desc, code, language, cat,
				taglist, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						lblStatus.setText("Success");
						close();
					}

					@Override
					public void onFailure(Throwable caught) {
						btCreate.setEnabled(true);
						if (caught == null)
							lblStatus.setText("Error creating snippet");
						else
							lblStatus.setText("Error creating snippet: "
									+ caught.getMessage());
					}
				});
	}

	private void close() {
		this.parent.hide();
	}

	public void addTag(final String tag) {
		if (tag == null || tag.isEmpty())
			return;

		if (taglist.contains(tag))
			return;
		taglist.add(tag);

		final Anchor anchTag = new Anchor(tag);
		anchTag.setStyleName("tag");

		// Click on Tag to remove it
		anchTag.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (taglist.contains(tag))
					taglist.remove(tag);
				pnlAddedTags.remove(anchTag);
			}
		});
		pnlAddedTags.add(anchTag);
	}

}
