package com.evrencoskun.tableview.listener.itemclick;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.handler.SelectionHandler;
import com.evrencoskun.tableview.listener.ITableViewListener;

/**
 * Created by evrencoskun on 22.11.2017.
 */

public abstract class AbstractItemClickListener implements RecyclerView.OnItemTouchListener {
    protected ITableViewListener m_jListener;
    protected GestureDetector m_jGestureDetector;
    protected CellRecyclerView m_jRecyclerView;
    protected SelectionHandler m_iSelectionHandler;
    protected ITableView m_iTableView;

    public AbstractItemClickListener(CellRecyclerView p_jRecyclerView, ITableView p_iTableView) {
        this.m_jRecyclerView = p_jRecyclerView;
        this.m_iTableView = p_iTableView;
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
        return clickAction(view, e);
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}


    abstract protected boolean clickAction(RecyclerView view, MotionEvent e);

    abstract protected void longPressAction(MotionEvent e);
}
