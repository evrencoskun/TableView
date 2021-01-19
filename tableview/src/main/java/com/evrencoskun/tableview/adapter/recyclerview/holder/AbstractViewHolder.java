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

package com.evrencoskun.tableview.adapter.recyclerview.holder;

import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by evrencoskun on 23/10/2017.
 */

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
    public enum SelectionState {SELECTED, UNSELECTED, SHADOWED}

    // Default value
    @NonNull
    private SelectionState m_eState = SelectionState.UNSELECTED;

    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setSelected(@NonNull SelectionState selectionState) {
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
