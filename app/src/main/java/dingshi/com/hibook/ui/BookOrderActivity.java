package dingshi.com.hibook.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Arrays;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;

/**
 * @author wangqi
 */
public class BookOrderActivity extends BaseActivity {

    @BindView(R.id.book_order_recycle)
    RecyclerView recyclerView;

    FuckYouAdapter fuckYouAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_order;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fuckYouAdapter = new FuckYouAdapter<String>(R.layout.view_book_order_item, Arrays.asList("1", "", "", "", "", "", "", "", ""));
        recyclerView.setAdapter(fuckYouAdapter);

        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                RecyclerView recyclerView = helper.getView(R.id.item_book_order_recycle);
                recycleHolder(recyclerView);
            }
        });
    }


    /**
     * 书柜列表
     *
     * @param recycle
     */
    public void recycleHolder(RecyclerView recycle) {
        recycle.setLayoutManager(new GridLayoutManager(this, 4));
        FuckYouAdapter adapter = new FuckYouAdapter<String>(R.layout.view_distance_item, Arrays.asList("", "", "", ""));
        recycle.setAdapter(adapter);
    }
}
