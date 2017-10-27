package com.example.s1k0de.entry;

/**
 * Created by Eugeen on 28/09/2017.
 * Форма входа
 */
import android.app.Activity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.andreyko0.myapplication.ScrollingActivity;
import com.example.application.R;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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

        final Button buttonlog = findViewById(R.id.angry_btn);
        buttonlog.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 // Берем поля
                 EditText logint, passwordt;
                 logint = (EditText) findViewById(R.id.emailspace);
                 String login = logint.getText().toString();
                 passwordt = (EditText) findViewById(R.id.passwordspace);
                 String password = passwordt.getText().toString();
                 //*******************************************************
                 //**************** Проверка на пустые поля **************
                 if ((!login.equals("")) && (!password.equals(""))) {
                     RequestQueue queuekd = Volley.newRequestQueue(EntryFormActivity.this.getApplicationContext());
                     String urls = "http://51.15.47.229/user/log";
                     JSONObject o = new JSONObject();
                     try{
                         o.put("name", "unknown");
                         o.put("login", login);
                         o.put("password", password);
                     } catch (JSONException e) {
                         throw new RuntimeException(e);
                     }
                     JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, urls, o,
                         new Response.Listener<JSONObject>() {
                             @Override
                             public void onResponse(JSONObject response) {
                                 VolleyLog.v("Got resp", response);
                             }
                         }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {
                             VolleyLog.e("Error: ", error.getMessage());
                         }
                     });
                     queuekd.add(req);

                     Intent returnIntent = new Intent();
                     setResult(EntryFormActivity.RESULT_OK, returnIntent);
                     finish();
                 }

             }
         });
    }
}
