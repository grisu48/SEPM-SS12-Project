package org.smartsnip.client;

import org.smartsnip.shared.IComment;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XComment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * PopupPanel for editing a given comment
 * 
 * @author Felix Niederwanger
 * 
 */
public class EditComment {
	/** Comment that currently is edited */
	private final XComment comment;

	/* Components */
	private final PopupPanel popup = new PopupPanel(false, true);

	private final VerticalPanel pnlVertMain = new VerticalPanel();
	private final Label lblTitle = new Label("Edit comment");
	private final Label lblOwner = new Label();
	private final Label lblLemons = new Label();
	private final Label lblChocolates = new Label();
	private final TextArea txtComment = new TextArea();
	private final HorizontalPanel pnlToolbar = new HorizontalPanel();
	private final Button btnApply = new Button("Apply");
	private final Button btnDelete = new Button("Delete");
	private final Button btnClose = new Button("Close");
	private final Label lblStatus = new Label();

	/* End of components */

	/**
	 * Only inner instances allowed. Create a new instance for editing a comment
	 * 
	 * @param comment
	 *            to be edited
	 */
	private EditComment(final XComment comment) {
		this.comment = comment;

		lblOwner.setText("Owner: " + comment.owner);
		lblLemons.setText("Positive votes: " + comment.positiveVotes);
		lblChocolates.setText("Negative votes: " + comment.negativeVotes);
		txtComment.setText(comment.message);

		if (!comment.canEdit)
			btnApply.setText("Apply (Access denied)");
		resetButtons();

		pnlToolbar.add(btnApply);
		pnlToolbar.add(btnDelete);
		pnlToolbar.add(btnClose);

		btnApply.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				apply();
			}
		});
		btnDelete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				delete();
			}
		});
		btnClose.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});

		pnlVertMain.add(lblTitle);
		pnlVertMain.add(lblOwner);
		pnlVertMain.add(lblLemons);
		pnlVertMain.add(lblChocolates);
		pnlVertMain.add(txtComment);
		pnlVertMain.add(pnlToolbar);
		pnlVertMain.add(lblStatus);

		applyStyle();
		popup.setTitle("Edit comment");
		popup.setWidget(pnlVertMain);
	}

	/** Applies the style for the popupmenu */
	private void applyStyle() {
		Window.scrollTo(0, 0);
		popup.setStyleName("contactForm");
		popup.setGlassEnabled(true);
		popup.setPopupPosition(90, 104);
		popup.setWidth("450px");
	}

	/**
	 * Starts the editing of a comment. This method shows a new
	 * {@link EditComment} popup and finishes, when the editing has done
	 * 
	 * 
	 * If the given parameter comment is null, nothing happens
	 * 
	 * @param comment
	 *            to be edited
	 */
	public static void startEditComment(XComment comment) {
		if (comment == null)
			return;

		EditComment edit = new EditComment(comment);
		edit.show();
	}

	/** Shows the popuppanel and starts the editing */
	private void show() {
		popup.show();
	}

	/**
	 * Closes the popup
	 */
	private void close() {
		popup.hide();
	}

	/** Applies the new comment */
	private void apply() {
		String newComment = txtComment.getText();
		if (newComment.isEmpty() || newComment.equals(comment.message))
			return;

		btnDelete.setEnabled(false);
		btnApply.setEnabled(false);

		lblStatus.setText("Editing ... ");
		IComment.Util.getInstance().edit(comment.id, newComment, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				lblStatus.setText("Success");
				close();
			}

			@Override
			public void onFailure(Throwable caught) {
				resetButtons();
				if (caught == null)
					lblStatus.setText("Failed (unknown error");
				else if (caught instanceof NoAccessException)
					lblStatus.setText("Access denied");
				else
					lblStatus.setText("Failed: " + caught.getMessage());
			}
		});
	}

	/** Resets the buttons */
	private void resetButtons() {
		btnDelete.setEnabled(comment.canDelete);
		btnApply.setEnabled(comment.canEdit);

	}

	/** Deletes the comment */
	private void delete() {
		btnDelete.setEnabled(false);
		btnApply.setEnabled(false);

		Control.myGUI.showConfirmPopup("Do you really want to delete this comment?\nA undo is NOT possible!", new ConfirmCallback() {

			@Override
			public void onYes() {
				// Delete
				lblStatus.setText("Deleting ... ");
				IComment.Util.getInstance().delete(comment.id, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						lblStatus.setText("Success");
						close();
					}

					@Override
					public void onFailure(Throwable caught) {
						resetButtons();
						if (caught == null)
							lblStatus.setText("Failed (unknown error");
						else if (caught instanceof NoAccessException)
							lblStatus.setText("Access denied");
						else
							lblStatus.setText("Failed: " + caught.getMessage());
					}
				});
			}

			@Override
			public void onNo() {
				resetButtons();
			}
		});
	}
}
