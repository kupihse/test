package com.example.s1k0de.entry;

/**
 * Created by Eugeen on 28/09/2017.
 *
 * Первое что будет открываться, так это данная форма входа, в которой будут
 * кнопки : * регистрация, отправляющая на соответствующий активитиформ регистрации
 *          * логин, отправляющая на активитиформ входа.
 *
 * Сделал без тулбара,
 *          вместо него добавлю другую шапку
 *
 *                                                                  To be continued...
 */
        import android.app.Activity;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.Window;
        import android.view.WindowManager;

        import com.example.application.R;


public class EntryFormActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_entryform);
        String text =getIntent().getStringExtra("text");
        setTitle(text);
    }

}