package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.IBookDetailsView;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BookPerson;
import dingshi.com.hibook.bean.BookTalent;
import dingshi.com.hibook.bean.CommentGrade;
import dingshi.com.hibook.bean.CommentInfo;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.present.BookDetailsPresent;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.LoadingLayout;
import retrofit2.http.Part;

/**
 * @author wangqi
 * @since 2017/12/21 上午9:37
 */


public class EvalBookListActivity extends BaseActivity implements IBookDetailsView {
    @BindView(R.id.eval_book_list_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.eval_book_list_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.eval_book_list_recycle)
    RecyclerView recyclerView;

    FuckYouAdapter evalAdapter;
    List<CommentGrade.JsonDataBean> commentGradeList = new ArrayList<>();


    BookDetailsPresent present = new BookDetailsPresent(this, this);

    String bookIsbn;
    int praiseIndex;

    int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_eval_book_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bookIsbn = getIntent().getStringExtra("isbn");
        requestActionBarStyle(true, "书友评论", "", R.drawable.eval_edit);


        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                present.loadBookEval(bookIsbn, String.valueOf(page));
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                present.loadBookEval(bookIsbn, String.valueOf(page));
            }
        });

        evalAdapter = new FuckYouAdapter<>(R.layout.view_book_details_eval_item, commentGradeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(evalAdapter);
        evalAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(final BaseViewHolder helper, Object item) {
                final CommentGrade.JsonDataBean bean = (CommentGrade.JsonDataBean) item;
                ImageView avatar = helper.getView(R.id.item_book_details_eval_avatar);
                GlideUtils.loadCircleImage(EvalBookListActivity.this, bean.getAvatar(), avatar);

                helper.setText(R.id.item_book_details_eval_nick, bean.getNick_name());
                AppCompatRatingBar rating = helper.getView(R.id.item_book_details_eval_rating);
                rating.setRating(bean.getScore());
                helper.setText(R.id.item_book_details_eval_score, bean.getScore() + "分");
                helper.setText(R.id.item_book_details_eval_content, bean.getContent());
                helper.setText(R.id.item_book_details_eval_praise_num, bean.getPraise() + "");
                //点赞点击事件
                helper.getView(R.id.item_book_details_eval_praise).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        praiseIndex = helper.getLayoutPosition();
                        present.loadEvalPraise(bookIsbn, "1", bean.getId());
                    }
                });
            }
        });
        present.loadBookEval(bookIsbn, String.valueOf(page));
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        Intent intent = new Intent();
        intent.putExtra("isbn", bookIsbn);
        intent.setClass(this, EvalBookActivity.class);
        startActivityForResult(intent, EvalBookActivity.EVAL_BOOK_REQUEST);
    }

    /**
     * 评价
     *
     * @param commentGrade
     */
    @Override
    public void onBookEval(CommentGrade commentGrade) {
        if (page == 1) {
            commentGradeList.clear();
        }
        loadingLayout.showContentView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
        commentGradeList.addAll(commentGrade.getJsonData());
        evalAdapter.notifyDataSetChanged();
    }

    /**
     * 点赞
     *
     * @param type
     */
    @Override
    public void onBookPraise(String type) {
        CommentGrade.JsonDataBean gradeBean = commentGradeList.get(praiseIndex);
        gradeBean.setPraise(gradeBean.getPraise() + 1);
        evalAdapter.notifyItemChanged(praiseIndex);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case EvalBookActivity.EVAL_BOOK_REQUEST:
                CommentGrade.JsonDataBean commGradeAdd = (CommentGrade.JsonDataBean) data.getSerializableExtra("CommGradeAdd");

                commentGradeList.add(0, commGradeAdd);

                evalAdapter.notifyDataSetChanged();
                break;
            default:
        }
    }


    @Override
    public void onLoad() {
        loadingLayout.showLoadingView();
    }

    @Override
    public void onSuccess(Result result) {

    }

    @Override
    public void onError(String error) {
        loadingLayout.showErrorView();
    }

    @Override
    public void onEmpty() {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
        loadingLayout.showEmptyView();
    }

    @Override
    public void onBookFriend(CommentInfo commentInfo) {

    }

    @Override
    public void onBookPerson(BookPerson bookPerson) {

    }

    @Override
    public void onBookDetails(BookDetails bookDetails) {

    }

    @Override
    public void onBookCase(BookCase bookCase) {

    }

    @Override
    public void onBookTalent(BookTalent bookTalent) {

    }


}
