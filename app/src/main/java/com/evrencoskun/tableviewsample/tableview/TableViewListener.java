package com.evrencoskun.tableviewsample.tableview;

import android.content.Context;
import android.widget.Toast;

import com.evrencoskun.tableview.listener.ITableViewListener;

/**
 * Created by evrencoskun on 21/09/2017.
 */

public class TableViewListener implements ITableViewListener {

    private Toast m_jToast;
    private Context m_jContext;

    public TableViewListener(Context p_jContext) {
        this.m_jContext = p_jContext;
    }

    @Override
    public void onCellClicked(int p_nXPosition, int p_nYPosition) {
        showToast("Cell " + p_nXPosition + " " + p_nYPosition + " has been clicked.");
    }

    @Override
    public void onColumnHeaderClicked(int p_nXPosition) {
        showToast("Column header  " + p_nXPosition + " has been clicked.");
    }

    @Override
    public void onRowHeaderClicked(int p_nYPosition) {
        showToast("Row header " + p_nYPosition + " has been clicked.");
    }

    private void showToast(String p_strMessage) {
        if (m_jToast == null) {
            m_jToast = Toast.makeText(m_jContext, "", Toast.LENGTH_SHORT);
        }

        m_jToast.setText(p_strMessage);
        m_jToast.show();
    }
}
