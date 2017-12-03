package com.evrencoskun.tableview.handler;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState;

/**
 * Created by evrencoskun on 24/10/2017.
 */

public class SelectionHandler {

    public static final int UNSELECTED_POSITION = -1;
    private int m_nSelectedRowPosition = UNSELECTED_POSITION;
    private int m_nSelectedColumnPosition = UNSELECTED_POSITION;


    private ITableView m_iTableView;
    private AbstractViewHolder m_jPreviousSelectedViewHolder;
    private CellRecyclerView m_iColumnHeaderRecyclerView;
    private CellRecyclerView m_iRowHeaderRecyclerView;

    public SelectionHandler(ITableView p_iTableView) {
        this.m_iTableView = p_iTableView;
        this.m_iColumnHeaderRecyclerView = m_iTableView.getColumnHeaderRecyclerView();
        this.m_iRowHeaderRecyclerView = m_iTableView.getRowHeaderRecyclerView();
    }

    public void setSelectedCellPositions(AbstractViewHolder p_jSelectedViewHolder, int
            p_nXPosition, int p_nYPosition) {
        this.setPreviousSelectedView(p_jSelectedViewHolder);

        this.m_nSelectedColumnPosition = p_nXPosition;
        this.m_nSelectedRowPosition = p_nYPosition;

        selectedCellView();
    }


    public void setSelectedColumnPosition(AbstractViewHolder p_jSelectedViewHolder, int
            p_nXPosition) {
        this.setPreviousSelectedView(p_jSelectedViewHolder);

        this.m_nSelectedColumnPosition = p_nXPosition;

        selectedColumnHeader();

        // Set unselected others
        m_nSelectedRowPosition = UNSELECTED_POSITION;
    }

    public int getSelectedColumnPosition() {
        return m_nSelectedColumnPosition;
    }

    public void setSelectedRowPosition(AbstractViewHolder p_jSelectedViewHolder, int p_nYPosition) {
        this.setPreviousSelectedView(p_jSelectedViewHolder);

        this.m_nSelectedRowPosition = p_nYPosition;

        selectedRowHeader();

        // Set unselected others
        m_nSelectedColumnPosition = UNSELECTED_POSITION;
    }

    public int getSelectedRowPosition() {
        return m_nSelectedRowPosition;
    }


    public void setPreviousSelectedView(AbstractViewHolder p_jViewHolder) {
        restorePreviousSelectedView();

        if (m_jPreviousSelectedViewHolder != null) {
            // Change color
            m_jPreviousSelectedViewHolder.setBackgroundColor(m_iTableView.getUnSelectedColor());
            // Change state
            m_jPreviousSelectedViewHolder.setSelected(SelectionState.UNSELECTED);
        }

        AbstractViewHolder oldViewHolder = m_iTableView.getCellLayoutManager().getCellViewHolder
                (getSelectedColumnPosition(), getSelectedRowPosition());

        if (oldViewHolder != null) {
            // Change color
            oldViewHolder.setBackgroundColor(m_iTableView.getUnSelectedColor());
            // Change state
            oldViewHolder.setSelected(SelectionState.UNSELECTED);
        }

        this.m_jPreviousSelectedViewHolder = p_jViewHolder;

        // Change color
        m_jPreviousSelectedViewHolder.setBackgroundColor(m_iTableView.getSelectedColor());
        // Change state
        m_jPreviousSelectedViewHolder.setSelected(SelectionState.SELECTED);
    }


    private void restorePreviousSelectedView() {
        if (m_nSelectedColumnPosition != UNSELECTED_POSITION && m_nSelectedRowPosition !=
                UNSELECTED_POSITION) {
            unselectedCellView();
        } else if (m_nSelectedColumnPosition != UNSELECTED_POSITION) {
            unselectedColumnHeader();
        } else if (m_nSelectedRowPosition != UNSELECTED_POSITION) {
            unselectedRowHeader();
        }
    }

    private void selectedRowHeader() {
        // Change background color of the selected cell views
        changeVisibleCellViewsBackgroundForRow(m_nSelectedRowPosition, true);

        // Change background color of the column headers to be shown as a shadow.
        m_iTableView.getColumnHeaderRecyclerView().setSelected(SelectionState.SHADOWED,
                m_iTableView.getShadowColor(), false);
    }

