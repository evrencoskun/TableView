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

package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.R;
import com.evrencoskun.tableview.listener.scroll.HorizontalRecyclerViewListener;
import com.evrencoskun.tableview.listener.scroll.VerticalRecyclerViewListener;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public class CellRecyclerView extends RecyclerView {
    private static final String LOG_TAG = CellRecyclerView.class.getSimpleName();

    private int mScrolledX = 0;
    private int mScrolledY = 0;

    private boolean mIsHorizontalScrollListenerRemoved = true;
    private boolean mIsVerticalScrollListenerRemoved = true;

    public CellRecyclerView(@NonNull Context context) {
        super(context);

        // These are necessary.
        this.setHasFixedSize(false);
        this.setNestedScrollingEnabled(false);
        // These are for better scrolling process.
        this.setItemViewCacheSize(context.getResources().getInteger(R.integer
                .default_item_cache_size));
        this.setDrawingCacheEnabled(true);
        this.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        mScrolledX += dx;
        mScrolledY += dy;

        super.onScrolled(dx, dy);
    }

    public int getScrolledX() {
        return mScrolledX;
    }

    public void clearScrolledX() {
        mScrolledX = 0;
    }

    public int getScrolledY() {
        return mScrolledY;
    }

    @Override
    public void addOnScrollListener(@NonNull OnScrollListener listener) {
        if (listener instanceof HorizontalRecyclerViewListener) {
            if (mIsHorizontalScrollListenerRemoved) {
                mIsHorizontalScrollListenerRemoved = false;
                super.addOnScrollListener(listener);
            } else {
                // Do not let add the listener
                Log.w(LOG_TAG, "mIsHorizontalScrollListenerRemoved has been tried to add itself "
                        + "before remove the old one");
            }
        } else if (listener instanceof VerticalRecyclerViewListener) {
            if (mIsVerticalScrollListenerRemoved) {
                mIsVerticalScrollListenerRemoved = false;
                super.addOnScrollListener(listener);
            } else {
                // Do not let add the listener
                Log.w(LOG_TAG, "mIsVerticalScrollListenerRemoved has been tried to add itself " +
                        "before remove the old one");
            }
        } else {
            super.addOnScrollListener(listener);
        }
    }

    @Override
    public void removeOnScrollListener(@NonNull OnScrollListener listener) {
        if (listener instanceof HorizontalRecyclerViewListener) {
            if (mIsHorizontalScrollListenerRemoved) {
                // Do not let remove the listener
                Log.e(LOG_TAG, "HorizontalRecyclerViewListener has been tried to remove " +
                        "itself before add new one");
            } else {
                mIsHorizontalScrollListenerRemoved = true;
                super.removeOnScrollListener(listener);
            }
        } else if (listener instanceof VerticalRecyclerViewListener) {
            if (mIsVerticalScrollListenerRemoved) {
                // Do not let remove the listener
                Log.e(LOG_TAG, "mIsVerticalScrollListenerRemoved has been tried to remove " +
                        "itself before add new one");
            } else {
                mIsVerticalScrollListenerRemoved = true;
                super.removeOnScrollListener(listener);
            }
        } else {
            super.removeOnScrollListener(listener);
        }
    }

    public boolean isHorizontalScrollListenerRemoved() {
        return mIsHorizontalScrollListenerRemoved;
    }

    public boolean isScrollOthers() {
        return !mIsHorizontalScrollListenerRemoved;
    }

    /**
     * Begin a standard fling with an initial velocity along each axis in pixels per second.
     * If the velocity given is below the system-defined minimum this method will return false
     * and no fling will occur.
     *
     * @param velocityX Initial horizontal velocity in pixels per second
     * @param velocityY Initial vertical velocity in pixels per second
     * @return true if the fling was started, false if the velocity was too low to fling or
     * LayoutManager does not support scrolling in the axis fling is issued.
     * @see LayoutManager#canScrollVertically()
     * @see LayoutManager#canScrollHorizontally()
     */
    @Override
    public boolean fling(int velocityX, int velocityY) {
        // Adjust speeds to be able to provide smoother scroll.
        //velocityX *= 0.6;
        //velocityY *= 0.6;
        return super.fling(velocityX, velocityY);
    }
}
