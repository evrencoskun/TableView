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
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;
import com.evrencoskun.tableview.handler.ISelectableModel;
import com.evrencoskun.tableview.sort.ColumnSortHelper;
import com.evrencoskun.tableview.sort.SortState;

import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class ColumnHeaderRecyclerViewAdapter<CH> extends AbstractRecyclerViewAdapter<CH> {
    private static final String LOG_TAG = ColumnHeaderRecyclerViewAdapter.class.getSimpleName();

    private ITableAdapter mTableAdapter;
    private ColumnSortHelper mColumnSortHelper;

    public ColumnHeaderRecyclerViewAdapter(Context context, List<CH> itemList, ITableAdapter
            tableAdapter) {
        super(context, itemList);
        this.mTableAdapter = tableAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mTableAdapter.onCreateColumnHeaderViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
        Object value = getItem(position);

        // Apply Selection Style
        if(mTableAdapter.getTableView().isSelectable()) {
            if (value instanceof ISelectableModel) {
                viewHolder.setSelected(((ISelectableModel) value).getSelectionState());
                final int color = mTableAdapter.getColorForSelection(((ISelectableModel) value).getSelectionState());
                viewHolder.setBackgroundColor(color);
            } else if(value != null){
                // trigger exception, if isSelectable, Cells MUST implements ISelectableModel
                throw new ClassCastException("Invalid Class type for CH: "+position+", ISelectableModel expected. Please implement ISelectable in your ColumnHeaderCell in order to have it selectable.");

            }
        }

        mTableAdapter.onBindColumnHeaderViewHolder(viewHolder, value, position);
    }

    @Override
    public int getItemViewType(int position) {
        return mTableAdapter.getColumnHeaderItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        if(mTableAdapter.getTableView().isSelectable()) {
            SelectionState selectionState = mTableAdapter.getTableView().getSelectionHandler()
                    .getSelectionStateColumnHeader(viewHolder.getAdapterPosition());

            // Control to ignore selection color
            if (!mTableAdapter.getTableView().isIgnoreSelectionColors()) {

                // Change background color of the view considering it's selected state
                mTableAdapter.getTableView().getSelectionHandler()
                        .changeColumnBackgroundColorBySelectionStatus(viewHolder, selectionState);
            }

            // Change selection status
            viewHolder.setSelected(selectionState);
        }

        // Control whether the TableView is sortable or not.
        if (mTableAdapter.getTableView().isSortable()) {
            if (viewHolder instanceof AbstractSorterViewHolder) {
                // Get its sorting state
                SortState state = getColumnSortHelper().getSortingStatus(viewHolder
                        .getAdapterPosition());
                // Fire onSortingStatusChanged
                ((AbstractSorterViewHolder) viewHolder).onSortingStatusChanged(state);
            }
        }
    }


    public ColumnSortHelper getColumnSortHelper() {
        if (mColumnSortHelper == null) {
            // It helps to store sorting state of column headers
            this.mColumnSortHelper = new ColumnSortHelper(mTableAdapter.getTableView()
                    .getColumnHeaderLayoutManager());
        }
        return mColumnSortHelper;
    }
}
