package com.evrencoskun.tableview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.ITableView;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public interface ITableAdapter {

    int getColumnHeaderItemViewType(int position);

    int getRowHeaderItemViewType(int position);

    RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType);

    void onBindCellViewHolder(RecyclerView.ViewHolder holder, int p_nXPosition, int p_nYPosition);

    RecyclerView.ViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType);

    void onBindColumnHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

    RecyclerView.ViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType);

    void onBindRowHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

    View onCreateCornerView();

    ITableView getTableView();

}
