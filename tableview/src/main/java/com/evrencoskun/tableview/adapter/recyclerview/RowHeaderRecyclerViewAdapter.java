package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.ITableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;

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
        return m_iTableAdapter.onCreateRowHeaderViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
        Object value = getItem(position);

        m_iTableAdapter.onBindRowHeaderViewHolder(viewHolder, value, position);
    }

    @Override
    public int getItemViewType(int position) {
        return m_iTableAdapter.getRowHeaderItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        SelectionState selectionState = m_iTableAdapter.getTableView().getSelectionHandler()
                .getRowSelectionState(holder.getAdapterPosition());


        // Control to ignore selection color
        if (!m_iTableAdapter.getTableView().isIgnoreSelectionColors()) {
            // Change background color of the view considering it's selected state
            m_iTableAdapter.getTableView().getSelectionHandler()
                    .changeRowBackgroundColorBySelectionStatus(viewHolder, selectionState);
        }

        // Change selection status
        viewHolder.setSelected(selectionState);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        // Control to ignore selection color
        if (!m_iTableAdapter.getTableView().isIgnoreSelectionColors()) {
            viewHolder.setBackgroundColor(m_iTableAdapter.getTableView().getUnSelectedColor());
        }

        viewHolder.setSelected(SelectionState.UNSELECTED);
    }
}
