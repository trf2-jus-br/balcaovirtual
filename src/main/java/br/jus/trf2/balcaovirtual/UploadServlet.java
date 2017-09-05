package br.jus.trf2.balcaovirtual;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.crivano.swaggerservlet.SwaggerAuthorizationException;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

	private boolean isMultipart;
	private int maxFileSize = 4 * 1024 * 1024; // 4MB
	private int maxMemSize = 4 * 1024;
	private File file;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		String dirFinal = Utils.getDirFinal();
		String dirTemp = Utils.getDirTemp();

		try {
			SessionsCreatePost.verify(request.getHeader("Authorization"));
		} catch (SwaggerAuthorizationException e) {
			throw new ServletException("Falha de autenticação", e);
		}
		// Check that we have a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter();
		if (!isMultipart) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<p>No file uploaded</p>");
			out.println("</body>");
			out.println("</html>");
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File(dirTemp));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			@SuppressWarnings("rawtypes")
			List fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			@SuppressWarnings("rawtypes")
			Iterator i = fileItems.iterator();

			JSONObject o = new JSONObject();
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fileName = fi.getName();
					long sizeInBytes = fi.getSize();
					String fileId = UUID.randomUUID().toString();
					// Write the file
					file = new File(dirFinal + "/" + fileId + ".pdf");
					fi.write(file);
					o.put("name", fileName);
					o.put("size", sizeInBytes);
					o.put("id", fileId);
				}
			}

			corsHeaders(response);

			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(o.toString(3));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		throw new ServletException("GET method used with " + getClass().getName() + ": POST method required.");
	}

	@Override
	public void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		corsHeaders(response);
		response.setStatus(200);
		response.getWriter().write("OK");
		response.getWriter().close();
	}

	public static void corsHeaders(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,Cache-Control,X-Requested-With");
	}
}