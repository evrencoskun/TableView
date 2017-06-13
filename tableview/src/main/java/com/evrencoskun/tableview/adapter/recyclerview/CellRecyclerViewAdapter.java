package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.ITableAdapter;
import com.evrencoskun.tableview.layoutmanager.ColumnLayoutManager;
import com.evrencoskun.tableview.listener.ColumnScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class CellRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private List<RecyclerView.Adapter> m_jAdapterList;

    private ITableAdapter m_iTableAdapter;

    private ColumnScrollListener m_jColumnScrollListener;

    public CellRecyclerViewAdapter(Context context, List p_jItemList, ITableAdapter
            p_iTableAdapter) {
        super(context, p_jItemList);
        this.m_iTableAdapter = p_iTableAdapter;

        // Initialize the array
        m_jAdapterList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a RecyclerView as a Row of the CellRecyclerView
        RecyclerView jRecyclerView = new RecyclerView(m_jContext);

        // Set the custom layout manager that helps the fit width of the cell and column header
        ColumnLayoutManager layoutManager = new ColumnLayoutManager(m_jContext, m_iTableAdapter
                .getColumnHeaderLayoutManager());
        layoutManager.setOrientation(ColumnLayoutManager.HORIZONTAL);
        jRecyclerView.setLayoutManager(layoutManager);

        // Add scroll listener to be able to scroll all rows synchrony.
        if (m_jColumnScrollListener != null) {
            jRecyclerView.removeOnScrollListener(m_jColumnScrollListener);
            jRecyclerView.addOnScrollListener(m_jColumnScrollListener);
        }

        return new CellColumnViewHolder(jRecyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof CellColumnViewHolder)) {
            return;
        }

        CellColumnViewHolder viewHolder = (CellColumnViewHolder) holder;
        // Set adapter to the RecyclerView
        List<C> rowList = (List) m_jItemList.get(position);

        CellColumnRecyclerViewAdapter viewAdapter = new CellColumnRecyclerViewAdapter(m_jContext,
                rowList, m_iTableAdapter, position);
        viewHolder.m_jRecyclerView.setAdapter(viewAdapter);

        // Add the adapter to the list
        m_jAdapterList.add(viewAdapter);
    }

    static class CellColumnViewHolder extends RecyclerView.ViewHolder {
        public final RecyclerView m_jRecyclerView;

        public CellColumnViewHolder(View itemView) {
            super(itemView);
            m_jRecyclerView = (RecyclerView) itemView;
        }
    }

    public void notifyCellDataSetChanged() {
        if (m_jAdapterList != null && !m_jAdapterList.isEmpty()) {
            for (RecyclerView.Adapter adapter : m_jAdapterList) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void setColumnScrollListener(ColumnScrollListener p_jScrollListener) {
        m_jColumnScrollListener = p_jScrollListener;
    }
}
