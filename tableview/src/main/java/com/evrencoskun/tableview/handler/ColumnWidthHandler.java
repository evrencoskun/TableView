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

package com.evrencoskun.tableview.handler;

import com.evrencoskun.tableview.ITableView;

/**
 * Created by evrencoskun on 25.04.2018.
 */

public class ColumnWidthHandler {

    private ITableView mTableView;

    public ColumnWidthHandler(ITableView tableView) {
        mTableView = tableView;
    }

    public void setColumnWidth(int columnPosition, int width) {

        // Firstly set the column header cache map
        mTableView.getColumnHeaderLayoutManager().setCacheWidth(columnPosition, width);

        // Set each of cell items that is located on the column position
        mTableView.getCellLayoutManager().setCacheWidth(columnPosition, width);
    }


}
