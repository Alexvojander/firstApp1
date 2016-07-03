package com.example.re3.firstapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.app.Activity;
import android.widget.ListView;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;

public class MainActivity extends  Activity {
    MediaMetadataRetriever mData;
    Bitmap image;
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

        DatabaseHelper sqlHelper;
        sqlHelper = new DatabaseHelper(this);

        Cursor cursor = sqlHelper.cursorGetAllDAta();

        while (cursor.moveToNext()) {
            String cover = cursor.getString(cursor
                    .getColumnIndex(DatabaseHelper.COLUMN_NAME));
            File thisFile = new File(cover);
            if (!thisFile.exists()){
                sqlHelper.deleteRow(sqlHelper.COLUMN_ID + " = " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                //Toast.makeText(MainActivity.this, sqlHelper.COLUMN_ID + " = " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)), Toast.LENGTH_LONG).show();
            }
        }
        cursor.close();
        cursor=sqlHelper.cursorGetAllDAta();
        todoAdapter.changeCursor(cursor);

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключения
        //db.close();
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
            sqlHelper.insertData(filePath);


            Cursor cursor =  sqlHelper.cursorGetAllDAta();
            todoAdapter.changeCursor(cursor);

        }
    }

}
