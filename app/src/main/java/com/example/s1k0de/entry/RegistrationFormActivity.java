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

import com.example.application.R;
import org.json.JSONException;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.util.Log.*;  //<----- логер не убирать

import org.json.JSONObject;



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
                        login+="@"+selected;
                        selected = null;
                        android.util.Log.d("mylog ", login);
                        //получено готовое поле login
                        User s = new User(name, login, password);
//                        RequestQueue queuek = Volley.newRequestQueue(RegistrationFormActivity.this.getApplicationContext());
//                        String url = "http://51.15.92.91/user/new";
//                        JSONObject o = new JSONObject();
//                         обрабатываем так как Exception нужно либо обработать либо указать наверху через throws
//                         второй вариант не работает в данном случае, сделал через первый, замена на рантайм
//                        try {
//                            o.put("name", name);
//                            o.put("login", login);
//                            o.put("password",password);
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, o,
//                                new Response.Listener<JSONObject>() {
//                                    @Override
//                                    public void onResponse(JSONObject response) {
//                                        VolleyLog.v("Got resp", response);
//                                    }
//                                }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                VolleyLog.e("Error: ", error.getMessage());
//                            }
//                        });
//                        queuek.add(req);

                        Intent returnIntent = new Intent();
                        setResult(RegistrationFormActivity.RESULT_OK, returnIntent);
                        finish();
                    }

                    //******************************************************************************
                }
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