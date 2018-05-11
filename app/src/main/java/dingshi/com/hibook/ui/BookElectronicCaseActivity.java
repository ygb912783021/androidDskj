package dingshi.com.hibook.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.ElectronicCaseAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookChapter;
import dingshi.com.hibook.bean.BookShelf;
import dingshi.com.hibook.bean.EbookGratis;
import dingshi.com.hibook.bean.EbookGroup;
import dingshi.com.hibook.bean.EbookList;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.eventbus.EventBusHelper;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.view.FuckDialog;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class BookElectronicCaseActivity extends BaseActivity  {

    @BindView(R.id.electroic_gridview_book)
    GridView electroic_gridview_book;
    @BindView(R.id.electroi_book_bottom)
    LinearLayout electroi_book_bottom;
    @BindView(R.id.electroi_lin_sou)
    LinearLayout electroi_lin_sou;
    boolean editState = true;
//    private List<BookShelfBean> bookShelfBeans = new ArrayList<>();
    FuckDialog fuckDialog;
    ElectronicCaseAdapter electronicCaseAdapter;
    float x,y;
    private List<EbookGratis.JsonDataBean> ebookGratis = new ArrayList<>();
    private List<BookShelf.JsonDataBean.Ebook_group> ebookGroups = new ArrayList<>();
    private List<EbookList> ebookDatas = new ArrayList<>();
    private List<EbookGroup.JsonData> groupList = new ArrayList<>();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            electronicCaseAdapter  = new ElectronicCaseAdapter(BookElectronicCaseActivity.this,ebookGroups,ebookDatas);
            electroic_gridview_book.setAdapter(electronicCaseAdapter);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_electronic_book;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        Constant.isEditDelete=false;
        requestActionBarStyle(true, "电子书柜", "编辑");
        showEbook();
//        addEBooksGroup("我爱的书");
//        ebookMove("1","19,");
//        getEbookChapter("19");
        electronicCaseAdapter  = new ElectronicCaseAdapter(this,ebookGroups,ebookDatas);
        electroic_gridview_book.setAdapter(electronicCaseAdapter);

        electroic_gridview_book.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                }
                if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if ((motionEvent.getY()-y)>50){
                        if (editState) {
                            electroi_lin_sou.setVisibility(View.VISIBLE);
                        }
                    }
                    if ((motionEvent.getY()-y)<-50){
                        electroi_lin_sou.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onRightClick() {
        super.onRightClick();

        setEbooks();
    }
    private void setEbooks(){
        if (editState){
            showselBookGroup();
            electroi_book_bottom.setVisibility(View.VISIBLE);
            editState = false;
            electronicCaseAdapter.setState();
            electronicCaseAdapter.notifyDataSetChanged();
            requestActionBarStyle(true, "电子书柜", "取消");
        }else {
            electroi_book_bottom.setVisibility(View.GONE);
            editState = true;
            electronicCaseAdapter.setState();
            electronicCaseAdapter.notifyDataSetChanged();
            requestActionBarStyle(true, "电子书柜", "编辑");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unRegister(this);
    }

    /**
     * 移除/移动 点击事件
     * @param v
     */
    @OnClick({R.id.electroi_book_yichu_remove,R.id.electroi_book_yidao})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.electroi_book_yichu_remove:
                View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_electronic_remove, null, false);
                fuckDialog = new FuckDialog(this).addView(view).builder();
                fuckDialog.show();
                //取消移除
                view.findViewById(R.id.electroi_book_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fuckDialog.dissmis();
                    }
                });
                //移除选中项
                view.findViewById(R.id.electroi_book_remove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /**
                         * 缺少电子书选中数组字符串
                         */
                        Log.i("READ", "onClick: electronicCaseAdapter = "+electronicCaseAdapter.getStringEbooID());
                        deleteEbooks("");
                        fuckDialog.dissmis();
                        showEbook();
                        setEbooks();
                    }
                });
                break;
            case R.id.electroi_book_yidao:

                showCollection();

                break;
        }

    }
    private void showCollection(){

        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_electronic_collection, null, false);
        fuckDialog = new FuckDialog(this).addView(view).builder();

        fuckDialog.show();
        ListView electroi_list_collection =  view.findViewById(R.id.electroi_list_collection);
        view.findViewById(R.id.electroi_collection_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fuckDialog.dissmis();
                showCollectionAdd();
            }
        });
        Log.i("READ", "showCollection: groupList =  "+groupList.get(0).getTag());
        MyCollectionAdapter myCollectionAdapter= new MyCollectionAdapter(this);
        electroi_list_collection.setAdapter(myCollectionAdapter);
    }
    private void showCollectionAdd(){
        View viewb = LayoutInflater.from(this).inflate(R.layout.view_dialog_electronic_collection_add, null, false);
        fuckDialog = new FuckDialog(this).addView(viewb).builder();
        fuckDialog.show();
        final EditText electroi_book_addgroudsname = viewb.findViewById(R.id.electroi_book_addgroudsname);
        viewb.findViewById(R.id.electroi_book_collection_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fuckDialog.dissmis();
                if (null!=electroi_book_addgroudsname.getText()&&!electroi_book_addgroudsname.getText().equals(""))
                addEBooksGroup(electroi_book_addgroudsname.getText().toString());
            }
        });
    }


    class MyCollectionAdapter extends BaseAdapter{
        Context context;
        MyCollectionAdapter (Context context){
            this.context = context;
        }
        @Override
        public int getCount() {
            return groupList.size()>0?groupList.size():0;
        }
        @Override
        public Object getItem(int i) {
            return groupList.size()>0?groupList.get(i):null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.view_item_ebookgroud, null);
            TextView item_lib_nick = view.findViewById(R.id.item_groud_text);
            item_lib_nick.setText(groupList.get(i).getTag());
            Log.i("READ", "getView: ebookGroupList.get(i).getTag() ===== "+groupList.get(i).getTag());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fuckDialog.dissmis();
                    View views = LayoutInflater.from(context).inflate(R.layout.view_dialog_electronic_remove, null, false);
                    fuckDialog = new FuckDialog(context).addView(views).builder();
                    fuckDialog.show();
                    TextView electroi_book_text = views.findViewById(R.id.electroi_book_text);
                    electroi_book_text.setText("是否移动到"+groupList.get(i).getTag());
                    //取消移除
                    views.findViewById(R.id.electroi_book_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fuckDialog.dissmis();
                        }
                    });
                    //移除选中项
                    views.findViewById(R.id.electroi_book_remove).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /**
                             * 缺少电子书选中数组字符串
                             */
                            ebookMove(groupList.get(i).getId()+"","");
                            fuckDialog.dissmis();
                            showEbook();
                        }
                    });
                }
            });
            return view;
        }
    }

    /**
     * 获取存在本地数据库中的书架列表
     */
    private void getDate(){
//        Observable.create(new ObservableOnSubscribe<List<BookShelfBean>>() {
//            @Override
//            public void subscribe(ObservableEmitter<List<BookShelfBean>> e) throws Exception {
//                bookShelfBeans = DbHelper.getInstance(getApplicationContext()).getmDaoSession().getBookShelfBeanDao().queryBuilder().orderDesc(BookShelfBeanDao.Properties.FinalDate).list();
//                for (int i = 0; i < bookShelfBeans.size(); i++) {
//                    List<BookInfoBean> temp = DbHelper.getInstance(getApplicationContext()).getmDaoSession().getBookInfoBeanDao().queryBuilder().where(BookInfoBeanDao.Properties.NoteUrl.eq(bookShelfBeans.get(i).getNoteUrl())).limit(1).build().list();
//                    if (temp != null && temp.size() > 0) {
//                        BookInfoBean bookInfoBean = temp.get(0);
//                        bookInfoBean.setChapterlist(DbHelper.getInstance(getApplicationContext()).getmDaoSession().getChapterListBeanDao().queryBuilder().where(ChapterListBeanDao.Properties.NoteUrl.eq(bookShelfBeans.get(i).getNoteUrl())).orderAsc(ChapterListBeanDao.Properties.DurChapterIndex).build().list());
//                        bookShelfBeans.get(i).setBookInfoBean(bookInfoBean);
//                    } else {
//                        DbHelper.getInstance(getApplicationContext()).getmDaoSession().getBookShelfBeanDao().delete(bookShelfBeans.get(i));
//                        bookShelfBeans.remove(i);
//                        i--;
//                    }
//                }
//                e.onNext(bookShelfBeans == null ? new ArrayList<BookShelfBean>() : bookShelfBeans);
//                handler.sendEmptyMessage(0);
//                Log.i("READ", "subscribe:bookShelfBeans =  "+bookShelfBeans.size());
//            }
//        });
    }

    /**
     *  获取后台所有电子书
     */
    public void showEbook() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookShelf>("eBooks") {
            @Override
            protected void onStart(Disposable d) {
                Log.i("READ", "onError:  showselBookGroup  Disposable d = "+ d);
            }

            @Override
            protected void onError(ApiException e) {
                Log.i("READ", "onError:  showselBookGroup ApiException e = "+ e);
            }

            @Override
            protected void onSuccess(BookShelf response) {
                if (ebookDatas.isEmpty()) {
                    ebookDatas.addAll(response.getJsonData().getEbook_data());

                }else {
                    ebookDatas.clear();
                    ebookDatas.addAll(response.getJsonData().getEbook_data());
                }
                if (ebookGroups.isEmpty()){
                    ebookGroups.addAll(response.getJsonData().getEbook_group());
                }else {
                    ebookGroups.clear();
                    ebookGroups.addAll(response.getJsonData().getEbook_group());
                }
//                electronicCaseAdapter.notifyDataSetChanged();
                handler.sendEmptyMessage(0);
                Log.i("READ", "onSuccess: showEbook ebookGroups = "+ebookDatas.size()+"     ebookDatas = "+response.toString());
            }
        };

        HashMap<String, String> map = new HashMap<>(2);
        map = AppSign.buildMap(map);
        Observable<BookShelf> observable = NetUtils.getGsonRetrofit().eBooks(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     * 获取电子书分组列表
     */
    public void showselBookGroup() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<EbookGroup>("selBookGroup") {
            @Override
            protected void onStart(Disposable d) {
                Log.i("READ", "onError:  showselBookGroup  Disposable d = "+ d);
            }

            @Override
            protected void onError(ApiException e) {
                Log.i("READ", "onError:  showselBookGroup ApiException e = "+ e);
            }

            @Override
            protected void onSuccess(EbookGroup response) {
                if (groupList.isEmpty()) {
                    groupList.addAll(response.getJsonData());
                }else {
                    groupList.clear();
                    groupList.addAll(response.getJsonData());
                }
                Log.i("READ", "onSuccess: showselBookGroup EbookGroup = "+groupList.get(0).getTag());
            }


        };

        HashMap<String, String> map = new HashMap<>(2);
        map = AppSign.buildMap(map);
        Observable<EbookGroup> observable = NetUtils.getGsonRetrofit().selBookGroup(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     *  创建新的电子书分组
     * @param name
     */
    private void addEBooksGroup(String name){
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("newEBooksGroup") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                Log.i("READ", "onError: e = "+e);
                showToast("创建失败");
            }

            @Override
            protected void onSuccess(Result response) {
                Log.i("READ", "onSuccess:创建成功 ");
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("tag",name);
//        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().newEBooksGroup(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     *  移除电子书 19 1
     * @param strings
     */
    private void deleteEbooks(String strings){
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("deleteEbooks") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                Log.i("READ", "onError: e = "+e);
                showToast("移除失败");
            }

            @Override
            protected void onSuccess(Result response) {
                Log.i("READ", "onSuccess:移除成功 ");
                showToast("移除成功");
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("ebookid",strings);
//        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().deleteEbooks(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }

    /**
     *  移动电子书到分组
     * @param
     */
    private void ebookMove(String groupsid,String ebookid){
        HttpRxObserver httpRxObserver = new HttpRxObserver<Result>("ebookMove") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                Log.i("READ", "onError: e = "+e);
                showToast("移动电子书失败");
            }

            @Override
            protected void onSuccess(Result response) {
                Log.i("READ", "onSuccess:创建成功 ");
                showToast("移动电子书成功");
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("id",groupsid);
        map.put("ebook_id",ebookid);
//        map = AppSign.buildMap(map);
        Observable<Result> observable = NetUtils.getGsonRetrofit().ebookMove(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
    /**
     *  获取章节列表
     * @param
     */
    public  void getEbookChapter(String ebookid){
        HttpRxObserver httpRxObserver = new HttpRxObserver<BookChapter>("getEbookChapter") {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                Log.i("READ", "onError: e = "+e);
                showToast("获取章节失败");
            }

            @Override
            protected void onSuccess(BookChapter response) {
                Log.i("READ", "onSuccess:getEbookChapter  ======== response ===");
                showToast("获取章节成功");
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map.put("id",ebookid);
//        map = AppSign.buildMap(map);
//        Observable<BookChapter> observable = NetUtils.getGsonRetrofit().getEbookChapter(map);
//        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }
}
