package dingshi.com.hibook.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.hw.txtreaderlib.ui.HwTxtPlayActivity;

import com.example.administrator.readbook.BitIntentDataManager;
import com.example.administrator.readbook.bean.BookShelfBean;
import com.example.administrator.readbook.dao.BookShelfBeanDao;
import com.example.administrator.readbook.dao.DbHelper;
import com.example.administrator.readbook.presenter.impl.ReadBookPresenterImpl;
import com.example.administrator.readbook.view.impl.MainActivity;
import com.example.administrator.readbook.view.impl.ReadBookActivity;

import java.util.ArrayList;
import java.util.List;

import dingshi.com.hibook.R;
import dingshi.com.hibook.bean.BookShelf;
import dingshi.com.hibook.bean.EbookGratis;
import dingshi.com.hibook.bean.EbookList;
import dingshi.com.hibook.ui.BookReadActivity;

public class ElectronicCaseAdapter extends BaseAdapter {
    Context context;
    List<EbookList> arrayList = new ArrayList<>();
    private boolean state = false;
    int groudSize = 0;
    List<String> stringList = new ArrayList<>();
    public ElectronicCaseAdapter(Context context, List<BookShelf.JsonDataBean.Ebook_group > groups,List<EbookList > arrayList) {
        this.context = context;
        Log.i("READ", "ElectronicCaseAdapter: arrayList = "+arrayList.size()+"       groups = "+groups.size());
        groudSize = groups.size();
        if (groups.size()>0){
            for (int i = 0 ;i<groups.size();i++){
                EbookList ebook = new EbookList();
                ebook.setBook_id(groups.get(i).getId());
                ebook.setName(groups.get(i).getTag());
                this.arrayList.add(ebook);
            }
        }else {

        }
        this.arrayList.addAll(arrayList);
        EbookList ebook = new EbookList();
        ebook.setBook_id(0);
        ebook.setName("");
        this.arrayList.add(ebook);
//        EbookList ebookList = new EbookList();
//        this.arrayList.add(ebookList);
    }
    @Override
    public int getCount() {
        return arrayList.size()>0?arrayList.size():0;
    }

    @Override
    public Object getItem(int i) {
        return arrayList.size()>0?arrayList.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.view_electronic_book_gridview_item, null);
        final CheckBox checkBox = view.findViewById(R.id.electroic_gridview_checkBox);
        RelativeLayout electronic_gridview_img = view.findViewById(R.id.electronic_gridview_img);
        LinearLayout electroic_gridview_duwan = view.findViewById(R.id.electroic_gridview_duwan);
        TextView electronic_gridview_text = view.findViewById(R.id.electronic_gridview_text);
        if (groudSize>0){
            if (i<=groudSize-1){
                electroic_gridview_duwan.setVisibility(View.GONE);
            }
        }
        if (state){
            checkBox.setVisibility(View.VISIBLE);
        }else {
            checkBox.setVisibility(View.GONE);
        }
        if ((arrayList.size()-1)==i){
            electroic_gridview_duwan.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            electronic_gridview_img.setBackgroundResource(R.drawable.addbook);
            electronic_gridview_text.setText("");
        }else {

                electronic_gridview_text.setText(arrayList.get(i).getName());
        }

//        Log.i("READ", "getView: arrayList.get(i).getBookInfoBean().getName() =====   "+arrayList.get(i).getBookInfoBean().getName());
        electronic_gridview_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state){
                    if (checkBox.isChecked()){
                        Log.i("READ", "onClick: 我第一次点击");
                        checkBox.setChecked(false);
                        for (int j = 0;j<stringList.size();j++){
                            if (stringList.get(j).equals(arrayList.get(i).getBook_id()+"")){
                                stringList.remove(j);
                            }
                        }
                    }else {
                        checkBox.setChecked(true);
                        stringList.add(arrayList.get(i).getBook_id()+"");
                        Log.i("READ", "onClick: 我第二次点击");
                    }

                }else {
                    if ((arrayList.size()-1) > i){
//                    context.startActivity(new Intent(context, BookReadActivity.class));
//                        HwTxtPlayActivity.loadTxtFile(context,"/storage/emulated/0/最强新手剑.txt");
//                        context.startActivity(new Intent(context,M) );
//                        context.startActivity(new Intent(context, MainActivity.class));
//                        List<BookShelfBean> bookShelfes = DbHelper.getInstance(context).getmDaoSession().getBookShelfBeanDao().queryBuilder().orderDesc(BookShelfBeanDao.Properties.FinalDate).list();
                        Log.i("READ", "onClick: bookShelfes = "+arrayList.size());
                        /**
                        BookShelfBean bookShelfBean = arrayList.get(i);
                        Log.i("READ", "onClick: bookShelfBean    getNoteUrl   = "+bookShelfBean.getNoteUrl());
                        Log.i("READ", "onClick: bookShelfBean    getTag   = "+bookShelfBean.getTag());
                        Log.i("READ", "onClick: bookShelfBean    getBookInfoBean   = "+bookShelfBean.getBookInfoBean().getName());
                        Log.i("READ", "onClick: bookShelfBean    getFinalDate   = "+bookShelfBean.getFinalDate());
                        Log.i("READ", "onClick: bookShelfBean    getDurChapter   getDurChapter   = "+arrayList.get(i).getDurChapter()+"     getDurChapterPage = "+arrayList.get(i).getDurChapterPage());
                        Intent intent = new Intent(context, ReadBookActivity.class);
                        intent.putExtra("from", ReadBookPresenterImpl.OPEN_FROM_APP);
                        String key = String.valueOf(System.currentTimeMillis());
                        Log.i("READ", "onClick: key  == "+key);
                        intent.putExtra("data_key", key);
                        try {
                            BitIntentDataManager.getInstance().putData(key, bookShelfBean.clone());
                        } catch (CloneNotSupportedException e) {
                            BitIntentDataManager.getInstance().putData(key, bookShelfBean);
                            e.printStackTrace();
                        }
                        context.startActivity(intent);
                         */
                    }
                }
            }
        });
        return view;
    }
    public void setState(){
        if (state){
            state = false;
        }else {
            state = true;
        }
    }
    public String  getStringEbooID(){
        String  stringEbooID = "";
        if (stringList.size()>0) {
            for (int d = 0; d < stringList.size(); d++) {
                stringEbooID= stringEbooID+stringList.get(d)+",";
            }
        }
        return stringEbooID;
    }
}
