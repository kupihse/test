package com.example.fragments;

import android.os.Bundle;
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

import com.example.activities.ChatMessage;
import com.example.application.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ChatList extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_chat_list, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        ListView userChats = root.findViewById(R.id.user_chats);
//        FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getEmail())
//                .child("chats")

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int idx = email.indexOf('@');
        email = email.substring(0, idx+1);
        //Load content
        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(email)
                .child("chats");


        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.fragmet_chat_message)
                .build();

        FirebaseListAdapter adapter = new FirebaseListAdapter<ChatMessage>(options)
        {

            @Override
            protected void populateView(View v, final ChatMessage model, int position) {

                TextView message, messageUser, messageTime;
                message = v.findViewById(R.id.message);
                messageUser = v.findViewById(R.id.messageUser);
                messageTime = v.findViewById(R.id.messageTime);

                message.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().beginTransaction()
                                .add(R.id.fragment_chat_list_container, Chat.newInstance(model.getMessageUser()))
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        };

        adapter.startListening();

        userChats.setAdapter(adapter);

        Log.d("testtest", "testtest");
        return root;
    }
}