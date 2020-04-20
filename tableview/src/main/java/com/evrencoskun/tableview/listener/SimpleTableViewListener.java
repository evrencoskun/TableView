package com.evrencoskun.tableview.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Convenience class to extend when you are only interested in a subset of events of {@link ITableViewListener}.
 */
public abstract class SimpleTableViewListener implements ITableViewListener {
	@Override
	public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
	}

	@Override
	public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
	}

	@Override
	public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
	}

	@Override
	public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
	}

	@Override
	public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
	}

	@Override
	public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
	}
}
