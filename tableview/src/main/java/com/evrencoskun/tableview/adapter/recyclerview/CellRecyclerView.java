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

    private int m_nScrolledX = 0;
    private int m_nScrolledY = 0;

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
        m_nScrolledX += dx;
        m_nScrolledY += dy;

        super.onScrolled(dx, dy);
    }


    public int getScrolledX() {
        return m_nScrolledX;
    }

    public void clearScrolledX() {
        m_nScrolledX = 0;
    }

    public int getScrolledY() { return m_nScrolledY; }


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

    public void setSelected(SelectionState p_eSelected, @ColorInt int p_nBackgroundColor, boolean
            p_bIsIgnoreSelectionColors) {
        for (int i = 0; i < getAdapter().getItemCount(); i++) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) findViewHolderForAdapterPosition
                    (i);
            if (viewHolder != null) {

                if (!p_bIsIgnoreSelectionColors) {
                    // Change background color
                    viewHolder.setBackgroundColor(p_nBackgroundColor);
                }

                // Change selection status of the view holder
                viewHolder.setSelected(p_eSelected);
            }
        }
    }

}
