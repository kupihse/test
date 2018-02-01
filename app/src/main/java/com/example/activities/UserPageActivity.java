package com.example.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.application.R;
import com.example.models.User;
import com.example.storages.CurrentUser;

public class UserPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);



        User user = CurrentUser.user();
        String tkn = CurrentUser.token();

        TextView login = (TextView) findViewById(R.id.user_page_login);
        login.setText("login: \n"+user.getLogin());

        TextView pass = (TextView) findViewById(R.id.user_page_pass);
        pass.setText("pass: \n" + user.getPassword());

        TextView token = (TextView) findViewById(R.id.user_page_token);
        token.setText("token: \n" + tkn);


        Button logOut = (Button) findViewById(R.id.user_page_log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentUser.user(null);
                CurrentUser.token(null);
                Intent returnIntent = new Intent();
                setResult(UserPageActivity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
