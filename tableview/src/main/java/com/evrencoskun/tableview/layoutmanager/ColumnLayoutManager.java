package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.listener.HorizontalRecyclerViewListener;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class ColumnLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = ColumnLayoutManager.class.getSimpleName();

    private RecyclerView.LayoutManager m_jLayoutManager;
    private ITableView m_iTableView;
    private HorizontalRecyclerViewListener m_iListener;

    public ColumnLayoutManager(Context context, ITableView p_iTableView) {
        super(context);
        this.m_iTableView = p_iTableView;
        this.m_jLayoutManager = m_iTableView.getColumnHeaderRecyclerView().getLayoutManager();
        this.m_iListener = m_iTableView.getHorizontalRecyclerViewListener();

        // Set default orientation
        this.setOrientation(ColumnLayoutManager.HORIZONTAL);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scrollHorizontallyBy(m_iListener.getLastXPosition(), recycler, state);
        //fitColumnWidth();
        //Log.e(LOG_TAG, "onLayoutChildren");
    }


    private void fitColumnWidth() {
        if (m_jLayoutManager == null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (m_jLayoutManager.getChildCount() > i) {
                View jColumnView = getChildAt(i);
                View jCellView = m_jLayoutManager.getChildAt(i);

                // Get width size both of them
                int nColumnHeaderWidth = jColumnView.getWidth();
                int nCellWidth = jCellView.getWidth();

                // Control Which one has the broadest width ?
                if (nColumnHeaderWidth > nCellWidth) {
                    changeColumnWidth(jCellView, nColumnHeaderWidth);
                } else if (nCellWidth > nColumnHeaderWidth) {
                    changeColumnWidth(jColumnView, nCellWidth);
                }
            }
        }
    }


    private void changeColumnWidth(View p_jViewHolder, int p_nNewWidth) {
        ViewGroup.LayoutParams params = p_jViewHolder.getLayoutParams();
        params.width = p_nNewWidth;
        requestLayout();

    }
}
