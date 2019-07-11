package com.example.parsetagram5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    CommentAdapter.ViewHolder viewHolder;
    private List<ParseObject> comments;
    private Context context;

    public CommentAdapter(List<ParseObject> comments) {
        this.comments = comments;

    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.comment_layout, parent, false);
        viewHolder = new CommentAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        // get the data according to position

        final ParseObject comment = comments.get(position);
        holder.tvUsername.setText(((ParseObject) comment.get("user")).getString("username"));
        holder.tvCaption.setText(comment.getString("description"));
        holder.tvDate.setText(comment.getCreatedAt().toString());
        if (((ParseUser) comment.get("user")).get("profilePicture") != null) {
            Glide.with(context).load(((ParseFile) (((ParseUser) comment.get("user")).get("profilePicture"))).getUrl()).apply(RequestOptions.circleCropTransform()).into(holder.ivUsericon);
        }

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivUsericon;
        public TextView tvUsername;
        public TextView tvDate;
        public TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);
            ivUsericon = itemView.findViewById(R.id.ivUsericon);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvDate = itemView.findViewById(R.id.tvDate);

        }

    }
}
