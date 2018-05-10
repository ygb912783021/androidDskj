//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.presenter;

import com.example.administrator.readbook.base.IPresenter;

import java.util.LinkedHashMap;

public interface ILibraryPresenter extends IPresenter {

    LinkedHashMap<String, String> getKinds();

    void getLibraryData();
}
