package org.smartsnip.client;

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

}
