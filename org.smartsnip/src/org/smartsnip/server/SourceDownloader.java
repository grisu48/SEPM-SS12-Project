package org.smartsnip.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.shared.ISession;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;

public class SourceDownloader extends HttpServlet {

	/** Timer task for checking if tickets are expired */
	private static final TimerTask expirationTask;
	/** Timer for checking if tickets are expired */
	private static final Timer expirationTimer;

	/** Associated session for this servlet call */
	private Session session = null;

	/** Snippet request argument header */
	public static final String SNIPPET_REQUEST = "snip_id";

	/** Max file size */
	public static final long maxFileBytes = 1024 * 1024 * 25;

	static {
		expirationTimer = new Timer(true);
		expirationTask = new TimerTask() {

			@Override
			public void run() {
				// Check for expired tickets and remove them

				synchronized (tickets) {
					List<Ticket> allTickets = new ArrayList<Ticket>(
							tickets.values());

					for (Ticket ticket : allTickets) {
						if (ticket.isExpired())
							tickets.remove(ticket.id);
					}
				}
			}
		};

		expirationTimer.schedule(expirationTask, 1000);
	}

	/** Serialisation ID */
	private static final long serialVersionUID = 0L;
	/** This needs a new random generator */
	private static final Random rnd = new Random();

	/** Default expiration time for a ticket is two hours */
	private static long expirationTime = 1000 * 60 * 60 * 2;

	/** HTTP argument header for the ticket id */
	public static final String ticketHeader = "ticket";

	/**
	 * A ticket for downloading
	 * 
	 */
	public static class Ticket {
		private final long id;
		private final String code;
		private final long creationTime;

		private Ticket(long id, String code) {
			this.id = id;
			this.code = code;
			this.creationTime = System.currentTimeMillis();
		}

		@Override
		public int hashCode() {
			return Long.valueOf(id).hashCode();
		}

		/**
		 * @return the id of the ticket
		 */
		public long getTicketId() {
			return id;
		}

		/**
		 * @return the code of the ticket
		 */
		public String getCode() {
			return code;
		}

		private boolean isExpired() {
			return System.currentTimeMillis() > (creationTime + expirationTime);
		}

		public void delete() {
			synchronized (tickets) {
				if (tickets.containsKey(this.id))
					tickets.remove(this.id);
			}
		}
	}

	/** Container for all download tickets */
	private static final HashMap<Long, Ticket> tickets = new HashMap<Long, SourceDownloader.Ticket>();

	public static long createTicket(String code) {
		long id = code.hashCode();
		Ticket ticket;

		synchronized (tickets) {
			while (tickets.containsKey(id))
				id = id ^ rnd.nextLong();

			ticket = new Ticket(id, code);
			tickets.put(id, ticket);
		}

		// Ticket has been created
		return ticket.id;
	}

	/**
	 * Sets the expiration time for a download ticket in milliseconds
	 * 
	 * @param milliseconds
	 *            ms of the new expiration time. Must be larger than zero,
	 *            otherwise ignored
	 */
	public static void setExpirationTime(long milliseconds) {
		if (milliseconds < 0)
			return;
		expirationTime = milliseconds;
	}

	/**
	 * Sets the expiration time for a download ticket
	 * 
	 * @param seconds
	 *            Must be positive
	 * @param minutes
	 *            Must be positive
	 * @param hours
	 *            Must be positive
	 */
	public static void setExpirationTime(int seconds, int minutes, int hours) {
		setExpirationTime(1000 * seconds + 1000 * 60 * minutes + 1000 * 60 * 60
				* hours);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = null;
		Ticket ticket = null;

		try {
			writer = resp.getWriter();

			long ticketid = extractTicketId(req); // Throws
													// IllegalArgumentException
													// if invalid

			synchronized (tickets) {
				if (!tickets.containsKey(ticketid))
					throw new IllegalArgumentException("Ticket id not found");
				ticket = tickets.get(ticketid);
				if (ticket == null)
					throw new IllegalArgumentException("Ticket not found");
			}

			// Ticket found. Upload the source code
			writer.print(ticket.code);
			writer.flush();

		} catch (IOException e) {
			// Ignore it - Maybe the stream is closed by other side while
			// downloading
			writer = null;
			return;
		} catch (IllegalArgumentException e) {
			// Illegal ticket
			writer.println("<head>");
			writer.println("<title>org.smartsnip - Code downloader</title>");
			writer.println("</head>");

			writer.println("<body>");
			writer.println("<h1>The requested ticket is invalid</h1>");
			writer.println("<p>The returned error was: <b>" + e.getMessage()
					+ "</b></p>");
			writer.println("<hr>");
			writer.println("<p>Re-check the download link and try again.</p>");
			writer.println("<p>It's also possible that your ticket has expired</p>");
			writer.println("</body>");
			return;
		} catch (Exception e) {
			writer.println("<head>");
			writer.println("<title>org.smartsnip - Code downloader</title>");
			writer.println("</head>");

			writer.println("<body>");
			writer.println("<h1>500 - Server error</h1>");
			writer.println("<p>There was an unhandled exception on the server:</p>");
			writer.println("<p>" + e.getClass().getName() + ": <b>"
					+ e.getMessage() + "</b></p>");
			e.printStackTrace(writer);
			writer.println("</body>");
			resp.setStatus(500);
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}

		// Ticket downloaded successfully
		ticket.delete();
	}

