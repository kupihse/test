package com.example.s1k0de.entry;

/**
 * Created by Eugeen on 30/09/2017.
 * Форма регистрации
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.example.models.User;
import com.example.services.Services;
import com.example.application.R;

import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationFormActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set View to register.xml
        setContentView(R.layout.activity_registration);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Получаем экземпляр элемента Spinner
        final Spinner spinner = (Spinner)findViewById(R.id.emailList);

        // Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.emailAddition,R.layout.simple_spinner_item_custom);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Вызываем адаптер
        spinner.setAdapter(adapter);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }

        });
        final Button button = findViewById(R.id.btnRegister);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                //************ берем поля ************

                EditText logint,namet,passwordt,passwordt1;
                logint = (EditText) findViewById(R.id.reg_email);
                String login=logint.getText().toString();
                logint = null;
                namet = (EditText) findViewById(R.id.reg_fullname);
                String name = namet.getText().toString();
                namet = null;
                passwordt = (EditText) findViewById(R.id.reg_password);
                String password = passwordt.getText().toString();
                passwordt = null;
                passwordt1 = (EditText) findViewById(R.id.reg_password1);
                String password1= passwordt1.getText().toString();
                passwordt1=null;

                //*******************************************************
                //**************** Проверка на корректные поля **************
                if(!Regular.doMatch(login) && !login.equals("") && !name.equals("") && !password.equals("") ) {

                    //************* сравнивает пароли,если не совпадают, то нотификейшн ***********
                    if (!password.equals(password1)) {
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegistrationFormActivity.this);

                        View mView = getLayoutInflater().inflate(R.layout.activity_popupwindow, null);
                        Button button_popup = mView.findViewById(R.id.button_popup);
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();
                        button_popup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                    //********************** Иначе готовим и передаем юзера *********************************
                    else {
                        //берем проверенное поле логина и конкетинируем с строкой из спинера, логгер временный
                        Spinner spinner = (Spinner) findViewById(R.id.emailList);
                        String selected = spinner.getSelectedItem().toString();
                        login += "@" + selected;
                        selected = null;
                        android.util.Log.d("mylog ", login);
                        //получено готовое поле login

                        //Далее проводим регистрацию
                        Call<Void> c = Services.users.add(new User(name, login, password));
                        c.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
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
                //******************************************************************************

                else {
                    final AlertDialog.Builder mBuilderr = new AlertDialog.Builder(RegistrationFormActivity.this);

                    View mView1 = getLayoutInflater().inflate(R.layout.activity_popuwindow1, null);
                    Button button_popup = mView1.findViewById(R.id.button_popup);
                    mBuilderr.setView(mView1);
                    final AlertDialog dialog1 = mBuilderr.create();
                    dialog1.show();
                    button_popup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });
                }
            }
        });
    }
}