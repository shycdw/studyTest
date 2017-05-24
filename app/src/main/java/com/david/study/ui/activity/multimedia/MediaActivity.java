package com.david.study.ui.activity.multimedia;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.david.study.R;
import com.david.study.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaActivity extends BaseActivity {

    /*MediaPlayer mMediaPlayer;*/

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        if (ContextCompat.checkSelfPermission(MediaActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MediaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请求限权", Toast.LENGTH_SHORT).show();
            show = false;
            ActivityCompat.requestPermissions(MediaActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            show = true;
            Toast.makeText(this, "有权限", Toast.LENGTH_SHORT).show();
        }
        /*String url = "http://rm.sina.com.cn/wm/VZ200705101138395899VK/music/p17_42.mp3";
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int i = 0;
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                Log.i("123123", "onCreate: " + thisId + " " + thisTitle);
                if (i <= 0) {
                    i += 1;
                    Uri contentUri = ContentUris.withAppendedId(
                            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, thisId);
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mMediaPlayer.setDataSource(getApplicationContext(), contentUri);
                        mMediaPlayer.prepareAsync();
                        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mMediaPlayer.start();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // ...process entry...
            } while (cursor.moveToNext());
            cursor.close();
        }*/
        /*try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        showLoading();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    private boolean show;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                // 如果请求取消了，数组将是空的
                show = true;
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // 权限请求通过，可以执行权限相关的操作
                    show = true;
                } else {
                    // 未授权，不允许执行权限相关操作
                    show = false;
                }
                return;
        }
    }

    public void takephote(View view) {
        if (show) {
            dispatchTakePictureIntent();
        } else {
            Toast.makeText(this, "没权限", Toast.LENGTH_SHORT).show();
        }
    }


    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 确保有相机应用
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // 创建图片保存的文件
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 创建文件发生错误
                ex.printStackTrace();
            }
            // 仅当文件创建成功，才继续执行
            if (photoFile != null) {
                Log.i("123", "dispatchTakePictureIntent: " + photoFile.getAbsolutePath());
                /*Uri photoURI = Uri.fromFile(photoFile);*/

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.david.study.fileprovider",
                        photoFile);
                Log.i("123123", "dispatchTakePictureIntent: " + photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra("return-data", true);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // 创建文件名称
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        /*File storageDir = new File(Environment.getExternalStoragePublicDirectory(null), "Android/data/com.david.study/files/DCIM");*/
        /*File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);*/
        /*File storageDir = new File(Environment.getExternalStorageDirectory(), "Android/data/com.david.study/files/Pictures");*/
       /* if (!storageDir.exists()) {
            storageDir.mkdir();
        }*/
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("123123", "createImageFile: " + storageDir.getAbsolutePath());
        /*File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);*/
        File f = getCacheDir();
        f = getExternalCacheDir();
        f = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        f = getFilesDir();
        f = Environment.getExternalStorageDirectory();
        f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        /*File storageDir = new File(getFilesDir(), "pic");*/
        /*File storageDir = Environment.getExternalStorageDirectory();*/
        /*File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pic");*/
        /*File storageDir = new File(getExternalCacheDir(), "pic");*/
        /*storageDir.mkdir();*/
        /*File storageDir = getExternalCacheDir();*/

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        galleryAddPic();
        ImageView iv_result = (ImageView) findViewById(R.id.iv_result);
        ImageView iv_result1 = (ImageView) findViewById(R.id.iv_result1);
        if (data != null) {
            Bundle bundle = data.getExtras();
            iv_result.setImageBitmap((Bitmap) bundle.get("data"));
        }
        // 创建图片保存的文件
        File photoFile;
        photoFile = new File(mCurrentPhotoPath);
        // 仅当文件创建成功，才继续执行
        if (photoFile.exists()) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.david.study.fileprovider",
                    photoFile);
            iv_result1.setImageURI(photoURI);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }*/
}
