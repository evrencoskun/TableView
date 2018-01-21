package com.evrencoskun.tableview.handler;

import android.support.v7.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;
import com.evrencoskun.tableview.layoutmanager.ColumnLayoutManager;

/**
 * Created by evrencoskun on 13.01.2018.
 */

public class ScrollHandler {

    private ITableView mTableView;

    public ScrollHandler(ITableView tableView) {
        this.mTableView = tableView;
    }

    public void scrollToColumnPosition(int columnPosition) {
        scrollCellHorizontally(columnPosition);
        scrollColumnHeader(columnPosition);
    }

    public void scrollToRowPosition(int rowPosition) {
        mTableView.getCellLayoutManager().scrollToPosition(rowPosition);
        mTableView.getRowHeaderLayoutManager().scrollToPosition(rowPosition);
    }


    private void scrollCellHorizontally(int pColumnPosition) {
        CellLayoutManager cellLayoutManager = mTableView.getCellLayoutManager();

        for (int i = cellLayoutManager.findFirstVisibleItemPosition(); i < cellLayoutManager
                .findLastVisibleItemPosition() + 1; i++) {

            RecyclerView cellRowRecyclerView = (RecyclerView) cellLayoutManager
                    .findViewByPosition(i);

            if (cellRowRecyclerView != null) {
                ColumnLayoutManager columnLayoutManager = (ColumnLayoutManager)
                        cellRowRecyclerView.getLayoutManager();

                columnLayoutManager.scrollToPosition(pColumnPosition);
            }

        }
    }

    private void scrollColumnHeader(int pColumnPosition) {
        mTableView.getColumnHeaderLayoutManager().scrollToPosition(pColumnPosition);
    }
}
