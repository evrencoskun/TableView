package com.evrencoskun.tableviewsample.tableview.popup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableviewsample.R;

/**
 * Created by evrencoskun on 21.01.2018.
 */

public class RowHeaderLongPressPopup extends PopupMenu implements PopupMenu
        .OnMenuItemClickListener {

    // Menu Item constants
    private static final int SCROLL_COLUMN = 1;
    private static final int SHOWHIDE_COLUMN = 2;

    private ITableView m_iTableView;
    private Context mContext;

    public RowHeaderLongPressPopup(RecyclerView.ViewHolder p_iViewHolder, ITableView p_jTableView) {
        super(p_iViewHolder.itemView.getContext(), p_iViewHolder.itemView);

        this.m_iTableView = p_jTableView;
        this.mContext = p_iViewHolder.itemView.getContext();

        initialize();
    }

    private void initialize() {
        createMenuItem();

        this.setOnMenuItemClickListener(this);
    }

    private void createMenuItem() {
        this.getMenu().add(Menu.NONE, SCROLL_COLUMN, 0, mContext.getString(R.string
                .scroll_to_column_position));
        this.getMenu().add(Menu.NONE, SHOWHIDE_COLUMN, 1, mContext.getString(R.string
                .show_hide_the_column));
        // add new one ...

    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // Note: item id is index of menu item..

        switch (menuItem.getItemId()) {
            case SCROLL_COLUMN:
                m_iTableView.scrollToColumnPosition(15);
                break;
            case SHOWHIDE_COLUMN:
                int column = 1;
                if (m_iTableView.isColumnVisible(column)) {
                    m_iTableView.hideColumn(column);
                } else {
                    m_iTableView.showColumn(column);
                }

                break;
        }
        return true;
    }

}
