package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ICase2BookView;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.Case2Book;
import dingshi.com.hibook.present.Case2BookPresent;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.PixelUtils;
import dingshi.com.hibook.view.DividVerticalDecoration;

/**
 * @author wangqi
 *         <p>
 *         书柜里的所有图书
 */
public class Case2BookActivity extends BaseActivity implements ICase2BookView {

    @BindView(R.id.book_list_recycle)
    RecyclerView mRecycleview;
    FuckYouAdapter fuckYouAdapter;

    List<Case2Book.JsonDataBean.CellsBean> list = new ArrayList<>();

    private String serialNumber;
    private String radius;

    Case2BookPresent present = new Case2BookPresent(this, this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_case2_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        serialNumber = getIntent().getStringExtra("serial_number");
        radius = getIntent().getStringExtra("radius");


        requestActionBarStyle(true, "书柜图书");

        mRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycleview.addItemDecoration(new DividVerticalDecoration(this, PixelUtils.dip2Px(this, 10), 0xfff1f1f1));
        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_list_item, list);
        mRecycleview.setAdapter(fuckYouAdapter);


        present.onLoad(serialNumber);


        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                Case2Book.JsonDataBean.CellsBean bean = (Case2Book.JsonDataBean.CellsBean) item;
                ImageView bookPhoto = helper.getView(R.id.item_list_photo);
                GlideUtils.load(Case2BookActivity.this, bean.getBook_cover(), bookPhoto);
                helper.setText(R.id.item_list_book, bean.getBook_name());
                helper.setText(R.id.item_list_author, "作者: " + bean.getBook_author());
                if (bean!=null&&bean.getBook_press()!=null){
                    if (bean.getBook_press().contains("null")||bean.getBook_press()==""){
                        helper.setText(R.id.item_list_concern, "出版社: 暂无");
                    }else {
                        helper.setText(R.id.item_list_concern, "出版社: " + bean.getBook_press());
                    }
                }
                helper.setText(R.id.item_list_concern_time, "出版时间: " + bean.getBook_publish_time());

                TextView txStatus = helper.getView(R.id.item_list_status);
                txStatus.setVisibility(View.VISIBLE);
                if (bean.getStatus() == 1) {
                    txStatus.setText("可借");
                    txStatus.setTextColor(getResources().getColor(R.color.blue_32));
                } else if (bean.getStatus() == 0) {
                    txStatus.setText("不可借");
                    txStatus.setTextColor(getResources().getColor(R.color.red));
                } else if (bean.getStatus() == 2) {
                    txStatus.setText("已预约");
                    txStatus.setTextColor(getResources().getColor(R.color.gold_d8));
                }

            }
        });

        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                jumpActivity(position);
            }
        });
    }


    /**
     * 拼接书柜信息和书单信息
     *
     * @param position
     */
    private void jumpActivity(int position) {

        if (list.get(position).getStatus() != 1) {
            showToast("当前不可借");
            return;
        }

        /*
            拼接图书信息
         */
        Case2Book.JsonDataBean.CellsBean bean = list.get(position);
        BookDetails.JsonDataBean bookDetails = new BookDetails.JsonDataBean();
        bookDetails.setCover(bean.getBook_cover());
        bookDetails.setName(bean.getBook_name());
        bookDetails.setAuthor(bean.getBook_author());
        bookDetails.setIsbn(bean.getBook_isbn());
        bookDetails.setPress(bean.getBook_press());
        bookDetails.setPublish_time(bean.getBook_publish_time());

        /*
         拼接书柜信息
         */
        List<BookCase.JsonDataBean> bookCaseList = new ArrayList<>();
        BookCase.JsonDataBean caseBean = new BookCase.JsonDataBean();
        caseBean.setAddress(caseInfo.getAddress());
        caseBean.setRadius(radius);
        caseBean.setSerial_number(caseInfo.getSerial_number());
        bookCaseList.add(caseBean);
        /*
           跳转
         */
        Intent intent = new Intent();
        intent.setClass(this, BorrowBookActivity.class);
        intent.putExtra("bookCase", (Serializable) bookCaseList);
        intent.putExtra("bookDetails", bookDetails);
        startActivity(intent);
    }


    Case2Book.JsonDataBean caseInfo;

    @Override
    public void onSuccess(Case2Book bookList) {
        this.caseInfo = bookList.getJsonData();
        list.addAll(caseInfo.getCells());
        fuckYouAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {

    }
}
