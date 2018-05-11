package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Arrays;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;

public class AddressManagerActivity extends BaseActivity {
    @BindView(R.id.address_manager_recycle)
    RecyclerView recyclerView;

  //  @BindView(R.id.receive_address_manager)
    @Override
    public int getLayoutId() {
        return R.layout.activity_address_manager;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true,"管理收货地址","确认");

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        FuckYouAdapter fuckYouAdapter=new FuckYouAdapter(R.layout.view_address_manager_item, Arrays.asList("","","",""));
        recyclerView.setAdapter(fuckYouAdapter);
        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
//                helper.setText();
////                helper.setText();
////                helper.setText();
////                helper.setText();
                helper.getView(R.id.address_manager_defaultbtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //设置默认地址
                    }
                });
                helper.getView(R.id.address_manager_edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //编辑地址
                        Intent intent=new Intent(AddressManagerActivity.this,AddNewADddressActivity.class);
                        startActivity(intent);
                    }
                });
                helper.getView(R.id.address_manager_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //删除地址
                    }
                });

        }
        });
    }


}
