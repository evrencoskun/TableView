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

import android.view.View;

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by evrencoskun on 21.01.2018.
 */

public class TableViewLayoutChangeListener implements View.OnLayoutChangeListener {
    @NonNull
    private final CellRecyclerView mCellRecyclerView;
    @NonNull
    private final CellRecyclerView mColumnHeaderRecyclerView;
    @NonNull
    private final CellLayoutManager mCellLayoutManager;

    public TableViewLayoutChangeListener(@NonNull ITableView tableView) {
        this.mCellRecyclerView = tableView.getCellRecyclerView();
        this.mColumnHeaderRecyclerView = tableView.getColumnHeaderRecyclerView();
        this.mCellLayoutManager = tableView.getCellLayoutManager();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int
            oldTop, int oldRight, int oldBottom) {

        if (v.isShown() && (right - left) != (oldRight - oldLeft)) {

            // Control who need the remeasure
            if (mColumnHeaderRecyclerView.getWidth() > mCellRecyclerView.getWidth()) {
                // Remeasure all nested CellRow recyclerViews
                mCellLayoutManager.remeasureAllChild();

            } else if (mCellRecyclerView.getWidth() > mColumnHeaderRecyclerView.getWidth()) {

                // It seems Column Header is needed.
                mColumnHeaderRecyclerView.getLayoutParams().width = WRAP_CONTENT;
                mColumnHeaderRecyclerView.requestLayout();
            }
        }
    }
}
