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

package com.evrencoskun.tableviewsample.tableview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.evrencoskun.tableviewsample.R;
import com.evrencoskun.tableviewsample.tableview.model.Cell;
import com.evrencoskun.tableviewsample.tableview.model.ColumnHeader;
import com.evrencoskun.tableviewsample.tableview.model.RowHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class TableViewModel {

    // Columns indexes
    public static final int MOOD_COLUMN_INDEX = 3;
    public static final int GENDER_COLUMN_INDEX = 4;

    // Constant values for icons
    public static final int SAD = 1;
    public static final int HAPPY = 2;
    public static final int BOY = 1;
    public static final int GIRL = 2;

    // Constant size for dummy data sets
    private static final int COLUMN_SIZE = 500;
    private static final int ROW_SIZE = 500;

    // Drawables
    private final Drawable mBoyDrawable;
    private final Drawable mGirlDrawable;
    private final Drawable mHappyDrawable;
    private final Drawable mSadDrawable;

    private Context mContext;

    public TableViewModel(Context context) {
        mContext = context;

        // initialize drawables
        mBoyDrawable = ContextCompat.getDrawable(context, R.drawable.ic_male);
        mGirlDrawable = ContextCompat.getDrawable(context, R.drawable.ic_female);
        mHappyDrawable = ContextCompat.getDrawable(context, R.drawable.ic_happy);
        mSadDrawable = ContextCompat.getDrawable(context, R.drawable.ic_sad);
    }

    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            RowHeader header = new RowHeader(String.valueOf(i), "row " + i);
            list.add(header);
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    public static List<RowHeader> getSimpleRowHeaderList(int startIndex) {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            RowHeader header = new RowHeader(String.valueOf(i), "row " + (startIndex + i));
            list.add(header);
        }

        return list;
    }


    private List<ColumnHeader> getSimpleColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();

        for (int i = 0; i < COLUMN_SIZE; i++) {
            String title = "column " + i;
            if (i % 6 == 2) {
                title = "large column " + i;
            } else if (i == MOOD_COLUMN_INDEX) {
                title = "mood";
            } else if (i == GENDER_COLUMN_INDEX) {
                title = "gender";
            }
            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    private List<ColumnHeader> getRandomColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();

        for (int i = 0; i < COLUMN_SIZE; i++) {
            String title = "column " + i;
            int nRandom = new Random().nextInt();
            if (nRandom % 4 == 0 || nRandom % 3 == 0 || nRandom == i) {
                title = "large column " + i;
            }

            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }

        return list;
    }

    private List<List<Cell>> getSimpleCellList() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();

            for (int j = 0; j < COLUMN_SIZE; j++) {
                String text = "cell " + j + " " + i;
                if (j % 4 == 0 && i % 5 == 0) {
                    text = "large cell " + j + " " + i + ".";
                }
                String id = j + "-" + i;

                Cell cell = new Cell(id, text);
                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    private List<List<Cell>> getCellListForSortingTest() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < COLUMN_SIZE; j++) {
                Object text = "cell " + j + " " + i;

                final int random = new Random().nextInt();
                if (j == 0) {
                    text = i;
                } else if (j == 1) {
                    text = random;
                } else if (j == MOOD_COLUMN_INDEX) {
                    text = random % 2 == 0 ? HAPPY : SAD;
                } else if (j == GENDER_COLUMN_INDEX) {
                    text = random % 2 == 0 ? BOY : GIRL;
                }

                // Create dummy id.
                String id = j + "-" + i;

                Cell cell;
                if (j == 3) {
                    cell = new Cell(id, text);
                } else if (j == 4) {
                    // NOTE female and male keywords for filter will have conflict since "female"
                    // contains "male"
                    cell = new Cell(id, text);
                } else {
                    cell = new Cell(id, text);
                }
                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    private List<List<Cell>> getRandomCellList() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            list.add(cellList);
            for (int j = 0; j < COLUMN_SIZE; j++) {
                String text = "cell " + j + " " + i;
                int random = new Random().nextInt();
                if (random % 2 == 0 || random % 5 == 0 || random == j) {
                    text = "large cell  " + j + " " + i + getRandomString() + ".";
                }

                // Create dummy id.
                String id = j + "-" + i;

                Cell cell = new Cell(id, text);
                cellList.add(cell);
            }
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    private List<List<Cell>> getEmptyCellList() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            list.add(cellList);
            for (int j = 0; j < COLUMN_SIZE; j++) {

                // Create dummy id.
                String id = j + "-" + i;

                Cell cell = new Cell(id, "");
                cellList.add(cell);
            }
        }

        return list;
    }

    private List<RowHeader> getEmptyRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            RowHeader header = new RowHeader(String.valueOf(i), "");
            list.add(header);
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    public static List<List<Cell>> getRandomCellList(int startIndex) {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            list.add(cellList);
            for (int j = 0; j < COLUMN_SIZE; j++) {
                String text = "cell " + j + " " + (i + startIndex);
                int random = new Random().nextInt();
                if (random % 2 == 0 || random % 5 == 0 || random == j) {
                    text = "large cell  " + j + " " + (i + startIndex) + getRandomString() + ".";
                }

                String id = j + "-" + (i + startIndex);

                Cell cell = new Cell(id, text);
                cellList.add(cell);
            }
        }

        return list;
    }


    private static String getRandomString() {
        Random r = new Random();
        String str = " a ";
        for (int i = 0; i < r.nextInt(); i++) {
            str = str + " a ";
        }

        return str;
    }

    public Drawable getDrawable(int value, boolean isGender) {
        if (isGender) {
            return value == BOY ? mBoyDrawable : mGirlDrawable;
        } else {
            return value == SAD ? mSadDrawable : mHappyDrawable;
        }
    }

    public List<List<Cell>> getCellList() {
        return getCellListForSortingTest();
    }

    public List<RowHeader> getRowHeaderList() {
        return getSimpleRowHeaderList();
    }

    public List<ColumnHeader> getColumnHeaderList() {
        return getRandomColumnHeaderList();
    }


}
