package com.example.layouts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.activities.ProductActivity;
import com.example.activities.UserPageActivity;
import com.example.storages.CurrentUser;
import com.example.storages.ImageStorage;
import com.example.application.R;
import com.example.models.Product;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
/**
 * Created by Andreyko0 on 26/09/2017.
 */

public class ProductLayout extends LinearLayout {

    TextView nameView,sellerName,priceView,dateView;

    ImageView imgPicture, favoriteView;

    Context context;

    public ProductLayout(Context context) {
        super(context);
        inflate(getContext(), R.layout.single_product, this);
        setViews();
        this.context = context;
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

    public void setOnClickListeners(final Context ctx, Product p) {
        final String productId = p.getId();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doClick(ctx, productId);
            }
        });
        if (!CurrentUser.isSet()) {
            return;
        }
        favoriteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CurrentUser.wishlist.contains(productId)) {

                    CurrentUser.wishlist.remove(productId);
                    favoriteView.setImageResource(R.drawable.button_star_empty);

                } else {

                    CurrentUser.wishlist.add(productId);
                    favoriteView.setImageResource(R.drawable.button_star_full);

                }

                CurrentUser.save();
            }
        });
    }

    public void setProduct(Product p) {
        setProductsViews(nameView, sellerName, priceView, dateView,imgPicture, favoriteView, p);
        setOnClickListeners(this.context, p);
    }

    public static void setProductsViews(
            TextView nameView, TextView sellerView, TextView priceView,
            TextView dateView, ImageView imageView, ImageView favoriteView, Product p) {

        final String name = p.getName();
        nameView.setText(name);

        final String seller_id = p.getSellerId();
        sellerView.setText(seller_id);


        final Integer price = p.getPrice();
        priceView.setText(Integer.toString(price) + " руб.");

        dateView.setText(getDateText(p));

        final String productId = p.getId();

        if (!CurrentUser.isSet()) {
            favoriteView.setImageResource(R.drawable.button_star_inactive);
        } else {

            if (CurrentUser.wishlist.contains(productId)) {
                favoriteView.setImageResource(R.drawable.button_star_full);
            } else {
                favoriteView.setImageResource(R.drawable.button_star_empty);
            }
        }

        final String imId = p.getImage(0);

        ImageStorage.inject(imageView, imId);
    }

    public static String getDateText(Product p) {
        //формат даты
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yy");
        // Даты сегодня и та что у продукта
        Date todayDate = new Date();
        String today = formatForDateNow.format(todayDate);
        Date productDate = new Date(p.getSendableDate());
        String dateFromServer = productDate != null ? formatForDateNow.format(productDate) : "";
        String result;
        // проверка даты, мб костыльно
        // получение вчерашней даты
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date todate1 = cal.getTime();
        String yesterday = formatForDateNow.format(todate1);
        if (dateFromServer.equals(today)){
            result = "Сегодня";
        }
        else if (dateFromServer.equals(yesterday)){
            result = "Вчера";
        }
        else {
            result = dateFromServer;
        }
        return result;
    }
}
