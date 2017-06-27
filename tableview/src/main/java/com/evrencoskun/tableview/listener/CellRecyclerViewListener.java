package com.evrencoskun.tableview.listener;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public class CellRecyclerViewListener extends RecyclerView.OnScrollListener implements
        RecyclerView.OnItemTouchListener {

    private static final String LOG_TAG = CellRecyclerViewListener.class.getSimpleName();


    private RecyclerView m_jColumnHeaderRecyclerView;
    private RecyclerView.LayoutManager m_jCellLayoutManager;
    private RecyclerView m_jLastTouchedRecyclerView;
    private CellRecyclerViewAdapter m_iAdapter;

    private boolean m_bMoved = false;
    private int m_nXPosition;
    private int m_nLastXPosition;

    public CellRecyclerViewListener(CellRecyclerViewAdapter p_iAdapter, ITableView p_iTableView) {
        this.m_iAdapter = p_iAdapter;
        this.m_jColumnHeaderRecyclerView = p_iTableView.getColumnHeaderRecyclerView();
        this.m_jCellLayoutManager = p_iTableView.getCellRecyclerView().getLayoutManager();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                if (m_jLastTouchedRecyclerView != null && rv != m_jLastTouchedRecyclerView) {
                    int nLastTouchedIndex = getIndex(m_jLastTouchedRecyclerView);

                    // Control whether the last touched recyclerView is still attached or not
                    if (nLastTouchedIndex > 0 && nLastTouchedIndex < m_jCellLayoutManager
                            .getChildCount()) {
                        // Control the scroll listener is already removed. For instance; if user
                        // scroll the parent recyclerView vertically, at that time, ACTION_CANCEL
                        // will be triggered that removes the scroll listener of the last touched
                        // recyclerView.
                        if (!((CellRecyclerView) m_jLastTouchedRecyclerView)
                                .isScrollListenerRemoved()) {
                            // Remove scroll listener of the last touched recyclerView
                            // Because user touched another recyclerView before the last one get
                            // SCROLL_STATE_IDLE state that removes the scroll listener
                            ((RecyclerView) m_jCellLayoutManager.getChildAt(nLastTouchedIndex))
                                    .removeOnScrollListener(this);

                            // the last one scroll must be stopped to be sync with others
                            ((RecyclerView) m_jCellLayoutManager.getChildAt(nLastTouchedIndex))
                                    .stopScroll();
                        }
                    }
                }

                m_nXPosition = ((CellRecyclerView) rv).getScrolledX();
                rv.addOnScrollListener(this);
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            // Why is it matter ?
            // user scroll any recyclerView like brushing, at that time, ACTION_UP will be
            // triggered
            // before scrolling. So, we need to store whether it moved or not.
            m_bMoved = true;
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            int nScrollX = ((CellRecyclerView) rv).getScrolledX();
            // Is it just touched without scrolling then remove the listener
            if (m_nXPosition == nScrollX && !m_bMoved) {
                rv.removeOnScrollListener(this);
            }

            m_jLastTouchedRecyclerView = rv;

        } else if (e.getAction() == MotionEvent.ACTION_CANCEL) {
            // ACTION_CANCEL action will be triggered if users try to scroll vertically
            // For this situation, it doesn't matter whether the x position is changed or not
            // Beside this, even if moved action will be triggered, scroll listener won't
            // triggered on cancel action. So, we need to change state of the m_bMoved value as
            // well.

            rv.removeOnScrollListener(this);
            m_bMoved = false;

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
        super.onScrolled(recyclerView, dx, dy);

        // Scroll each cell recyclerViews
        for (int i = 0; i < m_jCellLayoutManager.getChildCount(); i++) {
            RecyclerView child = (RecyclerView) m_jCellLayoutManager.getChildAt(i);
            if (child != recyclerView) {
                // Scroll horizontally
                child.scrollBy(dx, 0);
            }
        }

        // Scroll column header recycler view as well
        m_jColumnHeaderRecyclerView.scrollBy(dx, 0);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // Renew the last x position
            m_nLastXPosition = ((CellRecyclerView) recyclerView).getScrolledX();
            // Send last x position to be able to change scroll position of the new attached
            // recyclerViews
            m_iAdapter.setXPosition(m_nLastXPosition);

            recyclerView.removeOnScrollListener(this);
            m_bMoved = false;
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

    /**
     * When parent RecyclerView scrolls vertically, the child horizontal recycler views should be
     * displayed on right scroll position. So m_nLastXPosition is needed for ColumnLayoutManager
     */
    public int getLastXPosition() {
        return m_nLastXPosition;
    }

}
