package dingshi.com.hibook.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author wangqi
 *         Created by wangqi on 2017/8/16.
 */

public class FuckYouAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {


    public FuckYouAdapter(@LayoutRes int layoutResId, List<T> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        if (onCallBackData != null) {
            onCallBackData.convertView(helper, item);
        }
    }

    private OnCallBackData onCallBackData;

    public void setOnCallBackData(OnCallBackData onCallBackData) {
        this.onCallBackData = onCallBackData;
    }

    /**
     * 数据填充布局的回调
     *
     * @param <T> 数据
     */
    public interface OnCallBackData<T> {
        /**
         * 布局抽象接口
         *
         * @param helper 布局
         * @param item   数据源
         */
        void convertView(BaseViewHolder helper, T item);
    }
}