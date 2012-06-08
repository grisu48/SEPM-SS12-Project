package org.smartsnip.client;

import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SnipArea extends Composite {

	private final XSnippet snippet;

	private final VerticalPanel vertPanel;
	private final HorizontalPanel horPanel;
	private final ScrollPanel scrPanel;
	private final Grid properties;
	private final Label title;
	private final Label description;
	private final Label language;
	private final Label license;

	private final Anchor anchViewFull;
	private final Anchor anchEdit;
	private final Anchor anchDownload;
	private final Rating rating;

	private final HTMLPanel snipFull;
	private final Button btnFav;

	SnipArea(final XSnippet mySnip) {

		snippet = mySnip;

		vertPanel = new VerticalPanel();
		horPanel = new HorizontalPanel();
		scrPanel = new ScrollPanel();
		properties = new Grid(4, 2);
		title = new Label("titel");
		description = new Label(mySnip.description);
		language = new Label(mySnip.language);
		license = new Label(mySnip.license);
		snipFull = new HTMLPanel(mySnip.codeHTML);
		anchViewFull = new Anchor("View full code");
		anchEdit = new Anchor("Edit snippet");
		anchDownload = new Anchor("Download source");
		rating = new Rating(5);

		properties.setWidget(0, 0, title);
		properties.setWidget(1, 0, description);
		properties.setWidget(2, 0, language);
		properties.setWidget(3, 0, license);
		properties.setWidget(0, 1, anchViewFull);
		properties.setWidget(1, 1, anchEdit);
		properties.setWidget(2, 1, anchDownload);
		properties.setWidget(3, 1, rating);

		btnFav = new Button("Add to Favourites");
		btnFav.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				control.toFav(mySnip.hash);
				btnFav.setEnabled(false);
			}
		});

		rating.setRatingHandler(new Rating.RatingHandler() {

			@Override
			public void unrate() {
				rate(0);
			}

			@Override
			public void rate(int rate) {
				Control.getInstance().rateSnippet(rate, snippet.hash);
			}
		});
		anchViewFull.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO improve code viewer
				Control.myGUI.showTestPopup(snippet.code);
			}
		});
		anchEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Control.myGUI
						.showErrorPopup("This has not been implemented yet!");

			}
		});
		anchDownload.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				anchDownload.setEnabled(false);

				ISnippet.Util.getInstance().getDownloadSourceTicket(
						snippet.hash, new AsyncCallback<Long>() {

							@Override
							public void onSuccess(Long result) {
								// Got the download link
								Control.myGUI
										.showDownloadPopup(
												"Downloadticket received ... \nClick on the following link to download ... ",
												convertToLink(result));
							}

							@Override
							public void onFailure(Throwable caught) {
								anchDownload.setEnabled(true);
								if (caught == null)
									return;
								if (caught instanceof NoAccessException) {
									Control.myGUI.showErrorPopup(
											"Access denied", caught);
								} else if (caught instanceof NotFoundException) {
									Control.myGUI
											.showErrorPopup(
													"Snippet hash id not found",
													caught);
								} else
									Control.myGUI.showErrorPopup("Error",
											caught);
							}

							/* Converts the donwload ticket into a link */
							private String convertToLink(long id) {

								String result = GWT.getHostPageBaseURL();
								if (!result.endsWith("/"))
									result += "/";

								result += "downloader?ticket=" + id;
								return result;
							}
						});
			}
		});

		anchDownload.setEnabled(false);
		ISnippet.Util.getInstance().hasDownloadableSource(snippet.hash,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							anchDownload
									.setTitle("Click here to request a download ticket");
							anchDownload.setEnabled(true);
						} else {
							anchDownload
									.setTitle("This snippet does not support any downloadable source");
							anchDownload.setEnabled(false);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						anchDownload.setTitle("Error"
								+ (caught == null ? "" : ": "
										+ caught.getMessage()));
						anchDownload.setEnabled(false);
					}
				});

		vertPanel.add(properties);
		vertPanel.add(scrPanel);
		vertPanel.add(horPanel);
		scrPanel.add(snipFull);
		horPanel.add(btnFav);

		initWidget(vertPanel);
		// Give the overall composite a style name.
		setStyleName("snipArea");
	}

	public void update() {
		// if necessary
	}

}
