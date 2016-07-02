package com.example.re3.firstapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;


public class PlaySoundActivity extends AppCompatActivity {

    private Button buttonPlay;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private final Handler handler = new Handler();

    public void initViews(String pathToCover) throws IOException {
        buttonPlay = (Button) findViewById(R.id.buttonPlay);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(pathToCover);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
        //
        }
       // File thisFile = new File( Environment.getExternalStorageDirectory().getPath()+"/Music/123.mp3");
//
//
//        if (thisFile.exists()){}
//
//        else{}
//

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });
        startPlayProgressUpdater();
    }
    private void seekChange(View v){
        if(mediaPlayer.isPlaying()){
            SeekBar sb = (SeekBar)v;
            mediaPlayer.seekTo(sb.getProgress());
        }
    }
    public void playMusic(View view){
        try{
            mediaPlayer.start();
            startPlayProgressUpdater();
        }catch (IllegalStateException e) {
          //  mediaPlayer.pause();
        }
    }
    public void pauseMusic(View viev){
        mediaPlayer.pause();
    }
    public void stopMusic(View viev){
        mediaPlayer.stop();
        seekBar.setProgress(0);
    }
    public void startPlayProgressUpdater() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }else{
            mediaPlayer.pause();
            //seekBar.setProgress(0);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (this.isFinishing()){
            mediaPlayer.stop();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            mediaPlayer.stop();
            Intent intent = new Intent(PlaySoundActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else if(keyCode == KeyEvent.KEYCODE_HOME)
        {
            mediaPlayer.stop();
            Intent intent = new Intent(PlaySoundActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sound);

        Bundle extras=getIntent().getExtras();
        if (extras != null) {
            TextView text1=(TextView)findViewById(R.id.name);
            TextView author1=(TextView)findViewById(R.id.author);
            ImageView  imageView = (ImageView) findViewById(R.id.flag);
            MediaMetadataRetriever mData=new MediaMetadataRetriever();
            Bitmap image;

            String pathToCover=extras.getString("pathToMusic");

            mData.setDataSource(pathToCover);
            try{
                    byte art[]=mData.getEmbeddedPicture();
                    image= BitmapFactory.decodeByteArray(art, 0, art.length);
                    imageView.setImageBitmap(image);
                }
            catch(Exception  e)
                {
                    //image=null;
                    imageView.setImageResource(R.drawable.image);
                }

            String soundName = mData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String soundAuthor = mData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            assert text1 != null;
            text1.setText(soundName);

            assert author1 != null;
            author1.setText(soundAuthor);
            try {
                initViews(pathToCover);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }



}
