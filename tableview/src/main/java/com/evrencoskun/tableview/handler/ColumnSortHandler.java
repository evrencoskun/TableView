package com.evrencoskun.tableview.handler;

import android.support.v7.util.DiffUtil;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.sort.ColumnSortCallback;
import com.evrencoskun.tableview.sort.ColumnSortComparator;
import com.evrencoskun.tableview.sort.ISortableModel;
import com.evrencoskun.tableview.sort.SortState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by evrencoskun on 24.11.2017.
 */

public class ColumnSortHandler {

    private CellRecyclerViewAdapter mCellRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter mRowHeaderRecyclerViewAdapter;
    private ColumnHeaderRecyclerViewAdapter mColumnHeaderRecyclerViewAdapter;

    public ColumnSortHandler(ITableView p_iTableView) {
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter) p_iTableView
                .getCellRecyclerView().getAdapter();

        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter) p_iTableView
                .getRowHeaderRecyclerView().getAdapter();

        this.mColumnHeaderRecyclerViewAdapter = (ColumnHeaderRecyclerViewAdapter) p_iTableView
                .getColumnHeaderRecyclerView().getAdapter();
    }

    public void sort(int p_nXPosition, SortState p_nSortState) {
        List<List<ISortableModel>> m_jOriginalList = mCellRecyclerViewAdapter.getItems();
        List<List<ISortableModel>> m_jSortedList = new ArrayList<>(m_jOriginalList);


        if (p_nSortState != SortState.UNSORTED) {
            // Do descending / ascending sort
            Collections.sort(m_jSortedList, new ColumnSortComparator(p_nXPosition, p_nSortState));
        }

        // Update sorting list of column headers
        mColumnHeaderRecyclerViewAdapter.getColumnSortHelper().setSortingStatus(p_nXPosition,
                p_nSortState);

        // Set sorted data list
        swapItems(m_jOriginalList, m_jSortedList, p_nXPosition);
    }

    private void swapItems(List<List<ISortableModel>> m_jOldItems, List<List<ISortableModel>>
            m_jNewItems, int p_nXPosition) {

        // Find the differences between old cell items and new items.
        final ColumnSortCallback diffCallback = new ColumnSortCallback(m_jOldItems, m_jNewItems,
                p_nXPosition);

        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // Set new items without calling notifyCellDataSetChanged method of CellRecyclerViewAdapter
        mCellRecyclerViewAdapter.setItems(m_jNewItems, false);

        diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
        diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);

    }

    public void swapItems(List<List<ISortableModel>> m_jNewItems, int p_nXPosition) {

        List<List<ISortableModel>> m_jOldItems = (List<List<ISortableModel>>)
                mCellRecyclerViewAdapter.getItems();

        // Find the differences between old cell items and new items.
        final ColumnSortCallback diffCallback = new ColumnSortCallback(m_jOldItems, m_jNewItems,
                p_nXPosition);

        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // Set new items without calling notifyCellDataSetChanged method of CellRecyclerViewAdapter
        mCellRecyclerViewAdapter.setItems(m_jNewItems, false);

        diffResult.dispatchUpdatesTo(mCellRecyclerViewAdapter);
        diffResult.dispatchUpdatesTo(mRowHeaderRecyclerViewAdapter);

    }

    public SortState getSortingStatus(int column) {
        return mColumnHeaderRecyclerViewAdapter.getColumnSortHelper().getSortingStatus(column);
    }
}
