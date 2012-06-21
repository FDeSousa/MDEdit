package de.bsd.mdedit;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import android.util.Log;
import android.webkit.WebView;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * MarkdownViewHandler.java
 * 
 * @author Filipe De Sousa
 * @version %I%, %G%
 * 
 */
public class MarkdownViewHandler {
	private static final String CSS_BASE_URL = "file:///android_asset/";
	private static final String CSS_STRING = "<link rel='stylesheet' type='text/css' href='style.css' />";
	private static final String MIME_TYPE = "text/html";
	private static final String ENCODING = "UTF-8";

	private WebView webView;
	private final Markdown markdown;
	private String cssBaseUrl;

	public MarkdownViewHandler(WebView webView) {
		this(webView, null);
	}

	public MarkdownViewHandler(WebView webView, String cssBaseUrl) {
		this.webView = webView;
		this.markdown = new Markdown();
		this.cssBaseUrl = cssBaseUrl;
	}
	
	public void setCssBaseUrl(String cssBaseUrl) {
		this.cssBaseUrl = cssBaseUrl;
	}

	public String update(String text) {
		Reader in = new StringReader(text);
		StringWriter out = new StringWriter();

		try {
			markdown.transform(in, out);
			// Load the HTML with chosen CSS into WebView
			String result = CSS_STRING + out.toString();
			String baseUrl = CSS_BASE_URL;
			if (this.cssBaseUrl != null)
				baseUrl = this.cssBaseUrl;
			webView.loadDataWithBaseURL(baseUrl, result, MIME_TYPE, ENCODING, null);
		} catch (ParseException e) {
			Log.e("MarkdownViewHandler.update()", "ParseException when updating MarkdownViewHandler", e);
		}
		
		return out.toString();
	}

	public String getHtml(String text) {
		return update(text);
	}

}
