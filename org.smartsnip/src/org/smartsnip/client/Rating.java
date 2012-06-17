package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Rating extends Composite {

	private final VerticalPanel pnlVertical;
	private final HorizontalPanel pnlHorizontal;
	private final Button btnNull;
	private final Button[] btnRatings;

	private final Label lblRating;

	/** Current set rating */
	private int rating = 0;

	private RatingHandler handler = new RatingHandler() {

		@Override
		public void unrate() {
		}

		@Override
		public void rate(int rate) {
		}
	};

	/**
	 * General class acting as click handler for all anchors
	 * 
	 */
	private class RatingClickHandler implements ClickHandler {

		private final int rate;

		protected RatingClickHandler(int rate) {
			this.rate = rate;
		}

		@Override
		public void onClick(ClickEvent event) {
			setRating(rate);
		}

	}

	/**
	 * Interface for the observer
	 * 
	 */
	public interface RatingHandler {
		/**
		 * Unrate has been clicked
		 */
		public void unrate();

		/**
		 * A rating has been clicked
		 */
		public void rate(int rate);
	}

	/**
	 * Creates a default rating widget with 5 stars plus a no rating start
	 */
	public Rating() {
		this(5);
	}

	/**
	 * Creates a rating composite within the given interval
	 * 
	 * @param count
	 *            Number of items, beginning from 1 to this number. If lower
	 *            than 1, only one element will be created
	 */
	public Rating(int count) {
		if (count < 1)
			count = 1;

		pnlVertical = new VerticalPanel();
		pnlHorizontal = new HorizontalPanel();

		lblRating = new Label("");

		btnNull = new Button("Unrate");
		btnNull.addClickHandler(new RatingClickHandler(0));
		pnlHorizontal.add(btnNull);
		btnRatings = new Button[count];
		for (int i = 0; i < count; i++) {
			btnRatings[i] = new Button((i + 1) + "");
			btnRatings[i].addClickHandler(new RatingClickHandler(i + 1));
			pnlHorizontal.add(btnRatings[i]);
		}

		pnlVertical.add(pnlHorizontal);
		pnlVertical.add(lblRating);
		initWidget(pnlVertical);
	}

	public void setRatingHandler(RatingHandler handler) {
		this.handler = handler;
	}

	@Override
	public void setStyleName(String name) {

		btnNull.setStyleName(name);
		for (Button btnRating : btnRatings) {
			btnRating.setStyleName(name);
		}
	}

	public void setEnabled(boolean enabled) {
		btnNull.setEnabled(enabled);
		for (Button btnRating : btnRatings)
			btnRating.setEnabled(enabled);
	}

	/**
	 * Sets the rating that is displayed
	 * 
	 * @param value
	 *            ratining, ranging from 0 to the defined maximum
	 */
	public void setRating(int value) {
		if (value == 0) {
			handler.unrate();
			lblRating.setText("Unrated");
		} else {
			handler.rate(value);
			lblRating.setText("Rated: " + value);
		}
		rating = value;

	}

	/**
	 * @return the maximum rating of this widget
	 */
	public int getMax() {
		return btnRatings.length;
	}

	/**
	 * @return the current set rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * Enabled or disabled the control buttons for rating. If disabled, the
	 * rating buttons are not shown.
	 * 
	 * @param enabled
	 *            if enabled or disabled
	 */
	public void setRatingEanbled(boolean enabled) {
		btnNull.setVisible(enabled);
		for (Button btnRating : btnRatings)
			btnRating.setVisible(enabled);
	}
}
