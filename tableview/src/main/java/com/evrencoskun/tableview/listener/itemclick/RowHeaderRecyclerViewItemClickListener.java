package com.evrencoskun.tableview.listener.itemclick;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.handler.SelectionHandler;
import com.evrencoskun.tableview.listener.ITableViewListener;

/**
 * Created by evrencoskun on 26/09/2017.
 */

public class RowHeaderRecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {
    private ITableViewListener m_jListener;
    private GestureDetector m_jGestureDetector;
    private RecyclerView m_jRecyclerView;
    private SelectionHandler m_iSelectionHandler;

    public RowHeaderRecyclerViewItemClickListener(RecyclerView p_jRecyclerView, ITableView
            p_iTableView) {
        this.m_jRecyclerView = p_jRecyclerView;
        this.m_jListener = p_iTableView.getTableViewListener();
        this.m_iSelectionHandler = p_iTableView.getSelectionHandler();

        m_jGestureDetector = new GestureDetector(m_jRecyclerView.getContext(), new
                GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //TODO: long press implementation should have done.
                // Ignore for now;
                //longPressAction(e);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {

        // Get interacted view from x,y coordinate.
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && m_jListener != null && m_jGestureDetector.onTouchEvent(e)) {
            // Find the view holder
            AbstractViewHolder holder = (AbstractViewHolder) m_jRecyclerView.getChildViewHolder
                    (childView);

            int nYPosition = holder.getAdapterPosition();

            m_iSelectionHandler.setSelectedRowPosition(holder, nYPosition);

            // Call ITableView listener for item click
            m_jListener.onRowHeaderClicked(holder, nYPosition);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    private void longPressAction(MotionEvent e) {
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
            //m_jListener.onRowHeaderLongPressed(holder, holder.getAdapterPosition());
        }
    }
}
