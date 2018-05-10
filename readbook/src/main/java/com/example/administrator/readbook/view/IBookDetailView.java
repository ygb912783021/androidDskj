//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.view;


import com.example.administrator.readbook.base.IView;

public interface IBookDetailView extends IView {
    /**
     * 更新书籍详情UI
     */
    void updateView();

    /**
     * 数据获取失败
     */
    void getBookShelfError();
}
