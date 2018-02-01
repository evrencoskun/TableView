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

    public boolean isRowVisible(int row) {
        return mHideRowList.get(row) == null;
    }

    private Row getRowValueFromPosition(int row) {

        Object rowHeaderModel = mTableView.getAdapter().getRowHeaderItem(row);
        List<Object> cellModelList = (List<Object>) mTableView.getAdapter().getCellRowItem(row);

        return new Row(row, rowHeaderModel, cellModelList);
    }
}
