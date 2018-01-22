package com.example.andreyko0.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.application.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity2 extends AppCompatActivity {
    protected static String name, description;
    protected int price;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    ArrayList<Bitmap> images = new ArrayList<>();
    int num_imgs = 0;
    String ViewId_Str;
    LinearLayout ll;
    Bitmap bm;

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
//                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), image);
                    images.add(image);
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
        for (Bitmap img : images) {
            SingleImage Im = new SingleImage(this, img, i);
            ll.addView(Im);
            i++;
        }
    }


    // удаляем картинку из массива и ререндерим (я ж говорил выше, что так проще)
    // + проверка на вообще возможность удаления
    private void moveImages(Integer idxStart) {
        if (idxStart < 0 || idxStart >= images.size()) {
            return;
        }
        images.remove(idxStart);
        rerenderImages();
    }

    // При нажатии на картинку показываем менюшечку
    // Делал александр, я не вникал что тут происходит
    public void showPopUp(View v) {
        ImageView Image = (ImageView) findViewById(v.getId());
        TextView test = (TextView) findViewById(R.id.test);

        if (Image.getDrawable() != null) {
            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_photo, popup.getMenu());
            popup.show();
            ImageView im = (ImageView) findViewById(v.getId());
            im.buildDrawingCache();
            ViewId_Str = Integer.toString((Integer) v.getTag());
        }

        test.setText(String.format("%s", v.getTag()));
    }

    // При нажатии на удаление – удаляем (ВААУ, да?)
    // Делал александр, я не вникал, в чем тут смысл
    public void buttonDeleteImage(MenuItem item) {
//        TextView test = (TextView)findViewById(R.id.test);
        Button image_button = (Button) findViewById(R.id.select_photo);
        if (num_imgs != 0) {
//            ImageView first_img = (ImageView)findViewById(R.id.imgPicture0);
            num_imgs -= 1;
//            test.setText(String.format("%s", Integer.parseInt(ViewId_Str) - first_img.getId()));
            moveImages(Integer.parseInt(ViewId_Str));
            if (num_imgs < 6) {
                image_button.setEnabled(true);
            }
        }
    }

    // очевидно из названия
    // Делал александр, я не вникал, в чем тут смысл
    public void buttonFullScreen(MenuItem item) {
        ImageView im = ll.findViewWithTag(Integer.parseInt(ViewId_Str));

        im.buildDrawingCache();
        Bitmap image = im.getDrawingCache();

        Intent intent = new Intent(AddProductActivity2.this, FullScreenImage.class);

        Bundle extras = new Bundle();
        extras.putParcelable("Bitmap", image);
        intent.putExtras(extras);
        startActivity(intent);
    }

    // при нажатии на "Submit"
    // Делал александр, я не вникал что тут происходит (до запроса к серверу)
    public void buttonOnClick(View v) throws Exception {
        CircularProgressButton button = (CircularProgressButton) v;
        final EditText edit_name = (EditText) findViewById(R.id.item_name);
        name = edit_name.getText().toString();
        final EditText edit_desc = (EditText) findViewById(R.id.item_description);
        description = edit_desc.getText().toString();
        final TextView params_empty = (TextView) findViewById(R.id.empty_parameters);
        final EditText edit_price = (EditText) findViewById(R.id.item_price);
        button.setIndeterminateProgressMode(true);

        if (images.size() == 0) {
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.unknown);
            images.add(bm);
        }

        if (name.equals("") | edit_price.getText().toString().equals("")) {
            params_empty.setVisibility(View.VISIBLE);
            button.setProgress(-1);
        } else {
            price = Integer.parseInt(edit_price.getText().toString());
            SendableProduct p = new SendableProduct(name)
                    .setDescription(description)
                    .setPrice(price);

            for (Bitmap img: images) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                String base64 = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                p.addImage(base64);
            }
//            ProductStorage.addProduct(p);
            // Делаем запрос, показываем Прогресс Бар (не работает, втф)
            Call<Void> c = Services.productService.newProduct(p);
            final ProgressBar progressBar = new ProgressBar(this);
            progressBar.setVisibility(View.VISIBLE);
            c.enqueue(new Callback<Void>() {

                // Если все ок, убираем прогресс бар, и возвращаемся обратно
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    progressBar.setVisibility(View.GONE);
                    Intent returnIntent = new Intent();
                    setResult(ScrollingActivity.RESULT_OK, returnIntent);
                    finish();
                }


                // Если все плохо и сервер вернул 5хх или 4хх
                // Показываем тост (за здоровье сервера) и возвращаемя
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failed to send", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(ScrollingActivity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }
    }
}
