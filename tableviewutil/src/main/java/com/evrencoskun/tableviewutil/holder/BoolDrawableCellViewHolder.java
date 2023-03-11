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

package com.evrencoskun.tableviewutil.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableviewutil.R;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class BoolDrawableCellViewHolder extends AbstractViewHolder {
    @NonNull
    private final ImageView cell_image;
    @DrawableRes private final int mIdDrawableTrue;
    @DrawableRes private final int mIdDrawableFalse;

    public BoolDrawableCellViewHolder(@NonNull View itemView, @DrawableRes int idDrawableTrue, @DrawableRes int idDrawableFalse) {
        super(itemView);
        cell_image = itemView.findViewById(R.id.cell_image);
        mIdDrawableTrue = idDrawableTrue;
        mIdDrawableFalse = idDrawableFalse;
    }

    public void setCell(@Nullable Object content, int columnPosition, int rowPosition, Object pojo) {
        super.setCell(content, columnPosition, rowPosition, pojo);
        setData(content == null ? null : (Boolean) content);
    }

    public void setData(Boolean data) {
        if (data != null) {
            int moodDrawable = data ? mIdDrawableTrue : mIdDrawableFalse;

            cell_image.setImageResource(moodDrawable);
        } else {
            cell_image.setImageBitmap(null);
        }
    }
}