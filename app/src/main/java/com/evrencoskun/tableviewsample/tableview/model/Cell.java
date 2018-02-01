package com.evrencoskun.tableviewsample.tableview.model;

import com.evrencoskun.tableview.sort.ISortableModel;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class Cell implements ISortableModel {

    private String m_strId;
    private Object m_strData;

    public Cell(String p_strId) {
        this.m_strId = p_strId;
    }

    public Cell(String p_strId, Object p_strData) {
        this.m_strId = p_strId;
        this.m_strData = p_strData;
    }

    public String getId() {
        return m_strId;
    }

    @Override
    public Object getContent() {
        return m_strData;
    }


    public Object getData() {
        return m_strData;
    }

    public void setData(String p_strData) {
        m_strData = p_strData;
    }
}

