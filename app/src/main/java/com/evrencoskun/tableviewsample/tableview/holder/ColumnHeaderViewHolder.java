package com.evrencoskun.tableviewsample.tableview.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableviewsample.R;

/**
 * Created by evrencoskun on 23/10/2017.
 */

public class ColumnHeaderViewHolder extends AbstractViewHolder {
    public final LinearLayout column_header_container;
    public final TextView column_header_textview;

    public ColumnHeaderViewHolder(View itemView) {
        super(itemView);
        column_header_textview = (TextView) itemView.findViewById(R.id.column_header_textView);
        column_header_container = (LinearLayout) itemView.findViewById(R.id
                .column_header_container);
    }

}
