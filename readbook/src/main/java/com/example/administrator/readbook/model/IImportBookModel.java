//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.model;

import com.example.administrator.readbook.bean.LocBookShelfBean;

import java.io.File;

import io.reactivex.Observable;

public interface IImportBookModel {

    Observable<LocBookShelfBean> importBook(File book);
}
