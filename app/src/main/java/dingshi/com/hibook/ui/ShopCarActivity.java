package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yuyh.easyadapter.helper.ViewHelper;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.share.EasyWxEntryActivity;

public class ShopCarActivity extends BaseActivity {
    @BindView(R.id.shop_car_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.shop_car_price)
    TextView totalePrice;
    @BindView(R.id.shop_car_gopay)
    TextView goPay;
    @BindView(R.id.shop_car_allcheckbox)
    CheckBox allCheckBox;
    private double totalePriceText = 0;
    private boolean isAllChecked = false;
    private FuckYouAdapter allFuckYouAdapter;
    private  boolean isfirst;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shopcar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "购物车");

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        allFuckYouAdapter = new FuckYouAdapter(R.layout.view_shop_car_item, Arrays.asList("", ""));
        recyclerView.setAdapter(allFuckYouAdapter);

        allCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                allFuckYouAdapter. notifyDataSetChanged();
            }
        });
        allFuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                RecyclerView recyclerView = helper.getView(R.id.shop_car_item_recycleview);
                final CheckBox itemCheckBox = helper.getView(R.id.shop_car_item_checkbox);
                recyclerView.setLayoutManager(new LinearLayoutManager(ShopCarActivity.this, LinearLayoutManager.VERTICAL, false));
                final FuckYouAdapter itemadapter = new FuckYouAdapter(R.layout.view_shop_car_item_item, Arrays.asList("", ""));
                recyclerView.setAdapter(itemadapter);
                //判断全选是否选中 选中 店铺的checkbox为选中  反之
                if (allCheckBox.isChecked()) {
                    itemCheckBox.setChecked(true);
                } else {
                    itemCheckBox.setChecked(false);
                }
                //店铺的checkbox改变状态  需要刷新适配器
                itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        itemadapter.notifyDataSetChanged();
                    }
                });


                itemadapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
                    @Override
                    public void convertView(BaseViewHolder helper, Object item) {
                        CheckBox subCheckBox = helper.getView(R.id.shop_car_sub_check);


                        //判断店铺的checkbox是否选中 选中 书的checkbox为选中  反之
                            if (itemCheckBox.isChecked()){
                                subCheckBox.setChecked(true);
                            }else {
                                subCheckBox.setChecked(false);
                            }


                        //判断店铺的checkbox是否选中 选中 书的checkbox为选中  反之
//                        if (itemCheckBox.isChecked()){
//                            subCheckBox.setChecked(true);
//                        }else {
//                            subCheckBox.setChecked(false);
//                        }


//                        if (allCheckBox.isChecked()&&itemCheckBox.isChecked()){
//
//                            if (!subCheckBox.isChecked()){
//                                totalePriceText += 1;
//                            }
//                            totalePrice.setText("" + totalePriceText);
//                        }
//                        if (!allCheckBox.isChecked()&&!itemCheckBox.isChecked()){
//                            totalePriceText =0;
//                            totalePrice.setText("" + totalePriceText);
//                        }
                        subCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b) {
                                    totalePriceText += 1;
                                } else {
                                    totalePriceText -= 1;
                                }
                                totalePrice.setText("" + totalePriceText);
                            }
                        });
                    }
                });


            }
        });
    }


    @OnClick({R.id.shop_car_gopay, R.id.shop_car_allcheckboxtext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop_car_gopay:
                Intent intent = new Intent(getApplicationContext(), ConfirmOrderActivity.class);
                startActivity(intent);
                break;
           //case R.id.shop_car_allcheckbox:
//                if (allCheckBox.isChecked()){
//                    allCheckBox.setChecked(false);
//                    allFuckYouAdapter.notifyDataSetChanged();
//                    //allCheckBox.setImageResource(R.drawable.pay_choose_normal);
//                }else {
//                    allCheckBox.setChecked(true);
//                    allFuckYouAdapter.notifyDataSetChanged();
//                    //allCheckBox.setImageResource(R.drawable.pay_choose_focus);
//                }
               // allFuckYouAdapter.notifyDataSetChanged();
             //   break;
            case R.id.shop_car_allcheckboxtext:
                if (allCheckBox.isChecked()) {
                    allCheckBox.setChecked(false);
                    //allCheckBox.setImageResource(R.drawable.pay_choose_normal);
                } else {
                    allCheckBox.setChecked(true);
                    //allCheckBox.setImageResource(R.drawable.pay_choose_focus);
                }
                allFuckYouAdapter.notifyDataSetChanged();
                break;
        }

    }
//                final CheckBox   shopCheck=helper.getView(R.id.shop_car_item_checkbox1) ;
//                final CheckBox  bookCheck=helper.getView(R.id.shop_car_item_checkbox2);
//                if (isAllChecked){
//                    shopCheck.setChecked(true);
//                    bookCheck.setChecked(true);
//                }else {
//                    shopCheck.setChecked(false);
//                    bookCheck.setChecked(false);
//                }
//
//                shopCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                        if (b){bookCheck.setChecked(true);
//                            totalePriceText+=1.00;}else {bookCheck.setChecked(false);
//                            totalePriceText-=1.00;}
//                    }
//                });
//                bookCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                        if (b){shopCheck.setChecked(true);}else {shopCheck.setChecked(false);}
//                    }
//                });


}
