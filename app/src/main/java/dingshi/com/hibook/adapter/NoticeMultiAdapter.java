package dingshi.com.hibook.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import dingshi.com.hibook.R;
import dingshi.com.hibook.bean.Notice;
import dingshi.com.hibook.utils.GlideUtils;

/**
 * @author wangqi
 * @since 2018/1/29 10:14
 */

public class NoticeMultiAdapter extends BaseMultiItemQuickAdapter<NoticeMultipleItem, BaseViewHolder> {
    private Context context;

    public NoticeMultiAdapter(Context context, List<NoticeMultipleItem> data) {
        super(data);
        this.context = context;
        addItemType(NoticeMultipleItem.NOTICE_MSG, R.layout.view_msg_notice);
        addItemType(NoticeMultipleItem.NOTICE_TXT, R.layout.view_msg_txt);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeMultipleItem item) {
        switch (item.getItemType()) {
            case NoticeMultipleItem.NOTICE_MSG:
                setMsgNotice(helper, (Notice.JsonDataBean) item.getData());
                break;
            case NoticeMultipleItem.NOTICE_TXT:
                stMsgTxt(helper, (Notice.JsonDataBean) item.getData());
                break;
            default:
        }
    }

    /**
     * 设置通知消息
     *
     * @param helper
     * @param bean
     */
    private void setMsgNotice(BaseViewHolder helper, Notice.JsonDataBean bean) {
        ImageView img = helper.getView(R.id.view_msg_notice_img);
        GlideUtils.load(context, bean.getImg(), img);
        helper.setText(R.id.view_msg_notice_time, bean.getCreated_at());
        helper.setText(R.id.view_msg_notice_title, bean.getContent());
    }

    /**
     * 设置通知的文本消息
     *
     * @param helper
     * @param bean
     */
    private void stMsgTxt(BaseViewHolder helper, Notice.JsonDataBean bean) {
        helper.setText(R.id.view_msg_txt, bean.getContent());
    }

}
