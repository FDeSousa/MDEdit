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
			if (txtEditor == null) {
				txtEditor = new TextEditorFragment();
				Bundle txargs = new Bundle();
				txargs.putString("title", "Editor");
				txargs.putString("text", initText);
				txtEditor.setArguments(txargs);
			}
			return txtEditor;
		case 1:
			if (mdView == null) {
				mdView = new MarkdownViewFragment();
				Bundle mdargs = new Bundle();
				mdargs.putString("title", "Viewer");
				mdView.setArguments(mdargs);
			}
			return mdView;
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
				txtEditor.getSelection());
	}

	public void onRestoreInstanceState(Bundle inState) {
		if (inState != null)
			initText = inState.getString("text");
	}

	public void setText(String string) {
		initText = string;
		if (txtEditor != null)
			txtEditor.setText(string);
	}

	public String getText() {
		return txtEditor == null ? initText : txtEditor.getText();
	}

	public boolean isTextEmpty() {
		return txtEditor == null ? true : txtEditor.isEmpty();
	}

	public String getHtml() {
		return mdView.getHtml(txtEditor.getText());
	}

	public void onPageSelected(int position) {
		if (txtEditor != null && mdView != null)
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
