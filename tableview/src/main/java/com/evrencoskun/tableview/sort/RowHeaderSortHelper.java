package com.evrencoskun.tableview.sort;

import androidx.annotation.Nullable;

/**
 * Created by cedricferry on 6/2/18.
 */

public class RowHeaderSortHelper {
    @Nullable
    private SortState mSortState;

    public RowHeaderSortHelper() {
    }

    private void sortingStatusChanged(@Nullable SortState sortState) {
        mSortState = sortState;
        // TODO: Should we add an interface and listener and call listener when it is sorted?
    }

    public void setSortingStatus(@Nullable SortState status) {
        mSortState = status;
        sortingStatusChanged(status);
    }

    public void clearSortingStatus() {
        mSortState = SortState.UNSORTED;
    }

    public boolean isSorting() {
        return mSortState != SortState.UNSORTED;
    }

    @Nullable
    public SortState getSortingStatus() {
        return mSortState;
    }
}
