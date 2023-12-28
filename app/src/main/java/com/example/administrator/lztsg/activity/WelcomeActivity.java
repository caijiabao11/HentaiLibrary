package com.example.administrator.lztsg.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.Constants;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WelcomeActivity extends AppCompatActivity {
    private static final int WRITE_PERMISSION = 0x01;
    private static final String TAG = "a";
    private ImageView mImageView;
    private WebView mWebView;

    private long localFileSize;
    private long serverFileSize;
    private int REQUEST_CODE_CONTACT = 101;
    private String path = "https://cdn.jsdelivr.net/gh/caijiabao11/HentaiLibrary/app/src/main/res/raw/HentaiDB.db";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
        initData();
    }

    private void init() {
        mImageView = findViewById(R.id.welcom_bg);
        mWebView = findViewById(R.id.web_gethtml);
    }

    private void initData() {
        Glide.with(MyApplication.getContext())
                .load(R.drawable.video_bgimg_1)
                .centerCrop()
                .into(mImageView);



        File localFile = new File(String.valueOf(WelcomeActivity.this.getDatabasePath(Constants.DATABASE_NAME)));
        localFileSize = localFile.length();//大小
        serverFileSize = 17 * 1000;


        if(!localFile.exists() || serverFileSize > localFileSize){
            getupdatabase(this,localFile);

        }
        askPermissions();

    }

    private void getupdatabase(Context context, File databasepath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(path)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String contentLength = response.header("Content-Length");
                        if (contentLength != null) {
                            serverFileSize = Long.parseLong(contentLength);
                            if (serverFileSize > localFileSize) {
                                File outputFile = new File(String.valueOf(databasepath)); // 本地保存的文件路径和名称
                                try {
                                    FileOutputStream fos = new FileOutputStream(outputFile);
                                    fos.write(response.body().bytes());
                                    fos.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void askPermissions() {
        //动态申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            // 检查是否已经获得了读取外部存储的权限
            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                // 请求读取外部存储的权限
                requestPermissions(permissions, REQUEST_CODE_CONTACT);
            } else {
                // 已经获得了读取外部存储的权限，可以继续操作
                startMainActivity();
            }
//            int REQUEST_CODE_CONTACT = 101;
//            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//            // 检查是否已经获得了读取外部存储的权限
//            for (String str : permissions) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    // 请求读取外部存储的权限
//                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
//                } else if (this.checkSelfPermission(str) == PackageManager.PERMISSION_GRANTED) {
//                    // 已经获得了读取外部存储的权限，可以继续操作
//                    final Intent intent = new Intent(this, MainActivity.class);
//                    Timer timer = new Timer();
//                    TimerTask tack = new TimerTask() {
//                        public void run() {
//                            startActivity(intent);
//                            finish();
//                        }
//                    };
//                    timer.schedule(tack, 1000 * 2);
//                }
//            }
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
//            }
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }
    }

    // 权限请求结果回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_CONTACT) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                // 用户允许了权限，可以继续操作
//                startMainActivity();
//            } else {
//                // 用户拒绝了权限，可以在这里进行处理
//                Toast.makeText(this, "需要授予读取外部存储的权限才能继续操作", Toast.LENGTH_SHORT).show();
//            }
//        }
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionsResult: ");
                Toast.makeText(this, "需要授予读取外部存储的权限才能继续操作", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    // 启动MainActivity
    private void startMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        Timer timer = new Timer();
        TimerTask tack = new TimerTask() {
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(tack, 1000 * 2);
    }
}
