package dingshi.com.hibook.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;

public class FriendvalidationActivity extends BaseActivity{
    @BindView(R.id.user_friend_tel)
    TextView userFriendTel;
    @BindView(R.id.user_friend_show)
    TextView userFriendShow;
    private boolean isShow=false;
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true,"好友验证");
        userFriendShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShow) {
                    isShow = false;
                    userFriendShow.setText("隐藏");
                    userFriendTel.setText("13245677654");
                } else {
                    isShow = true;
                    userFriendShow.setText("显示");
                    userFriendTel.setText("号码已隐藏");
                }
            }
    });
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_friend_item;
    }


}
