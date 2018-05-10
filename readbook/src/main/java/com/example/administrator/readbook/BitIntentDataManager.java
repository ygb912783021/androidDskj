//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook;

import java.util.HashMap;
import java.util.Map;

/**
 * 位意图数据管理器
 */
public class BitIntentDataManager {
    public static Map<String,Object> bigData;

    private static BitIntentDataManager instance = null;

    public static BitIntentDataManager getInstance(){
        if(instance == null){
            synchronized (BitIntentDataManager.class){
                if(instance == null){
                    instance = new BitIntentDataManager();
                }
            }
        }
        return instance;
    }

    private BitIntentDataManager(){
        bigData = new HashMap<>();
    }
    public Object getData(String key){
        return bigData.get(key);
    }
    public void putData(String key, Object data){
        bigData.put(key,data);
    }
    public void cleanData(String key){
        bigData.remove(key);
    }
}
