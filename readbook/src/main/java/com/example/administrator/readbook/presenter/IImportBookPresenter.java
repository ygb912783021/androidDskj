//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.presenter;

import com.example.administrator.readbook.base.IPresenter;

import java.io.File;
import java.util.List;

public interface IImportBookPresenter extends IPresenter {
    void searchLocationBook();

    void importBooks(List<File> books);
}
