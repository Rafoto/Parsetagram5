package com.example.parsetagram5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentActivity extends AppCompatActivity {
    Button btnBack;
    Button btnComment;
    EditText editComment;
    Post post;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        context = this;
        post = getIntent().getParcelableExtra("post");
        // Inflate the layout for this fragment
        btnBack = findViewById(R.id.btnBack);
        btnComment = findViewById(R.id.btnComment);
        editComment = findViewById(R.id.editComment);
        editComment.requestFocus();
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject comment = ParseComment.create("ParseComment");
                comment.put("description", editComment.getText().toString());
                comment.put("user", ParseUser.getCurrentUser());
                comment.put("post", post);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        } else {
                            Log.d("CommentFragment", "Success");
                            Intent intent = new Intent(context, TimelineActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }
}
