package org.smartsnip.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SourceDownloader extends HttpServlet {

	/** Timer task for checking if tickets are expired */
	private static final TimerTask expirationTask;
	/** Timer for checking if tickets are expired */
	private static final Timer expirationTimer;

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
}
