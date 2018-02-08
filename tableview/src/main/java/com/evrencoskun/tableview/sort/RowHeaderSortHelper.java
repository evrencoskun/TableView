package com.evrencoskun.tableview.sort;

import android.util.Log;
import android.view.View;


/**
 * Created by cedricferry on 6/2/18.
 */

public class RowHeaderSortHelper {

    private static final String LOG_TAG = RowHeaderSortHelper.class.getSimpleName();

    private SortState mSortState;

    public RowHeaderSortHelper() {
    }

    private void sortingStatusChanged(SortState sortState) {
        mSortState = sortState;
        // TODO: Should we add an interface and listner and call listner when it is sorted?
    }

    public void setSortingStatus(SortState status) {
        mSortState = status;
        sortingStatusChanged(status);
    }

    public void clearSortingStatus() {
        mSortState = SortState.UNSORTED;
    }

    public boolean isSorting() {
        return mSortState != SortState.UNSORTED;
    }

    public SortState getSortingStatus() {
        return mSortState;
    }


    public class TableViewSorterException extends Exception {

        public TableViewSorterException() {
            super("For sorting process, column header view holders must be " + "extended from " +
                    "AbstractSorterViewHolder class");
        }

    }
}
