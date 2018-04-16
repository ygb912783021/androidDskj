package dingshi.com.hibook.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import dingshi.com.hibook.R;

/**
 * @author wangqi
 * @since 2017/12/20 09:27
 */

public class CardMoreView extends BottomSheetDialog implements View.OnClickListener {
    public CardMoreView(@NonNull Context context) {
        super(context);
        initView(context);
    }


    public CardMoreView(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        initView(context);
    }

    protected CardMoreView(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private void initView(Context context) {
        setContentView(R.layout.view_dialog_card);
        findViewById(R.id.card_zxing).setOnClickListener(this);
        findViewById(R.id.card_edit).setOnClickListener(this);
        findViewById(R.id.card_main).setOnClickListener(this);
        findViewById(R.id.card_authen).setOnClickListener(this);
        findViewById(R.id.card_share).setOnClickListener(this);
        findViewById(R.id.card_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_zxing:
                onBackListener.onBackId(1);
                break;
            case R.id.card_edit:
                onBackListener.onBackId(2);
                break;
            case R.id.card_main:
                onBackListener.onBackId(3);
                break;
            case R.id.card_authen:
                onBackListener.onBackId(4);
                break;
            case R.id.card_share:
                onBackListener.onBackId(5);
                break;
            default:
        }
        this.dismiss();
    }

    OnBackListener onBackListener;

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }


    public interface OnBackListener {
        void onBackId(int id);
    }

}
