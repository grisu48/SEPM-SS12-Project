package org.smartsnip.client;

import java.util.List;

import org.smartsnip.shared.IUser;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class PersonalArea extends Composite {

	private final HorizontalPanel horPanel;
	private final PersonalField myPersonalField;
	private final ResultArea raOwn;
	private final ResultArea raFav;

	public PersonalArea() {

		horPanel = new HorizontalPanel();
		myPersonalField = new PersonalField();
		raOwn = new ResultArea();
		raOwn.setStyleName("raOwn");
		raOwn.setWidth("400px");
		raFav = new ResultArea();
		raFav.setStyleName("raFav");
		raFav.setWidth("400px");

		horPanel.add(myPersonalField);
		horPanel.add(raOwn);
		horPanel.add(raFav);

		initWidget(horPanel);
		// Give the overall composite a style name.
		setStyleName("personalArea");

		updateSnippets();

	}

	public void update(boolean worked) {
		myPersonalField.update(worked);
		updateSnippets();
	}

	public void updateSnippets() {
		IUser.Util.getInstance().getFavorites(
				new AsyncCallback<List<XSnippet>>() {

					@Override
					public void onSuccess(List<XSnippet> result) {
						if (result == null)
							return;
						raFav.update(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
		IUser.Util.getInstance().getSnippets(
				new AsyncCallback<List<XSnippet>>() {

					@Override
					public void onSuccess(List<XSnippet> result) {
						if (result == null)
							return;
						raOwn.update(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}
}
