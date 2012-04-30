package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XCategory implements IsSerializable {
	public final String name;
	public final String description;
	public final String parent;
	public final List<String> subcategories;

	public XCategory(String name, String description, String parent, List<String> subcategories) {
		this.name = name;
		this.description = description;
		this.parent = parent;
		this.subcategories = subcategories;
	}

}
