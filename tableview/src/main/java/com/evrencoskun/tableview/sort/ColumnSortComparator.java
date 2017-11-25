package com.evrencoskun.tableview.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Created by evrencoskun on 25.11.2017.
 */

public class ColumnSortComparator implements Comparator<List<ITableViewComparator>> {
    private int m_nXPosition;
    private SortOrder m_nSortOrder;

    public ColumnSortComparator(int p_nXPosition, SortOrder p_nSortOrder) {
        this.m_nXPosition = p_nXPosition;
        this.m_nSortOrder = p_nSortOrder;
    }

    @Override
    public int compare(List<ITableViewComparator> t1, List<ITableViewComparator> t2) {
        if (m_nSortOrder == SortOrder.DESCENDING) {
            return t2.get(m_nXPosition).getContent().compareTo(t1.get(m_nXPosition).getContent());
        } else {
            // Default sorting process is ASCENDING
            return t1.get(m_nXPosition).getContent().compareTo(t2.get(m_nXPosition).getContent());
        }
    }
}