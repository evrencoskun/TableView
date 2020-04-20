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

package com.evrencoskun.tableviewsample.tableview.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableviewsample.R;
import com.evrencoskun.tableviewsample.tableview.TableViewModel;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class MoodCellViewHolder extends AbstractViewHolder {
    @NonNull
    public final ImageView cell_image;

    public MoodCellViewHolder(@NonNull View itemView) {
        super(itemView);
        cell_image = itemView.findViewById(R.id.cell_image);
    }

    public void setData(Object data) {
        int mood = (int) data;
        int moodDrawable = mood == TableViewModel.HAPPY ? R.drawable.ic_happy : R.drawable.ic_sad;

        cell_image.setImageResource(moodDrawable);
    }
}