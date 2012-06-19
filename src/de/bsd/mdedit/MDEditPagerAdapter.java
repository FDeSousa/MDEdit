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

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * MarkdownPagerAdapter.java
 * 
 * @author Fil
 * @version %I%, %G%
 * 
 */
public class MDEditPagerAdapter extends PagerAdapter {

	private final int numViews;
	private String[] titles;
	private View[] views;

	public MDEditPagerAdapter(int numViews) {
		super();
		this.numViews = numViews;
		this.titles = new String[this.numViews];
		this.views = new View[this.numViews];
	}

	@Override
	public int getCount() {
		return this.numViews;
	}

	/**
	 * Create the page for the given position. The adapter is responsible for
	 * adding the view to the container given here, although it only must ensure
	 * this is done by the time it returns from {@link #finishUpdate()}.
	 * 
	 * @param container
	 *            The containing View in which the page will be shown.
	 * @param position
	 *            The page position to be instantiated.
	 * @return Returns an Object representing the new page. This does not need
	 *         to be a View, but can be some other container of the page.
	 */
	@Override
	public Object instantiateItem(View collection, int position) {
		View v = this.views[position];
		((ViewPager) collection).addView(v, position);
		return v;
	}

	/**
	 * Remove a page for the given position. The adapter is responsible for
	 * removing the view from its container, although it only must ensure this
	 * is done by the time it returns from {@link #finishUpdate()}.
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
		((ViewPager) collection).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return this.titles[position];
	}

	public String[] getTitles() {
		return this.titles;
	}

	public View[] getViews() {
		return this.views;
	}

	public void setViews(View[] views) {
		this.views = views;
	}

	public void setViews(String[] titles, View[] views) {
		this.titles = titles;
		this.views = views;
	}

}
