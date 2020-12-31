package com.packtpub.a3ws.samples.cmis.wiki.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CmisWiki implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final WikiPage wikiPage = new WikiPage();

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				if (historyToken != null) {
					wikiPage.loadPage(historyToken);
				}
			}
		});

		RootLayoutPanel.get().add(wikiPage);
		History.newItem("Main");
		
	}
}
