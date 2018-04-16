package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.search_cancel)
    TextView txSearch;
    @BindView(R.id.search_content)
    EditText editContent;

    @BindView(R.id.search_recycle)
    RecyclerView recyclerView;

    FuckYouAdapter fuckYouAdapter;

    List<String> hotList = new ArrayList<>();
    List<String> histotyList = new ArrayList<>();

    View headerView;

    String keyWords;

    boolean isCancel = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        initData();
        hideTitleBar();
        initEditListener();
        initRecycleView();
    }

    private void initData() {
        hotList.add("石黑一雄");
        hotList.add("马伯庸");
        hotList.add("木心");
        hotList.add("三体");
        hotList.add("余华");
        hotList.add("阿加莎");
        hotList.add("村上春树");
        hotList.add("王小波");

        histotyList.addAll(SpUtils.getSearch());

    }

    private void initRecycleView() {
        initRecycleViewHeader();
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.item_search, histotyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(fuckYouAdapter);
        fuckYouAdapter.addHeaderView(headerView);
        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(final BaseViewHolder helper, Object item) {
                helper.setText(R.id.item_search_text, (String) item);
                helper.getView(R.id.item_search_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteHistory(helper.getLayoutPosition() - 1);
                    }
                });
            }
        });

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                jumpActivity(histotyList.get(position));
            }
        });
    }

    private void deleteHistory(int layoutPosition) {
        histotyList.remove(layoutPosition);
        fuckYouAdapter.notifyDataSetChanged();
        SpUtils.putAllSearch(histotyList);
    }


    ImageView deleteImg;

    private void initRecycleViewHeader() {
        headerView = LayoutInflater.from(this).inflate(R.layout.search_header, null, false);
        headerView.setFocusable(false);
        deleteImg = headerView.findViewById(R.id.search_header_delete);
        RecyclerView headerRecycle = headerView.findViewById(R.id.search_header_recycle);
        FuckYouAdapter adapter = new FuckYouAdapter<>(R.layout.item_search_header, hotList);
        headerRecycle.setLayoutManager(new GridLayoutManager(this, 5));
        headerRecycle.setAdapter(adapter);
        adapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                helper.setText(R.id.item_search_header_text, (String) item);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                jumpActivity(hotList.get(position));
            }
        });

        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                histotyList.clear();
                SpUtils.putAllSearch(histotyList);
                fuckYouAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initEditListener() {
        editContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    keyWords = editContent.getText().toString();
                    jumpActivity(keyWords);
                }
                return false;
            }
        });

        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    txSearch.setText("确定");
                    isCancel = false;
                } else {
                    txSearch.setText("取消");
                    isCancel = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.search_cancel, R.id.search_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancel:
                if (isCancel) {
                    finish();
                } else {
                    keyWords = editContent.getText().toString().trim();
                    jumpActivity(keyWords);
                }
                break;
            case R.id.search_delete:
                editContent.setText("");
                break;
            default:
        }
    }

    private void jumpActivity(String keyWords) {
        if ("".equals(keyWords)) {
            showToast("请输入关键字");
            return;
        }
        histotyList.add(keyWords);
        fuckYouAdapter.notifyDataSetChanged();
        SpUtils.putSearch(keyWords);

        HashMap<String, String> map = new HashMap<>();
        map.put("key_word", keyWords);
        Intent intent = new Intent(this, BookListActivity.class);
        intent.putExtra("map", map);
        startActivity(intent);
    }


}
