package com.example.parsetagram5;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder>{
    private List<Post> posts;
    private Context context;
    ViewHolder viewHolder;

    public TimelineAdapter(List<Post> posts) {
        this.posts = posts;

    }
    @NonNull
    @Override
    public TimelineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.post_layout, parent, false);
        viewHolder = new ViewHolder(tweetView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to position
        final Post post = posts.get(position);
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvCaption.setText(post.getDescription());
        holder.tvDate.setText(post.getDate());
        Glide.with(context).load(post.getImage().getUrl()).into(holder.ivImage);
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);
            }
        });
        if (post.getUser().get("profilePicture") != null) {
            Glide.with(context).load(((ParseFile) (post.getUser().get("profilePicture"))).getUrl()).into(holder.ivUsericon);
        }


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivComment;
        public ImageView ivImage;
        public ImageView ivLike;
        public ImageView ivUsericon;
        public TextView tvUsername;
        public TextView tvDate;
        public TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);
            ivComment = itemView.findViewById(R.id.ivComment);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivUsericon = itemView.findViewById(R.id.ivUsericon);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvDate = itemView.findViewById(R.id.tvDate);

        }




    }

}
