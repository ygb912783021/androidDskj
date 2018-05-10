package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import org.w3c.dom.Text;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IPayMent;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Payment;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.FuckDialog;

public class ConfirmOrderActivity extends BaseActivity implements IPayMent {
    @BindView(R.id.confirm_order_recycle)
    RecyclerView allRecyclerView;
    @BindView(R.id.confirm_order_default_name)
    TextView name;
    @BindView(R.id.confirm_order_default_add)
    TextView address;
    @BindView(R.id.confirm_order_pay_coupon)
    TextView coupon;
    @BindView(R.id.confirm_order_money_balance)
    ImageView payBalance;
    @BindView(R.id.confirm_order_money_wx)
    ImageView payWX;
    @BindView(R.id.confirm_order_money_ali)
    ImageView payAli;
    @BindView(R.id.comfirm_order_gopay)
    TextView gopay;

    /**
     * 支付方式  1.支付宝,2.微信,3.账户余额
     */
    private int payMethod = 1;

    private FuckYouAdapter allAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_order;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true,"确认订单");

        allRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        allAdapter=new FuckYouAdapter(R.layout.view_shop_car_list, Arrays.asList("","",""));
        allRecyclerView.setAdapter(allAdapter);
        allAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                RecyclerView itemRecyclerView=helper.getView(R.id.shop_car_list_recycleview);
                itemRecyclerView.setLayoutManager(new LinearLayoutManager(ConfirmOrderActivity.this, LinearLayoutManager.VERTICAL, false));
                FuckYouAdapter itemadapter=new FuckYouAdapter(R.layout.view_shop_car_list_item, Arrays.asList("",""));
                itemRecyclerView.setAdapter(itemadapter);
                itemadapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
                    @Override
                    public void convertView(BaseViewHolder helper, Object item) {

                    }
                });
            }

        });
                RecyclerView itemRecyclerView=helper.getView(R.id.shop_car_list_recycleview);
                itemRecyclerView.setLayoutManager(new LinearLayoutManager(ConfirmOrderActivity.this, LinearLayoutManager.VERTICAL, false));
                FuckYouAdapter itemadapter=new FuckYouAdapter(R.layout.view_shop_car_list_item, Arrays.asList("",""));
                itemRecyclerView.setAdapter(itemadapter);
                itemadapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
                    @Override
                    public void convertView(BaseViewHolder helper, Object item) {

                    }
                });
            }

        });
    }

    @OnClick({R.id.tiaozhuan,R.id.pay_confirm_coupon_layout, R.id.pay_confirm_money_layout,R.id.pay_confirm_wx_layout,
            R.id.comfirm_order_gopay,R.id.pay_confirm_ali_layout})
    public  void onClick(View view){
        Intent intent=new Intent();
        switch (view.getId()){
            case R.id.tiaozhuan:
                intent.setClass(this,ReceiviaddressActivity.class);
                startActivity(intent);
                break;
            case R.id.pay_confirm_coupon_layout:
                 intent.setClass(this, CouponActivity.class);
                intent.putExtra("payCoupon", true);
                startActivityForResult(intent, CouponActivity.COUPON_REQUEST_CODE);
                break;
            case R.id.pay_confirm_money_layout:
                reset();
                payBalance.setImageResource(R.drawable.pay_choose_focus);
                payMethod = 3;
                break;
            case R.id.pay_confirm_wx_layout:
                reset();
                payWX.setImageResource(R.drawable.pay_choose_focus);
                payMethod = 2;
                break;
            case R.id.pay_confirm_ali_layout:
                reset();
                payAli.setImageResource(R.drawable.pay_choose_focus);
                payMethod = 1;
                break;
            case R.id.comfirm_order_gopay:
                //判断是否缴纳押金
//                if (SpUtils.getUser().getJsonData().getCert_status() == 1
//                        && SpUtils.getUser().getJsonData().getBalance_refund_status() == 0) {
                    if (payMethod == 3) {
                        showMoney();
                    } else {
                    //    present.submit(map, payMethod);
                    }
//                } else {
//                    showDialog();
//                }
                break;



        }


    }

    public void reset() {
        payBalance.setImageResource(R.drawable.pay_choose_normal);
        payAli.setImageResource(R.drawable.pay_choose_normal);
        payWX.setImageResource(R.drawable.pay_choose_normal);
    }

    AlertDialog dialog;

    public void showMoney() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("确认")
                .setMessage("确认使用余额支付")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // present.submit(map, payMethod);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onAli(Payment orderInfo) {

    }

    @Override
    public void onWx(Payment orderInfo) {

    }

    @Override
    public void onMoney(Payment payment) {

    }

    @Override
    public void onCoupon() {

    }

    @Override
    public void onError(String str) {

    }

    @Override
    public void start() {

    }

    @OnClick({R.id.tiaozhuan})
    public  void onClick(View view){
        Intent intent=new Intent(this,ReceiviaddressActivity.class);
        startActivity(intent);
    }


}
