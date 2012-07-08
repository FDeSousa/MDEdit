package de.bsd.mdedit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import com.viewpagerindicator.TitlePageIndicator;

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

	public MDEditPagerAdapter(Context context, LayoutInflater layoutInflater,
			ViewPager vPager, TitlePageIndicator tpIndicator, String initText) {
		super();
		this.context = context;

		this.vPager = vPager;
		this.vPager.setAdapter(this);
		this.tpIndicator = tpIndicator;
		tpIndicator.setViewPager(this.vPager);

		// TODO Add ability to define custom CSS
		View webLayout = layoutInflater.inflate(R.layout.markdown_view, null);
		WebView webView = (WebView) webLayout.findViewById(R.id.md_view);

		View editorLayout = layoutInflater.inflate(R.layout.editor_view, null);
		EditText editor = (EditText) editorLayout.findViewById(R.id.editor);
		// TODO Add ability to change background and text colours

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

	public void onSaveInstanceState(Bundle outState) {
		String text = txtEditor.getText();
		outState.putString("text", text);
		outState.putIntArray(TextEditorViewHandler.TEXT_SELECTION,
				this.txtEditor.getSelection());
	}

	public void onRestoreInstanceState(Bundle inState) {
		this.txtEditor.setText(inState.getString("text"));
		this.txtEditor.gainFocus();
		this.txtEditor.setSelection(inState
				.getIntArray(TextEditorViewHandler.TEXT_SELECTION));
	}

	public void setText(String string) {
		this.txtEditor.setText(string);
	}

	public String getText() {
		return this.txtEditor.getText();
	}
	
	public boolean isTextEmpty() {
		return this.txtEditor.isEmpty();
	}

	public String getHtml() {
		return this.mdView.getHtml(this.txtEditor.getText());
	}

}
