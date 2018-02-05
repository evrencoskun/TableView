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

package com.evrencoskun.tableview.filter;

import java.util.List;

public abstract class FilterChangedListener<T> {

    /**
     * Called when a filter has been changed.
     *
     * @param filteredCellItems      The list of filtered cell items.
     * @param filteredRowHeaderItems The list of filtered row items.
     */
    public void onFilterChanged(List<List<T>> filteredCellItems, List<T> filteredRowHeaderItems) {
    }

    /**
     * Called when the TableView filters are cleared.
     *
     * @param originalCellItems      The unfiltered cell item list.
     * @param originalRowHeaderItems The unfiltered row header list.
     */
    public void onFilterCleared(List<List<T>> originalCellItems, List<T> originalRowHeaderItems) {
    }
}
