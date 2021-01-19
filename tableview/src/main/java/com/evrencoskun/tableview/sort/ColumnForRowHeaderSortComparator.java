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

package com.evrencoskun.tableview.sort;

import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

/**
 * In order to keep RowHeader DataSet and Main DataSet aligned
 * it is required to sort RowHeader the same.
 * So if MainDataSet row 1 moved to position 10, RowHeader 1 move to position 10 too.
 * To accomplish that we need to set a comparator that use MainDataSet
 * in order to sort RowHeader.
 * Created by cedricferry on 7/2/18.
 */
public class ColumnForRowHeaderSortComparator implements Comparator<ISortableModel> {
    @NonNull
    private final List<ISortableModel> mRowHeaderList;
    @NonNull
    private final List<List<ISortableModel>> mReferenceList;
    private final int column;
    @NonNull
    private final SortState mSortState;
    @NonNull
    private final ColumnSortComparator mColumnSortComparator;

    public ColumnForRowHeaderSortComparator(@NonNull List<ISortableModel> rowHeader,
                                            @NonNull List<List<ISortableModel>> referenceList,
                                            int column,
                                            @NonNull SortState sortState) {
        this.mRowHeaderList = rowHeader;
        this.mReferenceList = referenceList;
        this.column = column;
        this.mSortState = sortState;
        this.mColumnSortComparator = new ColumnSortComparator(column, sortState);
    }

    @Override
    public int compare(ISortableModel o, ISortableModel t1) {
        Object o1 = mReferenceList.get(this.mRowHeaderList.indexOf(o)).get(column).getContent();
        Object o2 = mReferenceList.get(this.mRowHeaderList.indexOf(t1)).get(column).getContent();
        if (mSortState == SortState.DESCENDING) {
            return mColumnSortComparator.compareContent(o2, o1);
        } else {
            return mColumnSortComparator.compareContent(o1, o2);
        }
    }
}
