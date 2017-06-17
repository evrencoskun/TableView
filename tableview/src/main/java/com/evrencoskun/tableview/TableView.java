package com.evrencoskun.tableview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.listener.ColumnScrollListener;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class TableView extends FrameLayout {

    protected View dividerLine;
    protected RecyclerView m_jCellRecyclerView;
    protected RecyclerView m_jColumnHeaderRecyclerView;
    protected RecyclerView m_jRowHeaderRecyclerView;

    protected AbstractTableAdapter m_iTableAdapter;

    private int m_nRowHeaderWidth;
    private int m_nColumnHeaderHeight;

    private ColumnScrollListener m_jColumnScrollListener;

    public TableView(@NonNull Context context) {
        super(context);
        initialize();
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        // initialize default size
        m_nRowHeaderWidth = (int) getResources().getDimension(R.dimen.default_row_header_width);
        m_nColumnHeaderHeight = (int) getResources().getDimension(R.dimen.default_row_header_width);

        // Create Views
        m_jCellRecyclerView = createCellRecyclerView();
        m_jColumnHeaderRecyclerView = createColumnHeaderRecyclerView();
        m_jRowHeaderRecyclerView = createRowHeaderRecyclerView();
        dividerLine = createDividerToLeftHeader();

        // Add Views
        addView(m_jCellRecyclerView);
        addView(m_jColumnHeaderRecyclerView);
        addView(m_jRowHeaderRecyclerView);
        addView(dividerLine);
    }


    protected RecyclerView createColumnHeaderRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(getContext());
        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                m_nColumnHeaderHeight);
        layoutParams.leftMargin = m_nRowHeaderWidth;
        recyclerView.setLayoutParams(layoutParams);

        // Set scroll listener to be able to scroll all rows synchrony.
        m_jColumnScrollListener = new ColumnScrollListener(m_jCellRecyclerView.getLayoutManager()
                , recyclerView);

        // Set scroll listener to be able to scroll all rows synchrony.
        recyclerView.addOnScrollListener(m_jColumnScrollListener);

        // Add vertical item decoration to display column line
        recyclerView.addItemDecoration(createItemDecoration(DividerItemDecoration.HORIZONTAL));
        return recyclerView;
    }

    protected RecyclerView createRowHeaderRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(getContext());
        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // Set layout params
        LayoutParams layoutParams = new LayoutParams(m_nRowHeaderWidth, LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = m_nColumnHeaderHeight;
        recyclerView.setLayoutParams(layoutParams);

        // Add vertical item decoration to display row line
        recyclerView.addItemDecoration(createItemDecoration(DividerItemDecoration.VERTICAL));

        //recyclerView.addOnScrollListener(contentScrollListener);
        return recyclerView;
    }

    protected RecyclerView createCellRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(getContext());

        // Set layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        layoutParams.leftMargin = m_nRowHeaderWidth;
        layoutParams.topMargin = m_nColumnHeaderHeight;
        recyclerView.setLayoutParams(layoutParams);

        // Add vertical item decoration to display row line on center recycler view
        recyclerView.addItemDecoration(createItemDecoration(DividerItemDecoration.VERTICAL));
        return recyclerView;
    }

    protected View createDividerToLeftHeader() {
        View view = new View(getContext());
        view.setVisibility(GONE);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color
                .default_background_line_color));
        // Set layout params
        LayoutParams layoutParams = new LayoutParams(1, LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = m_nRowHeaderWidth;
        view.setLayoutParams(layoutParams);
        return view;
    }

    private DividerItemDecoration createItemDecoration(int orientation) {
        Drawable mDivider = ContextCompat.getDrawable(getContext(), R.drawable.cell_line_divider);

        DividerItemDecoration jItemDecoration = new DividerItemDecoration(getContext(),
                orientation);
        jItemDecoration.setDrawable(mDivider);
        return jItemDecoration;
    }


    public void setAdapter(AbstractTableAdapter p_iTableAdapter) {
        if (p_iTableAdapter != null) {
            this.m_iTableAdapter = p_iTableAdapter;
            this.m_iTableAdapter.setRowHeaderWidth(m_nRowHeaderWidth);
            this.m_iTableAdapter.setColumnHeaderHeight(m_nColumnHeaderHeight);
            this.m_iTableAdapter.setTableView(this);
            this.m_iTableAdapter.setColumnScrollListener(m_jColumnScrollListener);

            // set adapters
            if (m_jRowHeaderRecyclerView != null) {
                m_jRowHeaderRecyclerView.setAdapter(m_iTableAdapter
                        .getRowHeaderRecyclerViewAdapter());
            }
            if (m_jColumnHeaderRecyclerView != null) {
                m_jColumnHeaderRecyclerView.setAdapter(m_iTableAdapter
                        .getColumnHeaderRecyclerViewAdapter());

                // Send column header layout manager to cell layout manager
                // to be able to fit each of items width the same size
                m_iTableAdapter.setColumnHeaderLayoutManager(m_jColumnHeaderRecyclerView
                        .getLayoutManager());
            }
            if (m_jCellRecyclerView != null) {
                m_jCellRecyclerView.setAdapter(m_iTableAdapter.getCellRecyclerViewAdapter());
            }
        }
    }
}
