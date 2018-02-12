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

package com.evrencoskun.tableview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public interface ITableAdapter {

    int getColumnHeaderItemViewType(int position);

    int getRowHeaderItemViewType(int position);

    int getCellItemViewType(int position);

    View getCornerView();

    RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType);

    void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int
            columnPosition, int rowPosition);

    RecyclerView.ViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType);

    void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel,
                                      int columnPosition);

    RecyclerView.ViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType);

    void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int
            rowPosition);

    View onCreateCornerView();

    ITableView getTableView();

    int getColorForSelection(AbstractViewHolder.SelectionState selectionState);

    /**
     * Sets the listener for changes of data set on the TableView.
     *
     * @param listener The AdapterDataSetChangedListener listener.
     */
    void addAdapterDataSetChangedListener(AdapterDataSetChangedListener listener);
}
