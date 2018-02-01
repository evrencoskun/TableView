package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.ITableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;
import com.evrencoskun.tableview.sort.ColumnSortHelper;
import com.evrencoskun.tableview.sort.SortState;

import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class ColumnHeaderRecyclerViewAdapter<CH> extends AbstractRecyclerViewAdapter<CH> {
    private static final String LOG_TAG = ColumnHeaderRecyclerViewAdapter.class.getSimpleName();

    private ITableAdapter m_iTableAdapter;
    private ColumnSortHelper mColumnSortHelper;

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
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;
        Object value = getItem(position);

        m_iTableAdapter.onBindColumnHeaderViewHolder(viewHolder, value, position);
    }

    @Override
    public int getItemViewType(int position) {
        return m_iTableAdapter.getColumnHeaderItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        AbstractViewHolder viewHolder = (AbstractViewHolder) holder;

        SelectionState selectionState = m_iTableAdapter.getTableView().getSelectionHandler()
                .getColumnSelectionState(viewHolder.getAdapterPosition());

        // Control to ignore selection color
        if (!m_iTableAdapter.getTableView().isIgnoreSelectionColors()) {

            // Change background color of the view considering it's selected state
            m_iTableAdapter.getTableView().getSelectionHandler()
                    .changeColumnBackgroundColorBySelectionStatus(viewHolder, selectionState);
        }

        // Change selection status
        viewHolder.setSelected(selectionState);

        // Control whether the TableView is sortable or not.
        if (m_iTableAdapter.getTableView().isSortable()) {
            if (viewHolder instanceof AbstractSorterViewHolder) {
                // Get its sorting state
                SortState state = getColumnSortHelper().getSortingStatus(viewHolder
                        .getAdapterPosition());
                // Fire onSortingStatusChanged
                ((AbstractSorterViewHolder) viewHolder).onSortingStatusChanged(state);
            }
        }
    }


    public ColumnSortHelper getColumnSortHelper() {
        if (mColumnSortHelper == null) {
            // It helps to store sorting state of column headers
            this.mColumnSortHelper = new ColumnSortHelper(m_iTableAdapter.getTableView()
                    .getColumnHeaderLayoutManager());
        }
        return mColumnSortHelper;
    }
}
