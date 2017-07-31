package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;

/**
 * Created by evrencoskun on 24/06/2017.
 */

public class CellLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = CellLayoutManager.class.getSimpleName();

    private ColumnHeaderLayoutManager m_jColumnHeaderLayoutManager;
    private int m_iLastItem = -1;
    private int m_iFirstItem = -1;

    public CellLayoutManager(Context context, LinearLayoutManager p_jColumnHeaderLayoutManager) {
        super(context);
        this.m_jColumnHeaderLayoutManager = (ColumnHeaderLayoutManager)
                p_jColumnHeaderLayoutManager;

        this.setMeasurementCacheEnabled(false);
    }


    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        Log.e(LOG_TAG, "onLayoutCompleted");
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        Log.e(LOG_TAG, "onLayoutChildren");
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State
            state) {
        int nScroll = super.scrollVerticallyBy(dy, recycler, state);
        if (dy != 0) {
            //fitWidthSize();
        }
        /*if (dy > 0) {
            if (m_iLastItem != findLastVisibleItemPosition()) {
                m_iFirstItem = findFirstVisibleItemPosition();
                m_iLastItem = findLastVisibleItemPosition();
                fitWidthSize();
            }
        } else if (dy < 0) {
            if (m_iFirstItem != findFirstVisibleItemPosition()) {
                m_iFirstItem = findFirstVisibleItemPosition();
                m_iLastItem = findLastVisibleItemPosition();
                fitWidthSize();
            }
        }*/
        return nScroll;
    }


    public void fitWidthSize() {
        for (int i = m_jColumnHeaderLayoutManager.findFirstVisibleItemPosition(); i <
                m_jColumnHeaderLayoutManager.findLastVisibleItemPosition() + 1; i++) {
            fitWidthSize(i);
        }
    }

    public void fitWidthSize(int p_nPosition) {
        for (int j = findFirstVisibleItemPosition(); j < findLastVisibleItemPosition() + 1; j++) {

            CellRecyclerView child = (CellRecyclerView) findViewByPosition(j);
            if (child != null) {
                ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) child
                        .getLayoutManager();
                View cell = childLayoutManager.findViewByPosition(p_nPosition);
                View column = m_jColumnHeaderLayoutManager.findViewByPosition(p_nPosition);

                if (cell != null && column != null) {


                    int nCellCacheWidth = childLayoutManager.getCacheWidth(p_nPosition);
                    int nColumnCacheWidth = m_jColumnHeaderLayoutManager.getCacheWidth(p_nPosition);

                    Log.e(LOG_TAG, "x: " + p_nPosition + " y: " + j + " nCellCacheWidth: " +
                            nCellCacheWidth + " nColumnCacheWidth:" + nColumnCacheWidth + " cell " +
                            "" + "" + "" + "" + "" + "" + "width: " + cell.getWidth() + " column " +
                            "width" + " : " + column.getWidth());

                    if (cell.getWidth() != column.getWidth()) {
                        setWidth(cell, nColumnCacheWidth);
                        cell.setRight(column.getRight());
                        cell.setLeft(column.getLeft());
                        childLayoutManager.layoutDecorated(cell, cell.getLeft(), cell.getTop(),
                                cell.getRight(), cell.getBottom());
                    }
                    if (nCellCacheWidth != nColumnCacheWidth) {
                        childLayoutManager.setCacheWidth(p_nPosition, nColumnCacheWidth);
                    }
                }
            }
        }
    }

    private void setWidth(View p_jView, int p_nWidth) {
        // Change width value from params
        //RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) p_jView.getLayoutParams();
        //params.width = p_nWidth;

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) p_jView.getLayoutParams();
        params.width = p_nWidth;
        p_jView.setLayoutParams(params);
        //p_jView.setLayoutParams(params);

        //p_jView.setMinimumWidth(p_nWidth);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_nWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_jView.getMeasuredHeight(),
                View.MeasureSpec.EXACTLY);
        p_jView.measure(widthMeasureSpec, heightMeasureSpec);

        p_jView.invalidate();
        p_jView.requestLayout();
        p_jView.forceLayout();

    }


    public void fitCellColumns() {
        for (int i = 0; i < getChildCount(); i++) {
            CellRecyclerView child = (CellRecyclerView) getChildAt(i);
            if (child != null) {
                LinearLayoutManager childLayoutManager = (LinearLayoutManager) child
                        .getLayoutManager();
                for (int j = childLayoutManager.findFirstVisibleItemPosition(); j <
                        childLayoutManager.findLastVisibleItemPosition() + 1; j++) {

                    View viewHolder = childLayoutManager.findViewByPosition(j);
                    if (viewHolder != null) {
                        viewHolder.requestLayout();
                    }
                }
            }
        }
    }

    public boolean shouldFitColumns(int p_nPosition) {
        int nLastVisiblePosition = findLastVisibleItemPosition();

        CellRecyclerView cellRecyclerView = (CellRecyclerView) findViewByPosition(p_nPosition);
        CellRecyclerView lastCellRecyclerView = (CellRecyclerView) findViewByPosition
                (nLastVisiblePosition);

        if (!cellRecyclerView.isScrollOthers()) {
            if (p_nPosition == nLastVisiblePosition) {
                return true;
            } else if (lastCellRecyclerView.isScrollOthers() && p_nPosition ==
                    nLastVisiblePosition - 1) {
                return true;
            }
        }


        return false;
    }


}
