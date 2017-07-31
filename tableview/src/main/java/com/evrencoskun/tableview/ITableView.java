package com.evrencoskun.tableview;

import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.listener.HorizontalRecyclerViewListener;
import com.evrencoskun.tableview.listener.VerticalRecyclerViewListener;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public interface ITableView {

    void addView(View child, ViewGroup.LayoutParams params);

    CellRecyclerView getCellRecyclerView();

    CellRecyclerView getColumnHeaderRecyclerView();

    CellRecyclerView getRowHeaderRecyclerView();

    HorizontalRecyclerViewListener getHorizontalRecyclerViewListener();

    VerticalRecyclerViewListener getVerticalRecyclerViewListener();
}
