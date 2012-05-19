package org.smartsnip.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.smartsnip.shared.XCategory;
import org.smartsnip.shared.XComment;
import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;

public class ResultArea extends Composite {

	CellList<XSnippet> myCellList;

	static final List<String> TESTTAGS = Arrays.asList("Tag1", "Tag2", "Tag3",
			"Tag4");
	static final XCategory CAT = new XCategory("catname", "description",
			"parent", new ArrayList<String>());
	static final XSnippet SNIPPET = new XSnippet("owner", 123, "title",
			"description", CAT, new ArrayList<String>(TESTTAGS),
			new ArrayList<XComment>(), "code", "codeHTML", "language",
			"license", 4);
	static final List<XSnippet> TESTLIST = Arrays.asList(SNIPPET, SNIPPET,
			SNIPPET, SNIPPET, SNIPPET, SNIPPET, SNIPPET, SNIPPET, SNIPPET,
			SNIPPET, SNIPPET, SNIPPET, SNIPPET, SNIPPET, SNIPPET, SNIPPET);

	public ResultArea() {

		ScrollPanel myPanel = new ScrollPanel();

		ShortSnipCell myShortSnipCell = new ShortSnipCell();

		myCellList = new CellList<XSnippet>(myShortSnipCell);
		myCellList.setRowData(0, TESTLIST);
		// Some special Features

		myCellList.setRowCount(TESTLIST.size(), true);

		myPanel.setHeight("600px");
		myPanel.add(myCellList);
		initWidget(myPanel);
		// Give the overall composite a style name.
		setStyleName("resultArea");
	}

	/**
	 * Internal callback, when a search returned (new) data values
	 * 
	 * @param result
	 */
	void searchDone(XSearch result) {
		// TODO Auto-generated method stub

	}

}
