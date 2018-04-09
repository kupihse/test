package com.example.events;

public class FavoriteEvent {
    private final String mId;
    private final boolean mToFavorite;

    public FavoriteEvent(final String id, final boolean toFav) {
        mId = id;
        mToFavorite = toFav;
    }

    public String getId() {
        return mId;
    }

    public boolean toFavorite() {
        return mToFavorite;
    }
}
