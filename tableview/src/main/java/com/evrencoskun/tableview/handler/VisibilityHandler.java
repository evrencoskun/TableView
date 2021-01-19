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

package com.evrencoskun.tableview.handler;

import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import java.util.List;

/**
 * Created by evrencoskun on 24.12.2017.
 */

public class VisibilityHandler {
    private static final String LOG_TAG = VisibilityHandler.class.getSimpleName();

    @NonNull
    private final ITableView mTableView;
    @NonNull
    private SparseArray<Row> mHideRowList = new SparseArray<>();
    @NonNull
    private SparseArray<Column> mHideColumnList = new SparseArray<>();

    public VisibilityHandler(@NonNull ITableView tableView) {
        this.mTableView = tableView;
    }

    public void hideRow(int row) {
        int viewRow = convertIndexToViewIndex(row, mHideRowList);

        if (mHideRowList.get(row) == null) {
            // add row the list
            mHideRowList.put(row, getRowValueFromPosition(row));

            // remove row model from adapter
            mTableView.getAdapter().removeRow(viewRow);
        } else {
            Log.e(LOG_TAG, "This row is already hidden.");
        }
    }

    public void showRow(int row) {
        showRow(row, true);
    }

    private void showRow(int row, boolean removeFromList) {
        Row hiddenRow = mHideRowList.get(row);

        if (hiddenRow != null) {
            // add row model to the adapter
            mTableView.getAdapter().addRow(row, hiddenRow.getRowHeaderModel(),
                    hiddenRow.getCellModelList());
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
        int viewColumn = convertIndexToViewIndex(column, mHideColumnList);

        if (mHideColumnList.get(column) == null) {
            // add column the list
            mHideColumnList.put(column, getColumnValueFromPosition(column));

            // remove row model from adapter
            mTableView.getAdapter().removeColumn(viewColumn);
        } else {
            Log.e(LOG_TAG, "This column is already hidden.");
        }
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


    /**
     * Hiding row and column process needs to consider the hidden rows or columns with a smaller
     * index to be able hide the correct index.
     *
     * @param index, stands for column or row index.
     * @param list,  stands for HideRowList or HideColumnList
     */
    private <T> int getSmallerHiddenCount(int index, SparseArray<T> list) {
        int count = 0;
        for (int i = 0; i < index && i < list.size(); i++) {
            if (list.valueAt(i) != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * It converts model index to View index considering the previous hidden rows or columns. So,
     * when we add or remove any item of RecyclerView, we need to view index.
     */
    private <T> int convertIndexToViewIndex(int index, SparseArray<T> list) {
        return index - getSmallerHiddenCount(index, list);
    }

    static class Row {
        private final int mYPosition;
        @Nullable
        private final Object mRowHeaderModel;
        @Nullable
        private final List<Object> mCellModelList;

        public Row(int row, @Nullable Object rowHeaderModel, @Nullable List<Object> cellModelList) {
            this.mYPosition = row;
            this.mRowHeaderModel = rowHeaderModel;
            this.mCellModelList = cellModelList;
        }

        public int getYPosition() {
            return mYPosition;
        }

        @Nullable
        public Object getRowHeaderModel() {
            return mRowHeaderModel;
        }

        @Nullable
        public List<Object> getCellModelList() {
            return mCellModelList;
        }

    }

    static class Column {
        private final int mYPosition;
        @Nullable
        private final Object mColumnHeaderModel;
        @NonNull
        private final List<Object> mCellModelList;

        public Column(int yPosition, @Nullable Object columnHeaderModel,
                      @NonNull List<Object> cellModelList) {
            this.mYPosition = yPosition;
            this.mColumnHeaderModel = columnHeaderModel;
            this.mCellModelList = cellModelList;
        }

        public int getYPosition() {
            return mYPosition;
        }

        @Nullable
        public Object getColumnHeaderModel() {
            return mColumnHeaderModel;
        }

        @NonNull
        public List<Object> getCellModelList() {
            return mCellModelList;
        }

    }

    @NonNull
    private Row getRowValueFromPosition(int row) {
        AbstractTableAdapter adapter = mTableView.getAdapter();
        Object rowHeaderModel = adapter.getRowHeaderItem(row);
        List<Object> cellModelList = adapter.getCellRowItems(row);

        return new Row(row, rowHeaderModel, cellModelList);
    }

    @NonNull
    private Column getColumnValueFromPosition(int column) {
        AbstractTableAdapter adapter = mTableView.getAdapter();
        Object columnHeaderModel = adapter.getColumnHeaderItem(column);
        List<Object> cellModelList = adapter.getCellColumnItems(column);

        return new Column(column, columnHeaderModel, cellModelList);
    }

    @NonNull
    public SparseArray<Row> getHideRowList() {
        return mHideRowList;
    }

    @NonNull
    public SparseArray<Column> getHideColumnList() {
        return mHideColumnList;
    }

    public void setHideRowList(@NonNull SparseArray<Row> rowList) {
        this.mHideRowList = rowList;
    }

    public void setHideColumnList(@NonNull SparseArray<Column> columnList) {
        this.mHideColumnList = columnList;
    }
}
