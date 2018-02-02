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

package com.evrencoskun.tableview.handler;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.filter.FilterCallback;
import com.evrencoskun.tableview.filter.FilterHelper;

import java.util.ArrayList;
import java.util.List;

public class FilterHandler<T> {

    private CellRecyclerViewAdapter mCellRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter mRowHeaderRecyclerViewAdapter;
    private FilterHelper mFilterHelper;

    public FilterHandler(ITableView tableView) {
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter) tableView.getCellRecyclerView()
                .getAdapter();

        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter) tableView
                .getRowHeaderRecyclerView().getAdapter();
    }

    @SuppressWarnings("unchecked")
    public void filter(int column, String filter) {
        if (mFilterHelper == null) {
            mFilterHelper = new FilterHelper(mCellRecyclerViewAdapter.getItems(),
                    mRowHeaderRecyclerViewAdapter.getItems());
        }

        final List<List<IFilterableModel>> originalCellData = mFilterHelper.getCellOriginalData();
        final List<T> originalRowData = mFilterHelper.getRowOriginalData();
        List<List<IFilterableModel>> filteredCellList = new ArrayList<>();
        List<T> filteredRowList = new ArrayList<>();

        if (TextUtils.isEmpty(filter)) {
            filteredCellList.addAll(originalCellData);
            filteredRowList.addAll(originalRowData);
        } else {
            if (column == -1) {
                for (List<IFilterableModel> itemsList : originalCellData) {
                    for (IFilterableModel item : itemsList) {
                        if (item.getFilterableKeyword().toLowerCase().contains(filter.toLowerCase())) {
                            filteredCellList.add(itemsList);
                            filteredRowList.add(originalRowData.get(filteredCellList.indexOf(itemsList)));
                            break;
                        }
                    }
                }
            } else {
                for (List<IFilterableModel> itemsList : originalCellData) {
                    final IFilterableModel item = itemsList.get(column);
                    if (item.getFilterableKeyword().toLowerCase().contains(filter.toLowerCase())) {
                        filteredCellList.add(itemsList);
                        filteredRowList.add(originalRowData.get(filteredCellList.indexOf(itemsList)));
                    }
                }
            }
        }

        final FilterCallback diffCellCallback = new FilterCallback(originalCellData, filteredCellList);
        final FilterCallback diffRowCallback = new FilterCallback(originalRowData, filteredRowList);
        final DiffUtil.DiffResult diffCellResult = DiffUtil.calculateDiff(diffCellCallback);
        final DiffUtil.DiffResult diffRowResult = DiffUtil.calculateDiff(diffRowCallback);

        mCellRecyclerViewAdapter.setItems(filteredCellList, false);
        mRowHeaderRecyclerViewAdapter.setItems(filteredRowList, false);

        diffCellResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
        diffRowResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);
    }

    public void filter(String filter) {
        filter(-1, filter);
    }
}
