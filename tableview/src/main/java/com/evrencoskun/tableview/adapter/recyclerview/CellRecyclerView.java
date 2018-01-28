/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;
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

    public CellRecyclerView(Context context) {
        super(context);

        this.setHasFixedSize(false);
        this.setNestedScrollingEnabled(false);
        /*this.setItemViewCacheSize(100);
        this.setDrawingCacheEnabled(true);
        this.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);*/
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

    public int getScrolledY() { return mScrolledY; }


    @Override
    public void addOnScrollListener(OnScrollListener listener) {
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
    public void removeOnScrollListener(OnScrollListener listener) {
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

    public void setSelected(SelectionState selectionState, @ColorInt int backgroundColor, boolean
            ignoreSelectionColors) {
        for (int i = 0; i < getAdapter().getItemCount(); i++) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) findViewHolderForAdapterPosition
                    (i);
            if (viewHolder != null) {

                if (!ignoreSelectionColors) {
                    // Change background color
                    viewHolder.setBackgroundColor(backgroundColor);
                }

                // Change selection status of the view holder
                viewHolder.setSelected(selectionState);
            }
        }
    }

}
