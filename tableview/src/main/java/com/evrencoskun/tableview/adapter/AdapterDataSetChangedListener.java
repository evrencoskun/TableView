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

package com.evrencoskun.tableview.adapter;

import java.util.List;

public abstract class AdapterDataSetChangedListener<CH, RH, C> {

    /**
     * Dispatches changes on column header items to listener.
     *
     * @param columnHeaderItems The current column header items.
     */
    public void onColumnHeaderItemsChanged(List<CH> columnHeaderItems) {
    }

    /**
     * Dispatches changes on row header items to listener.
     *
     * @param rowHeaderItems The current row header items.
     */
    public void onRowHeaderItemsChanged(List<RH> rowHeaderItems) {
    }

    /**
     * Dispatches changes on cell items to listener.
     *
     * @param cellItems The current cell items.
     */
    public void onCellItemsChanged(List<List<C>> cellItems) {
    }

    /**
     * Dispatches the changes on column header, row header and cell items.
     *
     * @param columnHeaderItems The current column header items.
     * @param rowHeaderItems    The current row header items.
     * @param cellItems         The current cell items.
     */
    public void onDataSetChanged(
            List<CH> columnHeaderItems,
            List<RH> rowHeaderItems,
            List<List<C>> cellItems) {
    }
}
