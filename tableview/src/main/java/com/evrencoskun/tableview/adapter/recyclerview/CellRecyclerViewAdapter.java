/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;
import com.evrencoskun.tableview.handler.ScrollHandler;
import com.evrencoskun.tableview.handler.SelectionHandler;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;
import com.evrencoskun.tableview.layoutmanager.ColumnLayoutManager;
import com.evrencoskun.tableview.listener.itemclick.CellRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class CellRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private static final String LOG_TAG = CellRecyclerViewAdapter.class.getSimpleName();

    private ITableView mTableView;
    private final RecyclerView.RecycledViewPool mRecycledViewPool;

    // This is for testing purpose
    private int mRecyclerViewId = 0;

    public CellRecyclerViewAdapter(Context context, List<C> itemList, ITableView tableView) {
        super(context, itemList);
        this.mTableView = tableView;

        // Create view pool to share Views between multiple RecyclerViews.
        mRecycledViewPool = new RecyclerView.RecycledViewPool();
        //TODO set the right value.
        //mRecycledViewPool.setMaxRecycledViews(0, 110);
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Create a RecyclerView as a Row of the CellRecyclerView
        CellRecyclerView recyclerView = new CellRecyclerView(mContext);

        // Use the same view pool
        recyclerView.setRecycledViewPool(mRecycledViewPool);

        if (mTableView.isShowHorizontalSeparators()) {
            // Add divider
            recyclerView.addItemDecoration(mTableView.getHorizontalItemDecoration());
        }

        // To get better performance for fixed size TableView
        recyclerView.setHasFixedSize(mTableView.hasFixedWidth());

        // set touch mHorizontalListener to scroll synchronously
        recyclerView.addOnItemTouchListener(mTableView.getHorizontalRecyclerViewListener());

        // Add Item click listener for cell views
        recyclerView.addOnItemTouchListener(new CellRecyclerViewItemClickListener(recyclerView,
                mTableView));

        // Set the Column layout manager that helps the fit width of the cell and column header
        // and it also helps to locate the scroll position of the horizontal recyclerView
        // which is row recyclerView
        recyclerView.setLayoutManager(new ColumnLayoutManager(mContext, mTableView));

        // Create CellRow adapter
        recyclerView.setAdapter(new CellRowRecyclerViewAdapter(mContext, mTableView));

        // This is for testing purpose to find out which recyclerView is displayed.
        recyclerView.setId(mRecyclerViewId);

        mRecyclerViewId++;

        return new CellRowViewHolder(recyclerView);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int yPosition) {
        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        CellRowRecyclerViewAdapter viewAdapter = (CellRowRecyclerViewAdapter) viewHolder
                .recyclerView.getAdapter();

        // Get the list
        List<C> rowList = (List<C>) mItemList.get(yPosition);

        // Set Row position
        viewAdapter.setYPosition(yPosition);

        // Set the list to the adapter
        viewAdapter.setItems(rowList);
    }

    @Override
    public void onViewAttachedToWindow(AbstractViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;

        ScrollHandler scrollHandler = mTableView.getScrollHandler();

        // The below code helps to display a new attached recyclerView on exact scrolled position.
        ((ColumnLayoutManager) viewHolder.recyclerView.getLayoutManager())
                .scrollToPositionWithOffset(scrollHandler.getColumnPosition(), scrollHandler
                        .getColumnPositionOffset());

        SelectionHandler selectionHandler = mTableView.getSelectionHandler();

        if (selectionHandler.isAnyColumnSelected()) {

            AbstractViewHolder cellViewHolder = (AbstractViewHolder) viewHolder.recyclerView
                    .findViewHolderForAdapterPosition(selectionHandler.getSelectedColumnPosition());

            if (cellViewHolder != null) {
                // Control to ignore selection color
                if (!mTableView.isIgnoreSelectionColors()) {
                    cellViewHolder.setBackgroundColor(mTableView.getSelectedColor());
                }
                cellViewHolder.setSelected(SelectionState.SELECTED);

            }
        } else if (selectionHandler.isRowSelected(holder.getAdapterPosition())) {
            selectionHandler.changeSelectionOfRecyclerView(viewHolder.recyclerView,
                    SelectionState.SELECTED, mTableView.getSelectedColor());
        }

    }

    @Override
    public void onViewDetachedFromWindow(AbstractViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        // Clear selection status of the view holder
        mTableView.getSelectionHandler().changeSelectionOfRecyclerView(((CellRowViewHolder)
                holder).recyclerView, SelectionState.UNSELECTED, mTableView.getUnSelectedColor());
    }

    @Override
    public void onViewRecycled(AbstractViewHolder holder) {
        super.onViewRecycled(holder);

        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        // ScrolledX should be cleared at that time. Because we need to prepare each
        // recyclerView
        // at onViewAttachedToWindow process.
        viewHolder.recyclerView.clearScrolledX();
    }

    static class CellRowViewHolder extends AbstractViewHolder {
        final CellRecyclerView recyclerView;

        CellRowViewHolder(View itemView) {
            super(itemView);
            recyclerView = (CellRecyclerView) itemView;
        }
    }

    public void notifyCellDataSetChanged() {
        CellRecyclerView[] visibleRecyclerViews = mTableView.getCellLayoutManager()
                .getVisibleCellRowRecyclerViews();

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
        CellRecyclerView[] visibleRecyclerViews = mTableView.getCellLayoutManager()
                .getVisibleCellRowRecyclerViews();

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

    public void addColumnItems(int column, List<C> cellColumnItems) {
        // It should be same size with exist model list.
        if (cellColumnItems.size() != mItemList.size() || cellColumnItems.contains(null)) {
            return;
        }

        // Firstly, add columns from visible recyclerViews.
        // To be able provide removing animation, we need to notify just for given column position.
        CellLayoutManager layoutManager = mTableView.getCellLayoutManager();
        for (int i = layoutManager.findFirstVisibleItemPosition(); i < layoutManager
                .findLastVisibleItemPosition() + 1; i++) {
            // Get the cell row recyclerView that is located on i position
            RecyclerView cellRowRecyclerView = (RecyclerView) layoutManager.findViewByPosition(i);

            // Add the item using its adapter.
            ((AbstractRecyclerViewAdapter) cellRowRecyclerView.getAdapter()).addItem(column,
                    cellColumnItems.get(i));
        }


        // Lets change the model list silently
        List<List<C>> cellItems = new ArrayList<>();
        for (int i = 0; i < mItemList.size(); i++) {
            List<C> rowList = new ArrayList<>((List<C>) mItemList.get(i));

            if (rowList.size() > column) {
                rowList.add(column, cellColumnItems.get(i));
            }

            cellItems.add(rowList);
        }

        // Change data without notifying. Because we already did for visible recyclerViews.
        setItems((List<C>) cellItems, false);
    }
}
