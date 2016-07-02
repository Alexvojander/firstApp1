//package com.example.re3.firstapp;
//
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.provider.SyncStateContract;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteDatabase;
//import android.content.Context;
//import android.content.ContentValues;
//import android.widget.Toast;
//
//import com.nbsp.materialfilepicker.ui.FilePickerActivity;
//
//
//public class AddSoundActivity extends AppCompatActivity {
//    final int FILE_CHOOSER=1;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_sound);
//
//
//    }
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////
////        getMenuInflater().inflate(R.menu.main, menu);
////        return true;
////    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//            // Do anything with file
//        }
//    }
//
//
//}
