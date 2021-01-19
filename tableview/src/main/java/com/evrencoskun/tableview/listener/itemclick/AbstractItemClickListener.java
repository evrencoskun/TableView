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

package com.evrencoskun.tableview.listener.itemclick;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.handler.SelectionHandler;
import com.evrencoskun.tableview.listener.ITableViewListener;

/**
 * Created by evrencoskun on 22.11.2017.
 */

public abstract class AbstractItemClickListener implements RecyclerView.OnItemTouchListener {
    private ITableViewListener mListener;
    @NonNull
    protected GestureDetector mGestureDetector;
    @NonNull
    protected CellRecyclerView mRecyclerView;
    @NonNull
    protected SelectionHandler mSelectionHandler;
    @NonNull
    protected ITableView mTableView;

    public AbstractItemClickListener(@NonNull CellRecyclerView recyclerView, @NonNull ITableView tableView) {
        this.mRecyclerView = recyclerView;
        this.mTableView = tableView;
        this.mSelectionHandler = tableView.getSelectionHandler();

        mGestureDetector = new GestureDetector(mRecyclerView.getContext(), new GestureDetector
                .SimpleOnGestureListener() {

            @Nullable
            MotionEvent start;

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return clickAction(mRecyclerView, e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return doubleClickAction(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                start = e;
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // Check distance to prevent scroll to trigger the event
                if (start != null && Math.abs(start.getRawX() - e.getRawX()) < 20 && Math.abs
                        (start.getRawY() - e.getRawY()) < 20) {
                    longPressAction(e);
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        // Return false intentionally
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @NonNull
    protected ITableViewListener getTableViewListener() {
        if (mListener == null) {
            mListener = mTableView.getTableViewListener();
        }
        return mListener;
    }

    abstract protected boolean clickAction(@NonNull RecyclerView view, @NonNull MotionEvent e);

    abstract protected void longPressAction(@NonNull MotionEvent e);

    abstract protected boolean doubleClickAction(@NonNull MotionEvent e);
}
