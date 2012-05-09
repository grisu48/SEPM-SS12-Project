package org.smartsnip.client;

import java.util.Set;

import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShortSnipCell extends AbstractCell<XSnippet> {

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			XSnippet snip, SafeHtmlBuilder sb) {
		
		
		
		// TODO Auto-generated method stub
		
		
		 // Value can be null, so do a null check..
	      if (snip == null) {
	        return;
	      }

	      sb.appendHtmlConstant("<table>");

	      // Add title
	      sb.appendHtmlConstant("<tr><td>");
	      sb.appendHtmlConstant("title");
	      sb.appendHtmlConstant("</td></tr>");
	      // Add desc
	      sb.appendHtmlConstant("<tr><td>");
	      sb.appendHtmlConstant(snip.description);
	      sb.appendHtmlConstant("</td></tr>");
	      // Add code
	      sb.appendHtmlConstant("<tr><td>");
	      sb.appendHtmlConstant(snip.codeHTML);
	      sb.appendHtmlConstant("</td></tr>");
	      
	      sb.appendHtmlConstant("</table>");
	    
		
		
	}




	
	
	
}
