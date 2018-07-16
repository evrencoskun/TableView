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

package com.evrencoskun.tableview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public abstract class AbstractTableAdapter<CH, RH, C> implements ITableAdapter {

    private int mRowHeaderWidth;
    private int mColumnHeaderHeight;

    protected Context mContext;
    private ColumnHeaderRecyclerViewAdapter mColumnHeaderRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter mRowHeaderRecyclerViewAdapter;
    private CellRecyclerViewAdapter mCellRecyclerViewAdapter;
    private View mCornerView;

    protected List<CH> mColumnHeaderItems;
    protected List<RH> mRowHeaderItems;
    protected List<List<C>> mCellItems;

    private ITableView mTableView;
    private List<AdapterDataSetChangedListener> dataSetChangedListeners;

    public AbstractTableAdapter(Context context) {
        mContext = context;
    }

    public void setTableView(TableView tableView) {
        mTableView = tableView;
        initialize();
    }

    private void initialize() {
        // Create Column header RecyclerView Adapter
        mColumnHeaderRecyclerViewAdapter = new ColumnHeaderRecyclerViewAdapter(mContext,
                mColumnHeaderItems, this);

        // Create Row Header RecyclerView Adapter
        mRowHeaderRecyclerViewAdapter = new RowHeaderRecyclerViewAdapter(mContext,
                mRowHeaderItems, this);

        // Create Cell RecyclerView Adapter
        mCellRecyclerViewAdapter = new CellRecyclerViewAdapter(mContext, mCellItems, mTableView);
    }

    public void setColumnHeaderItems(List<CH> columnHeaderItems) {
        if (columnHeaderItems == null) {
            return;
        }

        mColumnHeaderItems = columnHeaderItems;

        // Set the items to the adapter
        mColumnHeaderRecyclerViewAdapter.setItems(mColumnHeaderItems);
        dispatchColumnHeaderDataSetChangesToListeners(columnHeaderItems);
    }

    public void setRowHeaderItems(List<RH> rowHeaderItems) {
        if (rowHeaderItems == null) {
            return;
        }

        mRowHeaderItems = rowHeaderItems;

        // Set the items to the adapter
        mRowHeaderRecyclerViewAdapter.setItems(mRowHeaderItems);
        dispatchRowHeaderDataSetChangesToListeners(mRowHeaderItems);
    }

    public void setCellItems(List<List<C>> cellItems) {
        if (cellItems == null) {
            return;
        }

        mCellItems = cellItems;

        // Set the items to the adapter
        mCellRecyclerViewAdapter.setItems(mCellItems);
        dispatchCellDataSetChangesToListeners(mCellItems);
    }

    public void setAllItems(List<CH> columnHeaderItems, List<RH> rowHeaderItems, List<List<C>>
            cellItems) {
        // Set all items
        setColumnHeaderItems(columnHeaderItems);
        setRowHeaderItems(rowHeaderItems);
        setCellItems(cellItems);

        // Control corner view
        if ((columnHeaderItems != null && !columnHeaderItems.isEmpty()) && (rowHeaderItems !=
                null && !rowHeaderItems.isEmpty()) && (cellItems != null && !cellItems.isEmpty())
                && mTableView != null && mCornerView == null) {

            // Create corner view
            mCornerView = onCreateCornerView();
            mTableView.addView(mCornerView, new FrameLayout.LayoutParams(mRowHeaderWidth,
                    mColumnHeaderHeight));
        } else if (mCornerView != null) {

            // Change corner view visibility
            if (rowHeaderItems != null && !rowHeaderItems.isEmpty()) {
                mCornerView.setVisibility(View.VISIBLE);
            } else {
                mCornerView.setVisibility(View.GONE);
            }
        }
    }

    public View getCornerView() {
        return mCornerView;
    }

    public ColumnHeaderRecyclerViewAdapter getColumnHeaderRecyclerViewAdapter() {
        return mColumnHeaderRecyclerViewAdapter;
    }

    public RowHeaderRecyclerViewAdapter getRowHeaderRecyclerViewAdapter() {
        return mRowHeaderRecyclerViewAdapter;
    }

    public CellRecyclerViewAdapter getCellRecyclerViewAdapter() {
        return mCellRecyclerViewAdapter;
    }

    public void setRowHeaderWidth(int rowHeaderWidth) {
        this.mRowHeaderWidth = rowHeaderWidth;

        if (mCornerView != null) {
            ViewGroup.LayoutParams layoutParams = mCornerView.getLayoutParams();
            layoutParams.width = rowHeaderWidth;
        }
    }

    public void setColumnHeaderHeight(int columnHeaderHeight) {
        this.mColumnHeaderHeight = columnHeaderHeight;
    }

    public CH getColumnHeaderItem(int position) {
        if ((mColumnHeaderItems == null || mColumnHeaderItems.isEmpty()) || position < 0 ||
                position >= mColumnHeaderItems.size()) {
            return null;
        }
        return mColumnHeaderItems.get(position);
    }

    public RH getRowHeaderItem(int position) {
        if ((mRowHeaderItems == null || mRowHeaderItems.isEmpty()) || position < 0 || position >=
                mRowHeaderItems.size()) {
            return null;
        }
        return mRowHeaderItems.get(position);
    }

    public C getCellItem(int columnPosition, int rowPosition) {
        if ((mCellItems == null || mCellItems.isEmpty()) || columnPosition < 0 || rowPosition >=
                mCellItems.size() || mCellItems.get(rowPosition) == null || rowPosition < 0 ||
                columnPosition >= mCellItems.get(rowPosition).size()) {
            return null;
        }

        return mCellItems.get(rowPosition).get(columnPosition);
    }

    public List<C> getCellRowItems(int rowPosition) {
        return (List<C>) mCellRecyclerViewAdapter.getItem(rowPosition);
    }

    public void removeRow(int rowPosition) {
        mCellRecyclerViewAdapter.deleteItem(rowPosition);
        mRowHeaderRecyclerViewAdapter.deleteItem(rowPosition);
    }

    public void removeRow(int rowPosition, boolean updateRowHeader) {
        mCellRecyclerViewAdapter.deleteItem(rowPosition);

        // To be able update the row header data
        if (updateRowHeader) {
            rowPosition = mRowHeaderRecyclerViewAdapter.getItemCount() - 1;

            // Cell RecyclerView items should be notified.
            // Because, other items stores the old row position.
            mCellRecyclerViewAdapter.notifyDataSetChanged();
        }

        mRowHeaderRecyclerViewAdapter.deleteItem(rowPosition);

    }

    public void removeRowRange(int rowPositionStart, int itemCount) {
        mCellRecyclerViewAdapter.deleteItemRange(rowPositionStart, itemCount);
        mRowHeaderRecyclerViewAdapter.deleteItemRange(rowPositionStart, itemCount);
    }

    public void removeRowRange(int rowPositionStart, int itemCount, boolean updateRowHeader) {
        mCellRecyclerViewAdapter.deleteItemRange(rowPositionStart, itemCount);

        // To be able update the row header data sets
        if (updateRowHeader) {
            rowPositionStart = mRowHeaderRecyclerViewAdapter.getItemCount() - 1 - itemCount;

            // Cell RecyclerView items should be notified.
            // Because, other items stores the old row position.
            mCellRecyclerViewAdapter.notifyDataSetChanged();
        }

        mRowHeaderRecyclerViewAdapter.deleteItemRange(rowPositionStart, itemCount);
    }

    public void addRow(int rowPosition, RH rowHeaderItem, List<C> cellItems) {
        mCellRecyclerViewAdapter.addItem(rowPosition, cellItems);
        mRowHeaderRecyclerViewAdapter.addItem(rowPosition, rowHeaderItem);
    }

    public void addRowRange(int rowPositionStart, List<RH> rowHeaderItem, List<List<C>> cellItems) {
        mRowHeaderRecyclerViewAdapter.addItemRange(rowPositionStart, rowHeaderItem);
        mCellRecyclerViewAdapter.addItemRange(rowPositionStart, cellItems);
    }

    public void changeRowHeaderItem(int rowPosition, RH rowHeaderModel) {
        mRowHeaderRecyclerViewAdapter.changeItem(rowPosition, rowHeaderModel);
    }

    public void changeRowHeaderItemRange(int rowPositionStart, List<RH> rowHeaderModelList) {
        mRowHeaderRecyclerViewAdapter.changeItemRange(rowPositionStart, rowHeaderModelList);
    }

    public void changeCellItem(int columnPosition, int rowPosition, C cellModel) {
        List<C> cellItems = (List<C>) mCellRecyclerViewAdapter.getItem(rowPosition);
        if (cellItems != null && cellItems.size() > columnPosition) {
            // Update cell row items.
            cellItems.set(columnPosition, cellModel);

            mCellRecyclerViewAdapter.changeItem(rowPosition, cellItems);
        }
    }

    public void changeColumnHeader(int columnPosition, CH columnHeaderModel) {
        mColumnHeaderRecyclerViewAdapter.changeItem(columnPosition, columnHeaderModel);
    }

    public void changeColumnHeaderRange(int columnPositionStart, List<CH> columnHeaderModelList) {
        mColumnHeaderRecyclerViewAdapter.changeItemRange(columnPositionStart,
                columnHeaderModelList);
    }


    public List<C> getCellColumnItems(int columnPosition) {
        return mCellRecyclerViewAdapter.getColumnItems(columnPosition);
    }

    public void removeColumn(int columnPosition) {
        mColumnHeaderRecyclerViewAdapter.deleteItem(columnPosition);
        mCellRecyclerViewAdapter.removeColumnItems(columnPosition);
    }

    public void addColumn(int columnPosition, CH columnHeaderItem, List<C> cellItems) {
        mColumnHeaderRecyclerViewAdapter.addItem(columnPosition, columnHeaderItem);
        mCellRecyclerViewAdapter.addColumnItems(columnPosition, cellItems);
    }


    public final void notifyDataSetChanged() {
        mColumnHeaderRecyclerViewAdapter.notifyDataSetChanged();
        mRowHeaderRecyclerViewAdapter.notifyDataSetChanged();
        mCellRecyclerViewAdapter.notifyCellDataSetChanged();
    }

    @Override
    public ITableView getTableView() {
        return mTableView;
    }

    @SuppressWarnings("unchecked")
    private void dispatchColumnHeaderDataSetChangesToListeners(List<CH> newColumnHeaderItems) {
        if (dataSetChangedListeners != null) {
            for (AdapterDataSetChangedListener listener : dataSetChangedListeners) {
                listener.onColumnHeaderItemsChanged(newColumnHeaderItems);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void dispatchRowHeaderDataSetChangesToListeners(final List<RH> newRowHeaderItems) {
        if (dataSetChangedListeners != null) {
            for (AdapterDataSetChangedListener listener : dataSetChangedListeners) {
                listener.onRowHeaderItemsChanged(newRowHeaderItems);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void dispatchCellDataSetChangesToListeners(List<List<C>> newCellItems) {
        if (dataSetChangedListeners != null) {
            for (AdapterDataSetChangedListener listener : dataSetChangedListeners) {
                listener.onCellItemsChanged(newCellItems);
            }
        }
    }

    /**
     * Sets the listener for changes of data set on the TableView.
     *
     * @param listener The AdapterDataSetChangedListener listener.
     */
    public void addAdapterDataSetChangedListener(AdapterDataSetChangedListener listener) {
        if (dataSetChangedListeners == null) {
            dataSetChangedListeners = new ArrayList<>();
        }

        dataSetChangedListeners.add(listener);
    }
}
