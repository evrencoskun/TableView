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

public class FilterHelper<T> {

    private List<List<IFilterableModel>> mCellOriginalData;
    private List<T> mRowOriginalData;

    public List<List<IFilterableModel>> getCellOriginalData() {
        return mCellOriginalData;
    }

    public List<T> getRowOriginalData() {
        return mRowOriginalData;
    }

    public FilterHelper(
            List<List<IFilterableModel>> mCellOriginalData,
            List<T> mRowOriginalData
    ) {
        this.mCellOriginalData = mCellOriginalData;
        this.mRowOriginalData = mRowOriginalData;
    }
}
