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

package com.evrencoskun.tableview;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.filter.Filter;
import com.evrencoskun.tableview.handler.ColumnSortHandler;
import com.evrencoskun.tableview.handler.FilterHandler;
import com.evrencoskun.tableview.handler.ScrollHandler;
import com.evrencoskun.tableview.handler.SelectionHandler;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;
import com.evrencoskun.tableview.layoutmanager.ColumnHeaderLayoutManager;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.evrencoskun.tableview.listener.scroll.HorizontalRecyclerViewListener;
import com.evrencoskun.tableview.listener.scroll.VerticalRecyclerViewListener;
import com.evrencoskun.tableview.sort.SortState;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public interface ITableView {

    void addView(View child, ViewGroup.LayoutParams params);

    boolean hasFixedWidth();

    boolean isIgnoreSelectionColors();

    boolean isShowHorizontalSeparators();
    
    boolean isShowVerticalSeparators();

    boolean isSortable();

    CellRecyclerView getCellRecyclerView();

    CellRecyclerView getColumnHeaderRecyclerView();

    CellRecyclerView getRowHeaderRecyclerView();

    ColumnHeaderLayoutManager getColumnHeaderLayoutManager();

    CellLayoutManager getCellLayoutManager();

    LinearLayoutManager getRowHeaderLayoutManager();

    HorizontalRecyclerViewListener getHorizontalRecyclerViewListener();

    VerticalRecyclerViewListener getVerticalRecyclerViewListener();

    ITableViewListener getTableViewListener();

    SelectionHandler getSelectionHandler();
    
    ColumnSortHandler getColumnSortHandler();

    DividerItemDecoration getHorizontalItemDecoration();
    
    DividerItemDecoration getVerticalItemDecoration();

    SortState getSortingStatus(int column);

    SortState getRowHeaderSortingStatus();

    void scrollToColumnPosition(int column);

    void scrollToColumnPosition(int column, int offset);

    void scrollToRowPosition(int row);

    void scrollToRowPosition(int row, int offset);

    void showRow(int row);

    void hideRow(int row);

    boolean isRowVisible(int row);

    void showAllHiddenRows();

    void clearHiddenRowList();

    void showColumn(int column);

    void hideColumn(int column);

    boolean isColumnVisible(int column);

    void showAllHiddenColumns();

    void clearHiddenColumnList();

    int getShadowColor();

    int getSelectedColor();

    int getUnSelectedColor();

    void sortColumn(int columnPosition, SortState sortState);

    void sortRowHeader(SortState sortState);

    void remeasureColumnWidth(int column);

    int getRowHeaderWidth();

    void setRowHeaderWidth(int rowHeaderWidth);

    AbstractTableAdapter getAdapter();

    /**
     * Filters the whole table using the provided Filter object which supports multiple filters.
     *
     * @param filter The filter object.
     */
    void filter(Filter filter);

    /**
     * Retrieves the FilterHandler of the TableView.
     *
     * @return The FilterHandler of the TableView.
     */
    FilterHandler getFilterHandler();
    
    /**
     * Retrieves the ScrollHandler of the TableView.
     *
     * @return The ScrollHandler of the TableView.
     */
    ScrollHandler getScrollHandler();
}
