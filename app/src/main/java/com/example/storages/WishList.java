package com.example.storages;

import android.util.Log;
import android.widget.Toast;

import com.example.HSEOutlet;
import com.example.events.FavoriteEvent;
import com.example.events.UserChangedEvent;
import com.example.models.User;
import com.example.services.Services;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WishList {
    private List<String> items = new ArrayList<>();
    public User user;

    private static WishList instance = new WishList();

    public static WishList getInstance() {
        return instance;
    }

    @Subscribe
    public void onUserChangedEvent(UserChangedEvent event) {
        user = event.getUser();
        if (user!=null) {
            items = user.getWishlist();
        }
    }

    @Subscribe
    public void onFavouriteEvent(FavoriteEvent event) {
        Toast.makeText(HSEOutlet.context, "got event "+ event, Toast.LENGTH_SHORT).show();
        if (event.toFavorite()) {
            this.add(event.getId());
            Toast.makeText(HSEOutlet.context, "data: "+ WishList.getInstance().user.getLogin()+", "+event.getId(), Toast.LENGTH_LONG).show();
            Services.users.addToWishlist(WishList.getInstance().user, event.getId()).enqueue(Services.emptyCallBack);
        } else {
            this.remove(event.getId());
            Services.users.removeFromWishlist(WishList.getInstance().user, event.getId()).enqueue(Services.emptyCallBack);
        }

    }

    public void add(String pId) {
        items.add(pId);
    }

    public void remove(String pId) {
        items.remove(pId);
    }

    public boolean contains(String pId) {
        return items.indexOf(pId) != -1;
    }
}
