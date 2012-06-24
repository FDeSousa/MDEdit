package de.bsd.mdedit;

import android.view.View;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * PageView.java
 * 
 * @author Filipe De Sousa
 * @version %I%, %G%
 * 
 */
public class PageView {
	private String title;
	private int number;
	private View view;

	public PageView(String title, int number, View view) {
		this.title = title;
		this.number = number;
		this.view = view;
	}

	public String getTitle() {
		return title;
	}

	public int getNumber() {
		return number;
	}

	public View getView() {
		return view;
	}

}
