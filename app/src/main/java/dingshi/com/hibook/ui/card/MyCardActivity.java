package dingshi.com.hibook.ui.card;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IMyCard;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.card.CardList;
import dingshi.com.hibook.present.MyCardPresent;
import dingshi.com.hibook.utils.GlideUtils;

/**
 * @author wangqi
 * @since 2017/12/19 上午11:16
 */


public class MyCardActivity extends BaseActivity implements IMyCard {
    @BindView(R.id.my_card_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.my_card_recycle)
    RecyclerView recyclerView;


    FuckYouAdapter fuckYouAdapter;

    MyCardPresent present = new MyCardPresent(this, this);

    List<CardList.JsonDataBean> list = new ArrayList<>();


    int currentPosition;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_card;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        requestActionBarStyle(true, "我的名片", "编辑");
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.item_my_card, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(fuckYouAdapter);

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyCardActivity.this, CardDetailsActivity.class);
                intent.putExtra("card_id", list.get(position).getId());
                intent.putExtra("uid", user.getJsonData().getUser_id());
                startActivity(intent);
            }
        });

        fuckYouAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                currentPosition = position;
                deleteCard();
                return true;
            }
        });

        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                CardList.JsonDataBean bean = (CardList.JsonDataBean) item;
                ImageView photo = helper.getView(R.id.my_card_photo);
                GlideUtils.loadCircleImage(MyCardActivity.this, bean.getAvatar(), photo);

                helper.setText(R.id.my_card_nick, bean.getName());
                helper.setText(R.id.my_card_company, bean.getCompany() + "\t\t" + bean.getPosition());
                //0.未认证,1.已认证
                helper.setText(R.id.my_card_authen, bean.getIdentify() == 0 ? "未认证" : "已认证");

                if (bean.getIs_main() == 0) {
                    helper.getView(R.id.my_card_main).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.my_card_main).setVisibility(View.VISIBLE);
                }

            }
        });
        smartRefreshLayout.setEnableLoadmore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                present.getCardList();
            }
        });


        present.getCardList();

    }


    @Override
    public void onRightClick() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("长按可以删除名片哦~")
                .create();
        dialog.show();
    }


    @OnClick({R.id.my_card_create})
    public void onClick(View view) {
        startActivity(new Intent(this, CreateCardActivity.class));
    }


    /**
     * 删除卡片
     */
    public void deleteCard() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否删除该名片~")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        present.deleteCard(list.get(currentPosition).getId());
                    }
                }).setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        present.getCardList();
    }

    @Subscribe
    public void updateData(CardList cardList) {
        present.getCardList();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCardList(CardList cardList) {
        if (cardList.getJsonData() != null) {
            list.clear();
            list.addAll(cardList.getJsonData());
            fuckYouAdapter.notifyDataSetChanged();
            smartRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onDeleteCard() {
        fuckYouAdapter.notifyItemRemoved(currentPosition);
        list.remove(currentPosition);
    }

    @Override
    public void onError(String error) {
        showToast(error);
    }
}
