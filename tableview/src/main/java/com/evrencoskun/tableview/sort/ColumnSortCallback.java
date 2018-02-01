package com.evrencoskun.tableview.sort;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by evrencoskun on 23.11.2017.
 */

public class ColumnSortCallback extends DiffUtil.Callback {

    private static final String LOG_TAG = ColumnSortCallback.class.getSimpleName();

    private List<List<ISortableModel>> m_jOldCellItems;
    private List<List<ISortableModel>> m_jNewCellItems;
    private int m_nColumnPosition;

    public ColumnSortCallback(List<List<ISortableModel>> p_jOldCellItems,
                              List<List<ISortableModel>> p_jNewCellItems, int
                                      p_nColumnPosition) {
        this.m_jOldCellItems = p_jOldCellItems;
        this.m_jNewCellItems = p_jNewCellItems;
        this.m_nColumnPosition = p_nColumnPosition;
    }

    @Override
    public int getOldListSize() {
        return m_jOldCellItems.size();
    }

    @Override
    public int getNewListSize() {
        return m_jNewCellItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Control for precaution from IndexOutOfBoundsException
        if (m_jOldCellItems.size() > oldItemPosition && m_jNewCellItems.size() > newItemPosition) {
            if (m_jOldCellItems.get(oldItemPosition).size() > m_nColumnPosition &&
                    m_jNewCellItems.get(newItemPosition).size() > m_nColumnPosition) {
                // Compare ids
                String oldId = m_jOldCellItems.get(oldItemPosition).get(m_nColumnPosition).getId();
                String newId = m_jNewCellItems.get(newItemPosition).get(m_nColumnPosition).getId();
                return oldId.equals(newId);
            }
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Control for precaution from IndexOutOfBoundsException
        if (m_jOldCellItems.size() > oldItemPosition && m_jNewCellItems.size() > newItemPosition) {
            if (m_jOldCellItems.get(oldItemPosition).size() > m_nColumnPosition &&
                    m_jNewCellItems.get(newItemPosition).size() > m_nColumnPosition) {
                // Compare contents
                Object oldContent = m_jOldCellItems.get(oldItemPosition).get(m_nColumnPosition).getContent();
                Object newContent = m_jNewCellItems.get(newItemPosition).get(m_nColumnPosition).getContent();
                return oldContent.equals(newContent);
            }
        }
        return false;
    }
}
