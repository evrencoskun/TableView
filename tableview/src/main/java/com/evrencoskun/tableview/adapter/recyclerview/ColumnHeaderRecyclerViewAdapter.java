package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.ITableAdapter;

import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class ColumnHeaderRecyclerViewAdapter<CH> extends AbstractRecyclerViewAdapter<CH> {
    private static final String LOG_TAG = ColumnHeaderRecyclerViewAdapter.class.getSimpleName();
    private ITableAdapter m_iTableAdapter;

    public ColumnHeaderRecyclerViewAdapter(Context context, List<CH> p_jItemList, ITableAdapter
            p_iTableAdapter) {
        super(context, p_jItemList);
        this.m_iTableAdapter = p_iTableAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return m_iTableAdapter.onCreateColumnHeaderViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(LOG_TAG, position + " onBindViewHolder ");
        m_iTableAdapter.onBindColumnHeaderViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return m_iTableAdapter.getColumnHeaderItemViewType(position);
    }
}
