package com.evrencoskun.tableview.handler;

import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 24.12.2017.
 */

public class VisibilityHandler {

    private ITableView mTableView;
    private List<Integer> mHideRowList = new ArrayList<>();
    private CellRecyclerView mRowHeaderRecyclerView, mCellRecyclerView;
    private AbstractTableAdapter mTableViewAdapter;

    public VisibilityHandler(ITableView pTableView) {
        this.mTableView = pTableView;
        this.mCellRecyclerView = pTableView.getCellRecyclerView();
        this.mRowHeaderRecyclerView = pTableView.getRowHeaderRecyclerView();
        this.mTableViewAdapter = pTableView.getAdapter();
    }

    public void hideRow(int pYPosition) {
        mHideRowList.add(pYPosition);
        changeRowVisibility(pYPosition, false);
    }

    public void showRow(int pYPosition) {
        if (mHideRowList.indexOf(pYPosition) > -1) {
            changeRowVisibility(pYPosition, true);
            mHideRowList.remove(pYPosition);
        }
    }

    public void clearHideRowList() {
        mHideRowList.clear();
    }

    public void showAllHiddenRows() {
        for (int i = 0; i < mHideRowList.size(); i++) {
            int nYPosition = mHideRowList.get(i);
            changeRowVisibility(nYPosition, true);
        }

        clearHideRowList();
    }

    private void changeRowVisibility(int p_nYPosition, boolean p_bIsVisible) {
        // Row Header
        AbstractViewHolder rowHeaderViewHolder = (AbstractViewHolder) mRowHeaderRecyclerView
                .findViewHolderForAdapterPosition(p_nYPosition);

        // Cell View
        AbstractViewHolder cellViewHolder = (AbstractViewHolder) mCellRecyclerView
                .findViewHolderForAdapterPosition(p_nYPosition);

        if (rowHeaderViewHolder != null && cellViewHolder != null) {
            rowHeaderViewHolder.itemView.setVisibility(p_bIsVisible ? View.VISIBLE : View.GONE);
            cellViewHolder.itemView.setVisibility(p_bIsVisible ? View.VISIBLE : View.GONE);
        }
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
}
