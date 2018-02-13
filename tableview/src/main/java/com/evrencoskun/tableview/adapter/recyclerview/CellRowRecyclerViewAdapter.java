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

import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class CellRowRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private static final String LOG_TAG = CellRowRecyclerViewAdapter.class.getSimpleName();

    private int mYPosition;
    private ITableAdapter mTableAdapter;

    public CellRowRecyclerViewAdapter(Context context, List itemList, ITableAdapter
            p_iTableAdapter, int yPosition) {
        super(context, itemList);
        this.mYPosition = yPosition;
        this.mTableAdapter = p_iTableAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mTableAdapter != null) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) mTableAdapter
                    .onCreateCellViewHolder(parent, viewType);

            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int xPosition) {
        if (mTableAdapter != null) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
            Object value = getItem(xPosition);

            // Apply Selection Style
            if(mTableAdapter.getTableView().isSelectable()) {
                if (value instanceof ISelectableModel) {
                    viewHolder.setSelected(((ISelectableModel) value).getSelectionState());
                    int color = mTableAdapter.getColorForSelection(((ISelectableModel) value).getSelectionState());
                    viewHolder.setBackgroundColor(color);
                } else if(value != null){
                    // trigger exception, if isSelectable, Cells MUST implements ISelectableModel
                    throw new ClassCastException("Invalid Class type for Cell: ("+mYPosition+","+xPosition+"), ISelectableModel expected. Please implement ISelectable in your Cell in order to have it selectable.");
                }
            }

            mTableAdapter.onBindCellViewHolder(viewHolder, value, xPosition, mYPosition);
        }
    }

    public int getYPosition() {
        return mYPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return mTableAdapter.getCellItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        
        if(mTableAdapter.getTableView().isSelectable()) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
            SelectionState selectionState = mTableAdapter.getTableView().getSelectionHandler()
                    .getSelectionStateCell(holder.getAdapterPosition(), mYPosition);

            // Control to ignore selection color
            if (!mTableAdapter.getTableView().isIgnoreSelectionColors()) {

                // Change the background color of the view considering selected row/cell position.
                if (selectionState == SelectionState.SELECTED) {
                    viewHolder.setBackgroundColor(mTableAdapter.getTableView().getSelectedColor());
                } else {
                    viewHolder.setBackgroundColor(mTableAdapter.getTableView().getUnSelectedColor());
                }
            }

            // Change selection status
            viewHolder.setSelected(selectionState);
        }
    }
}
