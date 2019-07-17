/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableview.adapter.recyclerview.holder;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by evrencoskun on 23/10/2017.
 */

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
    public enum SelectionState {SELECTED, UNSELECTED, SHADOWED}

    // Default value
    private SelectionState m_eState = SelectionState.UNSELECTED;

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public void setSelected(SelectionState selectionState) {
        m_eState = selectionState;

        if (selectionState == SelectionState.SELECTED) {
            itemView.setSelected(true);
        } else if (selectionState == SelectionState.UNSELECTED) {
            itemView.setSelected(false);
        }
    }

    public boolean isSelected() {
        return m_eState == SelectionState.SELECTED;
    }

    public boolean isShadowed() {
        return m_eState == SelectionState.SHADOWED;
    }

    public void setBackgroundColor(@ColorInt int p_nColor) {
        itemView.setBackgroundColor(p_nColor);
    }

    public void onViewRecycled() {
    }

    public boolean onFailedToRecycleView() {
        return false;
    }

}
