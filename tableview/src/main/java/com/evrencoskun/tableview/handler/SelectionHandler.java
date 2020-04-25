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
 * Refactored by cedricferry on 10/02/2018
 */

public class SelectionHandler {

    public enum SELECTION_TYPE {NONE, CELLS, ROWS, COLUMNS}

    ;


    private SELECTION_TYPE selectionType = SELECTION_TYPE.NONE;

    private boolean mMultiSelectionEnabled = false;

    private boolean shadowEnabled = true;

    @NonNull
    private ITableView mTableView;
    private AbstractViewHolder mPreviousSelectedViewHolder;
    @NonNull
    private CellRecyclerView mColumnHeaderRecyclerView;
    @NonNull
    private CellRecyclerView mRowHeaderRecyclerView;
    @NonNull
    private CellLayoutManager mCellLayoutManager;

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

    public void setSelectedColumnPosition(@Nullable AbstractViewHolder selectedViewHolder, int column) {
        setSelectedColumnPosition(column);
    }

    public void setSelectedRowPosition(AbstractViewHolder selectedViewHolder, int row) {
        setSelectedRowPosition(row);
    }

    /**
     * set selected state for one cell at (row, column) coordinates
     *
     * @param row
     * @param column
     */
    public void setSelectedCellPositions(int row, int column) {
        setSelectedCellPositions(row, column, true);
    }

    public void setSelectedCellPositions(@Nullable AbstractViewHolder selectedViewHolder, int row, int column){
        setSelectedCellPositions(row, column);
    }

    public boolean isCellSelected(int column, int row) {
        return isColumnSelected(column) && isRowSelected(row);
    }

    public boolean isRowSelected(int column) {
        return rowHasItemSelected(column);
    }

    public boolean isColumnSelected(int column) {
        return columnHasItemSelected(column);
    }

    @NonNull
    public SelectionState getCellSelectionState(int column, int row) {
        if (isCellSelected(column, row)) {
            return SelectionState.SELECTED;
        }
        return SelectionState.UNSELECTED;
    }

    public boolean isColumnShadowed(int column) {
        return columnHasItemShadowed(column);
    }

