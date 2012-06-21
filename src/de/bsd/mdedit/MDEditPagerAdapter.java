package de.bsd.mdedit;

import com.viewpagerindicator.TitlePageIndicator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Scroller;

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
public class MDEditPagerAdapter extends PagerAdapter {

	private static final int NUMBER_OF_VIEWS = 2;

	private final Context context;
	private String[] titles;
	private View[] views;

	private TextEditorViewHandler txtEditor;
	private MarkdownViewHandler mdView;

	private ViewPager vPager;
	private TitlePageIndicator tpIndicator;

	private FileHandler fileHandler;

	public MDEditPagerAdapter(Context context, FileHandler fileHandler,
			ViewPager vPager, TitlePageIndicator tpIndicator, String initText) {
		super();
		this.context = context;

		this.fileHandler = fileHandler;

		this.vPager = vPager;
		this.vPager.setAdapter(this);
		this.tpIndicator = tpIndicator;
		tpIndicator.setViewPager(this.vPager);

		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		WebView webView = new WebView(this.context);
		webView.setLayoutParams(lParams);
		EditText editor = new EditText(this.context);
		editor.setLayoutParams(lParams);
		// TODO Add ability to change background and text colours
		editor.setBackgroundResource(android.R.color.darker_gray);
		editor.setTextAppearance(this.context,
				android.R.color.tertiary_text_light);
		// Set the text gravity to the top
		editor.setGravity(Gravity.TOP);
		// Text, with capitalized sentences and multiple line input.
		editor.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
				| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		// TODO Should setup scroll bar, but still doesn't appear
		editor.setScroller(new Scroller(this.context));
		editor.setVerticalFadingEdgeEnabled(true);
		editor.setVerticalScrollBarEnabled(true);

		this.mdView = new MarkdownViewHandler(webView);
		this.txtEditor = new TextEditorViewHandler(editor, mdView, initText,
				(InputMethodManager) this.context
						.getSystemService(Context.INPUT_METHOD_SERVICE));

		this.titles = new String[] { "Editor", "Viewer" };
		this.views = new View[] { editor, webView };
	}

	@Override
	public int getCount() {
		return MDEditPagerAdapter.NUMBER_OF_VIEWS;
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		View v = this.views[position];
		((ViewPager) collection).addView(v, position);
		return v;
	}

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

	public void onResume() {
		if (txtEditor.getText().isEmpty()) {
			String text = fileHandler.loadFromFile(FileHandler.TEMP_FILE_NAME,
					false);
			if (text != null) {
				txtEditor.setText(text);
			}
		}

		// Only set the listener now so webview has had a chance to be instantiated
		tpIndicator.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					// Tell text editor it's gaining focus
					txtEditor.gainFocus();
				} else {
					// Tell text editor it's losing focus
					txtEditor.update();
					txtEditor.loseFocus();
				}
				super.onPageSelected(position);
			}
		});
	}

	public void onPause() {
		String text = txtEditor.getText();
		fileHandler.saveToFile(FileHandler.TEMP_FILE_NAME, text);
	}

	public void onSaveInstanceState(Bundle outState) {
		String text = txtEditor.getText();
		outState.putString("text", text);
		outState.putIntArray(TextEditorViewHandler.TEXT_SELECTION,
				this.txtEditor.getSelection());
	}

	public void onRestoreInstanceState(Bundle inState) {
		this.txtEditor.setText(inState.getString("text"));
		this.txtEditor.gainFocus();
		this.txtEditor.setSelection(inState.getIntArray(TextEditorViewHandler.TEXT_SELECTION));
	}

	public void setText(String string) {
		this.txtEditor.setText(string);
	}

	public String getText() {
		return this.txtEditor.getText();
	}

	public String getHtml() {
		return this.mdView.getHtml(this.txtEditor.getText());
	}

}
