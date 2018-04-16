package dingshi.com.hibook.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IPayMent;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.Coupon;
import dingshi.com.hibook.bean.Payment;
import dingshi.com.hibook.present.PayBookPresent;
import dingshi.com.hibook.share.EasyPayShare;
import dingshi.com.hibook.share.ShareCallBack;
import dingshi.com.hibook.utils.AppManager;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.FuckDialog;

/**
 * @author wangqi
 * @since 2017/12/14 上午11:04
 */


public class PayBookActivity extends BaseActivity implements IPayMent {

    /**
     * 图书信息
     */
    BookDetails.JsonDataBean bookDetails;

    /**
     * 支付方式  1.支付宝,2.微信,3.账户余额
     */
    private int payMethod = 1;

    @BindView(R.id.pay_book_img)
    ImageView imgBook;
    @BindView(R.id.pay_book_book)
    TextView txBookName;
    @BindView(R.id.pay_book_author)
    TextView txBookAuthor;
    @BindView(R.id.pay_book_concern)
    TextView txConcern;
    @BindView(R.id.pay_book_concern_time)
    TextView txConcernTime;
    @BindView(R.id.pay_book_coupon)
    TextView txCoupon;

    @BindView(R.id.pay_book_money_choose)
    ImageView imgMoney;
    @BindView(R.id.pay_book_ali_choose)
    ImageView imgAli;
    @BindView(R.id.pay_book_wx_choose)
    ImageView imgWx;

    PayBookPresent present = new PayBookPresent(this, this);

    HashMap<String, String> map = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "支付", "");
        bookDetails = (BookDetails.JsonDataBean) getIntent().getSerializableExtra("bookDetails");
        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        initBookDetails();

    }

    public void initBookDetails() {
        GlideUtils.load(this, bookDetails.getCover(), imgBook);
        txBookName.setText(bookDetails.getName());
        txBookAuthor.setText("作者: " + bookDetails.getAuthor());
        if (bookDetails != null && bookDetails.getPress() != null) {
            if (bookDetails.getPress().contains("null")||bookDetails.getPress()=="") {
                txConcern.setText("出版社: 暂无");
            } else {
                txConcern.setText("出版社: " + bookDetails.getPress());
            }

        }
        txConcernTime.setText("出版时间: " + bookDetails.getPublish_time());

    }


    @OnClick({R.id.pay_book_submit, R.id.pay_book_ali_layout, R.id.pay_book_wx_layout, R.id.pay_book_money_layout, R.id.pay_book_coupon_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_book_submit:
                //判断是都缴纳押金
                if (SpUtils.getUser().getJsonData().getCert_status() == 1
                        && SpUtils.getUser().getJsonData().getBalance_refund_status() == 0) {
                    if (payMethod == 3) {
                        showMoney();
                    } else {
                        present.submit(map, payMethod);
                    }
                } else {
                    showDialog();
                }
                break;
            case R.id.pay_book_ali_layout:
                reset();
                imgAli.setImageResource(R.drawable.pay_choose_focus);
                payMethod = 1;
                break;
            case R.id.pay_book_wx_layout:
                reset();
                imgWx.setImageResource(R.drawable.pay_choose_focus);
                payMethod = 2;
                break;
            case R.id.pay_book_money_layout:
                reset();
                imgMoney.setImageResource(R.drawable.pay_choose_focus);
                payMethod = 3;
                break;
            case R.id.pay_book_coupon_layout:
                Intent intent = new Intent(this, CouponActivity.class);
                intent.putExtra("payCoupon", true);
                startActivityForResult(intent, CouponActivity.COUPON_REQUEST_CODE);
                break;
            default:
        }
    }


    public void reset() {
        imgMoney.setImageResource(R.drawable.pay_choose_normal);
        imgAli.setImageResource(R.drawable.pay_choose_normal);
        imgWx.setImageResource(R.drawable.pay_choose_normal);
    }


    /**
     * 支付返回的订单号id
     */
    String orderId;

    @Override
    public void onAli(Payment payment) {
        dismissProgressDialog();
        orderId = payment.getJsonData().getOut_trade_no();
        EasyPayShare.getInstance().doPayAli(payment.getJsonData().getSign(), this, new ShareCallBack() {
            @Override
            public void onSuccess(String result) {
                paySuccess();
            }

            @Override
            public void onCanceled(String result) {
                showToast("取消支付");
            }

            @Override
            public void onFailed(String result) {
                showToast("支付失败");
            }
        });
    }

    @Override
    public void onWx(Payment payment) {
        dismissProgressDialog();
        orderId = payment.getJsonData().getOut_trade_no();
        EasyPayShare.getInstance().doPayWx(this, payment.getJsonData(), new ShareCallBack() {
            @Override
            public void onSuccess(String result) {
                paySuccess();
            }

            @Override
            public void onCanceled(String result) {
                showToast("取消支付");
            }

            @Override
            public void onFailed(String result) {
                showToast("支付失败");
            }
        });
    }


    @Override
    public void onMoney(Payment payment) {
        dismissProgressDialog();
        orderId = payment.getJsonData().getOut_trade_no();
        paySuccess();
    }

    FuckDialog fuckDialog;

    public void showDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_pay, null, false);
        fuckDialog = new FuckDialog(this).addView(view).builder();
        fuckDialog.show();
        view.findViewById(R.id.dialog_pay_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayBookActivity.this, PayForegiftActivity.class));
                fuckDialog.dissmis();
            }
        });

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
                        present.submit(map, payMethod);
                    }
                })
                .create();
        dialog.show();
    }


    /**
     * 支付成功后处理
     * <p>
     * 如果有uid的话，说明支付是从书友过来
     * <p>
     * 如果有serial_number，说明支付是从书柜过来
     */
    public void paySuccess() {
        showToast("支付成功");
        //个人借书跳转
        if (map.get("uid") != null) {
            Intent intent = new Intent(this, OrderPersonActivity.class);
            intent.putExtra("book", bookDetails.getName());
            intent.putExtra("isbn", bookDetails.getIsbn());
            intent.putExtra("orderId", orderId);
            intent.putExtra("uid", map.get("uid"));
            startActivity(intent);
            finish();
            //柜子延长时间
        } else if (map.get("serial_number") != null && map.get("recharge_type") == null) {
            Intent intent = new Intent(this, OrderBookActivity.class);
            intent.putExtra("bookDetails", bookDetails);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
            finish();
            //个人延长时间
        } else if (map.get("owner_uid") != null && map.get("person_lengthen") == null) {

            EMMessage message = EMMessage.createTxtSendMessage("您好，我延长了您《" + bookDetails.getName() + "》这本书的时间" + map.get("borrow_day") + "天", map.get("owner_uid"));//526692416898469894
            message.setAttribute("nick", user.getJsonData().getNick_name());
            message.setAttribute("avatar", user.getJsonData().getAvatar());
            message.setAttribute("userid", user.getJsonData().getUser_id());
            EMClient.getInstance().chatManager().sendMessage(message);

        } else {
            AppManager.getInstance().finishOthersActivity(MainActivity.class);
            startActivity(new Intent(PayBookActivity.this, MyBorrowActivity.class));
            finish();
        }
    }


    @Override
    public void onCoupon() {


    }

    @Override
    public void onError(String str) {
        showToast(str);
        dismissProgressDialog();
    }

    @Override
    public void start() {
        showProgressDialog("正在支付中", true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == CouponActivity.COUPON_REQUEST_CODE) {
            Coupon.JsonDataBean bean = (Coupon.JsonDataBean) data.getSerializableExtra("bean");
            txCoupon.setText(bean.getName());
        }

    }
}
