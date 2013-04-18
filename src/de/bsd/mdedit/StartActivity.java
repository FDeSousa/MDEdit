package de.bsd.mdedit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFileSystem;

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
	private static final int REQUEST_LINK_TO_DBX = 0xDB6;

	private MDEditPagerAdapter mdpAdapter;
	private ViewPager mViewPager;
	private FileHandler fileHandler;
	private DbxAccountManager mDbxAcctMgr;
	private DbxFileSystem mDbxFileSys;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(),
				"APP-KEY", "APP-SECRET"); // App key, app secret

		fileHandler = new FileHandler(this);
		String initText = "";

		if (savedInstanceState != null)
			// Get text from savedInstanceState
			if ((initText = savedInstanceState.getString("text")) == null)
				initText = "";

		mdpAdapter = new MDEditPagerAdapter(getSupportFragmentManager(),
				initText);
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
		readIntent();
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

	private void linkToDropbox() {
		if (!mDbxAcctMgr.hasLinkedAccount())
			mDbxAcctMgr.startLink(this, StartActivity.REQUEST_LINK_TO_DBX);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save:
			// Intent save = new Intent(this, FileChooserActivity.class)
			// .putExtra(FileChooserActivity._Rootpath,
			// (Parcelable) new LocalFile(FileHandler.SD_FOLDER))
			// .putExtra(FileChooserActivity._SaveDialog, true)
			// .putExtra(FileChooserActivity._DefaultFilename,
			// "markdown.md")
			// .putExtra(FileChooserActivity._DisplayHiddenFiles, false);
			// startActivityForResult(save, SAVE_FILE_RESULT_CODE);
			linkToDropbox();
			fileHandler.doSomething();
			break;
		case R.id.menu_load:
			// Intent load = new Intent(this,
			// FileChooserActivity.class).putExtra(
			// FileChooserActivity._Rootpath,
			// (Parcelable) new LocalFile(FileHandler.SD_FOLDER))
			// .putExtra(FileChooserActivity._DisplayHiddenFiles, false);
			// startActivityForResult(load, LOAD_FILE_RESULT_CODE);
			linkToDropbox();
			break;
		case R.id.menu_export_html:
			// Intent export = new Intent(this, FileChooserActivity.class)
			// .putExtra(FileChooserActivity._Rootpath,
			// (Parcelable) new LocalFile(FileHandler.SD_FOLDER))
			// .putExtra(FileChooserActivity._SaveDialog, true)
			// .putExtra(FileChooserActivity._DefaultFilename,
			// "markup.html")
			// .putExtra(FileChooserActivity._DisplayHiddenFiles, false);
			// startActivityForResult(export, HTML_FILE_RESULT_CODE);
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
		if (requestCode == StartActivity.REQUEST_LINK_TO_DBX) {
			if (resultCode == Activity.RESULT_OK) {
				// User linked to Dropbox successfully...
				// Just confirm it's really linked
				if (mDbxAcctMgr.hasLinkedAccount())
					try {
						mDbxFileSys = DbxFileSystem.forAccount(mDbxAcctMgr
								.getLinkedAccount());
					} catch (Unauthorized u) {
						// User has de-authorised the app, or has failed to
						// login
						linkToDropbox();
					}
				else
					linkToDropbox();
			} else
				// User cancelled or operation failed...
				Toast.makeText(
						this,
						"Could not continue operation. Dropbox account not linked.",
						Toast.LENGTH_SHORT).show();
		} else
			super.onActivityResult(requestCode, resultCode, data);
	}

	private void readIntent() {
		final Intent intent = getIntent();
		String text = "";

		if (intent != null) {
			final Uri data = intent.getData();
			final String extra = intent.getStringExtra(Intent.EXTRA_TEXT);

			if (data != null) {
				final String path = data.getEncodedPath();
				text = fileHandler.loadFromFile(path);
			} else if (extra != null)
				text = extra;
		}

		if (mdpAdapter.isTextEmpty())
			mdpAdapter.setText(text);
	}
}
