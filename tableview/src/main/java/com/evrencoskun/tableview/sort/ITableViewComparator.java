package com.evrencoskun.tableview.sort;

import java.util.Comparator;

/**
 * Created by evrencoskun on 24.11.2017.
 */

public interface ITableViewComparator extends Comparator {

    String getId();

    String getContent();
}
