package com.evrencoskun.tableviewsample.tableview.popup;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.sort.SortState;
import com.evrencoskun.tableviewsample.R;
import com.evrencoskun.tableviewsample.tableview.holder.ColumnHeaderViewHolder;

/**
 * Created by evrencoskun on 24.12.2017.
 */

public class ColumnHeaderLongPressPopup extends PopupMenu implements PopupMenu
        .OnMenuItemClickListener {
    private static final String LOG_TAG = ColumnHeaderLongPressPopup.class.getSimpleName();

    // Menu Item constants
    private static final int ASCENDING = 1;
    private static final int DESCENDING = 2;
    private static final int HIDE_ROW = 3;
    private static final int SHOW_ROW = 4;
    private static final int SCROLL_ROW = 5;


    private ColumnHeaderViewHolder m_iViewHolder;
    private ITableView m_iTableView;
    private Context mContext;
    private int mXPosition;

    public ColumnHeaderLongPressPopup(ColumnHeaderViewHolder p_iViewHolder, ITableView
            p_jTableView) {
        super(p_iViewHolder.itemView.getContext(), p_iViewHolder.itemView);
        this.m_iViewHolder = p_iViewHolder;
        this.m_iTableView = p_jTableView;
        this.mContext = p_iViewHolder.itemView.getContext();
        this.mXPosition = m_iViewHolder.getAdapterPosition();

        // find the view holder
        m_iViewHolder = (ColumnHeaderViewHolder) m_iTableView.getColumnHeaderRecyclerView()
                .findViewHolderForAdapterPosition(mXPosition);

        initialize();
    }

    private void initialize() {
        createMenuItem();
        changeMenuItemVisibility();

        this.setOnMenuItemClickListener(this);
    }

    private void createMenuItem() {
        this.getMenu().add(Menu.NONE, ASCENDING, 0, mContext.getString(R.string.sort_ascending));
        this.getMenu().add(Menu.NONE, DESCENDING, 1, mContext.getString(R.string.sort_descending));
        this.getMenu().add(Menu.NONE, HIDE_ROW, 2, mContext.getString(R.string.hiding_row_sample));
        this.getMenu().add(Menu.NONE, SHOW_ROW, 3, mContext.getString(R.string.showing_row_sample));
        this.getMenu().add(Menu.NONE, SCROLL_ROW, 4, mContext.getString(R.string
                .scroll_to_row_position));
        // add new one ...

    }

    private void changeMenuItemVisibility() {
        // Determine which one shouldn't be visible
        SortState sortState = m_iTableView.getSortingStatus(mXPosition);
        if (sortState == SortState.UNSORTED) {
            // Show others
        } else if (sortState == SortState.DESCENDING) {
            // Hide DESCENDING menu item
            getMenu().getItem(1).setVisible(false);
        } else if (sortState == SortState.ASCENDING) {
            // Hide ASCENDING menu item
            getMenu().getItem(0).setVisible(false);
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // Note: item id is index of menu item..

        switch (menuItem.getItemId()) {
            case ASCENDING:
                m_iTableView.sortColumn(mXPosition, SortState.ASCENDING);

                break;
            case DESCENDING:
                m_iTableView.sortColumn(mXPosition, SortState.DESCENDING);
                break;
            case HIDE_ROW:
                m_iTableView.hideRow(5);
                break;
            case SHOW_ROW:
                m_iTableView.showRow(5);
                break;
            case SCROLL_ROW:
                m_iTableView.scrollToRowPosition(5);
                break;

        }
        return true;
    }

}
