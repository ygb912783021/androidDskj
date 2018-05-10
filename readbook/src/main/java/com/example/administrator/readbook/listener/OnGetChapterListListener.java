//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.listener;


import com.example.administrator.readbook.bean.BookShelfBean;

public interface OnGetChapterListListener {
    public void success(BookShelfBean bookShelfBean);
    public void error();
}
