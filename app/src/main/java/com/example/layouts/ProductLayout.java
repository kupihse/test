package com.example.layouts;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activities.ProductActivity;
import com.example.application.R;
import com.example.events.FavoriteEvent;
import com.example.events.UserChangedEvent;
import com.example.fragments.UserPageFragment;
import com.example.models.Product;
import com.example.services.Services;
import com.example.storages.ImageStorage;
import com.example.storages.WishList;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class ProductLayout extends LinearLayout {

    TextView nameView,sellerName,priceView,dateView;
    static String rub, todayStr, yesterdayStr;

    ImageView imgPicture, favoriteView;

    Context context;

    Product product;

    boolean hideImage = false;

    public ProductLayout(Context context) {
        this(context, R.layout.single_product);
    }

    public ProductLayout(Context context, final int layout) {
        super(context);
        rub = context.getResources().getString(R.string.rub);
        todayStr = context.getResources().getString(R.string.today);
        yesterdayStr = context.getResources().getString(R.string.yesterday);
        this.context = context;
        setLayout(layout);
    }

    public void setLayout(final int layout) {
        inflate(getContext(), layout, this);
        setViews();
    }

    public void setHideImage(boolean hideImage) {
        this.hideImage = hideImage;
    }

    public ProductLayout(final Context ctx, Product p) {
        this(ctx);
        setProduct(p);
    }

    private void doClick(Context ctx, String id) {
        Intent productIntent = new Intent(ctx, ProductActivity.class);
        productIntent.putExtra("item_id", id);
        ctx.startActivity(productIntent);
    }


    public void setViews() {
        nameView  =  findViewById(R.id.product_text);
        sellerName = findViewById(R.id.product_user_login);
        priceView  =  findViewById(R.id.price);
        dateView = findViewById(R.id.product_date);
        imgPicture = findViewById(R.id.ImageView);
        favoriteView = findViewById(R.id.image_favorite);
    }

    private void switchFavoriteView(final String productId) {
        boolean toFavorite = !WishList.getInstance().contains(productId);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            EventBus.getDefault().post(new FavoriteEvent(productId, toFavorite));
        }
    }

    @Subscribe
    public void onFavoriteEvent(FavoriteEvent event){
        if (event.getId().equals(product.getId())) {
            setFavoriteView(event.toFavorite());
        }
    }

    public void setFavoriteView(boolean isFav) {
        if (isFav) {
            favoriteView.setImageResource(R.drawable.button_star_full);
            favoriteView.setBackgroundResource(R.drawable.button_star_empty_black);
        } else {
            favoriteView.setImageResource(R.drawable.button_star_empty);
            favoriteView.setBackgroundResource(R.drawable.button_star_empty);
        }
    }

    @Subscribe
    public void onUserChangedEvent(UserChangedEvent event) {
        if (event.getUser() != null) {
            boolean isFav = event.getUser().getWishlist().indexOf(product.getId()) != -1;
            setFavoriteView(isFav);
        }
    }


    public void setProduct(final Product p) {
        product = p;
        setProductsViews(nameView, sellerName, priceView, dateView,imgPicture, favoriteView, p, hideImage);
        favoriteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFavoriteView(p.getId());
            }
        });
    }



    public static void setProductsViews(
            TextView nameView, TextView sellerView, TextView priceView,
            TextView dateView, ImageView imageView, ImageView favoriteView, Product p, boolean hideImage) {

        final String name = p.getName();
        nameView.setText(name);

        final String seller_id = p.getSellerId();
        sellerView.setText(seller_id);


        final Integer price = p.getPrice();
        priceView.setText(Integer.toString(price) + ' ' + rub);

        dateView.setText(getDateText(p));

        final String productId = p.getId();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            favoriteView.setVisibility(GONE);
        } else {
            boolean isFavourite = WishList.getInstance().contains(productId);
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (isFavourite) {
                    favoriteView.setImageResource(R.drawable.button_star_full);
                    favoriteView.setBackgroundResource(R.drawable.button_star_empty_black);
                } else {
                    favoriteView.setImageResource(R.drawable.button_star_empty);
                    favoriteView.setBackgroundResource(R.drawable.button_star_empty);

                }
            }
        }
        // todo убрать второе условие
        if (p.getImages() == null
                || p.getImages().size() == 0
                || p.getImage(0).equals("0")) {
            if (hideImage) {
                imageView.setVisibility(GONE);
            } else {
                imageView.setVisibility(VISIBLE);
                imageView.setImageResource(R.drawable.unknown_item);
            }
            return;
        }

        final String imId = p.getImage(0);
        imageView.setVisibility(VISIBLE);
        ImageStorage.inject(imageView, imId);
    }

    public static String getDateText(Product p) {
        //формат даты
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yy");
        // Даты сегодня и та что у продукта
        Date todayDate = new Date();
        String today = formatForDateNow.format(todayDate);
        Date productDate = new Date(p.getSendableDate());
        Calendar test = Calendar.getInstance();
        test.setTime(productDate);
        test.add(Calendar.DATE, 1);
        Date prDate = test.getTime();

        String dateFromServer = productDate != null ? formatForDateNow.format(prDate) : "";
        String result;
        // проверка даты, мб костыльно
        // получение вчерашней даты
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date todate1 = cal.getTime();
        String yesterday = formatForDateNow.format(todate1);
        if (dateFromServer.equals(today)){
            result = todayStr;
        }
        else if (dateFromServer.equals(yesterday)){
            result = yesterdayStr;
        }
        else {
            result = dateFromServer;
        }
        return result;
    }
}
