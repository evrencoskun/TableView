package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.listener.CellRecyclerViewListener;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public class CellRecyclerView extends RecyclerView {
    private static final String LOG_TAG = CellRecyclerView.class.getSimpleName();

    private int m_nScrolledX = 0;
    private ITableView m_jTableView;
    private boolean mIsScrollListenerRemoved = true;

    public CellRecyclerView(Context context, ITableView p_jTableView) {
        super(context);
        this.m_jTableView = p_jTableView;

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        m_nScrolledX += dx;
        super.onScrolled(dx, dy);
        //Log.e(LOG_TAG, "index : " + getIndex(this) + " x : " + dx + " " +
        //mIsScrollListenerRemoved + " " + this);
    }

    public int getScrolledX() {
        return m_nScrolledX;
    }


    @Override
    public void addOnScrollListener(OnScrollListener listener) {
        if (!(listener instanceof CellRecyclerViewListener)) {
            super.addOnScrollListener(listener);
        } else if (listener instanceof CellRecyclerViewListener && mIsScrollListenerRemoved) {
            mIsScrollListenerRemoved = false;
            super.addOnScrollListener(listener);
        }
    }

    @Override
    public void removeOnScrollListener(OnScrollListener listener) {
        if (listener instanceof CellRecyclerViewListener) {
            if (mIsScrollListenerRemoved) {
                // Do not let remove the listener
                Log.e(LOG_TAG, "CellRecyclerViewListener has been tried to remove itself before "
                        + "add new " + "one");
            } else {
                mIsScrollListenerRemoved = true;
                super.removeOnScrollListener(listener);
            }
        } else {
            super.removeOnScrollListener(listener);
        }
    }

    public boolean isScrollListenerRemoved() {
        return mIsScrollListenerRemoved;
    }


    private int getIndex(RecyclerView rv) {
        for (int i = 0; i < m_jTableView.getCellRecyclerView().getLayoutManager().getChildCount()
                ; i++) {
            RecyclerView child = (RecyclerView) m_jTableView.getCellRecyclerView()
                    .getLayoutManager().getChildAt(i);
            if (child == rv) {
                return i;
            }
        }
        return -1;
    }
}
