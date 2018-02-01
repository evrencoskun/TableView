package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.util.TableViewUtils;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class ColumnLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = ColumnLayoutManager.class.getSimpleName();

    private SparseArray<SparseArray<Integer>> m_aCachedWidthList;

    private ITableView m_iTableView;
    private CellRecyclerView m_iCellRowRecyclerView, m_iColumnHeaderRecyclerView;
    private ColumnHeaderLayoutManager m_jColumnHeaderLayoutManager;
    private CellLayoutManager m_iCellLayoutManager;
    private int m_nLastDx = 0;
    private boolean m_bNeedFit = false;


    public ColumnLayoutManager(Context context, ITableView p_iTableView, CellRecyclerView
            m_iCellRowRecyclerView) {
        super(context);
        this.m_iTableView = p_iTableView;
        this.m_iColumnHeaderRecyclerView = m_iTableView.getColumnHeaderRecyclerView();
        this.m_jColumnHeaderLayoutManager = m_iTableView.getColumnHeaderLayoutManager();
        this.m_iCellRowRecyclerView = m_iCellRowRecyclerView;
        this.m_iCellLayoutManager = m_iTableView.getCellLayoutManager();

        m_aCachedWidthList = new SparseArray<>();

        // Set default orientation
        this.setOrientation(ColumnLayoutManager.HORIZONTAL);
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        // If has fixed width is true, than calculation of the column width is not necessary.
        if (m_iTableView.hasFixedWidth()) {
            return;
        }

        measureChild(child, widthUsed, heightUsed);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
        // If has fixed width is true, than calculation of the column width is not necessary.
        if (m_iTableView.hasFixedWidth()) {
            super.measureChild(child, widthUsed, heightUsed);
            return;
        }

        int nPosition = getPosition(child);

        // Get cached width size of column and cell
        int nCacheWidth = getCacheWidth(nPosition);
        int nColumnCacheWidth = m_jColumnHeaderLayoutManager.getCacheWidth(nPosition);

        if (nCacheWidth != -1 && nCacheWidth == nColumnCacheWidth) {
            // Already each of them is same width size.
            TableViewUtils.setWidth(child, nCacheWidth);
        } else {
            // Need to calculate which one has the broadest width ?
            fitWidthSize(child, nPosition, nCacheWidth, nColumnCacheWidth);
        }

        // Control all of the rows which has same column position.
        if (shouldFitColumns(nPosition)) {
            if (m_nLastDx < 0) {
                Log.e(LOG_TAG, "x: " + nPosition + " y: " + getRowPosition() + " fitWidthSize " +
                        "left side");
                m_iCellLayoutManager.fitWidthSize(nPosition, true);
            } else {
                m_iCellLayoutManager.fitWidthSize(nPosition, false);
                Log.e(LOG_TAG, "x: " + nPosition + " y: " + getRowPosition() + " fitWidthSize " +
                        "right side");
            }
            m_bNeedFit = false;
        }
    }

    private boolean shouldFitColumns(int p_nXPosition) {
        if (m_bNeedFit) {
            int nYPosition = m_iCellLayoutManager.getPosition(m_iCellRowRecyclerView);
            if (m_iCellLayoutManager.shouldFitColumns(nYPosition)) {
                if (m_nLastDx > 0) {
                    if (p_nXPosition == findLastVisibleItemPosition()) {
                        return true;
                    }
                } else if (m_nLastDx < 0) {
                    if (p_nXPosition == findFirstVisibleItemPosition()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State
            state) {
        if (m_iColumnHeaderRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                m_iCellRowRecyclerView.isScrollOthers()) {
            // Every CellRowRecyclerViews should be scrolled after the ColumnHeaderRecyclerView.
            // Because it is the main compared one to make each columns fit.
            m_iColumnHeaderRecyclerView.scrollBy(dx, 0);
        }
        // It is important to determine the next attached view to fit all columns
        m_nLastDx = dx;

        return super.scrollHorizontallyBy(dx, recycler, state);
    }


    private void fitWidthSize(View child, int p_nPosition, int p_nCellCacheWidth, int
            p_nColumnCacheWidth) {

        int nCellWidth = p_nCellCacheWidth;
        if (nCellWidth == -1) {
            nCellWidth = child.getMeasuredWidth(); // Alternatively, getWidth(child)
        }

        if (p_nPosition > -1) {
            View columnHeaderChild = m_jColumnHeaderLayoutManager.findViewByPosition(p_nPosition);
            if (columnHeaderChild != null) {

                int nColumnHeaderWidth = p_nColumnCacheWidth;
                if (nColumnHeaderWidth == -1) {
                    nColumnHeaderWidth = columnHeaderChild.getMeasuredWidth(); // Alternatively,
                    // getWidth(columnHeaderChild)
                }

                if (nCellWidth != 0) {

                    if (nColumnHeaderWidth > nCellWidth) {
                        nCellWidth = nColumnHeaderWidth;

                    } else if (nCellWidth > nColumnHeaderWidth) {
                        nColumnHeaderWidth = nCellWidth;
                    }

                    // Control whether column header needs to be change interns of width
                    if (nColumnHeaderWidth != columnHeaderChild.getWidth()) {
                        TableViewUtils.setWidth(columnHeaderChild, nColumnHeaderWidth);
                        m_bNeedFit = true;
                    }

                    // Set the value to cache it for column header.
                    m_jColumnHeaderLayoutManager.setCacheWidth(p_nPosition, nColumnHeaderWidth);
                }
            }
        }

        // Set the width value to cache it for cell .
        TableViewUtils.setWidth(child, nCellWidth);
        setCacheWidth(p_nPosition, nCellWidth);
    }

    private int getRowPosition() {
        return m_iCellLayoutManager.getPosition(m_iCellRowRecyclerView);
    }

    public void setCacheWidth(int p_nPosition, int p_nWidth) {
        int nYPosition = getRowPosition();
        SparseArray<Integer> cellRowCaches = m_aCachedWidthList.get(nYPosition);
        if (cellRowCaches == null) {
            cellRowCaches = new SparseArray<>();
        }
        cellRowCaches.put(p_nPosition, p_nWidth);
        m_aCachedWidthList.put(nYPosition, cellRowCaches);
    }

    public int getCacheWidth(int p_nPosition) {
        int nYPosition = getRowPosition();

        SparseArray<Integer> cellRowCaches = m_aCachedWidthList.get(nYPosition);
        if (cellRowCaches != null) {
            Integer nCachedWidth = cellRowCaches.get(p_nPosition);
            if (nCachedWidth != null) {
                return cellRowCaches.get(p_nPosition);
            }
        }

        return -1;
    }

    public int getLastDx() {
        return m_nLastDx;
    }

    public boolean isNeedFit() {
        return m_bNeedFit;
    }

    public void clearNeedFit() {
        m_bNeedFit = false;
    }

    public AbstractViewHolder[] getVisibleViewHolders() {
        int nVisibleChildCount = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
        int nIndex = 0;

        AbstractViewHolder[] views = new AbstractViewHolder[nVisibleChildCount];
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {

            views[nIndex] = (AbstractViewHolder) m_iCellRowRecyclerView
                    .findViewHolderForAdapterPosition(i);

            nIndex++;
        }
        return views;
    }

}
