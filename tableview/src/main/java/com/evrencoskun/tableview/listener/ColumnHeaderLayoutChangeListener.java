package com.evrencoskun.tableview.listener;

import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;

/**
 * Created by evrencoskun on 21.01.2018.
 */

public class ColumnHeaderLayoutChangeListener implements View.OnLayoutChangeListener {

    private CellRecyclerView mCellRecyclerView;
    private CellRecyclerView mColumnHeaderRecyclerView;
    private CellLayoutManager mCellLayoutManager;

    public ColumnHeaderLayoutChangeListener(ITableView tableView) {
        this.mCellRecyclerView = tableView.getCellRecyclerView();
        this.mColumnHeaderRecyclerView = tableView.getColumnHeaderRecyclerView();
        this.mCellLayoutManager = tableView.getCellLayoutManager();
    }


    @Override
    public void onLayoutChange((View v, int left, int top, int right, int bottom, int oldLeft,
                               int oldTop, int oldRight, int oldBottom) {
        int newWidth = right - left;
        int oldWidth = oldRight - oldLeft;

        if (mCellRecyclerView.isShown() && newWidth != oldWidth && mColumnHeaderRecyclerView
                .getWidth() > mCellRecyclerView.getWidth()) {

            // Remeasure all nested CellRow recyclerViews
            mCellLayoutManager.remeasureAllChild();
        }
    }
}
