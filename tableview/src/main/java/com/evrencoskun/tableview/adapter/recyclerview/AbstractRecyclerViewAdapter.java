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

    protected List<T> m_jItemList;

    protected Context m_jContext;

    public AbstractRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public AbstractRecyclerViewAdapter(Context context, List<T> p_jItemList) {
        m_jContext = context;

        if (p_jItemList != null) {
            m_jItemList = new ArrayList<>(p_jItemList);
            this.notifyDataSetChanged();
        } else {
            m_jItemList = new ArrayList<>();
        }
    }

    @Override
    public int getItemCount() {
        return m_jItemList.size();
    }

    public List<T> getItems() {
        return m_jItemList;
    }

    public void setItems(List<T> p_jItemList) {
        m_jItemList = new ArrayList<>(p_jItemList);

        this.notifyDataSetChanged();
    }

    public void setItems(List<T> p_jItemList, boolean p_bNotifyDataSet) {
        m_jItemList = new ArrayList<>(p_jItemList);

        if (p_bNotifyDataSet) {
            this.notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
        if (m_jItemList == null || m_jItemList.isEmpty() || position < 0 || position >=
                m_jItemList.size()) {
            return null;
        }
        return m_jItemList.get(position);
    }

    public void deleteItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            m_jItemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void deleteItemRange(int positionStart, int itemCount) {
        if (m_jItemList.size() > positionStart + itemCount) {
            for (int i = positionStart; i < positionStart + itemCount + 1; i++) {
                if (i != RecyclerView.NO_POSITION) {
                    m_jItemList.remove(i);
                }
            }
            notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    public void addItem(int position, T item) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            m_jItemList.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void addItemRange(int positionStart, int itemCount, List<T> items) {
        if (m_jItemList.size() > positionStart + itemCount && items != null) {
            for (int i = positionStart; i < positionStart + itemCount + 1; i++) {
                if (i != RecyclerView.NO_POSITION) {
                    m_jItemList.add(i, items.get(i));
                }
            }
            notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    public void changeItem(int position, T item) {
        if (position != RecyclerView.NO_POSITION && item != null) {
            m_jItemList.set(position, item);
            notifyItemChanged(position);
        }
    }

    public void changeItemRange(int positionStart, int itemCount, List<T> items) {
        if (m_jItemList.size() > positionStart + itemCount && items != null) {
            for (int i = positionStart; i < positionStart + itemCount + 1; i++) {
                if (i != RecyclerView.NO_POSITION) {
                    m_jItemList.set(i, items.get(i));
                }
            }
            notifyItemRangeChanged(positionStart, itemCount);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
