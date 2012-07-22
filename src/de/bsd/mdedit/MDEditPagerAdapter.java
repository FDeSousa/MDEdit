package de.bsd.mdedit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * MarkdownPagerAdapter.java
 * 
 * @author Filipe De Sousa
 * @version %I%, %G%
 * 
 */
public class MDEditPagerAdapter extends FragmentPagerAdapter {
	private static final int NUMBER_OF_VIEWS = 2;

	// private Fragment[] fragments;
	private TextEditorFragment txtEditor;
	private MarkdownViewFragment mdView;

	private String initText;

	public MDEditPagerAdapter(FragmentManager fm, String initText) {
		super(fm);
		this.initText = initText;
	}

	@Override
	public int getCount() {
		return MDEditPagerAdapter.NUMBER_OF_VIEWS;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return getItem(position).getArguments().getString("title");
	}

	@Override
	public Fragment getItem(int position) {
		// return this.fragments[position];
		switch (position) {
		case 0:
			if (this.txtEditor == null) {
				this.txtEditor = new TextEditorFragment();
				Bundle txargs = new Bundle();
				txargs.putString("title", "Editor");
				txargs.putString("text", this.initText);
				this.txtEditor.setArguments(txargs);
			}
			return this.txtEditor;
		case 1:
			if (this.mdView == null) {
				this.mdView = new MarkdownViewFragment();
				Bundle mdargs = new Bundle();
				mdargs.putString("title", "Viewer");
				this.mdView.setArguments(mdargs);
			}
			return this.mdView;
		}
		return null;
	}

	public void onResume() {
		// Currently just a noop
	}

	public void onSaveInstanceState(Bundle outState) {
		String text = txtEditor.getText();
		outState.putString("text", text);
		outState.putIntArray(TextEditorFragment.TEXT_SELECTION,
				this.txtEditor.getSelection());
	}

	public void onRestoreInstanceState(Bundle inState) {
		if (inState != null)
			this.initText = inState.getString("text");
	}

	public void setText(String string) {
		this.initText = string;
		if (this.txtEditor != null)
			this.txtEditor.setText(string);
	}

	public String getText() {
		return this.txtEditor == null ? this.initText : this.txtEditor.getText();
	}

	public boolean isTextEmpty() {
		return this.txtEditor == null ? true : this.txtEditor.isEmpty();
	}

	public String getHtml() {
		return this.mdView.getHtml(this.txtEditor.getText());
	}

	public void onPageSelected(int position) {
		if (txtEditor != null && mdView != null) {
			switch (position) {
			case 0:
				txtEditor.gainFocus();
				break;
			case 1:
				String text = getText();
				if (text != null)
					mdView.update(text);
				txtEditor.loseFocus();
				break;
			}
		}
	}

}
