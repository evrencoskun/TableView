package com.evrencoskun.tableview.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by evrencoskun on 12/06/2017.
 */

public class ColumnScrollListener extends RecyclerView.OnScrollListener {

    private RecyclerView.LayoutManager m_jCellLayoutManager;
    private RecyclerView m_jColumnRecyclerView;

    public ColumnScrollListener(RecyclerView.LayoutManager p_jCellLayoutManager, RecyclerView
            p_jColumnRecyclerView) {
        this.m_jCellLayoutManager = p_jCellLayoutManager;
        this.m_jColumnRecyclerView = p_jColumnRecyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        scrollAllRecyclerView(recyclerView, dx, dy);
    }

    private void scrollAllRecyclerView(RecyclerView recyclerView, int dx, int dy) {
        if (m_jColumnRecyclerView == recyclerView) {
            // Scroll all Cell RecyclerViews
            for (int i = 0; i < m_jCellLayoutManager.getChildCount(); i++) {
                RecyclerView child = (RecyclerView) m_jCellLayoutManager.getChildAt(i);
                scroll(child, dx, dy);
            }
        } else {
            // Scroll Cell RecyclerViews except the recyclerView that is listened.
            for (int i = 0; i < m_jCellLayoutManager.getChildCount(); i++) {
                RecyclerView child = (RecyclerView) m_jCellLayoutManager.getChildAt(i);
                if (child != recyclerView) {
                    scroll(child, dx, dy);
                }
            }

            // Scroll Column Header RecyclerView
            scroll(m_jColumnRecyclerView, dx, dy);
        }
    }


    private void scroll(RecyclerView recyclerView, int dx, int dy) {
        recyclerView.removeOnScrollListener(this);
        recyclerView.scrollBy(dx, dy);
        recyclerView.addOnScrollListener(this);
    }
}
