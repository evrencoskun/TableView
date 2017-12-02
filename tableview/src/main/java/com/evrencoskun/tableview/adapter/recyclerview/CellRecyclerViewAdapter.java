package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.ITableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;
import com.evrencoskun.tableview.handler.SelectionHandler;
import com.evrencoskun.tableview.layoutmanager.ColumnLayoutManager;
import com.evrencoskun.tableview.listener.itemclick.CellRecyclerViewItemClickListener;
import com.evrencoskun.tableview.listener.scroll.HorizontalRecyclerViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class CellRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private static final String LOG_TAG = CellRecyclerViewAdapter.class.getSimpleName();

    private List<RecyclerView.Adapter> m_jAdapterList;

    private ITableAdapter m_iTableAdapter;

    private HorizontalRecyclerViewListener m_iHorizontalListener;

    // This is for testing purpose
    private int m_nRecyclerViewId = 0;

    public CellRecyclerViewAdapter(Context context, List<C> p_jItemList, ITableAdapter
            p_iTableAdapter) {
        super(context, p_jItemList);
        this.m_iTableAdapter = p_iTableAdapter;

        // Initialize the array
        m_jAdapterList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get TableView
        ITableView iTableView = m_iTableAdapter.getTableView();

        // Create a RecyclerView as a Row of the CellRecyclerView
        final CellRecyclerView jRecyclerView = new CellRecyclerView(m_jContext);

        if (iTableView.isShowHorizontalSeparators()) {
            // Add divider
            jRecyclerView.addItemDecoration(iTableView.getHorizontalItemDecoration());
        }

        if (iTableView != null) {
            // To get better performance for fixed size TableView
            jRecyclerView.setHasFixedSize(iTableView.hasFixedWidth());

            // set touch m_iHorizontalListener to scroll synchronously
            if (m_iHorizontalListener == null) {
                m_iHorizontalListener = iTableView.getHorizontalRecyclerViewListener();
            }

            jRecyclerView.addOnItemTouchListener(m_iHorizontalListener);

            // Add Item click listener for cell views
            jRecyclerView.addOnItemTouchListener(new CellRecyclerViewItemClickListener
                    (jRecyclerView, iTableView));

            // Set the Column layout manager that helps the fit width of the cell and column header
            // and it also helps to locate the scroll position of the horizontal recyclerView
            // which is row recyclerView
            ColumnLayoutManager layoutManager = new ColumnLayoutManager(m_jContext, iTableView,
                    jRecyclerView);
            jRecyclerView.setLayoutManager(layoutManager);

            // This is for testing purpose to find out which recyclerView is displayed.
            jRecyclerView.setId(m_nRecyclerViewId);

            m_nRecyclerViewId++;
        }

        return new CellRowViewHolder(jRecyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int p_nYPosition) {
        if (!(holder instanceof CellRowViewHolder)) {
            return;
        }

        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        // Set adapter to the RecyclerView
        List<C> rowList = (List<C>) m_jItemList.get(p_nYPosition);

        CellRowRecyclerViewAdapter viewAdapter = new CellRowRecyclerViewAdapter(m_jContext,
                rowList, m_iTableAdapter, p_nYPosition);
        viewHolder.m_jRecyclerView.setAdapter(viewAdapter);

        // Add the adapter to the list
        m_jAdapterList.add(viewAdapter);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        // The below code helps to display a new attached recyclerView on exact scrolled position.
        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        ((ColumnLayoutManager) viewHolder.m_jRecyclerView.getLayoutManager())
                .scrollToPositionWithOffset(m_iHorizontalListener.getScrollPosition(),
                        m_iHorizontalListener.getScrollPositionOffset());


        SelectionHandler selectionHandler = m_iTableAdapter.getTableView().getSelectionHandler();

        if (selectionHandler.isAnyColumnSelected()) {

            AbstractViewHolder cellViewHolder = (AbstractViewHolder) ((CellRowViewHolder) holder)
                    .m_jRecyclerView.findViewHolderForAdapterPosition(selectionHandler
                            .getSelectedColumnPosition());

            if (cellViewHolder != null) {
                // Control to ignore selection color
                if (!m_iTableAdapter.getTableView().isIgnoreSelectionColors()) {
                    cellViewHolder.setBackgroundColor(m_iTableAdapter.getTableView()
                            .getSelectedColor());
                }
                cellViewHolder.setSelected(SelectionState.SELECTED);

            }
        } else if (selectionHandler.isRowSelected(holder.getAdapterPosition())) {

            viewHolder.m_jRecyclerView.setSelected(SelectionState.SELECTED, m_iTableAdapter.getTableView()
                    .getSelectedColor(), m_iTableAdapter.getTableView().isIgnoreSelectionColors());
        }

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        // Clear selection status of the view holder
        ((CellRowViewHolder) holder).m_jRecyclerView.setSelected(SelectionState.UNSELECTED, m_iTableAdapter
                .getTableView().getUnSelectedColor(), m_iTableAdapter.getTableView()
                .isIgnoreSelectionColors());
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        // ScrolledX should be cleared at that time. Because we need to prepare each
        // recyclerView
        // at onViewAttachedToWindow process.
        viewHolder.m_jRecyclerView.clearScrolledX();
    }

    static class CellRowViewHolder extends RecyclerView.ViewHolder {
        final CellRecyclerView m_jRecyclerView;

        CellRowViewHolder(View itemView) {
            super(itemView);
            m_jRecyclerView = (CellRecyclerView) itemView;
        }
    }

    public void notifyCellDataSetChanged() {
        if (m_jAdapterList != null) {
            if (m_jAdapterList.isEmpty()) {
                notifyDataSetChanged();
            } else {
                for (RecyclerView.Adapter adapter : m_jAdapterList) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
