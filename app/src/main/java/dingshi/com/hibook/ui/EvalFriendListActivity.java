package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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

/**
 * @author wangqi
 * @since 2017/12/21 上午9:30
 */


public class EvalFriendListActivity extends BaseActivity implements IBookDetailsView {
    @BindView(R.id.eval_friend_list_loading)
    LoadingLayout loadingLayout;
    @BindView(R.id.eval_friend_list_smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.eval_friend_list_recycle)
    RecyclerView friendRecycle;


    String bookIsbn;
    int praiseIndex;
    int evalFriendIndex = -1;

    int page = 1;
    BookDetailsPresent present = new BookDetailsPresent(this, this);

    FuckYouAdapter friendAdapter;
    List<CommentInfo.JsonDataBean> commentInfoList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_eval_friend_list;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "书友评论", "", R.drawable.eval_edit);
        bookIsbn = getIntent().getStringExtra("isbn");
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                present.loadBookFriend(bookIsbn, String.valueOf(page));
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                present.loadBookFriend(bookIsbn, String.valueOf(page));
            }
        });


        friendAdapter = new FuckYouAdapter<>(R.layout.view_book_details_friend_item, commentInfoList);
        friendRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        friendRecycle.setAdapter(friendAdapter);
        friendAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(final BaseViewHolder helper, Object item) {
                final CommentInfo.JsonDataBean bean = (CommentInfo.JsonDataBean) item;
                ImageView avatar = helper.getView(R.id.item_book_details_friend_photo);
                GlideUtils.loadCircleImage(EvalFriendListActivity.this, bean.getAvatar(), avatar);
                helper.setText(R.id.item_book_details_friend_nick, bean.getNick_name());
                helper.setText(R.id.item_book_details_friend_time, bean.getCreated_at());
                helper.setText(R.id.item_book_details_friend_content, bean.getContent());
                helper.setText(R.id.item_book_details_friend_msg_num, bean.getComment_total() + "");
                helper.setText(R.id.item_book_details_friend_praise_num, bean.getPraise() + "");
                //点赞点击事件
                helper.getView(R.id.item_book_details_friend_praise).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        praiseIndex = helper.getLayoutPosition();
                        present.loadEvalPraise(bookIsbn, "2", bean.getId());
                    }
                });

                //评论点击事件
                helper.getView(R.id.item_book_details_friend_msg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        evalFriendIndex = helper.getLayoutPosition();
                        Intent intent = new Intent();
                        intent.setClass(EvalFriendListActivity.this, EvalFriendActivity.class);
                        intent.putExtra("isbn", bookIsbn);
                        intent.putExtra("CommentId", commentInfoList.get(evalFriendIndex).getId());
                        startActivityForResult(intent, EvalFriendActivity.EVAL_FRIEND_REQUEST);
                    }
                });

                RecyclerView recyclerView = helper.getView(R.id.item_book_details_friend_recycle);
                childEval(recyclerView, bean);

            }
        });
        friendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        present.loadBookFriend(bookIsbn, String.valueOf(page));
    }

    /**
     * 加载子recycle
     *
     * @param recyclerView
     * @param bean
     */
    public void childEval(RecyclerView recyclerView, final CommentInfo.JsonDataBean bean) {
        FuckYouAdapter adapter = new FuckYouAdapter<>(R.layout.view_book_details_chid_friend_item, bean.getComment_cascade());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                CommentInfo.JsonDataBean.CommentCascadeBean caseBean= (CommentInfo.JsonDataBean.CommentCascadeBean) item;

                ImageView avatar = helper.getView(R.id.item_book_details_child_friend_photo);
                GlideUtils.loadCircleImage(EvalFriendListActivity.this, caseBean.getAvatar(), avatar);

                helper.setText(R.id.item_book_details_child_friend_nick, caseBean.getNick_name());
                helper.setText(R.id.item_book_details_child_friend_time, caseBean.getCreated_at());
                helper.setText(R.id.item_book_details_child_friend_content, caseBean.getContent());
            }
        });
    }


    @Override
    public void onBookFriend(CommentInfo commentInfo) {
        if (page == 1) {
            commentInfoList.clear();
        }
        loadingLayout.showContentView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
        commentInfoList.addAll(commentInfo.getJsonData());
        friendAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBookPraise(String type) {
        CommentInfo.JsonDataBean infoBean = commentInfoList.get(praiseIndex);
        infoBean.setPraise(infoBean.getPraise() + 1);
        friendAdapter.notifyItemChanged(praiseIndex);
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        evalFriendIndex = -1;
        Intent intent = new Intent();
        intent.putExtra("CommentId", "0");
        intent.putExtra("isbn", bookIsbn);
        intent.setClass(this, EvalFriendActivity.class);
        startActivityForResult(intent, EvalFriendActivity.EVAL_FRIEND_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        CommentInfo.JsonDataBean commInfoAdd = (CommentInfo.JsonDataBean) data.getSerializableExtra("CommInfoAdd");


        Log.i("commInfoAdd",commInfoAdd.toString());


        if (evalFriendIndex == -1) {
            commentInfoList.add(0, commInfoAdd);
        } else {

            //创建二层信息
            CommentInfo.JsonDataBean.CommentCascadeBean bean = new CommentInfo.JsonDataBean.CommentCascadeBean();

            bean.setAvatar(commInfoAdd.getAvatar());
            bean.setBook_isbn(commInfoAdd.getBook_isbn());
            bean.setComment_id(commInfoAdd.getComment_id());
            bean.setContent(commInfoAdd.getContent());
            bean.setCreated_at(commInfoAdd.getCreated_at());
            bean.setId(commInfoAdd.getId());
            bean.setNick_name(commInfoAdd.getNick_name());
            bean.setPraise(commInfoAdd.getPraise());
            bean.setUser_id(commInfoAdd.getUser_id());

            Log.i("bean",bean.toString());



            CommentInfo.JsonDataBean infoBean = commentInfoList.get(evalFriendIndex);

            if (infoBean.getComment_cascade() == null) {
                infoBean.setComment_cascade(new ArrayList<CommentInfo.JsonDataBean.CommentCascadeBean>());
            }
            infoBean.getComment_cascade().add(bean);
            infoBean.setComment_total(infoBean.getComment_total() + 1);
        }
        friendAdapter.notifyDataSetChanged();
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

    @Override
    public void onBookEval(CommentGrade commentGrade) {

    }

    @Override
    public void onBookPerson(BookPerson bookPerson) {

    }

    @Override
    public void onLoad() {
        loadingLayout.showLoadingView();
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
    public void onSuccess(Result result) {

    }

}
