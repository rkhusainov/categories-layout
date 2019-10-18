package com.khusainov.rinat.categorieslayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CategoriesLayout categoriesLayout = findViewById(R.id.categories_layout);
        categoriesLayout.setCategories(Arrays.asList(
                new Category(123),
                new Category(342342),
                new Category(3434333),
                new Category(1332443),
                new Category(33),
                new Category(34343),
                new Category(3434334)));

    }
}
