package com.evrencoskun.tableview.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Created by cedricferry on 14/2/18.
 */

public class RowHeaderForCellSortComparator implements Comparator {

    private List<ISortableModel> mReferenceList;
    private List<List<ISortableModel>> mColumnList;

    private SortState mRortState;
    private RowHeaderSortComparator mRowHeaderSortComparator;

    public RowHeaderForCellSortComparator(List<ISortableModel> referenceList,
                                          List<List<ISortableModel>> columnList,
                                          SortState sortState) {
        this.mReferenceList = referenceList;
        this.mColumnList = columnList;
        this.mRortState = sortState;
        this.mRowHeaderSortComparator = new RowHeaderSortComparator(sortState);
    }

    @Override
    public int compare(Object o, Object t1) {
        Object o1 = mReferenceList.get(this.mColumnList.indexOf(o)).getContent();
        Object o2 = mReferenceList.get(this.mColumnList.indexOf(t1)).getContent();
        if (mRortState == SortState.DESCENDING) {
            return mRowHeaderSortComparator.compareContent(o2, o1);
        } else {
            return mRowHeaderSortComparator.compareContent(o1, o2);
        }
    }
}
