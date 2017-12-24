package com.evrencoskun.tableview.adapter.recyclerview.holder;

import android.view.View;

import com.evrencoskun.tableview.sort.SortState;

/**
 * Created by evrencoskun on 16.12.2017.
 */

public class AbstractSorterViewHolder extends AbstractViewHolder {

    private SortState mSortState = SortState.UNSORTED;

    public AbstractSorterViewHolder(View itemView) {
        super(itemView);
    }

    public void onSortingStatusChanged(SortState pSortState) {
        this.mSortState = pSortState;
    }

    public SortState getSortState() {
        return mSortState;
    }
}
