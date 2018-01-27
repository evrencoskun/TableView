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


    public VisibilityHandler(ITableView pTableView) {
        this.mTableView = pTableView;
    }

    public void hideRow(int pYPosition) {
        // add row the list
        mHideRowList.put(pYPosition, getRowValueFromPosition(pYPosition));

        // remove row model from adapter
        mTableView.getAdapter().removeRow(pYPosition);
    }

    public void showRow(int pYPosition) {
        showRow(pYPosition, true);
    }

    private void showRow(int pYPosition, boolean p_bRemoveFromList) {
        Row hiddenRow = mHideRowList.get(pYPosition);

        if (hiddenRow != null) {
            // add row model to the adapter
            mTableView.getAdapter().addRow(pYPosition, hiddenRow.getRowHeaderModel(), hiddenRow
                    .getCellModelList());
        } else {
            Log.e(LOG_TAG, "This row is already visible.");
        }

        if (p_bRemoveFromList) {
            mHideRowList.remove(pYPosition);
        }
    }

    public void clearHideRowList() {
        mHideRowList.clear();
    }


    public void showAllHiddenRows() {
        for (int i = 0; i < mHideRowList.size(); i++) {
            int nYPosition = mHideRowList.keyAt(i);
            showRow(nYPosition, false);
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

    private void showColumn(int column, boolean p_bRemoveFromList) {
        Column hiddenColumn = mHideColumnList.get(column);

        if (hiddenColumn != null) {
            // add column model to the adapter
            mTableView.getAdapter().addColumn(column, hiddenColumn.getColumnHeaderModel(),
                    hiddenColumn.getCellModelList());
        } else {
            Log.e(LOG_TAG, "This column is already visible.");
        }

        if (p_bRemoveFromList) {
            mHideColumnList.remove(column);
        }
    }

    public void clearHideColumnList() {
        mHideColumnList.clear();
    }

    public void showAllHiddenColumns() {
        for (int i = 0; i < mHideColumnList.size(); i++) {
            int nXPosition = mHideColumnList.keyAt(i);
            showColumn(nXPosition, false);
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

        public Row(int pYPosition, Object pRowHeaderModel, List<Object> pCellModelList) {
            this.mYPosition = pYPosition;
            this.mRowHeaderModel = pRowHeaderModel;
            this.mCellModelList = pCellModelList;
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

        public Column(int pYPosition, Object pColumnHeaderModel, List<Object> pCellModelList) {
            this.mYPosition = pYPosition;
            this.mColumnHeaderModel = pColumnHeaderModel;
            this.mCellModelList = pCellModelList;
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
}
