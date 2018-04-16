package dingshi.com.hibook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import dingshi.com.hibook.R;
import dingshi.com.hibook.utils.GlideUtils;

/**
 * @author wangqi
 *         <p>
 *         注释：在滑动的过程中，首先会调用onStateChanged的 PullDownToRefresh 触发下拉
 *         在拖拽的过程中，会调用PullDownToRefresh方法，可以拿到 offset 偏移量
 *         滑动超过设置的高度的位置时会触发ReleaseToRefresh提示用户去刷新
 *         松开手的时候会调用onStateChanged的Refreshing和onStartAnimator方法
 *         下拉结束时会调用finish方法
 */
public class BookRefreshHeader extends RelativeLayout implements RefreshHeader {

    /**
     * 下拉的样式
     */
    private SpinnerStyle mSpinnerStyle = SpinnerStyle.Scale;

    public BookRefreshHeader(Context context) {
        super(context);
        this.initView(context, null);
    }

    public BookRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    public BookRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        setMinimumHeight(DensityUtil.dp2px(80));
//        setBackgroundColor(0xfff1f1f1);
        ImageView img = new ImageView(context);
        img.setImageResource(R.mipmap.ic_launcher);

        GlideUtils.load(context, R.drawable.loading, img);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(DensityUtil.dp2px(50), DensityUtil.dp2px(50));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        addView(img, params);
    }


    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
    }


    /**
     * 返回null header是否停留在顶上
     *
     * @return
     */
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return mSpinnerStyle;
    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
//        Log.i("header", "onStartAnimator---->");
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
//        Log.i("header", "finish---->");
        return 0;//延迟0毫秒之后再弹回、
    }

    @Override
    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
//        Log.i("header", "onPullingDown---->" + offset);
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
//                Log.i("header", "PullDownToRefresh---->");
                break;
            case Refreshing:
//                Log.i("header", "Refreshing---->");
                break;
            case ReleaseToRefresh:
//                Log.i("header", "ReleaseToRefresh---->");
                break;
            case Loading:
//                Log.i("header", "Loading---->");
                break;
            default:
        }
    }


    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {

    }


    @Override
    public void setPrimaryColors(int... colors) {
    }

    @Override
    public View getView() {
        return this;
    }

}
