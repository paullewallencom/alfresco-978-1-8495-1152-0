package com.packtpub.a3ws.samples.cmis.wiki.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload
				.isMultipartContent(new ServletRequestContext(req));
		if (isMultipart) {
			FileItem uploadItem = getFileItem(req);
			if (uploadItem != null) {
				CMISClient client = CMISClient.getInstance();
				client.writeImage(uploadItem.get(), uploadItem.getContentType(), uploadItem.getName());
				resp.setContentType("text/plain");
				PrintWriter out = resp.getWriter();
				out.println("images/" + uploadItem.getName());
				return;
			}
		}
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	@SuppressWarnings("unchecked")
	private FileItem getFileItem(HttpServletRequest request) {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(10000000);

		try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {
				if (!item.isFormField() && "upload".equals(item.getFieldName())) {
					return item;
				}
			}
		} catch (FileUploadException e) {
			return null;
		}
		return null;
	}
}
