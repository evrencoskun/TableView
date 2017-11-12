package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseArray;
import android.view.View;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.util.TableViewUtils;

/**
 * Created by evrencoskun on 30/07/2017.
 */

public class ColumnHeaderLayoutManager extends LinearLayoutManager {
    private SparseArray<Integer> m_aWidthList;
    private ITableView m_iTableView;

    public ColumnHeaderLayoutManager(Context context, ITableView p_iTableView) {
        super(context);
        m_aWidthList = new SparseArray<>();
        m_iTableView = p_iTableView;

        this.setOrientation(ColumnHeaderLayoutManager.HORIZONTAL);
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
        int nCacheWidth = getCacheWidth(nPosition);

        // If the width value of the cell has already calculated, then set the value
        if (nCacheWidth != -1) {
            TableViewUtils.setWidth(child, nCacheWidth);
        } else {
            super.measureChild(child, widthUsed, heightUsed);
        }
    }


    public void setCacheWidth(int p_nPosition, int p_nWidth) {
        m_aWidthList.put(p_nPosition, p_nWidth);
    }

    public int getCacheWidth(int p_nPosition) {
        Integer nCachedWidth = m_aWidthList.get(p_nPosition);
        if (nCachedWidth != null) {
            return m_aWidthList.get(p_nPosition);
        }
        return -1;
    }

    public int getFirstItemLeft() {
        View firstColumnHeader = findViewByPosition(findFirstVisibleItemPosition());
        return firstColumnHeader.getLeft();
    }

    /**
     * Helps to recalculate the width value of the cell that is located in given position.
     */
    public void removeCachedWidth(int p_nPosition) {
        m_aWidthList.remove(p_nPosition);
    }


    public void customRequestLayout() {
        int nLeft = getFirstItemLeft();
        int nRight;
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {
            nRight = nLeft + getCacheWidth(i);

            View columnHeader = findViewByPosition(i);
            columnHeader.setLeft(nLeft);
            columnHeader.setRight(nRight);

            layoutDecoratedWithMargins(columnHeader, columnHeader.getLeft(), columnHeader.getTop
                    (), columnHeader.getRight(), columnHeader.getBottom());

            nLeft = nRight + 1;
        }
    }
}
