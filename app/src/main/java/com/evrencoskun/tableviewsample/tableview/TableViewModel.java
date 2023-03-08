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

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.model.IColumnValueProvider;
import com.evrencoskun.tableview.model.IRow;
import com.evrencoskun.tableview.model.Row;
import com.evrencoskun.tableview.model.Cell;
import com.evrencoskun.tableviewsample.tableview.model.MySamplePojo;
import com.evrencoskun.tableview.model.RowHeader;
import com.evrencoskun.tableviewutil.ColumnDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class TableViewModel {
    // Columns indexes
    public static final int COLUMN_INDEX_MOOD_HAPPY = 3;
    public static final int COLUMN_INDEX_GENDER_MALE = 4;
    // Constant size for dummy data sets
    private static final int COLUMN_SIZE = 500;
    private static final int ROW_SIZE = 500;

    @NonNull
    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            RowHeader header = new RowHeader(new MySamplePojo(String.valueOf(i)));
            list.add(header);
        }

        return list;
    }

    private static final IColumnValueProvider<MySamplePojo> TEXT_VALUE_PROVIDER = r -> r.mText;
    private static final List<ColumnDefinition<MySamplePojo>> COLUMN_DEFINITIONS =
            Arrays.asList(
            new ColumnDefinition<>("Random 0", r -> r.mRandom),
            new ColumnDefinition<>("Short 1", r -> r.mRandomShort),
            new ColumnDefinition<>("Text 2", TEXT_VALUE_PROVIDER),
            new ColumnDefinition<>("Gender 3", r -> r.mGenderMale),
            new ColumnDefinition<>("Mood 4", r -> r.mMoodHappy));

    /**
     * This is a dummy model list test some cases.
     */
    @NonNull
    private List<String> getRandomColumnHeaderList() {
        List<String> list = new ArrayList<>();

        for (ColumnDefinition<MySamplePojo>  c : COLUMN_DEFINITIONS) {
            list.add(c.getHeader());
        }

        for (int i = COLUMN_DEFINITIONS.size(); i < COLUMN_SIZE; i++) {
            String title = "column " + i;
            int nRandom = new Random().nextInt();
            if (nRandom % 4 == 0 || nRandom % 3 == 0 || nRandom == i) {
                title = "large column " + i;
            }

            list.add(title);
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    @NonNull
    private List<IRow<Cell<MySamplePojo>>> getCellListForSortingTest() {
        List<IRow<Cell<MySamplePojo>>> list = new ArrayList<>();
        for (int rowId = 0; rowId < ROW_SIZE; rowId++) {
            IRow<Cell<MySamplePojo>> cellList = new Row<>();
            for (int colId = 0; colId < COLUMN_SIZE; colId++) {
                IColumnValueProvider<MySamplePojo> provider =
                        colId < COLUMN_DEFINITIONS.size()
                                ? COLUMN_DEFINITIONS.get(colId).getValueProvider()
                                : TEXT_VALUE_PROVIDER;

                Cell<MySamplePojo> cell = new Cell(new MySamplePojo("" + rowId), provider);
                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }

    @NonNull
    public List<IRow<Cell<MySamplePojo>>> getCellList() {
        return getCellListForSortingTest();
    }

    @NonNull
    public List<RowHeader> getRowHeaderList() {
        return getSimpleRowHeaderList();
    }

    @NonNull
    public List<String> getColumnHeaderList() {
        return getRandomColumnHeaderList();
    }
}
