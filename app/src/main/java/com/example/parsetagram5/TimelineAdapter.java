package com.example.parsetagram5;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {
    ViewHolder viewHolder;
    private List<Post> posts;
    private Context context;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // get the data according to position
        final List<Like> likes = new ArrayList<>();
        final Post post = posts.get(position);
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvCaption.setText(post.getDescription());
        holder.tvDate.setText(post.getDate());
        Glide.with(context).load(post.getImage().getUrl()).into(holder.ivImage);
        ParseQuery<Like> query = new ParseQuery<Like>(Like.class);
        query.whereEqualTo(Like.POST, post);
        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> newLikes, ParseException e) {
                if (e != null) {
                    Log.e("DetailActivity", e.toString());
                } else {
                    Glide.with(holder.ivLike.getContext()).load(R.drawable.ufi_heart).into(holder.ivLike);
                    for (int i = 0; i < newLikes.size(); i++) {
                        if (newLikes.get(i).getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            Glide.with(holder.ivLike.getContext()).load(R.drawable.ufi_heart_active).into(holder.ivLike);
                        }
                    }
                    likes.addAll(newLikes);
                }
            }
        });
        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < likes.size(); i++) {
                    if (likes.get(i).getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        likes.get(i).deleteInBackground();
                        updateLikes(post, likes, holder.ivLike);
                        return;
                    }
                }
                Like like = new Like();
                like.setUser(ParseUser.getCurrentUser());
                like.setPost(post);
                like.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        } else {
                            Log.d("DETAIL_ACTIVITY", "Success");
                            updateLikes(post, likes, holder.ivLike);
                        }
                    }
                });
            }
        });


        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);
            }
        });
        if (post.getUser().get("profilePicture") != null) {
            Glide.with(context).load(((ParseFile) (post.getUser().get("profilePicture"))).getUrl()).apply(RequestOptions.circleCropTransform()).into(holder.ivUsericon);
        }
        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public void updateLikes(Post post, final List<Like> likes, final ImageView ivLike) {
        ParseQuery<Like> query = new ParseQuery<Like>(Like.class);
        query.whereEqualTo(Like.POST, post);

        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> newLikes, ParseException e) {
                if (e != null) {
                    Log.e("DetailActivity", e.toString());
                } else {
                    Glide.with(ivLike.getContext()).load(R.drawable.ufi_heart).into(ivLike);
                    for (int i = 0; i < newLikes.size(); i++) {
                        if (newLikes.get(i).getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            Glide.with(ivLike.getContext()).load(R.drawable.ufi_heart_active).into(ivLike);
                        }
                    }
                    likes.removeAll(likes);
                    likes.addAll(newLikes);
                }

            }
        });

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
