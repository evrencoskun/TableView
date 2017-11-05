package com.evrencoskun.tableview.listener;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by evrencoskun on 20/09/2017.
 */

public interface ITableViewListener {

    void onCellClicked(@NonNull RecyclerView.ViewHolder p_jCellView, int p_nXPosition, int
            p_nYPosition);

    void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int
            p_nXPosition);

    void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int p_nYPosition);

}
