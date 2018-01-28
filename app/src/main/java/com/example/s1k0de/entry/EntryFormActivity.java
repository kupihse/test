package com.example.s1k0de.entry;

/**
 * Created by Eugeen on 28/09/2017.
 * Форма входа
 */
import android.app.Activity;
import android.os.Bundle;

import com.example.Services.Services;
import com.example.application.R;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.ProgressBar;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

public class EntryFormActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.activity_entryform);

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegistrationFormActivity.class);
                startActivity(i);
            }
        });
/*      Прогресс бар не нужен, все происходит слишком быстро
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        Circle doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce); */

        final Button buttonlog = findViewById(R.id.loginButton);
        buttonlog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //progressBar.setVisibility(ProgressBar.VISIBLE);

                // Берем поля
                EditText logint, passwordt;
                logint = (EditText) findViewById(R.id.emailspace);
                String login = logint.getText().toString();
                logint=null;
                passwordt = (EditText) findViewById(R.id.passwordspace);
                String password = passwordt.getText().toString();
                passwordt=null;

                //*******************************************************
                //**************** Проверка на пустые поля **************
                if ((!login.equals("")) && (!password.equals(""))) {
                    Call<Void> c = Services.users.log(new User(login,password));
                    c.enqueue(new Callback<Void>(){
                        @Override
                        public void onResponse(Call<Void>call, Response<Void> response) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            setResult(EntryFormActivity.RESULT_OK, returnIntent);
                            finish();
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            setResult(EntryFormActivity.RESULT_OK, returnIntent);
                            finish();
                        }
                    });
                }
            }
            //progressBar.setVisibility(ProgressBar.INVISIBLE);
        });
    }
}
