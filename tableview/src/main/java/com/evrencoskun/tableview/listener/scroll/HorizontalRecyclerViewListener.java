/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.evrencoskun.tableview.listener.scroll;

import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public class HorizontalRecyclerViewListener extends RecyclerView.OnScrollListener implements
        RecyclerView.OnItemTouchListener {

    private static final String LOG_TAG = HorizontalRecyclerViewListener.class.getSimpleName();

    @NonNull
    private final CellRecyclerView mColumnHeaderRecyclerView;
    @Nullable
    private final RecyclerView.LayoutManager mCellLayoutManager;
    @Nullable
    private RecyclerView mLastTouchedRecyclerView;

    // X position means column position
    private int mXPosition;
    private boolean mIsMoved;

    private int mScrollPosition;
    private int mScrollPositionOffset = 0;

    @Nullable
    private RecyclerView mCurrentRVTouched = null;

    @NonNull
    private final VerticalRecyclerViewListener mVerticalRecyclerViewListener;

    public HorizontalRecyclerViewListener(@NonNull ITableView tableView) {
        this.mColumnHeaderRecyclerView = tableView.getColumnHeaderRecyclerView();
        this.mCellLayoutManager = tableView.getCellRecyclerView().getLayoutManager();
        this.mVerticalRecyclerViewListener = tableView.getVerticalRecyclerViewListener();
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        // Prevent multitouch, once we start to listen with a RV,
        // we ignore any other RV until the touch is released (UP)
        if (mCurrentRVTouched != null && rv != mCurrentRVTouched) {
            return true;
        }

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            mCurrentRVTouched = rv;
            if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                if (mLastTouchedRecyclerView != null && rv != mLastTouchedRecyclerView) {
                    if (mLastTouchedRecyclerView == mColumnHeaderRecyclerView) {
                        mColumnHeaderRecyclerView.removeOnScrollListener(this);
                        mColumnHeaderRecyclerView.stopScroll();
                        Log.d(LOG_TAG, "Scroll listener  has been removed to " +
                                "mColumnHeaderRecyclerView at last touch control");
                    } else {
                        int lastTouchedIndex = getIndex(mLastTouchedRecyclerView);

                        // Control whether the last touched recyclerView is still attached or not
                        if (lastTouchedIndex >= 0 && lastTouchedIndex < mCellLayoutManager
                                .getChildCount()) {
                            // Control the scroll listener is already removed. For instance; if user
                            // scroll the parent recyclerView vertically, at that time,
                            // ACTION_CANCEL
                            // will be triggered that removes the scroll listener of the last
                            // touched
                            // recyclerView.
                            if (!((CellRecyclerView) mLastTouchedRecyclerView)
                                    .isHorizontalScrollListenerRemoved()) {
                                // Remove scroll listener of the last touched recyclerView
                                // Because user touched another recyclerView before the last one get
                                // SCROLL_STATE_IDLE state that removes the scroll listener
                                ((RecyclerView) mCellLayoutManager.getChildAt(lastTouchedIndex))
                                        .removeOnScrollListener(this);

                                Log.d(LOG_TAG, "Scroll listener  has been removed to " +
                                        mLastTouchedRecyclerView.getId() + " CellRecyclerView " +
                                        "at last touch control");

                                // the last one scroll must be stopped to be sync with others
                                ((RecyclerView) mCellLayoutManager.getChildAt(lastTouchedIndex))
                                        .stopScroll();
                            }
                        }
                    }
                }

                mXPosition = ((CellRecyclerView) rv).getScrolledX();
                rv.addOnScrollListener(this);
                Log.d(LOG_TAG, "Scroll listener  has been added to " + rv.getId() + " at action "
                        + "down");
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            mCurrentRVTouched = rv;
            // Why does it matter ?
            // user scroll any recyclerView like brushing, at that time, ACTION_UP will be
            // triggered
            // before scrolling. So, we need to store whether it moved or not.
            mIsMoved = true;
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            mCurrentRVTouched = null;
            int nScrollX = ((CellRecyclerView) rv).getScrolledX();
            // Is it just touched without scrolling then remove the listener
            if (mXPosition == nScrollX && !mIsMoved) {
                rv.removeOnScrollListener(this);
                Log.d(LOG_TAG, "Scroll listener  has been removed to " + rv.getId() + " at " +
                        "action" + " up");
            }

            mLastTouchedRecyclerView = rv;

        } else if (e.getAction() == MotionEvent.ACTION_CANCEL) {
            // ACTION_CANCEL action will be triggered if users try to scroll vertically
            // For this situation, it doesn't matter whether the x position is changed or not
            // Beside this, even if moved action will be triggered, scroll listener won't
            // triggered on cancel action. So, we need to change state of the mIsMoved value as
            // well.

            // Renew the scroll position and its offset
            renewScrollPosition(rv);

            rv.removeOnScrollListener(this);
            Log.d(LOG_TAG, "Scroll listener  has been removed to " + rv.getId() + " at action " +
                    "cancel");

            mIsMoved = false;

            mLastTouchedRecyclerView = rv;

            mCurrentRVTouched = null;
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        // Column Header should be scrolled firstly. Because it is the compared recyclerView to
        // make column width fit.

        if (recyclerView == mColumnHeaderRecyclerView) {
            super.onScrolled(recyclerView, dx, dy);

            // Scroll each cell recyclerViews
            for (int i = 0; i < mCellLayoutManager.getChildCount(); i++) {
                CellRecyclerView child = (CellRecyclerView) mCellLayoutManager.getChildAt(i);
                // Scroll horizontally
                child.scrollBy(dx, 0);
            }
        } else {
            // Scroll column header recycler view as well
            //mColumnHeaderRecyclerView.scrollBy(dx, 0);

            super.onScrolled(recyclerView, dx, dy);

            // Scroll each cell recyclerViews except the current touched one
            for (int i = 0; i < mCellLayoutManager.getChildCount(); i++) {
                CellRecyclerView child = (CellRecyclerView) mCellLayoutManager.getChildAt(i);
                if (child != recyclerView) {
                    // Scroll horizontally
                    child.scrollBy(dx, 0);
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // Renew the scroll position and its offset
            renewScrollPosition(recyclerView);

            recyclerView.removeOnScrollListener(this);
            Log.d(LOG_TAG, "Scroll listener has been removed to " + recyclerView.getId() + " at "
                    + "onScrollStateChanged");
            mIsMoved = false;

            // When a user scrolls horizontally, VerticalRecyclerView add vertical scroll
            // listener because of touching process.However, mVerticalRecyclerViewListener
            // doesn't know anything about it. So, it is necessary to remove the last touched
            // recyclerView which uses the mVerticalRecyclerViewListener.
            boolean isNeeded = mLastTouchedRecyclerView != mColumnHeaderRecyclerView;
            mVerticalRecyclerViewListener.removeLastTouchedRecyclerViewScrollListener(isNeeded);
        }
    }

    private int getIndex(@NonNull RecyclerView rv) {
        for (int i = 0; i < mCellLayoutManager.getChildCount(); i++) {
            if (mCellLayoutManager.getChildAt(i) == rv) {
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
    private void renewScrollPosition(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        mScrollPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

        // That means there is no completely visible Position.
        if (mScrollPosition == -1) {
            mScrollPosition = layoutManager.findFirstVisibleItemPosition();

            // That means there is just a visible item on the screen
            if (mScrollPosition == layoutManager.findLastVisibleItemPosition()) {
                // in this case we use the position which is the last & first visible item.
            } else {
                // That means there are 2 visible item on the screen. However, second one is not
                // completely visible.
                mScrollPosition = mScrollPosition + 1;
            }
        }

        mScrollPositionOffset = layoutManager.findViewByPosition(mScrollPosition).getLeft();
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
        return mScrollPosition;
    }

    /**
     * Users can scroll the recyclerViews to the any x position which may not the exact position. So
     * we need to know store the offset value to locate a specific location for a new attached
     * recyclerView
     *
     * @see #getScrollPosition()
     */
    public int getScrollPositionOffset() {
        return mScrollPositionOffset;
    }

    public void setScrollPositionOffset(int offset) {
        mScrollPositionOffset = offset;
    }

    /**
     * To change default scroll position that is before TableView is not populated.
     */
    public void setScrollPosition(int position) {
        this.mScrollPosition = position;
    }
}
