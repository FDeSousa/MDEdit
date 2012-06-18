package de.bsd.mdedit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;

public class StartActivity extends Activity {

	private static final String FILE_NAME;
	private static final int NUM_AWESOME_VIEWS;

	static {
		FILE_NAME = "_tmP-save.md";
		NUM_AWESOME_VIEWS = 2;
	}

	private TextEditorHandler txtEditor;
	private MarkdownViewHandler mdView;

	private String initText;

	private ViewPager vPager;
	private MarkdownPagerAdapter mdpAdapter;
	private TitlePageIndicator tpIndicator;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Always set the initText to something
		initText = "";

		if (savedInstanceState != null) {
			// Get text from savedInstanceState
			initText = savedInstanceState.getString("text");
		} else if (savedInstanceState == null && getIntent() != null
				&& getIntent().getData() != null) {
			// Called to open a .md file
			Uri uri = getIntent().getData();
			initText = loadFromFile(uri.getPath(), true);
		}

		mdpAdapter = new MarkdownPagerAdapter(NUM_AWESOME_VIEWS);
		vPager = (ViewPager) findViewById(R.id.markdown_pager);
		vPager.setAdapter(mdpAdapter);
		// TODO Setup the names for titles for TitlePageIndicator
		tpIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		tpIndicator.setViewPager(vPager);
	}

	@Override
	public void onResume() {
		super.onResume();

		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		WebView webView = new WebView(this);
		webView.setLayoutParams(lParams);

		EditText editor = new EditText(this);
		editor.setLayoutParams(lParams);
		// TODO Add ability to change background and text colours. Maybe from
		// presets?
		editor.setBackgroundResource(android.R.color.darker_gray);
		editor.setTextAppearance(this, android.R.color.tertiary_text_light);
		// Set the text gravity to the top
		editor.setGravity(Gravity.TOP);
		// TODO Should setup scroll bar, but still doesn't appear
		editor.setScroller(new Scroller(this));
		editor.setVerticalFadingEdgeEnabled(true);
		editor.setVerticalScrollBarEnabled(true);

		this.mdView = new MarkdownViewHandler(webView);
		this.txtEditor = new TextEditorHandler(editor, mdView, initText);
		View[] views = { editor, webView };
		String[] titles = { "Editor", "Viewer" };
		this.mdpAdapter.setViews(titles, views);

		if (txtEditor.getText().isEmpty()) {
			String text = loadFromFile(StartActivity.FILE_NAME, false);
			if (text != null) {
				txtEditor.setText(text);
			}
		}

		// Only set the listener now so webview has had a chance to be instantiated
		tpIndicator.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// Update if next view to be displayed will be the webview
				if (position == 1)
					txtEditor.update();
				super.onPageSelected(position);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		String text = txtEditor.getText();
		saveToFile(StartActivity.FILE_NAME, text);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		String text = txtEditor.getText();
		outState.putString("text", text);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_save:
			break;
		case R.id.menu_load:
			break;
		case R.id.menu_export_html:
			break;
		case R.id.menu_load_from_url:
			break;
		case R.id.menu_send:
			String text = txtEditor.getText();
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_TEXT, text);
			String subject = getString(R.string.subject_markdown);
			i.putExtra(Intent.EXTRA_SUBJECT, subject);
			startActivity(i);
			break;
		case R.id.menu_clear:
			txtEditor.setText("");
			break;
		}

		return true;
	}

	private void saveToFile(String fileName, String text) {
		File file = new File(getExternalFilesDir(null), fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(text.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			Toast.makeText(this, "Save failed: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	private String loadFromFile(String fileName, boolean absolutePath) {
		File file;
		if (!absolutePath)
			file = new File(getExternalFilesDir(null), fileName);
		else
			file = new File(fileName);
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			String text = new String(buffer);
			fis.close();
			return text;
		} catch (IOException e) {
			// Hacky way of not showing toast for failed loading of temp file
			if (!fileName.equals(FILE_NAME))
				Toast.makeText(this, "Load failed: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
		}
		return null;
	}

}
