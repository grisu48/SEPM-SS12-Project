package org.smartsnip.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.smartsnip.client.UploadCode;
import org.smartsnip.core.Code;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;

/**
 * This servlet accepts uploaded files from {@link UploadCode} and puts them
 * into the code object, that is given with the argument "codeID"
 * 
 * The servlet listens on "upload" and is defined in the file web.xml
 * 
 * @author Felix Niederwanger.
 * 
 */
public class Uploader extends SessionServlet {

	/** Serialisation ID */
	private static final long serialVersionUID = 6158787027931799617L;

	private final static String UPLOAD_DIRECTORY = System
			.getProperty("java.io.tmpdir");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PrintWriter writer = new PrintWriter(new OutputStream() {

			@Override
			public void write(int arg0) throws IOException {
			}
		});

		try {
			writer = resp.getWriter();

			writer.println("<h1>Code Uploader</h1>");
			Session session = getSession(req);
			if (!session.isLoggedIn()) {
				writer.println("<p> This is the source uploader servlet. You must log in, to get access to this service </p>");
				writer.println("<p> Return to the <a href=\"index.html\">main page</a> and log in. </p>");
				writer.println("<hr>");
				writer.println("<p>This servlet should just be accessed by a POST request</p>");
				return;
			}

		} catch (IOException e) {
			e.printStackTrace(writer);
		} finally {
			writer.flush();
			writer.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Code ID if set
		final long codeID;
		final Session session;
		final Code code;
		boolean fileReceived = false;
		try {

			String codeIDParam = req.getParameter("codeID");
			String sessionCookie = req.getParameter("session");
			if (codeIDParam == null || codeIDParam.isEmpty())
				throw new RuntimeException("codeID not set");
			if (sessionCookie == null || sessionCookie.isEmpty())
				throw new RuntimeException("Session ID not set");

			session = Session.getSession(sessionCookie, false);

			// TODO Uncomment needed lines for access denial

			// Get Code object and check if it existing
			codeID = Long.parseLong(codeIDParam);
			// code = Code.getCode(codeID);
			code = null;
			// if (code == null)
			// throw new NotFoundException("Code object with id " + codeID +
			// " cannot be found.");

			// Check session access policy
			if (session == null)
				throw new NoAccessException("No session associated");
			// if (!session.isLoggedIn()
			// || !session.getPolicy().canEditSnippet(session,
			// code.snippet))
			// throw new NoAccessException("Session policy denies the access");

		} catch (NotFoundException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("Object not found: " + e.getMessage());
			return;
		} catch (NoAccessException e) {
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
			resp.getWriter().print("Access denied: " + e.getMessage());
			return;
		} catch (RuntimeException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("Error: " + e.getMessage());
			return;
		}

		// process only multipart requests (Must be set on client!!)
		if (ServletFileUpload.isMultipartContent(req)) {

			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			try {
				@SuppressWarnings("unchecked")
				List<FileItem> items = upload.parseRequest(req);

				for (FileItem item : items) {
					req.getAttribute("codeID");
					// process only file upload - discard other form item types
					if (item.isFormField())
						continue;

					String fileName = item.getName();
					// get only the file name not whole path
					if (fileName != null) {
						fileName = FilenameUtils.getName(fileName);
					}

					File uploadedFile = new File(UPLOAD_DIRECTORY, fileName);
					if (uploadedFile.exists())
						uploadedFile.delete(); // We are in the temp directory!

					if (uploadedFile.createNewFile()) {
						item.write(uploadedFile);
						resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
						resp.getWriter().print("File successfully received");
						resp.flushBuffer();
						// Done, the file has been downloaded, now apply the
						// thing
						Code.writeCodeFile(codeID,
								uploadedFile.getAbsolutePath());
						File deleteFile = new File(
								uploadedFile.getAbsolutePath());
						if (!deleteFile.delete()) {
							deleteFile.deleteOnExit();
							System.err
									.println("Code for code obj received, but file cannot be deleted!");
						} else {
							System.out.println("Code for code object " + codeID
									+ " received.");
						}

						resp.getWriter().print("File successfully received");
						fileReceived = true;
						break; // Do NOT accept more files

					} else
						throw new IOException(
								"The file already exists in repository.");
				}

				// OK, if the request has been handled
				if (fileReceived) {
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.getWriter().print("Upload transaction completed");
				} else {
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					resp.getWriter().print(
							"No file received for setting source code");
					return;
				}
			} catch (Exception e) {
				resp.getWriter().print("Error: " + e.getMessage());

				resp.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"An error occurred while loading the file : "
								+ e.getMessage());
			}

		} else {
			resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
					"Request contents type is not supported by the uploader servlet.");
		}
	}

	/*
	 * @Override protected void doPost(HttpServletRequest request,
	 * HttpServletResponse response) { ServletFileUpload upload = new
	 * ServletFileUpload();
	 * 
	 * try { FileItemIterator iter = upload.getItemIterator(request);
	 * 
	 * while (iter.hasNext()) { FileItemStream item = iter.next();
	 * 
	 * String name = item.getFieldName(); InputStream stream =
	 * item.openStream();
	 * 
	 * // Process the input stream OutputStream out = new FileOutputStream(
	 * "/home/phoenix/temp/temp.tmp", false); int len; byte[] buffer = new
	 * byte[8192]; long bytesReceived = 0; int maxFileSize = 10 * (1024 * 1024);
	 * // 10 megs max
	 * 
	 * long time = System.currentTimeMillis(); while ((len = stream.read(buffer,
	 * 0, buffer.length)) != -1) { out.write(buffer, 0, len); bytesReceived +=
	 * len; if (bytesReceived > maxFileSize) { stream.close(); out.close();
	 * System.err.println("Max file size exceeded"); } } time =
	 * System.currentTimeMillis() - time; double speed = (double)
	 * (bytesReceived) / (double) time;
	 * 
	 * System.out.println(bytesReceived + " bytes received in " + time +
	 * "ms (~ " + speed + "kBytes/s)"); } } catch (Exception e) { throw new
	 * RuntimeException(e); }
	 * 
	 * }
	 */

	private void fetchFile(InputStream input, OutputStream output)
			throws IOException {
		try {
			byte[] buffer = new byte[2048];
			int len = 0;

			do {
				len = input.read(buffer);
				if (len <= 0)
					break;

				output.write(buffer, 0, len);
			} while (true);

			// done
		} finally {
			input.close();
			output.close();
		}
	}
}
