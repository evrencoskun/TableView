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

package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.ITableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class CellRowRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private int mYPosition;
    private final ITableAdapter mTableAdapter;

    @NonNull
    private final ITableView mTableView;

    public CellRowRecyclerViewAdapter(@NonNull Context context, @NonNull ITableView tableView) {
        super(context, null);
        this.mTableAdapter = tableView.getAdapter();
        this.mTableView = tableView;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mTableAdapter.onCreateCellViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final AbstractViewHolder holder, final int xPosition) {
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
    public void onViewAttachedToWindow(@NonNull AbstractViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);

        SelectionState selectionState = mTableView.getSelectionHandler().getCellSelectionState
                (viewHolder.getBindingAdapterPosition(), mYPosition);

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
    public boolean onFailedToRecycleView(@NonNull AbstractViewHolder holder) {
        return holder.onFailedToRecycleView();
    }

    @Override
    public void onViewRecycled(@NonNull AbstractViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }
}
