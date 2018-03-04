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

/**
 * Created by evrencoskun on 4.03.2018.
 */

public class Preferences implements Parcelable {

    public int rowPosition;
    public int rowPositionOffset;
    public int columnPosition;
    public int columnPositionOffset;
    public int selectedRowPosition;
    public int selectedColumnPosition;

    public Preferences() {
    }

    protected Preferences(Parcel in) {
        rowPosition = in.readInt();
        rowPositionOffset = in.readInt();
        columnPosition = in.readInt();
        columnPositionOffset = in.readInt();
        selectedRowPosition = in.readInt();
        selectedColumnPosition = in.readInt();
    }

    public static final Creator<Preferences> CREATOR = new Creator<Preferences>() {
        @Override
        public Preferences createFromParcel(Parcel in) {
            return new Preferences(in);
        }

        @Override
        public Preferences[] newArray(int size) {
            return new Preferences[size];
        }
    };


    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable
     * object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link
     *              #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rowPosition);
        dest.writeInt(rowPositionOffset);
        dest.writeInt(columnPosition);
        dest.writeInt(columnPositionOffset);
        dest.writeInt(selectedRowPosition);
        dest.writeInt(selectedColumnPosition);
    }
}
