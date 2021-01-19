/*
 * MIT License
 *
 * Copyright (c) 2021 Andrew Beck
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

    public SimpleData(int size){
        init(size, size);
    }

    public SimpleData(int columnSize, int rowSize) {
        init(columnSize, rowSize);
    }

    private void init(int columnSize, int rowSize) {
        rowHeaders = new ArrayList<>();
        for (int i = 0; i < rowSize; i++) {
            rowHeaders.add(new RowHeader(String.valueOf(i), "r:" + i));
        }

        columnHeaders = new ArrayList<>();
        for (int i = 0; i < columnSize; i++) {
            columnHeaders.add(new ColumnHeader(String.valueOf(i), "c:" + i));
        }

        cells = new ArrayList<>();
        for (int i = 0; i < rowSize; i++) {
            ArrayList<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < columnSize; j++) {
                String id = j + ":" + i;
                cellList.add(new Cell(id, "r:" + i + "c:" + j));
            }
            cells.add(cellList);
        }
    }

    public List<List<Cell>> getCells() {
        return cells;
    }

    public void setCells(List<List<Cell>> cells) {
        this.cells = cells;
    }

    public List<ColumnHeader> getColumnHeaders() {
        return columnHeaders;
    }

    public void setColumnHeaders(List<ColumnHeader> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public List<RowHeader> getRowHeaders() {
        return rowHeaders;
    }

    public void setRowHeaders(List<RowHeader> rowHeaders) {
        this.rowHeaders = rowHeaders;
    }
}