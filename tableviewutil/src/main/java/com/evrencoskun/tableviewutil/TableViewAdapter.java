/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun, 2023 k3b
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

package com.evrencoskun.tableviewutil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.model.IModelWithId;
import com.evrencoskun.tableview.model.IViewHolderFactory;
import com.evrencoskun.tableview.sort.SortState;
import com.evrencoskun.tableviewutil.holder.BoolDrawableCellViewHolder;
import com.evrencoskun.tableviewutil.holder.ColumnHeaderViewHolder;
import com.evrencoskun.tableviewutil.holder.GenericTextCellViewHolder;
import com.evrencoskun.tableviewutil.holder.RowHeaderViewHolder;
import com.evrencoskun.tableview.model.Cell;
import com.evrencoskun.tableview.model.RowHeader;

import java.util.List;

/**
 * Generic adapter that translates a POJO to TableView-Cells.
 *
 * Defaultimplementation translates all columns to TextView fields.
 */
public class TableViewAdapter<POJO extends IModelWithId>
        extends AbstractTableAdapter<String, RowHeader, Cell<POJO>> {
    private static final String LOG_TAG = TableViewAdapter.class.getSimpleName();

    public static final int COLUMN_TYPE_GENERIC = 9999;
    @Nullable private final List<ColumnDefinition<POJO>> columnDefinitions;

    public TableViewAdapter(@Nullable List<ColumnDefinition<POJO>> columnDefinitions) {
        super();
        this.columnDefinitions = columnDefinitions;
    }

    /**
     * This is where you create your custom Column Header ViewHolder. This method is called when
     * Column Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getColumnHeaderItemViewType" method to support
     *                 different type of viewHolder as a Column Header item.
     * @see #getColumnHeaderItemViewType(int);
     */
    @NonNull
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_column_header_layout, parent, false);

        // Create a ColumnHeader ViewHolder
        return new ColumnHeaderViewHolder(layout, getTableView());

    }

    /**
     * That is where you set Column Header data to your custom Column Header ViewHolder.
     * This method is Called by ColumnHeader RecyclerView of the TableView to display the data at
     * the specified position. This method gives you everything you need about a column header
     * item.
     *
     * @param holder                : This is one of your column header ViewHolders that was created
     *                              on ```onCreateColumnHeaderViewHolder``` method. In this example
     *                              we have created "ColumnHeaderViewHolder" holder.
     * @param columnHeader          : This is the column header located on this X
     *                              position. In this example, the model class is "String".
     * @param columnPosition        : This is the X (Column) position of the column header item.
     * @see #onCreateColumnHeaderViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable String
            columnHeader, int columnPosition) {
        holder.setCell(columnHeader, columnPosition,0, getColumnDefinition(columnPosition));
    }

    /**
     * This is where you create your custom Cell ViewHolder. This method is called when Cell
     * RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given type to
     * represent an item.
     *
     * @param viewType : This value comes from "getCellItemViewType" method to support different
     *                 type of viewHolder as a Cell item.
     * @see #getCellItemViewType(int);
     */
    @NonNull
    @Override
    public AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolderFactory(viewType, TableViewAdapter::createGenericTextCellViewHolder).createViewHolder(parent);
    }

    /**
     * Translates columnNumber to CellItemViewType.
     *
     * @return columnNumber or COLUMN_TYPE_GENERIC if there is no special CellItemViewType
     */
    @Override
    public int getCellItemViewType(int columnNumber) {
        IViewHolderFactory viewHolderFactory = getViewHolderFactory(columnNumber, null);

        if (viewHolderFactory != null) {
            // if columnDefinitions found use columnNumber as returned viewtype
            return columnNumber;
        }

        // if no specialised viewType is found use generic
        return COLUMN_TYPE_GENERIC;
    }

    private IViewHolderFactory getViewHolderFactory(int column,@Nullable IViewHolderFactory notFoundValue) {
        ColumnDefinition<POJO> definition = getColumnDefinition(column);
        if (definition != null) {
            IViewHolderFactory factory = definition.getViewHolderFactory();
            if (factory != null) {
                return factory;
            }
        }
        return notFoundValue;
    }

    private ColumnDefinition<POJO> getColumnDefinition(int column) {
        if (columnDefinitions != null && column >= 0 && column < columnDefinitions.size()) {
            return columnDefinitions.get(column);
        }
        return null;
    }

    /**
     * For cells that display a text
     *
     * @param parent where view and viewholder will be attached to
     * @return viewholder for view attached to R.layout.table_view_cell_layout
     */
    @NonNull
    public static GenericTextCellViewHolder createGenericTextCellViewHolder(@NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // For cells that display a text
        View layout = inflater.inflate(R.layout.table_view_cell_layout, parent, false);

        // Create a Cell ViewHolder
        return new GenericTextCellViewHolder(layout);
    }

    // Get image cell layout which has ImageView on the base instead of TextView.

    /**
     * For cells that display one of two images based on a boolean value
     * @param parent where view and viewholder will be attached to
     * @param idDrawableTrue icon id for the image that represents true.
     * @param idDrawableFalse icon id for the image that represents false.
     * @return viewholder for view attached to R.layout.table_view_image_cell_layout
     */
    @NonNull public static AbstractViewHolder createBoolDrawableCellViewHolder(@NonNull ViewGroup parent, @DrawableRes int idDrawableTrue, @DrawableRes int idDrawableFalse) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.table_view_image_cell_layout, parent, false);
        return new BoolDrawableCellViewHolder(layout, idDrawableTrue, idDrawableFalse);
    }

    /**
     * That is where you set Cell View Model data to your custom Cell ViewHolder. This method is
     * Called by Cell RecyclerView of the TableView to display the data at the specified position.
     * This method gives you everything you need about a cell item.
     *
     * @param holder         : This is one of your cell ViewHolders that was created on
     *                       ```onCreateCellViewHolder``` method. In this example we have created
     *                       "GenericTextCellViewHolder" holder.
     * @param cellItemModel  : This is the cell view model located on this X and Y position. In this
     *                       example, the model class is "Cell".
     * @param columnPosition : This is the X (Column) position of the cell item.
     * @param rowPosition    : This is the Y (Row) position of the cell item.
     * @see #onCreateCellViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable Cell<POJO> cellItemModel, int
            columnPosition, int rowPosition) {
        holder.setCell(cellItemModel != null ? cellItemModel.getContent() : null, columnPosition, rowPosition, cellItemModel.getData());
    }

    /**
     * This is where you create your custom Row Header ViewHolder. This method is called when
     * Row Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getRowHeaderItemViewType" method to support
     *                 different type of viewHolder as a row Header item.
     * @see #getRowHeaderItemViewType(int);
     */
    @NonNull
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get Row Header xml Layout
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_row_header_layout, parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    /**
     * That is where you set Row Header View Model data to your custom Row Header ViewHolder. This
     * method is Called by RowHeader RecyclerView of the TableView to display the data at the
     * specified position. This method gives you everything you need about a row header item.
     *
     * @param holder             : This is one of your row header ViewHolders that was created on
     *                           ```onCreateRowHeaderViewHolder``` method. In this example we have
     *                           created "RowHeaderViewHolder" holder.
     * @param rowHeaderItemModel : This is the row header view model located on this Y position. In
     *                           this example, the model class is "RowHeader".
     * @param rowPosition        : This is the Y (row) position of the row header item.
     * @see #onCreateRowHeaderViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable RowHeader rowHeaderItemModel,
                                          int rowPosition) {

        // Get the holder to update row header item text
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.setCell(rowHeaderItemModel.getId(),-1, rowPosition, rowHeaderItemModel.getData());
    }

    @NonNull
    @Override
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        // Get Corner xml layout
        View corner = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_corner_layout, parent, false);
        corner.setOnClickListener(view -> {
            SortState sortState = TableViewAdapter.this.getTableView()
                    .getRowHeaderSortingStatus();
            if (sortState != SortState.ASCENDING) {
                Log.d("TableViewAdapter", "Order Ascending");
                TableViewAdapter.this.getTableView().sortRowHeader(SortState.ASCENDING);
            } else {
                Log.d("TableViewAdapter", "Order Descending");
                TableViewAdapter.this.getTableView().sortRowHeader(SortState.DESCENDING);
            }
        });
        return corner;
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        // The unique ID for this type of column header item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of GenericTextCellViewHolder on "onCreateCellViewHolder"
        return COLUMN_TYPE_GENERIC;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return COLUMN_TYPE_GENERIC;
    }
}
