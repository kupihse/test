package com.example.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layouts.ProductLayout;
import com.example.models.Product;
import com.example.layouts.SingleImageLayout;
import com.example.s1k0de.entry.EntryFormActivity;
import com.example.services.ImageStorage;
import com.example.services.Services;
import com.example.application.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
            for (final String id : product.getImages()) {
                Bitmap bmp = ImageStorage.get(id);
//                bmp.getByteCount()

                int imSize = BitmapCompat.getAllocationByteCount(bmp);
                int imSizeKB = imSize/1024;
                int quality;
                if (imSizeKB > 512) {
                    quality = 51200/imSizeKB;
                } else {
                    quality = 100;
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, quality, stream);
                String base64 = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                Services.SendableImage img = new Services.SendableImage();
                img.id = id;
                img.body = base64;

                Services.images.add(img).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // #todo
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // #todo
                        Toast.makeText(getApplicationContext(), "Failed to send Product image #"+id, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            Intent returnIntent = new Intent();
            setResult(ScrollingActivity.RESULT_OK, returnIntent);
            finish();
        }
    }

    public static class ProductActivity extends AppCompatActivity {
        private ImageView imgPicture;
        private Product product;
        // В активити передается id товара
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product);
            String id = getIntent().getStringExtra("item_id");
            // Делаем запрос по id
            Services.products.get(id).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {

                    // Если все плохо, показываем тост (за здоровье сервера)
                    if(!response.isSuccessful()) {
                        Toast.makeText(ProductActivity.this,"Failed to load", Toast.LENGTH_LONG).show();
                        return;
                    }
                    product = response.body();
                    // Если товар не найден на сервере, то ВТФ?? как так-то
                    // должен всегда возвращать, но на всякий случай
                    if (product == null) {
                        Toast.makeText(ProductActivity.this,"No such item found", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Тут все очевидно на мой взгляд, и я устал уже писать все это говно
                    setTitle(product.getName());
                    TextView textView = (TextView) findViewById(R.id.product_activity_text);
                    HorizontalScrollView scroll = (HorizontalScrollView) findViewById(R.id.scroll);
                    final LinearLayout ll = (LinearLayout)findViewById(R.id.photos_2);
                    textView.setText(product.getDescription() + "\n\n" + "Price: " + Integer.toString(product.getPrice()));
                    imgPicture = (ImageView) findViewById(R.id.image);
                    imgPicture.setImageBitmap(ImageStorage.get(product.getImage(0)));
                    if (product.getImages().size() > 1) {
                        // если вообще есть дополнительные картинки
                        // идем по массиву и непосредственно добавляем в Layout
                        for (int i = 1; i < product.getImages().size(); i++) {
                            final String id = product.getImage(i);
                            Bitmap img;
                            final int i2 = i;
                            if (ImageStorage.has(id)) {
                                SingleImageLayout Im = new SingleImageLayout(ProductActivity.this, ImageStorage.get(id), i2);
                                ll.addView(Im);
                            } else {
                                Services.images.get(id).enqueue(new Callback<Services.SendableImage>() {
                                    @Override
                                    public void onResponse(Call<Services.SendableImage> call, Response<Services.SendableImage> response) {
                                        if (!response.isSuccessful()) {
                                            // maybe #todo
                                            return;
                                        }
                                        Services.SendableImage encImg = response.body();
                                        if (encImg == null) {
                                            // maybe #todo
                                            return;
                                        }
                                        byte[] decodedString = Base64.decode(encImg.body, Base64.DEFAULT);
                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                        ImageStorage.set(id, decodedByte);
                                        SingleImageLayout Im = new SingleImageLayout(ProductActivity.this, decodedByte, i2);
                                        ll.addView(Im);
                                    }

                                    @Override
                                    public void onFailure(Call<Services.SendableImage> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    }
                    else {
                        scroll.setVisibility(View.GONE);
                    }
    //                imgPicture.setTag(0);
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(ProductActivity.this,"Failed to load", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void buttonFullScreen(View v) {
            // #todo
            /* Тут проблема, drawing cache всегда null, если добавить buildDrawingCache, то
            крашится. К тому же, дополнительные фото сделаны через SingleImage, а там
            onClick = "showPopUp", нужно подумать как лучше изменить. */
    //        int n = (int) v.getTag();

    //        ImageView view = (ImageView)findViewById(v.getId());
    //        view.buildDrawingCache(true);
    //        Bitmap image = view.getDrawingCache();

    //        if(view.getDrawingCache() == null) {
    //            Log.d("Drawing cache", "cache is null");
    //        }
            // Переход на FullScreenImageActivity
            Intent intent = new Intent(ProductActivity.this, FullScreenImageActivity.class);

            // Передаем в FullScreenImageActivity bitmap картинки и стартуем
    //        Bundle extras = new Bundle();
    //        extras.putParcelable("Bitmap", product.getImage(0));
    //        intent.putExtras(extras);
            intent.putExtra("Bitmap", product.getImage(0));
            Log.d("IMG LOG", "ASD");
            startActivity(intent);
        }
        public void showPopUp(View v) {
            // #todo
            /* Тут проблема, drawing cache всегда null, если добавить buildDrawingCache, то
            крашится. К тому же, дополнительные фото сделаны через SingleImage, а там
            onClick = "showPopUp", нужно подумать как лучше изменить. */
            int n = (int) v.getTag();

    //        ImageView view = (ImageView)findViewById(v.getId());

    //        view.buildDrawingCache(true);
    //        Bitmap image = view.getDrawingCache();

    //        if(view.getDrawingCache() == null) {
    //            Log.d("Drawing cache", "cache is null");
    //        }
            // Переход на FullScreenImageActivity
            Intent intent = new Intent(ProductActivity.this, FullScreenImageActivity.class);

            // Передаем в FullScreenImageActivity bitmap картинки и стартуем
            intent.putExtra("Bitmap", product.getImage(n));

            startActivity(intent);
        }
    }

    public static class ScrollingActivity extends AppCompatActivity {
        public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
        private LinearLayout ll;
        private ArrayList<Product> products = new ArrayList<>();
        private ArrayList<Product> products_search = new ArrayList<>();
        MaterialSearchView searchView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scrolling);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Recent items");
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));

            searchView = (MaterialSearchView)findViewById(R.id.search_view);
            // Тут пишем что происходит при закрытии поиска
            searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                @Override
                public void onSearchViewShown() {

                }

                @Override
                public void onSearchViewClosed() {
                    rerender();
                }
            });

            // Тут сам поиск
            searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // При нажатии на поиск ищем (мб можно как-то лучше сделать процесс поиска)
                    for (int i = 0; i < products.size(); i++) {
                        if (products.get(i).getName().contains(query)) {
                            products_search.add(products.get(i));
                        }
                    }
                    renderItems_search();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Это для динамического поиска
                    return false;
                }
            });

            ll = (LinearLayout) findViewById(R.id.products);
            rerender();


            // На потом, надо сделать обновление по свайпу вниз
            //
            //        SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
    //        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    //            @Override
    //            public void onRefresh() {
    //                rerender();
    //            }
    //        });
        }

        // Если делать через тот же метод и тот же список products, то работает хреново
        private void renderItems_search() {
            // если добавлять некуда, то зачем?
            if (ll == null) {
                return;
            }
            ll.removeAllViews();

            // Если добавлять неоткуда, то как?
            if (products_search.isEmpty()) {
                return;
            }
            // добавляем
            for (Product p: products_search) {
                ll.addView(new ProductLayout(this, p));
            }
        }

        // Рендерим товары, предварительно запоминаем их в массиве (надо ли?)
        private void renderItems() {
            // если добавлять некуда, то зачем?
            if (ll == null) {
                return;
            }
            ll.removeAllViews();

            // Если добавлять неоткуда, то как?
            if (products.isEmpty()) {
                return;
            }
            // добавляем
            for (Product p: products) {
                ll.addView(new ProductLayout(this, p));
            }
        }

        private void rerender() {
            //очищаем все товары, нам же не нужно дублировать
            // оптимизировать это потом ??
            products.clear();
            products_search.clear();

            // делаем запрос на все товары
            Services.products.getAll().enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    List<Product> prs = response.body();

                    // Если ничего не пришло, то ничего не делаем
                    if (prs == null) {
                        return;
                    }
                    // Если что-то есть закидываем это в массив
                    for(Product sp: prs) {
                        products.add(sp);
                    }
                    // Ну и ререндерим
                    renderItems();
                }

                // Если чет все плохо, то просто пишем в лог, пока что
                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    Log.d("RERENDER FAIL", t.toString());
                }
            });
        }

        // КОСТЫЛЬ
        // В отедельном, неотображаемом TextView у нас записан id
        // забираем его и записываем в параметры запроса новой активити
        public void doClick(View v) {
            TextView tv =  v.findViewById(R.id.product_id);
            Intent productIntent = new Intent(this, ProductActivity.class);
            productIntent.putExtra("item_id", tv.getText());
            startActivity(productIntent);
        }

        // Просто создание меню
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_scrolling, menu);
            MenuItem item = menu.findItem(R.id.scrolling_menu_search);
            searchView.setMenuItem(item);
            return true;
        }

        // Нажали на кнопочку сверху справа (три точки)
        // Думаю тут все в целом понятно, просто switch по меню
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle item selection
            switch (item.getItemId()) {
                case R.id.scrolling_menu_settings:
                    return true;
                case R.id.scrolling_menu_add_product:
                    startActivityForResult(new Intent(this, AddProductActivity.class), 1);
                    return true;
                case R.id.scrolling_menu_reg:
                    startActivity(new Intent(this, EntryFormActivity.class));
                    return true;
                case R.id.scrolling_menu_refresh:
                    rerender();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

        // При удачном возврате из активити добавления товара, просто ререндерим
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode==RESULT_OK){
                if(requestCode==1){
                    rerender();
                }
            }
        }
    }
}
