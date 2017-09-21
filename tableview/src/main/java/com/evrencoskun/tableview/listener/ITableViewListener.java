package com.evrencoskun.tableview.listener;

/**
 * Created by evrencoskun on 20/09/2017.
 */

public interface ITableViewListener {

    void onCellClicked(int p_nXPosition, int p_nYPosition);

    void onColumnHeaderClicked(int p_nXPosition);

    void onRowHeaderClicked(int p_nYPosition);
}
