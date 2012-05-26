package org.smartsnip.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.smartsnip.shared.XCategory;
import org.smartsnip.shared.XComment;
import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

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
		
		// Add a selection model to handle user selection.
	    final SingleSelectionModel<XSnippet> selectionModel = new SingleSelectionModel<XSnippet>();
	    myCellList.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        XSnippet selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	          Control control = Control.getInstance();
	          control.changeToSnipPage(selected);
	        }
	      }
	    });
	    
	    
		//myCellList.setRowData(0, TESTLIST);
		myPanel.setHeight("600px");
		myPanel.add(myCellList);
		initWidget(myPanel);
		// Give the overall composite a style name.
		setStyleName("resultArea");
	}

	
	void update(List<XSnippet> snippets) {
		myCellList.setRowData(0, snippets);
		myCellList.setRowCount(snippets.size(), true);
	}

}
