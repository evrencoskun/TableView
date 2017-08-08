package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;

import java.util.HashMap;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class ColumnLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = ColumnLayoutManager.class.getSimpleName();

    private HashMap<Integer, HashMap<Integer, Integer>> m_aWidthList;

    private ITableView m_iTableView;
    private CellRecyclerView m_iCellRowRecyclerView;
    private CellRecyclerView m_iColumnHeaderRecyclerView;
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

        m_aWidthList = new HashMap<>();

        // Set default orientation
        this.setOrientation(ColumnLayoutManager.HORIZONTAL);
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        measureChild(child, widthUsed, heightUsed);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
        int nPosition = getPosition(child);

        // Get cached width size of column and cell
        int nCacheWidth = getCacheWidth(nPosition);
        int nColumnCacheWidth = m_jColumnHeaderLayoutManager.getCacheWidth(nPosition);

        if (nCacheWidth != -1 && nCacheWidth == nColumnCacheWidth) {
            // Already each of them is same width size.
            setWidth(child, nCacheWidth);
        } else {
            // Need to calculate which one has the broadest width ?
            fitWidthSize(child);
        }

        /*Log.e(LOG_TAG, "x: " + nPosition + " y: " + getRowPosition() + " nCellCacheWidth: " +
                nCacheWidth + "" + " nColumnCacheWidth: " + nColumnCacheWidth + " scrolled x : "
                + m_iCellRowRecyclerView.getScrolledX() + " width: " + child.getWidth() + " left " +
                "" + "" + "" + "" + "" + ": " + child.getLeft() + " right : " + child.getRight())
                ; */

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

    private boolean shouldFitColumns(int p_nPosition) {
        if (m_bNeedFit) {
            int nYPosition = m_iCellLayoutManager.getPosition(m_iCellRowRecyclerView);
            if (m_iCellLayoutManager.shouldFitColumns(nYPosition)) {
                if (m_nLastDx > 0) {
                    if (p_nPosition == findLastVisibleItemPosition()) {
                        return true;
                    }
                } else if (m_nLastDx < 0) {
                    if (p_nPosition == findFirstVisibleItemPosition()) {
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

    private void setWidth(View p_jView, int p_nWidth) {
        // Change width value from params
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) p_jView.getLayoutParams();
        params.width = p_nWidth;
        p_jView.setLayoutParams(params);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_nWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_jView.getMeasuredHeight(),
                View.MeasureSpec.EXACTLY);
        p_jView.measure(widthMeasureSpec, heightMeasureSpec);

        p_jView.requestLayout();
    }

    private int getWidth(View p_jView) {
        p_jView.measure(LinearLayout.LayoutParams.WRAP_CONTENT, View.MeasureSpec.makeMeasureSpec
                (p_jView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        return p_jView.getMeasuredWidth();
    }

    private void fitWidthSize(View child) {
        int nPosition = getPosition(child);
        int nCellWidth = getCacheWidth(nPosition);
        if (nCellWidth == -1) {
            nCellWidth = child.getMeasuredWidth(); // Alternatively, getWidth(child)
        }

        if (nPosition > -1) {
            View columnHeaderChild = m_jColumnHeaderLayoutManager.findViewByPosition(nPosition);
            if (columnHeaderChild != null) {

                int nColumnHeaderWidth = m_jColumnHeaderLayoutManager.getCacheWidth(nPosition);
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
                        setWidth(columnHeaderChild, nColumnHeaderWidth);
                        m_bNeedFit = true;
                    }

                    // Set the value to cache it for column header.
                    m_jColumnHeaderLayoutManager.setCacheWidth(nPosition, nColumnHeaderWidth);
                }
            }
        }

        // Set the width value to cache it for cell .
        setWidth(child, nCellWidth);
        setCacheWidth(nPosition, nCellWidth);
    }

    private int getRowPosition() {
        return m_iCellLayoutManager.getPosition(m_iCellRowRecyclerView);
    }

    public void setCacheWidth(int p_nPosition, int p_nWidth) {
        int nYPosition = getRowPosition();
        HashMap<Integer, Integer> cellRowCaches = m_aWidthList.get(nYPosition);
        if (cellRowCaches == null) {
            cellRowCaches = new HashMap<>();
        }
        cellRowCaches.put(p_nPosition, p_nWidth);
        m_aWidthList.put(nYPosition, cellRowCaches);
    }

    public int getCacheWidth(int p_nPosition) {
        int nYPosition = getRowPosition();

        HashMap<Integer, Integer> cellRowCaches = m_aWidthList.get(nYPosition);
        if (cellRowCaches != null) {
            Integer nCachedWidth = cellRowCaches.get(p_nPosition);
            if (nCachedWidth != null) {
                return cellRowCaches.get(p_nPosition);
            }
        }

        return -1;
    }

    public boolean isNeedFit() {
        return m_bNeedFit;
    }

    public void clearNeedFit() {
        m_bNeedFit = false;
    }
}
