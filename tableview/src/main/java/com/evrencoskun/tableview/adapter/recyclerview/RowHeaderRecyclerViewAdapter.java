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
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.ITableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;
import com.evrencoskun.tableview.handler.ISelectableModel;
import com.evrencoskun.tableview.sort.RowHeaderSortHelper;


import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class RowHeaderRecyclerViewAdapter<RH> extends AbstractRecyclerViewAdapter<RH> {

    private ITableAdapter mTableAdapter;
    private RowHeaderSortHelper mRowHeaderSortHelper;

    public RowHeaderRecyclerViewAdapter(Context context, List<RH> itemList, ITableAdapter
            tableAdapter) {
        super(context, itemList);
        this.mTableAdapter = tableAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mTableAdapter.onCreateRowHeaderViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
        Object value = getItem(position);

        // Apply Selection Style
        if(mTableAdapter.getTableView().isSelectable()) {
            if (value instanceof ISelectableModel) {
                viewHolder.setSelected(((ISelectableModel) value).getSelectionState());
                int color = mTableAdapter.getColorForSelection(((ISelectableModel) value).getSelectionState());
                viewHolder.setBackgroundColor(color);
            } else if(value != null){
                // trigger exception, if isSelectable, Cells MUST implements ISelectableModel
                throw new ClassCastException("Invalid Class type for RH: "+position+", ISelectableModel expected. Please implement ISelectable in your RowHeaderCell in order to have it selectable.");
            }
        }

        mTableAdapter.onBindRowHeaderViewHolder(viewHolder, value, position);
    }

    @Override
    public int getItemViewType(int position) {
        return mTableAdapter.getRowHeaderItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        if(mTableAdapter.getTableView().isSelectable()) {
            SelectionState selectionState = mTableAdapter.getTableView().getSelectionHandler()
                    .getSelectionStateRowHeader(holder.getAdapterPosition());


            // Control to ignore selection color
            if (!mTableAdapter.getTableView().isIgnoreSelectionColors()) {
                // Change background color of the view considering it's selected state
                mTableAdapter.getTableView().getSelectionHandler()
                        .changeRowBackgroundColorBySelectionStatus(viewHolder, selectionState);
            }

            // Change selection status
            viewHolder.setSelected(selectionState);
        }
    }

    public RowHeaderSortHelper getRowHeaderSortHelper() {
        if (mRowHeaderSortHelper == null) {
            // It helps to store sorting state of column headers
            this.mRowHeaderSortHelper = new RowHeaderSortHelper();
        }
        return mRowHeaderSortHelper;
    }

}
