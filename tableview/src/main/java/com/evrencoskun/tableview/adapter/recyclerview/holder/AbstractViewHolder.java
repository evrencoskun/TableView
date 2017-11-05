package com.evrencoskun.tableview.adapter.recyclerview.holder;

import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by evrencoskun on 23/10/2017.
 */

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public void setSelected(boolean p_bIsSelected) {
        itemView.setSelected(p_bIsSelected);

    }

    public boolean isSelected() {
        return itemView.isSelected();
    }

    public void setBackgroundColor(@ColorInt int p_nColor) {
        itemView.setBackgroundColor(p_nColor);
    }

}
