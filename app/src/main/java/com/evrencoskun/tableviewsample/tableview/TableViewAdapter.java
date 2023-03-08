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

package com.evrencoskun.tableviewsample.tableview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableviewsample.R;
import com.evrencoskun.tableviewutil.TableViewAdapterBase;
import com.evrencoskun.tableviewutil.holder.BoolDrawableCellViewHolder;
import com.evrencoskun.tableview.model.Cell;
import com.evrencoskun.tableviewsample.tableview.model.MySamplePojo;

/**
 * Created by evrencoskun on 11/06/2017.
 * <p>
 * This is a sample of custom TableView Adapter that is customized for MySamplePojo:
 * Columns MOOD_CELL and GENDER_CELL are displayed as images.
 */

public class TableViewAdapter extends TableViewAdapterBase<MySamplePojo> {
    private static final String LOG_TAG = TableViewAdapter.class.getSimpleName();

    // Cell View Types by Column Position
    private static final int MOOD_CELL_TYPE = 1;
    private static final int GENDER_CELL_TYPE = 2;
    // add new one if it necessary..

    @NonNull
    protected final TableViewModel mTableViewModel;

    public TableViewAdapter(@NonNull TableViewModel tableViewModel) {
        mTableViewModel = tableViewModel;
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
        //TODO check
        Log.e(LOG_TAG, " onCreateCellViewHolder has been called");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout;

        switch (viewType) {
            case MOOD_CELL_TYPE:
                // Get image cell layout which has ImageView on the base instead of TextView.
                layout = inflater.inflate(R.layout.table_view_image_cell_layout, parent, false);

                return new BoolDrawableCellViewHolder(layout, R.drawable.ic_happy ,R.drawable.ic_sad);
            case GENDER_CELL_TYPE:
                // Get image cell layout which has ImageView instead of TextView.
                layout = inflater.inflate(R.layout.table_view_image_cell_layout, parent, false);

                return new BoolDrawableCellViewHolder(layout, R.drawable.ic_male, R.drawable.ic_female);
            default:
                return super.onCreateCellViewHolder(parent,viewType);
        }
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
    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable Cell<MySamplePojo> cellItemModel, int
            columnPosition, int rowPosition) {

        switch (holder.getItemViewType()) {
            case MOOD_CELL_TYPE:
                BoolDrawableCellViewHolder moodViewHolder = (BoolDrawableCellViewHolder) holder;
                moodViewHolder.setData(cellItemModel.getData().mMoodHappy);
                break;
            case GENDER_CELL_TYPE:
                BoolDrawableCellViewHolder genderViewHolder = (BoolDrawableCellViewHolder) holder;
                genderViewHolder.setData(cellItemModel.getData().mGenderMale);
                break;
            default:
                // Get the holder to update cell item text
                super.onBindCellViewHolder(holder, cellItemModel, columnPosition, rowPosition);
                break;
        }
    }

    @Override
    public int getCellItemViewType(int column) {

        // The unique ID for this type of cell item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of GenericTextCellViewHolder on "onCreateCellViewHolder"
        switch (column) {
            case TableViewModel.COLUMN_INDEX_MOOD_HAPPY:
                return MOOD_CELL_TYPE;
            case TableViewModel.COLUMN_INDEX_GENDER_MALE:
                return GENDER_CELL_TYPE;
            default:
                // Default view type
                return getRowHeaderItemViewType(0);
        }
    }
}
