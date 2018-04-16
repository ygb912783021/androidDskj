package dingshi.com.hibook.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dingshi.com.hibook.R;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.ui.FriendvalidationActivity;

/**
 * 新用户界面的adapter
 *
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>{
    private List<String> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    public UserListAdapter(Context context, List<String> datas){
        this. mContext=context;
        this. mDatas=datas;
        inflater=LayoutInflater. from(mContext);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.user_list_item  ,parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        holder.userAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, FriendvalidationActivity.class);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.userPhoto
//        holder.userName
//        holder.userAdd
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView userPhoto;
        TextView userName;
        Button userAdd;

        public MyViewHolder(View view) {
            super(view);
            userPhoto=view.findViewById(R.id.user_list_item_photo);
            userName=view.findViewById(R.id.user_list_item_name);
            userAdd=view.findViewById(R.id.user_list_item_add);
        }

    }
}
