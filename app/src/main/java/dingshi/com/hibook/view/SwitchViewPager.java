package dingshi.com.hibook.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import dingshi.com.hibook.R;
import dingshi.com.hibook.utils.PixelUtils;

/**
 * @author wangqi
 *         Created by apple on 2017/11/2.
 */

public class SwitchViewPager extends LinearLayout implements View.OnClickListener {

    /**
     * 默认图片资源
     */
    int normalImgRes;
    /**
     * 选中图片资源
     */
    int focusImgRes;

    /**
     * 未选中字体的颜色
     */
    int normalTextColor;

    /**
     * 选中字体的颜色
     */
    int focusTextColor;

    /**
     * 字体的大小
     */
    int textSize;

    Context context;

    ViewPager viewPager;
    /**
     * 居中布局
     */
   public LinearLayout layout;

    List<TextView> list = new ArrayList<>();

    public SwitchViewPager(Context context) {
        this(context, null);
    }

    public SwitchViewPager(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchViewPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwitchViewPager);
        normalImgRes = ta.getResourceId(R.styleable.SwitchViewPager_normal_img, R.drawable.shap_gray);
        focusImgRes = ta.getResourceId(R.styleable.SwitchViewPager_focus_img, R.drawable.shap_white);
        normalTextColor = ta.getColor(R.styleable.SwitchViewPager_normal_text_color, getResources().getColor(R.color.black_999));
        focusTextColor = ta.getColor(R.styleable.SwitchViewPager_focus_text_color, getResources().getColor(R.color.gold_d8));
        textSize = ta.getDimensionPixelOffset(R.styleable.SwitchViewPager_text_size_switch, PixelUtils.dip2Px(context, 5));
        ta.recycle();

        setBackgroundColor(getResources().getColor(R.color.white));
        setGravity(Gravity.CENTER);
    }

    /**
     * 绑定viewpager并添加title
     *
     * @param viewPager
     * @param title
     */
    public void bindViewPager(ViewPager viewPager, String... title) {
        layout = new LinearLayout(context);
        layout.setBackgroundResource(R.drawable.shap_gray);
        layout.setPadding(10, 0, 10, 0);
        this.viewPager = viewPager;
        addView(layout);


        for (int i = 0; i < title.length; i++) {
            TextView tv = new TextView(context);
            tv.setText(title[i]);
            tv.setTag(i);
            tv.setBackgroundResource(normalImgRes);
            tv.setTextColor(normalTextColor);
            tv.setTextSize(textSize);
            addItem(tv);
        }

        if (list.size() > 0) {
            list.get(0).setTextColor(focusTextColor);
            list.get(0).setBackgroundResource(focusImgRes);
        }
        setPageListner();
    }


    /***
     * 绑定viewpager
     * @param viewPager
     */
    public void bindViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        setPageListner();
    }

    /**
     * 设置viewpager的监听
     */
    private void setPageListner() {

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                reset(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 添加item
     *
     * @param tv
     */
    public void addItem(TextView tv) {
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(0, DensityUtil.dp2px(5), 0, DensityUtil.dp2px(5));
        LayoutParams params = new LayoutParams(DensityUtil.dp2px(90), LayoutParams.MATCH_PARENT);
        params.setMargins(20, 10, 20, 10);
        layout.addView(tv, params);
        list.add(tv);
        tv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        reset(position);
        if (viewPager != null) {
            viewPager.getAdapter().notifyDataSetChanged();
            viewPager.setCurrentItem(position);
        }
    }

    /**
     * 重置控件
     *
     * @param position
     */
    public void reset(int position) {
        for (int i = 0; i < list.size(); i++) {
            TextView tv = list.get(i);
            if (i == position) {
                tv.setBackgroundResource(focusImgRes);
                tv.setTextColor(focusTextColor);
            } else {
                tv.setBackgroundResource(normalImgRes);
                tv.setTextColor(normalTextColor);
            }
        }
    }
}
