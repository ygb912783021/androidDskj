package dingshi.com.hibook.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Arrays;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;

public class AddressManagerActivity extends BaseActivity {
    @BindView(R.id.address_manager_recycle)
    RecyclerView recyclerView;
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true,"管理收货地址","确认");

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        FuckYouAdapter fuckYouAdapter=new FuckYouAdapter(R.layout.view_address_manager_item, Arrays.asList("","","",""));
        recyclerView.setAdapter(fuckYouAdapter);
        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_manager;
    }
}
