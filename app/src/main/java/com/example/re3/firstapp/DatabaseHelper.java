package com.example.re3.firstapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.os.Environment;


/**
 * Created by Alexey on 28.06.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "musicStore.db"; // название бд
    private static final int SCHEMA =611; // версия базы данных
    static final String TABLE = "sound"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
//    public static final String COLUMN_AUTHOR = "author";
//    public static final String COLUMN_RESOURCE = "resource";

    public  String path=(Environment.getExternalStorageDirectory().getPath()+"/Music/chocolate.mp3");

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,SCHEMA);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE sound (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
                + " TEXT);");
        // добавление начальных данных
//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
//                +  ") VALUES ('" + path + "');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
    public void insertData(String filePath){
        String insertQuery = "INSERT INTO " +
                TABLE +
                " (" + COLUMN_NAME + ") VALUES ('"+ filePath +"')";
        this.getWritableDatabase().execSQL(insertQuery);

    }
    public Cursor cursorGetAllDAta(){

        return  this.getReadableDatabase().rawQuery("select * from "+ DatabaseHelper.TABLE, null);
    }

}
