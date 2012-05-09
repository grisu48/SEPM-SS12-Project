package org.smartsnip.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.smartsnip.shared.XComment;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;


public class ResultArea extends Composite {

	CellList<XSnippet> myCellList;

	private static final List<XSnippet> DAYS = Arrays.asList(new XSnippet("owner", 123, "description", new ArrayList<String>(), new ArrayList<XComment>(),"code", "codeHTML", "language", "license", 4), new XSnippet("owner", 123, "description", new ArrayList<String>(), new ArrayList<XComment>(),"code", "codeHTML", "language", "license", 4));
	
	
	public ResultArea() {
		ShortSnipCell myShortSnipCell = new ShortSnipCell();
		
		myCellList = new CellList<XSnippet>(myShortSnipCell);
		myCellList.setRowData(0, DAYS);

	

		initWidget(myCellList);
	    // Give the overall composite a style name.
	    setStyleName("resultArea");
	}
	
	
}
