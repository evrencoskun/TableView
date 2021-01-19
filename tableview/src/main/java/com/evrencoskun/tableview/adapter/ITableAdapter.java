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

package com.evrencoskun.tableview.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public interface ITableAdapter<CH, RH, C> {

    int getColumnHeaderItemViewType(int position);

    int getRowHeaderItemViewType(int position);

    int getCellItemViewType(int position);

    View getCornerView();

    @NonNull
    AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable C cellItemModel, int columnPosition, int rowPosition);

    @NonNull
    AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable CH columnHeaderItemModel, int columnPosition);

    @NonNull
    AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable RH rowHeaderItemModel, int rowPosition);

    @NonNull
    View onCreateCornerView(@NonNull ViewGroup parent);

    ITableView getTableView();

    /**
     * Sets the listener for changes of data set on the TableView.
     *
     * @param listener The AdapterDataSetChangedListener listener.
     */
    void addAdapterDataSetChangedListener(@NonNull AdapterDataSetChangedListener<CH, RH, C> listener);
}