    private void unselectedRowHeader() {
        changeVisibleCellViewsBackgroundForRow(m_nSelectedRowPosition, false);

        // Change background color of the column headers to be shown as a normal.
        m_iTableView.getColumnHeaderRecyclerView().setSelected(SelectionState.UNSELECTED,
                m_iTableView.getUnSelectedColor(), false);
    }

    private void selectedCellView() {
        int nShadowColor = m_iTableView.getShadowColor();


        // Change background color of the row header which is located on Y Position of the cell
        // view.
        AbstractViewHolder rowHeader = (AbstractViewHolder) m_iRowHeaderRecyclerView
                .findViewHolderForAdapterPosition(m_nSelectedRowPosition);

        // If view is null, that means the row view holder was already recycled.
        if (rowHeader != null) {
            // Change color
            rowHeader.setBackgroundColor(nShadowColor);
            // Change state
            rowHeader.setSelected(SelectionState.SHADOWED);
        }

        // Change background color of the column header which is located on X Position of the cell
        // view.
        AbstractViewHolder columnHeader = (AbstractViewHolder) m_iColumnHeaderRecyclerView
                .findViewHolderForAdapterPosition(m_nSelectedColumnPosition);

        if (columnHeader != null) {
            // Change color
            columnHeader.setBackgroundColor(nShadowColor);
            // Change state
            columnHeader.setSelected(SelectionState.SHADOWED);
        }

    }

    private void unselectedCellView() {
        int nUnSelectedColor = m_iTableView.getUnSelectedColor();

        // Change background color of the row header which is located on Y Position of the cell
        // view.
        AbstractViewHolder rowHeader = (AbstractViewHolder) m_iRowHeaderRecyclerView
                .findViewHolderForAdapterPosition(m_nSelectedRowPosition);

        // If view is null, that means the row view holder was already recycled.
        if (rowHeader != null) {
            // Change color
            rowHeader.setBackgroundColor(nUnSelectedColor);
            // Change state
            rowHeader.setSelected(SelectionState.UNSELECTED);
        }

        // Change background color of the column header which is located on X Position of the cell
        // view.
        AbstractViewHolder columnHeader = (AbstractViewHolder) m_iColumnHeaderRecyclerView
                .findViewHolderForAdapterPosition(m_nSelectedColumnPosition);

        if (columnHeader != null) {
            // Change color
            columnHeader.setBackgroundColor(nUnSelectedColor);
            // Change state
            columnHeader.setSelected(SelectionState.UNSELECTED);
        }
    }

    private void selectedColumnHeader() {
        changeVisibleCellViewsBackgroundForColumn(m_nSelectedColumnPosition, true);

        m_iTableView.getRowHeaderRecyclerView().setSelected(SelectionState.SHADOWED, m_iTableView
                .getShadowColor(), false);

    }

    private void unselectedColumnHeader() {
        changeVisibleCellViewsBackgroundForColumn(m_nSelectedColumnPosition, false);

        m_iTableView.getRowHeaderRecyclerView().setSelected(SelectionState.UNSELECTED,
                m_iTableView.getUnSelectedColor(), false);
    }

    public boolean isCellSelected(int p_nXPosition, int p_nYPosition) {
        return (getSelectedColumnPosition() == p_nXPosition && getSelectedRowPosition() ==
                p_nYPosition) || isColumnSelected(p_nXPosition) || isRowSelected(p_nYPosition);
    }

    public SelectionState getCellSelectionState(int p_nXPosition, int p_nYPosition) {
        if (isCellSelected(p_nXPosition, p_nYPosition)) {
            return SelectionState.SELECTED;
        }
        return SelectionState.UNSELECTED;
    }

    public boolean isColumnSelected(int p_nXPosition) {
        return (getSelectedColumnPosition() == p_nXPosition && getSelectedRowPosition() ==
                UNSELECTED_POSITION);
    }

    public boolean isColumnShadowed(int p_nXPosition) {
        return (getSelectedColumnPosition() == p_nXPosition && getSelectedRowPosition() !=
                UNSELECTED_POSITION) || (getSelectedColumnPosition() == UNSELECTED_POSITION &&
                getSelectedRowPosition() != UNSELECTED_POSITION);
    }

