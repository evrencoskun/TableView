package com.evrencoskun.tableview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;

import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public abstract class AbstractTableAdapter<CH, RH, C> implements ITableAdapter {

    private int m_nRowHeaderWidth;
    private int m_nColumnHeaderHeight;

    protected Context m_jContext;
    private ColumnHeaderRecyclerViewAdapter m_iColumnHeaderRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter m_iRowHeaderRecyclerViewAdapter;
    private CellRecyclerViewAdapter m_iCellRecyclerViewAdapter;
    private View m_jCornerView;

    protected List<CH> m_jColumnHeaderItems;
    protected List<RH> m_jRowHeaderItems;
    protected List<List<C>> m_jCellItems;

    private ITableView m_iTableView;

    public AbstractTableAdapter(Context p_jContext) {
        m_jContext = p_jContext;
        initialize();
    }

    private void initialize() {
        // Create Column header RecyclerView Adapter
        m_iColumnHeaderRecyclerViewAdapter = new ColumnHeaderRecyclerViewAdapter(m_jContext,
                m_jColumnHeaderItems, this);

        // Create Row Header RecyclerView Adapter
        m_iRowHeaderRecyclerViewAdapter = new RowHeaderRecyclerViewAdapter(m_jContext,
                m_jRowHeaderItems, this);

        // Create Cell RecyclerView Adapter
        m_iCellRecyclerViewAdapter = new CellRecyclerViewAdapter(m_jContext, m_jCellItems, this);
    }

    public void setColumnHeaderItems(List<CH> p_jColumnHeaderItems) {
        if (p_jColumnHeaderItems == null) {
            return;
        }

        m_jColumnHeaderItems = p_jColumnHeaderItems;

        // Set the items to the adapter
        m_iColumnHeaderRecyclerViewAdapter.setItems(m_jColumnHeaderItems);
    }

    public void setRowHeaderItems(List<RH> p_jRowHeaderItems) {
        if (p_jRowHeaderItems == null) {
            return;
        }

        m_jRowHeaderItems = p_jRowHeaderItems;

        // Set the items to the adapter
        m_iRowHeaderRecyclerViewAdapter.setItems(m_jRowHeaderItems);
    }

    public void setCellItems(List<List<C>> p_jCellItems) {
        if (p_jCellItems == null) {
            return;
        }

        m_jCellItems = p_jCellItems;

        // Set the items to the adapter
        m_iCellRecyclerViewAdapter.setItems(m_jCellItems);
    }

    public void setAllItems(List<CH> p_jColumnHeaderItems, List<RH> p_jRowHeaderItems,
                            List<List<C>> p_jCellItems) {
        // Set all items
        setColumnHeaderItems(p_jColumnHeaderItems);
        setRowHeaderItems(p_jRowHeaderItems);
        setCellItems(p_jCellItems);

        // Control corner view
        if ((p_jColumnHeaderItems != null && !p_jColumnHeaderItems.isEmpty()) &&
                (p_jRowHeaderItems != null && !p_jRowHeaderItems.isEmpty()) && (p_jCellItems !=
                null && !p_jCellItems.isEmpty()) && m_iTableView != null && m_jCornerView == null) {

            // Create corner view
            m_jCornerView = onCreateCornerView();
            m_iTableView.addView(m_jCornerView, new FrameLayout.LayoutParams(m_nRowHeaderWidth,
                    m_nColumnHeaderHeight));
        } else if (m_jCornerView != null) {

            // Change corner view visibility
            if (p_jRowHeaderItems != null && !p_jRowHeaderItems.isEmpty()) {
                m_jCornerView.setVisibility(View.GONE);
            } else {
                m_jCornerView.setVisibility(View.VISIBLE);
            }
        }
    }


    public ColumnHeaderRecyclerViewAdapter getColumnHeaderRecyclerViewAdapter() {
        return m_iColumnHeaderRecyclerViewAdapter;
    }

    public RowHeaderRecyclerViewAdapter getRowHeaderRecyclerViewAdapter() {
        return m_iRowHeaderRecyclerViewAdapter;
    }

    public CellRecyclerViewAdapter getCellRecyclerViewAdapter() {
        return m_iCellRecyclerViewAdapter;
    }

    public void setRowHeaderWidth(int p_nRowHeaderWidth) {
        this.m_nRowHeaderWidth = p_nRowHeaderWidth;
    }

    public void setColumnHeaderHeight(int p_nColumnHeaderHeight) {
        this.m_nColumnHeaderHeight = p_nColumnHeaderHeight;
    }

    public CH getColumnHeaderItem(int p_nPosition) {
        if ((m_jColumnHeaderItems == null || m_jColumnHeaderItems.isEmpty()) || p_nPosition < 0
                || p_nPosition >= m_jColumnHeaderItems.size()) {
            return null;
        }
        return m_jColumnHeaderItems.get(p_nPosition);
    }

    public RH getRowHeaderItem(int p_nPosition) {
        if ((m_jRowHeaderItems == null || m_jRowHeaderItems.isEmpty()) || p_nPosition < 0 ||
                p_nPosition >= m_jRowHeaderItems.size()) {
            return null;
        }
        return m_jRowHeaderItems.get(p_nPosition);
    }

    public C getCellItem(int p_nXPosition, int p_nYPosition) {
        if ((m_jCellItems == null || m_jCellItems.isEmpty()) || p_nXPosition < 0 || p_nYPosition
                >= m_jCellItems.size() || m_jCellItems.get(p_nYPosition) == null || p_nYPosition
                < 0 || p_nXPosition >= m_jCellItems.get(p_nYPosition).size()) {
            return null;
        }

        return m_jCellItems.get(p_nYPosition).get(p_nXPosition);
    }

    public final void notifyDataSetChanged() {
        m_iColumnHeaderRecyclerViewAdapter.notifyDataSetChanged();
        m_iRowHeaderRecyclerViewAdapter.notifyDataSetChanged();
        m_iCellRecyclerViewAdapter.notifyCellDataSetChanged();
    }

    public void setTableView(TableView p_iTableView) {
        m_iTableView = p_iTableView;
    }

    @Override
    public ITableView getTableView() {
        return m_iTableView;
    }

}
