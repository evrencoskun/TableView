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

package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public abstract class AbstractRecyclerViewAdapter<T> extends RecyclerView
        .Adapter<AbstractViewHolder> {

    @NonNull
    protected List<T> mItemList;

    @NonNull
    protected Context mContext;

    public AbstractRecyclerViewAdapter(@NonNull Context context) {
        this(context, null);
    }

    public AbstractRecyclerViewAdapter(@NonNull Context context, @Nullable List<T> itemList) {
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

    @NonNull
    public List<T> getItems() {
        return mItemList;
    }

    public void setItems(@NonNull List<T> itemList) {
        mItemList = new ArrayList<>(itemList);

        this.notifyDataSetChanged();
    }

    public void setItems(@NonNull List<T> itemList, boolean notifyDataSet) {
        mItemList = new ArrayList<>(itemList);

        if (notifyDataSet) {
            this.notifyDataSetChanged();
        }
    }

    @Nullable
    public T getItem(int position) {
        if (mItemList.isEmpty() || position < 0 || position >= mItemList.size()) {
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

    public void addItem(int position, @Nullable T item) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            mItemList.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void addItemRange(int positionStart, @Nullable List<T> items) {
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                mItemList.add((i + positionStart), items.get(i));
            }

            notifyItemRangeInserted(positionStart, items.size());
        }
    }

    public void changeItem(int position, @Nullable T item) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            mItemList.set(position, item);
            notifyItemChanged(position);
        }
    }

    public void changeItemRange(int positionStart, @Nullable List<T> items) {
        if (items != null && mItemList.size() > positionStart + items.size()) {
            for (int i = 0; i < items.size(); i++) {
                mItemList.set(i + positionStart, items.get(i));
            }
            notifyItemRangeChanged(positionStart, items.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
