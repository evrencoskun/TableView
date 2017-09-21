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

public class CellRowRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private static final String LOG_TAG = CellRowRecyclerViewAdapter.class.getSimpleName();

    private int m_nYPosition;
    private ITableAdapter m_iTableAdapter;
    private RecyclerView m_jCellRowRecyclerView;

    public CellRowRecyclerViewAdapter(Context context, List p_jItemList, ITableAdapter
            p_iTableAdapter, CellRecyclerView p_jRecyclerView, int p_nYPosition) {
        super(context, p_jItemList);
        this.m_nYPosition = p_nYPosition;
        this.m_iTableAdapter = p_iTableAdapter;
        this.m_jCellRowRecyclerView = p_jRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (m_iTableAdapter != null) {
            RecyclerView.ViewHolder viewHolder = m_iTableAdapter.onCreateCellViewHolder(parent,
                    viewType);

            // Add cell click listener
            if (m_iTableAdapter.getTableView().getTableViewListener() != null) {
                viewHolder.itemView.setOnClickListener(mItemAction);
            }
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int p_nXPosition) {
        if (m_iTableAdapter != null) {
            m_iTableAdapter.onBindCellViewHolder(holder, p_nXPosition, m_nYPosition);
        }
    }

    private View.OnClickListener mItemAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int nXPosition = m_jCellRowRecyclerView.getChildAdapterPosition(v);
            // Callback for the TableView listener
            m_iTableAdapter.getTableView().getTableViewListener().onCellClicked(nXPosition,
                    m_nYPosition);
        }
    };
}
