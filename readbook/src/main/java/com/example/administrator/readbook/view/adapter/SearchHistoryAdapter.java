//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.example.administrator.readbook.R;
import com.example.administrator.readbook.bean.SearchHistoryBean;
import com.example.administrator.readbook.widget.flowlayout.FlowLayout;
import com.example.administrator.readbook.widget.flowlayout.TagAdapter;

import java.util.ArrayList;

public class SearchHistoryAdapter extends TagAdapter<SearchHistoryBean> {
    public SearchHistoryAdapter() {
        super(new ArrayList<SearchHistoryBean>());
    }

    public interface OnItemClickListener{
        void itemClick(SearchHistoryBean searchHistoryBean);
    }
    private SearchHistoryAdapter.OnItemClickListener onItemClickListener;

    public OnItemClickListener getListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public View getView(FlowLayout parent, int position, final SearchHistoryBean searchHistoryBean) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_searchhistory_item,
                parent, false);
        tv.setText(searchHistoryBean.getContent());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onItemClickListener){
                    onItemClickListener.itemClick(searchHistoryBean);
                }
            }
        });
        return tv;
    }

    public SearchHistoryBean getItemData(int position){
        return mTagDatas.get(position);
    }

    public int getDataSize(){
        return mTagDatas.size();
    }
}
