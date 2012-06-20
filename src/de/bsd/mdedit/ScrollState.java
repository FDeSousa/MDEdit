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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * <p>
 * de.bsd.mdedit<br/>
 * ScrollState.java
 * 
 * @author Fil
 * @version %I%, %G%
 * 
 */
public class ScrollState implements Parcelable {
	
	private int[] scrollPos;
	
	public static Parcelable.Creator<ScrollState> CREATOR = new Parcelable.Creator<ScrollState>() {
		@Override
		public ScrollState createFromParcel(Parcel source) {
			int size = source.readInt();
			int[] scrollPos = new int[size];
			source.readIntArray(scrollPos);
			return new ScrollState(scrollPos);
		}

		@Override
		public ScrollState[] newArray(int size) {
			return new ScrollState[size];
		}
	};
	
	public ScrollState(int[] scrollPos) {
		this.scrollPos = scrollPos;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(scrollPos.length);
		dest.writeIntArray(scrollPos);
	}
	
	public int[] getScrollPos() {
		return this.scrollPos;
	}
	
	public void setScrollPos(int[] scrollPos) {
		this.scrollPos = scrollPos;
	}

}
