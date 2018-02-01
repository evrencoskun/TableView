package com.evrencoskun.tableview.sort;

import android.util.Log;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.layoutmanager.ColumnHeaderLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 15.12.2017.
 */

public class ColumnSortHelper {

    private List<Directive> sortingColumns = new ArrayList<>();
    private ColumnHeaderLayoutManager mColumnHeaderLayoutManager;

    public ColumnSortHelper(ColumnHeaderLayoutManager pColumnHeaderLayoutManager) {
        this.mColumnHeaderLayoutManager = pColumnHeaderLayoutManager;
    }

    private void sortingStatusChanged(int pColumn, SortState status) {
        AbstractViewHolder holder = mColumnHeaderLayoutManager.getViewHolder(pColumn);


        if (holder != null) {
            if (holder instanceof AbstractSorterViewHolder) {
                ((AbstractSorterViewHolder) holder).onSortingStatusChanged(status);

            } else {
                // TODO: throw the TableViewSorterException
                //throw new TableViewSorterException();
                Log.e(ColumnSortHelper.class.getSimpleName(), "For sorting process, column " +
                        "header" + " view holders must be " + "extended from " +
                        "AbstractSorterViewHolder " + "class");
            }
        }
    }


    public void setSortingStatus(int column, SortState status) {
        Directive directive = getDirective(column);
        if (directive != EMPTY_DIRECTIVE) {
            sortingColumns.remove(directive);
        }
        if (status != SortState.UNSORTED) {
            sortingColumns.add(new Directive(column, status));
        }

        sortingStatusChanged(column, status);
    }

    public void clearSortingStatus() {
        sortingColumns.clear();
    }

    public boolean isSorting() {
        return sortingColumns.size() != 0;
    }

    public SortState getSortingStatus(int column) {
        return getDirective(column).direction;
    }


    private Directive getDirective(int column) {
        for (int i = 0; i < sortingColumns.size(); i++) {
            Directive directive = sortingColumns.get(i);
            if (directive.column == column) {
                return directive;
            }
        }
        return EMPTY_DIRECTIVE;
    }

    private static class Directive {
        private int column;
        private SortState direction;

        public Directive(int column, SortState direction) {
            this.column = column;
            this.direction = direction;
        }
    }

    private static Directive EMPTY_DIRECTIVE = new Directive(-1, SortState.UNSORTED);

    public class TableViewSorterException extends Exception {

        public TableViewSorterException() {
            super("For sorting process, column header view holders must be " + "extended from " +
                    "AbstractSorterViewHolder class");
        }

    }

}
