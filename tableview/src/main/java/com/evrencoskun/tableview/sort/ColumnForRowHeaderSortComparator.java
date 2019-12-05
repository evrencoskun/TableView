package com.evrencoskun.tableview.sort;

import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

/**
 * In order to keep RowHeader DataSet and Main DataSet aligned
 * it is required to sort RowHeader the same.
 * So if MainDataSet row 1 moved to position 10, RowHeader 1 move to position 10 too.
 * To accomplish that we need to set a comparator that use MainDataSet
 * in order to sort RowHeader.
 * Created by cedricferry on 7/2/18.
 */
public class ColumnForRowHeaderSortComparator implements Comparator<ISortableModel> {
    @NonNull
    private List<ISortableModel> mRowHeaderList;
    @NonNull
    private List<List<ISortableModel>> mReferenceList;
    private int column;
    @NonNull
    private SortState mSortState;
    @NonNull
    private ColumnSortComparator mColumnSortComparator;

    public ColumnForRowHeaderSortComparator(@NonNull List<ISortableModel> rowHeader,
                                            @NonNull List<List<ISortableModel>> referenceList,
                                            int column,
                                            @NonNull SortState sortState) {
        this.mRowHeaderList = rowHeader;
        this.mReferenceList = referenceList;
        this.column = column;
        this.mSortState = sortState;
        this.mColumnSortComparator = new ColumnSortComparator(column, sortState);
    }

    @Override
    public int compare(ISortableModel o, ISortableModel t1) {
        Object o1 = mReferenceList.get(this.mRowHeaderList.indexOf(o)).get(column).getContent();
        Object o2 = mReferenceList.get(this.mRowHeaderList.indexOf(t1)).get(column).getContent();
        if (mSortState == SortState.DESCENDING) {
            return mColumnSortComparator.compareContent(o2, o1);
        } else {
            return mColumnSortComparator.compareContent(o1, o2);
        }
    }
}
