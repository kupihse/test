package com.example.events;

public class LanguageChangeEvent {

    private final boolean isLanguageRussian;

    public LanguageChangeEvent(boolean isLanguageRussian) {
        this.isLanguageRussian = isLanguageRussian;
    }

    public boolean isLanguageRussian() {
        return isLanguageRussian;
    }

}