    public boolean isAnyColumnSelected() {
        return (getSelectedColumnPosition() != SelectionHandler.UNSELECTED_POSITION &&
                getSelectedRowPosition() == SelectionHandler.UNSELECTED_POSITION);
    }

    public SelectionState getColumnSelectionState(int p_nXPosition) {

        if (isColumnShadowed(p_nXPosition)) {
            return SelectionState.SHADOWED;

        } else if (isColumnSelected(p_nXPosition)) {
            return SelectionState.SELECTED;

        } else {
            return SelectionState.UNSELECTED;
        }
    }

    public boolean isRowSelected(int p_nYPosition) {
        return (getSelectedRowPosition() == p_nYPosition && getSelectedColumnPosition() ==
                UNSELECTED_POSITION);
    }

    public boolean isRowShadowed(int p_nYPosition) {
        return (getSelectedRowPosition() == p_nYPosition && getSelectedColumnPosition() !=
                UNSELECTED_POSITION) || (getSelectedRowPosition() == UNSELECTED_POSITION &&
                getSelectedColumnPosition() != UNSELECTED_POSITION);
    }

    public SelectionState getRowSelectionState(int p_nYPosition) {

        if (isRowShadowed(p_nYPosition)) {
            return SelectionState.SHADOWED;

        } else if (isRowSelected(p_nYPosition)) {
            return SelectionState.SELECTED;

        } else {
            return SelectionState.UNSELECTED;
        }
    }

    private void changeVisibleCellViewsBackgroundForRow(int p_nYPosition, boolean p_bIsSelected) {
        int nSelectedColor = m_iTableView.getSelectedColor();
        int nUnSelectedColor = m_iTableView.getUnSelectedColor();

        CellRecyclerView recyclerView = (CellRecyclerView) m_iTableView.getCellLayoutManager()
                .findViewByPosition(p_nYPosition);

        if (recyclerView == null) {
            return;
        }

        recyclerView.setSelected(p_bIsSelected ? SelectionState.SELECTED : SelectionState
                .UNSELECTED, p_bIsSelected ? nSelectedColor : nUnSelectedColor, false);
    }

    private void changeVisibleCellViewsBackgroundForColumn(int p_nXPosition, boolean
            p_bIsSelected) {
        int nSelectedColor = m_iTableView.getSelectedColor();
        int nUnSelectedColor = m_iTableView.getUnSelectedColor();

        AbstractViewHolder[] visibleCellViews = m_iTableView.getCellLayoutManager()
                .getVisibleCellViewsByColumnPosition(p_nXPosition);

        if (visibleCellViews != null) {
            for (AbstractViewHolder viewHolder : visibleCellViews) {
                if (viewHolder != null) {
                    // Get each view container of the cell view and set unselected color.
                    viewHolder.setBackgroundColor(p_bIsSelected ? nSelectedColor :
                            nUnSelectedColor);

                    // Change selection status of the view holder
                    viewHolder.setSelected(p_bIsSelected ? SelectionState.SELECTED :
                            SelectionState.UNSELECTED);
                }
            }
        }
    }

    public void changeRowBackgroundColorBySelectionStatus(AbstractViewHolder p_iViewHolder,
                                                          SelectionState p_iSelectionState) {
        if (p_iSelectionState == SelectionState.SHADOWED) {
            p_iViewHolder.setBackgroundColor(m_iTableView.getShadowColor());

        } else if (p_iSelectionState == SelectionState.SELECTED) {
            p_iViewHolder.setBackgroundColor(m_iTableView.getSelectedColor());

        } else {
            p_iViewHolder.setBackgroundColor(m_iTableView.getUnSelectedColor());
        }
    }

    public void changeColumnBackgroundColorBySelectionStatus(AbstractViewHolder p_iViewHolder,
                                                             SelectionState p_jSelectionState) {
        if (p_jSelectionState == SelectionState.SHADOWED) {
            p_iViewHolder.setBackgroundColor(m_iTableView.getShadowColor());

        } else if (p_jSelectionState == SelectionState.SELECTED) {
            p_iViewHolder.setBackgroundColor(m_iTableView.getSelectedColor());

        } else {
            p_iViewHolder.setBackgroundColor(m_iTableView.getUnSelectedColor());
        }
    }

}
