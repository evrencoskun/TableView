package com.evrencoskun.tableview.adapter;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by theapache64 on 25/1/18.
 */

public abstract class AbstractTableAdapterWithGhostableColumns<CH extends GhostableColumn, RH, C> extends AbstractTableAdapter<CH, RH, C> {

    private String[] ghostColumns;

    public AbstractTableAdapterWithGhostableColumns(Context p_jContext) {
        super(p_jContext);
    }

    public void setGhostColumns(String... ghostColumns) {
        this.ghostColumns = ghostColumns;
    }

    @Override
    public void setAllItems(List<CH> p_jColumnHeaderItems, List<RH> p_jRowHeaderItems, List<List<C>> p_jCellItems) {

        if (ghostColumns != null) {

            System.out.println("Ghost columns found: " + Arrays.toString(ghostColumns));

            //hiding each column
            for (final String ghostColumn : ghostColumns) {

                //Finding index of column
                int colIndex = -1;
                for (int i = 0; i < p_jColumnHeaderItems.size(); i++) {

                    final CH header = p_jColumnHeaderItems.get(i);

                    System.out.println("Header: " + header.toString());

                    if (header.getColumnName().equals(ghostColumn)) {
                        colIndex = i;
                        break;
                    }
                }

                if (colIndex != -1) {

                    //Removing header
                    p_jColumnHeaderItems.remove(colIndex);

                    //Removing column from rows
                    for (final List<C> row : p_jCellItems) {
                        row.remove(colIndex);
                    }

                } else {
                    Log.e("TableView", "Ghost column not found in result : " + ghostColumn);
                }

            }

        }

        super.setAllItems(p_jColumnHeaderItems, p_jRowHeaderItems, p_jCellItems);
    }
}
