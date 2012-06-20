package org.smartsnip.client;

import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Class for providing access to the download of source codes
 * 
 * @author Felix Niederwanger
 * 
 */
public class DownloadPopup {
	/** Associated snippet */
	private final XSnippet snippet;

	/** Main popup panel */
	private final PopupPanel popup = new PopupPanel();
	/** Root panel in the popup panel */
	private final Panel rootPanel = new VerticalPanel();

	/* Components */
	private final Label lblTitle;
	private final Label lblSnippet;
	private final Label lblStatus;
	private final Button btnStart;
	private final Button btnClose;
	private final Anchor anchLink;

	/* End components */

	/** Only inner instances allowed */
	private DownloadPopup(XSnippet snippet) {
		this.snippet = snippet;

		lblTitle = new Label("Downloading source");
		lblSnippet = new Label("Snippet: " + snippet.title);
		lblStatus = new Label("Click on the Button to start the process");
		btnStart = new Button("Get download");
		btnClose = new Button("Close");

		// Not active
		anchLink = new Anchor();
		anchLink.setVisible(false);

		btnStart.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnStart.setEnabled(false);
				startDownload();
			}
		});
		btnClose.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});

		HorizontalPanel pnlToolbar = new HorizontalPanel();
		pnlToolbar.add(btnStart);
		pnlToolbar.add(btnClose);

		rootPanel.add(lblTitle);
		rootPanel.add(lblSnippet);
		rootPanel.add(lblStatus);
		rootPanel.add(anchLink);
		rootPanel.add(pnlToolbar);
		popup.setWidget(rootPanel);

		applyStyle();
	}

	/** Applies all styles */
	private void applyStyle() {
		Window.scrollTo(0, 0);
		popup.setStyleName("contactForm");
		popup.setGlassEnabled(true);
		popup.setPopupPosition(90, 104);
		popup.setWidth("450px");
	}

	/**
	 * Shows the download popup for a given snippet
	 * 
	 * @param snippet
	 *            for that the code should be downloaded
	 */
	public static void showDownloadPopup(XSnippet snippet) {
		if (snippet == null)
			return;

		DownloadPopup popup = new DownloadPopup(snippet);
		popup.show();
	}

	/** Close popup dialog */
	private void close() {
		popup.hide();
	}

	/**
	 * Shows the popup dialog
	 */
	private void show() {
		popup.setAutoHideEnabled(true);
		popup.setModal(true);
		popup.show();
	}

	/**
	 * Starts the download sequence
	 */
	private void startDownload() {
		popup.setAutoHideEnabled(false);
		lblStatus.setText("Requesting download ticket ... ");

		ISnippet.Util.getInstance().getDownloadSourceTicket(snippet.hash,
				new AsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						if (result == null) {
							onFailure(new IllegalArgumentException(
									"Null-returned"));
							return;
						}

						// Got the download ticket - start download
						lblStatus.setText("Ticket received: " + result);
						startDownload(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						if (caught == null)
							lblStatus
									.setText("Getting download ticket failed: Unknown error");
						else if (caught instanceof NoAccessException)
							lblStatus
									.setText("Getting download ticket failed: Access denied");
						else if (caught instanceof NoAccessException)
							lblStatus
									.setText("Getting download ticket failed: No source found");
						else
							lblStatus
									.setText("Getting download ticket failed: "
											+ caught.getMessage());
						btnStart.setEnabled(true);
						btnStart.setText("Retry download");
					}
				});
	}

	/**
	 * Second step: Start the download with a received download ticket
	 * 
	 * @param downloadTicket
	 */
	private void startDownload(long downloadTicket) {
		lblStatus.setText("Requesting download ... ");
		String URL = Control.baseURL + "/downloader?ticket=" + downloadTicket;
		anchLink.setText("Download!");
		anchLink.setHref(URL);
		anchLink.setTarget("_blank"); // New window
	}
}
