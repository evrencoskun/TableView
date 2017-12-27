package com.evrencoskun.tableview;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
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

    boolean isSortable();

    boolean isRowVisible(int row);

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

    DividerItemDecoration getHorizontalItemDecoration();

    SortState getSortingStatus(int column);

    void clearHiddenRowList();

    void showRow(int row);

    void hideRow(int row);

    void showAllHiddenRows();

    int getShadowColor();

    int getSelectedColor();

    int getUnSelectedColor();

    void sortColumn(int p_nColumnPosition, SortState p_eSortState);

    void remeasureColumnWidth(int column);

    AbstractTableAdapter getAdapter();
}
