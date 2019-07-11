package com.example.parsetagram5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    List<Like> likes;
    Toolbar toolbar;

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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setComments(post);
        likes = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        adapter = new CommentAdapter(comments);
        rvComments.setAdapter(adapter);
        Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        if (post.getUser().get("profilePicture") != null) {
            Glide.with(this).load(((ParseFile) (post.getUser().get("profilePicture"))).getUrl()).into(ivUsericon);
        }
        updateLikes();
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < likes.size(); i++) {
                    if (((ParseUser) likes.get(i).getUser()).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        likes.get(i).deleteInBackground();
                        updateLikes();
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
                            updateLikes();
                        }
                    }
                });
            }
        });
    }

    public void updateLikes() {
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
                        if (((ParseUser) newLikes.get(i).getUser()).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            Glide.with(ivLike.getContext()).load(R.drawable.ufi_heart_active).into(ivLike);
                        }
                    }
                    tvLikes.setText(String.valueOf(newLikes.size()));
                    likes = newLikes;
                }
            }
        });

    }
    public void onLogOutClick(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_small_menu, menu);
        return true;
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
                                                   tvComments.setText(String.valueOf(comments.size()));
                                                   adapter.notifyDataSetChanged();
                                               }
                                           }
                                       });
                                   }


    }

