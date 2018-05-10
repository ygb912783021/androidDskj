//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.presenter;

import com.example.administrator.readbook.base.IPresenter;
import com.example.administrator.readbook.bean.BookShelfBean;
import com.example.administrator.readbook.bean.SearchBookBean;

public interface IBookDetailPresenter extends IPresenter {

    int getOpenfrom();

    SearchBookBean getSearchBook();

    BookShelfBean getBookShelf();

    Boolean getInBookShelf();

    void getBookShelfInfo();

    void addToBookShelf();

    void removeFromBookShelf();
}
