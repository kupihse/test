package com.example.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.text.format.DateFormat;

import com.example.models.ChatMessage;
import com.example.application.R;
import com.example.events.LogInEvent;
import com.example.models.LastChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.Query;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class Chat extends Fragment {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    FloatingActionButton fab;
    RelativeLayout chat;
    TextView infoText;
    DatabaseReference databaseReference;
    String chatId;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public static Chat newInstance(String otherUserId, String chatId) {

        Bundle args = new Bundle();
        args.putString("otherUserId", otherUserId);
        args.putString("chatId", chatId);

        Chat fragment = new Chat();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatId = getArguments().getString("chatId");
        final View rootView = inflater.inflate(R.layout.fragment_chat_layout, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        chat = (RelativeLayout) rootView.findViewById(R.id.chat);
        infoText = (TextView) rootView.findViewById(R.id.not_loggedin_text);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            chat.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.INVISIBLE);
        } else {
            chat.setVisibility(View.INVISIBLE);
            infoText.setVisibility(View.VISIBLE);
        }

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int idx = email.indexOf('@');
        email = email.substring(0, idx);

        String email2 = getArguments().getString("otherUserId");
        int otheridx = email2.indexOf('@');
        email2 = email2.substring(0, otheridx);
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference();

        final String myEmail = email;
        final String otherEmail = email2;

        if (chatId == null) {
            chatId = databaseReference
                    .child("users")
                    .child(myEmail)
                    .push()
                    .getKey();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText input = (EditText) rootView.findViewById(R.id.inputs);
                ChatMessage msg = new ChatMessage(input.getText().toString(),
                        FirebaseAuth
                                .getInstance()
                                .getCurrentUser()
                                .getEmail());
                databaseReference
                        .child("users")
                        .child(myEmail)
                        .child(chatId)
                        .setValue(new LastChatMessage(otherEmail, msg, chatId));
                databaseReference
                        .child("users")
                        .child(otherEmail)
                        .child(chatId)
                        .setValue(new LastChatMessage(otherEmail, msg, chatId));

                databaseReference
                        .child("chats")
                        .child(chatId)
                        .child("messages")
                        .push()
                        .setValue(msg);
                input.setText("");
            }
        });
        Log.d("testtest", "testtest");

        //TODO CHECK IF NOT SIGN IN, THEN NAVIGATE SIGNIN PAGE

        /*
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // TODO NORMAL LOGIC CHANGE TO AUTH FRAGMENT
            rootView = inflater.inflate(R.layout.fragment_entry_form, container, false);
            toolbar = rootView.findViewById(R.id.toolbar);
        }

        else{
            rootView = inflater.inflate(R.layout.fragment_chat_layout, container, false);
            toolbar = rootView.findViewById(R.id.toolbar);
        }*/

        //Load content
        ListView listOfMessage = (ListView) rootView.findViewById(R.id.list_of_message);

        Query query = databaseReference
                .child("chats")
                .child(chatId)
                .child("messages");


        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.fragmet_chat_message)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options)
                //adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class, R.layout.fragment_chat_list, FirebaseDatabase.getInstance().getReference()) {
        {

            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                TextView message, messageUser, messageTime;
                message = v.findViewById(R.id.message);
                messageUser = v.findViewById(R.id.messageUser);
                messageTime = v.findViewById(R.id.messageTime);


                message.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };

        adapter.startListening();

        listOfMessage.setAdapter(adapter);
//
//        FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
//                Toast.makeText(getContext(), message.getMessageText(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        /*toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });*/

//        LinearLayout sendMessage = rootView.findViewById(R.id.send);
        return rootView;
    }

    @Subscribe
    public void OnLogInEvent(LogInEvent event) {
        if (event.isUserLoggedIn()) {
            chat.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.INVISIBLE);
        } else {
            chat.setVisibility(View.INVISIBLE);
            infoText.setVisibility(View.VISIBLE);
        }
    }

}