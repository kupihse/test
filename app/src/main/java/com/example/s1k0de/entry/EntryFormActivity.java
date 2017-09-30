package com.example.s1k0de.entry;

/**
 * Created by Eugeen on 28/09/2017.
 * Форма входа
 */
import android.app.Activity;
import android.os.Bundle;
import com.example.application.R;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

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
    }
}
