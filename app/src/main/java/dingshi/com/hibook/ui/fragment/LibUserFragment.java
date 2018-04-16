package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ILibUserView;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.lib.LibIntro;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.present.LibUserPresent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.card.CardDetailsActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.CallUtils;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.PixelUtils;
import dingshi.com.hibook.view.DividVerticalDecoration;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


/**
 * @author wangqi
 * @since 2017/12/22 下午2:57
 */

public class LibUserFragment extends BaseFragment implements ILibUserView {
    @BindView(R.id.lib_user_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.lib_user_search)
    EditText editSearch;
    @BindView(R.id.lib_user_recycle)
    RecyclerView mRecycleview;

    int page = 1;
    String catalogId;
    String keyWorlds = "";
    FuckYouAdapter fuckYouAdapter;
    List<LibIntro.JsonDataBean> list = new ArrayList<>();


    LibUserPresent present = new LibUserPresent(this, this);


    int currentPosition;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_lib_user;
    }

    @Override
    public void initView() {
        catalogId = getArguments().getString("catalog_id");
        mRecycleview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecycleview.addItemDecoration(new DividVerticalDecoration(mActivity, PixelUtils.dip2Px(mActivity, 10), 0xfff1f1f1));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_lib_user, list);
        mRecycleview.setAdapter(fuckYouAdapter);


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(final BaseViewHolder helper, Object item) {
                final LibIntro.JsonDataBean bean = (LibIntro.JsonDataBean) item;
                helper.setText(R.id.view_lib_user_id, (helper.getLayoutPosition() + 1) + ".");
                helper.setText(R.id.view_lib_user_nick, bean.getName());
                ImageView photo = helper.getView(R.id.view_lib_user_photo);
                GlideUtils.loadCircleImage(mActivity, bean.getAvatar(), photo);
                helper.setText(R.id.view_lib_user_content, "藏书 :" + bean.getTotal() + "   共享图书 :" + bean.getShare());

                if (bean.getIs_creator() == 1) {
                    helper.getView(R.id.view_lib_user_flag).setVisibility(View.VISIBLE);
                    helper.getView(R.id.view_lib_phone_layout).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.view_lib_user_flag).setVisibility(View.GONE);
                    helper.getView(R.id.view_lib_phone_layout).setVisibility(View.VISIBLE);
                }

                //打电话
                helper.getView(R.id.view_lib_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallUtils.callPhone(mActivity, bean.getPhone());
                    }
                });
                //发信息
                helper.getView(R.id.view_lib_msg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallUtils.msgPhone(mActivity, bean.getPhone());
                    }
                });
                //踢出目录
                helper.getView(R.id.view_lib_quit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition = helper.getLayoutPosition();
                        present.deleteUserShowDialog(catalogId, bean.getUser_id());
                    }
                });
            }
        });

        /**
         * 点击跳转卡片详情
         */
        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, CardDetailsActivity.class);
                intent.putExtra("card_id", list.get(position).getCard_id());
                intent.putExtra("uid", list.get(position).getUser_id());
                startActivity(intent);
            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                keyWorlds = "";
                present.getUserList(keyWorlds, catalogId, page);
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                present.getUserList(keyWorlds, catalogId, page);
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (handler.hasMessages(MSG_SEARCH)) {
                    handler.removeMessages(MSG_SEARCH);
                }
                handler.sendEmptyMessageDelayed(MSG_SEARCH, 500);

            }
        });
    }


    @Override
    public void lazyLoad() {
        super.lazyLoad();
        present.getUserList(keyWorlds, catalogId, page);
    }


    public static int MSG_SEARCH = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            keyWorlds = editSearch.getText().toString();
            page = 1;
            present.getUserList(keyWorlds, catalogId, page);
        }
    };


    @Override
    public void onUserList(LibIntro libIntro) {
        if (libIntro.getJsonData() != null) {
            if (page == 1) {
                list.clear();
            }
            list.addAll(libIntro.getJsonData());
            fuckYouAdapter.notifyDataSetChanged();
        }
        smartRefreshLayout.finishLoadmore();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void onDeleteUser() {
        fuckYouAdapter.notifyItemRemoved(currentPosition);
        list.remove(currentPosition);
        EventBus.getDefault().post(new LibRefreshEvent());
    }

    @Override
    public void onError(String error) {
        showToast(error);
    }
}
