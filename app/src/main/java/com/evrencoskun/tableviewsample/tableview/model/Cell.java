package com.evrencoskun.tableviewsample.tableview.model;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class Cell {

    private String m_strId;
    private String m_strData;

    public Cell(String p_strId) {
        this.m_strId = p_strId;
        this.m_strData = "";
    }

    public Cell(String p_strId, String p_strData) {
        this.m_strId = p_strId;
        this.m_strData = p_strData;
    }

    public String getId() {
        return m_strId;
    }

    public String getData() {
        return m_strData;
    }

    public void setData(String p_strData) {
        m_strData = p_strData;
    }
}

