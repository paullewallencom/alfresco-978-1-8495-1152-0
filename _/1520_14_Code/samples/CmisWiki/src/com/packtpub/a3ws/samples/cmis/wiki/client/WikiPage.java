package com.packtpub.a3ws.samples.cmis.wiki.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

public class WikiPage extends Composite {
	
	private static final int VIEW_TAB   = 0;
	private static final int EDIT_TAB   = 1;
	private static final int SEARCH_TAB = 2;

	interface WikiPageUiBinder extends UiBinder<Widget, WikiPage> {}
	
	private static WikiPageUiBinder uiBinder = GWT.create(WikiPageUiBinder.class);
	
	@UiField SpanElement pageName;
	
	@UiField SpanElement pageText;
	
	@UiField TabLayoutPanel tabs;
	
	@UiField TextArea editor;
	
	@UiField Button saveButton;
	
	@UiField Button searchButton;
	
	@UiField TextBox searchBox;
	
	@UiField FlexTable searchResultsTable;
	
	@UiField FileUpload fileUpload;
	
	@UiField Button uploadButton;
	
	@UiField FormPanel uploadForm;

	/**
	 * Create a remote service proxy to talk to the server-side Wiki service.
	 */
	private final WikiServiceAsync wikiService = GWT.create(WikiService.class);
	
	public WikiPage() {
		initWidget(uiBinder.createAndBindUi(this));

		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				savePage();
			}
		});

		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tabs.selectTab(SEARCH_TAB);
				searchPages(searchBox.getText());
			}
		});

		fileUpload.setName("upload");
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);
		uploadForm.setAction(GWT.getModuleBaseURL() + "upload");
		uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String result = event.getResults();
				result = result.replace("<pre>", "!"+ GWT.getModuleBaseURL() + "download/");
				result = result.replace("</pre>", "!");
				result = result.replaceAll("[\\r\\n]", "");
				// Insert a link to the image at the current cursor position
				int pos = editor.getCursorPos();
				String before = editor.getText().substring(0, pos);
				String after = editor.getText().substring(pos);
				StringBuffer newText = new StringBuffer(before);
				newText.append(result);
				newText.append(after);
				editor.setText(newText.toString());
			}
		});

		uploadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String filename = fileUpload.getFilename();
				if (filename.length() > 0) {
					uploadForm.submit();
				}
			}
		});

	}
	
	public void setPageName(String name) {
		pageName.setInnerText(name);
		History.newItem(name);
		Window.setTitle("CMIS Wiki - " + name);
	}
	
	public void setPageText(String htmlText, String wikiText) {
		pageText.setInnerHTML(htmlText);
		editor.setText(wikiText);
	}

	public void loadPage(final String pageName) {
		setPageName(pageName);
		// Set up the callback object.
		AsyncCallback<CMISPage> callback = new AsyncCallback<CMISPage>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			public void onSuccess(CMISPage result) {
				if (result != null) {
					setPageName(result.getName());
					setPageText(result.getHtmlText(), result.getWikiText());
					tabs.selectTab(VIEW_TAB);
				} else {
					// Switch to edit tab for new pages
					setPageText("", "");
					tabs.selectTab(EDIT_TAB);
				}
			}
		};

		wikiService.loadPage(pageName, callback);
	}

	private void savePage() {
		AsyncCallback<CMISPage> callback = new AsyncCallback<CMISPage>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			public void onSuccess(CMISPage result) {
				tabs.selectTab(VIEW_TAB);
				setPageText(result.getHtmlText(), result.getWikiText());
			}
		};

		wikiService.savePage(pageName.getInnerText(), editor.getText(), callback);

	}

	private void searchPages(String query) {
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<String> results) {
				searchResultsTable.removeAllRows();
				for (int i = 0 ; i < results.size(); ++i) {
					String name = results.get(i);
					int pos = name.lastIndexOf(".txt");
					if (pos > 0) {
						name = name.substring(0, pos);
						searchResultsTable.setWidget(i, 0, new Hyperlink(name, name));
					}
				}
			}
		};
		
		wikiService.search(query, callback);
	}

}
