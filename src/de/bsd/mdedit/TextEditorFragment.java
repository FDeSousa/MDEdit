package de.bsd.mdedit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * TextEditorFragment.java
 * 
 * @author Fil
 * @version %I%, %G%
 * 
 */
public class TextEditorFragment extends Fragment {
	// TODO Add ability to change background and text colours
	public static final String TEXT_SELECTION = "TextSelection";

	private InputMethodManager imm;
	private EditText editor;

	public TextEditorFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imm = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);

		View view = inflater.inflate(R.layout.editor_view, container, false);
		editor = (EditText) view.findViewById(R.id.editor);

		if (savedInstanceState != null) {
			String initText = savedInstanceState.getString("title");
			if (initText != null)
				editor.setText(initText);

			int[] selection = savedInstanceState
					.getIntArray(TextEditorFragment.TEXT_SELECTION);
			if (selection != null)
				setSelection(selection);
		}

		return view;
	}

	public String getText() {
		return editor == null ? null : editor.getText().toString();
	}

	public void setText(String text) {
		editor.setText(text);
	}

	public boolean isEmpty() {
		return TextUtils.isEmpty(getText());
	}

	public void gainFocus() {
		editor.requestFocus();
		imm.showSoftInput(editor, InputMethodManager.SHOW_IMPLICIT);
	}

	public void loseFocus() {
		imm.hideSoftInputFromWindow(editor.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		editor.clearFocus();
	}

	public int[] getSelection() {
		int[] selection = { editor.getSelectionStart(),
				editor.getSelectionEnd() };
		return selection;
	}

	public void setSelection(int[] selection) {
		editor.setSelection(selection[0], selection[1]);
	}

}
