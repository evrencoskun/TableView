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

public class FilterHandler<T> {

    private CellRecyclerViewAdapter mCellRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter mRowHeaderRecyclerViewAdapter;
    private List<List<IFilterableModel>> originalCellDataStore, originalCellData, filteredCellList;
    private List<T> originalRowDataStore, originalRowData, filteredRowList;

    private List<FilterChangedListener> filterChangedListeners;

    public FilterHandler(ITableView tableView) {
        tableView.getAdapter().addAdapterDataSetChangedListener(adapterDataSetChangedListener);
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter) tableView
                .getCellRecyclerView().getAdapter();

        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter) tableView
                .getRowHeaderRecyclerView().getAdapter();
    }

    @SuppressWarnings("unchecked")
    public void filter(Filter filter) {
        if (originalCellDataStore == null || originalRowDataStore == null) {
            return;
        }

        originalCellData = new ArrayList<>(originalCellDataStore);
        originalRowData = new ArrayList<>(originalRowDataStore);
        filteredCellList = new ArrayList<>();
        filteredRowList = new ArrayList<>();

        if (filter.getFilterItems().isEmpty()) {
            filteredCellList = new ArrayList<>(originalCellDataStore);
            filteredRowList = new ArrayList<>(originalRowDataStore);
            dispatchFilterClearedToListeners(originalCellDataStore, originalRowDataStore);
        } else {
            for (int x = 0; x < filter.getFilterItems().size(); ) {
                final FilterItem filterItem = filter.getFilterItems().get(x);
                if (filterItem.getFilterType().equals(FilterType.ALL)) {
                    for (List<IFilterableModel> itemsList : originalCellData) {
                        for (IFilterableModel item : itemsList) {
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
                    for (List<IFilterableModel> itemsList : originalCellData) {
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

    @SuppressWarnings("unchecked")
    private AdapterDataSetChangedListener adapterDataSetChangedListener =
            new AdapterDataSetChangedListener() {
                @Override
                public void onRowHeaderItemsChanged(List rowHeaderItems) {
                    if (rowHeaderItems != null) {
                        originalRowDataStore = new ArrayList<>(rowHeaderItems);
                    }
                }

                @Override
                public void onCellItemsChanged(List cellItems) {
                    if (cellItems != null) {
                        originalCellDataStore = new ArrayList<>(cellItems);
                    }
                }
            };

    @SuppressWarnings("unchecked")
    private void dispatchFilterChangedToListeners(
            List<List<IFilterableModel>> filteredCellItems,
            List<T> filteredRowHeaderItems
    ) {
        if (filterChangedListeners != null) {
            for (FilterChangedListener listener : filterChangedListeners) {
                listener.onFilterChanged(filteredCellItems, filteredRowHeaderItems);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void dispatchFilterClearedToListeners(
            List<List<IFilterableModel>> originalCellItems,
            List<T> originalRowHeaderItems
    ) {
        if (filterChangedListeners != null) {
            for (FilterChangedListener listener : filterChangedListeners) {
                listener.onFilterCleared(originalCellItems, originalRowHeaderItems);
            }
        }
    }

    public void addFilterChangedListener(FilterChangedListener listener) {
        if (filterChangedListeners == null) {
            filterChangedListeners = new ArrayList<>();
        }

        filterChangedListeners.add(listener);
    }
}
