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

import android.util.Log;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;
import com.evrencoskun.tableview.sort.ISortableModel;

/**
 * Created by evrencoskun on 24/10/2017.
 * Refactored by cedricferry on 10/02/2018
 */

public class SelectionHandler {

    public enum SELECTION_TYPE { NONE, CELLS, ROWS, COLUMNS};



    private SELECTION_TYPE selectionType = SELECTION_TYPE.NONE;

    private boolean mMultiSelectionEnabled = false;

    private boolean mShadowEnabled = true;


    private ITableView mTableView;

    public SelectionHandler(ITableView tableView) {
        this.mTableView = tableView;
    }

    public boolean isShadowEnabled() {
        return mShadowEnabled;
    }

    public void setShadowEnabled(boolean shadowEnabled) {
        this.mShadowEnabled = shadowEnabled;
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
        if(selectionType != SELECTION_TYPE.CELLS && selectionType != SELECTION_TYPE.NONE) {
            // unselect everything
            clearAllSelection(true);
            clean = true;
        }
        selectionType = SELECTION_TYPE.CELLS;

        if(!mMultiSelectionEnabled && !clean) {
            // unselect everything first
            clearAllSelection(false);
        }



        if(getSelectionStateCell(column, row) != SelectionState.SELECTED) {
            // Select the Cell
            setSelectionStateCell(column, row, SelectionState.SELECTED);

            // Shadow column and row
            if(mShadowEnabled) {
                setSelectionStateRowHeader(row, SelectionState.SHADOWED);
                setSelectionStateColumnHeader(column, SelectionState.SHADOWED);
            }
        } else {
            setSelectionStateCell(column, row, SelectionState.UNSELECTED);

            // unShadow column and row
            // check if any other cell selection is shadowing the row (in case of multiselection)
            if(!mMultiSelectionEnabled || !rowHasItemSelected(row)) {
                setSelectionStateRowHeader(row, SelectionState.UNSELECTED);
            }

            // check if any other cell selection is shadowing the column (in case of multiselection)
            if (!mMultiSelectionEnabled || !columnHasItemSelected(column)) {
                setSelectionStateColumnHeader(column, SelectionState.UNSELECTED);
            }
        }

        if(notify) {
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
        if(selectionType != SELECTION_TYPE.ROWS && selectionType != SELECTION_TYPE.NONE) {
            // unselect everything
            clearAllSelection(true);
            clean = true;
        }
        selectionType = SELECTION_TYPE.ROWS;

        if(!mMultiSelectionEnabled && !clean) {
            // unselect everything first
            clearAllSelection(false);
        }

        for(Object cell: mTableView.getAdapter().getCellRowItems(rowPosition)) {
            if(cell instanceof ISelectableModel) {
                ISelectableModel selectableModel = (ISelectableModel)cell;
                if (selectableModel.getSelectionState() != AbstractViewHolder.SelectionState.SELECTED) {
                    selectableModel.setSelectionState(AbstractViewHolder.SelectionState.SELECTED);
                } else {
                    selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
                }
            }
        }

        Object rowHeader = mTableView.getAdapter().getRowHeaderItem(rowPosition);
        if(rowHeader instanceof ISelectableModel) {
            ISelectableModel selectableModel = (ISelectableModel)rowHeader;
            if (selectableModel.getSelectionState() != AbstractViewHolder.SelectionState.SELECTED) {
                selectableModel.setSelectionState(AbstractViewHolder.SelectionState.SELECTED);
            } else {
                selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
            }
        }


        if(mShadowEnabled) {
            // Shadow/UnShadow row headers
            for(int columnPosition = 0; columnPosition < mTableView.getAdapter().getColumnHeaderItemCount(); columnPosition++) {
                if(getSelectionStateRowHeader(columnPosition) != SelectionState.SHADOWED){
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
        if(selectionType != SELECTION_TYPE.COLUMNS && selectionType != SELECTION_TYPE.NONE) {
            // unselect everything
            clearAllSelection(true);
            clean = true;
        }
        selectionType = SELECTION_TYPE.COLUMNS;


        if(!mMultiSelectionEnabled && !clean) {
            // unselect everything first
            clearAllSelection(false);
        }

        for(Object cell: mTableView.getAdapter().getCellColumnItems(columnPosition)) {
            if(cell instanceof ISelectableModel) {
                ISelectableModel selectableModel = (ISelectableModel)cell;
                if (selectableModel.getSelectionState() != AbstractViewHolder.SelectionState.SELECTED) {
                    selectableModel.setSelectionState(AbstractViewHolder.SelectionState.SELECTED);
                } else {
                    selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
                }
            }
        }

        Object columnHeader = mTableView.getAdapter().getColumnHeaderItem(columnPosition);
        if(columnHeader instanceof ISelectableModel) {
            ISelectableModel selectableModel = (ISelectableModel)columnHeader;
            if (selectableModel.getSelectionState() != AbstractViewHolder.SelectionState.SELECTED) {
                selectableModel.setSelectionState(AbstractViewHolder.SelectionState.SELECTED);
            } else {
                selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
            }
        }

        if(mShadowEnabled) {
            // Shadow/UnShadow row headers
            for(int rowPosition = 0; rowPosition < mTableView.getAdapter().getRowHeaderItemCount(); rowPosition++) {
                if(getSelectionStateRowHeader(rowPosition) != SelectionState.SHADOWED){
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
    public void clearAllSelection(boolean notify){

        // clear cells
        for(int rowPosition = 0; rowPosition < mTableView.getAdapter().getRowHeaderItemCount(); rowPosition++) {
            // clear row headers
            setSelectionStateRowHeader(rowPosition, AbstractViewHolder.SelectionState.UNSELECTED);
            if(mTableView.getAdapter().getCellRowItems(rowPosition) != null) {
                for (Object cell : mTableView.getAdapter().getCellRowItems(rowPosition)) {
                    if (cell instanceof ISelectableModel) {
                        ISelectableModel selectableModel = (ISelectableModel) cell;
                        selectableModel.setSelectionState(AbstractViewHolder.SelectionState.UNSELECTED);
                    }
                }
            }
        }

        // clear column headers
        for(int columnPosition = 0; columnPosition < mTableView.getAdapter().getColumnHeaderItemCount(); columnPosition++) {
            setSelectionStateColumnHeader(columnPosition, AbstractViewHolder.SelectionState.UNSELECTED);
        }

        if(notify) {
            mTableView.getAdapter().notifyDataSetChanged();
        }

    }


    public SelectionState getSelectionStateCell(int column, int row){
        ISelectableModel cellModel = (ISelectableModel) mTableView.getAdapter().getCellItem(column, row);
        return cellModel.getSelectionState();
    }

    public SelectionState getSelectionStateRowHeader(int row) {
        ISelectableModel rowModel = (ISelectableModel) mTableView.getAdapter().getRowHeaderItem(row);
        return rowModel.getSelectionState();
    }

    public SelectionState getSelectionStateColumnHeader(int column) {
        ISelectableModel columnModel = (ISelectableModel) mTableView.getAdapter().getColumnHeaderItem(column);
        return columnModel.getSelectionState();
    }

    private void setSelectionStateCell(int column, int row , SelectionState selectionState){
        ISelectableModel cellModel = (ISelectableModel) mTableView.getAdapter().getCellItem(column, row);
        cellModel.setSelectionState(selectionState);

        Log.d("SelectionHandler", "r"+row+"-"+column+" val: "+((ISelectableModel)cellModel).getSelectionState()+" "+((ISortableModel)cellModel).getContent());

    }

    private void setSelectionStateRowHeader(Integer row, SelectionState selectionState) {
        ISelectableModel rowModel = (ISelectableModel) mTableView.getAdapter().getRowHeaderItem(row);
        rowModel.setSelectionState(selectionState);
    }

    private void setSelectionStateColumnHeader(Integer column, SelectionState selectionState) {
        ISelectableModel columnModel = (ISelectableModel) mTableView.getAdapter().getColumnHeaderItem(column);
        columnModel.setSelectionState(selectionState);
    }

    public boolean columnHasItemSelected(int column) {
        for(Object cell : mTableView.getAdapter().getCellColumnItems(column)) {
            if(cell instanceof ISelectableModel) {
                if( ((ISelectableModel)cell).getSelectionState() == SelectionState.SELECTED ) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean rowHasItemSelected(int row) {
        for(Object cell : mTableView.getAdapter().getCellRowItems(row)) {
            if(cell instanceof ISelectableModel) {
                if( ((ISelectableModel)cell).getSelectionState() == SelectionState.SELECTED ) {
                    return true;
                }
            }
        }
        return false;
    }


    public void changeRowBackgroundColorBySelectionStatus(AbstractViewHolder viewHolder,
                                                          SelectionState selectionState) {
        if(mShadowEnabled || (!mShadowEnabled && selectionState != SelectionState.SHADOWED)) {
            viewHolder.setBackgroundColor(mTableView.getAdapter().getColorForSelection(selectionState));
        }
    }

    public void changeColumnBackgroundColorBySelectionStatus(AbstractViewHolder viewHolder,
                                                             SelectionState selectionState) {
        if(mShadowEnabled || (!mShadowEnabled && selectionState != SelectionState.SHADOWED)) {
            viewHolder.setBackgroundColor(mTableView.getAdapter().getColorForSelection(selectionState));
        }
    }

    public boolean isMultiSelectionEnabled() {
        return mMultiSelectionEnabled;
    }

    public void setMultiSelectionEnabled(boolean enabled) {
        this.mMultiSelectionEnabled = enabled;
    }

}
