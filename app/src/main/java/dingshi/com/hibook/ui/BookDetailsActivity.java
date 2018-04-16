package dingshi.com.hibook.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
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
import dingshi.com.hibook.utils.KefuUtils;
import dingshi.com.hibook.view.AbsRecyclerView;
import dingshi.com.hibook.view.ExpandTextView;
import retrofit2.http.Part;

import static dingshi.com.hibook.R.id.default_activity_button;
import static dingshi.com.hibook.R.id.item_book_details_friend_photo;
import static dingshi.com.hibook.R.id.withText;

/**
 * @author wangqi
 * @since 2017/12/12 下午1:38
 */

public class BookDetailsActivity extends BaseActivity implements IBookDetailsView {

    /**
     * 书的图片
     */
    @BindView(R.id.book_details_img)
    ImageView imgBook;
    /**
     * 书的名称
     */
    @BindView(R.id.book_details_book)
    TextView txBookName;
    /**
     * 书的作者
     */
    @BindView(R.id.book_details_author)
    TextView txBookAuthor;
    /**
     * 出版社
     */
    @BindView(R.id.book_details_concern)
    TextView txConcern;
    /**
     * 出版时间
     */
    @BindView(R.id.book_details_concern_time)
    TextView txConcernTime;
    /**
     * 豆瓣评分
     */
    @BindView(R.id.book_details_douban_score)
    TextView txDbScore;
    /**
     * 豆瓣评级
     */
    @BindView(R.id.book_details_douban_rating)
    AppCompatRatingBar dbRating;
    /**
     * 豆瓣多少人评论
     */
    @BindView(R.id.book_details_douban_eval)
    TextView txDbEval;
    /**
     * hibook评分
     */
    @BindView(R.id.book_details_hibook_score)
    TextView txhbScore;
    /**
     * hibook评级
     */
    @BindView(R.id.book_details_hibook_rating)
    AppCompatRatingBar hbRating;
    /**
     * hibook多少人评论
     */
    @BindView(R.id.book_details_hibook_eval)
    TextView txhbEval;
    /**
     * 图书简介
     */
    @BindView(R.id.book_details_book_summary)
    ExpandTextView txSummary;


    @BindView(R.id.book_details_case_recycle)
    AbsRecyclerView caseRecycle;
    @BindView(R.id.book_details_person_recycle)
    AbsRecyclerView personRecycle;
    @BindView(R.id.book_details_talent_recycle)
    AbsRecyclerView talentRecycle;
    @BindView(R.id.book_details_eval_recycle)
    AbsRecyclerView evalRecycle;
    @BindView(R.id.book_details_friend_recycle)
    AbsRecyclerView friendRecycle;

    /**
     * 共享图书
     */
    @BindView(R.id.book_details_share)
    TextView txShareBook;
    /**
     * 马上借书
     */
    @BindView(R.id.book_details_borrow)
    TextView txBorrow;

    /**
     * 图书的isbn
     */
    String bookIsbn;

    BookDetailsPresent present = new BookDetailsPresent(this, this);

    /**
     * 存储点击评论进行评论index
     * 默认-1为图书的评论
     */
    int evalFriendIndex = -1;
    /**
     * 存储当前评论点赞的index
     */
    int praiseIndex = -1;
    /**
     * 0表示未被借出，1表示已经被借出
     */
    int is_borrow=0;


