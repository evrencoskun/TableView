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

package com.evrencoskun.tableview.listener;

import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by evrencoskun on 21.01.2018.
 */

public class TableViewLayoutChangeListener implements View.OnLayoutChangeListener {

    private CellRecyclerView mCellRecyclerView;
    private CellRecyclerView mColumnHeaderRecyclerView;
    private CellLayoutManager mCellLayoutManager;

    public TableViewLayoutChangeListener(ITableView tableView) {
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
