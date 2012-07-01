package de.bsd.mdedit;

import group.pals.android.lib.ui.filechooser.FileChooserActivity;
import group.pals.android.lib.ui.filechooser.io.LocalFile;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
	private static final int LOAD_FILE_RESULT_CODE = 27485;
	private static final int SAVE_FILE_RESULT_CODE = 11484;

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
			initText = fileHandler.loadFromFile(uri.getPath());
		}

		ViewPager vPager = (ViewPager) findViewById(R.id.markdown_pager);
		TitlePageIndicator tpIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mdpAdapter = new MDEditPagerAdapter(this, this.fileHandler,
				layoutInflater, vPager, tpIndicator, initText);
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
			Intent save = new Intent(this, FileChooserActivity.class)
					.putExtra(FileChooserActivity._Rootpath,
							(Parcelable) new LocalFile(FileHandler.SD_FOLDER))
					.putExtra(FileChooserActivity._SaveDialog, true)
					.putExtra(FileChooserActivity._DefaultFilename, "text.md")
					.putExtra(FileChooserActivity._DisplayHiddenFiles, false);
			startActivityForResult(save, SAVE_FILE_RESULT_CODE);
			break;
		case R.id.menu_load:
			// TODO Add loading of files
			Intent load = new Intent(this, FileChooserActivity.class)
					.putExtra(FileChooserActivity._Rootpath,
							(Parcelable) new LocalFile(FileHandler.SD_FOLDER))
					.putExtra(FileChooserActivity._DisplayHiddenFiles, false);
			startActivityForResult(load, SAVE_FILE_RESULT_CODE);
			break;
		case R.id.menu_export_html:
			// TODO Add exporting of HTML
			// String html = mdpAdapter.getHtml();
			break;
		case R.id.menu_send:
			// TODO Check out sending to Pocket
			String text_send = mdpAdapter.getText();
			String subject = getString(R.string.subject_markdown);
			Intent share = new Intent(Intent.ACTION_SEND).setType("text/plain")
					.putExtra(Intent.EXTRA_TEXT, text_send)
					.putExtra(Intent.EXTRA_SUBJECT, subject);
			startActivity(share);
			break;
		case R.id.menu_clear:
			mdpAdapter.setText("");
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			List<LocalFile> files = (List<LocalFile>) data.getSerializableExtra(FileChooserActivity._Results);
			switch (requestCode) {
			case LOAD_FILE_RESULT_CODE:
				for (File f : files) {
					String text = fileHandler.loadFromFile(f);
					mdpAdapter.setText(text);
				}
				break;
			case SAVE_FILE_RESULT_CODE:
				for (File f : files) {
					fileHandler.saveToFile(f, mdpAdapter.getText());
				}
				break;
			}
		}
	}
}