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

package com.evrencoskun.tableview.handler;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;

/**
 * Created by evrencoskun on 24/10/2017.
 */

public class SelectionHandler {

    public static final int UNSELECTED_POSITION = -1;
    private int mSelectedRowPosition = UNSELECTED_POSITION;
    private int mSelectedColumnPosition = UNSELECTED_POSITION;

    private boolean shadowEnabled = true;

    @NonNull
    private final ITableView mTableView;
    private AbstractViewHolder mPreviousSelectedViewHolder;
    @NonNull
    private final CellRecyclerView mColumnHeaderRecyclerView;
    @NonNull
    private final CellRecyclerView mRowHeaderRecyclerView;
    @NonNull
    private final CellLayoutManager mCellLayoutManager;

    public SelectionHandler(@NonNull ITableView tableView) {
        this.mTableView = tableView;
        this.mColumnHeaderRecyclerView = mTableView.getColumnHeaderRecyclerView();
        this.mRowHeaderRecyclerView = mTableView.getRowHeaderRecyclerView();
        this.mCellLayoutManager = mTableView.getCellLayoutManager();
    }

    public boolean isShadowEnabled() {
        return shadowEnabled;
    }

    public void setShadowEnabled(boolean shadowEnabled) {
        this.shadowEnabled = shadowEnabled;
    }

    public void setSelectedCellPositions(@Nullable AbstractViewHolder selectedViewHolder, int column, int
            row) {
        this.setPreviousSelectedView(selectedViewHolder);

        this.mSelectedColumnPosition = column;
        this.mSelectedRowPosition = row;

        if (shadowEnabled) {
            selectedCellView();
        }
    }

    public void setSelectedColumnPosition(@Nullable AbstractViewHolder selectedViewHolder, int column) {
        this.setPreviousSelectedView(selectedViewHolder);

        this.mSelectedColumnPosition = column;

        selectedColumnHeader();

        // Set unselected others
        mSelectedRowPosition = UNSELECTED_POSITION;
    }

    public int getSelectedColumnPosition() {
        return mSelectedColumnPosition;
    }

    public void setSelectedRowPosition(@Nullable AbstractViewHolder selectedViewHolder, int row) {
        this.setPreviousSelectedView(selectedViewHolder);

        this.mSelectedRowPosition = row;

        selectedRowHeader();

        // Set unselected others
        mSelectedColumnPosition = UNSELECTED_POSITION;
    }

    public int getSelectedRowPosition() {
        return mSelectedRowPosition;
    }

    public void setPreviousSelectedView(@Nullable AbstractViewHolder viewHolder) {
        restorePreviousSelectedView();

        if (mPreviousSelectedViewHolder != null) {
            // Change color
            mPreviousSelectedViewHolder.setBackgroundColor(mTableView.getUnSelectedColor());
            // Change state
            mPreviousSelectedViewHolder.setSelected(SelectionState.UNSELECTED);
        }

        AbstractViewHolder oldViewHolder = mCellLayoutManager.getCellViewHolder
                (getSelectedColumnPosition(), getSelectedRowPosition());

        if (oldViewHolder != null) {
            // Change color
            oldViewHolder.setBackgroundColor(mTableView.getUnSelectedColor());
            // Change state
            oldViewHolder.setSelected(SelectionState.UNSELECTED);
        }

        this.mPreviousSelectedViewHolder = viewHolder;

        // Change color
        mPreviousSelectedViewHolder.setBackgroundColor(mTableView.getSelectedColor());
        // Change state
        mPreviousSelectedViewHolder.setSelected(SelectionState.SELECTED);
    }

    private void restorePreviousSelectedView() {
        if (mSelectedColumnPosition != UNSELECTED_POSITION && mSelectedRowPosition !=
                UNSELECTED_POSITION) {
            unselectedCellView();
        } else if (mSelectedColumnPosition != UNSELECTED_POSITION) {
            unselectedColumnHeader();
        } else if (mSelectedRowPosition != UNSELECTED_POSITION) {
            unselectedRowHeader();
        }
    }

    private void selectedRowHeader() {
        // Change background color of the selected cell views
        changeVisibleCellViewsBackgroundForRow(mSelectedRowPosition, true);

        // Change background color of the column headers to be shown as a shadow.
        if (shadowEnabled) {
            changeSelectionOfRecyclerView(mColumnHeaderRecyclerView, SelectionState.SHADOWED,
                    mTableView.getShadowColor());
        }
    }

