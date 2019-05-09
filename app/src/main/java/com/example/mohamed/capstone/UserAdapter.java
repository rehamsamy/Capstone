package com.example.mohamed.capstone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Holder> {
    Context mContext;
    ArrayList<User> usersList;
     OnClick mItemClick;

    public  UserAdapter(Context context,ArrayList<User> users,OnClick itemClick){

        mContext=context;
        usersList=users;
        mItemClick=itemClick;
    }


    public interface OnClick{
       void getPosition(int position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,viewGroup,false);

        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        User user=usersList.get(position);
        holder.name.setText(user.getName());
        holder.status.setText(user.getStatus());
        Picasso.get().load(user.getImage()).into(holder.imageView);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClick.getPosition(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        Log.v("UserAdapter","size is"+usersList.size());
        return usersList.size();
    }

    public class Holder  extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView status,name;
        RelativeLayout root;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView=(CircleImageView) itemView.findViewById(R.id.image);
            status=(TextView) imageView.findViewById(R.id.user_status);
            name=(TextView) imageView.findViewById(R.id.user_name);

            root=(RelativeLayout) itemView.findViewById(R.id.root);

        }


    }
}