package com.packtpub.a3ws.samples.cmis.wiki.client;

import java.io.Serializable;

public class CMISPage implements Serializable {

	private static final long serialVersionUID = 1598282058153008369L;

	private String name;
	
	private String wikiText;
	
	private String htmlText;
	
	public CMISPage() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWikiText() {
		return wikiText;
	}

	public void setWikiText(String wikiText) {
		this.wikiText = wikiText;
	}

	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}
}