    private void unselectedRowHeader() {
        changeVisibleCellViewsBackgroundForRow(mSelectedRowPosition, false);

        // Change background color of the column headers to be shown as a normal.
        changeSelectionOfRecyclerView(mColumnHeaderRecyclerView, SelectionState.UNSELECTED,
                mTableView.getUnSelectedColor());
    }

    private void selectedCellView() {
        int shadowColor = mTableView.getShadowColor();

        // Change background color of the row header which is located on Y Position of the cell
        // view.
        AbstractViewHolder rowHeader = (AbstractViewHolder) mRowHeaderRecyclerView
                .findViewHolderForAdapterPosition(mSelectedRowPosition);

        // If view is null, that means the row view holder was already recycled.
        if (rowHeader != null) {
            // Change color
            rowHeader.setBackgroundColor(shadowColor);
            // Change state
            rowHeader.setSelected(SelectionState.SHADOWED);
        }

        // Change background color of the column header which is located on X Position of the cell
        // view.
        AbstractViewHolder columnHeader = (AbstractViewHolder) mColumnHeaderRecyclerView
                .findViewHolderForAdapterPosition(mSelectedColumnPosition);

        if (columnHeader != null) {
            // Change color
            columnHeader.setBackgroundColor(shadowColor);
            // Change state
            columnHeader.setSelected(SelectionState.SHADOWED);
        }

    }

    private void unselectedCellView() {
        int unSelectedColor = mTableView.getUnSelectedColor();

        // Change background color of the row header which is located on Y Position of the cell
        // view.
        AbstractViewHolder rowHeader = (AbstractViewHolder) mRowHeaderRecyclerView
                .findViewHolderForAdapterPosition(mSelectedRowPosition);

        // If view is null, that means the row view holder was already recycled.
        if (rowHeader != null) {
            // Change color
            rowHeader.setBackgroundColor(unSelectedColor);
            // Change state
            rowHeader.setSelected(SelectionState.UNSELECTED);
        }

        // Change background color of the column header which is located on X Position of the cell
        // view.
        AbstractViewHolder columnHeader = (AbstractViewHolder) mColumnHeaderRecyclerView
                .findViewHolderForAdapterPosition(mSelectedColumnPosition);

        if (columnHeader != null) {
            // Change color
            columnHeader.setBackgroundColor(unSelectedColor);
            // Change state
            columnHeader.setSelected(SelectionState.UNSELECTED);
        }
    }

    private void selectedColumnHeader() {
        changeVisibleCellViewsBackgroundForColumn(mSelectedColumnPosition, true);

        changeSelectionOfRecyclerView(mRowHeaderRecyclerView, SelectionState.SHADOWED, mTableView
                .getShadowColor());
    }

    private void unselectedColumnHeader() {
        changeVisibleCellViewsBackgroundForColumn(mSelectedColumnPosition, false);

        changeSelectionOfRecyclerView(mRowHeaderRecyclerView, SelectionState.UNSELECTED,
                mTableView.getUnSelectedColor());
    }

    public boolean isCellSelected(int column, int row) {
        return (getSelectedColumnPosition() == column && getSelectedRowPosition() == row) ||
                isColumnSelected(column) || isRowSelected(row);
    }

    @NonNull
    public SelectionState getCellSelectionState(int column, int row) {
        if (isCellSelected(column, row)) {
            return SelectionState.SELECTED;
        }
        return SelectionState.UNSELECTED;
    }

    public boolean isColumnSelected(int column) {
        return (getSelectedColumnPosition() == column && getSelectedRowPosition() ==
                UNSELECTED_POSITION);
    }

    public boolean isColumnShadowed(int column) {
        return (getSelectedColumnPosition() == column && getSelectedRowPosition() !=
                UNSELECTED_POSITION) || (getSelectedColumnPosition() == UNSELECTED_POSITION &&
                getSelectedRowPosition() != UNSELECTED_POSITION);
    }

    public boolean isAnyColumnSelected() {
        return (getSelectedColumnPosition() != SelectionHandler.UNSELECTED_POSITION &&
                getSelectedRowPosition() == SelectionHandler.UNSELECTED_POSITION);
    }

    @NonNull
    public SelectionState getColumnSelectionState(int column) {

        if (isColumnShadowed(column)) {
            return SelectionState.SHADOWED;

        } else if (isColumnSelected(column)) {
            return SelectionState.SELECTED;

        } else {
            return SelectionState.UNSELECTED;
        }
    }

    public boolean isRowSelected(int row) {
        return (getSelectedRowPosition() == row && getSelectedColumnPosition() ==
                UNSELECTED_POSITION);
    }

