package dingshi.com.hibook.action;

import android.os.Bundle;

/**
 * Created by Administrator on 2017/9/20.
 */

public interface LifeCycleListener {
    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
