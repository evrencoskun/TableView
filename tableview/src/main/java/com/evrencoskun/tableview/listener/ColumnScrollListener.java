package com.evrencoskun.tableview.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by evrencoskun on 12/06/2017.
 */

public class ColumnScrollListener extends RecyclerView.OnScrollListener {

    private RecyclerView.LayoutManager m_jCellLayoutManager;

    public ColumnScrollListener(RecyclerView.LayoutManager p_jCellLayoutManager) {
        this.m_jCellLayoutManager = p_jCellLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);


    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        scrollAllRecyclerView(recyclerView, dx, dy);
    }

    private void scrollAllRecyclerView(RecyclerView recyclerView, int dx, int dy) {
        for (int i = 0; i < m_jCellLayoutManager.getChildCount(); i++) {
            RecyclerView child = (RecyclerView) m_jCellLayoutManager.getChildAt(i);
            if (child != recyclerView) {
                ((LinearLayoutManager) child.getLayoutManager()).scrollToPositionWithOffset(-dx,
                        dy);
            }
        }
    }
}
