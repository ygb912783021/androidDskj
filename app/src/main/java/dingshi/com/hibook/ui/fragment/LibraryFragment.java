package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.lib.LibCreate;
import dingshi.com.hibook.bean.lib.LibHome;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.RallyActivity;
import dingshi.com.hibook.ui.RallyDetailsActivity;
import dingshi.com.hibook.ui.library.CreateLibActivity;
import dingshi.com.hibook.ui.library.JoinLibActivity;
import dingshi.com.hibook.ui.library.LibDetailsActivity;
import dingshi.com.hibook.ui.library.LibDiscActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/11 下午1:44
 * 图书馆
 */

public class LibraryFragment extends BaseFragment {
    @BindView(R.id.library_recycle)
    RecyclerView myRecycle;
    FuckYouAdapter fuckYouAdapter;
    List<LibHome.JsonDataBean.MyCatalogBean> myList = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.fragment_library;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);



        myRecycle.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_item_library, myList);
        myRecycle.setAdapter(fuckYouAdapter);


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                LibHome.JsonDataBean.MyCatalogBean bean = (LibHome.JsonDataBean.MyCatalogBean) item;

                if (helper.getLayoutPosition() == 0) {
                    helper.setText(R.id.item_lib_title, bean.getTitle());
                    helper.getView(R.id.item_lib_title).setVisibility(View.VISIBLE);
                } else if (bean.getType() != myList.get(helper.getLayoutPosition() - 1).getType()) {
                    helper.setText(R.id.item_lib_title, bean.getTitle());
                    helper.getView(R.id.item_lib_title).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.item_lib_title).setVisibility(View.GONE);
                }

                ImageView photo = helper.getView(R.id.item_lib_photo);
                GlideUtils.loadCircleImage(mActivity, bean.getIcon(), photo);
                helper.setText(R.id.item_lib_nick, bean.getName());
                helper.setText(R.id.item_lib_time, bean.getCreated_at());
                helper.setText(R.id.item_lib_authen, bean.getUser_total() + "人加入");

            }
        });

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent();
                intent.putExtra("catalog_id", myList.get(position).getCatalog_id());
                switch (myList.get(position).getType()) {
                    case 0:
                        intent.setClass(mActivity, LibDetailsActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(mActivity, RallyDetailsActivity.class);
                        intent.putExtra("pic", myList.get(position).getIcon());
                        intent.putExtra("its_me", true);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(mActivity, JoinLibActivity.class);
                        intent.putExtra("pic", myList.get(position).getIcon());
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(mActivity, RallyDetailsActivity.class);
                        intent.putExtra("pic", myList.get(position).getIcon());
                        startActivity(intent);
                        break;
                    default:
                }
            }
        });

    }


    @OnClick({R.id.library_disc, R.id.library_create, R.id.library_res})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.library_disc:
                startActivity(new Intent(mActivity, LibDiscActivity.class));
                break;
            case R.id.library_create:
                startActivity(new Intent(mActivity, CreateLibActivity.class));
                break;
            case R.id.library_res:
                startActivity(new Intent(mActivity, RallyActivity.class));
                break;
            default:
        }
    }


    @Override
    public void lazyLoad() {
        super.lazyLoad();
        pullNet();
    }

    public void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<LibHome>("LibHome") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
            }

            @Override
            protected void onSuccess(LibHome libHome) {
                myList.clear();
                if (libHome.getJsonData() != null && libHome.getJsonData().getMy_catalog() != null) {
                    addList(libHome.getJsonData().getMy_catalog(), 0, "我发起的图书馆");
                }

                if (libHome.getJsonData() != null && libHome.getJsonData().getMy_rally() != null) {
                    addList(libHome.getJsonData().getMy_rally(), 1, "我发起的书友会");
                }

                if (libHome.getJsonData() != null && libHome.getJsonData().getJoin_catalog() != null) {
                    addList(libHome.getJsonData().getJoin_catalog(), 2, "我加入的图书馆");
                }
                if (libHome.getJsonData() != null && libHome.getJsonData().getJoin_rally() != null) {
                    addList(libHome.getJsonData().getJoin_rally(), 3, "我加入的书友会");
                }
                fuckYouAdapter.notifyDataSetChanged();
            }
        };
        HashMap<String, String> map = new HashMap<>();
        map = AppSign.buildMap(map);
        Observable<LibHome> user = NetUtils.getGsonRetrofit().libHome(map);
        HttpRxObservable.getObservable(user, this, FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }


    public void addList(List<LibHome.JsonDataBean.MyCatalogBean> list, int type, String title) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setType(type);
            list.get(i).setTitle(title);
        }
        myList.addAll(list);
    }

    /**
     * 刷新图书馆
     *
     * @param refreshEvent
     */
    @Subscribe
    public void refreshLib(LibRefreshEvent refreshEvent) {
        pullNet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
