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

    private ITableAdapter mTableAdapter;

    private HorizontalRecyclerViewListener mHorizontalListener;

    // This is for testing purpose
    private int mRecyclerViewId = 0;

    public CellRecyclerViewAdapter(Context context, List<C> itemList, ITableAdapter tableAdapter) {
        super(context, itemList);
        this.mTableAdapter = tableAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get TableView
        ITableView tableView = mTableAdapter.getTableView();

        // Create a RecyclerView as a Row of the CellRecyclerView
        CellRecyclerView recyclerView = new CellRecyclerView(mContext);

        if (tableView.isShowHorizontalSeparators()) {
            // Add divider
            recyclerView.addItemDecoration(tableView.getHorizontalItemDecoration());
        }

        if (tableView != null) {
            // To get better performance for fixed size TableView
            recyclerView.setHasFixedSize(tableView.hasFixedWidth());

            // set touch mHorizontalListener to scroll synchronously
            if (mHorizontalListener == null) {
                mHorizontalListener = tableView.getHorizontalRecyclerViewListener();
            }

            recyclerView.addOnItemTouchListener(mHorizontalListener);

            // Add Item click listener for cell views
            recyclerView.addOnItemTouchListener(new CellRecyclerViewItemClickListener
                    (recyclerView, tableView));

            // Set the Column layout manager that helps the fit width of the cell and column header
            // and it also helps to locate the scroll position of the horizontal recyclerView
            // which is row recyclerView
            ColumnLayoutManager layoutManager = new ColumnLayoutManager(mContext, tableView,
                    recyclerView);
            recyclerView.setLayoutManager(layoutManager);

            // This is for testing purpose to find out which recyclerView is displayed.
            recyclerView.setId(mRecyclerViewId);

            mRecyclerViewId++;
        }

        return new CellRowViewHolder(recyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int yPosition) {
        if (!(holder instanceof CellRowViewHolder)) {
            return;
        }

        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        // Set adapter to the RecyclerView
        List<C> rowList = (List<C>) mItemList.get(yPosition);

        CellRowRecyclerViewAdapter viewAdapter = new CellRowRecyclerViewAdapter(mContext,
                rowList, mTableAdapter, yPosition);
        viewHolder.m_jRecyclerView.setAdapter(viewAdapter);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        // The below code helps to display a new attached recyclerView on exact scrolled position.
        CellRowViewHolder viewHolder = (CellRowViewHolder) holder;
        ((ColumnLayoutManager) viewHolder.m_jRecyclerView.getLayoutManager())
                .scrollToPositionWithOffset(mHorizontalListener.getScrollPosition(),
                        mHorizontalListener.getScrollPositionOffset());


    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        // Clear selection status of the view holder
        ((CellRowViewHolder) holder).m_jRecyclerView.setSelected(SelectionState.UNSELECTED,
                mTableAdapter.getTableView().getUnSelectedColor(), mTableAdapter.getTableView()
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
        CellRecyclerView[] visibleRecyclerViews = mTableAdapter.getTableView()
                .getCellLayoutManager().getVisibleCellRowRecyclerViews();

        if (visibleRecyclerViews.length > 0) {
            for (CellRecyclerView cellRowRecyclerView : visibleRecyclerViews) {
                cellRowRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } else {
            notifyDataSetChanged();
        }

    }


    public C getItem(int rowPosition, int columnPosition) {
        if(mItemList.size() > rowPosition) {
            List<C> row = (List<C>) mItemList.get(rowPosition);
            if(row.size() > columnPosition) {
                return row.get(columnPosition);
            }
        }
        return null;
    }

    /**
     * This method helps to get cell item model that is located on given row position.
     *
     * @param rowPosition
     */
    public List<C> getRowItems(int rowPosition) {
        List<C> cellItems = new ArrayList<>();

        return  (List<C>) mItemList.get(rowPosition);
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
        CellRecyclerView[] visibleRecyclerViews = mTableAdapter.getTableView()
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

    public void addColumnItems(int column, List<C> cellColumnItems) {
        // It should be same size with exist model list.
        if (cellColumnItems.size() != mItemList.size() || cellColumnItems.contains(null)) {
            return;
        }

        // Firstly, add columns from visible recyclerViews.
        // To be able provide removing animation, we need to notify just for given column position.
        CellLayoutManager layoutManager = mTableAdapter.getTableView().getCellLayoutManager();
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
