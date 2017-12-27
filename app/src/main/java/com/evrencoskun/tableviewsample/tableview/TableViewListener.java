package com.evrencoskun.tableviewsample.tableview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.evrencoskun.tableviewsample.tableview.holder.ColumnHeaderViewHolder;
import com.evrencoskun.tableviewsample.tableview.popup.ColumnHeaderLongPressPopup;

/**
 * Created by evrencoskun on 21/09/2017.
 */

public class TableViewListener implements ITableViewListener {

    private Toast m_jToast;
    private Context m_jContext;
    private TableView m_jTableView;

    public TableViewListener(TableView p_jTableView) {
        this.m_jContext = p_jTableView.getContext();
        this.m_jTableView = p_jTableView;
    }

    /**
     * Called when user click any cell item.
     *
     * @param p_jCellView  : Clicked Cell ViewHolder.
     * @param p_nXPosition : X (Column) position of Clicked Cell item.
     * @param p_nYPosition : Y (Row) position of Clicked Cell item.
     */
    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder p_jCellView, int p_nXPosition, int
            p_nYPosition) {
        // Do what you want.
        showToast("Cell " + p_nXPosition + " " + p_nYPosition + " has been clicked.");
    }

    /**
     * Called when user click any column header item.
     *
     * @param p_jColumnHeaderView : Clicked Column Header ViewHolder.
     * @param p_nXPosition        : X (Column) position of Clicked Column Header item.
     */
    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int
            p_nXPosition) {
        // Do what you want.
        showToast("Column header  " + p_nXPosition + " has been clicked.");
    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView,
                                          int p_nXPosition) {

        if (p_jColumnHeaderView != null && p_jColumnHeaderView instanceof ColumnHeaderViewHolder) {
            // Create Long Press Popup
            ColumnHeaderLongPressPopup popup = new ColumnHeaderLongPressPopup(
                    (ColumnHeaderViewHolder) p_jColumnHeaderView, m_jTableView);
            // Show
            popup.show();
        }
    }

    /**
     * Called when user click any Row Header item.
     *
     * @param p_jRowHeaderView : Clicked Row Header ViewHolder.
     * @param p_nYPosition     : Y (Row) position of Clicked Row Header item.
     */
    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
            p_nYPosition) {
        // Do what you want.


        showToast("Row header " + p_nYPosition + " has been clicked.");
    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
            p_nYPosition) {

    }


    private void showToast(String p_strMessage) {
        if (m_jToast == null) {
            m_jToast = Toast.makeText(m_jContext, "", Toast.LENGTH_SHORT);
        }

        m_jToast.setText(p_strMessage);
        m_jToast.show();
    }
}
