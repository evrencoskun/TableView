package com.evrencoskun.tableview.handler;

import android.support.v7.util.DiffUtil;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.sort.ColumnSortCallback;
import com.evrencoskun.tableview.sort.ColumnSortComparator;
import com.evrencoskun.tableview.sort.ITableViewComparator;
import com.evrencoskun.tableview.sort.SortOrder;

import java.util.Collections;
import java.util.List;

/**
 * Created by evrencoskun on 24.11.2017.
 */

public class ColumnSortHandler {

    private CellRecyclerViewAdapter m_iCellRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter m_iRowHeaderRecyclerViewAdapter;

    public ColumnSortHandler(ITableView p_iTableView) {
        this.m_iCellRecyclerViewAdapter = (CellRecyclerViewAdapter) p_iTableView
                .getCellRecyclerView().getAdapter();

        this.m_iRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter) p_iTableView
                .getRowHeaderRecyclerView().getAdapter();
    }

    public void sort(int p_nXPosition, SortOrder p_nSortOrder) {
        List<List<ITableViewComparator>> m_jOriginalList = m_iCellRecyclerViewAdapter.getItems();
        List<List<ITableViewComparator>> m_jSortedList = m_jOriginalList;


        if (p_nSortOrder != SortOrder.UNSORTED) {
            // Do descending / ascending sort
            Collections.sort(m_jSortedList, new ColumnSortComparator(p_nXPosition, p_nSortOrder));
        }

        // Set sorted data list
        swapItems(m_jSortedList, p_nXPosition);
    }

    public void swapItems(List<List<ITableViewComparator>> m_jNewItems, int p_nXPosition) {

        // Find the differences between old cell items and new items.
        final ColumnSortCallback diffCallback = new ColumnSortCallback(
                (List<List<ITableViewComparator>>) m_iCellRecyclerViewAdapter.getItems(),
                m_jNewItems, p_nXPosition);

        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // Set new items without calling notifyCellDataSetChanged method of CellRecyclerViewAdapter
        m_iCellRecyclerViewAdapter.setItems(m_jNewItems, false);


        // Update both cellRecyclerViewAdapter and RowHeaderRecyclerViewAdapter
        diffResult.dispatchUpdatesTo(m_iCellRecyclerViewAdapter);
        diffResult.dispatchUpdatesTo(m_iRowHeaderRecyclerViewAdapter);
    }
}
