package dingshi.com.hibook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;

/**
 * @author wangqi
 * @since 2018/3/1 下午3:38
 */

public class RefundActivity extends BaseActivity {

    @BindView(R.id.refund_flag2)
    TextView txFlag;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "");
        SpannableString str = new SpannableString("29元押金退款成功");
        ForegroundColorSpan fore = new ForegroundColorSpan(0xff3273fe);
        str.setSpan(fore, 2, str.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        txFlag.setText(str);
    }

    @OnClick(R.id.refund_submit)
    public void onClick(View view) {
        finish();
    }
}
