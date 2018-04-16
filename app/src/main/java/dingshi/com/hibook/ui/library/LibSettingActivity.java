package dingshi.com.hibook.ui.library;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ILibSettingView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.lib.LibList;
import dingshi.com.hibook.present.LibSettingPresent;

/**
 * @author wangqi
 * @since 2017/12/22 下午3:34
 */

public class LibSettingActivity extends BaseActivity implements ILibSettingView, CompoundButton.OnCheckedChangeListener {

    /**
     * 人数上限
     */
    @BindView(R.id.lib_setting_limit)
    EditText editLimit;
    /**
     * 目录允许加入
     */
    @BindView(R.id.lib_setting_join)
    SwitchCompat switchJoin;
    /**
     * 允许用户加入
     */
    @BindView(R.id.lib_setting_consult)
    SwitchCompat switchConsult;
    /**
     * 通知提醒设置
     */
    @BindView(R.id.lib_setting_notice)
    SwitchCompat switchNotice;
    /**
     * 设置虚拟人数
     */
    @BindView(R.id.lib_setting_fake)
    EditText editFake;
    /**
     * 公开到广场
     */
    @BindView(R.id.lib_setting_open)
    SwitchCompat switchOpen;

    LibSettingPresent settingPresent = new LibSettingPresent(this, this);
    LibList.JsonDataBean libBean;

    String strLimit;
    String strFake;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lib_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "高级设置", "");
        libBean = (LibList.JsonDataBean) getIntent().getSerializableExtra("libBean");
        updateUI();


        editLimit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //监听editText失去焦点
                if (!hasFocus) {
                    String txt = editLimit.getText().toString().trim();
                    //如果当前的文本与之前的文本不一致，请求后台
                    if (!txt.equals(strLimit)) {
                        //将设置同步到全局
                        strLimit = txt;
                        settingPresent.setLimitPerson(libBean.getCatalog_id(),strLimit);
                    }
                }
            }
        });

        editFake.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //监听editText失去焦点
                if (!hasFocus) {
                    String txt = editFake.getText().toString().trim();
                    //如果当前的文本与之前的文本不一致，请求后台
                    if (!txt.equals(strFake)) {
                        //将设置同步到全局
                        strFake = txt;
                        settingPresent.setFakePerson(libBean.getCatalog_id(),strFake);
                    }
                }
            }
        });
    }

    /**
     * 更新ui
     */
    private void updateUI() {
        strLimit = libBean.getUser_limit();
        strFake = libBean.getFake_user();

        editLimit.setText(libBean.getUser_limit());
        editFake.setText(libBean.getFake_user());
        switchJoin.setChecked(libBean.getIs_allow() == 1);
        switchConsult.setChecked(libBean.getIs_consult() == 1);
        switchNotice.setChecked(libBean.getMessage_state() == 1);
        switchOpen.setChecked(libBean.getIs_open() == 1);

        switchJoin.setOnCheckedChangeListener(this);
        switchConsult.setOnCheckedChangeListener(this);
        switchNotice.setOnCheckedChangeListener(this);
        switchOpen.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        editLimit.clearFocus();
        editFake.clearFocus();
        switch (buttonView.getId()) {
            case R.id.lib_setting_join:
                showToast(isChecked + "");
                settingPresent.setJoin(libBean.getCatalog_id(), isChecked);
                break;
            case R.id.lib_setting_consult:
                settingPresent.setConsult(libBean.getCatalog_id(), isChecked);
                break;
            case R.id.lib_setting_notice:
                settingPresent.setNotice(libBean.getCatalog_id(), isChecked);
                break;
            case R.id.lib_setting_open:
                settingPresent.setOpen(libBean.getCatalog_id(), isChecked);
                break;
            default:
        }
    }


    @Override
    public void onError(String string) {

    }
}