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

package com.evrencoskun.tableviewsample.tableview.model;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class Cell implements ISortableModel, IFilterableModel {
    @NonNull
    private String mId;
    @Nullable
    private Object mData;
    @NonNull
    private String mFilterKeyword;

    public Cell(@NonNull String id, @Nullable Object data) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
    }

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @NonNull
    @Override
    public String getId() {
        return mId;
    }

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @Nullable
    @Override
    public Object getContent() {
        return mData;
    }

    @Nullable
    public Object getData() {
        return mData;
    }

    public void setData(@Nullable Object data) {
        mData = data;
    }

    @NonNull
    @Override
    public String getFilterableKeyword() {
        return mFilterKeyword;
    }
}
