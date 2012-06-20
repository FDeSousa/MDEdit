/******************************************************************************
 * Copyright 2012 Filipe De Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
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
public class TextEditorHandler {

	private final EditText editText;
	private final MarkdownViewHandler mdView;
	private final InputMethodManager imm;

	/**
	 * @param findViewById
	 * @param mdView
	 * @param initText
	 */
	public TextEditorHandler(EditText editText, MarkdownViewHandler mdView,
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
	
	/**
	 * Gets the current X/Y scroll integers for the edit text.
	 * @return int[] containing X and Y scroll coordinates
	 */
	public int[] getScroll() {
		int[] scrollXY = { this.editText.getScrollX(), this.editText.getScrollY() };
		return scrollXY;
	}

}
