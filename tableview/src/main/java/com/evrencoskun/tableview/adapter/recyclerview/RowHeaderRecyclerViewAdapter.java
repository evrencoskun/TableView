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

package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.ITableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;
import com.evrencoskun.tableview.sort.RowHeaderSortHelper;

import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class RowHeaderRecyclerViewAdapter<RH> extends AbstractRecyclerViewAdapter<RH> {

    private ITableAdapter mTableAdapter;
    private ITableView mTableView;
    private RowHeaderSortHelper mRowHeaderSortHelper;

    public RowHeaderRecyclerViewAdapter(Context context, List<RH> itemList, ITableAdapter
            tableAdapter) {
        super(context, itemList);
        this.mTableAdapter = tableAdapter;
        this.mTableView = tableAdapter.getTableView();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mTableAdapter.onCreateRowHeaderViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        mTableAdapter.onBindRowHeaderViewHolder(holder, getItem(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return mTableAdapter.getRowHeaderItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(AbstractViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);

        SelectionState selectionState = mTableView.getSelectionHandler().getRowSelectionState
                (viewHolder.getAdapterPosition());

        // Control to ignore selection color
        if (!mTableView.isIgnoreSelectionColors()) {

            // Change background color of the view considering it's selected state
            mTableView.getSelectionHandler().changeRowBackgroundColorBySelectionStatus
                    (viewHolder, selectionState);
        }

        // Change selection status
        viewHolder.setSelected(selectionState);
    }

    public RowHeaderSortHelper getRowHeaderSortHelper() {
        if (mRowHeaderSortHelper == null) {
            // It helps to store sorting state of row headers
            this.mRowHeaderSortHelper = new RowHeaderSortHelper();
        }
        return mRowHeaderSortHelper;
    }

}
