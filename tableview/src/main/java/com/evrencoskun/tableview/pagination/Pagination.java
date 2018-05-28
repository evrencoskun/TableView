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

package com.evrencoskun.tableview.pagination;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.AdapterDataSetChangedListener;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.filter.FilterChangedListener;
import com.evrencoskun.tableview.sort.ColumnForRowHeaderSortComparator;
import com.evrencoskun.tableview.sort.ColumnSortComparator;
import com.evrencoskun.tableview.sort.ColumnSortStateChangedListener;
import com.evrencoskun.tableview.sort.ISortableModel;
import com.evrencoskun.tableview.sort.RowHeaderForCellSortComparator;
import com.evrencoskun.tableview.sort.RowHeaderSortComparator;
import com.evrencoskun.tableview.sort.SortState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pagination<T> implements IPagination {

    private static final int DEFAULT_ITEMS_PER_PAGE = 10;
    private int itemsPerPage;
    private int currentPage;
    private int pageCount;
    private List<List<ISortableModel>> originalCellData, currentPageCellData;
    private List<ISortableModel> originalRowData, currentPageRowData;

    private RowHeaderRecyclerViewAdapter mRowHeaderRecyclerViewAdapter;
    private CellRecyclerViewAdapter mCellRecyclerViewAdapter;
    private ITableView tableView;

    private OnTableViewPageTurnedListener onTableViewPageTurnedListener;

    /**
     * Basic constructor, TableView instance is required.
     *
     * @param tableView The TableView to be paginated.
     */
    public Pagination(ITableView tableView) {
        this(tableView, DEFAULT_ITEMS_PER_PAGE, null);
    }

    /**
     * Applies pagination to the supplied TableView with number of items per page.
     *
     * @param tableView    The TableView to be paginated.
     * @param itemsPerPage The number of items per page.
     */
    public Pagination(ITableView tableView, int itemsPerPage) {
        this(tableView, itemsPerPage, null);
    }

    /**
     * Applies pagination to the supplied TableView with number of items per page and an
     * OnTableViewPageTurnedListener for handling changes in the TableView pagination.
     *
     * @param tableView    The TableView to be paginated.
     * @param itemsPerPage The number of items per page.
     * @param listener     The OnTableViewPageTurnedListener for the TableView.
     */
    public Pagination(ITableView tableView, int itemsPerPage, OnTableViewPageTurnedListener listener) {
        initialize(tableView, itemsPerPage, listener);
    }

    @SuppressWarnings("unchecked")
    private void initialize(ITableView tableView, int itemsPerPage, OnTableViewPageTurnedListener listener) {
        this.onTableViewPageTurnedListener = listener;
        this.itemsPerPage = itemsPerPage;
        this.tableView = tableView;
        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter) tableView
                .getRowHeaderRecyclerView().getAdapter();
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter) tableView.getCellRecyclerView()
                .getAdapter();
        this.tableView.getColumnSortHandler().addColumnSortStateChangedListener(columnSortStateChangedListener);
        this.tableView.getAdapter().addAdapterDataSetChangedListener(adapterDataSetChangedListener);
        this.tableView.getFilterHandler().addFilterChangedListener(filterChangedListener);
        this.originalCellData = tableView.getAdapter().getCellRecyclerViewAdapter().getItems();
        this.originalRowData = tableView.getAdapter().getRowHeaderRecyclerViewAdapter().getItems();
        this.currentPage = 1;
        reloadPages();
    }

    @SuppressWarnings("unchecked")
    private void reloadPages() {
        if (originalCellData != null && originalRowData != null) {
            paginateData();
            goToPage(currentPage);
        }
    }

    @SuppressWarnings("unchecked")
    private void paginateData() {
        int start, end;
        currentPageCellData = new ArrayList<>();
        currentPageRowData = new ArrayList<>();
        // No pagination if itemsPerPage is 0, all data will be loaded into the TableView.
        if (itemsPerPage == 0) {
            currentPageCellData.addAll(originalCellData);
            currentPageRowData.addAll(originalRowData);
            pageCount = 1;
            start = 0;
            end = currentPageCellData.size();
        } else {
            start = (currentPage * itemsPerPage) - itemsPerPage;
            end = (currentPage * itemsPerPage) > originalCellData.size() ?
                    originalCellData.size() : (currentPage * itemsPerPage);

            for (int x = start; x < end; x++) {
                currentPageCellData.add(originalCellData.get(x));
                currentPageRowData.add(originalRowData.get(x));
            }

            // Using ceiling to calculate number of pages, e.g. 103 items of 10 items per page
            // will result to 11 pages.
            pageCount = (int) Math.ceil((double) originalCellData.size() / itemsPerPage);
        }

        // Sets the paginated data to the TableView.
        mRowHeaderRecyclerViewAdapter.setItems(currentPageRowData, true);
        mCellRecyclerViewAdapter.setItems(currentPageCellData, true);

        // Dispatches TableView changes to Listener interface
        if (onTableViewPageTurnedListener != null) {
            onTableViewPageTurnedListener.onPageTurned(currentPageCellData.size(), start, end - 1);
        }
    }

    @Override
    public void nextPage() {
        currentPage = currentPage + 1 > pageCount ? currentPage : ++currentPage;
        paginateData();
    }

    @Override
    public void previousPage() {
        currentPage = currentPage - 1 == 0 ? currentPage : --currentPage;
        paginateData();
    }

    @Override
    public void goToPage(int page) {
        currentPage = (page > pageCount || page < 1) ? (page > pageCount && pageCount > 0 ? pageCount : currentPage) : page;
        paginateData();
    }

    @Override
    public void setItemsPerPage(int numItems) {
        itemsPerPage = numItems;
        currentPage = 1;
        paginateData();
    }

    @Override
    public void setOnTableViewPageTurnedListener(OnTableViewPageTurnedListener onTableViewPageTurnedListener) {
        this.onTableViewPageTurnedListener = onTableViewPageTurnedListener;
    }

    @Override
    public void removeOnTableViewPageTurnedListener() {
        this.onTableViewPageTurnedListener = null;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    @Override
    public int getPageCount() {
        return pageCount;
    }

    @Override
    public boolean isPaginated() {
        return itemsPerPage > 0;
    }

    @SuppressWarnings("unchecked")
    private AdapterDataSetChangedListener adapterDataSetChangedListener =
            new AdapterDataSetChangedListener() {
                @Override
                public void onRowHeaderItemsChanged(List rowHeaderItems) {
                    if (rowHeaderItems != null) {
                        originalRowData = new ArrayList<>(rowHeaderItems);
                        reloadPages();
                    }
                }

                @Override
                public void onCellItemsChanged(List cellItems) {
                    if (cellItems != null) {
                        originalCellData = new ArrayList<>(cellItems);
                        reloadPages();
                    }
                }
            };

    @SuppressWarnings("unchecked")
    private FilterChangedListener filterChangedListener =
            new FilterChangedListener() {
                @Override
                public void onFilterChanged(List filteredCellItems, List filteredRowHeaderItems) {
                    originalCellData = new ArrayList<>(filteredCellItems);
                    originalRowData = new ArrayList<>(filteredRowHeaderItems);
                    reloadPages();
                }

                @Override
                public void onFilterCleared(List originalCellItems, List originalRowHeaderItems) {
                    originalCellData = new ArrayList<>(originalCellItems);
                    originalRowData = new ArrayList<>(originalRowHeaderItems);
                    reloadPages();
                }
            };

    private ColumnSortStateChangedListener columnSortStateChangedListener =
            new ColumnSortStateChangedListener() {
                @Override
                public void onColumnSortStatusChanged(int column, SortState sortState) {
                    paginateOnColumnSort(column, sortState);
                }

                @Override
                public void onRowHeaderSortStatusChanged(SortState sortState) {
                    paginateOnColumnSort(-1, sortState);
                }
            };

    @SuppressWarnings("unchecked")
    private void paginateOnColumnSort(int column, SortState sortState) {
        List<ISortableModel> sortedRowHeaderList = new ArrayList<>(originalRowData);
        List<List<ISortableModel>> sortedList = new ArrayList<>(originalCellData);
        if (sortState != SortState.UNSORTED) {
            if (column == -1) {
                Collections.sort(sortedRowHeaderList, new RowHeaderSortComparator(sortState));
                RowHeaderForCellSortComparator rowHeaderForCellSortComparator =
                        new RowHeaderForCellSortComparator(
                                originalRowData,
                                originalCellData,
                                sortState
                        );

                Collections.sort(sortedList, rowHeaderForCellSortComparator);
            } else {
                Collections.sort(sortedList, new ColumnSortComparator(column, sortState));
                ColumnForRowHeaderSortComparator columnForRowHeaderSortComparator =
                        new ColumnForRowHeaderSortComparator(
                                originalRowData,
                                originalCellData,
                                column,
                                sortState
                        );

                Collections.sort(sortedRowHeaderList, columnForRowHeaderSortComparator);
            }
        }

        originalRowData = new ArrayList<>(sortedRowHeaderList);
        originalCellData = new ArrayList<>(sortedList);
        reloadPages();
    }

    /**
     * Listener interface for changing of TableView page.
     */
    public interface OnTableViewPageTurnedListener {

        /**
         * Called when the page is changed in the TableView.
         *
         * @param numItems   The number of items currently being displayed in the TableView.
         * @param itemsStart The starting item currently being displayed in the TableView.
         * @param itemsEnd   The ending item currently being displayed in the TableView.
         */
        void onPageTurned(int numItems, int itemsStart, int itemsEnd);
    }
}
