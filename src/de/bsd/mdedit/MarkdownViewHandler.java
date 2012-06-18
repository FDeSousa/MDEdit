/******************************************************************************
 * Copyright 2012 Filipe De Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
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
 * @author Fil
 * @version %I%, %G%
 * 
 */
public class MarkdownViewHandler {
	private WebView webView;
	private final Markdown markdown;

	public MarkdownViewHandler(WebView webView) {
		this.webView = webView;
		this.markdown = new Markdown();
	}

	public void update(String text) {
		Reader in = new StringReader(text);
		StringWriter out = new StringWriter();

		try {
			markdown.transform(in, out);
			String result = out.toString();
			webView.loadData(result, "text/html", "utf-8");
		} catch (ParseException e) {
			Log.w("MarkdownViewHandler.update()", e);
		}
	}

}
