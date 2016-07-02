package com.example.re3.firstapp;

import android.app.ListActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SeekBar;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.ImageView;
import android.util.ArrayMap;
import android.os.PersistableBundle;
import android.transition.TransitionManager;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;


public class MainActivity extends  Activity {
    MediaMetadataRetriever mData;
    Bitmap image;

    SQLiteDatabase db;

    SimpleCursorAdapter userAdapter;
    ListView listItems;
    TodoCursorAdapter todoAdapter;

    private void goToPlaySoundActivity(String sound){
        Intent intent= new Intent(this, PlaySoundActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("pathToMusic",sound);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listItems =(ListView)findViewById(R.id.list);

        DatabaseHelper sqlHelper;
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getReadableDatabase();

        Cursor userCursor =  sqlHelper.cursorGetAllDAta();

        todoAdapter=new TodoCursorAdapter(this,userCursor,0);
        listItems.setAdapter(todoAdapter);

        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Cursor cursor =(Cursor)parent.getItemAtPosition(position);
                String path=cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                goToPlaySoundActivity(path);
               // Toast.makeText(MainActivity.this, path, Toast.LENGTH_LONG).show();
            }
        };

        listItems.setOnItemClickListener(itemListener);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddSoundActivity();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

//        sqlHelper = new DatabaseHelper(this);
//        db = sqlHelper.getReadableDatabase();
//
//        Cursor cursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
//
////
//        while (cursor.moveToNext()) {
//////
//            String cover = cursor.getString(cursor
//                    .getColumnIndex(DatabaseHelper.COLUMN_NAME));
//            File thisFile = new File(cover);
//            if (!thisFile.exists()){
//                sqlHelper.getWritableDatabase().delete("sound", "_id" + "='"
//                        +cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)) + "'", null);
//
//            }
//        }
//        Cursor userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
//        todoAdapter.changeCursor(userCursor);


//        String query = "SELECT " + DatabaseHelper.COLUMN_ID + ", "
//                + DatabaseHelper.COLUMN_NAME+ " FROM " + DatabaseHelper.TABLE;
//        Cursor cursor2 = db.rawQuery(query, null);
//        array1.clear();
//
//        while (cursor2.moveToNext()) {
////            int id = cursor2.getInt(cursor2
////                    .getColumnIndex(DatabaseHelper.COLUMN_ID));
//            String name = cursor2.getString(cursor2
//                    .getColumnIndex(DatabaseHelper.COLUMN_NAME));
//
//            array1.add(name);
//
//            String[] array=array1.toArray(new String[0]);
//            setListAdapter(new MusicAdapter(array) );
//
//        }
//        cursor2.close();

        //userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
              //  userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
       // header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
      //  mList.setAdapter(userAdapter);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключения
        db.close();
        //cur.close();
    }

    private void goToAddSoundActivity(){
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1).withRootPath(Environment.getExternalStorageDirectory().toString())
                .withFilterDirectories(true) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    public class TodoCursorAdapter extends CursorAdapter {
        ImageView imageView;
        TextView nameView;
        TextView authorView;
        public TodoCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
        }

        public void printRow(String cover){
            mData=new MediaMetadataRetriever();
            mData.setDataSource(cover);
            try{
                byte art[]=mData.getEmbeddedPicture();
                image= BitmapFactory.decodeByteArray(art, 0, art.length);
                imageView.setImageBitmap(image);
            }
            catch(Exception e)
            {
                image=null;
                imageView.setImageResource(R.drawable.image);
            }

            String soundName = mData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String soundAuthor = mData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            nameView.setText(soundName);
            authorView.setText(soundAuthor);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String cover =cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));

            File thisFile = new File(cover);
            if (thisFile.exists()){
                imageView = (ImageView) view.findViewById(R.id.flag);
                nameView = (TextView) view.findViewById(R.id.name);
                authorView = (TextView) view.findViewById(R.id.author);
                printRow(cover);
            }
            else{
            //
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            DatabaseHelper sqlHelper;
            sqlHelper = new DatabaseHelper(this);
            db = sqlHelper.getWritableDatabase();
            sqlHelper.insertData(filePath);

            db = sqlHelper.getReadableDatabase();
            Cursor userCursor =  sqlHelper.cursorGetAllDAta();
            todoAdapter.changeCursor(userCursor);

        }
    }

}
