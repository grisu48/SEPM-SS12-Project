package org.smartsnip.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smartsnip.core.Snippet;
import org.smartsnip.shared.XSnippet;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * SnippetOfDay servlet. This provides a servlet, that lets a (mobile) client
 * access a snippet of the day
 * 
 * 
 */
@RemoteServiceRelativePath("snippetofday")
public class SnippetOfDay extends HttpServlet {

	/** Serialisation ID */
	private static final long serialVersionUID = 7786134724230388825L;

	@SuppressWarnings("unused")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

		PrintWriter writer = null;
		try {
			writer = resp.getWriter();

			writer.println("<head>");
			writer.println("<title>Smartsnip.org - Snippet of the day</title>");
			writer.println("</head>");

			writer.println("<body>");

			// Get the snippet of the day
			Snippet snippetOfday = null;
			if (snippetOfday == null) {
				// Internal server error
				writer.println("<p><b>Error 500</b></p>");
				writer.println("<p>The server has faced an internal server error</p>");
				writer.println("<p>Unfortunately today there is no snippet of the day :-(</p>");
				writer.println("<hr>");
				writer.println("<p>We're working on this problem. Please try again later.</p>");
			} else {
				XSnippet result = snippetOfday.toXSnippet();

				// Add meta data

			}

			writer.println("</body>");
		} catch (IOException e) {
			// Ignore exception
			writer = null;
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

}
