package com.example.parsetagram5;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

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
    public CommentAdapter adapter;
    Post post;
    List<ParseObject> comments;

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
        comments = new ArrayList<ParseObject>();
        setComments(post);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        adapter = new CommentAdapter(comments);
        rvComments.setAdapter(adapter);
        Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        if (post.getUser().get("profilePicture") != null) {
            Glide.with(this).load(((ParseFile) (post.getUser().get("profilePicture"))).getUrl()).into(ivUsericon);
        }

    }

    public void setComments(Post post) {
        ParseQuery<ParseComment> postQuery = new ParseQuery<ParseComment>(ParseComment.class);
        postQuery.include(ParseComment.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder("createdAt");
        postQuery.whereEqualTo(ParseComment.POST, post);
        postQuery.findInBackground(new FindCallback<ParseComment>() {
                                       @Override
                                       public void done(List<ParseComment> newParseComments, ParseException e) {
                                           if (e != null) {
                                               Log.e("DetailActivity", e.toString());
                                           } else {
                                                   comments.addAll(newParseComments);
                                                   adapter.notifyDataSetChanged();
                                               }
                                           }
                                       });
                                   }


    }

