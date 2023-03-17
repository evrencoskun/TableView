/*
 * MIT License
 *
 * Copyright (c) 2023 K3b
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

package com.evrencoskun.tableview.model;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.model.IColumnValueProvider;
import com.evrencoskun.tableview.model.IViewHolderFactory;

/**
 * All informations needed to define a Table
 * @param <POJO>
 */
public class ColumnDefinition<POJO> {
    public static final int COLUMN_TYPE_GENERIC = 9999;
    @NonNull private final String columnHeaderText;
    @NonNull private final IColumnValueProvider<POJO> pojoToCellValueProvider;
    @Nullable private final IViewHolderFactory viewHolderFactory;

    private final int columnType;

    private static SparseArray<IViewHolderFactory> typId2ViewHolderFactory = new SparseArray<>();

    /**
     * Constructor
     * @param columnHeaderText text to be displayed as column header
     * @param pojoToCellValueProvider translates POJO to cell column Text
     * @param viewHolderFactory creates view with viewholder for this column. null means default viewholder
     * @param columnType id of the used viewHolderFactory (must be unique for each viewholder-type). COLUMN_TYPE_GENERIC means default viewholder
     */
    public ColumnDefinition(
            @NonNull String columnHeaderText,
            @NonNull  IColumnValueProvider<POJO> pojoToCellValueProvider,
            @Nullable IViewHolderFactory viewHolderFactory,
            int columnType) {
        this.columnHeaderText = columnHeaderText;
        this.pojoToCellValueProvider = pojoToCellValueProvider;
        this.viewHolderFactory = viewHolderFactory;
        this.columnType = columnType;
        if (columnType != COLUMN_TYPE_GENERIC) {
           typId2ViewHolderFactory.append(columnType, viewHolderFactory);
        }
    }

    public ColumnDefinition(
            @NonNull String columnHeaderText,
            @NonNull  IColumnValueProvider<POJO> pojoToCellValueProvider) {
        this(columnHeaderText,pojoToCellValueProvider,null, COLUMN_TYPE_GENERIC);
    }

    /** text to be displayed as column header */
    @NonNull public String getColumnHeaderText() {
        return columnHeaderText;
    }

    /** translates POJO to cell column Text */
    @NonNull public IColumnValueProvider<POJO> getPojoToCellValueProvider() {
        return pojoToCellValueProvider;
    }

    /** creates view with viewholder for this column. null means default viewholder */
    @Nullable public IViewHolderFactory getViewHolderFactory() {
        return viewHolderFactory;
    }

    @Override
    public String toString() {
        return "ColumnDefinition{" +
                "columnHeaderText='" + columnHeaderText + '\'' +
                '}';
    }
    public int getColumnType() {
        return columnType;
    }
}
