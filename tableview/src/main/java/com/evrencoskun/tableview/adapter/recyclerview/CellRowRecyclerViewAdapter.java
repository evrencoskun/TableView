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

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class CellRowRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private static final String LOG_TAG = CellRowRecyclerViewAdapter.class.getSimpleName();

    private int mYPosition;
    private ITableAdapter mTableAdapter;
    private ITableView mTableView;

    public CellRowRecyclerViewAdapter(Context context, ITableView tableView) {
        super(context, null);
        this.mTableAdapter = tableView.getAdapter();
        this.mTableView = tableView;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mTableAdapter.onCreateCellViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(final AbstractViewHolder holder, final int xPosition) {
        mTableAdapter.onBindCellViewHolder(holder, getItem(xPosition), xPosition, mYPosition);
    }

    public int getYPosition() {
        return mYPosition;
    }

    public void setYPosition(int rowPosition) {
        mYPosition = rowPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return mTableAdapter.getCellItemViewType(position);
    }


    @Override
    public void onViewAttachedToWindow(AbstractViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);

        SelectionState selectionState = mTableView.getSelectionHandler().getCellSelectionState
                (viewHolder.getAdapterPosition(), mYPosition);

        // Control to ignore selection color
        if (!mTableView.isIgnoreSelectionColors()) {

            // Change the background color of the view considering selected row/cell position.
            if (selectionState == SelectionState.SELECTED) {
                viewHolder.setBackgroundColor(mTableView.getSelectedColor());
            } else {
                viewHolder.setBackgroundColor(mTableView.getUnSelectedColor());
            }
        }

        // Change selection status
        viewHolder.setSelected(selectionState);
    }

    @Override
    public boolean onFailedToRecycleView(AbstractViewHolder holder) {
        return holder.onFailedToRecycleView();
    }

    @Override
    public void onViewRecycled(AbstractViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }
}
