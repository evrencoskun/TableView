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

package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public abstract class AbstractRecyclerViewAdapter<T> extends RecyclerView
        .Adapter<AbstractViewHolder> {

    protected List<T> mItemList;

    protected Context mContext;

    public AbstractRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public AbstractRecyclerViewAdapter(Context context, List<T> itemList) {
        mContext = context;

        if (itemList == null) {
            mItemList = new ArrayList<>();
        } else {
            setItems(itemList);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public List<T> getItems() {
        return mItemList;
    }

    public void setItems(List<T> itemList) {
        mItemList = new ArrayList<>(itemList);

        this.notifyDataSetChanged();
    }

    public void setItems(List<T> itemList, boolean notifyDataSet) {
        mItemList = new ArrayList<>(itemList);

        if (notifyDataSet) {
            this.notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
        if (mItemList == null || mItemList.isEmpty() || position < 0 || position >= mItemList
                .size()) {
            return null;
        }
        return mItemList.get(position);
    }

    public void deleteItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            mItemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void deleteItemRange(int positionStart, int itemCount) {
        for (int i = positionStart + itemCount - 1; i >= positionStart; i--) {
            if (i != RecyclerView.NO_POSITION) {
                mItemList.remove(i);
            }
        }

        notifyItemRangeRemoved(positionStart, itemCount);
    }

    public void addItem(int position, T item) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            mItemList.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void addItemRange(int positionStart, List<T> items) {
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (i != RecyclerView.NO_POSITION) {
                    mItemList.add((i + positionStart), items.get(i));
                }
            }

            notifyItemRangeInserted(positionStart, items.size());
        }
    }

    public void changeItem(int position, T item) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            mItemList.set(position, item);
            notifyItemChanged(position);
        }
    }

    public void changeItemRange(int positionStart, List<T> items) {
        if (mItemList.size() > positionStart + items.size() && items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (i != RecyclerView.NO_POSITION) {
                    mItemList.set(i + positionStart, items.get(i));
                }
            }
            notifyItemRangeChanged(positionStart, items.size());
        }
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