	/**
	 * Extract the ticket id out of a request.
	 * 
	 * throws a {@link IllegalArgumentException} if the request is invalid
	 * 
	 * @param req
	 *            HTTP request
	 * @return ticket id
	 * @throws IllegalArgumentException
	 *             Thrown if illegal request
	 */
	private long extractTicketId(HttpServletRequest req)
			throws IllegalArgumentException {
		Object ticket = req.getAttribute(ticketHeader);
		if (ticket == null)
			throw new IllegalArgumentException("No ticket defined");
		try {
			return Long.parseLong(ticket.toString());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Ticket id is invalid", e);
		}
	}

	/**
	 * Gets a cookie from the HTTP servlet session.
	 * 
	 * If the name is null or enmpty, null is returned.
	 * 
	 * @param name
	 *            name of the cookie
	 * @return the stored cookie, or null if not existing. Returns also null, if
	 *         the given name is null or empty
	 */
	protected Cookie getCookie(String name, HttpServletRequest request) {
		if (name == null || name.isEmpty())
			return null;

		if (request == null) {
			System.err.println("getThreadLocalRequest() delivered NULL");
			return null;
		}

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name))
					return cookie;
			}
		}
		return null;
	}

	/**
	 * Gets the session for this servlet object
	 * 
	 * @return the session for this revlet object
	 */
	protected final Session getSession(HttpServletRequest request) {
		if (session != null)
			return session;

		synchronized (this) {
			/*
			 * Thread-Saftey: This call MUST be executed once again in the
			 * synchronized block!!
			 */
			if (session != null)
				return session;

			Cookie cookie = getCookie(ISession.cookie_Session_ID, request);

			if (cookie == null)
				return null;
			else {
				String sid = cookie.getValue();
				if (sid == null) {
					session = Session.getStaticGuestSession();
				} else {
					session = Session.getSession(sid);
				}
			}

			// Does a activity on the running session
			session.doActivity();
			return session;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		// Post occurs, if a user wants to upload a new source code

		PrintWriter writer = null;
		try {
			writer = resp.getWriter();

			Session session = getSession(req);
			User user = session.getUser();
			if (session == null || user == null)
				throw new NoAccessException();

			// Get the snippet associated to the download link
			Snippet snippet = null;
			{
				long snipID;
				String snippet_id = req.getParameter(SNIPPET_REQUEST);
				if (snippet_id == null || snippet_id.isEmpty())
					throw new IllegalArgumentException(
							"Snippet id not definied");
				try {
					snipID = Long.parseLong(snippet_id);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
							"Snippet ID not a valid number", e);
				}
				snippet = Snippet.getSnippet(snipID);
				if (snippet == null)
					throw new IllegalArgumentException("Snippet not found",
							new NotFoundException());

			}

			// Check if the current session is privileged to edit the snippet
			if (!session.getPolicy().canEditSnippet(session, snippet))
				throw new NoAccessException(
						"Not privileged to edit the snippet");

			String filename = getNewTempFile(snippet);
			File file = new File(filename);

			// Response header
			writer.println("<head>");
			writer.println("<title>Smartsnip.org - Snippet uploader</title>");
			writer.println("</head>");

			writer.println("<body>");
			writer.println("<p><b>Uploading file to snippet <i>"
					+ snippet.getName() + "</i> ... </b></p>");

			long receivedBytes = 0;
			try {
				if (file.exists())
					file.delete();

				OutputStream output = new BufferedOutputStream(
						new FileOutputStream(file, false));
				InputStream input = req.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;

				do {
					len = input.read(buffer);
					if (len <= 0)
						break;
					receivedBytes += len;
					if (receivedBytes > maxFileBytes)
						break;

					// Write
					output.write(buffer);
				} while (true);

				output.flush();
				input.close();
				output.close();

			} catch (IOException e) {
				// File system IOException

				if (receivedBytes > 0)
					writer.println("<p>An <b>IOExcepton</b> occured after "
							+ receivedBytes + " bytes of data</p>");

				// Handle as normal IOException
				throw e;
			}

			// Check if quota has exceeded
			if (receivedBytes > maxFileBytes) {
				writer.println("<p> Max. file quota (" + maxFileBytes
						+ " bytes) exceeded. Upload cancelled </p>");
			} else {
				// Export to user
				snippet.getCode().applySourceCode(file.getAbsolutePath());

				// Result
				writer.println("<p>File uploaded successfully</p>");
			}

			writer.println("</body>");
			file.delete();

		} catch (IOException e) {
			if (writer != null) {
				writer.println("<hr>");
				writer.println("<p> IOException: " + e.getMessage() + "</p>");
				writer.print("<p>");
				e.printStackTrace(writer);
				writer.println("</p>");
			}
		} catch (NoAccessException e) {
			// Access denied
			writer.println("Access denial");
			if (e.getMessage() != null)
				writer.println(e.getMessage());

		} catch (IllegalArgumentException e) {
			// An argument is not valid
			writer.println("Illegal argument: " + e.getMessage());
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	/**
	 * Creates a temp file for a given snippet
	 * 
	 * @param snippet
	 *            the temp file is created for
	 * @return the temp filename
	 */
	private String getNewTempFile(Snippet snippet) {
		String filename = "snippet_temp_" + snippet.getHashId();
		int index = 0;
		File file;
		do {
			file = new File(filename + (index++) + ".tmp");
		} while (file.exists());

		return file.getAbsolutePath();
	}
}
