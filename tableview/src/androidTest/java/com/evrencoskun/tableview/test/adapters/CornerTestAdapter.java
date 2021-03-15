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

public class CornerTestAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

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

    static class TestColumnHeaderViewHolder extends AbstractViewHolder {

        final LinearLayout column_header_container;
        final TextView cell_textview;

        public TestColumnHeaderViewHolder(View itemView) {
            super(itemView);
            column_header_container = itemView.findViewById(R.id.column_header_container);
            cell_textview = itemView.findViewById(R.id.column_header_textView);
        }
    }

    @NonNull
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.column_layout, parent, false);
        return new TestColumnHeaderViewHolder(layout);
    }

    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder,
                                             ColumnHeader columnHeader, int position) {

        TestColumnHeaderViewHolder viewHolder = (TestColumnHeaderViewHolder) holder;
        if (columnHeader.getData() != null)
        viewHolder.cell_textview.setText(columnHeader.getData().toString());

        viewHolder.column_header_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.cell_textview.requestLayout();
    }


    static class TestRowHeaderViewHolder extends AbstractViewHolder {

        final TextView cell_textview;

        public TestRowHeaderViewHolder(View itemView) {
            super(itemView);
            cell_textview = itemView.findViewById(R.id.row_header_textView);
        }
    }

    @NonNull
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        return new TestRowHeaderViewHolder(layout);
    }


    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder,
                                          RowHeader rowHeader, int position) {

        TestRowHeaderViewHolder viewHolder = (TestRowHeaderViewHolder) holder;
        if (rowHeader.getData() != null)
        viewHolder.cell_textview.setText(rowHeader.getData().toString());
    }

    @NonNull
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.corner_layout, parent, false);
    }
}
