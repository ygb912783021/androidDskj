package dingshi.com.hibook.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import dingshi.com.hibook.R;

/**
 * @author wangqi
 * @since 2017/11/13 11:25
 */

public class GenderView extends BottomSheetDialog implements View.OnClickListener {
    public GenderView(@NonNull Context context) {
        super(context);
        initView();
    }

    public GenderView(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        initView();
    }

    protected GenderView(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    TextView man, women, cancel;

    protected void initView() {
        setContentView(R.layout.view_dialog_gender);
        man = (TextView) findViewById(R.id.gender_man);
        women = (TextView) findViewById(R.id.gender_women);
        cancel = (TextView) findViewById(R.id.gender_cancel);
        man.setOnClickListener(this);
        women.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    //0.未知,1.男,2.女
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gender_man:
                genderCallBack.back("1");
                break;
            case R.id.gender_women:
                genderCallBack.back("2");
                break;
            default:
        }
        dismiss();
    }
    GenderCallBack genderCallBack;
    public void setOnGenderCallBackListner(GenderCallBack genderCallBack) {
        this.genderCallBack = genderCallBack;
    }
    public interface GenderCallBack {
        void back(String result);
    }

    /**
     * dialog销毁的时候触发
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
