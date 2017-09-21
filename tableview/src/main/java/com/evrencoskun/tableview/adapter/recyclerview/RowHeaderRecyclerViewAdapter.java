package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.ITableAdapter;

import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class RowHeaderRecyclerViewAdapter<RH> extends AbstractRecyclerViewAdapter<RH> {

    private ITableAdapter m_iTableAdapter;

    public RowHeaderRecyclerViewAdapter(Context context, List<RH> p_jItemList, ITableAdapter
            p_iTableAdapter) {
        super(context, p_jItemList);
        this.m_iTableAdapter = p_iTableAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = m_iTableAdapter.onCreateRowHeaderViewHolder(parent,
                viewType);

        // Add row click listener
        if (m_iTableAdapter.getTableView().getTableViewListener() != null) {
            viewHolder.itemView.setOnClickListener(mItemAction);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        m_iTableAdapter.onBindRowHeaderViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return m_iTableAdapter.getRowHeaderItemViewType(position);
    }

    private View.OnClickListener mItemAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int nYPosition = m_iTableAdapter.getTableView().getRowHeaderRecyclerView()
                    .getChildAdapterPosition(v);

            // Callback for the TableView listener
            m_iTableAdapter.getTableView().getTableViewListener().onRowHeaderClicked(nYPosition);
        }
    };
}
