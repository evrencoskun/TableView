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

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.preference.Preferences;

/**
 * Created by evrencoskun on 3.03.2018.
 */

public class PreferencesHandler {

    private TableView tableView;
    private ScrollHandler scrollHandler;
    private SelectionHandler selectionHandler;

    public PreferencesHandler(TableView tableView) {
        this.tableView = tableView;
        this.scrollHandler = tableView.getScrollHandler();
        this.selectionHandler = tableView.getSelectionHandler();
    }


    public Preferences savePreferences(){
        Preferences preferences = new Preferences();
        preferences.columnPosition = scrollHandler.getColumnPosition();
        preferences.columnPositionOffset = scrollHandler.getColumnPositionOffset();
        preferences.rowPosition = scrollHandler.getRowPosition();
        preferences.rowPositionOffset = scrollHandler.getRowPositionOffset();
        preferences.selectedColumnPosition = selectionHandler.getSelectedColumnPosition();
        preferences.selectedRowPosition = selectionHandler.getSelectedRowPosition();
        return preferences;
    }


    public void loadPreferences(Preferences preferences){
        scrollHandler.scrollToColumnPosition(preferences.columnPosition, preferences.columnPositionOffset);
        scrollHandler.scrollToRowPosition(preferences.rowPosition, preferences.rowPositionOffset);
        selectionHandler.setSelectedColumnPosition(preferences.selectedColumnPosition);
        selectionHandler.setSelectedRowPosition(preferences.selectedRowPosition);
    }
}
