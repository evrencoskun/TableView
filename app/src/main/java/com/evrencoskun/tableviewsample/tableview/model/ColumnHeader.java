package com.evrencoskun.tableviewsample.tableview.model;

import com.evrencoskun.tableview.adapter.GhostableColumn;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class ColumnHeader extends Cell implements GhostableColumn {

    public ColumnHeader(String p_strId) {
        super(p_strId);
    }

    public ColumnHeader(String p_strId, String p_strData) {
        super(p_strId, p_strData);
    }

    @Override
    public String getColumnName() {
        return getData().toString();
    }
}

