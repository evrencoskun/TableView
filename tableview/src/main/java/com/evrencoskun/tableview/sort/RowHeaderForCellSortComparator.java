package com.evrencoskun.tableview.sort;

import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

/**
 * Created by cedricferry on 14/2/18.
 */

public class RowHeaderForCellSortComparator implements Comparator<List<ISortableModel>> {
    @NonNull
    private List<ISortableModel> mReferenceList;
    @NonNull
    private List<List<ISortableModel>> mColumnList;
    @NonNull
    private SortState mSortState;
    @NonNull
    private RowHeaderSortComparator mRowHeaderSortComparator;

    public RowHeaderForCellSortComparator(@NonNull List<ISortableModel> referenceList,
                                          @NonNull List<List<ISortableModel>> columnList,
                                          @NonNull SortState sortState) {
        this.mReferenceList = referenceList;
        this.mColumnList = columnList;
        this.mSortState = sortState;
        this.mRowHeaderSortComparator = new RowHeaderSortComparator(sortState);
    }

    @Override
    public int compare(List<ISortableModel> o, List<ISortableModel> t1) {
        Object o1 = mReferenceList.get(this.mColumnList.indexOf(o)).getContent();
        Object o2 = mReferenceList.get(this.mColumnList.indexOf(t1)).getContent();
        if (mSortState == SortState.DESCENDING) {
            return mRowHeaderSortComparator.compareContent(o2, o1);
        } else {
            return mRowHeaderSortComparator.compareContent(o1, o2);
        }
    }
}
