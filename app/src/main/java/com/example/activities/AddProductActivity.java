package com.example.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.models.SendableImage;
import com.example.storages.ImageStorage;
import com.example.services.Services;
import com.example.application.R;
import com.example.layouts.SingleImageLayout;
import com.example.models.Product;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    protected static String name, description;
    protected int price;
    public static final int IMAGE_GALLERY_REQUEST = 20;
//    ArrayList<Bitmap> images = new ArrayList<>();
    Product product = new Product();
    int num_imgs = 0;
    String ViewId_Str;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        // Ставим соотв. Layout
        setContentView(R.layout.activity_add_product_2);
        // Референс на Layout фоток
        ll = (LinearLayout) findViewById(R.id.photos_2);
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
        TextView test = (TextView) findViewById(R.id.test);
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
            SingleImageLayout Im = new SingleImageLayout(this, ImageStorage.get(imgId), i);
            ll.addView(Im);
            i++;
        }
    }


    // удаляем картинку из массива и ререндерим (я ж говорил выше, что так проще)
    // + проверка на вообще возможность удаления
    private void moveImages(int idx) {
//        for (Integer i = idxStart; i < images.size() - 1; i++) {
//            images.add(i, images.get(i + 1));
//        }
        String id = product.getImage(idx);
        product.getImages().remove(idx);
        ImageStorage.delete(id);
//        images.remove(images.size() - 1);
        rerenderImages();
    }

    // Метод, вызываемый при нажатии на картинку.
    // Показывает выпадающее меню
    public void showPopUp(View v) {
        // Получаем id картинки, на которую нажали и её image view
        ImageView Image = (ImageView) findViewById(v.getId());
        TextView test = (TextView) findViewById(R.id.test); // это для теста

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

        // Для теста вывод тэга
        test.setText(String.format("%s", v.getTag()));
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
        Integer idx = Integer.parseInt(ViewId_Str);
        String imId = product.getImage(idx);

        // Переход на FullScreenImageActivity
        Intent intent = new Intent(AddProductActivity.this, FullScreenImageActivity.class);

        // Передаем в FullScreenImageActivity bitmap картинки и стартуем
//        Bundle extras = new Bundle();
//        extras.putParcelable("Bitmap", );
//        intent.putExtras(extras);
        intent.putExtra("Bitmap", imId);
        startActivity(intent);
    }

    // при нажатии на "Submit"
    public void buttonOnClick(View v) throws Exception {
        AppCompatButton button = (AppCompatButton) v;
        final EditText edit_name = (EditText) findViewById(R.id.item_name);
        name = edit_name.getText().toString();
        final EditText edit_desc = (EditText) findViewById(R.id.item_description);
        description = edit_desc.getText().toString();
        final TextView params_empty = (TextView) findViewById(R.id.empty_parameters);
        final EditText edit_price = (EditText) findViewById(R.id.item_price);

        // Это для задания прогресса кнопки (а саму кнопку скорее всего заменим потом)
//        button.setIndeterminateProgressMode(true);

        if (product.getImages().size() == 0) {
            // Если нет картинок, добавляем стандартную
            // #todo Возможно переделать это немного по-другому, хз
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.unknown);
            ImageStorage.set("0", bm);
            product.addImage("0");
        }

        if (name.equals("") | edit_price.getText().toString().equals("")) {
            // Если есть пустые поля, показываем надпись и кнопку в состояние "Failed"
            params_empty.setVisibility(View.VISIBLE);
            button.setText("Failed");
            button.setBackgroundColor(Color.parseColor("#FF2B2B"));
//            button.setProgress(-1);
        } else {
            price = Integer.parseInt(edit_price.getText().toString());
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);

//            for (Bitmap img: images) {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                String base64 = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
//                p.addImage(base64);
//            }
//            ProductStorage.addProduct(p);
            // Делаем запрос, показываем Прогресс Бар (не работает, втф)
            Services.products.add(product).enqueue(new Callback<Void>() {

                // Если все ок, убираем прогресс бар, и возвращаемся обратно
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                }
                // Если все плохо и сервер вернул 5хх или 4хх
                // Показываем тост (за здоровье сервера) и возвращаемя
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failed to send Product data", Toast.LENGTH_SHORT).show();
                }
            });
            int i = 0;
            final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
             NotificationChannel channel = new NotificationChannel("hse_ch", "hse outlet ch", NotificationManager.IMPORTANCE_DEFAULT);
             channel.setDescription("hey,there");
             manager.createNotificationChannel(channel);
            }
            for (final String id : product.getImages()) {
                Bitmap bmp = ImageStorage.get(id);

                int imSize = BitmapCompat.getAllocationByteCount(bmp);
                int imSizeKB = imSize/1024;
                int quality;
                if (imSizeKB > 512) {
                    quality = 51200/imSizeKB;
                } else {
                    quality = 100;
                }
                SendableImage img = SendableImage.encode(id,bmp, quality);
                final int _i = i;
                Services.images.add(img).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // #todo
                        NotificationCompat.Builder builder = new NotificationCompat
                                .Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.logo)
                                .setChannel("hse_ch")
                                .setContentTitle(String.format("Sent image #%d", _i));
                        manager.notify(_i,builder.build());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // #todo
                        NotificationCompat.Builder builder = new NotificationCompat
                                .Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.logo)
                                .setChannel("hse_ch_01")
                                .setContentTitle(String.format("Failed to send image #%d", _i));
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(_i,builder.build());
                    }
                });
            }

            Intent returnIntent = new Intent();
            setResult(ScrollingActivity.RESULT_OK, returnIntent);
            finish();
        }
    }
}