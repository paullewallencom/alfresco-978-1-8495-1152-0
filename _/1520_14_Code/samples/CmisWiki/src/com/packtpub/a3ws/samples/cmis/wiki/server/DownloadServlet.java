package com.packtpub.a3ws.samples.cmis.wiki.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.api.ContentStream;

@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = req.getPathInfo();
		if (path == null) {
			path = "/";
		}
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		streamCMISDocument(path, resp);
	}
	
	private void streamCMISDocument(String path, HttpServletResponse resp) {
		CMISClient client = CMISClient.getInstance();
		int pos = path.lastIndexOf('/');
		Folder folder = client.getWikiSubFolder(path.substring(0, pos));
		if (folder == null) {
			throw new RuntimeException("Folder " + path + " missing. Create it under Company Home.");
		}
		Document image = client.getChild(folder, path.substring(pos + 1));
		if (image == null) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		try {
			ContentStream cs = image.getContentStream();
			resp.setContentType(cs.getMimeType());
			resp.setContentLength((int) cs.getLength()); 
			OutputStream out = resp.getOutputStream();
			InputStream in = cs.getStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while ((n = in.read(buf)) > 0) {
				out.write(buf, 0, n);
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

}
