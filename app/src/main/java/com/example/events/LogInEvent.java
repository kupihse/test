package com.example.events;

public class LogInEvent {

    private boolean userLoggedIn;

    public LogInEvent(boolean userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }

}
