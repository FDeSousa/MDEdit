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

import android.widget.EditText;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * TextEditorHandler.java
 * 
 * @author Fil
 * @version %I%, %G%
 * 
 */
public class TextEditorHandler {

	private final EditText editText;
	private final MarkdownViewHandler mdView;

	/**
	 * @param findViewById
	 * @param mdView
	 * @param initText
	 */
	public TextEditorHandler(EditText editText, MarkdownViewHandler mdView,
			String initText) {
		// TODO Auto-generated constructor stub
		this.editText = editText;
		this.mdView = mdView;
	}

	/**
	 * Convenience method for updating via TextWatcher.onTextChanged()
	 * 
	 * TODO: Should at some point be changed to call mdView.update() only when
	 * changing pages on ViewPager
	 */
	public void update() {
		this.mdView.update(this.getText());
	}

	public String getText() {
		return this.editText.getText().toString();
	}

	/**
	 * @param string
	 */
	public void setText(String string) {
		this.editText.setText(string);
		this.mdView.update(string);
	}

}
