package com.evrencoskun.tableview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;
import com.evrencoskun.tableview.layoutmanager.ColumnHeaderLayoutManager;
import com.evrencoskun.tableview.listener.HorizontalRecyclerViewListener;
import com.evrencoskun.tableview.listener.VerticalRecyclerViewListener;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class TableView extends FrameLayout implements ITableView {

    protected CellRecyclerView m_jCellRecyclerView;
    protected CellRecyclerView m_jColumnHeaderRecyclerView;
    protected CellRecyclerView m_jRowHeaderRecyclerView;

    protected AbstractTableAdapter m_iTableAdapter;

    private VerticalRecyclerViewListener m_jVerticalRecyclerListener;
    private HorizontalRecyclerViewListener m_jHorizontalRecyclerViewListener;

    private ColumnHeaderLayoutManager m_iColumnHeaderLayoutManager;
    private LinearLayoutManager m_jRowHeaderLayoutManager;
    private CellLayoutManager m_iCellLayoutManager;

    private int m_nRowHeaderWidth;
    private int m_nColumnHeaderHeight;

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
        m_jColumnHeaderRecyclerView = createColumnHeaderRecyclerView();
        m_jRowHeaderRecyclerView = createRowHeaderRecyclerView();
        m_jCellRecyclerView = createCellRecyclerView();

        // Add Views
        addView(m_jColumnHeaderRecyclerView);
        addView(m_jRowHeaderRecyclerView);
        addView(m_jCellRecyclerView);

        initializeListeners();
    }

    protected void initializeListeners() {
        // It handles Vertical scroll listener
        m_jVerticalRecyclerListener = new VerticalRecyclerViewListener(this);

        // Set this listener both of Cell RecyclerView and RowHeader RecyclerView
        m_jRowHeaderRecyclerView.addOnItemTouchListener(m_jVerticalRecyclerListener);
        m_jCellRecyclerView.addOnItemTouchListener(m_jVerticalRecyclerListener);

        // It handles Horizontal scroll listener
        m_jHorizontalRecyclerViewListener = new HorizontalRecyclerViewListener(this);
        // Set scroll listener to be able to scroll all rows synchrony.
        m_jColumnHeaderRecyclerView.addOnItemTouchListener(m_jHorizontalRecyclerViewListener);
    }

    protected CellRecyclerView createColumnHeaderRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());

        // Set layout manager
        recyclerView.setLayoutManager(getColumnHeaderLayoutManager());

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                m_nColumnHeaderHeight);
        layoutParams.leftMargin = m_nRowHeaderWidth;
        recyclerView.setLayoutParams(layoutParams);

        // Add vertical item decoration to display column line
        recyclerView.addItemDecoration(createItemDecoration(DividerItemDecoration.HORIZONTAL));
        return recyclerView;
    }

    protected CellRecyclerView createRowHeaderRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());

        // Set layout manager
        recyclerView.setLayoutManager(getRowHeaderLayoutManager());

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(m_nRowHeaderWidth, LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = m_nColumnHeaderHeight;
        recyclerView.setLayoutParams(layoutParams);

        // Add vertical item decoration to display row line
        recyclerView.addItemDecoration(createItemDecoration(DividerItemDecoration.VERTICAL));

        return recyclerView;
    }

    protected CellRecyclerView createCellRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());

        // Disable multitouch
        recyclerView.setMotionEventSplittingEnabled(false);

        // Set layout manager
        recyclerView.setLayoutManager(getCellLayoutManager());

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

            // set adapters
            if (m_jColumnHeaderRecyclerView != null) {
                m_jColumnHeaderRecyclerView.setAdapter(m_iTableAdapter
                        .getColumnHeaderRecyclerViewAdapter());
            }
            if (m_jRowHeaderRecyclerView != null) {
                m_jRowHeaderRecyclerView.setAdapter(m_iTableAdapter
                        .getRowHeaderRecyclerViewAdapter());
            }
            if (m_jCellRecyclerView != null) {
                m_jCellRecyclerView.setAdapter(m_iTableAdapter.getCellRecyclerViewAdapter());
            }
        }
    }

    @Override
    public CellRecyclerView getCellRecyclerView() {
        return m_jCellRecyclerView;
    }

    @Override
    public CellRecyclerView getColumnHeaderRecyclerView() {
        return m_jColumnHeaderRecyclerView;
    }

    @Override
    public CellRecyclerView getRowHeaderRecyclerView() {
        return m_jRowHeaderRecyclerView;
    }

    @Override
    public ColumnHeaderLayoutManager getColumnHeaderLayoutManager() {
        if (m_iColumnHeaderLayoutManager == null) {
            m_iColumnHeaderLayoutManager = new ColumnHeaderLayoutManager(getContext());
        }
        return m_iColumnHeaderLayoutManager;
    }

    @Override
    public CellLayoutManager getCellLayoutManager() {
        if (m_iCellLayoutManager == null) {
            m_iCellLayoutManager = new CellLayoutManager(getContext(), this);
        }
        return m_iCellLayoutManager;
    }

    @Override
    public LinearLayoutManager getRowHeaderLayoutManager() {
        if (m_jRowHeaderLayoutManager == null) {
            m_jRowHeaderLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager
                    .VERTICAL, false);
        }
        return m_jRowHeaderLayoutManager;
    }

    @Override
    public HorizontalRecyclerViewListener getHorizontalRecyclerViewListener() {
        return m_jHorizontalRecyclerViewListener;
    }

    @Override
    public VerticalRecyclerViewListener getVerticalRecyclerViewListener() {
        return m_jVerticalRecyclerListener;
    }
}
