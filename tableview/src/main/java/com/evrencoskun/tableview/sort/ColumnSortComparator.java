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
 * Created by evrencoskun on 25.11.2017.
 */

public class ColumnSortComparator extends AbstractSortComparator implements Comparator<List<ISortableModel>> {

    private final int mXPosition;

    public ColumnSortComparator(int xPosition, @NonNull SortState sortState) {
        this.mXPosition = xPosition;
        this.mSortState = sortState;
    }

    @Override
    public int compare(List<ISortableModel> t1, List<ISortableModel> t2) {
        Object o1 = t1.get(mXPosition).getContent();
        Object o2 = t2.get(mXPosition).getContent();

        if (mSortState == SortState.DESCENDING) {
            return compareContent(o2, o1);
        } else {
            // Default sorting process is ASCENDING
            return compareContent(o1, o2);
        }
    }

}
