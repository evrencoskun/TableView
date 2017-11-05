package com.evrencoskun.tableview.listener.scroll;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;

/**
 * Created by evrencoskun on 30/06/2017.
 */

public class VerticalRecyclerViewListener extends RecyclerView.OnScrollListener implements
        RecyclerView.OnItemTouchListener {

    private static final String LOG_TAG = VerticalRecyclerViewListener.class.getSimpleName();

    private CellRecyclerView m_jRowHeaderRecyclerView, m_jCellRecyclerView;
    private RecyclerView m_jLastTouchedRecyclerView;

    private boolean m_bMoved = false;
    private int m_nYPosition;

    public VerticalRecyclerViewListener(ITableView p_iTableView) {
        this.m_jRowHeaderRecyclerView = p_iTableView.getRowHeaderRecyclerView();
        this.m_jCellRecyclerView = p_iTableView.getCellRecyclerView();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                if (m_jLastTouchedRecyclerView != null && rv != m_jLastTouchedRecyclerView) {
                    removeLastTouchedRecyclerViewScrollListener(false);
                }
                m_nYPosition = ((CellRecyclerView) rv).getScrolledY();
                rv.addOnScrollListener(this);

                if (rv == m_jCellRecyclerView) {
                    Log.d(LOG_TAG, "m_jCellRecyclerView scroll listener added");
                } else if (rv == m_jRowHeaderRecyclerView) {
                    Log.d(LOG_TAG, "m_jRowHeaderRecyclerView scroll listener added");
                }

                // Refresh the value;
                m_bMoved = false;
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            // Why does it matter ?
            // user scroll any recyclerView like brushing, at that time, ACTION_UP will be
            // triggered
            // before scrolling. So, we need to store whether it moved or not.
            m_bMoved = true;
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            int nScrollY = ((CellRecyclerView) rv).getScrolledY();

            // TODO: Even if moved value is true and it may not scroll. This should be fixed.
            // TODO: The scenario is scroll lightly center RecyclerView vertically.
            // TODO: Below if condition may be changed later.

            // Is it just touched without scrolling then remove the listener
            if (m_nYPosition == nScrollY && !m_bMoved && rv.getScrollState() == RecyclerView
                    .SCROLL_STATE_IDLE) {
                rv.removeOnScrollListener(this);

                if (rv == m_jCellRecyclerView) {
                    Log.d(LOG_TAG, "m_jCellRecyclerView scroll listener removed from up ");
                } else if (rv == m_jRowHeaderRecyclerView) {
                    Log.d(LOG_TAG, "m_jRowHeaderRecyclerView scroll listener removed from up");
                }
            }

            m_jLastTouchedRecyclerView = rv;

        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // CellRecyclerViews should be scrolled after the RowHeaderRecyclerView.
        // Because it is one of the main compared criterion to make each columns fit.

        if (recyclerView == m_jCellRecyclerView) {
            super.onScrolled(recyclerView, dx, dy);
            // The below code has been moved in CellLayoutManager
            //m_jRowHeaderRecyclerView.scrollBy(0, dy);

        } else if (recyclerView == m_jRowHeaderRecyclerView) {
            super.onScrolled(recyclerView, dx, dy);

            m_jCellRecyclerView.scrollBy(0, dy);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.removeOnScrollListener(this);
            m_bMoved = false;

            if (recyclerView == m_jCellRecyclerView) {
                Log.d(LOG_TAG, "m_jCellRecyclerView scroll listener removed from " +
                        "onScrollStateChanged");
            } else if (recyclerView == m_jRowHeaderRecyclerView) {
                Log.d(LOG_TAG, "m_jRowHeaderRecyclerView scroll listener removed from " +
                        "onScrollStateChanged");
            }
        }
    }

    /**
     * If current recyclerView that is touched to scroll is not same as the last one, this method
     * helps to remove the scroll listener of the last touched recyclerView.
     * This method is a little bit different from HorizontalRecyclerViewListener.
     *
     * @param p_bIsNeeded Is m_jCellRecyclerView scroll listener should be removed ? The scenario is
     *                    a user scrolls vertically using RowHeaderRecyclerView. After that, the
     *                    user scrolls horizontally using ColumnHeaderRecyclerView.
     */
    public void removeLastTouchedRecyclerViewScrollListener(boolean p_bIsNeeded) {
        if (m_jLastTouchedRecyclerView == m_jCellRecyclerView) {
            m_jCellRecyclerView.removeOnScrollListener(this);
            m_jCellRecyclerView.stopScroll();
            Log.d(LOG_TAG, "m_jCellRecyclerView scroll listener removed from last touched");
        } else {
            m_jRowHeaderRecyclerView.removeOnScrollListener(this);
            m_jRowHeaderRecyclerView.stopScroll();
            Log.d(LOG_TAG, "m_jRowHeaderRecyclerView scroll listener removed from last touched");
            if (p_bIsNeeded) {
                m_jCellRecyclerView.removeOnScrollListener(this);
                m_jCellRecyclerView.stopScroll();
                Log.d(LOG_TAG, "m_jCellRecyclerView scroll listener removed from last touched");
            }

        }
    }
}
