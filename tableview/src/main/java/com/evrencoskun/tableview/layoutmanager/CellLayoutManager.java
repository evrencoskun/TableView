package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;

/**
 * Created by evrencoskun on 24/06/2017.
 */

public class CellLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = CellLayoutManager.class.getSimpleName();

    private ColumnHeaderLayoutManager m_jColumnHeaderLayoutManager;
    private LinearLayoutManager m_jRowHeaderLayoutManager;
    private CellRecyclerView m_iRowHeaderRecyclerView;

    private int m_nLastDy = 0;
    private boolean m_bNeedSetLeft = false;
    private boolean m_bNeedFit = false;

    public CellLayoutManager(Context context, ITableView p_iTableView) {
        super(context);
        this.m_jColumnHeaderLayoutManager = p_iTableView.getColumnHeaderLayoutManager();
        this.m_jRowHeaderLayoutManager = p_iTableView.getRowHeaderLayoutManager();
        this.m_iRowHeaderRecyclerView = p_iTableView.getRowHeaderRecyclerView();

        initialize();
    }


    private void initialize() {
        this.setOrientation(VERTICAL);
        //this.setMeasurementCacheEnabled(false);
        // Add new one
    }


    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        // Remeasure the column header.
        m_jColumnHeaderLayoutManager.requestLayout();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State
            state) {
        if (m_iRowHeaderRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                !m_iRowHeaderRecyclerView.isScrollOthers()) {
            // CellRecyclerViews should be scrolled after the RowHeaderRecyclerView.
            // Because it is one of the main compared criterion to make each columns fit.
            m_iRowHeaderRecyclerView.scrollBy(0, dy);
        }

        int nScroll = super.scrollVerticallyBy(dy, recycler, state);

        // It is important to determine right position to fit all columns which are the same y pos.
        m_nLastDy = dy;
        return nScroll;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            // It is important to set it 0 to be able to know which direction is being scrolled
            m_nLastDy = 0;
        }
    }

    /**
     * This method helps to fit all columns which are displayed on screen.
     * Especially it will be called when TableView is scrolled on vertically.
     */
    public void fitWidthSize() {
        int nLeft = m_jColumnHeaderLayoutManager.getFirstItemLeft();
        for (int i = m_jColumnHeaderLayoutManager.findFirstVisibleItemPosition(); i <
                m_jColumnHeaderLayoutManager.findLastVisibleItemPosition() + 1; i++) {
            nLeft = fitSize(i, nLeft, false);
        }

        m_bNeedSetLeft = false;
    }

    /**
     * This method helps to fit a column. it will be called when TableView is scrolled on
     * horizontally.
     */
    public void fitWidthSize(int p_nPosition, boolean p_nControlXPosition) {
        fitSize(p_nPosition, -1, p_nControlXPosition);

        if (m_bNeedSetLeft && p_nControlXPosition) {
            fitWidthSize();
        }
    }


    private int fitSize(int p_nPosition, int p_nLeft, boolean p_nControlXPosition) {
        int nCellRight = -1;
        int nOffset = -1;

        int nColumnCacheWidth = m_jColumnHeaderLayoutManager.getCacheWidth(p_nPosition);
        View column = m_jColumnHeaderLayoutManager.findViewByPosition(p_nPosition);

        if (column != null) {

            // Loop for all rows which are visible.
            for (int j = findFirstVisibleItemPosition(); j < findLastVisibleItemPosition() + 1;
                 j++) {

                CellRecyclerView child = (CellRecyclerView) findViewByPosition(j);
                ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) child
                        .getLayoutManager();

                int nCellCacheWidth = childLayoutManager.getCacheWidth(p_nPosition);

                // Control whether the cell needs to be fitted by column header or not.
                if (nCellCacheWidth != nColumnCacheWidth || m_bNeedSetLeft) {

                    View cell = childLayoutManager.findViewByPosition(p_nPosition);
                    if (cell != null) {

                        if (nCellCacheWidth != nColumnCacheWidth) {
                            if (p_nControlXPosition) {
                                nOffset = Math.max(nCellCacheWidth, nColumnCacheWidth) - Math.min
                                        (nCellCacheWidth, nColumnCacheWidth);
                                /*Log.e(LOG_TAG, p_nPosition + " " + j + " + left : " + cell
                                        .getLeft() + " " + cell.getRight() + " width : " + cell
                                        .getWidth() + " scrolled X : " + child.getScrolledX() +
                                        "" + " child ");*/
                            }

                            nCellCacheWidth = nColumnCacheWidth;
                            setWidth(cell, nCellCacheWidth);
                            childLayoutManager.setCacheWidth(p_nPosition, nCellCacheWidth);
                        }

                        if (p_nLeft != -1 && cell.getLeft() != p_nLeft) {
                            cell.setLeft(p_nLeft);
                        }
                        if (cell.getWidth() != nCellCacheWidth) {
                            if (!p_nControlXPosition) {
                                nCellRight = cell.getLeft() + nCellCacheWidth + 1;
                                cell.setRight(nCellRight);

                                childLayoutManager.layoutDecoratedWithMargins(cell, cell.getLeft
                                        (), cell.getTop(), cell.getRight(), cell.getBottom());
                            }
                            m_bNeedSetLeft = true;
                        }
                    }
                } else if (p_nControlXPosition && !child.isScrollOthers() && nOffset > 0) {
                    //childLayoutManager.scrollToPositionWithOffset(p_nPosition + 1, nOffset);

                    /*Log.e(LOG_TAG, p_nPosition + " " + j + " - left : " + childLayoutManager
                            .findViewByPosition(p_nPosition).getLeft() + " " + childLayoutManager
                            .findViewByPosition(p_nPosition).getRight() + "" + " width : " +
                            childLayoutManager.findViewByPosition(p_nPosition).getWidth() + " " +
                            "scrolled X : " + child.getScrolledX() + " " + "child ");*/
                }

                /*Log.e(LOG_TAG, "x: " + p_nPosition + " y: " + j + " nCellCacheWidth: " +
                        nCellCacheWidth + "" + " nColumnCacheWidth: " + nColumnCacheWidth + " " +
                        "scrolled x: " + child.getScrolledX()); */
            }
        }
        return nCellRight;
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


    public boolean shouldFitColumns(int p_nPosition) {

        // Scrolling horizontally
        if (m_nLastDy == 0) {
            CellRecyclerView cellRecyclerView = (CellRecyclerView) findViewByPosition(p_nPosition);
            if (!cellRecyclerView.isScrollOthers()) {

                int nLastVisiblePosition = findLastVisibleItemPosition();
                CellRecyclerView lastCellRecyclerView = (CellRecyclerView) findViewByPosition
                        (nLastVisiblePosition);

                if (lastCellRecyclerView != null) {
                    if (p_nPosition == nLastVisiblePosition) {
                        return true;
                    } else if (lastCellRecyclerView.isScrollOthers() && p_nPosition ==
                            nLastVisiblePosition - 1) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) ((CellRecyclerView) child)
                .getLayoutManager();
        if (childLayoutManager.isNeedFit()) {
            m_bNeedFit = true;

            // all columns have been fitted.
            childLayoutManager.clearNeedFit();
        }

        // Why we compare the position to the last position of the row header
        // instead of its findLastVisibleItemPosition because of performance approach.
        // When this layout manager is laying out for the first time, at every position
        // this method will be called. Because first & last visible item position is
        // already same position. However, through row header is already lay out, it will
        // give us the write position value to compare it.
        if (m_bNeedFit && m_jRowHeaderLayoutManager.findLastVisibleItemPosition() == getPosition
                (child)) {
            Log.e(LOG_TAG, getPosition(child) + " fitWidthSize all vertically");
            fitWidthSize();

            m_bNeedFit = false;
        }

    }

}
