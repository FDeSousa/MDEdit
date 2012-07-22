package de.bsd.mdedit;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * MarkdownViewFragment.java
 * 
 * @author Fil
 * @version %I%, %G%
 * 
 */
public class MarkdownViewFragment extends Fragment {
	// TODO Add ability to define custom CSS
	private static final String CSS_BASE_URL = "file:///android_asset/";
	private static final String CSS_STRING = "<link rel='stylesheet' type='text/css' href='style.css' />";
	private static final String MIME_TYPE = "text/html";
	private static final String ENCODING = "UTF-8";

	private WebView webView;
	private Markdown markdown;
	private String cssBaseUrl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.markdown_view, container, false);
		this.webView = (WebView) view.findViewById(R.id.md_view);
		this.markdown = new Markdown();
		return view;
	}

	public void setCssBaseUrl(String cssBaseUrl) {
		this.cssBaseUrl = cssBaseUrl;
	}

	public void update(String text) {
		String markup = transform(text);
		// Load the HTML with chosen CSS into WebView
		String result = CSS_STRING + markup;
		String baseUrl = CSS_BASE_URL;
		if (this.cssBaseUrl != null)
			baseUrl = this.cssBaseUrl;
		webView.loadDataWithBaseURL(baseUrl, result, MIME_TYPE, ENCODING, null);
	}

	private String transform(String text) {
		Reader in = new StringReader(text);
		StringWriter out = new StringWriter();

		try {
			markdown.transform(in, out);
		} catch (ParseException e) {
			Log.e("MarkdownViewHandler.transform()",
					"ParseException when updating MarkdownViewHandler", e);
		}

		return out.toString();
	}

	public String getHtml(String text) {
		return transform(text);
	}

}
