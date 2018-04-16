package dingshi.com.hibook.view;

/**
 * @author wangqi
 * @since 2017/12/12 11:05
 */

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.List;

import dingshi.com.hibook.R;
import dingshi.com.hibook.bean.Home;


/**
 * @author wangqi
 * @since 2017/12/12 上午11:34
 */

public class UpDownTextView extends LinearLayout {
    private Context mContext;
    /**
     * 配合滚动的三个TextView
     * 0和1配合向下  1和2配合向上
     */
    private TextView[] textViews = new TextView[3];

    private LinearLayout llayout;

    private String curText = null;

    /***
     * 动画时间
     */
    private int mAnimTime = 500;

    /**
     * 停留时间
     */
    private int mStillTime = 3500;

    /***
     * 轮播数据
     */
    private List<Home.JsonDataBean.HeadlineBean> mTextList;

    /***
     * 当前轮播的索引
     */
    private int currentIndex = 0;

    /***
     * 动画模式
     * 默认向上 0--向上，1--向下
     */
    private int animMode = 0;

    /***
     * 是否正在自动滚动
     */
    private boolean isRunning = false;

    public final static int ANIM_MODE_UP = 0;
    public final static int ANIM_MODE_DOWN = 1;

    private TranslateAnimation animationDown, animationUp;

    public UpDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (!isInEditMode()) {
            initViews();
        }
    }

    private void initViews() {
        llayout = new LinearLayout(mContext);
        llayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(llayout);

        textViews[0] = addText();
        textViews[1] = addText();
        textViews[2] = addText();


    }


    /***
     * 当界面销毁时
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 防止内存泄漏的操作
        //在RecycleView滑动的时候回调用onDetachedFromWindow方法导致动画关闭
        //解决方法是在onAttachedToWindow进行恢复
        stopAutoScroll();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTextList != null && mTextList.size() > 0) {
            startAutoScroll();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isInEditMode()) {
            //7.0以后，需要界面可见时，设置宽高才有效果
            post(new Runnable() {
                @Override
                public void run() {
                    setViewsHeight();
                }
            });
        }
    }

    /***
     * 重新设置VIEW的高度
     */
    private void setViewsHeight() {
        for (TextView tv : textViews) {
            LayoutParams lp = (LayoutParams) tv.getLayoutParams();
            lp.height = getHeight();
            lp.width = getWidth();
            tv.setLayoutParams(lp);
        }

        LayoutParams lp2 = (LayoutParams) llayout.getLayoutParams();
        lp2.height = getHeight() * (llayout.getChildCount());
        // 使向上偏移一定的高度，用padding,scrollTo都分有问题
        lp2.setMargins(0, -getHeight(), 0, 0);
        llayout.setLayoutParams(lp2);
    }

    @Override
    public void setGravity(int graty) {
        for (TextView tv : textViews) {
            tv.setGravity(graty);
        }
    }

    public void setTextSize(int dpSize) {
        for (TextView tv : textViews) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dpSize);
        }
    }

    public void setTextColor(int color) {
        for (TextView tv : textViews) {
            tv.setTextColor(color);
        }
    }

    private TextView addText() {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextSize(13);
        tv.setTextColor(getResources().getColor(R.color.gold_d9));
        llayout.addView(tv);
        return tv;
    }

    /***
     * 设置初始的字
     *
     * @param curText
     */
    public void setText(String curText) {
        this.curText = curText;
        textViews[1].setText(curText);
    }

    /***
     * 开始自动滚动
     */
    public void startAutoScroll() {

        if (!isRunning) {
            isRunning = true;
            if (mTextList == null || mTextList.size() == 0) {
                stopAutoScroll();
                return;
            }
            //设置初始字体
            textViews[1].setText(mTextList.get(0).getTitle());
            // 可用runnable来代替hander或者 timer
            this.postDelayed(runnable, mStillTime + mAnimTime);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTextClickListener != null) {
                    onTextClickListener.onClick(mTextList.get(currentIndex).getSource_url());
                }
            }
        });
    }

    /***
     * 停止自动滚动
     */
    public void stopAutoScroll() {
        if (isRunning) {
            isRunning = false;
            this.removeCallbacks(runnable);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentIndex++;
            currentIndex = (currentIndex) % mTextList.size();
            switch (animMode) {
                case ANIM_MODE_UP:
                    setTextUpAnim(mTextList.get(currentIndex).getTitle());
                    break;
                case ANIM_MODE_DOWN:
                    setTextDownAnim(mTextList.get(currentIndex).getTitle());
                    break;
                default:
            }
            UpDownTextView.this.postDelayed(runnable, mStillTime + mAnimTime);

        }
    };

    /***
     * 向上弹动画
     *
     * @param curText
     */
    public void setTextUpAnim(String curText) {
        this.curText = curText;
        textViews[2].setText(curText);
        up();// 向上的动画
    }

    public void setTextDownAnim(String text) {
        this.curText = text;
        textViews[0].setText(text);
        down();// 向上的动画
    }

    public void setDuring(int during) {
        this.mAnimTime = during;
    }

    /***
     * 向上动画
     */
    private void up() {
        llayout.clearAnimation();
        if (animationUp == null) {
            animationUp = new TranslateAnimation(0, 0, 0, -getHeight());
        }
        animationUp.setDuration(mAnimTime);
        llayout.startAnimation(animationUp);
        animationUp.setAnimationListener(listener);
    }

    /***
     * 向下动画
     */
    public void down() {
        llayout.clearAnimation();
        if (animationDown == null) {
            animationDown = new TranslateAnimation(0, 0, 0, getHeight());
        }
        animationDown.setDuration(mAnimTime);
        llayout.startAnimation(animationDown);
        animationDown.setAnimationListener(listener);
    }


    /***
     * 动画监听，动画完成后，动画恢复，设置文本
     */


    AnimationListener listener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            setText(curText);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };


    public int getAnimTime() {
        return mAnimTime;
    }

    public void setAnimTime(int mAnimTime) {
        this.mAnimTime = mAnimTime;
    }

    public int getStillTime() {
        return mStillTime;
    }

    public void setStillTime(int mStillTime) {
        this.mStillTime = mStillTime;
    }

    public List<Home.JsonDataBean.HeadlineBean> getTextList() {
        return mTextList;
    }

    public void setTextList(List<Home.JsonDataBean.HeadlineBean> mTextList) {
        this.mTextList = mTextList;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getAnimMode() {
        return animMode;
    }

    public void setAnimMode(int animMode) {
        this.animMode = animMode;
    }

    public void setSingleLine() {
        for (TextView tv : textViews) {
            tv.setSingleLine();
            //尾部打省略号
            tv.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    OnTextClickListener onTextClickListener;

    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        this.onTextClickListener = onTextClickListener;
    }

    public interface OnTextClickListener {
        void onClick(String url);
    }

}