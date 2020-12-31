package com.packtpub.a3ws.samples.cmis.wiki.server;

import java.io.StringWriter;
import java.util.List;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.packtpub.a3ws.samples.cmis.wiki.client.CMISPage;
import com.packtpub.a3ws.samples.cmis.wiki.client.WikiService;
import com.packtpub.a3ws.samples.cmis.wiki.shared.FieldVerifier;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WikiServiceImpl extends RemoteServiceServlet implements WikiService {

	@Override
	public CMISPage loadPage(String name) throws IllegalArgumentException {
		CMISClient client = CMISClient.getInstance();
		String text = client.loadCMISDocument(name + ".txt");
		if (text != null) {
			CMISPage page = new CMISPage();
			page.setName(name);
			page.setWikiText(text);
			page.setHtmlText(formatWikiText(text));
			return page;
		}
		return null;
	}

	public CMISPage savePage(String name, String text) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(name)) {
			throw new IllegalArgumentException(
			"Name must be at least 3 characters long");
		}
		CMISClient client = CMISClient.getInstance();
		client.writeCMISDocument(name + ".txt", text);
		CMISPage page = new CMISPage();
		page.setName(name);
		page.setWikiText(text);
		page.setHtmlText(formatWikiText(text));
		return page;
	}

	public List<String> search(String query) throws IllegalArgumentException {
		CMISClient client = CMISClient.getInstance();
		return client.searchCMISDocuments(query);
	}

	private String formatWikiText(String text) {
		StringWriter writer = new StringWriter();
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
		// avoid the <html> and <body> tags 
		builder.setEmitAsDocument(false);

		MarkupParser markupParser = new MarkupParser(new TextileLanguage());
		markupParser.setBuilder(builder);
		markupParser.parse(text);
		return writer.toString();
	}
	
}
