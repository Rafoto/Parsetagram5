package com.example.parsetagram5;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

public class DetailActivity extends AppCompatActivity {
    public ImageView ivComment;
    public ImageView ivImage;
    public ImageView ivLike;
    public ImageView ivUsericon;
    public TextView tvUsername;
    public TextView tvDate;
    public TextView tvCaption;
    public TextView tvLikes;
    public TextView tvComments;
    public RecyclerView rvComments;
    Post post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);
        ivComment = findViewById(R.id.ivComment);
        ivImage = findViewById(R.id.ivImage);
        ivLike = findViewById(R.id.ivLike);
        ivUsericon = findViewById(R.id.ivUsericon);
        tvUsername = findViewById(R.id.tvUsername);
        tvCaption = findViewById(R.id.tvCaption);
        tvDate = findViewById(R.id.tvDate);
        tvLikes = findViewById(R.id.tvLikes);
        tvComments = findViewById(R.id.tvComments);
        rvComments = findViewById(R.id.rvComments);
        post = getIntent().getParcelableExtra("post");
        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        tvDate.setText(post.getDate());
        Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        if (post.getUser().get("profilePicture") != null) {
            Glide.with(this).load(((ParseFile) (post.getUser().get("profilePicture"))).getUrl()).into(ivUsericon);
        }

    }
}
