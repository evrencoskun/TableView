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

package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.util.TableViewUtils;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class ColumnLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = ColumnLayoutManager.class.getSimpleName();

    @NonNull
    private final ITableView mTableView;
    private CellRecyclerView mCellRowRecyclerView;
    @NonNull
    private final CellRecyclerView mColumnHeaderRecyclerView;
    @NonNull
    private final ColumnHeaderLayoutManager mColumnHeaderLayoutManager;
    @NonNull
    private final CellLayoutManager mCellLayoutManager;

    private boolean mNeedFitForVerticalScroll, mNeedFitForHorizontalScroll;
    private int mLastDx = 0;
    private int mYPosition;

    public ColumnLayoutManager(@NonNull Context context, @NonNull ITableView tableView) {
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
    public void measureChildWithMargins(@NonNull View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        // If has fixed width is true, than calculation of the column width is not necessary.
        if (mTableView.hasFixedWidth()) {
            return;
        }

        measureChild(child, widthUsed, heightUsed);
    }

    @Override
    public void measureChild(@NonNull View child, int widthUsed, int heightUsed) {

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

    private void fitWidthSize(@NonNull View child, int row, int column, int cellWidth, int
            columnHeaderWidth, @NonNull View columnHeaderChild) {

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
                    return xPosition == last;
                } else if (mLastDx < 0) {
                    int first = findFirstVisibleItemPosition();
                    //Log.e(LOG_TAG, "Warning: findFirstVisibleItemPosition is " + first);
                    return xPosition == first;
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

    @NonNull
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
