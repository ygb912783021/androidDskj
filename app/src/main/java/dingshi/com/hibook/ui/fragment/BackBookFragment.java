package dingshi.com.hibook.ui.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.BorrowRefresh;
import dingshi.com.hibook.bean.Borrows;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.BackBookActivity;
import dingshi.com.hibook.ui.BackPersonActivity;
import dingshi.com.hibook.ui.BookDetailsActivity;
import dingshi.com.hibook.ui.BorrowBookActivity;
import dingshi.com.hibook.ui.LengthenActivity;
import dingshi.com.hibook.ui.OrderBookActivity;
import dingshi.com.hibook.ui.OrderPersonActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.PixelUtils;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.DividVerticalDecoration;
import dingshi.com.hibook.view.LoadingLayout;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Part;

/**
 * @author wangqi
 */
public class BackBookFragment extends BaseFragment {

    public static final int REMOVE_REQUEST = 0x99;


    @BindView(R.id.back_book_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.back_book_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.back_book_recycle)
    RecyclerView mRecycleview;
    FuckYouAdapter fuckYouAdapter;

    /**
     * 0.未还，1.已还
     */
    private int returned;
    private int page = 1;

    /**
     * 当前点击的position
     */
    public int currentPosition;

    List<Borrows.JsonDataBean> list = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.fragment_back_book;
    }

    @Override
    public void initView() {
        returned = getArguments().getInt("returned", 0);

        mRecycleview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_back_book_item, list);
        mRecycleview.setAdapter(fuckYouAdapter);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                pullNet();
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                pullNet();
            }
        });


        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                startActivity(new Intent(mActivity, BorrowBookActivity.class));