    public boolean isRowShadowed(int row) {
        return (getSelectedRowPosition() == row && getSelectedColumnPosition() !=
                UNSELECTED_POSITION) || (getSelectedRowPosition() == UNSELECTED_POSITION &&
                getSelectedColumnPosition() != UNSELECTED_POSITION);
    }

    @NonNull
    public SelectionState getRowSelectionState(int row) {

        if (isRowShadowed(row)) {
            return SelectionState.SHADOWED;

        } else if (isRowSelected(row)) {
            return SelectionState.SELECTED;

        } else {
            return SelectionState.UNSELECTED;
        }
    }

    private void changeVisibleCellViewsBackgroundForRow(int row, boolean isSelected) {
        int backgroundColor = mTableView.getUnSelectedColor();
        SelectionState selectionState = SelectionState.UNSELECTED;

        if (isSelected) {
            backgroundColor = mTableView.getSelectedColor();
            selectionState = SelectionState.SELECTED;
        }

        CellRecyclerView recyclerView = (CellRecyclerView) mCellLayoutManager.findViewByPosition
                (row);

        if (recyclerView == null) {
            return;
        }

        changeSelectionOfRecyclerView(recyclerView, selectionState, backgroundColor);
    }

    private void changeVisibleCellViewsBackgroundForColumn(int column, boolean isSelected) {
        int backgroundColor = mTableView.getUnSelectedColor();
        SelectionState selectionState = SelectionState.UNSELECTED;

        if (isSelected) {
            backgroundColor = mTableView.getSelectedColor();
            selectionState = SelectionState.SELECTED;
        }


        // Get visible Cell ViewHolders by Column Position
        for (int i = mCellLayoutManager.findFirstVisibleItemPosition(); i < mCellLayoutManager
                .findLastVisibleItemPosition() + 1; i++) {

            CellRecyclerView cellRowRecyclerView = (CellRecyclerView) mCellLayoutManager
                    .findViewByPosition(i);

            AbstractViewHolder holder = (AbstractViewHolder) cellRowRecyclerView
                    .findViewHolderForAdapterPosition(column);

            if (holder != null) {
                // Get each view container of the cell view and set unselected color.
                holder.setBackgroundColor(backgroundColor);

                // Change selection status of the view holder
                holder.setSelected(selectionState);
            }
        }
    }

    public void changeRowBackgroundColorBySelectionStatus(@NonNull AbstractViewHolder viewHolder,
                                                          @NonNull SelectionState selectionState) {
        if (shadowEnabled && selectionState == SelectionState.SHADOWED) {
            viewHolder.setBackgroundColor(mTableView.getShadowColor());

        } else if (selectionState == SelectionState.SELECTED) {
            viewHolder.setBackgroundColor(mTableView.getSelectedColor());

        } else {
            viewHolder.setBackgroundColor(mTableView.getUnSelectedColor());
        }
    }

    public void changeColumnBackgroundColorBySelectionStatus(@NonNull AbstractViewHolder viewHolder,
                                                             @NonNull SelectionState selectionState) {
        if (shadowEnabled && selectionState == SelectionState.SHADOWED) {
            viewHolder.setBackgroundColor(mTableView.getShadowColor());

        } else if (selectionState == SelectionState.SELECTED) {
            viewHolder.setBackgroundColor(mTableView.getSelectedColor());

        } else {
            viewHolder.setBackgroundColor(mTableView.getUnSelectedColor());
        }
    }

    public void changeSelectionOfRecyclerView(CellRecyclerView recyclerView, @NonNull AbstractViewHolder
            .SelectionState selectionState, @ColorInt int backgroundColor) {

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();

        for (int i = linearLayoutManager.findFirstVisibleItemPosition(); i < linearLayoutManager
                .findLastVisibleItemPosition() + 1; i++) {

            AbstractViewHolder viewHolder = (AbstractViewHolder) recyclerView
                    .findViewHolderForAdapterPosition(i);

            if (viewHolder != null) {
                if (!mTableView.isIgnoreSelectionColors()) {
                    // Change background color
                    viewHolder.setBackgroundColor(backgroundColor);
                }

                // Change selection status of the view holder
                viewHolder.setSelected(selectionState);
            }
        }
    }

    public void clearSelection() {
        unselectedRowHeader();
        unselectedCellView();
        unselectedColumnHeader();
    }

    public void setSelectedRowPosition(int row) {
        this.mSelectedRowPosition = row;
    }

    public void setSelectedColumnPosition(int column) {
        this.mSelectedColumnPosition = column;
    }

}
