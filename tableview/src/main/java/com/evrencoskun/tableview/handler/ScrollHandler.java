package com.evrencoskun.tableview.handler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;
import com.evrencoskun.tableview.layoutmanager.ColumnLayoutManager;

/**
 * Created by evrencoskun on 13.01.2018.
 */

public class ScrollHandler {

    private ITableView mTableView;
    private CellLayoutManager mCellLayoutManager;
    private LinearLayoutManager mRowHeaderLayoutManager;

    public ScrollHandler(ITableView tableView) {
        this.mTableView = tableView;
        this.mCellLayoutManager = tableView.getCellLayoutManager();
        this.mRowHeaderLayoutManager = tableView.getRowHeaderLayoutManager();
    }

    public void scrollToColumnPosition(int columnPosition) {
        // TableView is not on screen yet.
        if (!((View) mTableView).isShown()) {
            // Change default value of the listener
            mTableView.getHorizontalRecyclerViewListener().setScrollPosition(columnPosition);
        }

        // Column Header should be scrolled firstly because of fitting column width process.
        scrollColumnHeader(columnPosition);
        scrollCellHorizontally(columnPosition);
    }

    public void scrollToRowPosition(int rowPosition) {
        mRowHeaderLayoutManager.scrollToPosition(rowPosition);
        mCellLayoutManager.scrollToPosition(rowPosition);
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
