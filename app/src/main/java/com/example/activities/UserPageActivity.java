package com.example.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.events.UserChangedEvent;
import com.example.fragments.ProductListFragment;
import com.example.models.User;
import com.example.services.Services;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPageActivity extends AppCompatActivity {

    static public final String USER_ID = "user_id";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setHomeButtonEnabled(true);
        }

        Intent data = getIntent();
        final String id = data.getStringExtra(USER_ID);
        if (id == null || id.isEmpty()) {
            Toast.makeText(this, "WTF", Toast.LENGTH_LONG).show();
            finish();
        }
        final TextView goodsCountView = findViewById(R.id.number_of_goods);
        final TextView loginView = findViewById(R.id.user_page_login);

        Services.users.get(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setUser(response.body());
                loginView.setText(user.getLogin());
                Services.products.getAddedBySellerId(user.getLogin()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        goodsCountView.setText(String.valueOf(response.body()));
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });

                TextView userNameView = findViewById(R.id.user_page_name);
                userNameView.setText(user.getName());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserPageActivity.this, "WTF 2", Toast.LENGTH_LONG).show();
                if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    FirebaseAuth.getInstance().signOut();
                }
                finish();
            }
        });



        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", String.valueOf(loginView.getText()),
                        null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
    }


    private void setUser(User user) {
        if (user == null) {
            Toast.makeText(UserPageActivity.this, "WTF 3", Toast.LENGTH_LONG).show();
            return;
        }
        this.user = user;
        getSupportActionBar().setTitle(user.getName());
        if (FirebaseAuth.getInstance().getCurrentUser() == null || !user.getLogin().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            return;
        }

    }

}