    @Override
    public int getLayoutId() {
        return R.layout.activity_book_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //解决recycle滑动不流畅的问题
        friendRecycle.setNestedScrollingEnabled(false);
        evalRecycle.setNestedScrollingEnabled(false);
        requestActionBarStyle(true, "详情", "");
        bookIsbn = getIntent().getStringExtra("isbn");
        initBookDetails();
        initPersonRecycle();
        initCaseRecycle();
        initTalentRecycle();
        initEvalRecycle();
        initFriendRecycle();

        //判断进入的uid是否和自己的uid相同  是的话就是共享图书操作
        if (user.getJsonData().getUser_id().equals(getIntent().getStringExtra("uid"))) {
            txShareBook.setVisibility(View.VISIBLE);
            txBorrow.setVisibility(View.GONE);
            switch (getIntent().getIntExtra("type", 0)) {
                case 1:
                    txShareBook.setText("已共享,收回图书");
                    txShareBook.setClickable(true);
                    break;
                case 2:
                    txShareBook.setText("已共享");
                    txShareBook.setClickable(false);
                    break;
                case 3:
                    txShareBook.setText("共享该书");
                    txShareBook.setClickable(true);
                    break;
                default:

            }
        } else {
            txShareBook.setVisibility(View.GONE);
            txBorrow.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 初始化数据
     */
    private void initBookDetails() {
        Log.i("bookIsbn", bookIsbn);

        present.loadBookDetails(bookIsbn);
        present.loadBookPerson(bookIsbn);
        present.loadBookCase(bookIsbn);
        present.loadBookTalent(bookIsbn);
        present.loadBookEval(bookIsbn, "1");
        present.loadBookFriend(bookIsbn, "1");
    }


    /**
     * 书柜Recycle
     */
    FuckYouAdapter caseAdapter;
    List<BookCase.JsonDataBean> bookCaseList = new ArrayList<>();

    private void initCaseRecycle() {
        caseAdapter = new FuckYouAdapter<BookCase.JsonDataBean>(R.layout.view_book_details_case_item, bookCaseList);
        caseRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        caseRecycle.setAdapter(caseAdapter);
        caseAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                BookCase.JsonDataBean bean = (BookCase.JsonDataBean) item;
                helper.setText(R.id.item_book_details_case_address, bean.getAddress());
                helper.setText(R.id.item_book_details_case_distance, bean.getRadius() + "km");
            }
        });
    }

    /**
     * 共享图书的人
     */
    FuckYouAdapter personAdapter;
    List<BookPerson.JsonDataBean> bookPersonList = new ArrayList<>();

    private void initPersonRecycle() {
        personAdapter = new FuckYouAdapter<>(R.layout.view_book_details_person_item, bookPersonList);
        personRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        personRecycle.setAdapter(personAdapter);
        personAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                BookPerson.JsonDataBean bean = (BookPerson.JsonDataBean) item;
                if(is_borrow==0){
                    personRecycle.setVisibility(View.VISIBLE);
                    ImageView photo = helper.getView(R.id.book_details_person_photo);
                    GlideUtils.loadCircleImage(BookDetailsActivity.this, bean.getAvatar(), photo);
                    helper.setText(R.id.book_details_person_name, bean.getNick_name());
                    helper.setText(R.id.book_details_person_content, "读书" + bean.getRead_num() + "|藏书" + bean.getHave_num() + "本");

                }else {
                    personRecycle.setVisibility(View.GONE);
                }

            }
        });
    }


    /**
     * 达人Recycle
     */
    FuckYouAdapter talentAdapter;
    List<BookTalent.JsonDataBean> bookTalentList = new ArrayList<>();

    private void initTalentRecycle() {
        talentAdapter = new FuckYouAdapter<BookTalent.JsonDataBean>(R.layout.view_book_details_talent_item, bookTalentList);
        talentRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        talentRecycle.setAdapter(talentAdapter);
        talentAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                BookTalent.JsonDataBean bean = (BookTalent.JsonDataBean) item;
                ImageView photo = helper.getView(R.id.item_book_details_talent_avatar);
                GlideUtils.loadCircleImage(BookDetailsActivity.this, bean.getAvatar(), photo);
                helper.setText(R.id.item_book_details_talent_nick, bean.getNick_name());
            }
        });
    }

    /**
     * 图书评价Recycle
     */
    FuckYouAdapter evalAdapter;
    List<CommentGrade.JsonDataBean> commentGradeList = new ArrayList<>();

    private void initEvalRecycle() {
        evalAdapter = new FuckYouAdapter<CommentGrade.JsonDataBean>(R.layout.view_book_details_eval_item, commentGradeList);
        evalRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        evalRecycle.setAdapter(evalAdapter);
        evalAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(final BaseViewHolder helper, Object item) {
                final CommentGrade.JsonDataBean bean = (CommentGrade.JsonDataBean) item;
                ImageView avatar = helper.getView(R.id.item_book_details_eval_avatar);
                GlideUtils.loadCircleImage(BookDetailsActivity.this, bean.getAvatar(), avatar);

                helper.setText(R.id.item_book_details_eval_nick, bean.getNick_name());
                AppCompatRatingBar rating = helper.getView(R.id.item_book_details_eval_rating);
                rating.setRating(bean.getScore() );
                helper.setText(R.id.item_book_details_eval_score, (int) (bean.getScore()) + "分");
                helper.setText(R.id.item_book_details_eval_content, bean.getContent());
                helper.setText(R.id.item_book_details_eval_praise_num, bean.getPraise() + "");
                //点赞点击事件
                helper.getView(R.id.item_book_details_eval_praise).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        praiseIndex = helper.getLayoutPosition();
                        present.loadEvalPraise(bookIsbn, "1", bean.getId());
                        helper.setImageResource(R.id.item_book_details_eval_praise_img, R.drawable.praise_focus);

                    }
                });
            }
        });
    }

    /**
     * 书友交流评价
     */
    FuckYouAdapter friendAdapter;
    List<CommentInfo.JsonDataBean> commentInfoList = new ArrayList<>();

    private void initFriendRecycle() {
        friendAdapter = new FuckYouAdapter<CommentInfo.JsonDataBean>(R.layout.view_book_details_friend_item, commentInfoList);
        friendRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        friendRecycle.setAdapter(friendAdapter);
        friendAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(final BaseViewHolder helper, Object item) {
                final CommentInfo.JsonDataBean bean = (CommentInfo.JsonDataBean) item;
                ImageView avatar = helper.getView(R.id.item_book_details_friend_photo);
                GlideUtils.loadCircleImage(BookDetailsActivity.this, bean.getAvatar(), avatar);
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
                        helper.setImageResource(R.id.item_book_details_friend_praise_img, R.drawable.praise_focus);
                    }
                });

                //评论点击事件
                helper.getView(R.id.item_book_details_friend_msg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        evalFriendIndex = helper.getLayoutPosition();
                        Intent intent = new Intent();
                        intent.setClass(BookDetailsActivity.this, EvalFriendActivity.class);
                        intent.putExtra("isbn", bookIsbn);
                        intent.putExtra("CommentId", commentInfoList.get(evalFriendIndex).getId());
                        startActivityForResult(intent, EvalFriendActivity.EVAL_FRIEND_REQUEST);
                    }
                });


            }
        });
        friendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }


    @OnClick({R.id.book_details_edit_eval, R.id.book_details_edit_friend,
            R.id.book_details_borrow, R.id.book_details_contact,
            R.id.book_details_eval_query, R.id.book_details_friend_query, R.id.book_details_share})
    public void onClickActivity(View v) {
        Intent intent = new Intent();
        intent.putExtra("isbn", bookIsbn);
        switch (v.getId()) {
            case R.id.book_details_edit_eval:
                intent.setClass(this, EvalBookActivity.class);
                startActivityForResult(intent, EvalBookActivity.EVAL_BOOK_REQUEST);
                break;
            case R.id.book_details_edit_friend:
                evalFriendIndex = -1;
                intent.setClass(this, EvalFriendActivity.class);
                intent.putExtra("CommentId", "0");
                startActivityForResult(intent, EvalFriendActivity.EVAL_FRIEND_REQUEST);
                break;
            case R.id.book_details_borrow:
                jumpActivity();
                break;
            case R.id.book_details_contact:
                KefuUtils.jump(getApplication());
                break;
            case R.id.book_details_eval_query:
                intent.setClass(this, EvalBookListActivity.class);
                startActivity(intent);
                break;
            case R.id.book_details_friend_query:
                intent.setClass(this, EvalFriendListActivity.class);
                startActivity(intent);
                break;
            case R.id.book_details_share:
                switch (getIntent().getIntExtra("type", 0)) {
                    case 1:
                        recoverBook();
                        break;
                    case 2:
                        break;
                    case 3:
                        shareBook();
                        break;
                    default:
                }
                break;
            default:
        }
    }

    public String[] pros = {"向书柜借书", "向书友借书"};
    AlertDialog dialog;

    public void jumpActivity() {
        if (bookPersonList.size() == 0 && bookCaseList.size() == 0) {
            showToast("该书当前不可借阅");
            return;
        }

        dialog = new AlertDialog.Builder(this)
                .setTitle("请选择借阅方式")
                .setSingleChoiceItems(pros, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                if (which == 0) {
                                    if (bookCaseList.size()>0){
                                        Intent intent = new Intent();
                                        intent.putExtra("isbn", bookIsbn);
                                        intent.setClass(BookDetailsActivity.this, BorrowBookActivity.class);
                                        intent.putExtra("bookCase", (Serializable) bookCaseList);
                                        intent.putExtra("bookDetails", bookDetails);
                                        startActivity(intent);
                                    }else {
                                        showToast("当前无书柜");
                                    }

                                } else {
                                    Intent intent = new Intent();
                                    intent.putExtra("isbn", bookIsbn);
                                    intent.setClass(BookDetailsActivity.this, BorrowBookActivity.class);
                                    intent.putExtra("bookPerson", (Serializable) bookPersonList);
                                    intent.putExtra("bookDetails", bookDetails);
                                    startActivity(intent);
                                }
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();

    }


    public String[] share = {"共享到书柜", "留在自己手中"};

    public void shareBook() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("请选择共享方式")
                .setSingleChoiceItems(share, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                Intent intent = new Intent();
                                intent.setClass(BookDetailsActivity.this, ShareBookActivity.class);
                                intent.putExtra("share", which);
                                intent.putExtra("bookDetails", bookDetails);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }


    public void recoverBook() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("请联系客服妹妹")
                .setMessage("400-999-9999\n\n继续共享，可以获得更多的回报哦!")
                .setPositiveButton("以后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("立即联系", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400 999 9999"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .create();
        dialog.show();
    }


    BookDetails.JsonDataBean bookDetails;

    /**
     * 图书详情数据返回
     *
     * @param bookDetails 图书明细
     */
    @Override
    public void onBookDetails(BookDetails bookDetails) {
        BookDetails.JsonDataBean bean = bookDetails.getJsonData();
        this.bookDetails = bean;
        is_borrow=bean.getIs_borrow();
        GlideUtils.load(this, bean.getCover(), imgBook);
        txBookName.setText(bean.getName());
        if (bean!=null&&bean.getAuthor()!=null){
            if (bean.getAuthor().contains("null")){
                txBookAuthor.setText("作者: 暂无");
            }else {
                txBookAuthor.setText("作者: " + bean.getAuthor());
            }
        }
        if (bean!=null&&bean.getPress()!=null){
            if (bean.getPress().contains("null")||bean.getPress()==""){
                txConcern.setText("出版社: 暂无");
            }else {
                txConcern.setText("出版社: " + bean.getPress());
            }
        }
        txConcernTime.setText("出版时间: " + bean.getPublish_time());
        txDbScore.setText(String.format("%s", bean.getDouban_grade()));
        txDbEval.setText("豆瓣" + bean.getDouban_grade_number() + "人评论");
        dbRating.setRating(bean.getDouban_grade() / 2);
        txhbScore.setText(String.format("%s", bean.getGrade()));
        txhbEval.setText("图书" + bean.getGrade_number() + "人评论");
        hbRating.setRating(bean.getGrade());
        txSummary.setText("简介:\n" + bean.getSummary());
    }

    @Override
    public void onBookCase(BookCase bookCase) {
        bookCaseList.addAll(bookCase.getJsonData());
        caseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBookTalent(BookTalent bookTalent) {
        bookTalentList.addAll(bookTalent.getJsonData());
        talentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBookEval(CommentGrade commentGrade) {
        commentGradeList.addAll(commentGrade.getJsonData());
        evalAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBookFriend(CommentInfo commentInfo) {
        commentInfoList.addAll(commentInfo.getJsonData());
        friendAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBookPerson(BookPerson bookPerson) {
        bookPersonList.addAll(bookPerson.getJsonData());
        personAdapter.notifyDataSetChanged();
    }

    /**
     * 点赞
     *
     * @param type 类型 1.评分,2.评论
     */
    @Override
    public void onBookPraise(String type) {
        switch (type) {
            case "1":
                CommentGrade.JsonDataBean gradeBean = commentGradeList.get(praiseIndex);
                gradeBean.setPraise(gradeBean.getPraise() + 1);
                evalAdapter.notifyItemChanged(praiseIndex);
                break;
            case "2":
                CommentInfo.JsonDataBean infoBean = commentInfoList.get(praiseIndex);
                infoBean.setPraise(infoBean.getPraise() + 1);
                friendAdapter.notifyItemChanged(praiseIndex);
                break;
            default:
        }
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
                present.loadBookDetails(bookIsbn);
                break;
            /* 需要判断图书评论-1 还是评论中的评论 */
            case EvalFriendActivity.EVAL_FRIEND_REQUEST:
                CommentInfo.JsonDataBean commInfoAdd = (CommentInfo.JsonDataBean) data.getSerializableExtra("CommInfoAdd");
                if (evalFriendIndex == -1) {
                    commentInfoList.add(0, commInfoAdd);
                } else {
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
                    CommentInfo.JsonDataBean infoBean = commentInfoList.get(evalFriendIndex);
                    infoBean.setComment_cascade(new ArrayList<CommentInfo.JsonDataBean.CommentCascadeBean>());
                    infoBean.getComment_cascade().add(0, bean);
                    infoBean.setComment_total(infoBean.getComment_total() + 1);
                }
                friendAdapter.notifyDataSetChanged();
                break;
            default:
        }
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onSuccess(Result result) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onEmpty() {

    }
}
