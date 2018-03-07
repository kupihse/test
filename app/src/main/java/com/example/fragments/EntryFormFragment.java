package com.example.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activities.entry.EntryFormActivity;
import com.example.activities.entry.RegistrationFormActivity;
import com.example.application.R;
import com.example.models.User;
import com.example.services.Services;
import com.example.storages.CurrentUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntryFormFragment extends Fragment {


    public EntryFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_entry_form, container, false);


        TextView registerScreen = root.findViewById(R.id.link_to_register);

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen

                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_user_tab_container, new RegistrationFormFragment())
                        .addToBackStack(null)
                        .commit();

            }
        });

/*      Прогресс бар не нужен, все происходит слишком быстро
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        Circle doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce); */

        final Button buttonlog = root.findViewById(R.id.loginButton);
        buttonlog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //progressBar.setVisibility(ProgressBar.VISIBLE);

                // Берем поля
                EditText logint, passwordt;
                logint = root.findViewById(R.id.emailspace);
                String login = logint.getText().toString();
                passwordt = root.findViewById(R.id.passwordspace);
                String password = passwordt.getText().toString();

                //*******************************************************
                //**************** Проверка на пустые поля **************
                if ((!login.equals("")) && (!password.equals(""))) {
                    final User user = new User(login,password);
                    Call<String> c = Services.users.log(user);
                    c.enqueue(new Callback<String>(){

                        // пришлось делать через ResponseBody (или так и надо, хз)
                        // но выгляди неидеально, мб потом переделать имеет смысл
                        // допустим в отдельный класс TokenResp{respCode, token}
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            // Забираем тело запроса – строка, содержит токен
                            String token = response.body();

                            // если токена нет, посылаем нахер
                            if (token == null || token.equals("")) {
                                String text = token == null ? "Failed null" : "Failed empty";
                                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            } else {

                                // если токен есть ставим текущего юзера и возвращаемя
                                CurrentUser.save(user.getLogin(), token);

                                Toast.makeText(getContext(), "Success "+ response.code(), Toast.LENGTH_SHORT).show();
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_user_tab_container, new UserPageFragment())
                                        .commit();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), "Failed Login request", Toast.LENGTH_SHORT).show();
                            Log.d("THROW", t.getMessage());
                        }
                    });
                }
            }
            //progressBar.setVisibility(ProgressBar.INVISIBLE);
        });

        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (bar != null) {
            bar.setTitle("Вход");
        }

        super.onCreateOptionsMenu(menu, inflater);
    }
}
