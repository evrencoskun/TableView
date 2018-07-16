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
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.util.TableViewUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by evrencoskun on 30/07/2017.
 */

public class ColumnHeaderLayoutManager extends LinearLayoutManager {
    //private SparseArray<Integer> mCachedWidthList;
    private Map<Integer, Integer> mCachedWidthList = new HashMap<>();
    private ITableView mTableView;

    public ColumnHeaderLayoutManager(Context context, ITableView tableView) {
        super(context);
        mTableView = tableView;

        this.setOrientation(ColumnHeaderLayoutManager.HORIZONTAL);
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
        int cacheWidth = getCacheWidth(position);

        // If the width value of the cell has already calculated, then set the value
        if (cacheWidth != -1) {
            TableViewUtils.setWidth(child, cacheWidth);
        } else {
            super.measureChild(child, widthUsed, heightUsed);
        }
    }


    public void setCacheWidth(int position, int width) {
        mCachedWidthList.put(position, width);
    }

    public int getCacheWidth(int position) {
        Integer cachedWidth = mCachedWidthList.get(position);
        if (cachedWidth == null) {
            return -1;
        }
        return cachedWidth;
    }

    public int getFirstItemLeft() {
        View firstColumnHeader = findViewByPosition(findFirstVisibleItemPosition());
        return firstColumnHeader.getLeft();
    }

    /**
     * Helps to recalculate the width value of the cell that is located in given position.
     */
    public void removeCachedWidth(int position) {
        mCachedWidthList.remove(position);
    }


    public void customRequestLayout() {
        int left = getFirstItemLeft();
        int right;
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {

            // Column headers should have been already calculated.
            right = left + getCacheWidth(i);

            View columnHeader = findViewByPosition(i);
            columnHeader.setLeft(left);
            columnHeader.setRight(right);

            layoutDecoratedWithMargins(columnHeader, columnHeader.getLeft(), columnHeader.getTop
                    (), columnHeader.getRight(), columnHeader.getBottom());

            // + 1 is for decoration item.
            left = right + 1;
        }
    }

    public AbstractViewHolder[] getVisibleViewHolders() {
        int visibleChildCount = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
        int index = 0;

        AbstractViewHolder[] views = new AbstractViewHolder[visibleChildCount];
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {

            views[index] = (AbstractViewHolder) mTableView.getColumnHeaderRecyclerView()
                    .findViewHolderForAdapterPosition(i);

            index++;
        }
        return views;
    }

    public AbstractViewHolder getViewHolder(int xPosition) {
        return (AbstractViewHolder) mTableView.getColumnHeaderRecyclerView()
                .findViewHolderForAdapterPosition(xPosition);
    }

}
