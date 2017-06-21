package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.layoutmanager.ColumnLayoutManager;
import com.evrencoskun.tableview.listener.CellRecyclerViewListener;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public class CellRecyclerView extends RecyclerView {

    private int m_nScrolledX = 0;
    private ITableView m_jTableView;
    private CellRecyclerViewListener listener;

    public CellRecyclerView(Context context, ITableView p_jTableView) {
        super(context);
        this.m_jTableView = p_jTableView;

        initialize();
        //initializeListeners();
    }

    private void initialize() {
        // Set the Column layout manager that helps the fit width of the cell and column header
        ColumnLayoutManager layoutManager = new ColumnLayoutManager(getContext(), m_jTableView
                .getColumnHeaderRecyclerView().getLayoutManager());

        this.setLayoutManager(layoutManager);
    }

    private void initializeListeners() {
        listener = new CellRecyclerViewListener(m_jTableView);
        this.addOnItemTouchListener(listener);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        m_nScrolledX += dx;
        super.onScrolled(dx, dy);
    }


    public int getScrolledX() {
        return m_nScrolledX;
    }

    public void setScrollEnabled(boolean p_bIsScrollEnabled) {
        ((ColumnLayoutManager) getLayoutManager()).setScrollEnabled(p_bIsScrollEnabled);
        //((ColumnLayoutManager) getLayoutManager()).setScrollEnabled(true);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        return super.fling(velocityX, velocityY);
    }
}
