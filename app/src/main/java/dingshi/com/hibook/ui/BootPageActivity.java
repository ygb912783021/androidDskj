package dingshi.com.hibook.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.utils.GlideUtils;

public class BootPageActivity extends BaseActivity {

    @BindView(R.id.boot_page_banner)
    Banner banner;
    @BindView(R.id.boot_page_register)
    TextView txRegister;
    @BindView(R.id.boot_page_login)
    TextView txLogin;

    Integer[] imgId = new Integer[]{R.drawable.boot_page_1, R.drawable.boot_page_2,
            R.drawable.boot_page_3, R.drawable.boot_page_4};

    @Override
    public int getLayoutId() {
        return R.layout.activity_boot_page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hideTitleBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

//        SpannableString str = new SpannableString("您已有账号吗?点击登录");
//        ForegroundColorSpan fore = new ForegroundColorSpan(0xff3273fe);
//        str.setSpan(fore, 7, str.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
//        txLogin.setText(str);

        banner.setImages(Arrays.asList(imgId))
                .isAutoPlay(true)
                .setImageLoader(new BannerBootLoader())
                .start();
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==3){
                    banner.setViewPagerIsScroll(false);
                }
            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class BannerBootLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object object, ImageView imageView) {
            Integer resource = (Integer) object;
            GlideUtils.load(context, resource, imageView);
        }
    }


    @OnClick({R.id.boot_page_register})//, R.id.boot_page_login
    public void onClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        switch (view.getId()) {
            case R.id.boot_page_register:
//                intent.putExtra("register", true);
                intent.putExtra("register", false);
                break;
//            case R.id.boot_page_login:
//                intent.putExtra("register", false);
//                break;
            default:
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }
}
