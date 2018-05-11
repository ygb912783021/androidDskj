package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Arrays;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;

public class ShopCarActivity extends BaseActivity {
    @BindView(R.id.shop_car_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.shop_car_price)
    TextView totalePrice;
    @BindView(R.id.shop_car_gopay)
    TextView goPay;
    private double totalePriceText=0;
    private boolean isAllChecked=false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shopcar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "购物车");

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        final FuckYouAdapter fuckYouAdapter=new FuckYouAdapter(R.layout.view_shop_car_item, Arrays.asList("", ""));
        recyclerView.setAdapter(fuckYouAdapter);
        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
//                final CheckBox   shopCheck=helper.getView(R.id.shop_car_item_checkbox1) ;
////                final CheckBox  bookCheck=helper.getView(R.id.shop_car_item_checkbox2);
////                if (isAllChecked){
////                    shopCheck.setChecked(true);
////                    bookCheck.setChecked(true);
////                }else {
////                    shopCheck.setChecked(false);
////                    bookCheck.setChecked(false);
////                }
////
////                shopCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////                    @Override
////                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                        if (b){bookCheck.setChecked(true);
////                            totalePriceText+=1.00;}else {bookCheck.setChecked(false);
////                            totalePriceText-=1.00;}
////                    }
////                });
////                bookCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////                    @Override
////                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                        if (b){shopCheck.setChecked(true);}else {shopCheck.setChecked(false);}
////                    }
////                });

                RecyclerView  recyclerView = helper.getView(R.id.shop_car_item_recycleview);
                FuckYouAdapter itemadapter=new FuckYouAdapter(R.layout.view_shop_car_item_item, Arrays.asList("",""));
                recyclerView.setLayoutManager(new LinearLayoutManager(ShopCarActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(itemadapter);
                itemadapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
                    @Override
                    public void convertView(BaseViewHolder helper, Object item) {

                    }
                });

            }
        });

        goPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),ConfirmOrderActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });
    }


}
