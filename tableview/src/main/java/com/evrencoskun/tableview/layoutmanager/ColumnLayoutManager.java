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

package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.util.TableViewUtils;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class ColumnLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = ColumnLayoutManager.class.getSimpleName();

    private ITableView mTableView;
    private CellRecyclerView mCellRowRecyclerView, mColumnHeaderRecyclerView;
    private ColumnHeaderLayoutManager mColumnHeaderLayoutManager;
    private CellLayoutManager mCellLayoutManager;

    private boolean mNeedFitForVerticalScroll, mNeedFitForHorizontalScroll;
    private int mLastDx = 0;
    private int mYPosition;


    public ColumnLayoutManager(Context context, ITableView tableView) {
        super(context);
        this.mTableView = tableView;
        this.mColumnHeaderRecyclerView = mTableView.getColumnHeaderRecyclerView();
        this.mColumnHeaderLayoutManager = mTableView.getColumnHeaderLayoutManager();
        this.mCellLayoutManager = mTableView.getCellLayoutManager();

        // Set default orientation
        this.setOrientation(ColumnLayoutManager.HORIZONTAL);

        //If you are using a RecyclerView.RecycledViewPool, it might be a good idea to set this
        // flag to true so that views will be available to other RecyclerViews immediately.
        this.setRecycleChildrenOnDetach(true);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mCellRowRecyclerView = (CellRecyclerView) view;
        mYPosition = getRowPosition();
    }


    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        // If has fixed width is true, than calculation of the column width is not necessary.
        if (mTableView.hasFixedWidth()) {
            return;
        }

        measureChild(child, widthUsed, heightUsed);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {

        int columnPosition = getPosition(child);

        // Get cached width size of column and cell
        int cacheWidth = mCellLayoutManager.getCacheWidth(mYPosition, columnPosition);
        int columnCacheWidth = mColumnHeaderLayoutManager.getCacheWidth(columnPosition);

        // Already each of them is same width size.
        if (cacheWidth != -1 && cacheWidth == columnCacheWidth) {
            // Control whether we need to set width or not.
            if (child.getMeasuredWidth() != cacheWidth) {
                TableViewUtils.setWidth(child, cacheWidth);
            }
        } else {
            View columnHeaderChild = mColumnHeaderLayoutManager.findViewByPosition(columnPosition);
            if (columnHeaderChild == null) {
                return;
            }

            // Need to calculate which one has the broadest width ?
            fitWidthSize(child, mYPosition, columnPosition, cacheWidth, columnCacheWidth,
                    columnHeaderChild);
        }

        // Control all of the rows which has same column position.
        if (shouldFitColumns(columnPosition, mYPosition)) {
            if (mLastDx < 0) {
                Log.e(LOG_TAG, "x: " + columnPosition + " y: " + mYPosition + " fitWidthSize " +
                        "left side ");
                mCellLayoutManager.fitWidthSize(columnPosition, true);
            } else {
                mCellLayoutManager.fitWidthSize(columnPosition, false);
                Log.e(LOG_TAG, "x: " + columnPosition + " y: " + mYPosition + " fitWidthSize " +
                        "right side");
            }
            mNeedFitForVerticalScroll = false;
        }

        // It need to be cleared to prevent unnecessary calculation.
        mNeedFitForHorizontalScroll = false;
    }

    private void fitWidthSize(View child, int row, int column, int cellWidth, int
            columnHeaderWidth, View columnHeaderChild) {

        if (cellWidth == -1) {
            // Alternatively, TableViewUtils.getWidth(child);
            cellWidth = child.getMeasuredWidth();
        }

        if (columnHeaderWidth == -1) {
            // Alternatively, TableViewUtils.getWidth(columnHeaderChild)
            columnHeaderWidth = columnHeaderChild.getMeasuredWidth();
        }

        if (cellWidth != 0) {

            if (columnHeaderWidth > cellWidth) {
                cellWidth = columnHeaderWidth;

            } else if (cellWidth > columnHeaderWidth) {
                columnHeaderWidth = cellWidth;
            }

            // Control whether column header needs to be change interns of width
            if (columnHeaderWidth != columnHeaderChild.getWidth()) {
                TableViewUtils.setWidth(columnHeaderChild, columnHeaderWidth);
                mNeedFitForVerticalScroll = true;
                mNeedFitForHorizontalScroll = true;
            }

            // Set the value to cache it for column header.
            mColumnHeaderLayoutManager.setCacheWidth(column, columnHeaderWidth);
        }


        // Set the width value to cache it for cell .
        TableViewUtils.setWidth(child, cellWidth);
        mCellLayoutManager.setCacheWidth(row, column, cellWidth);
    }

    private boolean shouldFitColumns(int xPosition, int yPosition) {
        if (mNeedFitForHorizontalScroll) {
            if (!mCellRowRecyclerView.isScrollOthers() && mCellLayoutManager.shouldFitColumns
                    (yPosition)) {
                if (mLastDx > 0) {
                    int last = findLastVisibleItemPosition();
                    //Log.e(LOG_TAG, "Warning: findFirstVisibleItemPosition is " + last);
                    if (xPosition == last) {
                        return true;
                    }
                } else if (mLastDx < 0) {
                    int first = findFirstVisibleItemPosition();
                    //Log.e(LOG_TAG, "Warning: findFirstVisibleItemPosition is " + first);
                    if (xPosition == first) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State
            state) {
        if (mColumnHeaderRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                mCellRowRecyclerView.isScrollOthers()) {
            // Every CellRowRecyclerViews should be scrolled after the ColumnHeaderRecyclerView.
            // Because it is the main compared one to make each columns fit.
            mColumnHeaderRecyclerView.scrollBy(dx, 0);
        }
        // It is important to determine the next attached view to fit all columns
        mLastDx = dx;

        // Set the right initialPrefetch size to improve performance
        this.setInitialPrefetchItemCount(2);

        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    private int getRowPosition() {
        return mCellLayoutManager.getPosition(mCellRowRecyclerView);
    }

    public int getLastDx() {
        return mLastDx;
    }

    public boolean isNeedFit() {
        return mNeedFitForVerticalScroll;
    }

    public void clearNeedFit() {
        mNeedFitForVerticalScroll = false;
    }

    public AbstractViewHolder[] getVisibleViewHolders() {
        int visibleChildCount = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
        int index = 0;

        AbstractViewHolder[] views = new AbstractViewHolder[visibleChildCount];
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {

            views[index] = (AbstractViewHolder) mCellRowRecyclerView
                    .findViewHolderForAdapterPosition(i);

            index++;
        }
        return views;
    }
}
