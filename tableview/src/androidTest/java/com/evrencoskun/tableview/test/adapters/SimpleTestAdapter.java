/*
 * Copyright (c) 2020. Andrew Beck
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.evrencoskun.tableview.test.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.test.R;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.test.models.Cell;
import com.evrencoskun.tableview.test.models.ColumnHeader;
import com.evrencoskun.tableview.test.models.RowHeader;

public class SimpleTestAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

    static class TestCellViewHolder extends AbstractViewHolder {

        final LinearLayout cell_container;
        final TextView cell_textview;

        TestCellViewHolder(View itemView) {
            super(itemView);
            cell_container = itemView.findViewById(com.evrencoskun.tableview.test.R.id.cell_container);
            cell_textview = itemView.findViewById(com.evrencoskun.tableview.test.R.id.cell_data);
        }
    }

    @NonNull
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_layout, parent, false);
        return new TestCellViewHolder(layout);
    }

    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, Cell cell, int
            columnPosition, int rowPosition) {

        TestCellViewHolder viewHolder = (TestCellViewHolder) holder;
        viewHolder.cell_textview.setText(cell.getData() != null ? cell.getData().toString() : "");

        viewHolder.cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.cell_textview.requestLayout();
    }

    @NonNull
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_layout, parent, false);
        return new TestCellViewHolder(layout);
    }

    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder,
                                             ColumnHeader columnHeader, int position) {

        TestCellViewHolder viewHolder = (TestCellViewHolder) holder;
        if (columnHeader.getData() != null)
        viewHolder.cell_textview.setText(columnHeader.getData().toString());

        viewHolder.cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.cell_textview.requestLayout();
    }

    @NonNull
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_layout, parent, false);
        return new TestCellViewHolder(layout);
    }

    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder,
                                          RowHeader rowHeader, int position) {

        TestCellViewHolder viewHolder = (TestCellViewHolder) holder;
        if (rowHeader.getData() != null)
        viewHolder.cell_textview.setText(rowHeader.getData().toString());
    }

    @NonNull
    public View onCreateCornerView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_layout, parent, false);
    }

    public int getColumnHeaderItemViewType(int position) { return 0; }
    public int getRowHeaderItemViewType(int position) { return 0; }
    public int getCellItemViewType(int position) { return 0; }
}
