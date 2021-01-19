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

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.AdapterDataSetChangedListener;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.filter.Filter;
import com.evrencoskun.tableview.filter.FilterChangedListener;
import com.evrencoskun.tableview.filter.FilterItem;
import com.evrencoskun.tableview.filter.FilterType;
import com.evrencoskun.tableview.filter.IFilterableModel;

import java.util.ArrayList;
import java.util.List;

public class FilterHandler<T extends IFilterableModel> {

    private final CellRecyclerViewAdapter<List<T>> mCellRecyclerViewAdapter;
    private final RowHeaderRecyclerViewAdapter<T> mRowHeaderRecyclerViewAdapter;
    private List<List<T>> originalCellDataStore;
    private List<T> originalRowDataStore;

    private List<FilterChangedListener<T>> filterChangedListeners;

    public FilterHandler(@NonNull ITableView tableView) {
        tableView.getAdapter().addAdapterDataSetChangedListener(adapterDataSetChangedListener);
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter<List<T>>) tableView
                .getCellRecyclerView().getAdapter();

        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter<T>) tableView
                .getRowHeaderRecyclerView().getAdapter();
    }

    public void filter(@NonNull Filter filter) {
        if (originalCellDataStore == null || originalRowDataStore == null) {
            return;
        }

        List<List<T>> originalCellData = new ArrayList<>(originalCellDataStore);
        List<T> originalRowData = new ArrayList<>(originalRowDataStore);
        List<List<T>> filteredCellList = new ArrayList<>();
        List<T> filteredRowList = new ArrayList<>();

        if (filter.getFilterItems().isEmpty()) {
            filteredCellList = new ArrayList<>(originalCellDataStore);
            filteredRowList = new ArrayList<>(originalRowDataStore);
            dispatchFilterClearedToListeners(originalCellDataStore, originalRowDataStore);
        } else {
            for (int x = 0; x < filter.getFilterItems().size(); ) {
                final FilterItem filterItem = filter.getFilterItems().get(x);
                if (filterItem.getFilterType().equals(FilterType.ALL)) {
                    for (List<T> itemsList : originalCellData) {
                        for (T item : itemsList) {
                            if (item
                                    .getFilterableKeyword()
                                    .toLowerCase()
                                    .contains(filterItem
                                            .getFilter()
                                            .toLowerCase())) {
                                filteredCellList.add(itemsList);
                                filteredRowList.add(originalRowData.get(filteredCellList.indexOf(itemsList)));
                                break;
                            }
                        }
                    }
                } else {
                    for (List<T> itemsList : originalCellData) {
                        if (itemsList
                                .get(filterItem
                                        .getColumn())
                                .getFilterableKeyword()
                                .toLowerCase()
                                .contains(filterItem
                                        .getFilter()
                                        .toLowerCase())) {
                            filteredCellList.add(itemsList);
                            filteredRowList.add(originalRowData.get(filteredCellList.indexOf(itemsList)));
                        }
                    }
                }

                // If this is the last filter to be processed, the filtered lists will not be cleared.
                if (++x < filter.getFilterItems().size()) {
                    originalCellData = new ArrayList<>(filteredCellList);
                    originalRowData = new ArrayList<>(filteredRowList);
                    filteredCellList.clear();
                    filteredRowList.clear();
                }
            }
        }

        // Sets the filtered data to the TableView.
        mRowHeaderRecyclerViewAdapter.setItems(filteredRowList, true);
        mCellRecyclerViewAdapter.setItems(filteredCellList, true);

        // Tells the listeners that the TableView is filtered.
        dispatchFilterChangedToListeners(filteredCellList, filteredRowList);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private final AdapterDataSetChangedListener adapterDataSetChangedListener =
            new AdapterDataSetChangedListener() {
                @Override
                public void onRowHeaderItemsChanged(@NonNull List rowHeaderItems) {
                    originalRowDataStore = new ArrayList<>(rowHeaderItems);
                }

                @Override
                public void onCellItemsChanged(@NonNull List cellItems) {
                    originalCellDataStore = new ArrayList<>(cellItems);
                }
            };

    private void dispatchFilterChangedToListeners(
            @NonNull List<List<T>> filteredCellItems,
            @NonNull List<T> filteredRowHeaderItems
    ) {
        if (filterChangedListeners != null) {
            for (FilterChangedListener<T> listener : filterChangedListeners) {
                listener.onFilterChanged(filteredCellItems, filteredRowHeaderItems);
            }
        }
    }

    private void dispatchFilterClearedToListeners(
            @NonNull List<List<T>> originalCellItems,
            @NonNull List<T> originalRowHeaderItems
    ) {
        if (filterChangedListeners != null) {
            for (FilterChangedListener<T> listener : filterChangedListeners) {
                listener.onFilterCleared(originalCellItems, originalRowHeaderItems);
            }
        }
    }

    public void addFilterChangedListener(@NonNull FilterChangedListener<T> listener) {
        if (filterChangedListeners == null) {
            filterChangedListeners = new ArrayList<>();
        }

        filterChangedListeners.add(listener);
    }
}
