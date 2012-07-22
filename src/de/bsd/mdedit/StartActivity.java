package de.bsd.mdedit;

import group.pals.android.lib.ui.filechooser.FileChooserActivity;
import group.pals.android.lib.ui.filechooser.io.LocalFile;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
public class StartActivity extends FragmentActivity {
	private static final int LOAD_FILE_RESULT_CODE = 27485;
	private static final int SAVE_FILE_RESULT_CODE = 11484;
	private static final int HTML_FILE_RESULT_CODE = 15359;

	private MDEditPagerAdapter mdpAdapter;
	private ViewPager mViewPager;

	private FileHandler fileHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.fileHandler = new FileHandler(this);
		String initText = "";

		if (savedInstanceState != null) {
			// Get text from savedInstanceState
			if ((initText = savedInstanceState.getString("text")) == null)
				initText = "";
		}

		mdpAdapter = new MDEditPagerAdapter(getSupportFragmentManager(), initText);
		mViewPager = (ViewPager) findViewById(R.id.markdown_pager);
		mViewPager.setAdapter(mdpAdapter);
		mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mdpAdapter.onPageSelected(position);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		mdpAdapter.onResume();
		this.readIntent();
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
			Intent save = new Intent(this, FileChooserActivity.class)
					.putExtra(FileChooserActivity._Rootpath,
							(Parcelable) new LocalFile(FileHandler.SD_FOLDER))
					.putExtra(FileChooserActivity._SaveDialog, true)
					.putExtra(FileChooserActivity._DefaultFilename,
							"markdown.md")
					.putExtra(FileChooserActivity._DisplayHiddenFiles, false);
			startActivityForResult(save, SAVE_FILE_RESULT_CODE);
			break;
		case R.id.menu_load:
			Intent load = new Intent(this, FileChooserActivity.class).putExtra(
					FileChooserActivity._Rootpath,
					(Parcelable) new LocalFile(FileHandler.SD_FOLDER))
					.putExtra(FileChooserActivity._DisplayHiddenFiles, false);
			startActivityForResult(load, LOAD_FILE_RESULT_CODE);
			break;
		case R.id.menu_export_html:
			Intent export = new Intent(this, FileChooserActivity.class)
					.putExtra(FileChooserActivity._Rootpath,
							(Parcelable) new LocalFile(FileHandler.SD_FOLDER))
					.putExtra(FileChooserActivity._SaveDialog, true)
					.putExtra(FileChooserActivity._DefaultFilename,
							"markup.html")
					.putExtra(FileChooserActivity._DisplayHiddenFiles, false);
			startActivityForResult(export, HTML_FILE_RESULT_CODE);
			break;
		case R.id.menu_send:
			// TODO Check out sending to Pocket - fails stating data unreadable
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
			@SuppressWarnings("unchecked")
			List<LocalFile> files = (List<LocalFile>) data
					.getSerializableExtra(FileChooserActivity._Results);
			switch (requestCode) {
			case LOAD_FILE_RESULT_CODE:
				for (File f : files) {
					String text = fileHandler.loadFromFile(f);
					mdpAdapter.setText(text);
				}
				break;
			case SAVE_FILE_RESULT_CODE:
				for (File f : files)
					fileHandler.saveToFile(f, mdpAdapter.getText());
				break;
			case HTML_FILE_RESULT_CODE:
				for (File f : files)
					fileHandler.saveToFile(f, mdpAdapter.getHtml());
				break;
			}
		}
	}

	private void readIntent() {
		final Intent intent = getIntent();
		String text = "";

		if (intent != null) {
			final Uri data = intent.getData();
			final String extra = intent.getStringExtra(Intent.EXTRA_TEXT);

			if (data != null) {
				final String path = data.getEncodedPath();
				text = this.fileHandler.loadFromFile(path);
			} else if (extra != null) {
				text = extra;
			}
		}

		if (mdpAdapter.isTextEmpty())
			mdpAdapter.setText(text);
	}
}
