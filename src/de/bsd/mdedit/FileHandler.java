package de.bsd.mdedit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
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
	public static final String TEMP_FILE_NAME;

	static {
		TEMP_FILE_NAME = "_tmP-save.md";
	}

	private final Context context;

	public FileHandler(Context context) {
		this.context = context;
	}

	public void saveToFile(String fileName, String text) {
		File file = new File(this.context.getExternalFilesDir(null), fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(text.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			Toast.makeText(this.context, "Save failed: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	public String loadFromFile(String fileName, boolean absolutePath) {
		File file;
		if (!absolutePath)
			file = new File(this.context.getExternalFilesDir(null), fileName);
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
			// TODO Fix hacky way of not showing toast for failed loading of
			// temp file
			if (!fileName.equals(TEMP_FILE_NAME))
				Toast.makeText(this.context, "Load failed: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
		}
		return null;
	}
}
