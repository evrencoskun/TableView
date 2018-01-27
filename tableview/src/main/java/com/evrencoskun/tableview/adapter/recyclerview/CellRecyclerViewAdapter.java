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
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;
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

    private ITableAdapter m_iTableAdapter;

    private HorizontalRecyclerViewListener m_iHorizontalListener;

    // This is for testing purpose
    private int m_nRecyclerViewId = 0;

    public CellRecyclerViewAdapter(Context context, List<C> p_jItemList, ITableAdapter
            p_iTableAdapter) {
        super(context, p_jItemList);
        this.m_iTableAdapter = p_iTableAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get TableView
        ITableView iTableView = m_iTableAdapter.getTableView();

        // Create a RecyclerView as a Row of the CellRecyclerView
        final CellRecyclerView jRecyclerView = new CellRecyclerView(mContext);

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
            ColumnLayoutManager layoutManager = new ColumnLayoutManager(mContext, iTableView,
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
        List<C> rowList = (List<C>) mItemList.get(p_nYPosition);

        CellRowRecyclerViewAdapter viewAdapter = new CellRowRecyclerViewAdapter(mContext,
                rowList, m_iTableAdapter, p_nYPosition);
        viewHolder.m_jRecyclerView.setAdapter(viewAdapter);
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

            viewHolder.m_jRecyclerView.setSelected(SelectionState.SELECTED, m_iTableAdapter
                    .getTableView().getSelectedColor(), m_iTableAdapter.getTableView()
                    .isIgnoreSelectionColors());
        }

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        // Clear selection status of the view holder
        ((CellRowViewHolder) holder).m_jRecyclerView.setSelected(SelectionState.UNSELECTED,
                m_iTableAdapter.getTableView().getUnSelectedColor(), m_iTableAdapter.getTableView
                        ().isIgnoreSelectionColors());
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
        CellRecyclerView[] visibleRecyclerViews = m_iTableAdapter.getTableView()
                .getCellLayoutManager().getVisibleCellRowRecyclerViews();

        if (visibleRecyclerViews.length > 0) {
            for (CellRecyclerView cellRowRecyclerView : visibleRecyclerViews) {
                cellRowRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } else {
            notifyDataSetChanged();
        }

    }


    /**
     * This method helps to get cell item model that is located on given column position.
     *
     * @param columnPosition
     */
    public List<C> getColumnItems(int columnPosition) {
        List<C> cellItems = new ArrayList<>();

        for (int i = 0; i < mItemList.size(); i++) {
            List<C> rowList = (List<C>) mItemList.get(i);

            if (rowList.size() > columnPosition) {
                cellItems.add(rowList.get(columnPosition));
            }
        }

        return cellItems;
    }


    public void removeColumnItems(int column) {

        // Firstly, remove columns from visible recyclerViews.
        // To be able provide removing animation, we need to notify just for given column position.
        CellRecyclerView[] visibleRecyclerViews = m_iTableAdapter.getTableView()
                .getCellLayoutManager().getVisibleCellRowRecyclerViews();

        for (CellRecyclerView cellRowRecyclerView : visibleRecyclerViews) {
            ((AbstractRecyclerViewAdapter) cellRowRecyclerView.getAdapter()).deleteItem(column);
        }


        // Lets change the model list silently
        // Create a new list which the column is already removed.
        List<List<C>> cellItems = new ArrayList<>();
        for (int i = 0; i < mItemList.size(); i++) {
            List<C> rowList = new ArrayList<>((List<C>) mItemList.get(i));

            if (rowList.size() > column) {
                rowList.remove(column);
            }

            cellItems.add(rowList);
        }

        // Change data without notifying. Because we already did for visible recyclerViews.
        setItems((List<C>) cellItems, false);
    }

    public void addColumnItems(int column, List<C> pCellItems) {
        // It should be same size with exist model list.
        if (pCellItems.size() != mItemList.size() || pCellItems.contains(null)) {
            return;
        }

        // Firstly, add columns from visible recyclerViews.
        // To be able provide removing animation, we need to notify just for given column position.
        CellLayoutManager layoutManager = m_iTableAdapter.getTableView().getCellLayoutManager();
        for (int i = layoutManager.findFirstVisibleItemPosition(); i < layoutManager
                .findLastVisibleItemPosition() + 1; i++) {
            // Get the cell row recyclerView that is located on i position
            RecyclerView cellRowRecyclerView = (RecyclerView) layoutManager.findViewByPosition(i);

            // Add the item using its adapter.
            ((AbstractRecyclerViewAdapter) cellRowRecyclerView.getAdapter()).addItem(column,
                    pCellItems.get(i));
        }


        // Lets change the model list silently
        List<List<C>> cellItems = new ArrayList<>();
        for (int i = 0; i < mItemList.size(); i++) {
            List<C> rowList = new ArrayList<>((List<C>) mItemList.get(i));

            if (rowList.size() > column) {
                rowList.add(column, pCellItems.get(i));
            }

            cellItems.add(rowList);
        }

        // Change data without notifying. Because we already did for visible recyclerViews.
        setItems((List<C>) cellItems, false);
    }

}
