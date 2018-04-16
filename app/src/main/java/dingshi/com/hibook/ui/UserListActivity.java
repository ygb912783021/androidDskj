package dingshi.com.hibook.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuyh.library.imgsel.ISNav;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.StoreMultiAdapter;
import dingshi.com.hibook.adapter.UserListAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.utils.ImageSelector;

public class UserListActivity extends BaseActivity {
    @BindView(R.id.user_list_recycle)
    RecyclerView userListRecycle;


    @Override
    public int getLayoutId() {
        return R.layout.activity_userlist;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "新用户", "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        userListRecycle.setLayoutManager(layoutManager );
        UserListAdapter adapter=new UserListAdapter(this,null);
        userListRecycle.setAdapter(adapter);

    }

}
