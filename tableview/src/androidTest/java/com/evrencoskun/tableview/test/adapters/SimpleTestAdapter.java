/*
 * MIT License
 *
 * Copyright (c) 2021 Andrew Beck
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
    public AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int
            viewType) {
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
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.corner_layout, parent, false);
    }
}
