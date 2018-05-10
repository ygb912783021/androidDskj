//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.model;

import com.example.administrator.readbook.bean.BookContentBean;
import com.example.administrator.readbook.bean.BookShelfBean;
import com.example.administrator.readbook.bean.SearchBookBean;
import com.example.administrator.readbook.listener.OnGetChapterListListener;

import java.util.List;

import io.reactivex.Observable;

public interface IStationBookModel {

    /**
     * 搜索书籍
     */
    Observable<List<SearchBookBean>> searchBook(String content, int page);

    /**
     * 网络请求并解析书籍信息
     */
    Observable<BookShelfBean> getBookInfo(final BookShelfBean bookShelfBean);

    /**
     * 网络解析图书目录
     */
    void getChapterList(final BookShelfBean bookShelfBean, OnGetChapterListListener getChapterListListener);

    /**
     * 章节缓存
     */
    Observable<BookContentBean> getBookContent(final String durChapterUrl, final int durChapterIndex);
}
