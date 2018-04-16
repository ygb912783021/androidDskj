package dingshi.com.hibook.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.ElectronicCaseAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.eventbus.EventBusHelper;
import dingshi.com.hibook.view.FuckDialog;

public class BookElectronicCaseActivity extends BaseActivity{

    @BindView(R.id.electroic_gridview_book)
    GridView electroic_gridview_book;
    @BindView(R.id.electroi_book_bottom)
    LinearLayout electroi_book_bottom;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    boolean editState = true;
    FuckDialog fuckDialog;
    private ArrayList<String > colltionList = new ArrayList<>();
    private ArrayList<String > stringArrayList = new ArrayList<>();
    ElectronicCaseAdapter electronicCaseAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_electronic_book;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        Constant.isEditDelete=false;
//        EventBusHelper.register(this);
        requestActionBarStyle(true, "电子书柜", "编辑");
//        initData();
        int[] to={R.id.electronic_gridview_img,R.id.electronic_gridview_text};
//        adapter=new SimpleAdapter(this, dataList, R.layout.view_electronic_book_gridview_item, new String[]{"img","text"}
        stringArrayList.add("第一本书");
        stringArrayList.add("第一本书");
        stringArrayList.add("第一本书");
        stringArrayList.add("第一本书");
        stringArrayList.add("第一本书");
        stringArrayList.add("第一本书");
        electronicCaseAdapter  = new ElectronicCaseAdapter(this,stringArrayList);
        electroic_gridview_book.setAdapter(electronicCaseAdapter);
    }
    /**
     * 数据获取(假数据)
     */
    void initData() {
        //图标
        int icno[] = { R.drawable.books, R.drawable.books,
                R.drawable.books, R.drawable.books
                , R.drawable.books, R.drawable.books,
                R.drawable.books, R.drawable.books,
                R.drawable.books, R.drawable.books,
                R.drawable.books, R.drawable.books,
                R.drawable.books, R.drawable.books,

        };
        //图标下的文字
        String name[]={"第一个","第二个","第三个","第四个","第五个","第六个","第七个","第八个","第九个","第十个","第九个","第十个","第九个","第十个"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);
        }
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        if (editState){
            electroi_book_bottom.setVisibility(View.VISIBLE);
            editState = false;
            electronicCaseAdapter.setState();
            electronicCaseAdapter.notifyDataSetChanged();
        }else {
            electroi_book_bottom.setVisibility(View.GONE);
            editState = true;
            electronicCaseAdapter.setState();
            electronicCaseAdapter.notifyDataSetChanged();
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
                        fuckDialog.dissmis();
                    }
                });
                break;
            case R.id.electroi_book_yidao:
                showCollection();
                break;
        }

    }
    private void showCollection(){
        colltionList.add("萨克的脸开始大量");
        colltionList.add("萨克的脸开始大量");
        colltionList.add("萨克的脸开始大量");
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
        MyCollectionAdapter myCollectionAdapter = new MyCollectionAdapter(this);
        electroi_list_collection.setAdapter(myCollectionAdapter);
    }
    private void showCollectionAdd(){
        View viewb = LayoutInflater.from(this).inflate(R.layout.view_dialog_electronic_collection_add, null, false);
        fuckDialog = new FuckDialog(this).addView(viewb).builder();
        fuckDialog.show();
        viewb.findViewById(R.id.electroi_book_collection_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fuckDialog.dissmis();
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
            return colltionList.size()>0?colltionList.size():0;
        }
        @Override
        public Object getItem(int i) {
            return colltionList.size()>0?colltionList.get(i):null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.view_item_collection, null);
            return view;
        }
    }

}
