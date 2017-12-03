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
            AbstractViewHolder viewHolder = (AbstractViewHolder) m_iTableAdapter
                    .onCreateCellViewHolder(parent, viewType);

            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int p_nXPosition) {
        if (m_iTableAdapter != null) {
            AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
            Object value = getItem(p_nXPosition);

            m_iTableAdapter.onBindCellViewHolder(viewHolder, value, p_nXPosition, m_nYPosition);
        }
    }

    public int getYPosition() {
        return m_nYPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return m_iTableAdapter.getCellItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        SelectionState selectionState = m_iTableAdapter.getTableView().getSelectionHandler()
                .getCellSelectionState(holder.getAdapterPosition(), m_nYPosition);

        // Control to ignore selection color
        if (!m_iTableAdapter.getTableView().isIgnoreSelectionColors()) {

            // Change the background color of the view considering selected row/cell position.
            if (selectionState == SelectionState.SELECTED) {
                viewHolder.setBackgroundColor(m_iTableAdapter.getTableView().getSelectedColor());
            } else {
                viewHolder.setBackgroundColor(m_iTableAdapter.getTableView().getUnSelectedColor());
            }
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
            // Clear selection status of the view holder
            viewHolder.setBackgroundColor(m_iTableAdapter.getTableView().getUnSelectedColor());
        }

        viewHolder.setSelected(SelectionState.UNSELECTED);
    }

}
