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
 * A composed Widget to display the personal information
 *
 */
public class PersonalArea extends Composite {

	private final Grid grid;
	private final PersonalField myPersonalField;
	private final ResultArea raOwn;
	private final ResultArea raFav;

	// private final Label lblMyPersonalArea;
	private final Label lblOwnSnippets;
	private final Label lblFavorites;

	/**
	 * Initializes the personal area
	 * 
	 */
	public PersonalArea() {

		grid = new Grid(2, 3);
		// This is needed as placeholder
		// lblMyPersonalArea = new Label("");
		lblOwnSnippets = new Label("My created snippets");
		lblFavorites = new Label("Favourites");
		myPersonalField = new PersonalField();

		raOwn = new ResultArea();
		raOwn.setStyleName("raOwn");
		raOwn.setWidth("400px");
		raFav = new ResultArea();
		raFav.setStyleName("raFav");
		raFav.setWidth("400px");

		// grid.setWidget(0, 0, lblMyPersonalArea);
		grid.setWidget(1, 0, myPersonalField);
		grid.setWidget(0, 1, lblOwnSnippets);
		grid.setWidget(1, 1, raOwn);
		grid.setWidget(0, 2, lblFavorites);
		grid.setWidget(1, 2, raFav);

		initWidget(grid);
		// Give the overall composite a style name.
		setStyleName("personalArea");

		updateSnippets();

	}

	/**
	 * Updates the personal area
	 * 
	 * @param worked - a boolean message if the update on the server was succesful
	 */
	public void update(boolean worked) {
		myPersonalField.update(worked);
		updateSnippets();
	}
	
	/**
	 * Updates the personal area without the personal data
	 * 
	 */
	public void update() {
		updateSnippets();
	}
	

	/**
	 * Updates the snippet lists
	 */
	public void updateSnippets() {
		lblFavorites.setText("Getting favourites ... ");
		ISession.Util.getInstance().getFavorites(
				new AsyncCallback<List<XSnippet>>() {

					@Override
					public void onSuccess(List<XSnippet> result) {
						if (result == null) {
							onFailure(new IllegalArgumentException(
									"Server returned null"));
							return;
						}
						lblFavorites.setText("Favourites (" + result.size()
								+ ")");
						raFav.update(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						if (caught == null)
							lblFavorites
									.setText("Favourites: Unknown error - Please try again");
						else
							lblFavorites.setText("Error fetching favourites: "
									+ caught.getMessage());
					}
				});
		lblOwnSnippets.setText("Getting own snippets ... ");
		IUser.Util.getInstance().getSnippets(
				new AsyncCallback<List<XSnippet>>() {

					@Override
					public void onSuccess(List<XSnippet> result) {
						if (result == null) {
							onFailure(new IllegalArgumentException(
									"Server returned null"));
							return;
						}
						lblOwnSnippets.setText("My created snippets ("
								+ result.size() + ")");
						raOwn.update(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						if (caught == null)
							lblOwnSnippets
									.setText("Own snippets: Unknown error - Please try again");
						else
							lblOwnSnippets
									.setText("Error fetching own snippets: "
											+ caught.getMessage());
					}
				});
	}

	
}
