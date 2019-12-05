package com.evrencoskun.tableview.sort;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

/**
 * Created by cedricferry on 6/2/18.
 */

public abstract class AbstractSortComparator {
    @NonNull
    protected SortState mSortState;

    protected int compareContent(@Nullable Object o1, @Nullable Object o2) {
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

        return Double.compare(n1, n2);
    }

    public int compare(Date o1, Date o2) {
        long n1 = o1.getTime();
        long n2 = o2.getTime();

        return Long.compare(n1, n2);
    }

    public int compare(Boolean o1, Boolean o2) {
        return Boolean.compare(o1, o2);
    }
}
