package com.evrencoskun.tableviewsample.tableview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableviewsample.R;
import com.evrencoskun.tableviewsample.tableview.model.Cell;
import com.evrencoskun.tableviewsample.tableview.model.ColumnHeader;
import com.evrencoskun.tableviewsample.tableview.model.RowHeader;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

    private static final String LOG_TAG = TableViewAdapter.class.getSimpleName();

    private int m_nId = 0;

    public TableViewAdapter(Context p_jContext) {
        super(p_jContext);

    }

    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(m_jContext).inflate(R.layout.table_view_cell_layout,
                parent, false);

        CellViewHolder cellViewHolder = new CellViewHolder(layout);
        cellViewHolder.itemView.setId(m_nId);
        Log.e("TableViewAdapter:", m_nId + " onCreateCellViewHolder ");
        m_nId++;
        return cellViewHolder;
    }

    @Override
    public void onBindCellViewHolder(RecyclerView.ViewHolder holder, int p_nXPosition, int
            p_nYPosition) {
        Cell cell = getCellItem(p_nYPosition, p_nXPosition);
        if (null == holder || !(holder instanceof CellViewHolder) || cell == null) {
            return;
        }

        CellViewHolder viewHolder = (CellViewHolder) holder;
        viewHolder.cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.cell_container.setTag(cell);
        viewHolder.cell_textview.setText(cell.getData());
        viewHolder.cell_textview.requestLayout();
        viewHolder.cell_container.requestLayout();
        viewHolder.itemView.requestLayout();

        Log.e(LOG_TAG, viewHolder.itemView.getId() + " getwidth: " + viewHolder.itemView.getWidth
                () + " m w: " + viewHolder.itemView.getMeasuredWidth() + " params w: " +
                viewHolder.itemView.getLayoutParams().width + " getWidth(view): " + getWidth
                (viewHolder.itemView));

        //viewHolder.setIsRecyclable(false);
    }

    static class CellViewHolder extends RecyclerView.ViewHolder {

        public final TextView cell_textview;
        public final LinearLayout cell_container;

        public CellViewHolder(View itemView) {
            super(itemView);
            cell_textview = (TextView) itemView.findViewById(R.id.cell_data);
            cell_container = (LinearLayout) itemView.findViewById(R.id.cell_container);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(m_jContext).inflate(R.layout
                .table_view_column_header_layout, parent, false);
        ColumnHeaderViewHolder columnHeaderViewHolder = new ColumnHeaderViewHolder(layout);
        return columnHeaderViewHolder;
    }

    @Override
    public void onBindColumnHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        ColumnHeader columnHeader = getColumnHeaderItem(position);
        if (null == holder || !(holder instanceof ColumnHeaderViewHolder) || columnHeader == null) {
            return;
        }
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.column_header_container.getLayoutParams().width = LinearLayout
                .LayoutParams.WRAP_CONTENT;
        columnHeaderViewHolder.column_header_textview.setText(columnHeader.getData());
        columnHeaderViewHolder.column_header_textview.requestLayout();
        columnHeaderViewHolder.column_header_container.requestLayout();
        columnHeaderViewHolder.itemView.requestLayout();

    }

    static class ColumnHeaderViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout column_header_container;
        public final TextView column_header_textview;

        public ColumnHeaderViewHolder(View itemView) {
            super(itemView);
            column_header_textview = (TextView) itemView.findViewById(R.id.column_header_textView);
            column_header_container = (LinearLayout) itemView.findViewById(R.id
                    .column_header_container);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(m_jContext).inflate(R.layout
                .table_view_row_header_layout, parent, false);
        RowHeaderViewHolder rowHeaderViewHolder = new RowHeaderViewHolder(layout);
        return rowHeaderViewHolder;
    }

    @Override
    public void onBindRowHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        RowHeader rowHeader = getRowHeaderItem(position);
        if (null == holder || !(holder instanceof RowHeaderViewHolder) || rowHeader == null) {
            return;
        }
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeader.getData());
    }

    static class RowHeaderViewHolder extends RecyclerView.ViewHolder {
        public final TextView row_header_textview;

        public RowHeaderViewHolder(View p_jItemView) {
            super(p_jItemView);
            row_header_textview = (TextView) p_jItemView.findViewById(R.id.row_header_textview);
        }
    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(m_jContext).inflate(R.layout.table_view_corner_layout, null);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        // TODO:
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        // TODO:
        return 0;
    }

    private int getWidth(View p_jView) {
        p_jView.measure(LinearLayout.LayoutParams.WRAP_CONTENT, View.MeasureSpec.makeMeasureSpec
                (p_jView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        return p_jView.getMeasuredWidth();
    }
}
