package dingshi.com.hibook.ui.library;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.lib.LibDisc;
import dingshi.com.hibook.bean.lib.LibHome;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2017/12/25 下午6:14
 */
public class LibDiscActivity extends BaseActivity {

    @BindView(R.id.lib_disc_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.lib_disc_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.lib_disc_label)
    TextView txLabel;
    @BindView(R.id.search_content)
    EditText editSearch;

    FuckYouAdapter fuckYouAdapter;

    List<LibDisc.JsonDataBean> list = new ArrayList<>();

    int page = 1;

    String keyWords = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_lib_disc;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "发现广场", "");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_item_library, list);
        recyclerView.setAdapter(fuckYouAdapter);

        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                LibDisc.JsonDataBean bean = (LibDisc.JsonDataBean) item;
                ImageView photo = helper.getView(R.id.item_lib_photo);
                GlideUtils.loadCircleImage(LibDiscActivity.this, bean.getIcon(), photo);
                helper.setText(R.id.item_lib_nick, bean.getName());
                helper.setText(R.id.item_lib_time, bean.getCreated_at());
                helper.setText(R.id.item_lib_authen, bean.getUser_total() + "人加入");

            }
        });

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(LibDiscActivity.this, JoinLibActivity.class);
                intent.putExtra("catalog_id", list.get(position).getCatalog_id());
                intent.putExtra("is_join", list.get(position).getIs_join());
                intent.putExtra("is_allow", list.get(position).getIs_allow());
                intent.putExtra("pic", list.get(position).getIcon());
                startActivity(intent);
            }
        });


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

        pullNet();
    }


    public static int MSG_SEARCH = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            keyWords = editSearch.getText().toString();
            page = 1;
            pullNet();
        }
    };



    public void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<LibDisc>("libOpenList") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(LibDisc response) {
                if (response.getJsonData() != null) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(response.getJsonData());
                    fuckYouAdapter.notifyDataSetChanged();
                    txLabel.setText("公开图书目录(" + list.size() + "个)");
                }
                smartRefreshLayout.finishLoadmore();
                smartRefreshLayout.finishRefresh();
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("key_word", keyWords);
        map.put("page", String.valueOf(page));
        map = AppSign.buildMap(map);
        Observable<LibDisc> observable = NetUtils.getGsonRetrofit().libOpenList(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
