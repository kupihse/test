package com.example.events;

public class LayoutChangeEvent {
    private final boolean mIsInListView;

    public LayoutChangeEvent(boolean isInListView) {
        this.mIsInListView = isInListView;
    }

    public boolean isInListView() {
        return mIsInListView;
    }

}