//                Intent intent = new Intent(mActivity, BackPersonActivity.class);//
//                intent.putExtra("receive", false);//标志当前是取书状态
//                intent.putExtra("orderId", list.get(position).getOut_trade_no());
//                intent.putExtra("isbn", list.get(position).getIsbn());
//                intent.putExtra("uid", list.get(position).getOwner_user_id());
//                startActivity(intent);


            }
        });


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(final BaseViewHolder helper, Object item) {

                final Borrows.JsonDataBean bean = (Borrows.JsonDataBean) item;

                if (returned == 0) {
                    //为0的时候表示未取出   1表示已取出
                    if (bean.getPickup() == 0) {
                        helper.setTextColor(R.id.back_book_time, getResources().getColor(R.color.black_3f));
                        helper.setTextColor(R.id.back_book_price, getResources().getColor(R.color.black_3f));
                        helper.setTextColor(R.id.back_book_tip, getResources().getColor(R.color.red));
                        helper.getView(R.id.back_book_lengthen).setVisibility(View.GONE);
                        //1、个人  0、书柜
                        if (bean.getIs_personal() == 1) {
                            //0、不同意  1、同意  不同意需要隐藏联系框
                            if (bean.getTrading_state() == 0) {
                                helper.setText(R.id.back_book_tip, "藏书人没有同意您的借书请求,在去看看别的吧");
                                helper.getView(R.id.back_book_book).setVisibility(View.GONE);
                            } else {
                                helper.setText(R.id.back_book_tip, "等待联系个人取书");
                                helper.setText(R.id.back_book_book, "联系取书");
                                helper.getView(R.id.back_book_book).setVisibility(View.VISIBLE);
                            }

                        } else {
                            helper.setText(R.id.back_book_tip, "正在等待前去书柜取书");
                            helper.setText(R.id.back_book_book, "立即取书");
                            helper.getView(R.id.back_book_book).setVisibility(View.VISIBLE);
                        }

                    } else {


                        if (bean.getBorrow_surplus_day() >= 0) {
                            helper.setTextColor(R.id.back_book_time, getResources().getColor(R.color.black_3f));
                            helper.setTextColor(R.id.back_book_price, getResources().getColor(R.color.black_3f));
                            helper.setTextColor(R.id.back_book_tip, getResources().getColor(R.color.blue_32));
                            helper.setText(R.id.back_book_tip, "还有" + bean.getBorrow_surplus_day() + "天到期");
                        } else {
                            helper.setTextColor(R.id.back_book_time, getResources().getColor(R.color.red));
                            helper.setTextColor(R.id.back_book_price, getResources().getColor(R.color.red));
                            helper.setTextColor(R.id.back_book_tip, getResources().getColor(R.color.red));
                            helper.setText(R.id.back_book_tip, "已逾期" + Math.abs(bean.getBorrow_surplus_day()) + "天，请尽快还书");
                        }
                        helper.setText(R.id.back_book_book, "立即还书");
                        helper.getView(R.id.back_book_book).setVisibility(View.VISIBLE);
                        helper.getView(R.id.back_book_lengthen).setVisibility(View.VISIBLE);
                    }
                } else if (returned == 1) {
                    helper.setTextColor(R.id.back_book_time, getResources().getColor(R.color.black_3f));
                    helper.setTextColor(R.id.back_book_price, getResources().getColor(R.color.black_3f));
                    helper.setTextColor(R.id.back_book_tip, getResources().getColor(R.color.blue_32));
                    helper.setText(R.id.back_book_tip, "图书借阅时间为" + bean.getBorrow_interval() + "\n图书已于" + bean.getReturned_at() + "归还");
                    helper.getView(R.id.back_book_lengthen).setVisibility(View.GONE);
                    helper.setText(R.id.back_book_book, "再次借书");
                }


                helper.setText(R.id.back_book_time, "借阅时间：" + bean.getBorrow_day() + "天");
                helper.setText(R.id.back_book_price, "借阅费用：" + bean.getPay_fee() + "元");
                ImageView imgPhoto = helper.getView(R.id.back_book_img);
                GlideUtils.load(mActivity, bean.getCover(), imgPhoto);
                helper.setText(R.id.back_book_name, bean.getName());

                /*
                  Is_personal 0.个人,1.书柜
                 */
                helper.getView(R.id.back_book_book).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition = helper.getLayoutPosition();
                        //未还
                        if (returned == 0) {
                            //未借出
                            if (bean.getPickup() == 0) {
                                if (bean.getIs_personal() == 1) {//OrderPersonActivity

                                    Intent intent = new Intent(mActivity, BackPersonActivity.class);//
                                    intent.putExtra("receive", false);//标志当前是取书状态
                                    intent.putExtra("orderId", bean.getOut_trade_no());
                                    intent.putExtra("isbn", bean.getIsbn());
                                    intent.putExtra("uid", bean.getOwner_user_id());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(mActivity, OrderBookActivity.class);
                                    intent.putExtra("orderId", bean.getOut_trade_no());
                                    startActivity(intent);
                                }
                                //借出
                            } else {
                                if (bean.getIs_personal() == 1) {

                                    Intent intent = new Intent(mActivity, BackPersonActivity.class);
                                    intent.putExtra("receive", true);//标志当前是还书状态
                                    intent.putExtra("orderId", bean.getOut_trade_no());
                                    intent.putExtra("isbn", bean.getIsbn());
                                    intent.putExtra("uid", bean.getOwner_user_id());
                                    startActivity(intent);
                                } else if (bean.getIs_personal() == 0) {
                                    Intent intent = new Intent(mActivity, BackBookActivity.class);
                                    intent.putExtra("isbn", bean.getIsbn());
                                    intent.putExtra("out_trade_no", bean.getOut_trade_no());
                                    startActivity(intent);
                                }
                            }

                            //已还
                        } else if (returned == 1) {
                            Intent intent = new Intent(mActivity, BookDetailsActivity.class);
                            intent.putExtra("isbn", bean.getIsbn());
                            startActivity(intent);
                        }


                    }
                });

                helper.getView(R.id.back_book_lengthen).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HashMap<String, String> map = new HashMap<String, String>(2);
                        map.put("isbn", bean.getIsbn());
                        map.put("out_trade_no", bean.getOut_trade_no());
                        map.put("order_type", "1");
                        map.put("recharge_type", "2");
                        //此参数是为了区分当前是延长时间,支付成功后需要向拥有者发送延长消息提醒
                        map.put("owner_uid", bean.getOwner_user_id());
                        Intent intent = new Intent(mActivity, LengthenActivity.class);
                        //个人
                        if (bean.getIs_personal() == 1) {
                            //此参数是为了区分当前是延长时间
                            map.put("person_lengthen", "true");

                            //书柜
                        } else if (bean.getIs_personal() == 0) {
                            map.put("serial_number", bean.getSerial_number());
                        }
                        intent.putExtra("map", map);
                        startActivity(intent);
                    }
                });

            }
        });
    }


    @Override
    public void lazyLoad() {
        super.lazyLoad();
        EventBus.getDefault().register(this);
        pullNet();
    }


    public void pullNet() {

        HttpRxObserver httpRxObserver = new HttpRxObserver<Borrows>("borrows") {
            @Override
            protected void onStart(Disposable d) {
                loadingLayout.showLoadingView();
            }

            @Override
            protected void onError(ApiException e) {
                showToast(e.getMsg());
                loadingLayout.showErrorView();
            }

            @Override
            protected void onSuccess(Borrows borrows) {
                if (borrows.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(borrows.getJsonData());
                    fuckYouAdapter.notifyDataSetChanged();
                    loadingLayout.showContentView();
                } else {
                    loadingLayout.showEmptyView();
                }
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
            }
        };


        HashMap<String, String> map = new HashMap<>();
        map.put("returned", String.valueOf(returned));
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);

        Observable<Borrows> user = NetUtils.getGsonRetrofit().
                borrows(map);

        HttpRxObservable.getObservable(user, this, FragmentEvent.PAUSE).subscribe(httpRxObserver);

    }


    @Subscribe
    public void onRefresh(BorrowRefresh borrowRefresh) {
        page = 1;
        pullNet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
