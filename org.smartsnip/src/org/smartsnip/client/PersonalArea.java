package org.smartsnip.client;

import java.util.List;

import org.smartsnip.shared.ISession;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

/**
 * 
 * 
 * @author Paul
 * 
 * 
 *         A composed Widget to display the personal information
 * 
 */
public class PersonalArea extends Composite {

	/* Controls */
	private final Grid grid;
	private PersonalField myPersonalField;
	private ResultArea raOwn;
	private final ResultArea raFav;

	// private final Label lblMyPersonalArea;
	private final Label lblOwnSnippets;
	private final Label lblFavorites;

	/* End of controls */

	/** Indicating if currently displaying reduced controls for a guest session */
	private boolean guestSession;

	/**
	 * Initializes the personal area
	 * 
	 */
	public PersonalArea() {
		grid = new Grid(2, 3);

		lblOwnSnippets = new Label("My snippets");
		lblFavorites = new Label("Favorites");
		raFav = new ResultArea();
		myPersonalField = new PersonalField();

		// Check if guest session, that hied the field PersonalArea and
		// OwnSnippets
		if (!Control.getInstance().isLoggedIn()) {
			// Guest session
			guestSession = true;

			raOwn = null;

			grid.setWidget(0, 2, lblFavorites);
			grid.setWidget(1, 2, raFav);
		} else {
			// User session
			guestSession = false;

			raOwn = new ResultArea();

			grid.setWidget(0, 1, lblOwnSnippets);
			grid.setWidget(1, 1, raOwn);
		}

		// grid.setWidget(0, 0, lblMyPersonalArea);
		grid.setWidget(1, 0, myPersonalField);
		grid.setWidget(0, 2, lblFavorites);
		grid.setWidget(1, 2, raFav);
		initWidget(grid);

		updateSnippets();
		applyStyles();
	}

	private void applyStyles() {
		raFav.setWidth("400px");
		raFav.setStyleName("raFav");
		if (raOwn != null) {
			raOwn.setWidth("400px");
			raOwn.setStyleName("raOwn");
		}
		// Give the overall composite a style name.
		setStyleName("personalArea");
	}

	/**
	 * Updates the personal area without the personal data
	 * 
	 */
	public void update() {
		// Check logged in state
		if (Control.getInstance().isLoggedIn() != guestSession) {
			// Login state has been changed, also update reduced fields
			guestSession = Control.getInstance().isLoggedIn();
			if (guestSession) {
				// Guest session - hide fields
				grid.remove(lblOwnSnippets);
				grid.remove(raOwn);

				raOwn = null;
			} else {
				// User session - show new fields
				raOwn = new ResultArea();

				// grid.setWidget(0, 0, lblMyPersonalArea);
				grid.setWidget(0, 1, lblOwnSnippets);
				grid.setWidget(1, 1, raOwn);
			}
		}

		myPersonalField.update();

		updateSnippets();
	}

	/**
	 * Updates the snippet lists
	 */
	public void updateSnippets() {
		lblFavorites.setText("Getting favourites ... ");
		ISession.Util.getInstance().getFavorites(new AsyncCallback<List<XSnippet>>() {

			@Override
			public void onSuccess(List<XSnippet> result) {
				if (result == null) {
					onFailure(new IllegalArgumentException("Server returned null"));
					return;
				}
				lblFavorites.setText("Favourites (" + result.size() + ")");
				raFav.update(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught == null)
					lblFavorites.setText("Favourites: Unknown error - Please try again");
				else
					lblFavorites.setText("Error fetching favourites: " + caught.getMessage());
			}
		});

		if (raOwn != null) {

			lblOwnSnippets.setText("Getting own snippets ... ");
			IUser.Util.getInstance().getSnippets(new AsyncCallback<List<XSnippet>>() {

				@Override
				public void onSuccess(List<XSnippet> result) {
					if (result == null) {
						onFailure(new IllegalArgumentException("Server returned null"));
						return;
					}
					lblOwnSnippets.setText("My created snippets (" + result.size() + ")");
					raOwn.update(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					if (caught == null)
						lblOwnSnippets.setText("Own snippets: Unknown error - Please try again");
					else
						lblOwnSnippets.setText("Error fetching own snippets: " + caught.getMessage());
				}
			});

		}
	}

}
