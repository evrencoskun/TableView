package com.evrencoskun.tableview.listener.itemclick;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by evrencoskun on 26/09/2017.
 */

public class ColumnHeaderRecyclerViewItemClickListener extends AbstractItemClickListener {

    public ColumnHeaderRecyclerViewItemClickListener(CellRecyclerView p_jRecyclerView, ITableView
            p_iTableView) {
        super(p_jRecyclerView, p_iTableView);

    }

    @Override
    protected boolean clickAction(RecyclerView view, MotionEvent e) {
        // Get interacted view from x,y coordinate.
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && m_jGestureDetector.onTouchEvent(e)) {
            // Find the view holder
            AbstractViewHolder holder = (AbstractViewHolder) m_jRecyclerView.getChildViewHolder
                    (childView);

            int nXPosition = holder.getAdapterPosition();

            // Control to ignore selection color
            if (!m_iTableView.isIgnoreSelectionColors()) {
                m_iSelectionHandler.setSelectedColumnPosition(holder, nXPosition);
            }

            if (m_jListener != null) {
                // Call ITableView listener for item click
                m_jListener.onColumnHeaderClicked(holder, nXPosition);
            }

            return true;
        }
        return false;
    }

    protected void longPressAction(MotionEvent e) {
        // Consume the action for the time when the recyclerView is scrolling.
        if (m_jRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
            return;
        }

        // Get interacted view from x,y coordinate.
        View child = m_jRecyclerView.findChildViewUnder(e.getX(), e.getY());

        if (child != null && m_jListener != null) {
            // Find the view holder
            RecyclerView.ViewHolder holder = m_jRecyclerView.getChildViewHolder(child);

            // Call ITableView listener for long click
            //m_jListener.onColumnHeaderLongPressed(holder, holder.getAdapterPosition());
        }
    }
}