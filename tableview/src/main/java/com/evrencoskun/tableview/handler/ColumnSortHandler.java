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

package com.evrencoskun.tableview.handler;

import android.support.v7.util.DiffUtil;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.sort.ColumnForRowHeaderSortComparator;
import com.evrencoskun.tableview.sort.ColumnSortCallback;
import com.evrencoskun.tableview.sort.ColumnSortComparator;
import com.evrencoskun.tableview.sort.RowHeaderForCellSortComparator;
import com.evrencoskun.tableview.sort.ISortableModel;
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

    private CellRecyclerViewAdapter mCellRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter mRowHeaderRecyclerViewAdapter;
    private ColumnHeaderRecyclerViewAdapter mColumnHeaderRecyclerViewAdapter;

    public ColumnSortHandler(ITableView tableView) {
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter) tableView.getCellRecyclerView()
                .getAdapter();

        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter) tableView
                .getRowHeaderRecyclerView().getAdapter();

        this.mColumnHeaderRecyclerViewAdapter = (ColumnHeaderRecyclerViewAdapter) tableView
                .getColumnHeaderRecyclerView().getAdapter();
    }

    public void sortByRowHeader(final SortState sortState) {
        List<ISortableModel> originalRowHeaderList = mRowHeaderRecyclerViewAdapter.getItems();
        List<ISortableModel> sortedRowHeaderList = new ArrayList<>(originalRowHeaderList);

        List<List<ISortableModel>> originalList = mCellRecyclerViewAdapter.getItems();
        List<List<ISortableModel>> sortedList = new ArrayList<>(originalList);

        if (sortState != SortState.UNSORTED) {
            // Do descending / ascending sort
            Collections.sort(sortedRowHeaderList, new RowHeaderSortComparator(sortState) );

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
        swapItems(originalRowHeaderList, sortedRowHeaderList, sortedList);
    }

    public void sort(int column, SortState sortState) {
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
        swapItems(originalList, sortedList, column, sortedRowHeaderList);
    }

    private void swapItems(List<ISortableModel> oldRowHeader,
                           List<ISortableModel> newRowHeader,
                           List<List<ISortableModel>> newColumnItems) {

        // Find the differences between old cell items and new items.
        final RowHeaderSortCallback diffCallback = new RowHeaderSortCallback(oldRowHeader, newRowHeader);

        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // Set new items without calling notifyCellDataSetChanged method of CellRecyclerViewAdapter
        mRowHeaderRecyclerViewAdapter.setItems(newRowHeader, false);
        mCellRecyclerViewAdapter.setItems(newColumnItems, false);

        diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);
        diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
    }

    private void swapItems(List<List<ISortableModel>> oldItems, List<List<ISortableModel>>
            newItems, int column, List<ISortableModel> newRowHeader) {

        // Find the differences between old cell items and new items.
        final ColumnSortCallback diffCallback = new ColumnSortCallback(oldItems, newItems, column);

        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // Set new items without calling notifyCellDataSetChanged method of CellRecyclerViewAdapter
        mCellRecyclerViewAdapter.setItems(newItems, false);
        mRowHeaderRecyclerViewAdapter.setItems(newRowHeader, false);

        diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
        diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);

    }

    public void swapItems(List<List<ISortableModel>> newItems, int column) {

        List<List<ISortableModel>> oldItems = (List<List<ISortableModel>>)
                mCellRecyclerViewAdapter.getItems();

        // Find the differences between old cell items and new items.
        final ColumnSortCallback diffCallback = new ColumnSortCallback(oldItems, newItems, column);

        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // Set new items without calling notifyCellDataSetChanged method of CellRecyclerViewAdapter
        mCellRecyclerViewAdapter.setItems(newItems, false);

        diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
        diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);

    }

    public SortState getSortingStatus(int column) {
        return mColumnHeaderRecyclerViewAdapter.getColumnSortHelper().getSortingStatus(column);
    }

    public SortState getRowHeaderSortingStatus() {
        return mRowHeaderRecyclerViewAdapter.getRowHeaderSortHelper().getSortingStatus();
    }
}
