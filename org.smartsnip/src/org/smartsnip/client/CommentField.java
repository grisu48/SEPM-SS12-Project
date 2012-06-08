package org.smartsnip.client;

import java.util.Date;

import org.smartsnip.shared.IComment;
import org.smartsnip.shared.XComment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentField extends Composite {

	private final XComment comment;

	private final HorizontalPanel horPanel;
	private final HorizontalPanel horToolbar;
	private final HTMLPanel commentText;

	private final VerticalPanel verPanel;
	private final Label lblOwner;
	private final Label lblDate;
	private final Label lblRating;

	private final Anchor anchRatePositive;
	private final Anchor anchRateNegative;
	private final Anchor anchUnvote;

	public CommentField(final XComment myComment) {
		this.comment = myComment;

		horPanel = new HorizontalPanel();
		commentText = new HTMLPanel(myComment.message);

		verPanel = new VerticalPanel();
		lblOwner = new Label(myComment.owner);
		lblDate = new Label(getTimeString(myComment.time));
		lblRating = new Label(getRatingString(myComment.positiveVotes,
				myComment.negativeVotes));

		horToolbar = new HorizontalPanel();

		anchRatePositive = new Anchor("Rate positive");
		anchRatePositive.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				enableRatingLinks(false);
				IComment.Util.getInstance().votePositive(comment.id,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								enableRatingLinks(true);
								updateRatings();
							}

							@Override
							public void onFailure(Throwable caught) {
								enableRatingLinks(true);
							}
						});
			}
		});
		anchRateNegative = new Anchor("Rate negative");
		anchRateNegative.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				enableRatingLinks(false);
				IComment.Util.getInstance().voteNegative(comment.id,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								enableRatingLinks(true);
								updateRatings();
							}

							@Override
							public void onFailure(Throwable caught) {
								enableRatingLinks(true);
							}
						});
			}
		});

		anchUnvote = new Anchor("Unvote");
		anchUnvote.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				enableRatingLinks(false);
				IComment.Util.getInstance().unvote(comment.id,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								enableRatingLinks(true);
								updateRatings();
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
					}

					@Override
					public void onFailure(Throwable caught) {
						// Something went wrong
						// TODO: Error handling??
					}
				});

		horToolbar.add(anchUnvote);
		horToolbar.add(anchRatePositive);
		horToolbar.add(anchRateNegative);

		horPanel.add(lblOwner);
		horPanel.add(lblDate);
		horPanel.add(lblRating);

		verPanel.add(commentText);
		verPanel.add(horPanel);
		verPanel.add(horToolbar);

		initWidget(verPanel);
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
		StringBuffer buffer = new StringBuffer("");

		if (positiveVotes > 0) {
			buffer.append(positiveVotes);
			buffer.append(" positive ");
		}
		if (negativeVotes > 0) {
			buffer.append(negativeVotes);
			buffer.append(" negative");
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

	public void update() {
		// TODO Implement me
	}

	private void updateRatings() {
		// TODO Implement me

		enableRatingLinks(false);
		IComment.Util.getInstance().canComment(comment.id,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						enableRatingLinks(true);
					}

					@Override
					public void onFailure(Throwable caught) {
						// Something went wrong
						// TODO: Error handling??
					}
				});
	}
}
