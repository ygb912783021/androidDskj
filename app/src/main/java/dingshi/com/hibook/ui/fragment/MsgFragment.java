package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.MyViewPagerAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.hx.ChatActivity;
import dingshi.com.hibook.hx.EaseConversationListFragment;
import dingshi.com.hibook.view.SwitchViewPager;

/**
 * @author wangqi
 * @since 2017/12/11 下午1:48
 */


public class MsgFragment extends BaseFragment {
    @BindView(R.id.msg_switch)
    SwitchViewPager switchViewPager;
    @BindView(R.id.msg_viewpager)
    ViewPager viewPager;


    String[] title = new String[]{"私信", "评论", "通知"};
    List<Fragment> list = new ArrayList<>();

    EaseConversationListFragment letterFragment;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_msg;
    }

    @Override
    public void initView() {
        switchViewPager.bindViewPager(viewPager, title);

        letterFragment = new EaseConversationListFragment();
        list.add(letterFragment);
        BaseFragment evalFragment = new FriendRecFragment();
        list.add(evalFragment);
        MsgNoticeFragment noticeFragment = new MsgNoticeFragment();
        list.add(noticeFragment);

        viewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(), list, title));


        letterFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(mActivity, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });

    }


    public void refresh() {
        letterFragment.refresh();
    }
}
