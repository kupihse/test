package com.example.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activities.entry.EntryFormActivity;
import com.example.activities.entry.RegistrationFormActivity;
import com.example.application.R;
import com.example.models.User;
import com.example.services.RegularChecker;
import com.example.services.Services;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFormFragment extends Fragment {


    public RegistrationFormFragment() {
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
        final View root = inflater.inflate(R.layout.fragment_registration_form, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        TextView loginScreen = root.findViewById(R.id.link_to_login);

        // Получаем экземпляр элемента Spinner
        final Spinner spinner = root.findViewById(R.id.emailList);

        // Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.emailAddition,R.layout.simple_spinner_item_custom);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Вызываем адаптер
        spinner.setAdapter(adapter);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                getFragmentManager().beginTransaction()
                        .remove(RegistrationFormFragment.this)
                        .commit();
            }

        });

        final Button button = root.findViewById(R.id.btnRegister);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                //************ берем поля ************

                EditText logint,namet,passwordt,passwordt1;
                logint = root.findViewById(R.id.reg_email);
                String login=logint.getText().toString();
                namet = root.findViewById(R.id.reg_fullname);
                String name = namet.getText().toString();
                passwordt = root.findViewById(R.id.reg_password);
                String password = passwordt.getText().toString();
                passwordt1 = root.findViewById(R.id.reg_password1);
                String password1= passwordt1.getText().toString();

                //*******************************************************
                //**************** Проверка на корректные поля **************
                if(!RegularChecker.doMatch(login) && !login.equals("") && !name.equals("") && !password.equals("") ) {

                    //************* сравнивает пароли,если не совпадают, то нотификейшн ***********
                    if (!password.equals(password1)) {
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

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
                        Spinner spinner = (Spinner) root.findViewById(R.id.emailList);
                        String selected = spinner.getSelectedItem().toString();
                        login += selected;
                        android.util.Log.d("mylog ", login);
                        //получено готовое поле login

                        //Далее проводим регистрацию
                        Call<Void> c = Services.users.add(new User(name, login, password));
                        c.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            }
                        });
                    }

                }
                //******************************************************************************

                else {
                    final AlertDialog.Builder mBuilderr = new AlertDialog.Builder(getContext());

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

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (bar != null) {
            bar.setTitle("Регистрация");
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }
}
