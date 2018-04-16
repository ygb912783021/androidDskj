package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BookPerson;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.QuantityView;

/**
 * @author wangqi
 * 延长时间
 */
public class BorrowBookActivity extends BaseActivity {
    /**
     * 书柜信息
     */
    List<BookCase.JsonDataBean> bookCaseList;
    /**
     * 共享书人信息
     */
    List<BookPerson.JsonDataBean> bookPersonList;
    /**
     * 图书信息
     */
    BookDetails.JsonDataBean bookDetails;

    @BindView(R.id.borrow_book_img)
    ImageView imgBook;
    @BindView(R.id.borrow_book_book)
    TextView txBookName;
    @BindView(R.id.borrow_book_author)
    TextView txBookAuthor;
    @BindView(R.id.borrow_book_concern)
    TextView txConcern;
    @BindView(R.id.borrow_book_concern_time)
    TextView txConcernTime;
    @BindView(R.id.borrow_book_quantity)
    QuantityView quantityView;
    @BindView(R.id.borrow_book_case)
    RecyclerView caseRecycle;
    @BindView(R.id.borrow_book_person)
    RecyclerView personRecycle;
    @BindView(R.id.borrow_book_pay)
    TextView txPayment;
    @BindView(R.id.borrow_book_case_label)
    TextView txTip;
    @BindView(R.id.borrow_book_price)
    TextView txPrice;
    FuckYouAdapter caseAdapter;

    @BindView(R.id.borrow_book_letter)
    EditText editLetter;
    @BindView(R.id.borrow_book_letter_layout)
    LinearLayout letterLayout;

    FuckYouAdapter personAdapter;

    int currentChoose = 0;
    /**
     * 判断是否是从个人所借
     */
    boolean isPersonBorrow;


    @Override
    public int getLayoutId() {
        return R.layout.activity_borrow_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "马上借书", "");
        bookDetails = (BookDetails.JsonDataBean) getIntent().getSerializableExtra("bookDetails");
        initBookDetails();

        bookCaseList = (List<BookCase.JsonDataBean>) getIntent().getSerializableExtra("bookCase");
        if (bookCaseList == null) {
            isPersonBorrow = true;
            bookPersonList = (List<BookPerson.JsonDataBean>) getIntent().getSerializableExtra("bookPerson");
            initBookPerson();
            txTip.setText("选择书友借书：");
            letterLayout.setVisibility(View.VISIBLE);
        } else {
            txTip.setText("选择书柜取书：");
            isPersonBorrow = false;
            initBookCase();
            letterLayout.setVisibility(View.GONE);
        }


    }

    public void initBookDetails() {
        GlideUtils.load(this, bookDetails.getCover(), imgBook);

        txBookName.setText(bookDetails.getName());
        txBookAuthor.setText("作者: " + bookDetails.getAuthor());
        if(bookDetails!=null&&bookDetails.getPress()!=null){
            if (bookDetails.getPress().contains("null")||bookDetails.getPress()==""){
                txConcern.setText("出版社:暂无");
            }else{
                txConcern.setText("出版社: " + bookDetails.getPress());
            }
        }
        txConcernTime.setText("出版时间: " + bookDetails.getPublish_time());


        quantityView.setOnBackDay(new QuantityView.OnBackValue() {
            @Override
            public void day(int day) {
                if (day <= 10) {
                    txPrice.setText("合计: 1元");
                } else {
                    txPrice.setText("合计: " + ((day - 10) * 0.5 + 1) + "元");
                }
            }
        });
    }

    /**
     * 初始化书柜列表
     */
    public void initBookCase() {
        caseAdapter = new FuckYouAdapter<>(R.layout.view_book_details_case_item, bookCaseList);
        caseRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        caseRecycle.setAdapter(caseAdapter);
        caseAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                BookCase.JsonDataBean bean = (BookCase.JsonDataBean) item;
                helper.setText(R.id.item_book_details_case_address, bean.getAddress());
                helper.setText(R.id.item_book_details_case_distance, bean.getRadius() + "km");
                ImageView choose = helper.getView(R.id.item_book_details_case_choose);

                if (helper.getLayoutPosition() == currentChoose) {
                    choose.setVisibility(View.VISIBLE);
                } else {
                    choose.setVisibility(View.GONE);
                }
            }
        });
        caseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentChoose = position;
                caseAdapter.notifyDataSetChanged();
            }
        });

        if (bookCaseList.size() == 0) {
            letterLayout.setVisibility(View.GONE);
            txPayment.setClickable(false);
            txPayment.setBackgroundResource(R.drawable.shap_gray_7);
            Toast.makeText(this,"未找到书柜", Toast.LENGTH_SHORT).show();
        } else {
            txPayment.setClickable(true);
            txPayment.setBackgroundResource(R.drawable.shap_black_7);
        }

    }


    public void initBookPerson() {
        personAdapter = new FuckYouAdapter<>(R.layout.view_book_details_person_item, bookPersonList);
        personRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        personRecycle.setAdapter(personAdapter);
        personAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                BookPerson.JsonDataBean bean = (BookPerson.JsonDataBean) item;
                ImageView photo = helper.getView(R.id.book_details_person_photo);
                GlideUtils.loadCircleImage(BorrowBookActivity.this, bean.getAvatar(), photo);
                helper.setText(R.id.book_details_person_name, bean.getNick_name());
                helper.setText(R.id.book_details_person_content, "读书" + bean.getRead_num() + "|藏书" + bean.getHave_num() + "本");

                ImageView choose = helper.getView(R.id.item_book_details_person_choose);

                if (helper.getLayoutPosition() == currentChoose) {
                    choose.setVisibility(View.VISIBLE);
                } else {
                    choose.setVisibility(View.GONE);
                }
            }
        });

        personAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentChoose = position;
                personAdapter.notifyDataSetChanged();
            }
        });

        if (bookPersonList.size() == 0) {
            letterLayout.setVisibility(View.GONE);
            Toast.makeText(this,"未找到书友", Toast.LENGTH_SHORT).show();
            txPayment.setClickable(false);
            txPayment.setBackgroundResource(R.drawable.shap_gray_7);
        } else {
            txPayment.setClickable(true);
            txPayment.setBackgroundResource(R.drawable.shap_black_7);
        }

    }


    @OnClick({R.id.borrow_book_pay, R.id.borrow_book_rule})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.borrow_book_pay:
                jump();
                break;
            case R.id.borrow_book_rule:
                startActivity(new Intent(this, RuleActivity.class));
                break;
            default:
        }
    }

    /**
     * 跳转需要的参数
     * 1、图书的信息
     * 2、借阅的天数
     * 3、订单类型  1.借书、2.充值、3.延期
     * 4、书柜的编号
     */
    public void jump() {
        HashMap<String, String> map = new HashMap<>();
        map.put("borrow_day", String.valueOf(quantityView.getValue()));
        map.put("isbn", bookDetails.getIsbn());
        map.put("order_type", "1");

        if (isPersonBorrow) {
            if (TextUtils.isEmpty(editLetter.getText().toString())) {
                showToast("请输入备注");
                return;
            }
            map.put("remark", editLetter.getText().toString());
            map.put("uid", bookPersonList.get(currentChoose).getUser_id());
        } else {
            map.put("serial_number", bookCaseList.get(currentChoose).getSerial_number());
        }
        Intent intent = new Intent(this, PayBookActivity.class);
        intent.putExtra("bookDetails", bookDetails);
        intent.putExtra("map", map);
        startActivity(intent);
    }

}
