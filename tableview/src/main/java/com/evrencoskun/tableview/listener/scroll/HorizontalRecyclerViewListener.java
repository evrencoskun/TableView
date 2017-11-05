package com.evrencoskun.tableview.listener.scroll;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public class HorizontalRecyclerViewListener extends RecyclerView.OnScrollListener implements
        RecyclerView.OnItemTouchListener {

    private static final String LOG_TAG = HorizontalRecyclerViewListener.class.getSimpleName();

    private CellRecyclerView m_jColumnHeaderRecyclerView;
    private RecyclerView.LayoutManager m_jCellLayoutManager;
    private RecyclerView m_jLastTouchedRecyclerView;

    private boolean m_bMoved = false;
    private int m_nXPosition;

    private int m_nScrollPosition;
    private int m_nScrollPositionOffset = 0;

    private VerticalRecyclerViewListener m_iVerticalRecyclerViewListener;

    public HorizontalRecyclerViewListener(ITableView p_iTableView) {
        this.m_jColumnHeaderRecyclerView = p_iTableView.getColumnHeaderRecyclerView();
        this.m_jCellLayoutManager = p_iTableView.getCellRecyclerView().getLayoutManager();
        this.m_iVerticalRecyclerViewListener = p_iTableView.getVerticalRecyclerViewListener();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                if (m_jLastTouchedRecyclerView != null && rv != m_jLastTouchedRecyclerView) {
                    if (m_jLastTouchedRecyclerView == m_jColumnHeaderRecyclerView) {
                        m_jColumnHeaderRecyclerView.removeOnScrollListener(this);
                        m_jColumnHeaderRecyclerView.stopScroll();
                        Log.d(LOG_TAG, "Scroll listener  has been removed to " +
                                "m_jColumnHeaderRecyclerView at last touch control");
                    } else {
                        int nLastTouchedIndex = getIndex(m_jLastTouchedRecyclerView);

                        // Control whether the last touched recyclerView is still attached or not
                        if (nLastTouchedIndex >= 0 && nLastTouchedIndex < m_jCellLayoutManager
                                .getChildCount()) {
                            // Control the scroll listener is already removed. For instance; if user
                            // scroll the parent recyclerView vertically, at that time,
                            // ACTION_CANCEL
                            // will be triggered that removes the scroll listener of the last
                            // touched
                            // recyclerView.
                            if (!((CellRecyclerView) m_jLastTouchedRecyclerView)
                                    .isHorizontalScrollListenerRemoved()) {
                                // Remove scroll listener of the last touched recyclerView
                                // Because user touched another recyclerView before the last one get
                                // SCROLL_STATE_IDLE state that removes the scroll listener
                                ((RecyclerView) m_jCellLayoutManager.getChildAt
                                        (nLastTouchedIndex)).removeOnScrollListener(this);

                                Log.d(LOG_TAG, "Scroll listener  has been removed to " +
                                        m_jLastTouchedRecyclerView.getId() + " CellRecyclerView "
                                        + "at last touch control");

                                // the last one scroll must be stopped to be sync with others
                                ((RecyclerView) m_jCellLayoutManager.getChildAt
                                        (nLastTouchedIndex)).stopScroll();
                            }
                        }
                    }
                }

                m_nXPosition = ((CellRecyclerView) rv).getScrolledX();
                rv.addOnScrollListener(this);
                Log.d(LOG_TAG, "Scroll listener  has been added to " + rv.getId() + " at action "
                        + "down");
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            // Why does it matter ?
            // user scroll any recyclerView like brushing, at that time, ACTION_UP will be
            // triggered
            // before scrolling. So, we need to store whether it moved or not.
            m_bMoved = true;
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            int nScrollX = ((CellRecyclerView) rv).getScrolledX();
            // Is it just touched without scrolling then remove the listener
            if (m_nXPosition == nScrollX && !m_bMoved) {
                rv.removeOnScrollListener(this);
                Log.d(LOG_TAG, "Scroll listener  has been removed to " + rv.getId() + " at " +
                        "action" + " up");
            }

            m_jLastTouchedRecyclerView = rv;

        } else if (e.getAction() == MotionEvent.ACTION_CANCEL) {
            // ACTION_CANCEL action will be triggered if users try to scroll vertically
            // For this situation, it doesn't matter whether the x position is changed or not
            // Beside this, even if moved action will be triggered, scroll listener won't
            // triggered on cancel action. So, we need to change state of the m_bMoved value as
            // well.

            // Renew the scroll position and its offset
            renewScrollPosition(rv);

            rv.removeOnScrollListener(this);
            Log.d(LOG_TAG, "Scroll listener  has been removed to " + rv.getId() + " at action " +
                    "cancel");

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
        // Column Header should be scrolled firstly. Because it is the compared recyclerView to
        // make column width fit.

        if (recyclerView == m_jColumnHeaderRecyclerView) {
            super.onScrolled(recyclerView, dx, dy);

            // Scroll each cell recyclerViews
            for (int i = 0; i < m_jCellLayoutManager.getChildCount(); i++) {
                CellRecyclerView child = (CellRecyclerView) m_jCellLayoutManager.getChildAt(i);
                // Scroll horizontally
                child.scrollBy(dx, 0);
            }
        } else {
            // Scroll column header recycler view as well
            //m_jColumnHeaderRecyclerView.scrollBy(dx, 0);

            super.onScrolled(recyclerView, dx, dy);

            // Scroll each cell recyclerViews except the current touched one
            for (int i = 0; i < m_jCellLayoutManager.getChildCount(); i++) {
                CellRecyclerView child = (CellRecyclerView) m_jCellLayoutManager.getChildAt(i);
                if (child != recyclerView) {
                    // Scroll horizontally
                    child.scrollBy(dx, 0);
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // Renew the scroll position and its offset
            renewScrollPosition(recyclerView);

            recyclerView.removeOnScrollListener(this);
            Log.d(LOG_TAG, "Scroll listener has been removed to " + recyclerView.getId() + " at "
                    + "onScrollStateChanged");
            m_bMoved = false;

            // When a user scrolls horizontally, VerticalRecyclerView add vertical scroll
            // listener because of touching process.However, m_iVerticalRecyclerViewListener
            // doesn't know anything about it. So, it is necessary to remove the last touched
            // recyclerView which uses the m_iVerticalRecyclerViewListener.
            boolean bNeeded = m_jLastTouchedRecyclerView != m_jColumnHeaderRecyclerView;
            m_iVerticalRecyclerViewListener.removeLastTouchedRecyclerViewScrollListener(bNeeded);
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
     * This method calculates the current scroll position and its offset to help new attached
     * recyclerView on window at that position and offset
     *
     * @see #getScrollPosition()
     * @see #getScrollPositionOffset()
     */
    private void renewScrollPosition(RecyclerView p_jRecyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) p_jRecyclerView
                .getLayoutManager();
        m_nScrollPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

        // That means there is no completely visible Position.
        if (m_nScrollPosition == -1) {
            m_nScrollPosition = layoutManager.findFirstVisibleItemPosition();

            // That means there is just a visible item on the screen
            if (layoutManager.findFirstVisibleItemPosition() == layoutManager
                    .findLastVisibleItemPosition()) {
                // in this case we use the position which is the last & first visible item.
            } else {
                // That means there are 2 visible item on the screen. However, second one is not
                // completely visible.
                m_nScrollPosition = m_nScrollPosition + 1;
            }
        }

        m_nScrollPositionOffset = p_jRecyclerView.getLayoutManager().findViewByPosition
                (m_nScrollPosition).getLeft();
    }

    /**
     * When parent RecyclerView scrolls vertically, the child horizontal recycler views should be
     * displayed on right scroll position. So the first complete visible position of the
     * recyclerView is stored as a member to use it for a new attached recyclerview whose
     * orientation is horizontal as well.
     *
     * @see #getScrollPositionOffset()
     */
    public int getScrollPosition() {
        return m_nScrollPosition;
    }

    /**
     * Users can scroll the recyclerViews to the any x position which may not the exact position. So
     * we need to know store the offset value to locate a specific location for a new attached
     * recyclerView
     *
     * @see #getScrollPosition()
     */
    public int getScrollPositionOffset() {
        return m_nScrollPositionOffset;
    }

    public void setScrollPositionOffset(int p_nOffset) {
        m_nScrollPositionOffset = p_nOffset;
    }
}
