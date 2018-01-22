package com.example.andreyko0.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
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
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.application.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
    private ImageView imgPicture;
    ArrayList<Bitmap> images = new ArrayList<>();
    int num_imgs = 0;
    String ViewId_Str;
    LinearLayout ll;
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_2);
        ll = (LinearLayout) findViewById(R.id.photos_2);
    }

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

    private void rerenderImages() {
        int i = 0;
        ll.removeAllViews();
        LayoutInflater inflater = this.getLayoutInflater();
        for (Bitmap img : images) {
//            ImageView vv = new ImageView(this);
//            vv.setImageDrawable(img);
//            ll.addView(vv);
//            ImageView v = (ImageView) inflater.inflate(R.layout.image_view, null);
//            ImageView vvv = (ImageView) findViewById(R.id.test_image);
//            vvv.setImageDrawable(img);
//            ll.addView(vvv);
//            ImageView v = (ImageView) View.inflate(this, R.layout.image_view, ll);
//            v.setImageDrawable(img);
//            v.setVisibility(View.VISIBLE);
//            ll.addView(v);
            SIngleImage Im = new SIngleImage(this, img, i);
            ll.addView(Im);
//            ll.addView(new SIngleImage(this, img));
//            ll.setTag(i);
            i++;

//            ll.addView(new Img(this, img));
        }
    }

    private void moveImages(Integer idxStart) {
        for (Integer i = idxStart; i < images.size() - 1; i++) {
            images.set(i, images.get(i + 1));
        }

//        for (Integer i = 0; i <= images.size()-1; i++) {
//            Resources res = getResources();
//            String num = "imgPicture" + i;
//            ImageView img = (ImageView)findViewById(res.getIdentifier(num, "id", getPackageName()));
//            img.setImageDrawable(images.get(i));
//        }
//        Resources res = getResources();
//        String num = "imgPicture" + (images.size()-1);
//        ImageView img = (ImageView)findViewById(res.getIdentifier(num, "id", getPackageName()));
//        img.setImageDrawable(null);

        images.remove(images.size() - 1);
        rerenderImages();

    }

    public void showPopUp(View v) {
        ImageView Image = (ImageView) findViewById(v.getId());
        TextView test = (TextView) findViewById(R.id.test);

//        ImageView first_img = (ImageView)findViewById(R.id.imgPicture0);
        if (Image.getDrawable() != null) {
            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_photo, popup.getMenu());
            popup.show();
            ImageView im = (ImageView) findViewById(v.getId());
            im.buildDrawingCache();
            ViewId_Str = Integer.toString((Integer) v.getTag());
        }

//        test.setText(String.format("%s", Integer.parseInt(ViewId_Str)));
        test.setText(String.format("%s", v.getTag()));
    }

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

    public void buttonFullScreen(MenuItem item) {
        Resources res = getResources();
//        ImageView im = (ImageView) findViewById(res.getIdentifier(ViewId_Str, "id", getPackageName()));
//        ImageView first_img = (ImageView)findViewById(R.id.imgPicture0);
        ImageView im = ll.findViewWithTag(Integer.parseInt(ViewId_Str));

        im.buildDrawingCache();
        Bitmap image = im.getDrawingCache();

        Intent intent = new Intent(AddProductActivity2.this, FullScreenImage.class);

        Bundle extras = new Bundle();
        extras.putParcelable("Bitmap", image);
        intent.putExtras(extras);
        startActivity(intent);
    }

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
            ProductStorage.addProduct(p);
// +test
//            RequestQueue queue = Volley.newRequestQueue(this);
//            String url = "http://51.15.92.91/pr/new";
//            JSONObject o = new JSONObject();
//            o.put("name", name);
//            o.put("description", description);
//            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, o,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            VolleyLog.v("Got resp", response);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.e("Error: ", error.getMessage());
//                }
//            });
//            queue.add(req);
            Call<Void> c = Services.productService.newProduct(p);
            c.enqueue(Services.emptyCallBack);
            c.wait();
// -test
            Intent returnIntent = new Intent();
            setResult(ScrollingActivity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
