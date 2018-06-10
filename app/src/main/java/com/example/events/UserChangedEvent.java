package com.example.events;

import com.example.models.User;

public class UserChangedEvent {
    private User user;

    public UserChangedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
