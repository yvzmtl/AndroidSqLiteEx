package com.example.androidsqliteexisting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.androidsqliteexisting.adapter.MyAdapter;
import com.example.androidsqliteexisting.helper.DbHelper;
import com.example.androidsqliteexisting.model.ImageModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyler_view);

        init();
    }
    private void init(){
        dbHelper = new DbHelper(this);
        dbHelper.createDatabase();
        List<ImageModel> images = dbHelper.getAllImages();
        MyAdapter adapter = new MyAdapter(images);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}