package dingshi.com.hibook.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.eventbus.EventBusHelper;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.BookDetailsActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.LoadingLayout;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/11/14 下午12:01
 */


public class BookHouseFragment extends BaseFragment {
    @BindView(R.id.book_house_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.book_house_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.book_house_recycle)
    RecyclerView recyclerView;
//    @BindView(R.id.tv_book_share_num)
//    TextView tv_book_share_num;
//    @BindView(R.id.tv_book_house_edit)
//    TextView tv_book_house_edit;

    FuckYouAdapter fuckYouAdapter;

    List<BookDetails.JsonDataBean> list = new ArrayList<>();

    int page = 1;
    int pos=0;

    String uid;
    ImageView imageView;
    ImageView bookPhoto;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_book_house;
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
        }
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_item_book_house, list);

        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerView.setAdapter(fuckYouAdapter);
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view,  int position) {
                super.onItemChildClick(adapter, view, position);
                 pos=position;
                int itemViewId = view.getId();
                imageView=view.findViewById(R.id.iv_item_delete);
                if (itemViewId==R.id.iv_item_delete){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("确认删除？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    BookDetails.JsonDataBean bean = list.get(pos);
                                    String isbn=bean.getIsbn();
                                    bookDelete(isbn);
//                                    Toast.makeText(getActivity(),""+pos+"×",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            })
                            .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(getActivity(),"取消了"+pos+"×",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            })
                            .create()
                            .show();

                }
            }
        });
        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, final Object item) {
                helper.addOnClickListener(R.id.iv_item_delete);
                if (Constant.isEditDelete){
                    helper.getView(R.id.iv_item_delete).setVisibility(View.VISIBLE);
                }else {
                    helper.getView(R.id.iv_item_delete).setVisibility(View.GONE);
                }
                BookDetails.JsonDataBean bean = (BookDetails.JsonDataBean) item;
                bookPhoto = helper.getView(R.id.item_book_house_avatar);
                GlideUtils.load(mActivity, bean.getCover(), bookPhoto);
                helper.setText(R.id.item_book_house_name, bean.getName());
                //1、可借  0、不可借
                if (bean.getAvailable() == 1) {
                    helper.getView(R.id.item_book_house_share).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.item_book_house_share).setVisibility(View.GONE);
                }

            }
        });
        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                BookDetails.JsonDataBean bean = list.get(position);

                Intent intent = new Intent(mActivity, BookDetailsActivity.class);
                intent.putExtra("isbn", bean.getIsbn());
                intent.putExtra("uid", uid);

                //如果当前uid是自己的话，才会跳共享界面，不是的话，跳详情
                if (uid.equals(user.getJsonData().getUser_id())) {
                    if (bean.getIs_borrow() == 1) {
                        //已被借出
                        intent.putExtra("type", 1);
                    } else {
                        if (bean.getAvailable() == 1) {
                            //已共享
                            intent.putExtra("type", 2);
                        } else {
                            //未共享
                            intent.putExtra("type", 3);
                        }
                    }
                }
                startActivity(intent);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                getInfo();
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                getInfo();
            }
        });

    }


    public void updateData(String toUserId) {
        uid = toUserId;
        page = 1;
        getInfo();
    }


    @Override
    public void lazyLoad() {
        super.lazyLoad();
        getInfo();
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unRegister(this);
    }

    @Subscribe
    public void refresh(BookList bookList) {
        page = 1;
        getInfo();
    }

    public void getInfo() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookList>("personalBook") {
            @Override
            protected void onStart(Disposable d) {
                loadingLayout.showLoadingView();
            }

            @Override
            protected void onError(ApiException e) {
                loadingLayout.showErrorView();
            }

            @Override
            protected void onSuccess(BookList bookList) {
                if (bookList.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(bookList.getJsonData());
                    fuckYouAdapter.notifyDataSetChanged();
                    loadingLayout.showContentView();
                } else {
                    loadingLayout.showEmptyView();
                }
                //通知BookHouseActivity更新
                EventBusHelper.post(list);


                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
            }
        };

        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", uid);
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);
        Observable<BookList> user = NetUtils.getGsonRetrofit().personalBook(map);
        HttpRxObservable.getObservable(user, this, FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }
    /**
     * 删除图书
     * @param isbn
     */
    public void bookDelete(String isbn) {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("bookrack/delete") {
            @Override
            protected void onStart(Disposable d) {
                showProgressDialog("正在删除中...",true);
            }

            @Override
            protected void onError(ApiException e) {
                Log.e("删除图书异常：",e.toString());
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(Result bookList) {
                Log.i("删除图书成功：",bookList.toString());
                page = 1;
                dismissProgressDialog();
                getInfo();


            }
        };

        HashMap<String, String> map = new HashMap<>(2);
        map.put("isbn", isbn);
        map = AppSign.buildMap(map);
        Observable<Result> resultObservable = NetUtils.getGsonRetrofit().bookDelete(map);
        HttpRxObservable.getObservable(resultObservable, this, FragmentEvent.PAUSE).subscribe(httpRxObserver);
    }

}
