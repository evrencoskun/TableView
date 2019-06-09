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

    private CellRecyclerViewAdapter<List<T>> mCellRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter<T> mRowHeaderRecyclerViewAdapter;
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
    private AdapterDataSetChangedListener adapterDataSetChangedListener =
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
