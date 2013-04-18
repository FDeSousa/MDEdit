package de.bsd.mdedit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dropbox.sync.android.DbxFileSystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * FileHandler.java
 * 
 * @author Filipe De Sousa
 * @version %I%, %G%
 * 
 */
public class FileHandler {
	public static final File SD_FOLDER = new File(
			Environment.getExternalStorageDirectory(), "/MDEdit");

	private final Context context;

	public FileHandler(Context context) {
		FileHandler.SD_FOLDER.mkdirs(); // Make sure the folder structure is in place
		this.context = context;
		this.context.getExternalFilesDir(null);
	}
	
	public String doSomething() {
		String ret = "";
		final EditText input = new EditText(context);
		DialogInterface.OnClickListener okOnClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing
			}
		};
		DialogInterface.OnClickListener cancelOnClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing
			}
		};
		AlertDialog fileNameInput = new AlertDialog.Builder(this.context)
	    	.setTitle(R.string.input_filename_dialog_title)
	    	//.setMessage(R.string.input_filename_dialog_message)
	    	.setView(input)
	    	.setPositiveButton(R.string.ok, okOnClick)
	    	.setNegativeButton(R.string.cancel, cancelOnClick)
	    	.show();
		ret = input.getText().toString();
		return ret;
	}

	public void saveToFile(String fileName, String text) {
		File file = new File(FileHandler.SD_FOLDER, fileName);
		saveToFile(file, text);
	}

	public void saveToFile(File file, String text) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(text.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			Toast.makeText(context, "Save failed: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			Log.e("FileHandler.saveToFile", "Save failed.", e);
		}
	}

	public String loadFromFile(File file) {
		if (!file.isAbsolute())
			file = file.getAbsoluteFile();
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			String text = new String(buffer);
			fis.close();
			return text;
		} catch (IOException e) {
			Toast.makeText(context, "Load failed: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			Log.e("FileHandler.loadFromFile", "Load failed.", e);
		}
		return null;
	}

	public String loadFromFile(String fileName) {
		File file = new File(fileName);
		if (!file.isAbsolute())
			file = new File(FileHandler.SD_FOLDER, fileName);

		return loadFromFile(file);
	}
}
