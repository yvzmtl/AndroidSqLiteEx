package com.example.androidsqliteexisting.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import com.example.androidsqliteexisting.model.ImageModel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "";
    private static String DB_NAME = "MyDatabase.db";
    private static String TABLE_NAME = "Image";
    private static int DB_VER = 1;


    private SQLiteDatabase _database;
    private Context _context;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
        this._context = context;
        if(Build.VERSION.SDK_INT >= 17){
            DB_PATH = new StringBuilder(context.getApplicationInfo().dataDir).append("/databases/").toString();
        }
        else{
            DB_PATH = new StringBuilder("/data/data").append(context.getPackageName()).append("/databases/").toString();
        }
    }

    private boolean checkDatabase(){
        SQLiteDatabase tempDB = null;
        try {
            String path = new StringBuilder(DB_PATH).append(DB_NAME).toString();
            tempDB = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(tempDB != null){
            tempDB.close();
        }
        return tempDB != null;
    }

    private void copyDatabase(){
        try {
            InputStream input = _context.getAssets().open(DB_NAME);
            String outputFileName = new StringBuilder(DB_PATH).append(DB_NAME).toString();
            OutputStream output = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;
            while((length = input.read(buffer)) > 0){
                output.write(buffer,0,length);
            }
            output.flush();
            output.close();
            input.close();
        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void createDatabase(){
        boolean isDbExistOnPhone = checkDatabase();
        if(!isDbExistOnPhone){
            //DB yoksa, telefondaki paketten kopyalayacağız
            this.getReadableDatabase();
            try {
                copyDatabase();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void close() {
        if(_database != null){
            _database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //CRUD
    public List<ImageModel> getAllImages(){
        List<ImageModel> images = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                ImageModel model = new ImageModel(cursor.getInt(0),cursor.getString(1));
                images.add(model);
            }while (cursor.moveToNext());
        }
        return images;
    }
}
