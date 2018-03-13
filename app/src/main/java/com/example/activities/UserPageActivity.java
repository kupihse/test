package com.example.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.models.Product;
import com.example.models.User;
import com.example.services.Services;
import com.example.storages.CurrentUser;

import org.w3c.dom.Text;

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
        Services.users.get(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setUser(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserPageActivity.this, "WTF 2", Toast.LENGTH_LONG).show();
                if (id.equals(CurrentUser.getLogin())) {
                    CurrentUser.logOut();
                }
                finish();
            }
        });

        final TextView login = (TextView) findViewById(R.id.user_page_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", String.valueOf(login.getText().subSequence(8, login.getText().length())),
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
        final TextView login = (TextView) findViewById(R.id.user_page_login);
        login.setText("login: \n"+user.getLogin());

        TextView pass = (TextView) findViewById(R.id.user_page_pass);
        pass.setText("pass: \n" + user.getPassword());

        TextView token = (TextView) findViewById(R.id.user_page_name);
        token.setText("Name: \n" + user.getName());
        String currentUserLogin = CurrentUser.getLogin();
        if (!CurrentUser.isSet() || !user.getLogin().equals(currentUserLogin)){
            return;
        }

        Button logOut = (Button) findViewById(R.id.user_page_log_out);
        logOut.setVisibility(View.VISIBLE);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentUser.logOut();
                Intent returnIntent = new Intent();
                setResult(UserPageActivity.RESULT_OK, returnIntent);
                finish();
            }
        });


        Button myProducts = (Button) findViewById(R.id.user_page_products);
        myProducts.setVisibility(View.VISIBLE);

    }


    public void myProductsButton(View v) {
        for (String p: user.getProducts()) {
            Toast.makeText(UserPageActivity.this, p, Toast.LENGTH_SHORT).show();
        }
    }

}
