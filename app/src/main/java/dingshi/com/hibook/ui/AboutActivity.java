package dingshi.com.hibook.ui;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
/**
 * @author wangqi
 * @since 2018/1/31 下午4:02
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.about_describe)
    TextView txDescribe;


    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "关于我们");

        String str = "共享图书是一款提供图书在线借书，还书服务的APP，用户绑定支付宝在线支付就可享受读书优惠<br/></br>" +
                "<p>商务合作<p/>" +
                "共享图书是一款提供图书在线借书，还书服务的APP，用户绑定支付宝在线支付就可享受读书优惠<br/>" +
                "联系人：花生<br/>" +
                "Email：checklife@baozi.com<br/>" +
                "<p>加入我们<p/>" +
                "我们是一个年轻的团队，我们都有同一个梦想,在这里你将结识到优秀的同仁,拥抱无限宽广的职业前景<br/>" +
                "官方QQ群：999999<br/>" +
                "客服电话：400-888-8888";

        txDescribe.setText(Html.fromHtml(str));
    }
}
