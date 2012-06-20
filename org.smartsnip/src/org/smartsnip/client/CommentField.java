package org.smartsnip.client;

import java.util.Date;

import org.smartsnip.shared.IComment;
import org.smartsnip.shared.XComment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * 
 * @author Paul
 * @author Felix Niederwanger
 * 
 *         A composed Widget to display one single comment
 * 
 */
public class CommentField extends Composite {
	/** Current associated comment */
	private final XComment comment;

	/** Parent comment area this component belongs to */
	final CommentArea parent;

	private final HorizontalPanel horPnlMain;

	private final HorizontalPanel horPanel;
	private final HorizontalPanel horToolbar;
	private final VerticalPanel pnlVertButtons;
	private final HTMLPanel commentText;

	private final VerticalPanel verPanel;
	private final Label lblOwner;
	private final Label lblDate;
	private final Label lblRating;

	private final Anchor anchRatePositive;
	private final Anchor anchRateNegative;
	private final Anchor anchUnvote;

	private final Button btnEdit;
	private final Button btnDelete;

	private boolean canRate = false;

	/**
	 * Initializes the comment
	 * 
	 * @param myComment
	 *            a XComment
	 */
	public CommentField(final CommentArea parent, final XComment myComment) {
		this.comment = myComment;
		this.parent = parent;

		horPnlMain = new HorizontalPanel();
		horPanel = new HorizontalPanel();
		commentText = new HTMLPanel(myComment.message);

		verPanel = new VerticalPanel();
		lblOwner = new Label(myComment.owner);
		lblDate = new Label(getTimeString(myComment.time));
		lblRating = new Label(getRatingString(myComment.positiveVotes,
				myComment.negativeVotes));

		anchRatePositive = new Anchor("Vote positive");
		anchRateNegative = new Anchor("Vote negative");
		anchUnvote = new Anchor("Unvote");
		anchUnvote.setStyleName("toollink");
		btnEdit = new Button("Edit");
		btnDelete = new Button("Delete");

		horToolbar = new HorizontalPanel();
		pnlVertButtons = new VerticalPanel();

		anchRatePositive.setStyleName("toollink");
		anchRatePositive.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				enableRatingLinks(false);
				IComment.Util.getInstance().votePositive(comment.id,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								enableRatingLinks(true);
								update();
							}

							@Override
							public void onFailure(Throwable caught) {
								enableRatingLinks(true);
							}
						});
			}
		});
		anchRateNegative.setStyleName("toollink");
		anchRateNegative.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				enableRatingLinks(false);
				IComment.Util.getInstance().voteNegative(comment.id,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								enableRatingLinks(true);
								update();
							}

							@Override
							public void onFailure(Throwable caught) {
								enableRatingLinks(true);
							}
						});
			}
		});

		anchUnvote.setStyleName("toollink");
		anchUnvote.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				enableRatingLinks(false);
				IComment.Util.getInstance().unvote(comment.id,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								enableRatingLinks(true);
								update();
							}

							@Override
							public void onFailure(Throwable caught) {
								enableRatingLinks(true);
							}
						});
			}
		});
		enableRatingLinks(false);
		IComment.Util.getInstance().canComment(comment.id,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						enableRatingLinks(true);
						canRate = result;
					}

					@Override
					public void onFailure(Throwable caught) {
						// Something went wrong
						// TODO: Error handling??
					}
				});

		btnEdit.setVisible(comment.canEdit);
		btnEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				edit();
			}
		});

		btnDelete.setVisible(comment.canDelete);
		btnDelete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				delete();
			}
		});

		// XXX Ugly hack, that should be removed one day
		IComment.Util.getInstance().canRate(comment.id,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						enableRatingLinks(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// Ignore
					}
				});

		horToolbar.add(anchUnvote);
		horToolbar.add(anchRatePositive);
		horToolbar.add(anchRateNegative);

		horPanel.add(lblOwner);
		horPanel.add(lblDate);
		horPanel.add(lblRating);

		pnlVertButtons.add(btnEdit);
		pnlVertButtons.add(btnDelete);

		verPanel.add(commentText);
		verPanel.add(horPanel);
		verPanel.add(horToolbar);
		verPanel.add(pnlVertButtons);

		horPnlMain.add(verPanel);
		horPnlMain.add(pnlVertButtons);

		initWidget(horPnlMain);
		// Give the overall composite a style name.
		setStyleName("commentField");
	}

	/**
	 * Gets a string out of the ratings of a comment
	 * 
	 * @param positiveVotes
	 *            number of positive votes
	 * @param negativeVotes
	 *            number of negative votes
	 * @return the string that should be displayed
	 */
	private String getRatingString(int positiveVotes, int negativeVotes) {
		if (positiveVotes == 0 && negativeVotes == 0)
			return "No votes";

		StringBuffer buffer = new StringBuffer("");

		if (positiveVotes > 0) {
			buffer.append("+");
			buffer.append(positiveVotes);

		}
		if (negativeVotes > 0) {
			buffer.append("-");
			buffer.append(negativeVotes);

		}

		return buffer.toString();
	}

	/**
	 * The string that is coming out of a date
	 * 
	 * @param time
	 * @return
	 */
	private String getTimeString(Date time) {
		if (time == null)
			return "";
		long diff = System.currentTimeMillis() - time.getTime();

		int days = (int) (diff / (1000 * 60 * 60 * 24));
		if (days > 2)
			return days + " days ago";

		int hours = (int) (diff / (1000 * 60 * 60));
		if (hours > 1)
			return hours + " hours ago";

		int minutes = (int) (diff / (1000 * 60));
		if (minutes > 2)
			return minutes + " minutes ago";

		return "A moment ago ...";

	}

	private void enableRatingLinks(boolean enabled) {
		anchUnvote.setEnabled(enabled);
		anchRatePositive.setEnabled(enabled);
		anchRateNegative.setEnabled(enabled);
	}

	/**
	 * Updates the comment
	 */
	public void update() {
		IComment.Util.getInstance().getComment(comment.id,
				new AsyncCallback<XComment>() {

					@Override
					public void onSuccess(XComment result) {
						if (result == null)
							return;
						if (result.id != comment.id)
							return;

						comment.message = result.message;
						comment.negativeVotes = result.negativeVotes;
						comment.owner = result.owner;
						comment.positiveVotes = result.positiveVotes;
						comment.snippet = result.snippet;
						comment.time = result.time;

						updateComponents();
					}

					@Override
					public void onFailure(Throwable caught) {
						// Ignore
					}
				});
	}

	/**
	 * Updates all components according to the values set in comment
	 */
	private void updateComponents() {
		lblOwner.setText(comment.owner);
		lblDate.setText(getTimeString(comment.time));
		lblRating.setText(getRatingString(comment.positiveVotes,
				comment.negativeVotes));
	}

	/** Edit the comment */
	private void edit() {
		EditComment.startEditComment(comment);
		parent.update();
	}

	/** Delete the comment */
	private void delete() {
		if (Control.myGUI
				.showConfirmPopup(
						"Are you sure, you want to delete the selected comment? (Undo NOT possible!)\n  "
								+ comment.message, "Confirmation") == true) {
			// Delete
			disable();
			IComment.Util.getInstance().delete(comment.id,
					new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							// Is deleted
							CommentField.this.setVisible(false);
							parent.update();
						}

						@Override
						public void onFailure(Throwable caught) {
							// something went wrong ...
							resetControls();
						}
					});
		}
	}

	/** Disables all components */
	private void disable() {
		btnDelete.setEnabled(false);
		btnEdit.setEnabled(false);
		anchRateNegative.setEnabled(false);
		anchRatePositive.setEnabled(false);
		anchUnvote.setEnabled(false);
	}

	/** Resets the enabled status of all controls */
	private void resetControls() {
		btnDelete.setEnabled(comment.canDelete);
		btnEdit.setEnabled(comment.canEdit);
		anchRateNegative.setEnabled(canRate);
		anchRatePositive.setEnabled(canRate);
		anchUnvote.setEnabled(canRate);
	}

}
