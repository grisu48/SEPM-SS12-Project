package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Rating extends Composite {

	private final HorizontalPanel pnlHorizontal;
	private final Anchor anchNull;
	private final Anchor[] anchRatings;

	private RatingHandler handler = null;

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
			if (handler == null)
				return;

			if (rate == 0)
				handler.unrate();
			else
				handler.rate(rate);
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

		pnlHorizontal = new HorizontalPanel();

		anchNull = new Anchor("Unrate");
		anchNull.addClickHandler(new RatingClickHandler(0));
		pnlHorizontal.add(anchNull);
		anchRatings = new Anchor[count];
		for (int i = 0; i < count; i++) {
			anchRatings[i] = new Anchor((i + 1) + "");
			anchRatings[i].addClickHandler(new RatingClickHandler(i + 1));
			pnlHorizontal.add(anchRatings[i]);
		}

		initWidget(pnlHorizontal);
	}

	public void setRatingHandler(RatingHandler handler) {
		this.handler = handler;
	}

	@Override
	public void setStyleName(String name) {

		anchNull.setStyleName(name);
		for (Anchor anchRating : anchRatings) {
			anchRating.setStyleName(name);
		}
	}
}
