package com.example.andreyko0.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dd.CircularProgressButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.application.R;

import org.json.JSONObject;

import java.lang.Integer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {
    protected static String name, description;
    protected int price;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ImageView imgPicture;
    ArrayList<BitmapDrawable> images = new ArrayList<BitmapDrawable>();
    int num_imgs = 0;
    String ViewId_Str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
    }

    public void onImageGalleryClicked(View v) {
        Button image_button = (Button) v;
        if (num_imgs == 6) { image_button.setEnabled(false); }
        else {
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
        TextView test = (TextView)findViewById(R.id.test);
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

                    // get a bitmap from the stream.
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(image);
                    images.add(bitmapDrawable);

                    // show the image to the user
                    Resources res = getResources();
                    String idName = "imgPicture" + num_imgs;
                    imgPicture = (ImageView) findViewById(res.getIdentifier(idName, "id", getPackageName()));
                    imgPicture.setImageDrawable(images.get(num_imgs));
                    imgPicture.setVisibility(View.VISIBLE);

                    num_imgs += 1;
//                    test.setText(String.format("%s", images.size()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indicating that the image is unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void moveImages(Integer idxStart) {
        for (Integer i = idxStart; i < images.size()-1; i++) {
            images.set(i, images.get(i+1));
        }

        for (Integer i = 0; i <= images.size()-1; i++) {
            Resources res = getResources();
            String num = "imgPicture" + i;
            ImageView img = (ImageView)findViewById(res.getIdentifier(num, "id", getPackageName()));
            img.setImageDrawable(images.get(i));
        }
        Resources res = getResources();
        String num = "imgPicture" + (images.size()-1);
        ImageView img = (ImageView)findViewById(res.getIdentifier(num, "id", getPackageName()));
        img.setImageDrawable(null);

        images.remove(images.size()-1);
    }

    public void showPopUp(View v) {
        ImageView Image = (ImageView)findViewById(v.getId());
        TextView test = (TextView)findViewById(R.id.test);

        ImageView first_img = (ImageView)findViewById(R.id.imgPicture0);
        if (Image.getDrawable() != null) {
            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_photo, popup.getMenu());
            popup.show();
            ImageView im = (ImageView) findViewById(v.getId());
            im.buildDrawingCache();
            ViewId_Str = Integer.toString(v.getId());
        }
        test.setText(String.format("%s", Integer.parseInt(ViewId_Str) - first_img.getId()));
    }

    public void buttonDeleteImage(MenuItem item) {
//        TextView test = (TextView)findViewById(R.id.test);
        Button image_button = (Button)findViewById(R.id.select_photo);
        if (num_imgs != 0) {
            ImageView first_img = (ImageView)findViewById(R.id.imgPicture0);
            num_imgs -= 1;
//            test.setText(String.format("%s", Integer.parseInt(ViewId_Str) - first_img.getId()));
            moveImages(Integer.parseInt(ViewId_Str) - first_img.getId());
            if (num_imgs < 6) { image_button.setEnabled(true); }
        }
    }

    public void buttonFullScreen(MenuItem item) {
        Resources res = getResources();
        ImageView im = (ImageView) findViewById(res.getIdentifier(ViewId_Str, "id", getPackageName()));
        ImageView first_img = (ImageView)findViewById(R.id.imgPicture0);

        im.buildDrawingCache();
        Bitmap image = im.getDrawingCache();

        Intent intent = new Intent(AddProductActivity.this, FullScreenImage.class);

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

//        if (name.equals("") || description.equals("")) {
        if (name.equals("") | edit_price.getText().toString().equals("")) {
            params_empty.setVisibility(View.VISIBLE);
            button.setProgress(-1);
        }
        else {
            price = Integer.parseInt(edit_price.getText().toString());
            Product p = new Product(name);
            p.setDescription(description);
            p.setPrice(price);
            p.setImage(images);
            ProductStorage.addProduct(p);
// +test
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://51.15.92.91/pr/new";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("description", description);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, o,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            VolleyLog.v("Got resp", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });
            queue.add(req);
// -test
            Intent returnIntent = new Intent();
            setResult(ScrollingActivity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
