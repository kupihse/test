package com.example.andreyko0.myapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.R;

import org.json.JSONObject;


public class AddProductActivity extends AppCompatActivity {
    protected static String name, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

    }

    public void buttonOnClick(View v) throws Exception {
        Button button = (Button) v;
        final EditText edit_name =  (EditText) findViewById(R.id.item_name);
        name = edit_name.getText().toString();
        final EditText edit_desc =  (EditText) findViewById(R.id.item_description);
        description = edit_desc.getText().toString();
        final TextView params_empty =  (TextView) findViewById(R.id.empty_parameters);

        if (name.equals("") || description.equals("")) {
            params_empty.setVisibility(View.VISIBLE);
        }
        else {
            Product p = new Product(name, description);
            ProductStorage.addProduct(p);
// +test
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://51.15.92.91/pr/new";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("description", description);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,o,
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
//            StringRequest req = new StringRequest(Request.Method.GET, "http://example.com",
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            VolleyLog.d("Got res", response);
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            VolleyLog.e("Error2:");
//                        }
//                    }
//            );
            queue.add(req);
// -test
            Intent returnIntent = new Intent();
            setResult(ScrollingActivity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
