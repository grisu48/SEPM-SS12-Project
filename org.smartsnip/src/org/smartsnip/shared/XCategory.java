package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XCategory implements IsSerializable {
	public String name;
	public String description;
	public String parent;
	public List<String> subcategories;

	public XCategory() {

	}

	public XCategory(String name, String description, String parent, List<String> subcategories) {
		this.name = name;
		this.description = description;
		this.parent = parent;
		this.subcategories = subcategories;
	}

}
