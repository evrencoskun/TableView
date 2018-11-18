/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableview.preference;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by evrencoskun on 4.03.2018.
 */

public class SavedState extends View.BaseSavedState {

    public Preferences preferences;

    public SavedState(Parcelable superState) {
        super(superState);
    }

    private SavedState(Parcel in) {
        super(in);
        preferences = in.readParcelable(Preferences.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeParcelable(preferences, flags);
    }

    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable
            .Creator<SavedState>() {
        public SavedState createFromParcel(Parcel in) { return new SavedState(in); }

        public SavedState[] newArray(int size) { return new SavedState[size]; }
    };
}
