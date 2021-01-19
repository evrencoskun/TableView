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

package com.evrencoskun.tableview.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by evrencoskun on 20/09/2017.
 */

public interface ITableViewListener {

    void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int
            row);

    void onCellDoubleClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int
            row);

    void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int
            row);

    void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column);

    void onColumnHeaderDoubleClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column);

    void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column);

    void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row);

    void onRowHeaderDoubleClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row);

    void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int
            row);

}
