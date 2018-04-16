package dingshi.com.hibook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dingshi.com.hibook.R;

/**
 * @author wangqi
 *         Created by apple on 2017/10/25.
 */

public class EmptyView extends RelativeLayout {
    View mView;
    @BindView(R.id.empty_view_loading)
    LinearLayout loadingLayout;
    @BindView(R.id.empty_view_loadfail)
    LinearLayout loadFailLayout;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = LayoutInflater.from(context).inflate(R.layout.view_empty, null, false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
        addView(mView, param);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        ButterKnife.bind(this, mView);
    }

    /**
     * 显示正在加载的页面
     */
    public void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        loadFailLayout.setVisibility(View.GONE);
    }

    /**
     * 显示加载失败的页面
     */
    public void showLoadfail() {
        loadingLayout.setVisibility(View.GONE);
        loadFailLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载成功
     */
    public void showLoadSucess() {
        setVisibility(View.GONE);
    }

    @OnClick({R.id.empty_view_loadfail})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty_view_loadfail:
                showLoading();
                break;
            default:
        }
    }

}
