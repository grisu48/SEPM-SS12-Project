package org.smartsnip.client;

import java.util.List;

import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * 
 * 
 * @author Paul
 * 
 *
 * A composed Widget to display the results of a snippet search
 *
 */
public class ResultArea extends Composite {

	private final CellList<XSnippet> myCellList;
	private final VerticalPanel pnlVerticalMain;

	/**
	 * Initializes the result area
	 * 
	 */
	public ResultArea() {

		pnlVerticalMain = new VerticalPanel();
		ScrollPanel myPanel = new ScrollPanel();

		pnlVerticalMain.add(myPanel);

		ShortSnipCell myShortSnipCell = new ShortSnipCell();
		myCellList = new CellList<XSnippet>(myShortSnipCell);

		// Add a selection model to handle user selection.
		final SingleSelectionModel<XSnippet> selectionModel = new SingleSelectionModel<XSnippet>();
		myCellList.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						XSnippet selected = selectionModel.getSelectedObject();
						if (selected != null) {
							Control control = Control.getInstance();
							control.changeToSnipPage(selected);
						}
					}
				});

		// myCellList.setRowData(0, TESTLIST);
		myPanel.setHeight("400px");
		myPanel.add(myCellList);
		initWidget(pnlVerticalMain);
		// Give the overall composite a style name.
		setStyleName("resultArea");
	}

	/**
	 * Updates the result area
	 * 
	 * @param snippets a XSnippet list
	 */
	void update(List<XSnippet> snippets) {
		if (snippets == null)
			return;

		myCellList.setRowCount(snippets.size(), true);
		myCellList.setRowData(0, snippets);
	}

}
