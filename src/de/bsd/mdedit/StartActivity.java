package de.bsd.mdedit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

public class StartActivity extends Activity
{

    private WebView webView;
    private EditText editor;
    private final Markdown markdown = new Markdown();
    private static final String FILE_NAME = "_tmP-save.md";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        editor = (EditText) findViewById(R.id.editor);

        // get text from savedInstanceState
        if (savedInstanceState!=null) {
            String text = savedInstanceState.getString("text");
            editor.setText(text);
            renderText(text);

        }
    }

    public void onResume() {
        super.onResume();


        webView = (WebView) findViewById(R.id.webview);
        editor = (EditText) findViewById(R.id.editor);

        if (editor.getText().toString()!=null && editor.getText().toString().isEmpty()) {
            String text = loadFromFile(FILE_NAME);
            if (text!=null) {
                editor.setText(text);
                renderText(text);
            }
        }


        editor.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed
            }

            public void afterTextChanged(Editable editable) {
                // Not needed
            }

            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                String text = editor.getText().toString();

                renderText(text);

            }
        });

    }

    private void renderText(String text) {
        Reader in = new StringReader(text);
        StringWriter out = new StringWriter();

        try {
            markdown.transform(in,out);

            String result = out.toString();

            webView = (WebView) findViewById(R.id.webview);
            webView.loadData(result, "text/html", "utf-8");
        } catch (ParseException e) {
            e.printStackTrace();  // TODO: Customise this generated block
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String text = editor.getText().toString();
        saveToFile(FILE_NAME,text);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String text = editor.getText().toString();
        outState.putString("text",text);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);

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
            String text = editor.getText().toString();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT,text);
            String subject = getString(R.string.subject_markdown);
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            startActivity(i);
            break;
        case R.id.menu_clear:
            editor.setText("");
            renderText("");
            break;
        }

        return true;
    }


    private void saveToFile(String fileName, String text) {
        File file = new File(getExternalFilesDir(null),fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  // TODO: Customise this generated block
            Toast.makeText(this,"Save failed: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private String loadFromFile(String fileName) {
        File file = new File(getExternalFilesDir(null),fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String text = new String(buffer);
            fis.close();
            return text;
        } catch (IOException e) {
            e.printStackTrace();  // TODO: Customise this generated block
        }
        return null;

    }

}
