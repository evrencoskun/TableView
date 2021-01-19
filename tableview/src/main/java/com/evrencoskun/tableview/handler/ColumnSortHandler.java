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

package com.evrencoskun.tableview.handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.sort.ColumnForRowHeaderSortComparator;
import com.evrencoskun.tableview.sort.ColumnSortCallback;
import com.evrencoskun.tableview.sort.ColumnSortComparator;
import com.evrencoskun.tableview.sort.ColumnSortStateChangedListener;
import com.evrencoskun.tableview.sort.ISortableModel;
import com.evrencoskun.tableview.sort.RowHeaderForCellSortComparator;
import com.evrencoskun.tableview.sort.RowHeaderSortCallback;
import com.evrencoskun.tableview.sort.RowHeaderSortComparator;
import com.evrencoskun.tableview.sort.SortState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by evrencoskun on 24.11.2017.
 */

public class ColumnSortHandler {

    private final CellRecyclerViewAdapter<List<ISortableModel>> mCellRecyclerViewAdapter;
    private final RowHeaderRecyclerViewAdapter<ISortableModel> mRowHeaderRecyclerViewAdapter;
    private final ColumnHeaderRecyclerViewAdapter mColumnHeaderRecyclerViewAdapter;

    private List<ColumnSortStateChangedListener> columnSortStateChangedListeners = new ArrayList<>();
    private boolean mEnableAnimation = true;

    public boolean isEnableAnimation() {
        return mEnableAnimation;
    }

    public void setEnableAnimation(boolean mEnableAnimation) {
        this.mEnableAnimation = mEnableAnimation;
    }

    public ColumnSortHandler(@NonNull ITableView tableView) {
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter<List<ISortableModel>>) tableView.getCellRecyclerView()
                .getAdapter();

        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter<ISortableModel>) tableView
                .getRowHeaderRecyclerView().getAdapter();

        this.mColumnHeaderRecyclerViewAdapter = (ColumnHeaderRecyclerViewAdapter) tableView
                .getColumnHeaderRecyclerView().getAdapter();
    }

    public void sortByRowHeader(@NonNull final SortState sortState) {
        List<ISortableModel> originalRowHeaderList = mRowHeaderRecyclerViewAdapter.getItems();
        List<ISortableModel> sortedRowHeaderList = new ArrayList<>(originalRowHeaderList);

        List<List<ISortableModel>> originalList = mCellRecyclerViewAdapter.getItems();
        List<List<ISortableModel>> sortedList = new ArrayList<>(originalList);

        if (sortState != SortState.UNSORTED) {
            // Do descending / ascending sort
            Collections.sort(sortedRowHeaderList, new RowHeaderSortComparator(sortState));

            // Sorting Columns/Cells using the same logic has sorting DataSet
            RowHeaderForCellSortComparator rowHeaderForCellSortComparator
                    = new RowHeaderForCellSortComparator(
                    originalRowHeaderList,
                    originalList,
                    sortState);

            Collections.sort(sortedList, rowHeaderForCellSortComparator);
        }

        mRowHeaderRecyclerViewAdapter.getRowHeaderSortHelper().setSortingStatus(sortState);

        // Set sorted data list
        swapItems(originalRowHeaderList, sortedRowHeaderList, sortedList, sortState);
    }

    public void sort(int column, @NonNull SortState sortState) {
        List<List<ISortableModel>> originalList = mCellRecyclerViewAdapter.getItems();
        List<List<ISortableModel>> sortedList = new ArrayList<>(originalList);

        List<ISortableModel> originalRowHeaderList
                = mRowHeaderRecyclerViewAdapter.getItems();
        List<ISortableModel> sortedRowHeaderList
                = new ArrayList<>(originalRowHeaderList);

        if (sortState != SortState.UNSORTED) {
            // Do descending / ascending sort
            Collections.sort(sortedList, new ColumnSortComparator(column, sortState));

            // Sorting RowHeader using the same logic has sorting DataSet
            ColumnForRowHeaderSortComparator columnForRowHeaderSortComparator
                    = new ColumnForRowHeaderSortComparator(
                    originalRowHeaderList,
                    originalList,
                    column,
                    sortState);

            Collections.sort(sortedRowHeaderList, columnForRowHeaderSortComparator);
        }

        // Update sorting list of column headers
        mColumnHeaderRecyclerViewAdapter.getColumnSortHelper().setSortingStatus(column, sortState);

        // Set sorted data list
        swapItems(originalList, sortedList, column, sortedRowHeaderList, sortState);
    }

    private void swapItems(@NonNull List<ISortableModel> oldRowHeader,
                           @NonNull List<ISortableModel> newRowHeader,
                           @NonNull List<List<ISortableModel>> newColumnItems,
                           @NonNull SortState sortState
    ) {

        // Set new items without calling notifyCellDataSetChanged method of CellRecyclerViewAdapter
        mRowHeaderRecyclerViewAdapter.setItems(newRowHeader, !mEnableAnimation);
        mCellRecyclerViewAdapter.setItems(newColumnItems, !mEnableAnimation);

        if (mEnableAnimation) {
            // Find the differences between old cell items and new items.
            final RowHeaderSortCallback diffCallback = new RowHeaderSortCallback(oldRowHeader, newRowHeader);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);
            diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
        }

        for (ColumnSortStateChangedListener listener : columnSortStateChangedListeners) {
            listener.onRowHeaderSortStatusChanged(sortState);
        }
    }

    private void swapItems(@NonNull List<List<ISortableModel>> oldItems, @NonNull List<List<ISortableModel>>
            newItems, int column, @NonNull List<ISortableModel> newRowHeader, @NonNull SortState sortState) {

        // Set new items without calling notifyCellDataSetChanged method of CellRecyclerViewAdapter
        mCellRecyclerViewAdapter.setItems(newItems, !mEnableAnimation);
        mRowHeaderRecyclerViewAdapter.setItems(newRowHeader, !mEnableAnimation);

        if (mEnableAnimation) {
            // Find the differences between old cell items and new items.
            final ColumnSortCallback diffCallback = new ColumnSortCallback(oldItems, newItems, column);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
            diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);
        }

        for (ColumnSortStateChangedListener listener : columnSortStateChangedListeners) {
            listener.onColumnSortStatusChanged(column, sortState);
        }
    }

    public void swapItems(@NonNull List<List<ISortableModel>> newItems, int column) {

        List<List<ISortableModel>> oldItems = mCellRecyclerViewAdapter.getItems();

        // Set new items without calling notifyCellDataSetChanged method of CellRecyclerViewAdapter
        mCellRecyclerViewAdapter.setItems(newItems, !mEnableAnimation);

        if (mEnableAnimation) {
            // Find the differences between old cell items and new items.
            final ColumnSortCallback diffCallback = new ColumnSortCallback(oldItems, newItems, column);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
            diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);
        }

    }

    @NonNull
    public SortState getSortingStatus(int column) {
        return mColumnHeaderRecyclerViewAdapter.getColumnSortHelper().getSortingStatus(column);
    }

    @Nullable
    public SortState getRowHeaderSortingStatus() {
        return mRowHeaderRecyclerViewAdapter.getRowHeaderSortHelper().getSortingStatus();
    }

    /**
     * Sets the listener for the changes in column sorting.
     *
     * @param listener ColumnSortStateChangedListener listener.
     */
    public void addColumnSortStateChangedListener(@NonNull ColumnSortStateChangedListener listener) {
        if (columnSortStateChangedListeners == null) {
            columnSortStateChangedListeners = new ArrayList<>();
        }

        columnSortStateChangedListeners.add(listener);
    }
}
