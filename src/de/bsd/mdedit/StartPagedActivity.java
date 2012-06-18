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

import org.tautua.markdownpapers.Markdown;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * StartPagedActivity.java
 * 
 * @author Filipe De Sousa
 * @version %I%, %G%
 * 
 */
public class StartPagedActivity extends Activity {

	private ViewPager awesomePager;
	private static int NUM_AWESOME_VIEWS = 3;
	private AwesomePagerAdapter awesomeAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		awesomeAdapter = new AwesomePagerAdapter(this,
				this.getLayoutInflater(), NUM_AWESOME_VIEWS);
		awesomePager = (ViewPager) findViewById(R.id.markdown_pager);
		awesomePager.setAdapter(awesomeAdapter);

		// SavedInstanceState reading
		// get text from savedInstanceState
		if (savedInstanceState != null) {
			String text = savedInstanceState.getString("text");
			//editor.setText(text);
			//renderText(text);
		}
		// check if we were called to open a .md file
		if (savedInstanceState == null && getIntent() != null
				&& getIntent().getData() != null) {
			Uri uri = getIntent().getData();
			//String text = loadFromFile(uri.getPath(), true);
			//editor.setText(text);
			//renderText(text);
		}
	}

	private class AwesomePagerAdapter extends PagerAdapter {
		private Context context;
		private LayoutInflater layoutInflater;
		private final int numViews;

		private WebView webView;
		private EditText editor;
		
		private final Markdown markdown = new Markdown();
		private static final String FILE_NAME = "_tmP-save.md";

		/**
		 * 
		 */
		public AwesomePagerAdapter(Context context,
				LayoutInflater layoutInflater, int numViews) {
			super();
			this.context = context;
			this.layoutInflater = layoutInflater;
			this.numViews = numViews;
		}

		@Override
		public int getCount() {
			return this.numViews;
		}

		/**
		 * Create the page for the given position. The adapter is responsible
		 * for adding the view to the container given here, although it only
		 * must ensure this is done by the time it returns from
		 * {@link #finishUpdate()}.
		 * 
		 * @param container
		 *            The containing View in which the page will be shown.
		 * @param position
		 *            The page position to be instantiated.
		 * @return Returns an Object representing the new page. This does not
		 *         need to be a View, but can be some other container of the
		 *         page.
		 */
		@Override
		public Object instantiateItem(View collection, int position) {

			TextView tv = new TextView(context);
			tv.setText("Bonjour PAUG " + position);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(30);

			((ViewPager) collection).addView(tv, 0);

			return tv;
		}

		/**
		 * Remove a page for the given position. The adapter is responsible for
		 * removing the view from its container, although it only must ensure
		 * this is done by the time it returns from {@link #finishUpdate()}.
		 * 
		 * @param container
		 *            The containing View from which the page will be removed.
		 * @param position
		 *            The page position to be removed.
		 * @param object
		 *            The same object that was returned by
		 *            {@link #instantiateItem(View, int)}.
		 */
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((TextView) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((TextView) object);
		}

		/**
		 * Called when the change in the shown pages has been completed. At this
		 * point you must ensure that all of the pages have actually been added
		 * or removed from the container as appropriate.
		 * 
		 * @param container
		 *            The containing View which is displaying this adapter's
		 *            page views.
		 */
		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

}
