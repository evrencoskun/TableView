package com.evrencoskun.tableview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;

import java.util.HashMap;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public class ColumnLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = ColumnLayoutManager.class.getSimpleName();

    private HashMap<Integer, Integer> m_aWidthList;

    private ITableView m_iTableView;
    private CellRecyclerView m_iCellRowRecyclerView;
    private CellRecyclerView m_iColumnHeaderRecyclerView;
    private ColumnHeaderLayoutManager m_jColumnHeaderLayoutManager;
    private CellLayoutManager m_iCellLayoutManager;
    private int m_iLastItem = -1;
    private int m_iFirstItem = -1;
    private int m_nLastDx = 0;

    public ColumnLayoutManager(Context context, ITableView p_iTableView, CellRecyclerView
            m_iCellRowRecyclerView) {
        super(context);
        this.m_iTableView = p_iTableView;
        this.m_iColumnHeaderRecyclerView = m_iTableView.getColumnHeaderRecyclerView();
        this.m_jColumnHeaderLayoutManager = (ColumnHeaderLayoutManager)
                m_iColumnHeaderRecyclerView.getLayoutManager();
        this.m_iCellRowRecyclerView = m_iCellRowRecyclerView;
        this.m_iCellLayoutManager = (CellLayoutManager) m_iTableView.getCellRecyclerView()
                .getLayoutManager();

        m_aWidthList = new HashMap<>();
        // Set default orientation
        this.setOrientation(ColumnLayoutManager.HORIZONTAL);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        Log.e(LOG_TAG, "onLayoutChildren");
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        Log.e(LOG_TAG, "onLayoutCompleted");
    }

    private void fitColumnWidth() {
        if (m_jColumnHeaderLayoutManager == null) {
            return;
        }


        for (int i = findFirstVisibleItemPosition(); i < findLastVisibleItemPosition() + 1; i++) {
            View jCellView = findViewByPosition(i);
            View jColumnView = m_jColumnHeaderLayoutManager.findViewByPosition(i);

            if (jColumnView != null && jCellView != null) {
                // Get width size both of them
                int nColumnHeaderWidth = jColumnView.getWidth();
                int nCellWidth = jCellView.getWidth();

                // Control Which one has the broadest width ?
                if (nColumnHeaderWidth > nCellWidth) {
                    changeColumnWidth(jCellView, nColumnHeaderWidth);
                    Log.e(LOG_TAG, i + " cellView width changing with " + nColumnHeaderWidth);
                } else if (nCellWidth > nColumnHeaderWidth) {
                    changeColumnWidth(jColumnView, nCellWidth);
                    Log.e(LOG_TAG, i + " column width changing with " + nCellWidth);
                }
            } else {
                Log.e(LOG_TAG, i + " column is null  first : " + m_jColumnHeaderLayoutManager
                        .findFirstVisibleItemPosition() + "  last : " +
                        m_jColumnHeaderLayoutManager.findLastVisibleItemPosition());
            }
        }

    }


    private void fitColumnWidth(int p_nDx) {

        int nPosition;
        if (p_nDx > 0) {
            nPosition = findLastCompletelyVisibleItemPosition();
            if (nPosition != m_jColumnHeaderLayoutManager.findLastCompletelyVisibleItemPosition()) {
                nPosition = Math.min(m_jColumnHeaderLayoutManager
                        .findLastCompletelyVisibleItemPosition(), nPosition);
            }
        } else {
            nPosition = findFirstCompletelyVisibleItemPosition();
            if (nPosition != m_jColumnHeaderLayoutManager.findFirstCompletelyVisibleItemPosition
                    ()) {
                nPosition = Math.max(m_jColumnHeaderLayoutManager
                        .findFirstCompletelyVisibleItemPosition(), nPosition);
            }
        }

        int i = nPosition;
        View jCellView = findViewByPosition(i);
        View jColumnView = m_jColumnHeaderLayoutManager.findViewByPosition(i);

        if (jColumnView != null && jCellView != null) {
            // Get width size both of them
            int nColumnHeaderWidth = jColumnView.getWidth();
            int nCellWidth = jCellView.getWidth();
            //jColumnView.setBackgroundColor(jColumnView.getContext().getResources().getColor(R
            //        .color.header_line_color));

            // Control Which one has the broadest width ?
            if (nColumnHeaderWidth > nCellWidth) {
                Log.e(LOG_TAG, i + " cellView width changing " + jCellView.getWidth() + " " +
                        jCellView.getLayoutParams().width);
                //changeColumnWidth(jCellView, nColumnHeaderWidth);
                Log.e(LOG_TAG, i + " cellView width changed with " + nColumnHeaderWidth);
            } else if (nCellWidth > nColumnHeaderWidth) {
                Log.e(LOG_TAG, i + " column width changing " + jColumnView.getWidth() + " " +
                        jColumnView.getLayoutParams().width);
                changeColumnWidth(jColumnView, nCellWidth);
                Log.e(LOG_TAG, i + " column width changed with " + nCellWidth);
            } else {
                Log.e(LOG_TAG, i + " column width and cell width is same : " + nCellWidth);
            }
        } else {
            Log.e(LOG_TAG, i + " column is null  first : " + m_jColumnHeaderLayoutManager
                    .findFirstVisibleItemPosition() + "  last : " + m_jColumnHeaderLayoutManager
                    .findLastVisibleItemPosition());
        }
    }


    private void changeColumnWidth(View p_jViewHolder, int p_nNewWidth) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) p_jViewHolder
                .getLayoutParams();
        params.width = p_nNewWidth;
        p_jViewHolder.requestLayout();
    }


    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        super.measureChildWithMargins(child, widthUsed, heightUsed);

        measureChild(child, widthUsed, heightUsed);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
        int nPosition = getPosition(child);

        /*View column = m_jColumnHeaderLayoutManager.findViewByPosition(nPosition);
        if (column != null) {
            int nColumnWidth = column.getWidth();
            setWidth(child, nColumnWidth);
        }*/

        int nCacheWidth = getCacheWidth(nPosition);
        int nColumnCacheWidth = m_jColumnHeaderLayoutManager.getCacheWidth(nPosition);

        Log.e(LOG_TAG, m_iCellRowRecyclerView.getId() + " " + nPosition + " id: " + child.getId()
                + " nCacheWidth: " + nCacheWidth + " " + nColumnCacheWidth);
        if (nCacheWidth != -1 && nCacheWidth == nColumnCacheWidth) {
            setWidth(child, nCacheWidth);
        } else {
            fitWidthSize(child);
        }


        //Log.e(LOG_TAG, nPosition + " + width : " + child.getWidth() + " m w: " + child
        //        .getMeasuredWidth() + " id: " + child.getId() + " " + getWidth(child));


        //Log.e(LOG_TAG, nPosition + " - width : " + child.getWidth() + " m w: " + child
        //        .getMeasuredWidth() + " id: " + child.getId());

        if (shouldFitColumns(nPosition)) {
            Log.e(LOG_TAG, nPosition + " " + m_iCellLayoutManager.getPosition
                    (m_iCellRowRecyclerView) + " here i am");
            m_iCellLayoutManager.fitWidthSize(nPosition);
        }
    }

    @Override
    public void layoutDecoratedWithMargins(View child, int left, int top, int right, int bottom) {
        int nPosition = getPosition(child);
        int nWid = right - left - 1;
        View column = m_jColumnHeaderLayoutManager.findViewByPosition(nPosition);
        if (column != null) {

            int nColumnWidth = column.getWidth();
            if (nColumnWidth > nWid) {
                if (m_nLastDx > 0) {
                    //right = left + nColumnWidth + 1;
                } else if (m_nLastDx < 0) {
                    //left = right - nColumnWidth + 1;
                }


                //child.getLayoutParams().width = nColumnWidth;


                // Log.e(LOG_TAG, " " + right + " column right " + column.getRight() + " " +
                //         m_jColumnHeaderLayoutManager.getDecoratedRight(column));
                // Bu haliyle cell width > column dan doğru çalışıyor
                // ancak yeni eklenen viewholderlar icin column > cell calısmıyor
                // cunku superden sonra boyutunu set ediyoruz cell in haliyle
                // ancak superden once calıstırırsak fitWidthSize metodunu
                // o durumda da ilk acılısta calısan column > cell calısmıyor
            }
        }

        super.layoutDecoratedWithMargins(child, left, top, right, bottom);

        //fitWidthSize(child);
        /*Log.e(LOG_TAG, m_iCellRowRecyclerView.getId() + " " + nPosition + " id: " + child.getId()
                + " + width : " + child.getWidth() + "" + " m w: " + child.getMeasuredWidth() + "" +
                " " + getWidth(child) + " wid : " + nWid + " params : " + child.getLayoutParams()
                .width);*/
    }

    private boolean shouldFitColumns(int p_nPosition) {
        int nYPosition = m_iCellLayoutManager.getPosition(m_iCellRowRecyclerView);
        if (m_iCellLayoutManager.shouldFitColumns(nYPosition)) {
            if (m_nLastDx > 0) {
                if (p_nPosition == findLastVisibleItemPosition()) {
                    return true;
                }
            } else if (m_nLastDx < 0) {
                if (p_nPosition == findFirstVisibleItemPosition()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State
            state) {
        if (m_iColumnHeaderRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                m_iCellRowRecyclerView.isScrollOthers()) {
            // Every CellRowRecyclerViews should be scrolled after the ColumnHeaderRecyclerView.
            // Because it is the main compared one to make each columns fit.
            m_iColumnHeaderRecyclerView.scrollBy(dx, 0);
        }
        int nScroll = super.scrollHorizontallyBy(dx, recycler, state);
        if (dx != 0) {
            m_nLastDx = dx;
        }


        /*if (dx > 0) {
            if (m_iLastItem != findLastVisibleItemPosition()) {
                m_iFirstItem = findFirstVisibleItemPosition();
                m_iLastItem = findLastVisibleItemPosition();
                m_iCellLayoutManager.fitWidthSize();
            }
        } else if (dx < 0) {
            if (m_iFirstItem != findFirstVisibleItemPosition()) {
                m_iFirstItem = findFirstVisibleItemPosition();
                m_iLastItem = findLastVisibleItemPosition();
                m_iCellLayoutManager.fitWidthSize();
            }
        }*/


        return nScroll;
    }

    private void setWidth(View p_jView, int p_nWidth) {
        // Change width value from params
        //RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) p_jView.getLayoutParams();
        //params.width = p_nWidth;

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) p_jView.getLayoutParams();
        params.width = p_nWidth;
        p_jView.setLayoutParams(params);
        //p_jView.setLayoutParams(params);

        //p_jView.setMinimumWidth(p_nWidth);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_nWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_jView.getMeasuredHeight(),
                View.MeasureSpec.EXACTLY);
        p_jView.measure(widthMeasureSpec, heightMeasureSpec);

        p_jView.invalidate();
        p_jView.requestLayout();
        p_jView.forceLayout();

    }

    private int getWidth(View p_jView) {
        p_jView.measure(LinearLayout.LayoutParams.WRAP_CONTENT, View.MeasureSpec.makeMeasureSpec
                (p_jView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        return p_jView.getMeasuredWidth();
    }

    private void fitWidthSize(View child) {
        int nPosition = getPosition(child);
        int nCellWidth = getCacheWidth(nPosition);
        if (nCellWidth == -1) {
            nCellWidth = getWidth(child);//child.getMeasuredWidth(); //
        }

        if (nPosition > -1) {
            View columnHeaderChild = m_jColumnHeaderLayoutManager.findViewByPosition(nPosition);
            if (columnHeaderChild != null) {

                int nColumnHeaderWidth = m_jColumnHeaderLayoutManager.getCacheWidth(nPosition);
                if (nColumnHeaderWidth == -1) {
                    nColumnHeaderWidth = getWidth(columnHeaderChild);
                }

                Log.e(LOG_TAG, m_iCellRowRecyclerView.getId() + " " + nPosition + " id: " + child
                        .getId() + " + nCellWidth: " + nCellWidth + " nColumnHeaderWidth: " +
                        nColumnHeaderWidth + " cell width : " + getWidth(child) + " column width " +
                        "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + ": " +
                        getWidth(columnHeaderChild) + "" + " *");

                if (nCellWidth != 0) {

                    if (nColumnHeaderWidth > nCellWidth) {
                        nCellWidth = nColumnHeaderWidth;

                    } else if (nCellWidth > nColumnHeaderWidth) {
                        nColumnHeaderWidth = nCellWidth;
                    }

                    // Column header
                    if (nColumnHeaderWidth != columnHeaderChild.getWidth()) {
                        setWidth(columnHeaderChild, nColumnHeaderWidth);
                    }

                    m_jColumnHeaderLayoutManager.setCacheWidth(nPosition, nColumnHeaderWidth);

                } else {
                    Log.e(LOG_TAG, nPosition + " cell width is 0");
                }

                Log.e(LOG_TAG, m_iCellRowRecyclerView.getId() + " " + nPosition + " id: " + child
                        .getId() + " - nCellWidth: " + nCellWidth + " nColumnHeaderWidth: " +
                        nColumnHeaderWidth + " cell width : " + getWidth(child) + " column width " +
                        "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + ": " +
                        getWidth(columnHeaderChild) + "" + " *");
            } else {
                Log.e(LOG_TAG, "column header is null for " + nPosition + " last visible : " +
                        m_jColumnHeaderLayoutManager.findLastVisibleItemPosition());
            }
        } else {
            Log.e(LOG_TAG, "position is " + nPosition);
        }
        setWidth(child, nCellWidth);
        setCacheWidth(nPosition, nCellWidth);
    }


    public void setCacheWidth(int p_nPosition, int p_nWidth) {
        /*if (getCacheWidth(p_nPosition) == -1) {
            m_aWidthList.add(p_nPosition, p_nWidth);
        } else {
            m_aWidthList.set(p_nPosition, p_nWidth);
        }*/
        m_aWidthList.put(p_nPosition, p_nWidth);
    }

    public int getCacheWidth(int p_nPosition) {
        Integer nCachedWidth = m_aWidthList.get(p_nPosition);
        if (nCachedWidth != null) {
            return m_aWidthList.get(p_nPosition);
        }
        //if (m_aWidthList.size() > p_nPosition && m_aWidthList.get(p_nPosition) != null) {
        //    return m_aWidthList.get(p_nPosition);
        //}
        return -1;
    }

}
