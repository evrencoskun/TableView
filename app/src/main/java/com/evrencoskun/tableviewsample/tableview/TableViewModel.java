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

import com.evrencoskun.tableview.modell.IColumnValue;
import com.evrencoskun.tableview.modell.IRow;
import com.evrencoskun.tableview.modell.Row;
import com.evrencoskun.tableview.modell.Cell;
import com.evrencoskun.tableviewsample.tableview.model.MySamplePojo;
import com.evrencoskun.tableviewsample.tableview.model.RowHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class TableViewModel {
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

    /**
     * This is a dummy model list test some cases.
     */
    @NonNull
    private List<String> getRandomColumnHeaderList() {
        List<String> list = new ArrayList<>();

        // columns = new Object[]{mRandom, mRandomShort, mText, mGenderMale, mMoodHappy};
        for (int i = 0; i < COLUMN_SIZE; i++) {
            String title = "column " + i;
            int nRandom = new Random().nextInt();
            if (i == TableViewAdapter.COLUMN_INDEX_GENDER_MALE) {
                title = "Gender " + i;
            } else if (i == TableViewAdapter.COLUMN_INDEX_MOOD_HAPPY) {
                title = "Mood " + i;
            } else if (nRandom % 4 == 0 || nRandom % 3 == 0 || nRandom == i) {
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
        IColumnValue<MySamplePojo>[] getter = new IColumnValue[COLUMN_SIZE];
        for (int colId = 0; colId < COLUMN_SIZE; colId++) {

        }
        List<IRow<Cell<MySamplePojo>>> list = new ArrayList<>();
        for (int rowId = 0; rowId < ROW_SIZE; rowId++) {
            IRow<Cell<MySamplePojo>> cellList = new Row<>();
            for (int colId = 0; colId < COLUMN_SIZE; colId++) {
                Cell<MySamplePojo> cell = new Cell(new MySamplePojo("" + rowId), null);
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
