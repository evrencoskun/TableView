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

package com.evrencoskun.tableview.sort;

public abstract class ColumnSortStateChangedListener {

    /**
     * Dispatches sorting changes on a column to listeners.
     *
     * @param column    Column to be sorted.
     * @param sortState SortState of the column to be sorted.
     */
    public void onColumnSortStatusChanged(int column, SortState sortState) {
    }

    /**
     * Dispatches sorting changes to the row header column to listeners.
     *
     * @param sortState SortState of the row header column.
     */
    public void onRowHeaderSortStatusChanged(SortState sortState) {
    }
}
