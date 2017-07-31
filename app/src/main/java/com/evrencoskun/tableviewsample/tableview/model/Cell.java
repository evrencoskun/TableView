package com.evrencoskun.tableviewsample.tableview.model;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class Cell {

    private long m_nId;
    private String m_nData;
    private int m_nWidth = -1;

    public Cell(long p_nId) {
        this(p_nId, "");
    }

    public Cell(long p_nId, String p_strData) {
        this.m_nId = p_nId;
        this.m_nData = p_strData;
    }

    public long getId() {
        return m_nId;
    }

    public String getData() {
        return m_nData;
    }

    public void setData(String p_strData) {
        m_nData = p_strData;
    }

    public void setWidth(int p_nWidth) {
        this.m_nWidth = p_nWidth;
    }

    public int getWidth() {
        return m_nWidth;
    }
}

