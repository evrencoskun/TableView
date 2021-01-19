/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.evrencoskun.tableview.preference;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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

    @NonNull
    public static final Creator<Preferences> CREATOR = new Creator<Preferences>() {
        @NonNull
        @Override
        public Preferences createFromParcel(Parcel in) {
            return new Preferences(in);
        }

        @NonNull
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
