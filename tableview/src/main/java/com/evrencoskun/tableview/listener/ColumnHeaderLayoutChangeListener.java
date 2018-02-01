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
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6,
                               int i7) {
        if (mCellRecyclerView.isShown() && mColumnHeaderRecyclerView.getWidth() >
                mCellRecyclerView.getWidth()) {

            // Remeasure all nested CellRow recyclerViews
            mCellLayoutManager.remeasureAllChild();
        }
    }
}
