package com.brucej.revealgallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    GalleryScrollView galleryScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        galleryScrollView = findViewById(R.id.galleryView);
        List<RevealDrawable> list = new ArrayList<>();
        list.add(new RevealDrawable(
                getResources().getDrawable(R.drawable.p0),
                (getResources().getDrawable(R.drawable.p0_active)
                )));
        list.add(new RevealDrawable(
                getResources().getDrawable(R.drawable.p1),
                (getResources().getDrawable(R.drawable.p1_active)
                )));
        list.add(new RevealDrawable(
                getResources().getDrawable(R.drawable.p2),
                (getResources().getDrawable(R.drawable.p2_active)
                )));
        list.add(new RevealDrawable(
                getResources().getDrawable(R.drawable.p3),
                (getResources().getDrawable(R.drawable.p3_active)
                )));
        list.add(new RevealDrawable(
                getResources().getDrawable(R.drawable.p4),
                (getResources().getDrawable(R.drawable.p4_active)
                )));
        galleryScrollView.setDrawables(list);
    }
}
