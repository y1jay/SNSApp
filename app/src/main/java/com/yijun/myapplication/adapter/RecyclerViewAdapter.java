package com.yijun.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yijun.myapplication.R;
import com.yijun.myapplication.model.Post;
import com.yijun.myapplication.model.Row;
import com.yijun.myapplication.utils.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Row> postArrayList;

    public RecyclerViewAdapter(Context context, List<Row> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Row post = postArrayList.get(position);


        holder.txtPosting.setText(post.getPosting());
        holder.txtDate.setText(post.getCreatedAt());

        String url = Utils.BASE_URL + "/uploads/"+ post.getPhotoUrl();
        Glide.with(context).load(url).into(holder.imgPhoto);


    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPhoto;
        TextView txtPosting;
        TextView txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtPosting = itemView.findViewById(R.id.txtPosting);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
