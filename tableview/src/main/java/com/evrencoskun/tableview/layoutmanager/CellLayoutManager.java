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

package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.listener.scroll.HorizontalRecyclerViewListener;
import com.evrencoskun.tableview.util.TableViewUtils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by evrencoskun on 24/06/2017.
 */

public class CellLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = CellLayoutManager.class.getSimpleName();
    private static final int IGNORE_LEFT = -99999;

    @NonNull
    private final ColumnHeaderLayoutManager mColumnHeaderLayoutManager;

    @NonNull
    private final CellRecyclerView mRowHeaderRecyclerView;

    private HorizontalRecyclerViewListener mHorizontalListener;
    @NonNull
    private final ITableView mTableView;

    @NonNull
    private final SparseArray<SparseIntArray> mCachedWidthList = new SparseArray<>();
    //TODO: Store a single instance for both cell and column cache width values.

    private int mLastDy = 0;
    private boolean mNeedSetLeft;
    private boolean mNeedFit;

    public CellLayoutManager(@NonNull Context context, @NonNull ITableView tableView) {
        super(context);
        this.mTableView = tableView;
        this.mColumnHeaderLayoutManager = tableView.getColumnHeaderLayoutManager();
        this.mRowHeaderRecyclerView = tableView.getRowHeaderRecyclerView();

        initialize();
    }

    private void initialize() {
        this.setOrientation(VERTICAL);
        // Add new one
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);

        // initialize the instances
        if (mHorizontalListener == null) {
            mHorizontalListener = mTableView.getHorizontalRecyclerViewListener();
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State
            state) {
        if (mRowHeaderRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                !mRowHeaderRecyclerView.isScrollOthers()) {
            // CellRecyclerViews should be scrolled after the RowHeaderRecyclerView.
            // Because it is one of the main compared criterion to make each columns fit.
            mRowHeaderRecyclerView.scrollBy(0, dy);
        }

        int scroll = super.scrollVerticallyBy(dy, recycler, state);

        // It is important to determine right position to fit all columns which are the same y pos.
        mLastDy = dy;
        return scroll;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            // It is important to set it 0 to be able to know which direction is being scrolled
            mLastDy = 0;
        }
    }

    /**
     * This method helps to fit all columns which are displayed on screen.
     * Especially it will be called when TableView is scrolled on vertically.
     */
    public void fitWidthSize(boolean scrollingUp) {
        int left = mColumnHeaderLayoutManager.getFirstItemLeft();
        for (int i = mColumnHeaderLayoutManager.findFirstVisibleItemPosition(); i <
                mColumnHeaderLayoutManager.findLastVisibleItemPosition() + 1; i++) {
            left = fitSize(i, left, scrollingUp);
        }

        mNeedSetLeft = false;
    }

    /**
     * This method helps to fit a column. it will be called when TableView is scrolled on
     * horizontally.
     */
    public void fitWidthSize(int position, boolean scrollingLeft) {
        fitSize(position, IGNORE_LEFT, false);

        if (mNeedSetLeft & scrollingLeft) {
            // Works just like invoke later of swing utils.
            Handler handler = new Handler();
            handler.post(() -> fitWidthSize2(true));
        }
    }

    private int fitSize(int position, int left, boolean scrollingUp) {
        int cellRight = -1;

        int columnCacheWidth = mColumnHeaderLayoutManager.getCacheWidth(position);
        View column = mColumnHeaderLayoutManager.findViewByPosition(position);

        if (column != null) {
            // Determine default right
            cellRight = column.getLeft() + columnCacheWidth + 1;

            if (scrollingUp) {
                // Loop reverse order
                for (int i = findLastVisibleItemPosition(); i >= findFirstVisibleItemPosition();
                     i--) {
                    cellRight = fit(position, i, left, cellRight, columnCacheWidth);
                }
            } else {
                // Loop for all rows which are visible.
                for (int j = findFirstVisibleItemPosition(); j < findLastVisibleItemPosition() +
                        1; j++) {
                    cellRight = fit(position, j, left, cellRight, columnCacheWidth);
                }
            }
        } else {
            Log.e(LOG_TAG, "Warning: column couldn't found for " + position);
        }
        return cellRight;
    }

    private int fit(int xPosition, int yPosition, int left, int right, int columnCachedWidth) {
        CellRecyclerView child = (CellRecyclerView) findViewByPosition(yPosition);

        if (child != null) {
            ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) child.getLayoutManager();

            int cellCacheWidth = getCacheWidth(yPosition, xPosition);
            View cell = childLayoutManager.findViewByPosition(xPosition);

            // Control whether the cell needs to be fitted by column header or not.
            if (cell != null) {

                if (cellCacheWidth != columnCachedWidth || mNeedSetLeft) {

                    // This is just for setting width value
                    if (cellCacheWidth != columnCachedWidth) {
                        cellCacheWidth = columnCachedWidth;
                        TableViewUtils.setWidth(cell, cellCacheWidth);

                        setCacheWidth(yPosition, xPosition, cellCacheWidth);
                    }

                    // Even if the cached values are same, the left & right value wouldn't change.
                    // mNeedSetLeft & the below lines for it.
                    if (left != IGNORE_LEFT && cell.getLeft() != left) {

                        // Calculate scroll distance
                        int scrollX = Math.max(cell.getLeft(), left) - Math.min(cell.getLeft(),
                                left);

                        // Update its left
                        cell.setLeft(left);

                        int offset = mHorizontalListener.getScrollPositionOffset();

                        // It shouldn't be scroll horizontally and the problem is gotten just for
                        // first visible item.
                        if (offset > 0 && xPosition == childLayoutManager
                                .findFirstVisibleItemPosition() && getCellRecyclerViewScrollState() != RecyclerView.SCROLL_STATE_IDLE) {

                            int scrollPosition = mHorizontalListener.getScrollPosition();
                            offset = mHorizontalListener.getScrollPositionOffset() + scrollX;

                            // Update scroll position offset value
                            mHorizontalListener.setScrollPositionOffset(offset);
                            // Scroll considering to the desired value.
                            childLayoutManager.scrollToPositionWithOffset(scrollPosition, offset);
                        }
                    }

                    if (cell.getWidth() != cellCacheWidth) {
                        if (left != IGNORE_LEFT) {
                            // TODO: + 1 is for decoration item. It should be gotten from a
                            // generic method  of layoutManager
                            // Set right
                            right = cell.getLeft() + cellCacheWidth + 1;
                            cell.setRight(right);

                            childLayoutManager.layoutDecoratedWithMargins(cell, cell.getLeft(),
                                    cell.getTop(), cell.getRight(), cell.getBottom());
                        }

                        mNeedSetLeft = true;
                    }
                }
            }
        }
        return right;
    }

    /**
     * Alternative method of fitWidthSize().
     * The main difference is this method works after main thread draw the ui components.
     */
    public void fitWidthSize2(boolean scrollingLeft) {
        // The below line helps to change left & right value of the each column
        // header views
        // without using requestLayout().
        mColumnHeaderLayoutManager.customRequestLayout();

        // Get the right scroll position information from Column header RecyclerView
        int columnHeaderScrollPosition = mTableView.getColumnHeaderRecyclerView().getScrolledX();
        int columnHeaderOffset = mColumnHeaderLayoutManager.getFirstItemLeft();
        int columnHeaderFirstItem = mColumnHeaderLayoutManager.findFirstVisibleItemPosition();

        // Fit all visible columns widths
        for (int i = mColumnHeaderLayoutManager.findFirstVisibleItemPosition(); i <
                mColumnHeaderLayoutManager.findLastVisibleItemPosition() + 1; i++) {

            fitSize2(i, scrollingLeft, columnHeaderScrollPosition, columnHeaderOffset,
                    columnHeaderFirstItem);
        }

        mNeedSetLeft = false;
    }

    /**
     * Alternative method of fitWidthSize().
     * The main difference is this method works after main thread draw the ui components.
     */
    public void fitWidthSize2(int position, boolean scrollingLeft) {
        // The below line helps to change left & right value of the each column
        // header views
        // without using requestLayout().
        mColumnHeaderLayoutManager.customRequestLayout();

        // Get the right scroll position information from Column header RecyclerView
        int columnHeaderScrollPosition = mTableView.getColumnHeaderRecyclerView().getScrolledX();
        int columnHeaderOffset = mColumnHeaderLayoutManager.getFirstItemLeft();
        int columnHeaderFirstItem = mColumnHeaderLayoutManager.findFirstVisibleItemPosition();

        // Fit all visible columns widths
        fitSize2(position, scrollingLeft, columnHeaderScrollPosition, columnHeaderOffset,
                columnHeaderFirstItem);


        mNeedSetLeft = false;
    }

    private void fitSize2(int position, boolean scrollingLeft, int columnHeaderScrollPosition,
                          int columnHeaderOffset, int columnHeaderFirstItem) {
        int columnCacheWidth = mColumnHeaderLayoutManager.getCacheWidth(position);
        View column = mColumnHeaderLayoutManager.findViewByPosition(position);

        if (column != null) {
            // Loop for all rows which are visible.
            for (int j = findFirstVisibleItemPosition(); j < findLastVisibleItemPosition() + 1;
                 j++) {

                // Get CellRowRecyclerView
                CellRecyclerView child = (CellRecyclerView) findViewByPosition(j);

                if (child != null) {
                    ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) child
                            .getLayoutManager();

                    // Checking Scroll position is necessary. Because, even if they have same width
                    // values, their scroll positions can be different.
                    if (!scrollingLeft && columnHeaderScrollPosition != child.getScrolledX()) {

                        // Column Header RecyclerView has the right scroll position. So,
                        // considering it
                        childLayoutManager.scrollToPositionWithOffset(columnHeaderFirstItem,
                                columnHeaderOffset);
                    }

                    if (childLayoutManager != null) {
                        fit2(position, j, columnCacheWidth, column, childLayoutManager);
                    }
                }
            }
        }
    }

    private void fit2(int xPosition, int yPosition, int columnCachedWidth, @NonNull View column,
                      @NonNull ColumnLayoutManager childLayoutManager) {
        int cellCacheWidth = getCacheWidth(yPosition, xPosition);
        View cell = childLayoutManager.findViewByPosition(xPosition);

        // Control whether the cell needs to be fitted by column header or not.
        if (cell != null) {

            if (cellCacheWidth != columnCachedWidth || mNeedSetLeft) {

                // This is just for setting width value
                if (cellCacheWidth != columnCachedWidth) {
                    cellCacheWidth = columnCachedWidth;
                    TableViewUtils.setWidth(cell, cellCacheWidth);

                    setCacheWidth(yPosition, xPosition, cellCacheWidth);
                }

                // The left & right values of Column header can be considered. Because this
                // method will be worked
                // after drawing process of main thread.
                if (column.getLeft() != cell.getLeft() || column.getRight() != cell.getRight()) {
                    // TODO: + 1 is for decoration item. It should be gotten from a generic
                    // method  of layoutManager
                    // Set right & left values
                    cell.setLeft(column.getLeft());
                    cell.setRight(column.getRight() + 1);
                    childLayoutManager.layoutDecoratedWithMargins(cell, cell.getLeft(), cell
                            .getTop(), cell.getRight(), cell.getBottom());

                    mNeedSetLeft = true;
                }
            }
        }
    }

    public boolean shouldFitColumns(int yPosition) {

        // Scrolling horizontally
        if (getCellRecyclerViewScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

            int lastVisiblePosition = findLastVisibleItemPosition();
            CellRecyclerView lastCellRecyclerView = (CellRecyclerView) findViewByPosition
                    (lastVisiblePosition);

            if (lastCellRecyclerView != null) {
                if (yPosition == lastVisiblePosition) {
                    return true;
                } else if (lastCellRecyclerView.isScrollOthers() && yPosition ==
                        lastVisiblePosition - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void measureChildWithMargins(@NonNull View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        // If has fixed width is true, than calculation of the column width is not necessary.
        if (mTableView.hasFixedWidth()) {
            return;
        }

        int position = getPosition(child);

        ColumnLayoutManager childLayoutManager = (ColumnLayoutManager) ((CellRecyclerView) child)
                .getLayoutManager();

        // the below codes should be worked when it is scrolling vertically
        if (getCellRecyclerViewScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
            if (childLayoutManager.isNeedFit()) {

                // Scrolling up
                if (mLastDy < 0) {
                    Log.e(LOG_TAG, position + " fitWidthSize all vertically up");
                    fitWidthSize(true);
                } else {
                    // Scrolling down
                    Log.e(LOG_TAG, position + " fitWidthSize all vertically down");
                    fitWidthSize(false);
                }
                // all columns have been fitted.
                childLayoutManager.clearNeedFit();
            }

            // Set the right initialPrefetch size to improve performance
            childLayoutManager.setInitialPrefetchItemCount(childLayoutManager.getChildCount());

            // That means,populating for the first time like fetching all data to display.
            // It shouldn't be worked when it is scrolling horizontally ."getLastDx() == 0"
            // control for it.
        } else if (childLayoutManager.getLastDx() == 0 && getCellRecyclerViewScrollState() ==
                RecyclerView.SCROLL_STATE_IDLE) {

            if (childLayoutManager.isNeedFit()) {
                mNeedFit = true;

                // all columns have been fitted.
                childLayoutManager.clearNeedFit();
            }

            if (mNeedFit) {
                // for the first time to populate adapter
                if (mTableView.getRowHeaderLayoutManager().findLastVisibleItemPosition() == position) {

                    fitWidthSize2(false);
                    Log.e(LOG_TAG, position + " fitWidthSize populating data for the first time");

                    mNeedFit = false;
                }
            }
        }
    }

    @NonNull
    public AbstractViewHolder[] getVisibleCellViewsByColumnPosition(int xPosition) {
        int visibleChildCount = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
        int index = 0;
        AbstractViewHolder[] viewHolders = new AbstractViewHolder[visibleChildCount];
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {
            CellRecyclerView cellRowRecyclerView = (CellRecyclerView) findViewByPosition(i);

            AbstractViewHolder holder = (AbstractViewHolder) cellRowRecyclerView
                    .findViewHolderForAdapterPosition(xPosition);

            viewHolders[index] = holder;

            index++;
        }
        return viewHolders;
    }

    @Nullable
    public AbstractViewHolder getCellViewHolder(int xPosition, int yPosition) {
        CellRecyclerView cellRowRecyclerView = (CellRecyclerView) findViewByPosition(yPosition);

        if (cellRowRecyclerView != null) {
            return (AbstractViewHolder) cellRowRecyclerView.findViewHolderForAdapterPosition
                    (xPosition);
        }
        return null;
    }

    public void remeasureAllChild() {
        // TODO: the below code causes requestLayout() improperly called by com.evrencoskun.tableview.adapter

        for (int j = 0; j < getChildCount(); j++) {
            CellRecyclerView recyclerView = (CellRecyclerView) getChildAt(j);

            recyclerView.getLayoutParams().width = WRAP_CONTENT;
            recyclerView.requestLayout();
        }
    }

    /**
     * Allows to set cache width value for single cell item.
     */
    public void setCacheWidth(int row, int column, int width) {
        SparseIntArray cellRowCache = mCachedWidthList.get(row);
        if (cellRowCache == null) {
            cellRowCache = new SparseIntArray();
        }

        cellRowCache.put(column, width);
        mCachedWidthList.put(row, cellRowCache);
    }

    /**
     * Allows to set cache width value for all cell items that is located on column position.
     */
    public void setCacheWidth(int column, int width) {
        for (int i = 0; i < mRowHeaderRecyclerView.getAdapter().getItemCount(); i++) {
            // set cache width for single cell item.
            setCacheWidth(i, column, width);
        }
    }

    public int getCacheWidth(int row, int column) {
        SparseIntArray cellRowCaches = mCachedWidthList.get(row);
        if (cellRowCaches != null) {
            return cellRowCaches.get(column, -1);
        }
        return -1;
    }

    /**
     * Clears the widths which have been calculated and reused.
     */
    public void clearCachedWidths() {
        mCachedWidthList.clear();
    }

    @NonNull
    public CellRecyclerView[] getVisibleCellRowRecyclerViews() {
        int length = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
        CellRecyclerView[] recyclerViews = new CellRecyclerView[length];

        int index = 0;
        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {
            recyclerViews[index] = (CellRecyclerView) findViewByPosition(i);
            index++;
        }

        return recyclerViews;
    }

    private int getCellRecyclerViewScrollState() {
        return mTableView.getCellRecyclerView().getScrollState();
    }
}
