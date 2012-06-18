package org.smartsnip.client;

import org.smartsnip.shared.XSnippet;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * 
 * 
 * @author Paul
 * 
 *
 * A extendet class to display a preview of a snippet (as list item)
 *
 */
public class ShortSnipCell extends AbstractCell<XSnippet> {

	/**
	 * 
	 * The render method with creates the list item
	 */
	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			XSnippet snip, SafeHtmlBuilder sb) {

		// Value can be null, so do a null check..
		if (snip == null) {
			return;
		}

		sb.appendHtmlConstant("<table>");
		// Add title
		sb.appendHtmlConstant("<tr><td><b>");
		sb.appendHtmlConstant(snip.title);
		sb.appendHtmlConstant("</b>");
		if (snip.isFavorite)
			sb.appendHtmlConstant(" <i>(Favourite)</i>");
		if (snip.isOwn)
			sb.appendHtmlConstant(" <i>(Own snippet)</i>");
		sb.appendHtmlConstant("</td></tr>");
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
		sb.appendHtmlConstant(" -- rating: " + getRating(snip.rating));
		sb.appendHtmlConstant("</td></tr>");
		sb.appendHtmlConstant("</table>");
		// Add space
		sb.appendHtmlConstant("<br />");
	}

	/**
	 * Formats the rating
	 * 
	 * @param rating
	 *            to be formatted
	 * @return formatted string out of a rating
	 */
	private String getRating(float rating) {
		int temp = (int) (rating * 10);
		rating = (float) (temp / 10.0);
		return Float.toString(rating);
	}

}
