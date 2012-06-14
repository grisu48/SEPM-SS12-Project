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

public class Uploader extends SessionServlet {

	/** Serialisation ID */
	private static final long serialVersionUID = 6158787027931799617L;

	private final static String UPLOAD_DIRECTORY = System
			.getProperty("user.home");

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

		// process only multipart requests
		if (ServletFileUpload.isMultipartContent(req)) {

			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			try {
				@SuppressWarnings("unchecked")
				List<FileItem> items = upload.parseRequest(req);
				for (FileItem item : items) {
					// process only file upload - discard other form item types
					if (item.isFormField())
						continue;

					String fileName = item.getName();
					// get only the file name not whole path
					if (fileName != null) {
						fileName = FilenameUtils.getName(fileName);
					}

					File uploadedFile = new File(UPLOAD_DIRECTORY, fileName);
					if (uploadedFile.createNewFile()) {
						item.write(uploadedFile);
						resp.setStatus(HttpServletResponse.SC_CREATED);
						resp.getWriter().print(
								"The file was created successfully.");
						resp.flushBuffer();
						System.out
								.println("File \"" + fileName + "\" received");
					} else
						throw new IOException(
								"The file already exists in repository.");
				}
			} catch (Exception e) {
				resp.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"An error occurred while creating the file : "
								+ e.getMessage());
			}

		} else {
			resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
					"Request contents type is not supported by the servlet.");
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