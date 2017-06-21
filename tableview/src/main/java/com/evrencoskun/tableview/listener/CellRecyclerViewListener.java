package com.evrencoskun.tableview.listener;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public class CellRecyclerViewListener extends RecyclerView.OnScrollListener implements
        RecyclerView.OnItemTouchListener {

    private static final String LOG_TAG = CellRecyclerViewListener.class.getSimpleName();

    private int mLastX;
    private RecyclerView m_jColumnHeaderRecyclerView;
    private RecyclerView.LayoutManager m_jCellLayoutManager;
    private RecyclerView m_jLastTouchedRecyclerView;


    public CellRecyclerViewListener(ITableView p_iTableView) {
        this.m_jColumnHeaderRecyclerView = p_iTableView.getColumnHeaderRecyclerView();
        this.m_jCellLayoutManager = p_iTableView.getCellRecyclerView().getLayoutManager();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        if (e.getAction() == MotionEvent.ACTION_DOWN) {

            if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                if (m_jLastTouchedRecyclerView != null && rv != m_jLastTouchedRecyclerView) {
                    // find last touched recyclerView
                    ((RecyclerView) m_jCellLayoutManager.getChildAt(getIndex
                            (m_jLastTouchedRecyclerView))).removeOnScrollListener(this);
                    Log.e(LOG_TAG, "index : " + getIndex(m_jLastTouchedRecyclerView) + " remove "
                            + "listener in touch" + " " + "event for last index : " + getIndex(rv));
                    /*((CellRecyclerView) m_jCellLayoutManager.getChildAt(getIndex
                            (m_jLastTouchedRecyclerView))).setScrollEnabled(false);*/

                    ((RecyclerView) m_jCellLayoutManager.getChildAt(getIndex
                            (m_jLastTouchedRecyclerView))).stopScroll();
                }

                mLastX = ((CellRecyclerView) rv).getScrolledX();
                rv.addOnScrollListener(this);
                Log.e(LOG_TAG, "index : " + getIndex(rv) + " add listener in touch event");

            } else {
                //Log.e(LOG_TAG, "index : " + getIndex(rv) + " I am here " + rv.getScrollState());
            }
        } else {
            final int nScrollX = ((CellRecyclerView) rv).getScrolledX();
            if (e.getAction() == MotionEvent.ACTION_UP) {

                if (mLastX == nScrollX) {
                    rv.removeOnScrollListener(this);
                    Log.e(LOG_TAG, "index : " + getIndex(rv) + " remove listener in touch event");
                }

                Log.e(LOG_TAG, "index : " + getIndex(rv) + " lastx : " + mLastX + " - " + nScrollX);
                m_jLastTouchedRecyclerView = rv;
            } else {
                //Log.e(LOG_TAG, "index : " + getIndex(rv) + " and I am here " + rv.getScrollState
                //        () + " " + e.getAction());
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (m_jLastTouchedRecyclerView != null || recyclerView != m_jLastTouchedRecyclerView) {


        }
        for (int i = 0; i < m_jCellLayoutManager.getChildCount(); i++) {
            RecyclerView child = (RecyclerView) m_jCellLayoutManager.getChildAt(i);
            if (child != recyclerView) {
                // Scroll horizontally
                child.scrollBy(dx, 0);
                Log.e(LOG_TAG, "index : " + getIndex(recyclerView) + " is scrolling to " +
                        getIndex(child));
            }
        }


    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.removeOnScrollListener(this);
            Log.e(LOG_TAG, "index : " + getIndex(recyclerView) + " remove listener in " +
                    "onScrolled event");
        }
    }

    private int getIndex(RecyclerView rv) {
        for (int i = 0; i < m_jCellLayoutManager.getChildCount(); i++) {
            RecyclerView child = (RecyclerView) m_jCellLayoutManager.getChildAt(i);
            if (child == rv) {
                return i;
            }
        }
        return -1;
    }
}
