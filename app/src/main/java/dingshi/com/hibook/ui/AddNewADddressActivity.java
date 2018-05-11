package dingshi.com.hibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import io.reactivex.internal.schedulers.NewThreadWorker;

public class AddNewADddressActivity extends BaseActivity {
    @BindView(R.id.add_newadd_choose)
    TextView chooseCity;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_new_address;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true,"新增收货地址","保存");

        chooseCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddNewADddressActivity.this, CityActivity.class);

                startActivity(intent);
            }
        });


    }


//    @OnClick({R.id.add_newadd_savebtn})
//    public void onClick(View view){
//        //保存新建
//    }

}
