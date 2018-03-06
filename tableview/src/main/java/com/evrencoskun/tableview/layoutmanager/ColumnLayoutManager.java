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
import android.util.SparseArray;
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

    private SparseArray<SparseArray<Integer>> mCachedWidthList;

    private ITableView mTableView;
    private CellRecyclerView mCellRowRecyclerView, mColumnHeaderRecyclerView;
    private ColumnHeaderLayoutManager mColumnHeaderLayoutManager;
    private CellLayoutManager mCellLayoutManager;
    private int mLastDx = 0;
    private boolean mNeedFit;


    public ColumnLayoutManager(Context context, ITableView tableView, CellRecyclerView
            cellRowRecyclerView) {
        super(context);
        this.mTableView = tableView;
        this.mColumnHeaderRecyclerView = mTableView.getColumnHeaderRecyclerView();
        this.mColumnHeaderLayoutManager = mTableView.getColumnHeaderLayoutManager();
        this.mCellRowRecyclerView = cellRowRecyclerView;
        this.mCellLayoutManager = mTableView.getCellLayoutManager();

        mCachedWidthList = new SparseArray<>();

        // Set default orientation
        this.setOrientation(ColumnLayoutManager.HORIZONTAL);
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
        // If has fixed width is true, than calculation of the column width is not necessary.
        if (mTableView.hasFixedWidth()) {
            super.measureChild(child, widthUsed, heightUsed);
            return;
        }

        int position = getPosition(child);

        // Get cached width size of column and cell
        int cacheWidth = getCacheWidth(position);
        int columnCacheWidth = mColumnHeaderLayoutManager.getCacheWidth(position);

        if (cacheWidth != -1 && cacheWidth == columnCacheWidth) {
            // Already each of them is same width size.
            TableViewUtils.setWidth(child, cacheWidth);
        } else {
            // Need to calculate which one has the broadest width ?
            fitWidthSize(child, position, cacheWidth, columnCacheWidth);
        }

        // Control all of the rows which has same column position.
        if (shouldFitColumns(position)) {
            if (mLastDx < 0) {
                Log.e(LOG_TAG, "x: " + position + " y: " + getRowPosition() + " fitWidthSize " +
                        "left side");
                mCellLayoutManager.fitWidthSize(position, true);
            } else {
                mCellLayoutManager.fitWidthSize(position, false);
                Log.e(LOG_TAG, "x: " + position + " y: " + getRowPosition() + " fitWidthSize " +
                        "right side");
            }
            mNeedFit = false;
        }
    }

    private boolean shouldFitColumns(int xPosition) {
        if (mNeedFit) {
            int yPosition = mCellLayoutManager.getPosition(mCellRowRecyclerView);
            if (mCellLayoutManager.shouldFitColumns(yPosition)) {
                if (mLastDx > 0) {
                    if (xPosition == findLastVisibleItemPosition()) {
                        return true;
                    }
                } else if (mLastDx < 0) {
                    if (xPosition == findFirstVisibleItemPosition()) {
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

        return super.scrollHorizontallyBy(dx, recycler, state);
    }


    private void fitWidthSize(View child, int position, int cellCacheWidth, int columnCacheWidth) {

        int cellWidth = cellCacheWidth;
        if (cellWidth == -1) {
            cellWidth = child.getMeasuredWidth(); // Alternatively, TableViewUtils.getWidth(child)
        }

        if (position > -1) {
            View columnHeaderChild = mColumnHeaderLayoutManager.findViewByPosition(position);
            if (columnHeaderChild != null) {

                int columnHeaderWidth = columnCacheWidth;
                if (columnHeaderWidth == -1) {
                    columnHeaderWidth = columnHeaderChild.getMeasuredWidth(); // Alternatively,
                    // TableViewUtils.getWidth(columnHeaderChild)
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
                        mNeedFit = true;
                    }

                    // Set the value to cache it for column header.
                    mColumnHeaderLayoutManager.setCacheWidth(position, columnHeaderWidth);
                }
            }
        }

        // Set the width value to cache it for cell .
        TableViewUtils.setWidth(child, cellWidth);
        setCacheWidth(position, cellWidth);
    }

    private int getRowPosition() {
        return mCellLayoutManager.getPosition(mCellRowRecyclerView);
    }

    public void setCacheWidth(int position, int width) {
        int yPosition = getRowPosition();
        SparseArray<Integer> cellRowCaches = mCachedWidthList.get(yPosition);
        if (cellRowCaches == null) {
            cellRowCaches = new SparseArray<>();
        }
        cellRowCaches.put(position, width);
        mCachedWidthList.put(yPosition, cellRowCaches);
    }

    public int getCacheWidth(int position) {
        int yPosition = getRowPosition();

        SparseArray<Integer> cellRowCaches = mCachedWidthList.get(yPosition);
        if (cellRowCaches != null) {
            Integer cachedWidth = cellRowCaches.get(position);
            if (cachedWidth != null) {
                return cellRowCaches.get(position);
            }
        }

        return -1;
    }

    public int getLastDx() {
        return mLastDx;
    }

    public boolean isNeedFit() {
        return mNeedFit;
    }

    public void clearNeedFit() {
        mNeedFit = false;
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
