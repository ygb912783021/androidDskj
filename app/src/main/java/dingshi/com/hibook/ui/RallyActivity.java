package dingshi.com.hibook.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.lib.ClubList;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.library.CreateLibActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.AbsExpandableListView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi
 * @since 2018/1/30 下午4:30
 */

public class RallyActivity extends BaseActivity {

    @BindView(R.id.rally_expand)
    ExpandableListView expandleList;


    List<ClubList.JsonDataBean> groupList = new ArrayList<>();

    MyAdapter myAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_rally;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "官方书友会", "创建");
        myAdapter = new MyAdapter();
        expandleList.setAdapter(myAdapter);
        pullNet();
    }


    @OnClick({R.id.search_content})
    public void onClick(View view) {
        startActivity(new Intent(this, RallySearchActivity.class));
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        Intent intent = new Intent(this, CreateLibActivity.class);
        intent.putExtra("isRally", true);
        startActivity(intent);
        finish();
    }

    private void pullNet() {
        HttpRxObserver httpRxObserver = new HttpRxObserver<ClubList>("clubList") {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {

            }

            @Override
            protected void onSuccess(ClubList response) {
                if (response.getJsonData() != null) {
                    groupList.addAll(response.getJsonData());
                    myAdapter.notifyDataSetChanged();
                    expandleList.expandGroup(getIntent().getIntExtra("index", 0));
                }
            }
        };
        HashMap<String, String> map = new HashMap<>(2);
        map = AppSign.buildMap(map);
        Observable<ClubList> observable = NetUtils.getGsonRetrofit().clubList(map);
        HttpRxObservable.getObservable(observable, this, ActivityEvent.PAUSE).subscribe(httpRxObserver);
    }


    private class MyAdapter extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groupList.get(groupPosition).getData().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(RallyActivity.this).inflate(R.layout.view_item_rally_group, parent, false);
                holder.jump = convertView.findViewById(R.id.item_rally_group_jump);
                holder.title = convertView.findViewById(R.id.item_rally_group_title);
                holder.icon = convertView.findViewById(R.id.item_rally_group_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (isExpanded) {
                holder.jump.setImageResource(R.drawable.jump_down);
            } else {
                holder.jump.setImageResource(R.drawable.jump);
            }
            holder.title.setText(String.format("%s(%s)", groupList.get(groupPosition).getType_name(), groupList.get(groupPosition).getTotal()));


            if (groupPosition == 0) {
                holder.icon.setImageResource(R.drawable.rally_city);
            } else if (groupPosition == 1) {
                holder.icon.setImageResource(R.drawable.rally_college);
            } else if (groupPosition == 2) {
                holder.icon.setImageResource(R.drawable.rally_country);
            }


            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(RallyActivity.this).inflate(R.layout.view_item_rally_chidren, null, false);
                holder.title = convertView.findViewById(R.id.item_rally_child_title);
                holder.joinNums = convertView.findViewById(R.id.item_rally_child_authen);
                holder.icon = convertView.findViewById(R.id.item_rally_child_photo);
                holder.time = convertView.findViewById(R.id.item_rally_child_time);
                holder.more = convertView.findViewById(R.id.item_rally_child_more);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ClubList.JsonDataBean.DataBean dataBean = groupList.get(groupPosition).getData().get(childPosition);
            holder.title.setText(dataBean.getName());
            GlideUtils.loadCircleImage(RallyActivity.this, dataBean.getIcon(), R.drawable.logo, holder.icon);

            holder.joinNums.setText(dataBean.getPerson_num() + "人加入");

            holder.time.setText(dataBean.getCreated_at());


            if (childPosition == groupList.get(groupPosition).getData().size() - 1) {
                holder.more.setVisibility(View.VISIBLE);
            } else {
                holder.more.setVisibility(View.GONE);
            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(RallyActivity.this, RallyDetailsActivity.class);
                    intent.putExtra("catalog_id", dataBean.getCatalog_id());
                    intent.putExtra("pic", dataBean.getIcon());
                    startActivity(intent);
                }
            });

            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RallyActivity.this, RallySearchActivity.class);
                    intent.putExtra("type", groupPosition + 6);
                    Log.i("type", (groupPosition + 6) + "------");
                    startActivity(intent);

                }
            });
            return convertView;
        }

    }

    private class ViewHolder {
        ImageView icon;
        TextView title;
        ImageView jump;
        TextView joinNums;
        TextView time;
        TextView more;
    }
}
