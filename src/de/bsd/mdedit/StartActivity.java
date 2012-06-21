package de.bsd.mdedit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.viewpagerindicator.TitlePageIndicator;

/**
 * 
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * StartActivity.java
 * 
 * @author Filipe De Sousa
 * @version %I%, %G%
 *
 */
public class StartActivity extends Activity {

	private MDEditPagerAdapter mdpAdapter;

	private FileHandler fileHandler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		this.fileHandler = new FileHandler(this);
		String initText = "";

		if (savedInstanceState != null) {
			// Get text from savedInstanceState
			initText = savedInstanceState.getString("text");
		} else if (savedInstanceState == null && getIntent() != null
				&& getIntent().getData() != null) {
			// Called to open a .md file
			Uri uri = getIntent().getData();
			initText = fileHandler.loadFromFile(uri.getPath(), true);
		}

		ViewPager vPager = (ViewPager) findViewById(R.id.markdown_pager);
		TitlePageIndicator tpIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		mdpAdapter = new MDEditPagerAdapter(this, this.fileHandler, vPager, tpIndicator, initText);
	}

	@Override
	public void onResume() {
		super.onResume();
		mdpAdapter.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mdpAdapter.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mdpAdapter.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
		mdpAdapter.onRestoreInstanceState(inState);
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
			// TODO Add saving of files
			// String text_save = mdpAdapter.getText();
			break;
		case R.id.menu_load:
			// TODO Add loading of files
			break;
		case R.id.menu_export_html:
			// TODO Add exporting of HTML
			// String html = mdpAdapter.getHtml();
			break;
		case R.id.menu_load_from_url:
			// TODO Add loading of files from URL
			break;
		case R.id.menu_send:
			String text_send = mdpAdapter.getText();
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_TEXT, text_send);
			String subject = getString(R.string.subject_markdown);
			i.putExtra(Intent.EXTRA_SUBJECT, subject);
			startActivity(i);
			break;
		case R.id.menu_clear:
			mdpAdapter.setText("");
			break;
		}
		return true;
	}

}
