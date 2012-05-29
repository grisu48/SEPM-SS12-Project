package org.smartsnip.client;

import java.util.List;

import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;


public class PersonalArea extends Composite {

	private HorizontalPanel horPanel;
	private PersonalField myPersonalField;
	private ResultArea raOwn;
	private ResultArea raFav;
	
	
	public PersonalArea() {

		horPanel = new HorizontalPanel();
		myPersonalField = new PersonalField();
		raOwn = new ResultArea();
		raOwn.setWidth("400px");
		raFav = new ResultArea();
		raFav.setWidth("400px");
		
		raOwn.update(getOwn());
		raFav.update(getFav());
	
		horPanel.add(myPersonalField);
		horPanel.add(raOwn);
		horPanel.add(raFav);

		initWidget(horPanel);
		// Give the overall composite a style name.
		setStyleName("personalArea");

	}
	
	public void update(boolean worked) {
		myPersonalField.update(worked);
	}
	
	private List<XSnippet> getOwn() {
		Control control = Control.getInstance();
		return control.getOwn();
		
	}
	
	private List<XSnippet> getFav() {
		return null;
	}

}