    public boolean isRowShadowed(int row) {
        return rowHasItemShadowed(row);
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

    public SelectionState getSelectionStateColumnHeader(int column) {
        ISelectableModel columnModel = (ISelectableModel) mTableView.getAdapter().getColumnHeaderRecyclerViewAdapter().getItem(column);
        return columnModel.getSelectionState();
    }

    private void setSelectionStateCell(int column, int row, SelectionState selectionState) {
        ISelectableModel cellModel = (ISelectableModel) mTableView.getAdapter().getCellRecyclerViewAdapter().getItem(row, column);
        cellModel.setSelectionState(selectionState);
    }

    private void setSelectionStateRowHeader(int row, SelectionState selectionState) {
        ISelectableModel rowModel = (ISelectableModel) mTableView.getAdapter().getRowHeaderRecyclerViewAdapter().getItem(row);
        rowModel.setSelectionState(selectionState);
    }

    private void setSelectionStateColumnHeader(int column, SelectionState selectionState) {
        ISelectableModel columnModel = (ISelectableModel) mTableView.getAdapter().getColumnHeaderRecyclerViewAdapter().getItem(column);
        columnModel.setSelectionState(selectionState);
    }

    public boolean columnHasItemSelected(int column) {
        for (Object cell : mTableView.getAdapter().getCellRecyclerViewAdapter().getColumnItems(column)) {
            if (cell instanceof ISelectableModel) {
                if (((ISelectableModel) cell).getSelectionState() == SelectionState.SELECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean rowHasItemSelected(int row) {
        for (Object cell : mTableView.getAdapter().getCellRecyclerViewAdapter().getRowItems(row)) {
            if (cell instanceof ISelectableModel) {
                if (((ISelectableModel) cell).getSelectionState() == SelectionState.SELECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean columnHasItemShadowed(int column) {
        for (Object cell : mTableView.getAdapter().getCellRecyclerViewAdapter().getColumnItems(column)) {
            if (cell instanceof ISelectableModel) {
                if (((ISelectableModel) cell).getSelectionState() == SelectionState.SHADOWED) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean rowHasItemShadowed(int row) {
        for (Object cell : mTableView.getAdapter().getCellRecyclerViewAdapter().getRowItems(row)) {
            if (cell instanceof ISelectableModel) {
                if (((ISelectableModel) cell).getSelectionState() == SelectionState.SHADOWED) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAnyColumnSelected(){
        for( int i=0; i < mTableView.getAdapter().getCellRecyclerViewAdapter().getItemCount()-1; i++) {
            if(columnHasItemSelected(i)){
                return true;
            }
        }
        return false;
    }


    public void changeRowBackgroundColorBySelectionStatus(@NonNull AbstractViewHolder viewHolder,
                                                          @NonNull SelectionState selectionState) {
        if (shadowEnabled || (!shadowEnabled && selectionState != SelectionState.SHADOWED)) {
            viewHolder.setBackgroundColor(mTableView.getAdapter().getColorForSelection(selectionState));
        }
    }

    public void changeColumnBackgroundColorBySelectionStatus(@NonNull AbstractViewHolder viewHolder,
                                                             @NonNull SelectionState selectionState) {
        if (shadowEnabled || (!shadowEnabled && selectionState != SelectionState.SHADOWED)) {
            viewHolder.setBackgroundColor(mTableView.getAdapter().getColorForSelection(selectionState));
        }
    }

    public boolean isMultiSelectionEnabled() {
        return mMultiSelectionEnabled;
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

    public void setMultiSelectionEnabled(boolean enabled) {
        this.mMultiSelectionEnabled = enabled;
    }


    /**
     * set selected state for one cell at (row, column) coordinates
     * notify can be set to true to notify adapter. However, is you
     * consider doing multiple operations, you might notify the adapter
     * manually.
     *
     * @param column
     * @param row
     * @param notify
     */
    private void setSelectedCellPositions(int row, int column, boolean notify) {
        boolean clean = false;
        if (selectionType != SELECTION_TYPE.CELLS && selectionType != SELECTION_TYPE.NONE) {
            // unselect everything
            clearAllSelection(true);
            clean = true;
        }
        selectionType = SELECTION_TYPE.CELLS;

        if (!mMultiSelectionEnabled && !clean) {
            // unselect everything first
            clearAllSelection(false);
        }


        if (getSelectionStateCell(column, row) != SelectionState.SELECTED) {
            // Select the Cell
            setSelectionStateCell(column, row, SelectionState.SELECTED);

            // Shadow column and row
            if (shadowEnabled) {
                setSelectionStateRowHeader(row, SelectionState.SHADOWED);
                setSelectionStateColumnHeader(column, SelectionState.SHADOWED);
            }
        } else {
            setSelectionStateCell(column, row, SelectionState.UNSELECTED);

            // unShadow column and row
            // check if any other cell selection is shadowing the row (in case of multiselection)
            if (!mMultiSelectionEnabled || !rowHasItemSelected(row)) {
                setSelectionStateRowHeader(row, SelectionState.UNSELECTED);
            }

            // check if any other cell selection is shadowing the column (in case of multiselection)
            if (!mMultiSelectionEnabled || !columnHasItemSelected(column)) {
                setSelectionStateColumnHeader(column, SelectionState.UNSELECTED);
            }
        }

        if (notify) {
            mTableView.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * Select the full row
     *
     * @param rowPosition
     */
    public void setSelectedRowPosition(int rowPosition) {
        boolean clean = false;
        if (selectionType != SELECTION_TYPE.ROWS && selectionType != SELECTION_TYPE.NONE) {
            // unselect everything
            clearAllSelection(true);
            clean = true;
        }
        selectionType = SELECTION_TYPE.ROWS;

        if (!mMultiSelectionEnabled && !clean) {
            // unselect everything first
            clearAllSelection(false);
        }


        for (Object cell : mTableView.getAdapter().getCellRecyclerViewAdapter().getRowItems(rowPosition)) {
            if (cell instanceof ISelectableModel) {
                ISelectableModel selectableModel = (ISelectableModel) cell;
                if (selectableModel.getSelectionState() != AbstractViewHolder.SelectionState.SELECTED) {
                    selectableModel.setSelectionState(AbstractViewHolder.SelectionState.SELECTED);
                } else {
                    selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
                }
            }
        }

        Object rowHeader = mTableView.getAdapter().getRowHeaderRecyclerViewAdapter().getItem(rowPosition);
        if (rowHeader instanceof ISelectableModel) {
            ISelectableModel selectableModel = (ISelectableModel) rowHeader;
            if (selectableModel.getSelectionState() != AbstractViewHolder.SelectionState.SELECTED) {
                selectableModel.setSelectionState(AbstractViewHolder.SelectionState.SELECTED);
            } else {
                selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
            }
        }


        if (shadowEnabled) {
            // Shadow/UnShadow row headers
            for (int columnPosition = 0; columnPosition < mTableView.getAdapter().getColumnHeaderRecyclerViewAdapter().getItemCount(); columnPosition++) {
                if (getSelectionStateRowHeader(columnPosition) != SelectionState.SHADOWED) {
                    setSelectionStateColumnHeader(columnPosition, SelectionState.SHADOWED);
                } else {
                    setSelectionStateColumnHeader(columnPosition, SelectionState.UNSELECTED);
                }
            }
        }

        mTableView.getAdapter().notifyDataSetChanged();
    }

    /**
     * Select the full column
     *
     * @param columnPosition
     */
    public void setSelectedColumnPosition(Integer columnPosition) {
        boolean clean = false;
        if (selectionType != SELECTION_TYPE.COLUMNS && selectionType != SELECTION_TYPE.NONE) {
            // unselect everything
            clearAllSelection(true);
            clean = true;
        }
        selectionType = SELECTION_TYPE.COLUMNS;


        if (!mMultiSelectionEnabled && !clean) {
            // unselect everything first
            clearAllSelection(false);
        }

        for (Object cell : mTableView.getAdapter().getCellRecyclerViewAdapter().getColumnItems(columnPosition)) {
            if (cell instanceof ISelectableModel) {
                ISelectableModel selectableModel = (ISelectableModel) cell;
                if (selectableModel.getSelectionState() != AbstractViewHolder.SelectionState.SELECTED) {
                    selectableModel.setSelectionState(AbstractViewHolder.SelectionState.SELECTED);
                } else {
                    selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
                }
            }
        }


        Object columnHeader = mTableView.getAdapter().getColumnHeaderRecyclerViewAdapter().getItem(columnPosition);
        if (columnHeader instanceof ISelectableModel) {
            ISelectableModel selectableModel = (ISelectableModel) columnHeader;
            if (selectableModel.getSelectionState() != AbstractViewHolder.SelectionState.SELECTED) {
                selectableModel.setSelectionState(AbstractViewHolder.SelectionState.SELECTED);
            } else {
                selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
            }
        }

        if (shadowEnabled) {
            // Shadow/UnShadow row headers

            for (int rowPosition = 0; rowPosition < mTableView.getAdapter().getRowHeaderRecyclerViewAdapter().getItemCount(); rowPosition++) {
                if (getSelectionStateRowHeader(rowPosition) != SelectionState.SHADOWED) {
                    setSelectionStateRowHeader(rowPosition, SelectionState.SHADOWED);
                } else {
                    setSelectionStateRowHeader(rowPosition, SelectionState.UNSELECTED);
                }
            }
        }

        mTableView.getAdapter().notifyDataSetChanged();

    }

    /**
     * clear all selection
     *
     * @param notify
     */
    public void clearAllSelection(boolean notify) {

        // clear cells
        for (int rowPosition = 0; rowPosition < mTableView.getAdapter().getRowHeaderItemCount(); rowPosition++) {
            // clear row headers
            setSelectionStateRowHeader(rowPosition, AbstractViewHolder.SelectionState.UNSELECTED);
            if (mTableView.getAdapter().getCellRowItems(rowPosition) != null) {
                for (Object cell : mTableView.getAdapter().getCellRowItems(rowPosition)) {
                    if (cell instanceof ISelectableModel) {
                        ISelectableModel selectableModel = (ISelectableModel) cell;
                        selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
                    }
                }
            }
        }

        // clear column headers
        for (int columnPosition = 0; columnPosition < mTableView.getAdapter().getColumnHeaderItemCount(); columnPosition++) {
            setSelectionStateColumnHeader(columnPosition, AbstractViewHolder.SelectionState.UNSELECTED);
        }

        if (notify) {
            mTableView.getAdapter().notifyDataSetChanged();
        }

    }

    public SelectionState getSelectionStateCell(int column, int row) {
        ISelectableModel cellModel = (ISelectableModel) mTableView.getAdapter().getCellRecyclerViewAdapter().getItem(row, column);
        return cellModel.getSelectionState();
    }

    public SelectionState getSelectionStateRowHeader(int row) {
        ISelectableModel rowModel = (ISelectableModel) mTableView.getAdapter().getRowHeaderRecyclerViewAdapter().getItem(row);
        return rowModel.getSelectionState();
    }
}
