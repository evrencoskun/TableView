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

import android.util.Log;
import android.util.SparseArray;

import com.evrencoskun.tableview.ITableView;

import java.util.List;

/**
 * Created by evrencoskun on 24.12.2017.
 */

public class VisibilityHandler {
    private static final String LOG_TAG = VisibilityHandler.class.getSimpleName();

    private ITableView mTableView;
    private SparseArray<Row> mHideRowList = new SparseArray<>();
    private SparseArray<Column> mHideColumnList = new SparseArray<>();


    public VisibilityHandler(ITableView tableView) {
        this.mTableView = tableView;
    }

    public void hideRow(int row) {
        // add row the list
        mHideRowList.put(row, getRowValueFromPosition(row));

        // remove row model from adapter
        mTableView.getAdapter().removeRow(row);
    }

    public void showRow(int row) {
        showRow(row, true);
    }

    private void showRow(int row, boolean removeFromList) {
        Row hiddenRow = mHideRowList.get(row);

        if (hiddenRow != null) {
            // add row model to the adapter
            mTableView.getAdapter().addRow(row, hiddenRow.getRowHeaderModel(), hiddenRow
                    .getCellModelList());
        } else {
            Log.e(LOG_TAG, "This row is already visible.");
        }

        if (removeFromList) {
            mHideRowList.remove(row);
        }
    }

    public void clearHideRowList() {
        mHideRowList.clear();
    }


    public void showAllHiddenRows() {
        for (int i = 0; i < mHideRowList.size(); i++) {
            int row = mHideRowList.keyAt(i);
            showRow(row, false);
        }

        clearHideRowList();
    }

    public boolean isRowVisible(int row) {
        return mHideRowList.get(row) == null;
    }


    public void hideColumn(int column) {
        // add column the list
        mHideColumnList.put(column, getColumnValueFromPosition(column));

        // remove row model from adapter
        mTableView.getAdapter().removeColumn(column);
    }

    public void showColumn(int column) {
        showColumn(column, true);
    }

    private void showColumn(int column, boolean removeFromList) {
        Column hiddenColumn = mHideColumnList.get(column);

        if (hiddenColumn != null) {
            // add column model to the adapter
            mTableView.getAdapter().addColumn(column, hiddenColumn.getColumnHeaderModel(),
                    hiddenColumn.getCellModelList());
        } else {
            Log.e(LOG_TAG, "This column is already visible.");
        }

        if (removeFromList) {
            mHideColumnList.remove(column);
        }
    }

    public void clearHideColumnList() {
        mHideColumnList.clear();
    }

    public void showAllHiddenColumns() {
        for (int i = 0; i < mHideColumnList.size(); i++) {
            int column = mHideColumnList.keyAt(i);
            showColumn(column, false);
        }

        clearHideColumnList();
    }

    public boolean isColumnVisible(int column) {
        return mHideColumnList.get(column) == null;
    }


    class Row {
        private int mYPosition;
        private Object mRowHeaderModel;
        private List<Object> mCellModelList;

        public Row(int row, Object rowHeaderModel, List<Object> cellModelList) {
            this.mYPosition = row;
            this.mRowHeaderModel = rowHeaderModel;
            this.mCellModelList = cellModelList;
        }

        public int getYPosition() {
            return mYPosition;
        }

        public Object getRowHeaderModel() {
            return mRowHeaderModel;
        }

        public List<Object> getCellModelList() {
            return mCellModelList;
        }

    }

    class Column {
        private int mYPosition;
        private Object mColumnHeaderModel;
        private List<Object> mCellModelList;

        public Column(int yPosition, Object columnHeaderModel, List<Object> cellModelList) {
            this.mYPosition = yPosition;
            this.mColumnHeaderModel = columnHeaderModel;
            this.mCellModelList = cellModelList;
        }

        public int getYPosition() {
            return mYPosition;
        }

        public Object getColumnHeaderModel() {
            return mColumnHeaderModel;
        }

        public List<Object> getCellModelList() {
            return mCellModelList;
        }

    }


    private Row getRowValueFromPosition(int row) {

        Object rowHeaderModel = mTableView.getAdapter().getRowHeaderItem(row);
        List<Object> cellModelList = (List<Object>) mTableView.getAdapter().getCellRowItems(row);

        return new Row(row, rowHeaderModel, cellModelList);
    }

    private Column getColumnValueFromPosition(int column) {
        Object columnHeaderModel = mTableView.getAdapter().getColumnHeaderItem(column);
        List<Object> cellModelList = (List<Object>) mTableView.getAdapter().getCellColumnItems
                (column);

        return new Column(column, columnHeaderModel, cellModelList);
    }

    public SparseArray<Row> getHideRowList() {
        return mHideRowList;
    }

    public SparseArray<Column> getHideColumnList() {
        return mHideColumnList;
    }

    public void setHideRowList(SparseArray<Row> rowList) {
        this.mHideRowList = rowList;
    }

    public void setHideColumnList(SparseArray<Column> columnList) {
        this.mHideColumnList = columnList;
    }
}
