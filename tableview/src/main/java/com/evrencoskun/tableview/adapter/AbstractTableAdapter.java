/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.evrencoskun.tableview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.IRow;
import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.sort.ISortableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public abstract class AbstractTableAdapter<CH, RH, C extends ISortableModel> implements ITableAdapter<CH, RH, C> {

    private final String tag = getClass().getSimpleName();
    private int mRowHeaderWidth;
    private int mColumnHeaderHeight;

    private ColumnHeaderRecyclerViewAdapter<CH> mColumnHeaderRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter<RH> mRowHeaderRecyclerViewAdapter;
    private CellRecyclerViewAdapter<C> mCellRecyclerViewAdapter;
    private View mCornerView;

    protected List<CH> mColumnHeaderItems;
    protected List<RH> mRowHeaderItems;
    protected List<IRow<C>> mCellItems;

    private ITableView mTableView;
    private List<AdapterDataSetChangedListener<CH, RH, C>> dataSetChangedListeners;

    public void setTableView(@NonNull ITableView tableView) {
        mTableView = tableView;
        initialize();
    }

    private void initialize() {
        Context context = mTableView.getContext();

        // Create Column header RecyclerView Adapter
        mColumnHeaderRecyclerViewAdapter = new ColumnHeaderRecyclerViewAdapter<>(context,
                mColumnHeaderItems, this);

        // Create Row Header RecyclerView Adapter
        mRowHeaderRecyclerViewAdapter = new RowHeaderRecyclerViewAdapter<>(context,
                mRowHeaderItems, this);

        // Create Cell RecyclerView Adapter
        mCellRecyclerViewAdapter = new CellRecyclerViewAdapter<>(context, mCellItems, mTableView);
    }

    public void setColumnHeaderItems(@Nullable List<CH> columnHeaderItems) {
        if (columnHeaderItems == null) {
            return;
        }

        mColumnHeaderItems = columnHeaderItems;
        // Invalidate the cached widths for letting the view measure the cells width
        // from scratch.
        mTableView.getColumnHeaderLayoutManager().clearCachedWidths();
        // Set the items to the adapter
        mColumnHeaderRecyclerViewAdapter.setItems(mColumnHeaderItems);
        dispatchColumnHeaderDataSetChangesToListeners(columnHeaderItems);
    }

    public void setRowHeaderItems(@Nullable List<RH> rowHeaderItems) {
        if (rowHeaderItems == null) {
            return;
        }

        mRowHeaderItems = rowHeaderItems;

        // Set the items to the adapter
        mRowHeaderRecyclerViewAdapter.setItems(mRowHeaderItems);
        dispatchRowHeaderDataSetChangesToListeners(mRowHeaderItems);
    }

    public void setCellItems(@Nullable List<IRow<C>> cellItems) {
        if (cellItems == null) {
            return;
        }

        mCellItems = cellItems;
        // Invalidate the cached widths for letting the view measure the cells width
        // from scratch.
        mTableView.getCellLayoutManager().clearCachedWidths();
        // Set the items to the adapter
        mCellRecyclerViewAdapter.setItems(mCellItems);
        dispatchCellDataSetChangesToListeners(mCellItems);
    }

    public void setAllItems(
            @Nullable List<CH> columnHeaderItems,
            @Nullable List<RH> rowHeaderItems,
            @Nullable List<IRow<C>> cellItems
    ) {
        // Set all items
        setColumnHeaderItems(columnHeaderItems);
        setRowHeaderItems(rowHeaderItems);
        setCellItems(cellItems);

        // Control corner view
        updateCornerViewState(columnHeaderItems, rowHeaderItems);
    }

    private void updateCornerViewState(
            @Nullable List<CH> columnHeaderItems,
            @Nullable List<RH> rowHeaderItems
    ) {
        boolean hasColumnHeaders = columnHeaderItems != null && !columnHeaderItems.isEmpty();
        boolean hasRowHeaders = rowHeaderItems != null && !rowHeaderItems.isEmpty();
        boolean showCornerView = mTableView != null && mTableView.getShowCornerView();
        boolean needCornerSpace = hasColumnHeaders && (hasRowHeaders || showCornerView);

        // Create the corner view if we need it
        if (mCornerView == null && needCornerSpace) {
            // No TableView is associated with this Adapter, so we can't create the corner view
            if (mTableView == null) {
                return;
            }

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    mRowHeaderWidth,
                    mColumnHeaderHeight,
                    mTableView.getGravity()
            );

            // Create corner view
            mCornerView = onCreateCornerView((ViewGroup) mTableView);

            // Set the corner location
            mTableView.addView(mCornerView, layoutParams);
        }

        // We don't have any corner view to update
        if (mCornerView == null) {
            return;
        }

        if (needCornerSpace) {
            mCornerView.setVisibility(View.VISIBLE);
        } else {
            mCornerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }

    @Nullable
    @Override
    public View getCornerView() {
        return mCornerView;
    }

    public ColumnHeaderRecyclerViewAdapter getColumnHeaderRecyclerViewAdapter() {
        return mColumnHeaderRecyclerViewAdapter;
    }

    public RowHeaderRecyclerViewAdapter getRowHeaderRecyclerViewAdapter() {
        return mRowHeaderRecyclerViewAdapter;
    }

    public CellRecyclerViewAdapter<C> getCellRecyclerViewAdapter() {
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

    @Nullable
    public CH getColumnHeaderItem(int columnPosition) {
        int size = (mColumnHeaderItems == null) ? 0 : mColumnHeaderItems.size();
        if (columnPosition < 0 || columnPosition >= size) {
            Log.i(tag,"getColumnHeaderItem(col=" + columnPosition +
                    ") : Illegal columnPosition allowed 0 .. " + size);
            return null;
        }
        return mColumnHeaderItems.get(columnPosition);
    }

    @Nullable
    public RH getRowHeaderItem(int rowPosition) {
        int size = (mRowHeaderItems == null) ? 0 : mRowHeaderItems.size();
        if (rowPosition < 0 || rowPosition >= size) {
            Log.i(tag,"getRowHeaderItem(row=" + rowPosition +
                    ") : Illegal rowPosition allowed 0 .. " + size);

            return null;
        }
        return mRowHeaderItems.get(rowPosition);
    }

    /**
     * gets data from model column/row (and not from visibile column/row
     * that may not match current filter/sorting)
     * @param columnPosition
     * @param rowPosition
     */
    @Deprecated
    @Nullable
    public C getCellItem(int columnPosition, int rowPosition) {
        IRow<C> row = getRowFromModel(rowPosition);
        if (row == null) {
            return null;
        }
        int size = row.size();
        if (columnPosition < 0 || columnPosition >= size) {
            Log.i(tag,"getCellItem(col=" + columnPosition +
                    ", row=" + rowPosition +
                    ") : Illegal columnPosition allowed 0 .. " + size);
            return null;
        }
        return row.get(columnPosition);
    }

    /**
     * gets data from model row (and not from visibile row
     * that may not match current filter/sorting)
     * @param rowPosition
     */
    @Deprecated
    private IRow<C> getRowFromModel(int rowPosition) {
        int size = (mCellItems == null) ? 0 : mCellItems.size();
        if (rowPosition < 0 || rowPosition >= size) {
            Log.i(tag,"getCellItems(row=" + rowPosition +
                    ") : Illegal rowPosition allowed 0 .. " + size);
            return null;
        }
        return mCellItems.get(rowPosition);
    }

    @Nullable
    public IRow<C> getCellRowItems(int rowPosition) {
        return mCellRecyclerViewAdapter.getItem(rowPosition);
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

    public void addRow(int rowPosition, @Nullable RH rowHeaderItem, @Nullable IRow<C> cellItems) {
        mCellRecyclerViewAdapter.addItem(rowPosition, cellItems);
        mRowHeaderRecyclerViewAdapter.addItem(rowPosition, rowHeaderItem);
    }

    public void addRowRange(int rowPositionStart, @Nullable List<RH> rowHeaderItem, @Nullable List<IRow<C>> cellItems) {
        mRowHeaderRecyclerViewAdapter.addItemRange(rowPositionStart, rowHeaderItem);
        mCellRecyclerViewAdapter.addItemRange(rowPositionStart, cellItems);
    }

    public void changeRowHeaderItem(int rowPosition, @Nullable RH rowHeaderModel) {
        mRowHeaderRecyclerViewAdapter.changeItem(rowPosition, rowHeaderModel);
    }

    public void changeRowHeaderItemRange(int rowPositionStart, @Nullable List<RH> rowHeaderModelList) {
        mRowHeaderRecyclerViewAdapter.changeItemRange(rowPositionStart, rowHeaderModelList);
    }

    public void changeCellItem(int columnPosition, int rowPosition, C cellModel) {
        IRow<C> cellItems = mCellRecyclerViewAdapter.getItem(rowPosition);
        if (cellItems != null && cellItems.size() > columnPosition) {
            // Update cell row items.
            cellItems.set(columnPosition, cellModel);

            mCellRecyclerViewAdapter.changeItem(rowPosition, cellItems);
        }
    }

    public void changeColumnHeader(int columnPosition, @Nullable CH columnHeaderModel) {
        mColumnHeaderRecyclerViewAdapter.changeItem(columnPosition, columnHeaderModel);
    }

    public void changeColumnHeaderRange(int columnPositionStart, @Nullable List<CH> columnHeaderModelList) {
        mColumnHeaderRecyclerViewAdapter.changeItemRange(columnPositionStart,
                columnHeaderModelList);
    }

    /**
     * This method helps to get cell item model that is located on given vertical column position.
     */
    @NonNull
    public List<C> getVerticalItemsAtColumn(int columnPosition) {
        return mCellRecyclerViewAdapter.getVerticalItemsAtColumn(columnPosition);
    }

    public void removeColumn(int columnPosition) {
        mColumnHeaderRecyclerViewAdapter.deleteItem(columnPosition);
        mCellRecyclerViewAdapter.removeColumnItems(columnPosition);
    }

    public void addColumn(int columnPosition, @Nullable CH columnHeaderItem, @NonNull List<C> cellItems) {
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

    private void dispatchColumnHeaderDataSetChangesToListeners(@NonNull List<CH> newColumnHeaderItems) {
        if (dataSetChangedListeners != null) {
            for (AdapterDataSetChangedListener<CH, RH, C> listener : dataSetChangedListeners) {
                listener.onColumnHeaderItemsChanged(newColumnHeaderItems);
            }
        }
    }

    private void dispatchRowHeaderDataSetChangesToListeners(@NonNull final List<RH> newRowHeaderItems) {
        if (dataSetChangedListeners != null) {
            for (AdapterDataSetChangedListener<CH, RH, C> listener : dataSetChangedListeners) {
                listener.onRowHeaderItemsChanged(newRowHeaderItems);
            }
        }
    }

    private void dispatchCellDataSetChangesToListeners(@NonNull List<IRow<C>> newCellItems) {
        if (dataSetChangedListeners != null) {
            for (AdapterDataSetChangedListener<CH, RH, C> listener : dataSetChangedListeners) {
                listener.onCellItemsChanged(newCellItems);
            }
        }
    }

    /**
     * Sets the listener for changes of data set on the TableView.
     *
     * @param listener The AdapterDataSetChangedListener listener.
     */
    @Override
    public void addAdapterDataSetChangedListener(@NonNull AdapterDataSetChangedListener<CH, RH, C> listener) {
        if (dataSetChangedListeners == null) {
            dataSetChangedListeners = new ArrayList<>();
        }

        dataSetChangedListeners.add(listener);
    }
}
