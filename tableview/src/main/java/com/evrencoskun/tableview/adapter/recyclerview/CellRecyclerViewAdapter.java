package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.R;
import com.evrencoskun.tableview.adapter.ITableAdapter;
import com.evrencoskun.tableview.layoutmanager.ColumnLayoutManager;
import com.evrencoskun.tableview.listener.CellRecyclerViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class CellRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {

    private static final String LOG_TAG = CellRecyclerViewAdapter.class.getSimpleName();

    private List<RecyclerView.Adapter> m_jAdapterList;

    private ITableAdapter m_iTableAdapter;

    private int mXPosition = 0;

    private final DividerItemDecoration m_jCellItemDecoration;
    private CellRecyclerViewListener listener;

    public CellRecyclerViewAdapter(Context context, List<C> p_jItemList, ITableAdapter
            p_iTableAdapter) {
        super(context, p_jItemList);
        this.m_iTableAdapter = p_iTableAdapter;

        // Initialize the array
        m_jAdapterList = new ArrayList<>();

        // Create Item decoration
        m_jCellItemDecoration = createCellItemDecoration();


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a RecyclerView as a Row of the CellRecyclerView
        CellRecyclerView jRecyclerView = new CellRecyclerView(m_jContext, m_iTableAdapter
                .getTableView());

        // Add divider
        jRecyclerView.addItemDecoration(m_jCellItemDecoration);


        if (m_iTableAdapter.getTableView() != null) {
            // set touch listener to scroll synchronously
            if (listener == null) {
                listener = new CellRecyclerViewListener(this, m_iTableAdapter.getTableView());
            }
            jRecyclerView.addOnItemTouchListener(listener);

            // Set the Column layout manager that helps the fit width of the cell and column header
            // and it also helps to locate the scroll position of the horizontal recyclerView
            // which is row recyclerView
            ColumnLayoutManager layoutManager = new ColumnLayoutManager(m_jContext,
                    m_iTableAdapter.getTableView().getColumnHeaderRecyclerView().getLayoutManager
                            (), listener);
            jRecyclerView.setLayoutManager(layoutManager);
        }

        return new CellColumnViewHolder(jRecyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof CellColumnViewHolder)) {
            return;
        }

        CellColumnViewHolder viewHolder = (CellColumnViewHolder) holder;
        // Set adapter to the RecyclerView
        List<C> rowList = (List<C>) m_jItemList.get(position);

        CellColumnRecyclerViewAdapter viewAdapter = new CellColumnRecyclerViewAdapter(m_jContext,
                rowList, m_iTableAdapter, position);
        viewHolder.m_jRecyclerView.setAdapter(viewAdapter);

        // Add the adapter to the list
        m_jAdapterList.add(viewAdapter);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        CellColumnViewHolder viewHolder = (CellColumnViewHolder) holder;
        //viewHolder.m_jRecyclerView.scrollBy(mXPosition, 0);
        //Log.e(LOG_TAG, "Attached " + viewHolder.m_jRecyclerView);
    }


    private static class CellColumnViewHolder extends RecyclerView.ViewHolder {
        public final RecyclerView m_jRecyclerView;

        public CellColumnViewHolder(View itemView) {
            super(itemView);
            m_jRecyclerView = (RecyclerView) itemView;
        }
    }

    private DividerItemDecoration createCellItemDecoration() {
        Drawable mDivider = ContextCompat.getDrawable(m_jContext, R.drawable.cell_line_divider);

        DividerItemDecoration jItemDecoration = new DividerItemDecoration(m_jContext,
                DividerItemDecoration.HORIZONTAL);
        jItemDecoration.setDrawable(mDivider);
        return jItemDecoration;
    }


    public void notifyCellDataSetChanged() {
        if (m_jAdapterList != null && !m_jAdapterList.isEmpty()) {
            for (RecyclerView.Adapter adapter : m_jAdapterList) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void setXPosition(int p_nXPosition) {
        //Log.e(LOG_TAG, p_nXPosition + " x position has been set");
        this.mXPosition = p_nXPosition;

    }
}
