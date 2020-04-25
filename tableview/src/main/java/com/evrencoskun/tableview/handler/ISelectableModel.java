package com.evrencoskun.tableview.handler;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

/**
 * Created by cedricferry on 8/2/18.
 */

public interface ISelectableModel {
    AbstractViewHolder.SelectionState getSelectionState();
    void setSelectionState(AbstractViewHolder.SelectionState selectionState);
}
