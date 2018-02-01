package com.evrencoskun.tableview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public interface ITableAdapter {

    int getColumnHeaderItemViewType(int position);

    int getRowHeaderItemViewType(int position);

    int getCellItemViewType(int position);

    RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType);

    void onBindCellViewHolder(AbstractViewHolder holder, Object p_jValue, int p_nXPosition, int
            p_nYPosition);

    RecyclerView.ViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType);

    void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int p_nXPosition);

    RecyclerView.ViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType);

    void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int p_nYPosition);

    View onCreateCornerView();

    ITableView getTableView();

}
