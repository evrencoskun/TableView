package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.ITableAdapter;

import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class CellRowRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private static final String LOG_TAG = CellRowRecyclerViewAdapter.class.getSimpleName();

    private int m_nYPosition;
    private ITableAdapter m_iTableAdapter;
    private LinearLayoutManager m_jCellLinearLayoutManager;

    public CellRowRecyclerViewAdapter(Context context, List p_jItemList, ITableAdapter
            p_iTableAdapter, int p_nXPosition) {
        super(context, p_jItemList);
        this.m_nYPosition = p_nXPosition;
        this.m_iTableAdapter = p_iTableAdapter;
        this.m_jCellLinearLayoutManager = (LinearLayoutManager) m_iTableAdapter.getTableView()
                .getCellRecyclerView().getLayoutManager();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (m_iTableAdapter != null) {

            return m_iTableAdapter.onCreateCellViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int p_nXPosition) {
        if (m_iTableAdapter != null) {
            m_iTableAdapter.onBindCellViewHolder(holder, p_nXPosition, m_nYPosition);

            /*if (m_jCellLinearLayoutManager.findLastVisibleItemPosition() == m_nYPosition) {
                //m_jCellLinearLayoutManager.requestLayout();
            }
            View jCellView = holder.itemView;
            View jColumnView = m_iTableAdapter.getTableView().getColumnHeaderRecyclerView()
                    .getLayoutManager().findViewByPosition(p_nXPosition);

            if (jColumnView != null) {
                controlWidth(jColumnView, jCellView);
                Log.e(LOG_TAG, p_nXPosition + " onBindViewHolder");
            }*/
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.e(LOG_TAG, holder.getAdapterPosition() + " onViewAttachedToWindow");
        /*View jCellView = holder.itemView;
        View jColumnView = m_iTableAdapter.getTableView().getColumnHeaderRecyclerView()
                .getLayoutManager().findViewByPosition(holder.getLayoutPosition());

        if (jColumnView != null && jCellView != null) {
            controlWidth(jColumnView, jCellView);
        }*/

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.e(LOG_TAG, holder.getAdapterPosition() + " onViewDetachedFromWindow");
    }

    private void controlWidth(View p_jColumnView, View p_jCellView) {
        View jColumnView = p_jColumnView;
        View jCellView = p_jCellView;

        // Get width size both of them
        int nColumnHeaderWidth = jColumnView.getWidth();
        int nCellWidth = jCellView.getWidth();

        if (nCellWidth == 0) {
            jCellView.measure(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.UNSPECIFIED);
            nCellWidth = jCellView.getMeasuredWidth();
        }
        // Control Which one has the broadest width ?
        if (nCellWidth > nColumnHeaderWidth) {
            changeColumnWidth(jColumnView, nCellWidth);
        } else if (nColumnHeaderWidth > nCellWidth) {
            changeColumnWidth(jCellView, nColumnHeaderWidth);
        }
    }

    private void changeColumnWidth(View p_jViewHolder, int p_nNewWidth) {
        ViewGroup.LayoutParams params = p_jViewHolder.getLayoutParams();
        params.width = p_nNewWidth;
        p_jViewHolder.setLayoutParams(params);
        p_jViewHolder.setMinimumWidth(p_nNewWidth);
    }

}
