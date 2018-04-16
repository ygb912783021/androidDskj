package dingshi.com.hibook.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dingshi.com.hibook.R;
import dingshi.com.hibook.utils.PixelUtils;

/**
 * @author wangqi
 *         Created by apple on 2017/11/3.
 */

public class TitleView extends LinearLayout {

    private View view;
    private ImageView backImg;
    private ImageView rightImage;
    private TextView centerText;
    private TextView rightText;

    private String strCenter;
    private String strRight;
    private boolean haveBackImg;

    int backGroundColor;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        haveBackImg = ta.getBoolean(R.styleable.TitleView_have_back_img, false);
        strCenter = ta.getString(R.styleable.TitleView_center_content);
        strRight = ta.getString(R.styleable.TitleView_right_content);
        backGroundColor = ta.getColor(R.styleable.TitleView_backgroud_color, getResources().getColor(R.color.theme));
        ta.recycle();
        initView(context);
    }

    private void initView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.view_title, null, false);
        view.setBackgroundColor(backGroundColor);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, PixelUtils.dip2Px(context, 45));
        addView(view, params);
        backImg = view.findViewById(R.id.title_back);
        centerText = view.findViewById(R.id.title_center);
        rightText = view.findViewById(R.id.title_right);
        rightImage = view.findViewById(R.id.title_right_img);


        showBackImage(haveBackImg);

        centerText.setText(strCenter);
        rightText.setText(strRight);

        backImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFinishListener != null) {
                    onFinishListener.over();
                }

            }
        });

        rightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightClickListner != null) {
                    onRightClickListner.click();
                }
            }
        });

        rightImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightClickListner != null) {
                    onRightClickListner.click();
                }
            }
        });
    }

    /**
     * 设置标题
     *
     * @param str
     */
    public void setTitle(String str) {
        centerText.setText(str);
    }

    /**
     * 设置标题栏右边内容
     *
     * @param str
     */
    public void setRight(String str) {
        rightText.setText(str);
    }

    /**
     * 显示返回按钮
     *
     * @param flag
     */
    public void showBackImage(boolean flag) {
        if (flag) {
            backImg.setVisibility(View.VISIBLE);
            backImg.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        } else {
            backImg.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题栏右边图片的资源
     *
     * @param resouse 0的时候不显示
     */
    public void setRightImage(int resouse) {
        if (resouse == 0) {
            rightText.setVisibility(View.VISIBLE);
            rightImage.setVisibility(View.GONE);
        } else {
            rightText.setVisibility(View.GONE);
            rightImage.setVisibility(View.VISIBLE);
            rightImage.setImageResource(resouse);
        }
    }

    /**
     * 批量设置标题
     *
     * @param flag       是否显示返回按钮
     * @param title      居中标题
     * @param rightTitle 居右标题
     */
    public void requestActionBarStyle(boolean flag, String title, String rightTitle) {
        showBackImage(flag);
        setTitle(title);
        setRight(rightTitle);
        setRightImage(0);
    }

    /**
     * 批量设置标题
     *
     * @param flag        是否显示返回按钮
     * @param title       居中标题
     * @param rightTitle  居右标题
     * @param imgResourse 居右图片
     */
    public void requestActionBarStyle(boolean flag, String title, String rightTitle, int imgResourse) {
        showBackImage(flag);
        setTitle(title);
        setRight(rightTitle);
        setRightImage(imgResourse);
    }


    OnFinishListener onFinishListener;
    OnRightClickListner onRightClickListner;


    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public void setOnRightListener(OnRightClickListner onRightClickListner) {
        this.onRightClickListner = onRightClickListner;
    }

    /**
     * 返回按钮回调
     */
    public interface OnFinishListener {
        /**
         * 返回按钮的回调
         */
        void over();
    }

    /**
     * 右边按钮点击
     */
    public interface OnRightClickListner {
        /**
         * 最右点击按钮的回调
         */
        void click();
    }

}
