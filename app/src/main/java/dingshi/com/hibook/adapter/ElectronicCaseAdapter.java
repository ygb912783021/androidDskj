package dingshi.com.hibook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import dingshi.com.hibook.R;
import dingshi.com.hibook.ui.BookReadActivity;

public class ElectronicCaseAdapter extends BaseAdapter {
    Context context;
    ArrayList<String > arrayList;
    private boolean state = false;
    public ElectronicCaseAdapter(Context context, ArrayList<String > arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayList.add(" ");
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
        CheckBox checkBox = view.findViewById(R.id.electroic_gridview_checkBox);
        RelativeLayout electronic_gridview_img = view.findViewById(R.id.electronic_gridview_img);
        LinearLayout electroic_gridview_duwan = view.findViewById(R.id.electroic_gridview_duwan);
        if (state){
            checkBox.setVisibility(View.VISIBLE);
        }else {
            checkBox.setVisibility(View.GONE);
        }
        if ((arrayList.size()-1)==i){
            electroic_gridview_duwan.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            electronic_gridview_img.setBackgroundResource(R.drawable.addbook);
        }

        electronic_gridview_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state){

                }else {
                    if ((arrayList.size()-1) > i){
                    context.startActivity(new Intent(context, BookReadActivity.class));
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
}
