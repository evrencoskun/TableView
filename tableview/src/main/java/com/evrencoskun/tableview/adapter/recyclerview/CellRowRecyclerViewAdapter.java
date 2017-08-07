package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    public CellRowRecyclerViewAdapter(Context context, List p_jItemList, ITableAdapter
            p_iTableAdapter, int p_nYPosition) {
        super(context, p_jItemList);
        this.m_nYPosition = p_nYPosition;
        this.m_iTableAdapter = p_iTableAdapter;
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
        }
    }
}
