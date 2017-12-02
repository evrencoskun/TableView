package com.evrencoskun.tableview.listener.itemclick;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRowRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by evrencoskun on 26/09/2017.
 */

public class CellRecyclerViewItemClickListener extends AbstractItemClickListener {
    private static final String LOG_TAG = CellRecyclerViewItemClickListener.class.getSimpleName();

    private CellRecyclerView m_iCellRecyclerView;

    public CellRecyclerViewItemClickListener(CellRecyclerView p_jRecyclerView, ITableView
            p_iTableView) {
        super(p_jRecyclerView, p_iTableView);
        this.m_iCellRecyclerView = p_iTableView.getCellRecyclerView();
    }

    @Override
    protected boolean clickAction(RecyclerView view, MotionEvent e) {
        // Get interacted view from x,y coordinate.
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && m_jGestureDetector.onTouchEvent(e)) {
            // Find the view holder
            AbstractViewHolder holder = (AbstractViewHolder) m_jRecyclerView.getChildViewHolder
                    (childView);

            // Get y position from adapter
            CellRowRecyclerViewAdapter adapter = (CellRowRecyclerViewAdapter) m_jRecyclerView
                    .getAdapter();

            int nXPosition = holder.getAdapterPosition();
            int nYPosition = adapter.getYPosition();

            // Control to ignore selection color
            if (!m_iTableView.isIgnoreSelectionColors()) {
                m_iSelectionHandler.setSelectedCellPositions(holder, nXPosition, nYPosition);
            }

            if (m_jListener != null) {
                // Call ITableView listener for item click
                m_jListener.onCellClicked(holder, nXPosition, nYPosition);
            }

            return true;
        }
        return false;
    }

    protected void longPressAction(MotionEvent e) {
        // Consume the action for the time when either the cell row recyclerView or
        // the cell recyclerView is scrolling.
        if ((m_jRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) ||
                (m_iCellRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) ||
                m_jRecyclerView.isScrollOthers()) {
            return;
        }

        // Get interacted view from x,y coordinate.
        View child = m_jRecyclerView.findChildViewUnder(e.getX(), e.getY());

        if (child != null && m_jListener != null) {
            // Find the view holder
            RecyclerView.ViewHolder holder = m_jRecyclerView.getChildViewHolder(child);

            // Get y position from adapter
            CellRowRecyclerViewAdapter adapter = (CellRowRecyclerViewAdapter) m_jRecyclerView
                    .getAdapter();

            // Call ITableView listener for long click
            //m_jListener.onCellLongPressed(holder, holder.getAdapterPosition(), adapter
            //        .getYPosition());
        }
    }
}