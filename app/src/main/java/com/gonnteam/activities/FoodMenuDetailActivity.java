package com.gonnteam.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gonnteam.R;
import com.squareup.picasso.Picasso;

public class FoodMenuDetailActivity extends AppCompatActivity {
    private TextView txtTitle;
    private ImageView imgBackdrop;
    private WebView wbvBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_detail);
        addControls();
    }

    private void addControls() {
        txtTitle = findViewById(R.id.txtTitle);
        imgBackdrop = findViewById(R.id.imgBackdrop);
        wbvBody = findViewById(R.id.wbvBody);

        String body = getIntent().getStringExtra("body");
        String title = getIntent().getStringExtra("title");
        String backdrop = getIntent().getStringExtra("backdrop");

        txtTitle.setText(title);
        if (backdrop.contains("data:image/jpeg;base64")) {
            backdrop = backdrop.substring(backdrop.indexOf(",") + 1);
            byte[] decodedString = Base64.decode(backdrop, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgBackdrop.setImageBitmap(decodedByte);
        } else {
            Picasso.with(FoodMenuDetailActivity.this).load(backdrop).into(imgBackdrop);
        }

        wbvBody.loadData("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + body,"text/html; charset=UTF-8", null);
    }
}
