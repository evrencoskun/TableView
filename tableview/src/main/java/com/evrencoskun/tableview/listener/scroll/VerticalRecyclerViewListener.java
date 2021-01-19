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
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;

/**
 * Created by evrencoskun on 30/06/2017.
 */

public class VerticalRecyclerViewListener extends RecyclerView.OnScrollListener implements
        RecyclerView.OnItemTouchListener {

    private static final String LOG_TAG = VerticalRecyclerViewListener.class.getSimpleName();

    @NonNull
    private final CellRecyclerView mRowHeaderRecyclerView, mCellRecyclerView;
    private RecyclerView mLastTouchedRecyclerView;

    // Y Position means row position
    private int mYPosition;
    private boolean mIsMoved;

    @Nullable
    private RecyclerView mCurrentRVTouched = null;

    public VerticalRecyclerViewListener(@NonNull ITableView tableView) {
        this.mRowHeaderRecyclerView = tableView.getRowHeaderRecyclerView();
        this.mCellRecyclerView = tableView.getCellRecyclerView();
    }

    private float dx = 0, dy = 0;

    /**
     * check which direction the user is scrolling
     *
     * @param ev
     * @return
     */
    private boolean verticalDirection(@NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (dx == 0) {
                dx = ev.getX();
            }
            if (dy == 0) {
                dy = ev.getY();
            }
            float xdiff = Math.abs(dx - ev.getX());
            float ydiff = Math.abs(dy - ev.getY());
            dx = ev.getX();
            dy = ev.getY();

            // if user scrolled more horizontally than vertically
            return xdiff <= ydiff;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        // Prevent multitouch, once we start to listen with a RV,
        // we ignore any other RV until the touch is released (UP)
        if ((mCurrentRVTouched != null && rv != mCurrentRVTouched)) {
            return true;
        }

        // If scroll direction is not Vertical, then ignore and reset last RV touched
        if (!verticalDirection(e)) {
            mCurrentRVTouched = null;
            return false;
        }

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            mCurrentRVTouched = rv;
            if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                if (mLastTouchedRecyclerView != null && rv != mLastTouchedRecyclerView) {
                    removeLastTouchedRecyclerViewScrollListener(false);
                }
                mYPosition = ((CellRecyclerView) rv).getScrolledY();
                rv.addOnScrollListener(this);

                if (rv == mCellRecyclerView) {
                    Log.d(LOG_TAG, "mCellRecyclerView scroll listener added");
                } else if (rv == mRowHeaderRecyclerView) {
                    Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener added");
                }

                // Refresh the value;
                mIsMoved = false;
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
            int nScrollY = ((CellRecyclerView) rv).getScrolledY();

            // TODO: Even if moved value is true and it may not scroll. This should be fixed.
            // TODO: The scenario is scroll lightly center RecyclerView vertically.
            // TODO: Below if condition may be changed later.

            // Is it just touched without scrolling then remove the listener
            if (mYPosition == nScrollY && !mIsMoved && rv.getScrollState() == RecyclerView
                    .SCROLL_STATE_IDLE) {
                rv.removeOnScrollListener(this);

                if (rv == mCellRecyclerView) {
                    Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from up ");
                } else if (rv == mRowHeaderRecyclerView) {
                    Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener removed from up");
                }
            }

            mLastTouchedRecyclerView = rv;

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
        // CellRecyclerViews should be scrolled after the RowHeaderRecyclerView.
        // Because it is one of the main compared criterion to make each columns fit.

        if (recyclerView == mCellRecyclerView) {
            super.onScrolled(recyclerView, dx, dy);
            // The below code has been moved in CellLayoutManager
            //mRowHeaderRecyclerView.scrollBy(0, dy);

        } else if (recyclerView == mRowHeaderRecyclerView) {
            super.onScrolled(recyclerView, dx, dy);

            mCellRecyclerView.scrollBy(0, dy);
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.removeOnScrollListener(this);
            mIsMoved = false;
            mCurrentRVTouched = null;
            if (recyclerView == mCellRecyclerView) {
                Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from " +
                        "onScrollStateChanged");
            } else if (recyclerView == mRowHeaderRecyclerView) {
                Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener removed from " +
                        "onScrollStateChanged");
            }
        }
    }

    /**
     * If current recyclerView that is touched to scroll is not same as the last one, this method
     * helps to remove the scroll listener of the last touched recyclerView.
     * This method is a little bit different from HorizontalRecyclerViewListener.
     *
     * @param isNeeded Is mCellRecyclerView scroll listener should be removed ? The scenario is a
     *                 user scrolls vertically using RowHeaderRecyclerView. After that, the user
     *                 scrolls horizontally using ColumnHeaderRecyclerView.
     */
    public void removeLastTouchedRecyclerViewScrollListener(boolean isNeeded) {

        if (mLastTouchedRecyclerView == mCellRecyclerView) {
            mCellRecyclerView.removeOnScrollListener(this);
            mCellRecyclerView.stopScroll();
            Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from last touched");
        } else {
            mRowHeaderRecyclerView.removeOnScrollListener(this);
            mRowHeaderRecyclerView.stopScroll();
            Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener removed from last touched");
            if (isNeeded) {
                mCellRecyclerView.removeOnScrollListener(this);
                mCellRecyclerView.stopScroll();
                Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from last touched");
            }

        }
    }
}
