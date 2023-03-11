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

package com.evrencoskun.tableview.filter;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class FilterChangedListener<T> {

    /**
     * Called when a filter has been changed.
     *
     * @param filteredCellItems      The list of filtered cell items.
     * @param filteredRowHeaderItems The list of filtered row items.
     */
    public void onFilterChanged(@NonNull List<List<T>> filteredCellItems, @NonNull List<T> filteredRowHeaderItems) {
    }

    /**
     * Called when the TableView filters are cleared.
     *
     * @param originalCellItems      The unfiltered cell item list.
     * @param originalRowHeaderItems The unfiltered row header list.
     */
    public void onFilterCleared(@NonNull List<List<T>> originalCellItems, @NonNull List<T> originalRowHeaderItems) {
    }
}
