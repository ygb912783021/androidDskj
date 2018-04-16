package dingshi.com.hibook.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dingshi.com.hibook.R;

/**
 * @author wangqi
 *         Created by apple on 2017/10/25.
 */

public class BottomBarItem extends LinearLayout {

    /**
     * 默认图片资源
     */
    int normalImgRes;
    /**
     * 选中图片资源
     */
    int focusImgRes;

    /**
     * 图片的尺寸
     */
    int imgSize;

    /**
     * 默认字体颜色
     */
    int normalColor;
    /**
     * 选中字体颜色
     */
    int focusColor;

    /**
     * 底部文字
     */
    String fontText;

    /**
     *
     */
    int textSize;

    ImageView imageView;
    TextView textView;

    public BottomBarItem(Context context) {
        this(context, null);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarItem);
        normalImgRes = ta.getResourceId(R.styleable.BottomBarItem_normal_icon, -1);
        focusImgRes = ta.getResourceId(R.styleable.BottomBarItem_focus_icon, -1);
        normalColor = ta.getColor(R.styleable.BottomBarItem_normal_color, 0xff000000);
        focusColor = ta.getColor(R.styleable.BottomBarItem_focus_color, 0xff000000);
        fontText = ta.getString(R.styleable.BottomBarItem_bottom_text);
        imgSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_icon_size, 30);
        textSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_text_size, 10);
        ta.recycle();

        initView(context);
    }

    /**
     * 初始化view
     */
    private void initView(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        imageView = new ImageView(context);
        //设置默认图片
        imageView.setImageResource(normalImgRes);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgSize, imgSize);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);


        textView = new TextView(context);
        textView.setText(fontText);
        textView.setTextColor(normalColor);
        textView.setTextSize(textSize);
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        addView(imageView);
        addView(textView);

        //如果没有设置底部的字的话就隐藏
        if ("".equals(fontText)) {
            textView.setVisibility(View.GONE);
        }
    }


    public void setStatus(boolean flag) {
        if (flag) {
            imageView.setImageResource(focusImgRes);
            textView.setTextColor(focusColor);
        } else {
            imageView.setImageResource(normalImgRes);
            textView.setTextColor(normalColor);
        }
    }

}
