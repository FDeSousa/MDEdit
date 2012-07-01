package de.bsd.mdedit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
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
	public static final File SD_FOLDER;
	public static final String TEMP_FILE_NAME;

	static {
		TEMP_FILE_NAME = "_tmp-save.md";
		SD_FOLDER = new File(Environment.getExternalStorageDirectory(), "/MDEdit");
	}

	private final Context context;

	public FileHandler(Context context) {
		SD_FOLDER.mkdirs(); // Make sure the folder structure is in place
		this.context = context;
		this.context.getExternalFilesDir(null);
	}

	public void saveToFile(String fileName, String text) {
		File file = new File(SD_FOLDER, fileName);
		saveToFile(file, text);
	}

	public void saveToFile(File file, String text) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(text.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			Toast.makeText(this.context, "Save failed: " + e.getMessage(),
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
			// TODO Fix hacky way of not showing toast for failed loading of temp file
			if (!file.getName().equals(TEMP_FILE_NAME))
				Toast.makeText(this.context, "Load failed: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			Log.e("FileHandler.loadFromFile", "Load failed.", e);
		}
		return null;
	}

	public String loadFromFile(String fileName) {
		File file = new File(fileName);
		if (!file.isAbsolute())
			file = new File(SD_FOLDER, fileName);

		return loadFromFile(file);
	}
}
