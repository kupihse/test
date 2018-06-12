package com.example.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.application.R;
import com.example.events.LogInEvent;
import com.example.models.LastChatMessage;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class ChatList extends Fragment {

    LinearLayout chat;
    TextView infoText;
    View rootView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        chat = rootView.findViewById(R.id.chats_view);
        infoText = rootView.findViewById(R.id.not_loggedin_text);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            infoText.setVisibility(View.VISIBLE);
            chat.setVisibility(View.GONE);
        } else {
            infoText.setVisibility(View.GONE);
            chat.setVisibility(View.VISIBLE);
            setChats();
        }

        return rootView;
    }

    private void setChats() {
        ListView userChats = rootView.findViewById(R.id.user_chats);
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int idx = email.indexOf('@');
        if (idx != -1) {
            email = email.substring(0, idx);
        }
        //Load content
        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(email);

        FirebaseListOptions<LastChatMessage> options = new FirebaseListOptions.Builder<LastChatMessage>()
                .setQuery(query, LastChatMessage.class)
                .setLayout(R.layout.fragmet_chat_message)
                .build();

        FirebaseListAdapter adapter = new FirebaseListAdapter<LastChatMessage>(options)
        {

            @Override
            protected void populateView(View v, final LastChatMessage model, int position) {

                TextView message, messageUser, messageTime;
                message = v.findViewById(R.id.message);
                messageUser = v.findViewById(R.id.messageUser);
                messageTime = v.findViewById(R.id.messageTime);
                message.setText(model.message.getMessageUser()+": "+model.message.getMessageText());
                messageUser.setText(model.otherEmail);
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.message.getMessageTime()));
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().beginTransaction()
                                .add(R.id.fragment_chat_list_container, Chat.newInstance(model.otherEmail, model.chatId))
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        };

        adapter.startListening();
        userChats.setAdapter(adapter);
    }

    @Subscribe
    public void OnLogInEvent(LogInEvent event) {
        if (event.isUserLoggedIn()) {
            chat.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.GONE);
            setChats();
        } else {
            chat.setVisibility(View.GONE);
            infoText.setVisibility(View.VISIBLE);
        }
    }

}