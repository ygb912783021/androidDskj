package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import io.reactivex.internal.schedulers.NewThreadWorker;

public class ReceiviaddressActivity extends BaseActivity{
    @BindView(R.id.receive_address_addtext)
    TextView addAddress;
    @BindView(R.id.receive_address_recycle)
    RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_receive_address;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true,"选择收货地址","管理");
        addAddress.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        FuckYouAdapter fuckYouAdapter= new FuckYouAdapter(R.layout.view_receive_address_item, Arrays.asList("","","","",""));
         recyclerView.setAdapter(fuckYouAdapter);
        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                TextView name=helper.getView(R.id.view_receive_address_name);
                TextView tel=helper.getView(R.id.view_receive_address_tel);
                TextView defaultadd=helper.getView(R.id.view_receive_address_default);
                TextView address=helper.getView(R.id.view_receive_address_address);
                defaultadd.setText("");
            }
        });
        fuckYouAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    @OnClick({R.id.receive_new_address})
    public void onClick(View v){
        Intent intent=new Intent(this,AddNewADddressActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        Intent intent=new Intent(this,AddressManagerActivity.class);
        startActivity(intent);
    }
}
