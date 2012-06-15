package org.smartsnip.client;

import org.smartsnip.shared.XSnippet;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class ShortSnipCell extends AbstractCell<XSnippet> {

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, XSnippet snip, SafeHtmlBuilder sb) {

		// Value can be null, so do a null check..
		if (snip == null) {
			return;
		}

		sb.appendHtmlConstant("<table>");

		// Add title
		sb.appendHtmlConstant("<tr><td><b><span style='h4'>");
		sb.appendHtmlConstant(snip.title);
		sb.appendHtmlConstant("</span></b></td></tr>");
		// Add desc
		sb.appendHtmlConstant("<tr><td>");
		sb.appendHtmlConstant(snip.description);
		sb.appendHtmlConstant("</td></tr>");
		// Add language and license
		sb.appendHtmlConstant("<tr><td>");
		sb.appendHtmlConstant(snip.language);
		sb.appendHtmlConstant(" -- ");
		sb.appendHtmlConstant(snip.license);
		sb.appendHtmlConstant("</td></tr>");
		// Add view counts
		sb.appendHtmlConstant("<tr><td>");
		sb.appendHtmlConstant(snip.viewcount + " views");
		sb.appendHtmlConstant(" -- rating: " + snip.rating);
		sb.appendHtmlConstant("</td></tr>");
		sb.appendHtmlConstant("</table>");
		// Add space
		sb.appendHtmlConstant("<br />");
	}

}
