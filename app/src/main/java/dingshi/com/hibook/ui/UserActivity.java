package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuyh.library.imgsel.ISNav;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IUserView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.present.UserPresent;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.ImageSelector;

/**
 * @author wangqi
 * @since 2017/11/9 下午5:54
 */

public class UserActivity extends BaseActivity implements IUserView {

    private static final int REQUEST_CAMERA_CODE = 0x01;
    @BindView(R.id.user_nick_text)
    TextView txNick;
    @BindView(R.id.user_gender_text)
    TextView txGender;
    @BindView(R.id.user_born_text)
    TextView txBorn;
    @BindView(R.id.user_mobile_text)
    TextView txMobile;
    @BindView(R.id.user_time_text)
    TextView txTime;
    @BindView(R.id.user_photo)
    ImageView userPhoto;

    UserPresent userPresent;

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "个人信息");
        Log.i("userActivity",user.toString());

        userPresent = new UserPresent(this, this, user);
        User.JsonDataBean bean = user.getJsonData();

        txNick.setText(bean.getNick_name());
        if (bean.getSex() == 1) {
            txGender.setText("男");
        } else if (bean.getSex() == 2) {
            txGender.setText("女");
        } else {
            txGender.setText("未设置");
        }

        if ("".equals(bean.getBirthday())) {
            txBorn.setText("未设置");
        } else {
            txBorn.setText(bean.getBirthday());
        }


        txMobile.setText(bean.getMobile());
        txTime.setText(bean.getCreated_at());
        GlideUtils.loadCircleImage(this, bean.getAvatar(), userPhoto);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_user;
    }


    @OnClick({R.id.user_born, R.id.user_photo,
            R.id.user_gender, R.id.user_nick})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_photo:
                ISNav.getInstance().toListActivity(this, ImageSelector.getSingleConfig(), REQUEST_CAMERA_CODE);
                break;
            case R.id.user_nick:
                userPresent.nick();
                break;
            case R.id.user_gender:
                userPresent.gender();
                break;
            case R.id.user_born:
                userPresent.born();
                break;
            default:
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            userPresent.avatar(pathList.get(0));
            GlideUtils.loadCircleImage(this, pathList.get(0), userPhoto);
        }
    }


    @Override
    public void updateNick(String nick) {
        txNick.setText(nick);
    }

    @Override
    public void updateGender(String gender) {
        txGender.setText(gender);
    }

    @Override
    public void updateBorn(String born) {
        txBorn.setText(born);
    }

    @Override
    public void updateAvatar(User user) {
        EventBus.getDefault().post(user);
    }

    @Override
    public void error(String error) {
        showToast(error);
    }
}
