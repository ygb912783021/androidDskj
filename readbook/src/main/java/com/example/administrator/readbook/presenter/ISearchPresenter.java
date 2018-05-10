//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.presenter;


import com.example.administrator.readbook.base.IPresenter;
import com.example.administrator.readbook.bean.SearchBookBean;

//ISearchPresenter 我的搜索引擎
public interface ISearchPresenter extends IPresenter {

    Boolean getHasSearch();

    void setHasSearch(Boolean hasSearch);

    void insertSearchHistory();

    void querySearchHistory();

    void cleanSearchHistory();

    int getPage();

    void initPage();

    void toSearchBooks(String key, Boolean fromError);

    void addBookToShelf(final SearchBookBean searchBookBean);

    Boolean getInput();

    void setInput(Boolean input);
}
