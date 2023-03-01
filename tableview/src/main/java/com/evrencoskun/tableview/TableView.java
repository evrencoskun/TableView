/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.evrencoskun.tableview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.filter.Filter;
import com.evrencoskun.tableview.handler.ColumnSortHandler;
import com.evrencoskun.tableview.handler.ColumnWidthHandler;
import com.evrencoskun.tableview.handler.FilterHandler;
import com.evrencoskun.tableview.handler.PreferencesHandler;
import com.evrencoskun.tableview.handler.ScrollHandler;
import com.evrencoskun.tableview.handler.SelectionHandler;
import com.evrencoskun.tableview.handler.VisibilityHandler;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;
import com.evrencoskun.tableview.layoutmanager.ColumnHeaderLayoutManager;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.evrencoskun.tableview.listener.TableViewLayoutChangeListener;
import com.evrencoskun.tableview.listener.itemclick.ColumnHeaderRecyclerViewItemClickListener;
import com.evrencoskun.tableview.listener.itemclick.RowHeaderRecyclerViewItemClickListener;
import com.evrencoskun.tableview.listener.scroll.HorizontalRecyclerViewListener;
import com.evrencoskun.tableview.listener.scroll.VerticalRecyclerViewListener;
import com.evrencoskun.tableview.preference.SavedState;
import com.evrencoskun.tableview.sort.SortState;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class TableView extends FrameLayout implements ITableView {
    @NonNull
    protected CellRecyclerView mCellRecyclerView;
    @NonNull
    protected CellRecyclerView mColumnHeaderRecyclerView;
    @NonNull
    protected CellRecyclerView mRowHeaderRecyclerView;
    @Nullable
    protected AbstractTableAdapter mTableAdapter;
    @Nullable
    private ITableViewListener mTableViewListener;
    @NonNull
    private VerticalRecyclerViewListener mVerticalRecyclerListener;
    @NonNull
    private HorizontalRecyclerViewListener mHorizontalRecyclerViewListener;
    @NonNull
    private ColumnHeaderLayoutManager mColumnHeaderLayoutManager;
    @NonNull
    private LinearLayoutManager mRowHeaderLayoutManager;
    @NonNull
    private CellLayoutManager mCellLayoutManager;
    @NonNull
    private DividerItemDecoration mVerticalItemDecoration;
    @NonNull
    private DividerItemDecoration mHorizontalItemDecoration;
    @NonNull
    private SelectionHandler mSelectionHandler;
    @Nullable
    private ColumnSortHandler mColumnSortHandler;
    @NonNull
    private VisibilityHandler mVisibilityHandler;
    @NonNull
    private ScrollHandler mScrollHandler;
    @Nullable
    private FilterHandler mFilterHandler;
    @NonNull
    private PreferencesHandler mPreferencesHandler;
    @NonNull
    private ColumnWidthHandler mColumnWidthHandler;

    private int mRowHeaderWidth;
    private int mColumnHeaderHeight;

    private int mSelectedColor;
    private int mUnSelectedColor;
    private int mShadowColor;
    private int mSeparatorColor = -1;

    private boolean mHasFixedWidth;
    private boolean mIgnoreSelectionColors;
    private boolean mShowHorizontalSeparators = true;
    private boolean mShowVerticalSeparators = true;
    private boolean mAllowClickInsideCell = false;
    private boolean mAllowClickInsideRowHeader = false;
    private boolean mAllowClickInsideColumnHeader = false;
    private boolean mIsSortable;
    private boolean mShowCornerView = false;

    private CornerViewLocation mCornerViewLocation;

    private boolean mReverseLayout = false;

    public TableView(@NonNull Context context) {
        super(context);
        initialDefaultValues(null);
        initialize();
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialDefaultValues(attrs);
        initialize();
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialDefaultValues(null);
        initialize();
    }

    /**
     * Two Part class construction<br>
     * Allows you to set various properties before class initialization if {@code intialize = false}<br>
     * Allowing more control when programmically creating the class
     *
     * @param context
     * @param initialize {@code false} to not call second part of class construction
     *
     * <p><b>Note:</b> If initialize is false you need to call {@code initilize()} method yourself.
     *
     */
    public TableView(@NonNull Context context, boolean initialize) {
        super(context);
        initialDefaultValues(null);
        if (initialize) initialize();
    }

    private void initialDefaultValues(@Nullable AttributeSet attrs) {
        // Dimensions
        mRowHeaderWidth = (int) getResources().getDimension(R.dimen.default_row_header_width);
        mColumnHeaderHeight = (int) getResources().getDimension(R.dimen
                .default_column_header_height);

        // Cornerview location
        mCornerViewLocation = ITableView.CornerViewLocation.TOP_LEFT;

        // Reverse Layout
        mReverseLayout = false;

        // Colors
        mSelectedColor = ContextCompat.getColor(getContext(), R.color
                .table_view_default_selected_background_color);
        mUnSelectedColor = ContextCompat.getColor(getContext(), R.color
                .table_view_default_unselected_background_color);
        mShadowColor = ContextCompat.getColor(getContext(), R.color
                .table_view_default_shadow_background_color);

        if (attrs == null) {
            // That means TableView is created programmatically.
            return;
        }

        // Get values from xml attributes
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable
                .TableView, 0, 0);
        try {
            // Dimensions
            mRowHeaderWidth = (int) a.getDimension(R.styleable.TableView_row_header_width,
                    mRowHeaderWidth);
            mColumnHeaderHeight = (int) a.getDimension(R.styleable
                    .TableView_column_header_height, mColumnHeaderHeight);

            // CornerView location
            mCornerViewLocation = CornerViewLocation.fromId(a.getInt(R.styleable.TableView_corner_view_location, 0));

            // Reverse Layout
            mReverseLayout = a.getBoolean(R.styleable.TableView_reverse_layout, mReverseLayout);

            // Colors
            mSelectedColor = a.getColor(R.styleable.TableView_selected_color, mSelectedColor);
            mUnSelectedColor = a.getColor(R.styleable.TableView_unselected_color, mUnSelectedColor);
            mShadowColor = a.getColor(R.styleable.TableView_shadow_color, mShadowColor);
            mSeparatorColor = a.getColor(R.styleable.TableView_separator_color, ContextCompat
                    .getColor(getContext(), R.color.table_view_default_separator_color));

            // Booleans
            mShowVerticalSeparators = a.getBoolean(R.styleable.TableView_show_vertical_separator,
                    mShowVerticalSeparators);
            mShowHorizontalSeparators = a.getBoolean(R.styleable
                    .TableView_show_horizontal_separator, mShowHorizontalSeparators);
            mAllowClickInsideCell = a.getBoolean(R.styleable.TableView_allow_click_inside_cell,
                    mAllowClickInsideCell);
            mAllowClickInsideRowHeader = a.getBoolean(R.styleable.TableView_allow_click_inside_row_header,
                    mAllowClickInsideRowHeader);
            mAllowClickInsideColumnHeader = a.getBoolean(R.styleable.TableView_allow_click_inside_column_header,
                    mAllowClickInsideColumnHeader);

        } finally {
            a.recycle();
        }
    }

    /**
     * Second Part of class construction
     *
     * <p><b>Note:</b> This should only be called directly if the class was constructed
     * with initialize boolean set to {@code false}
     */
    public void initialize() {

        // Create Views
        mColumnHeaderRecyclerView = createColumnHeaderRecyclerView();
        mRowHeaderRecyclerView = createRowHeaderRecyclerView();
        mCellRecyclerView = createCellRecyclerView();

        // Set some Id to help in identification
        mColumnHeaderRecyclerView.setId(R.id.ColumnHeaderRecyclerView);
        mRowHeaderRecyclerView.setId(R.id.RowHeaderRecyclerView);
        mCellRecyclerView.setId(R.id.CellRecyclerView);

        // Add Views
        addView(mColumnHeaderRecyclerView);
        addView(mRowHeaderRecyclerView);
        addView(mCellRecyclerView);

        // Create Handlers
        mSelectionHandler = new SelectionHandler(this);
        mVisibilityHandler = new VisibilityHandler(this);
        mScrollHandler = new ScrollHandler(this);
        mPreferencesHandler = new PreferencesHandler(this);
        mColumnWidthHandler = new ColumnWidthHandler(this);

        initializeListeners();
    }

    protected void initializeListeners() {

        // --- Listeners to help Scroll synchronously ---
        // It handles Vertical scroll listener
        mVerticalRecyclerListener = new VerticalRecyclerViewListener(this);

        // Set this listener both of Cell RecyclerView and RowHeader RecyclerView
        mRowHeaderRecyclerView.addOnItemTouchListener(mVerticalRecyclerListener);
        mCellRecyclerView.addOnItemTouchListener(mVerticalRecyclerListener);

        // It handles Horizontal scroll listener
        mHorizontalRecyclerViewListener = new HorizontalRecyclerViewListener(this);
        // Set scroll listener to be able to scroll all rows synchrony.
        mColumnHeaderRecyclerView.addOnItemTouchListener(mHorizontalRecyclerViewListener);


        // --- Listeners to help item clicks ---
        // Create item click listeners

        // Add item click listener for column header recyclerView
        if (mAllowClickInsideColumnHeader) {
            ColumnHeaderRecyclerViewItemClickListener columnHeaderRecyclerViewItemClickListener = new ColumnHeaderRecyclerViewItemClickListener
                    (mColumnHeaderRecyclerView, this);
            mColumnHeaderRecyclerView.addOnItemTouchListener(columnHeaderRecyclerViewItemClickListener);
        }

        // Add item click listener for row header recyclerView
        if (mAllowClickInsideRowHeader) {
            RowHeaderRecyclerViewItemClickListener rowHeaderRecyclerViewItemClickListener = new RowHeaderRecyclerViewItemClickListener
                    (mRowHeaderRecyclerView, this);
            mRowHeaderRecyclerView.addOnItemTouchListener(rowHeaderRecyclerViewItemClickListener);
        }


        // Add Layout change listener both of Column Header  & Cell recyclerView to detect
        // changing size
        // For some case, it is pretty necessary.
        TableViewLayoutChangeListener layoutChangeListener = new TableViewLayoutChangeListener
                (this);
        mColumnHeaderRecyclerView.addOnLayoutChangeListener(layoutChangeListener);
        mCellRecyclerView.addOnLayoutChangeListener(layoutChangeListener);

    }

    @NonNull
    protected CellRecyclerView createColumnHeaderRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());

        // Set layout manager
        recyclerView.setLayoutManager(getColumnHeaderLayoutManager());

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                mColumnHeaderHeight, getGravity());
        // If the corner is on the right the margin needs to be on the right
        if (mCornerViewLocation == CornerViewLocation.TOP_RIGHT || mCornerViewLocation == CornerViewLocation.BOTTOM_RIGHT) {
            layoutParams.rightMargin = mRowHeaderWidth;
        } else {
            layoutParams.leftMargin = mRowHeaderWidth;
        }

        recyclerView.setLayoutParams(layoutParams);

        if (isShowHorizontalSeparators()) {
            // Add vertical item decoration to display column line
            recyclerView.addItemDecoration(getHorizontalItemDecoration());
        }

        return recyclerView;
    }

    @NonNull
    protected CellRecyclerView createRowHeaderRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());

        // Set layout manager
        recyclerView.setLayoutManager(getRowHeaderLayoutManager());

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(mRowHeaderWidth, LayoutParams.WRAP_CONTENT, getGravity());
        // If the corner is on the bottom the margin needs to be on the bottom
        if (mCornerViewLocation == CornerViewLocation.BOTTOM_LEFT || mCornerViewLocation == CornerViewLocation.BOTTOM_RIGHT) {
            layoutParams.bottomMargin = mColumnHeaderHeight;
        } else {
            layoutParams.topMargin = mColumnHeaderHeight;
        }
        recyclerView.setLayoutParams(layoutParams);


        if (isShowVerticalSeparators()) {
            // Add vertical item decoration to display row line
            recyclerView.addItemDecoration(getVerticalItemDecoration());
        }

        return recyclerView;
    }

    @NonNull
    protected CellRecyclerView createCellRecyclerView() {
        CellRecyclerView recyclerView = new CellRecyclerView(getContext());

        // Disable multitouch
        recyclerView.setMotionEventSplittingEnabled(false);

        // Set layout manager
        recyclerView.setLayoutManager(getCellLayoutManager());

        // Set layout params
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT, getGravity());
        // If the corner is on the right the margin needs to be on the right
        if (mCornerViewLocation == CornerViewLocation.TOP_RIGHT || mCornerViewLocation == CornerViewLocation.BOTTOM_RIGHT) {
            layoutParams.rightMargin = mRowHeaderWidth;
        } else {
            layoutParams.leftMargin = mRowHeaderWidth;
        }
        // If the corner is on the bottom the margin needs to be on the bottom
        if (mCornerViewLocation == CornerViewLocation.BOTTOM_LEFT || mCornerViewLocation == CornerViewLocation.BOTTOM_RIGHT) {
            layoutParams.bottomMargin = mColumnHeaderHeight;
        } else {
            layoutParams.topMargin = mColumnHeaderHeight;
        }
        recyclerView.setLayoutParams(layoutParams);

        if (isShowVerticalSeparators()) {
            // Add vertical item decoration to display row line on center recycler view
            recyclerView.addItemDecoration(getVerticalItemDecoration());
        }

        return recyclerView;
    }

    public <CH, RH, C> void setAdapter(@Nullable AbstractTableAdapter<CH, RH, C> tableAdapter) {
        if (tableAdapter != null) {
            this.mTableAdapter = tableAdapter;
            this.mTableAdapter.setRowHeaderWidth(mRowHeaderWidth);
            this.mTableAdapter.setColumnHeaderHeight(mColumnHeaderHeight);
            this.mTableAdapter.setTableView(this);

            // set adapters
            mColumnHeaderRecyclerView.setAdapter(mTableAdapter.getColumnHeaderRecyclerViewAdapter());
            mRowHeaderRecyclerView.setAdapter(mTableAdapter.getRowHeaderRecyclerViewAdapter());
            mCellRecyclerView.setAdapter(mTableAdapter.getCellRecyclerViewAdapter());

            // Create Sort Handler
            mColumnSortHandler = new ColumnSortHandler(this);

            // Create Filter Handler
            mFilterHandler = new FilterHandler<>(this);
        }
    }

    @Override
    public boolean hasFixedWidth() {
        return mHasFixedWidth;
    }

    public void setHasFixedWidth(boolean hasFixedWidth) {
        this.mHasFixedWidth = hasFixedWidth;

        // RecyclerView has also the same control to provide better performance.
        mColumnHeaderRecyclerView.setHasFixedSize(hasFixedWidth);
    }

    @Override
    public boolean isIgnoreSelectionColors() {
        return mIgnoreSelectionColors;
    }

    public void setIgnoreSelectionColors(boolean ignoreSelectionColor) {
        this.mIgnoreSelectionColors = ignoreSelectionColor;
    }

    @Override
    public boolean isShowHorizontalSeparators() {
        return mShowHorizontalSeparators;
    }

    @Override
    public boolean isAllowClickInsideCell(){
        return mAllowClickInsideCell;
    }

    @Override
    public boolean isSortable() {
        return mIsSortable;
    }

    public void setShowHorizontalSeparators(boolean showSeparators) {
        this.mShowHorizontalSeparators = showSeparators;
    }

    @Override
    public boolean isShowVerticalSeparators() {
        return mShowVerticalSeparators;
    }

    public void setShowVerticalSeparators(boolean showSeparators) {
        this.mShowVerticalSeparators = showSeparators;
    }

    @NonNull
    @Override
    public CellRecyclerView getCellRecyclerView() {
        return mCellRecyclerView;
    }

    @NonNull
    @Override
    public CellRecyclerView getColumnHeaderRecyclerView() {
        return mColumnHeaderRecyclerView;
    }

    @NonNull
    @Override
    public CellRecyclerView getRowHeaderRecyclerView() {
        return mRowHeaderRecyclerView;
    }

    @NonNull
    @Override
    public ColumnHeaderLayoutManager getColumnHeaderLayoutManager() {
        if (mColumnHeaderLayoutManager == null) {
            mColumnHeaderLayoutManager = new ColumnHeaderLayoutManager(getContext(), this);
            if (mReverseLayout) mColumnHeaderLayoutManager.setReverseLayout(true);
        }
        return mColumnHeaderLayoutManager;
    }

    @NonNull
    @Override
    public CellLayoutManager getCellLayoutManager() {
        if (mCellLayoutManager == null) {
            mCellLayoutManager = new CellLayoutManager(getContext(), this);
        }
        return mCellLayoutManager;
    }

    @NonNull
    @Override
    public LinearLayoutManager getRowHeaderLayoutManager() {
        if (mRowHeaderLayoutManager == null) {
            mRowHeaderLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager
                    .VERTICAL, false);
        }
        return mRowHeaderLayoutManager;
    }

    @NonNull
    @Override
    public HorizontalRecyclerViewListener getHorizontalRecyclerViewListener() {
        return mHorizontalRecyclerViewListener;
    }

    @NonNull
    @Override
    public VerticalRecyclerViewListener getVerticalRecyclerViewListener() {
        return mVerticalRecyclerListener;
    }

    @Nullable
    @Override
    public ITableViewListener getTableViewListener() {
        return mTableViewListener;
    }

    public void setTableViewListener(@Nullable ITableViewListener tableViewListener) {
        this.mTableViewListener = tableViewListener;
    }

    @Override
    public void sortColumn(int columnPosition, @NonNull SortState sortState) {
        mIsSortable = true;
        mColumnSortHandler.sort(columnPosition, sortState);
    }

    @Override
    public void sortRowHeader(@NonNull SortState sortState) {
        mIsSortable = true;
        mColumnSortHandler.sortByRowHeader(sortState);
    }

    @Override
    public void remeasureColumnWidth(int column) {
        // Remove calculated width value to be ready for recalculation.
        getColumnHeaderLayoutManager().removeCachedWidth(column);
        // Recalculate of the width values of the columns
        getCellLayoutManager().fitWidthSize(column, false);
    }

    @Nullable
    @Override
    public AbstractTableAdapter getAdapter() {
        return mTableAdapter;
    }

    @Override
    public void filter(@NonNull Filter filter) {
        mFilterHandler.filter(filter);
    }

    @Nullable
    @Override
    public FilterHandler getFilterHandler() {
        return mFilterHandler;
    }

    @NonNull
    @Override
    public SortState getSortingStatus(int column) {
        return mColumnSortHandler.getSortingStatus(column);
    }

    @Nullable
    @Override
    public SortState getRowHeaderSortingStatus() {
        return mColumnSortHandler.getRowHeaderSortingStatus();
    }

    @Override
    public void scrollToColumnPosition(int column) {
        mScrollHandler.scrollToColumnPosition(column);
    }

    @Override
    public void scrollToColumnPosition(int column, int offset) {
        mScrollHandler.scrollToColumnPosition(column, offset);
    }

    @Override
    public void scrollToRowPosition(int row) {
        mScrollHandler.scrollToRowPosition(row);
    }

    @Override
    public void scrollToRowPosition(int row, int offset) {
        mScrollHandler.scrollToRowPosition(row, offset);
    }

    @NonNull
    public ScrollHandler getScrollHandler() {
        return mScrollHandler;
    }

    @Override
    public void showRow(int row) {
        mVisibilityHandler.showRow(row);
    }

    @Override
    public void hideRow(int row) {
        mVisibilityHandler.hideRow(row);
    }

    @Override
    public void showAllHiddenRows() {
        mVisibilityHandler.showAllHiddenRows();
    }

    @Override
    public void clearHiddenRowList() {
        mVisibilityHandler.clearHideRowList();
    }

    @Override
    public void showColumn(int column) {
        mVisibilityHandler.showColumn(column);
    }

    @Override
    public void hideColumn(int column) {
        mVisibilityHandler.hideColumn(column);
    }

    @Override
    public boolean isColumnVisible(int column) {
        return mVisibilityHandler.isColumnVisible(column);
    }

    @Override
    public void showAllHiddenColumns() {
        mVisibilityHandler.showAllHiddenColumns();
    }

    @Override
    public void clearHiddenColumnList() {
        mVisibilityHandler.clearHideColumnList();
    }

    @Override
    public boolean isRowVisible(int row) {
        return mVisibilityHandler.isRowVisible(row);
    }

    /**
     * Returns the index of the selected row, -1 if no row is selected.
     */
    public int getSelectedRow() {
        return mSelectionHandler.getSelectedRowPosition();
    }

    public void setSelectedRow(int row) {
        // Find the row header view holder which is located on row position.
        AbstractViewHolder rowViewHolder = (AbstractViewHolder) getRowHeaderRecyclerView()
                .findViewHolderForAdapterPosition(row);


        mSelectionHandler.setSelectedRowPosition(rowViewHolder, row);
    }

    /**
     * Returns the index of the selected column, -1 if no column is selected.
     */
    public int getSelectedColumn() {
        return mSelectionHandler.getSelectedColumnPosition();
    }

    public void setSelectedColumn(int column) {
        // Find the column view holder which is located on column position .
        AbstractViewHolder columnViewHolder = (AbstractViewHolder) getColumnHeaderRecyclerView()
                .findViewHolderForAdapterPosition(column);

        mSelectionHandler.setSelectedColumnPosition(columnViewHolder, column);
    }

    public void setSelectedCell(int column, int row) {
        // Find the cell view holder which is located on x,y (column,row) position.
        AbstractViewHolder cellViewHolder = getCellLayoutManager().getCellViewHolder(column, row);

        mSelectionHandler.setSelectedCellPositions(cellViewHolder, column, row);
    }

    @NonNull
    @Override
    public SelectionHandler getSelectionHandler() {
        return mSelectionHandler;
    }

    @Nullable
    @Override
    public ColumnSortHandler getColumnSortHandler() {
        return mColumnSortHandler;
    }

    @NonNull
    @Override
    public VisibilityHandler getVisibilityHandler() {
        return mVisibilityHandler;
    }

    @NonNull
    @Override
    public DividerItemDecoration getHorizontalItemDecoration() {
        if (mHorizontalItemDecoration == null) {
            mHorizontalItemDecoration = createItemDecoration(DividerItemDecoration.HORIZONTAL);
        }
        return mHorizontalItemDecoration;
    }

    @NonNull
    @Override
    public DividerItemDecoration getVerticalItemDecoration() {
        if (mVerticalItemDecoration == null) {
            mVerticalItemDecoration = createItemDecoration(DividerItemDecoration.VERTICAL);
        }
        return mVerticalItemDecoration;
    }

    @NonNull
    protected DividerItemDecoration createItemDecoration(int orientation) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), orientation);
        Drawable divider = ContextCompat.getDrawable(getContext(), R.drawable.cell_line_divider);
        if (divider == null) {
            return itemDecoration;
        }

        // That means; There is a custom separator color from user.
        if (mSeparatorColor != -1) {
            // Change its color
            divider.setColorFilter(mSeparatorColor, PorterDuff.Mode.SRC_ATOP);
        }

        itemDecoration.setDrawable(divider);
        return itemDecoration;
    }

    /**
     * This method helps to change default selected color programmatically.
     *
     * @param selectedColor It must be Color int.
     */
    public void setSelectedColor(@ColorInt int selectedColor) {
        this.mSelectedColor = selectedColor;
    }

    @ColorInt
    @Override
    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSeparatorColor(@ColorInt int mSeparatorColor) {
        this.mSeparatorColor = mSeparatorColor;
    }

    @ColorInt
    @Override
    public int getSeparatorColor() {
        return mSeparatorColor;
    }

    /**
     * This method helps to change default unselected color programmatically.
     *
     * @param unSelectedColor It must be Color int.
     */
    public void setUnSelectedColor(@ColorInt int unSelectedColor) {
        this.mUnSelectedColor = unSelectedColor;
    }

    @ColorInt
    @Override
    public int getUnSelectedColor() {
        return mUnSelectedColor;
    }

    public void setShadowColor(@ColorInt int shadowColor) {
        this.mShadowColor = shadowColor;
    }

    @Override
    public @ColorInt
    int getShadowColor() {
        return mShadowColor;
    }

    /**
     * get row header width
     *
     * @return size in pixel
     */
    @Override
    public int getRowHeaderWidth() {
        return mRowHeaderWidth;
    }

    /**
     * set RowHeaderWidth
     *
     * @param rowHeaderWidth in pixel
     */
    @Override
    public void setRowHeaderWidth(int rowHeaderWidth) {
        this.mRowHeaderWidth = rowHeaderWidth;

        // Update RowHeader layout width
        ViewGroup.LayoutParams layoutParamsRow = mRowHeaderRecyclerView.getLayoutParams();
        layoutParamsRow.width = rowHeaderWidth;
        mRowHeaderRecyclerView.setLayoutParams(layoutParamsRow);
        mRowHeaderRecyclerView.requestLayout();

        // Update ColumnHeader left margin
        LayoutParams layoutParamsColumn = (LayoutParams) mColumnHeaderRecyclerView.getLayoutParams();
        // If the corner is on the right the margin needs to be on the right
        if (mCornerViewLocation == CornerViewLocation.TOP_RIGHT || mCornerViewLocation == CornerViewLocation.BOTTOM_RIGHT) {
            layoutParamsColumn.rightMargin = rowHeaderWidth;
        } else {
            layoutParamsColumn.leftMargin = rowHeaderWidth;
        }
        mColumnHeaderRecyclerView.setLayoutParams(layoutParamsColumn);
        mColumnHeaderRecyclerView.requestLayout();

        // Update Cells left margin
        LayoutParams layoutParamsCell = (LayoutParams) mCellRecyclerView.getLayoutParams();
        if (mCornerViewLocation == CornerViewLocation.TOP_RIGHT || mCornerViewLocation == CornerViewLocation.BOTTOM_RIGHT) {
            layoutParamsCell.rightMargin = rowHeaderWidth;
        } else {
            layoutParamsCell.leftMargin = rowHeaderWidth;
        }
        mCellRecyclerView.setLayoutParams(layoutParamsCell);
        mCellRecyclerView.requestLayout();

        if (getAdapter() != null) {
            // update CornerView size
            getAdapter().setRowHeaderWidth(rowHeaderWidth);
        }
    }

    public void setColumnWidth(int columnPosition, int width) {
        mColumnWidthHandler.setColumnWidth(columnPosition, width);
    }

    public void setShowCornerView(boolean showCornerView){
        mShowCornerView = showCornerView;
    }

    public boolean getShowCornerView(){
        return mShowCornerView;
    }

    public CornerViewLocation getCornerViewLocation() { return mCornerViewLocation; }

    @Override
    public void setCornerViewLocation(CornerViewLocation cornerViewLocation) {
        mCornerViewLocation = cornerViewLocation;
    }

    public int getGravity() {
        int gravity;
        switch (mCornerViewLocation) {
            case TOP_LEFT:
                gravity = Gravity.TOP|Gravity.LEFT;
                break;
            case TOP_RIGHT:
                gravity = Gravity.TOP|Gravity.RIGHT;
                break;
            case BOTTOM_LEFT:
                gravity = Gravity.BOTTOM|Gravity.LEFT;
                break;
            case BOTTOM_RIGHT:
                gravity = Gravity.BOTTOM|Gravity.RIGHT;
                break;
            default:
                gravity = Gravity.TOP|Gravity.LEFT;
                break;
        }
        return gravity;
    }

    public boolean getReverseLayout(){ return mReverseLayout;}

    public void setReverseLayout(boolean reverseLayout) {
        mReverseLayout = reverseLayout;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        // Save all preferences of The TableView before the configuration changed.
        state.preferences = mPreferencesHandler.savePreferences();
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        // Reload the preferences
        mPreferencesHandler.loadPreferences(savedState.preferences);
    }
}
