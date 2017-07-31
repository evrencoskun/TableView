package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;

/**
 * Created by evrencoskun on 30/07/2017.
 */

public class ColumnHeaderLayoutManager extends LinearLayoutManager {
    private HashMap<Integer, Integer> m_aWidthList;

    public ColumnHeaderLayoutManager(Context context) {
        super(context);
        m_aWidthList = new HashMap<>();

        this.setOrientation(ColumnHeaderLayoutManager.HORIZONTAL);
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);
        measureChild(child, widthUsed, heightUsed);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
        int nPosition = getPosition(child);
        int nCacheWidth = getCacheWidth(nPosition);
        if (nCacheWidth != -1) {
            setWidth(child, nCacheWidth);
        } else {
            super.measureChild(child, widthUsed, heightUsed);
        }
    }

    private void setWidth(View p_jView, int p_nWidth) {
        // Change width value from params
        //RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) p_jView.getLayoutParams();
        //params.width = p_nWidth;

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) p_jView.getLayoutParams();
        params.width = p_nWidth;
        p_jView.setLayoutParams(params);

        //p_jView.setMinimumWidth(p_nWidth);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_nWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_jView.getMeasuredHeight(),
                View.MeasureSpec.EXACTLY);
        p_jView.measure(widthMeasureSpec, heightMeasureSpec);

        p_jView.invalidate();
        p_jView.requestLayout();
        p_jView.forceLayout();

    }

    private int getWidth(View p_jView) {
        p_jView.measure(LinearLayout.LayoutParams.WRAP_CONTENT, View.MeasureSpec.makeMeasureSpec
                (p_jView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        return p_jView.getMeasuredWidth();
    }

    public void setCacheWidth(int p_nPosition, int p_nWidth) {
        /*if (getCacheWidth(p_nPosition) == -1) {
            m_aWidthList.add(p_nPosition, p_nWidth);
        } else {
            m_aWidthList.set(p_nPosition, p_nWidth);
        }*/
        m_aWidthList.put(p_nPosition, p_nWidth);
    }

    public int getCacheWidth(int p_nPosition) {
        Integer nCachedWidth = m_aWidthList.get(p_nPosition);
        if (nCachedWidth != null) {
            return m_aWidthList.get(p_nPosition);
        }
        //if (m_aWidthList.size() > p_nPosition && m_aWidthList.get(p_nPosition) != null) {
        //    return m_aWidthList.get(p_nPosition);
        //}
        return -1;
    }
}
