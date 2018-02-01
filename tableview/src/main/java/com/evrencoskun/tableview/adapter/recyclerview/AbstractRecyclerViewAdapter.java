package com.evrencoskun.tableview.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 10/06/2017.
 */

public abstract class AbstractRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView
        .ViewHolder> {

    protected List<T> mItemList;

    protected Context mContext;

    public AbstractRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public AbstractRecyclerViewAdapter(Context context, List<T> itemList) {
        mContext = context;

        if (itemList != null) {
            mItemList = new ArrayList<>(itemList);
            this.notifyDataSetChanged();
        } else {
            mItemList = new ArrayList<>();
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
        for (int i = positionStart; i < positionStart + itemCount + 1; i++) {
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
