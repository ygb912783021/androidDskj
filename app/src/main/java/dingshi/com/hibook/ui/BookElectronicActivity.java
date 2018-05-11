package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.administrator.readbook.bean.BookShelfBean;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.EbookGratis;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.eventbus.EventBusHelper;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author yangguangbing
 * @since 2018/4/9
 *  电子书领取页面
 */


public class BookElectronicActivity extends BaseActivity {

    @BindView(R.id.book_electroic_gridview)
    GridView electroic_gridview;
    private List<Map<String, Object>> dataList = null;
    private SimpleAdapter adapter;
    private List<EbookGratis.JsonDataBean> ebookGratis = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_electronic_shelf;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Log.i("TEST", "initView: .......................................");
        Constant.isEditDelete=false;
//        EventBusHelper.register(this);
        requestActionBarStyle(true, "电子书柜", "");

        showEbook();


    }

    /**
     * 数据获取(假数据)
     */
    void initData() {
        //图标
        int icno[] = { R.drawable.books, R.drawable.books,
                R.drawable.books, R.drawable.books
                , R.drawable.books, R.drawable.books
                };
        //图标下的文字
        String name[]={"第一个","第二个","第三个","第四个","第五个","第六个"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <ebookGratis.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",ebookGratis.get(i).getName());
            dataList.add(map);
        }
    }


    @OnClick({R.id.electroic_gridview_text})
    public void onClick(View v) {
//        for (int i = 0;i<ebookGratis.size();i++){
//            BookShelfBean bookShelfBean = new BookShelfBean();
//            BookInfoBean bookInfoBean = new BookInfoBean();
//            bookInfoBean.setAuthor(ebookGratis.get(i).getAuthor());
//            bookInfoBean.setName(ebookGratis.get(i).getName());
////            bookInfoBean.setChapterlist();
//            bookInfoBean.setChapterUrl(ebookGratis.get(i).getFile_name());
//            bookInfoBean.setCoverUrl(ebookGratis.get(i).getCover());
//            bookInfoBean.setIntroduce(ebookGratis.get(i).getTranslator());
//            bookInfoBean.setNoteUrl(ebookGratis.get(i).getFile_name());
//            bookShelfBean.setBookInfoBean(bookInfoBean);
//            bookShelfBean.setDurChapter(1);
//            bookShelfBean.setDurChapterPage(1);
//            bookShelfBean.setNoteUrl(ebookGratis.get(i).getFile_name());
//            addToBookShelf(bookShelfBean);
//        }
        setOpen();

    }




    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unRegister(this);
    }
    public void showEbook() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<EbookGratis>("gratisEbook") {
            @Override
            protected void onStart(Disposable d) {
                Log.i("READ", "onError:  gratisEbook  Disposable d = "+ d);
            }

            @Override
            protected void onError(ApiException e) {
                Log.i("READ", "onError:  gratisEbook  ApiException e = "+ e);
            }

            @Override
            protected void onSuccess(EbookGratis response) {
                Log.i("READ", "onSuccess: gratis Ebookresponse = "+response.getJsonData());
                if(null!=response.getJsonData()) {
                    ebookGratis.addAll(response.getJsonData());
                    initData();
                    int[] to = {R.id.electronic_gridview_img, R.id.electronic_gridview_text};
                    adapter = new SimpleAdapter(BookElectronicActivity.this, dataList, R.layout.view_electronic_gridview_item, new String[]{"img", "text"}, to);
                    electroic_gridview.setAdapter(adapter);
                    Log.i("READ", "onSuccess: ebookGratis = " + ebookGratis.size());
                }
            }


        };

        HashMap<String, String> map = new HashMap<>(1);
        map = AppSign.buildMap(map);
        Observable<EbookGratis> observable = NetUtils.getGsonRetrofit().gratisEbook(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     *  领取电子书
     */
    public void setOpen() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("takeEBooks") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
//                getView().onError(e.getMsg());
                showToast("领取失败");
            }

            @Override
            protected void onSuccess(Result response) {
                startActivity(new Intent(BookElectronicActivity.this,BookElectronicCaseActivity.class));
                finish();
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().takeEBooks(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
    /**
     * 添加电子书到书架
     */
    public void addToBookShelf(final BookShelfBean bookShelf) {
        if (bookShelf != null) {
//            DbHelper.getInstance(getApplicationContext()).getmDaoSession().getChapterListBeanDao().insertOrReplaceInTx(bookShelf.getBookInfoBean().getChapterlist());
//            DbHelper.getInstance(getApplicationContext()).getmDaoSession().getBookInfoBeanDao().insertOrReplace(bookShelf.getBookInfoBean());
//            //网络数据获取成功  存入BookShelf表数据库
//            DbHelper.getInstance(getApplicationContext()).getmDaoSession().getBookShelfBeanDao().insertOrReplace(bookShelf);
        }
    }


}
