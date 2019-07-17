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

package com.evrencoskun.tableview.sort;

import androidx.core.util.ObjectsCompat;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * Created by evrencoskun on 23.11.2017.
 */

public class ColumnSortCallback extends DiffUtil.Callback {

    private static final String LOG_TAG = ColumnSortCallback.class.getSimpleName();

    private List<List<ISortableModel>> mOldCellItems;
    private List<List<ISortableModel>> mNewCellItems;
    private int mColumnPosition;

    public ColumnSortCallback(List<List<ISortableModel>> oldCellItems, List<List<ISortableModel>>
            newCellItems, int column) {
        this.mOldCellItems = oldCellItems;
        this.mNewCellItems = newCellItems;
        this.mColumnPosition = column;
    }

    @Override
    public int getOldListSize() {
        return mOldCellItems.size();
    }

    @Override
    public int getNewListSize() {
        return mNewCellItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Control for precaution from IndexOutOfBoundsException
        if (mOldCellItems.size() > oldItemPosition && mNewCellItems.size() > newItemPosition) {
            if (mOldCellItems.get(oldItemPosition).size() > mColumnPosition && mNewCellItems.get
                    (newItemPosition).size() > mColumnPosition) {
                // Compare ids
                String oldId = mOldCellItems.get(oldItemPosition).get(mColumnPosition).getId();
                String newId = mNewCellItems.get(newItemPosition).get(mColumnPosition).getId();
                return oldId.equals(newId);
            }
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Control for precaution from IndexOutOfBoundsException
        if (mOldCellItems.size() > oldItemPosition && mNewCellItems.size() > newItemPosition) {
            if (mOldCellItems.get(oldItemPosition).size() > mColumnPosition && mNewCellItems.get
                    (newItemPosition).size() > mColumnPosition) {
                // Compare contents
                Object oldContent = mOldCellItems.get(oldItemPosition).get(mColumnPosition)
                        .getContent();
                Object newContent = mNewCellItems.get(newItemPosition).get(mColumnPosition)
                        .getContent();
                return ObjectsCompat.equals(oldContent, newContent);
            }
        }
        return false;
    }
}
