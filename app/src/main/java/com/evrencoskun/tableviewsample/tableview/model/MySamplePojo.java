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

package com.evrencoskun.tableviewsample.tableview.model;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.sort.ISortableModel;

import java.util.Random;

/**
 * An example Pojo that is displayed in demo app-s tableview.
 */
public class MySamplePojo implements ISortableModel {

    public final Integer mRandom;
    public final Integer mRandomShort;
    private final Object[] columns;
    @NonNull
    private final String mId;
    @Nullable
    public final String mText;
    public final boolean mGenderMale;
    public final boolean mMoodHappy;

    /**
     * Create an item that will be displayed in the TableView.
     * the "column-values" are random generated.
     */
    public MySamplePojo(@NonNull String id) {
        this.mId = id;
        this.mText = "cell " + mId + " 1";
        mGenderMale = new Random().nextBoolean();
        mMoodHappy = new Random().nextBoolean();
        mRandom = abs(new Random().nextInt());
        mRandomShort = mRandom % 100;

        // the first colums of the table
        columns = new Object[]{mRandom, mRandomShort, mText, mGenderMale, mMoodHappy};
    }

    /**
     * This is necessary for sorting process. Id must be unique per data row.
     * See {@link ISortableModel}.
     */
    @NonNull
    @Override
    public String getId() {
        return mId;
    }

    /**
     * This is necessary for sorting process.
     * See {@link ISortableModel}.
     */
    @Nullable
    @Override
    public Object getContent(int column) {
        if (column >= 0 && column < columns.length) {
            return columns[column];
        }
        return  "cell " + mId + " " + column;
    }
}
