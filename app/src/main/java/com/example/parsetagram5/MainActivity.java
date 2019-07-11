package com.example.parsetagram5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    TextView tvUsername;
    TextView tvPassword;
    Button btnLogin;
    Button btnSignup;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, TimelineActivity.class);
            intent.putExtra("Username", currentUser.getUsername());
            this.startActivity(intent);
        } else {
            context = this;
            showLoginScreen();
        }
    }

    private void showLoginScreen() {
        setContentView(R.layout.activity_main);
        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logInInBackground(tvUsername.getText().toString(), tvPassword.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent intent = new Intent(context, TimelineActivity.class);
                            intent.putExtra("Username", tvUsername.getText());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             ParseUser user = new ParseUser();
                                             user.setUsername(tvUsername.getText().toString());
                                             user.setPassword(tvPassword.getText().toString());
                                             user.signUpInBackground(new SignUpCallback() {
                                                 public void done(ParseException e) {
                                                     if (e == null) {
                                                         Intent intent = new Intent(context, TimelineActivity.class);
                                                         intent.putExtra("Username", tvUsername.getText());
                                                         context.startActivity(intent);
                                                     } else {
                                                         Toast.makeText(context, "Sign Up Failed: " + e.toString(), Toast.LENGTH_LONG).show();
                                                     }
                                                 }
                                             });

                                         }
                                     }
        );
    }

}
