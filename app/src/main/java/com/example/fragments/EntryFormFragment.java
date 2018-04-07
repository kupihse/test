package com.example.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.models.User;
import com.example.services.Services;
import com.example.storages.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntryFormFragment extends Fragment {

    EditText loginText, passwordText;
    private FirebaseAuth mAuth;
    private boolean isVerified = false;

    public EntryFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
                // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_entry_form, container, false);

        TextView registerScreen = root.findViewById(R.id.link_to_register);

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen

                // Экспериментальная анимация
                getChildFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_entry_form_container, new RegistrationFormFragment())
                        .addToBackStack(null)
                        .commit();

            }
        });
        loginText = root.findViewById(R.id.emailspace);
        ;
        passwordText = root.findViewById(R.id.passwordspace);
        ;

/*      Прогресс бар не нужен, все происходит слишком быстро
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        Circle doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce); */

        Button check = root.findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = loginText.getText().toString();
                FirebaseAuth.getInstance().getCurrentUser().reload()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                Log.d("createUserWith", String.valueOf(user.isEmailVerified()));
                                Log.d("createUserWith", user.getEmail());
                            }
                        });
            }
        });

        final Button buttonlog = root.findViewById(R.id.loginButton);
        buttonlog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //progressBar.setVisibility(ProgressBar.VISIBLE);

                // Берем поля
                String login = loginText.getText().toString();
                String password = passwordText.getText().toString();

                if ((!login.equals("")) && (!password.equals(""))) {
//                    FirebaseAuth.getInstance().getCurrentUser().reload()
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                    isVerified = user.isEmailVerified();
//                                }
//                            });
//                    if (isVerified) {
                    mAuth.signInWithEmailAndPassword(login, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseAuth.getInstance().getCurrentUser().reload()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    isVerified = user.isEmailVerified();
                                                }
                                            });
                                        if (isVerified) {
                                            Toast.makeText(getContext(), "Success ", Toast.LENGTH_SHORT).show();
                                            getChildFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_entry_form_container, new UserPageFragment())
                                                    .commit();
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "Your account is not verified", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        String text = "Failed";
                                        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                                        getFragmentManager().popBackStack();
                                    }
                                }
                            });
//                }
//                    else {
//                        Toast.makeText(getActivity(), "Your account is not verified", Toast.LENGTH_SHORT).show();
//                    }

                }

                //*******************************************************
                //**************** Проверка на пустые поля **************
//                if ((!login.equals("")) && (!password.equals(""))) {
//                    final User user = new User(login, password);
//                    Call<String> c = Services.users.log(user);
//                    c.enqueue(new Callback<String>() {
//
//                        // пришлось делать через ResponseBody (или так и надо, хз)
//                        // но выгляди неидеально, мб потом переделать имеет смысл
//                        // допустим в отдельный класс TokenResp{respCode, token}
//                        @Override
//                        public void onResponse(Call<String> call, Response<String> response) {
//
//                            // Забираем тело запроса – строка, содержит токен
//                            String token = response.body();
//
//                            // если токена нет, посылаем нахер
//                            if (token == null || token.equals("")) {
//                                String text = token == null ? "Failed null" : "Failed empty";
//                                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
//                                getFragmentManager().popBackStack();
//                            } else {
//
//                                // если токен есть ставим текущего юзера и возвращаемя
//                                CurrentUser.save(user.getLogin(), token);
//
//                                Toast.makeText(getContext(), "Success " + response.code(), Toast.LENGTH_SHORT).show();
//                                getChildFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.fragment_entry_form_container, new UserPageFragment())
//                                        .commit();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<String> call, Throwable t) {
//                            Toast.makeText(getContext(), "Failed Login request", Toast.LENGTH_SHORT).show();
//                            Log.d("THROW", t.getMessage());
//                        }
//                    });
//                }
            }
            //progressBar.setVisibility(ProgressBar.INVISIBLE);
        });

        return root;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (loginText != null && !isVisibleToUser) {
            loginText.setEnabled(false);
            passwordText.setEnabled(false);
            loginText.setEnabled(true);
            passwordText.setEnabled(true);
        }
    }

}
