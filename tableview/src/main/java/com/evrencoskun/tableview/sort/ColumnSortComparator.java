/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableview.sort;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by evrencoskun on 25.11.2017.
 */

public class ColumnSortComparator implements Comparator<List<ISortableModel>> {

    private int mXPosition;
    private SortState mSortState;

    public ColumnSortComparator(int xPosition, SortState sortState) {
        this.mXPosition = xPosition;
        this.mSortState = sortState;
    }

    @Override
    public int compare(List<ISortableModel> t1, List<ISortableModel> t2) {
        Object o1 = t1.get(mXPosition).getContent();
        Object o2 = t2.get(mXPosition).getContent();

        if (mSortState == SortState.DESCENDING) {
            return compareContent(o2, o1);
        } else {
            // Default sorting process is ASCENDING
            return compareContent(o1, o2);
        }
    }

    public int compareContent(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            Class type = o1.getClass();
            if (Comparable.class.isAssignableFrom(type)) {
                return ((Comparable) o1).compareTo(o2);
            } else if (type.getSuperclass() == Number.class) {
                return compare((Number) o1, (Number) o2);
            } else if (type == String.class) {
                return ((String) o1).compareTo((String) o2);
            } else if (type == Date.class) {
                return compare((Date) o1, (Date) o2);
            } else if (type == Boolean.class) {
                return compare((Boolean) o1, (Boolean) o2);
            } else {
                return ((String) o1).compareTo((String) o2);
            }
        }
    }

    public int compare(Number o1, Number o2) {
        double n1 = o1.doubleValue();
        double n2 = o2.doubleValue();
        if (n1 < n2) {
            return -1;
        } else if (n1 > n2) {
            return 1;
        } else {
            return 0;
        }
    }

    public int compare(Date o1, Date o2) {
        long n1 = o1.getTime();
        long n2 = o2.getTime();
        if (n1 < n2) {
            return -1;
        } else if (n1 > n2) {
            return 1;
        } else {
            return 0;
        }
    }

    public int compare(Boolean o1, Boolean o2) {
        boolean b1 = o1.booleanValue();
        boolean b2 = o2.booleanValue();
        if (b1 == b2) {
            return 0;
        } else if (b1) {
            return 1;
        } else {
            return -1;
        }
    }

}