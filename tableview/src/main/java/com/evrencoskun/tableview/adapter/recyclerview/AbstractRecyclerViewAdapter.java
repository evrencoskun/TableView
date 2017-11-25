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

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
