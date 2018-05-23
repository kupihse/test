package com.example.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.layouts.SingleImageLayout;
import com.example.models.Product;
import com.example.services.Services;
import com.example.services.UploadImagesTask;
import com.example.storages.ImageStorage;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    protected static String name, description, seller_login;
    protected int price;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    Product product = new Product();
    int num_imgs = 0;
    String ViewId_Str;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);

//        String languageToLoad  = "ru";
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());

        // Ставим соотв. Layout
        setContentView(R.layout.activity_add_product);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setHomeButtonEnabled(true);
        }

        Button test = (Button) findViewById(R.id.button2);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LocaleHelper.setLocale(AddProductActivity.this, "ru");
                String languageToLoad  = "ru";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                Toast.makeText(AddProductActivity.this, "все ок", Toast.LENGTH_SHORT).show();
                recreate();
            }
        });

        // Референс на Layout фоток
        ll = findViewById(R.id.photos_2);
        final LinearLayout tagList = findViewById(R.id.tag_list);
        final EditText tagInput = findViewById(R.id.tag_add);
        tagInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    TextView newTag = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
                    String tag = v.getText().toString();
                    newTag.setText(tag);
                    tagList.addView(newTag);
                    tagInput.getText().clear();
                    product.addTag(tag);
                    return true;

                }
                return false;
            }
        });

    }

    // Кнопка добавления картинок
    // Пропадает при 6 картинках (или нет?) удалить/исправить
    public void onImageGalleryClicked(View v) {
        Button image_button = (Button) v;
        if (num_imgs == 6) {
            image_button.setEnabled(false);
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String pictureDirectoryPath = pictureDirectory.getPath();
            Uri data = Uri.parse(pictureDirectoryPath);
            photoPickerIntent.setDataAndType(data, "image/*");
            startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
        }
    }

    // Тут в целом на инглише описано (че не миэм, прочитать не сможешь?)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // if we are here, everything processed successfully.
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                // if we are here, we are hearing back from the image gallery.

                // the address of the image on the SD Card.
                Uri imageUri = data.getData();

                // declare a stream to read the image data from the SD Card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image.
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    String id = ImageStorage.add(image);
                    product.addImage(id);
//                    images.add(image);
                    num_imgs += 1;
                    rerenderImages();
                } catch (IOException e) {
                    e.printStackTrace();
                    // show a message to the user indicating that the image is unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    // Каждый раз при добавлении картинок, ререндерим весь список изображений
    // (перфоманс сильно не страдает, и так сильно проще)
    private void rerenderImages() {
        int i = 0;
        // очищаем все картинки
        ll.removeAllViews();
        // идем по всему их массиву и непосредственно добавляем в Layout
        for (String imgId : product.getImages()) {
            SingleImageLayout Im = new SingleImageLayout(this, imgId, i);
            ll.addView(Im);
            i++;
        }
    }


    // удаляем картинку из массива и ререндерим (я ж говорил выше, что так проще)
    // + проверка на вообще возможность удаления
    private void moveImages(int idx) {
        String id = product.getImage(idx);
        product.getImages().remove(idx);
        ImageStorage.delete(id);
        rerenderImages();
    }

    // Метод, вызываемый при нажатии на картинку.
    // Показывает выпадающее меню
    public void showPopUp(View v) {
        // Получаем id картинки, на которую нажали и её image view
        ImageView Image = (ImageView) findViewById(v.getId());

        // Это условие лишнее, так как мы изменили метод добавления картинок
        if (Image.getDrawable() != null) {
            // Создаем, добавляем, показываем layout menu_photo
            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_photo, popup.getMenu());
            popup.show();
            // Получаем id (а точнее tag, смысл тот же) image view, на которое нажали
            ViewId_Str = Integer.toString((Integer) v.getTag());
        }
    }

    public void showPhotoPickerMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_photo_picker_popup, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.photo_picker_select:
                        onImageGalleryClickedCopy();
                        return true;
                }
                return false;
            }
        });
    }

    public void onImageGalleryClickedCopy() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    // При нажатии на удаление – удаляем (ВААУ, да?)
    public void buttonDeleteImage(MenuItem item) {
        Button image_button = (Button) findViewById(R.id.select_photo);
        if (num_imgs != 0) {
            num_imgs -= 1;
            moveImages(Integer.parseInt(ViewId_Str));
            if (num_imgs < 6) {
                image_button.setEnabled(true);
            }
        }
    }

    // очевидно из названия
    public void buttonFullScreen(MenuItem item) {
//        Integer idx = Integer.parseInt(ViewId_Str);
//        String imId = product.getImage(idx);
//
//        // Переход на FullScreenImageActivity
//        Intent intent = new Intent(AddProductActivity.this, FullScreenImageActivity.class);
//
//        // Передаем в FullScreenImageActivity bitmap картинки и стартуем
//        intent.putExtra("Bitmap", imId);
//        startActivity(intent);

        Integer idx = Integer.parseInt(ViewId_Str);
        Intent intent = new Intent(AddProductActivity.this, FullScreenImageActivity.class);
        intent.putExtra("Bitmap", product.getImages());
        intent.putExtra("position", idx);
        startActivity(intent);
    }

    // при нажатии на "Submit"
    public void buttonOnClick(View v) throws Exception {
        AppCompatButton button = (AppCompatButton) v;
        seller_login = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        final EditText edit_name = (EditText) findViewById(R.id.item_name);
        name = edit_name.getText().toString().trim();
        final EditText edit_desc = (EditText) findViewById(R.id.item_description);
        description = edit_desc.getText().toString().trim();
        final TextView params_empty = (TextView) findViewById(R.id.empty_parameters);
        final EditText edit_price = (EditText) findViewById(R.id.item_price);

        if (product.getImages().size() == 0) {
            // Если нет картинок, добавляем стандартную
            // #todo Возможно переделать это немного по-другому, хз
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.unknown);
            ImageStorage.set("0", bm);
            product.addImage("0");
        }

        if (name.equals("") || edit_price.getText().toString().equals("")) {
            // Если есть пустые поля, показываем надпись и кнопку в состояние "Failed"
            showAlert("Название товара и цена обязательны");
        } else {
            price = Integer.parseInt(edit_price.getText().toString());
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setSellerId(seller_login);

            // Делаем запрос
            Services.products.add(product).enqueue(new Callback<Void>() {

                // Если все ок
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    new UploadImagesTask(getApplicationContext()).execute(product.getImages());
//                    CurrentUser.user().addProduct(product.getId());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("id", product.getId());
                    setResult(MainActivity.RESULT_OK, returnIntent);
                    finish();
                }

                // Если все плохо и сервер вернул 5хх или 4хх
                // Показываем тост (за здоровье сервера) и возвращаемя
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failed to send Product data", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void showAlert(CharSequence notificationText) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddProductActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.activity_popupwindow, null);
        Button button_popup = mView.findViewById(R.id.button_popup);
        TextView text = mView.findViewById(R.id.popupwindows);
        text.setText(notificationText);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        button_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
