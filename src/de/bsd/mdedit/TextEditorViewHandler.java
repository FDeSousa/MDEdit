package de.bsd.mdedit;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * TextEditorHandler.java
 * 
 * @author Filipe De Sousa
 * @version %I%, %G%
 * 
 */
public class TextEditorViewHandler {

	public static final String TEXT_SELECTION = "TextSelection";

	private final EditText editText;
	private final MarkdownViewHandler mdView;
	private final InputMethodManager imm;

	/**
	 * @param findViewById
	 * @param mdView
	 * @param initText
	 */
	public TextEditorViewHandler(EditText editText, MarkdownViewHandler mdView,
			String initText, InputMethodManager imm) {
		this.editText = editText;
		this.mdView = mdView;
		this.imm = imm;
	}

	/**
	 * Convenience method for updating via TextWatcher.onTextChanged() with the
	 * current text
	 */
	public void update() {
		this.mdView.update(this.getText());
	}

	/**
	 * Get the current text displayed.
	 * @return string
	 */
	public String getText() {
		return this.editText.getText().toString();
	}

	/**
	 * Method to execute when the text editor is gaining focus.
	 * Requests focus for the EditText View, and shows the soft keyboard.
	 */
	public void gainFocus() {
		this.editText.requestFocus();
		this.imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * Method to execute when the text editor is meant to lose focus.
	 * Clears focus from the EditText View, and hides the soft keyboard.
	 */
	public void loseFocus() {
		this.imm.hideSoftInputFromWindow(editText.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		this.editText.clearFocus();
	}

	/**
	 * Set the visible text.
	 * @param string
	 */
	public void setText(String string) {
		this.editText.setText(string);
		this.mdView.update(string);
	}
	
	public boolean isEmpty() {
		return this.editText.getText().length() == 0 ? true : false;
	}
	
	/**
	 * Gets the current selection start and end integers for the edit text.
	 * @return int[] containing start and end selection values
	 */
	public int[] getSelection() {
		int[] selection = { this.editText.getSelectionStart(), this.editText.getSelectionEnd() };
		return selection;
	}
	
	public void setSelection(int[] selection) {
		this.editText.setSelection(selection[0], selection[1]);
	}

}
