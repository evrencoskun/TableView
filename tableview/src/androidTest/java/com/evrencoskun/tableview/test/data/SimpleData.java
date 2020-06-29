/*
 * Copyright (c) 2020. Andrew Beck
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.evrencoskun.tableview.test.data;

import com.evrencoskun.tableview.test.models.Cell;
import com.evrencoskun.tableview.test.models.ColumnHeader;
import com.evrencoskun.tableview.test.models.RowHeader;

import java.util.ArrayList;
import java.util.List;

public class SimpleData {
    private List<List<Cell>> cells;
    private List<ColumnHeader> columnHeaders;
    private List<RowHeader> rowHeaders;

    public SimpleData(int size) {
        rowHeaders = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            rowHeaders.add(new RowHeader(String.valueOf(i), "r:" + i));
        }

        columnHeaders = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            columnHeaders.add(new ColumnHeader(String.valueOf(i), "c:" + i));
        }

        cells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                String id = j + ":" + i;
                cellList.add(new Cell(id, "r:" + i + "c:" + j));
            }
            cells.add(cellList);
        }
    }

    public List<List<Cell>> getCells() {
        return cells;
    }

    public List<ColumnHeader> getColumnHeaders() {
        return columnHeaders;
    }

    public List<RowHeader> getRowHeaders() {
        return rowHeaders;
    }
}