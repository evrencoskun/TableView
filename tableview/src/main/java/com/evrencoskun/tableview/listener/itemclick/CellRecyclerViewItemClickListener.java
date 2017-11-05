package com.evrencoskun.tableview.listener.itemclick;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRowRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.handler.SelectionHandler;
import com.evrencoskun.tableview.listener.ITableViewListener;

/**
 * Created by evrencoskun on 26/09/2017.
 */

public class CellRecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {
    private static final String LOG_TAG = CellRecyclerViewItemClickListener.class.getSimpleName();

    private ITableViewListener m_jListener;
    private GestureDetector m_jGestureDetector;
    private CellRecyclerView m_jCellRowRecyclerView;
    private CellRecyclerView m_iCellRecyclerView;
    private SelectionHandler m_iSelectionHandler;

    public CellRecyclerViewItemClickListener(CellRecyclerView p_jRecyclerView, ITableView
            p_iTableView) {
        this.m_jCellRowRecyclerView = p_jRecyclerView;
        this.m_jListener = p_iTableView.getTableViewListener();
        this.m_iCellRecyclerView = p_iTableView.getCellRecyclerView();
        this.m_iSelectionHandler = p_iTableView.getSelectionHandler();

        m_jGestureDetector = new GestureDetector(p_jRecyclerView.getContext(), new
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

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        // Get interacted view from x,y coordinate.
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && m_jListener != null && m_jGestureDetector.onTouchEvent(e)) {
            // Find the view holder
            AbstractViewHolder holder = (AbstractViewHolder) m_jCellRowRecyclerView
                    .getChildViewHolder(childView);

            // Get y position from adapter
            CellRowRecyclerViewAdapter adapter = (CellRowRecyclerViewAdapter)
                    m_jCellRowRecyclerView.getAdapter();

            int nXPosition = holder.getAdapterPosition();
            int nYPosition = adapter.getYPosition();

            m_iSelectionHandler.setSelectedCellPositions(holder, nXPosition, nYPosition);

            // Call ITableView listener for item click
            m_jListener.onCellClicked(holder, nXPosition, nYPosition);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    private void longPressAction(MotionEvent e) {
        // Consume the action for the time when either the cell row recyclerView or
        // the cell recyclerView is scrolling.
        if ((m_jCellRowRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) ||
                (m_iCellRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) ||
                m_jCellRowRecyclerView.isScrollOthers()) {
            return;
        }

        // Get interacted view from x,y coordinate.
        View child = m_jCellRowRecyclerView.findChildViewUnder(e.getX(), e.getY());

        if (child != null && m_jListener != null) {
            // Find the view holder
            RecyclerView.ViewHolder holder = m_jCellRowRecyclerView.getChildViewHolder(child);

            // Get y position from adapter
            CellRowRecyclerViewAdapter adapter = (CellRowRecyclerViewAdapter)
                    m_jCellRowRecyclerView.getAdapter();

            // Call ITableView listener for long click
            //m_jListener.onCellLongPressed(holder, holder.getAdapterPosition(), adapter
            //        .getYPosition());
        }
    }
}