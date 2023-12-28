package com.example.administrator.lztsg.activity;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.anima.ViewSizeChangeAnimation;
import com.example.administrator.lztsg.httpjson.HttpLoadDataResolution;
import com.example.administrator.lztsg.utils.DensityUtils;
import com.example.administrator.lztsg.utils.UploadUtils;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class SearchImgActivity extends AppCompatActivity implements View.OnClickListener {
    private static SetInto mSetInto;
    private static final int WRITE_PERMISSION = 0x01;
    private Button mBut_SelectImg,mBut_Upload;
    private ImageView mImg_Upload,mBg_upload_img;
    private RelativeLayout mCbirSites_Itemmain;
    private RealtimeBlurView mBlurview;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private Animator mShowAnim,mHideAnim;
    private int dataarroenindex = 0;
    private String[] mTitles = {"结果"};
    private String filePath,fileorPath;
    private String uploadUrl = "https://yandex.com/images-apphost/image-download?cbird=111&images_avatars_size=preview&images_avatars_namespace=images-cbir";

    public static void setInto(SetInto setInto){
        mSetInto = setInto;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_img);
        init();
        LinearRecyclerView();
        requestWritePermission();
        initData();
        mBut_SelectImg.setOnClickListener(this);
        mBut_Upload.setOnClickListener(this);
    }

    private void init() {
        mBut_SelectImg = findViewById(R.id.but_select_img);
        mBut_Upload = findViewById(R.id.but_upload);
        mImg_Upload =findViewById(R.id.upload_img);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager2 = findViewById(R.id.v_pager);
        mCbirSites_Itemmain = findViewById(R.id.cbirsites_itemmain);
        mBg_upload_img = findViewById(R.id.upload_img_bg);
        mBlurview = findViewById(R.id.blurview);
    }

    private void initData() {
        //标题
        for (int i=0;i<mTitles.length;i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[i]),false);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //设置tab选中状态下的文本状态
                TextView textView = new TextView(SearchImgActivity.this);
                float selectedSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,16,getResources().getDisplayMetrics());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,selectedSize);
                textView.setTextColor(getResources().getColor(R.color.colorTextTitle));
                textView.setText(tab.getText());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tab.setCustomView(textView);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //设置tab非选中状态下的文本状态
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //使ViewPager2与Tablayout联动
        new TabLayoutMediator(mTabLayout,mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitles[position]);
            }
        }).attach();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    SearchImgActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTabLayout.getTabAt(0).select();//默认选中第一个
                            mViewPager2.setCurrentItem(0);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void LinearRecyclerView() {
        //禁用预加载
        mViewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mViewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return SearchMapFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return mTitles.length;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_select_img:
                chooseFile();
                break;

            case R.id.but_upload:
                if (filePath != null){
                    upload();
                    onItemMainsup();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }

    private void requestWritePermission() {
        // 检查是否已经获得了读取外部存储的权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求读取外部存储的权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }


    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent,100);
        dataarroenindex = 0;
    }

    private void upload() {
        Handler handler =new Handler();
        if (dataarroenindex == 0 && fileorPath != filePath){
            fileorPath = filePath;
            if (SearchMapFragment.mData.size()>0){
                SearchMapFragment.mData.clear(); //清除数据
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UploadUtils.uploadFile(new File(filePath), uploadUrl, new HttpLoadDataResolution() {
                        @Override
                        public void onFinish(String imageurl, String size, String title, String titleurl, String domain, String description) {
                            mSetInto.onYandexFinish(imageurl, size, title, titleurl, domain, description);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }).start();
            dataarroenindex++;
        }
    }

    private void onItemMainsup() {
        int heightPx = DensityUtils.dip2px(this,1000);
        Animation animation = new ViewSizeChangeAnimation(mCbirSites_Itemmain,heightPx);
        animation.setDuration(500);
        mCbirSites_Itemmain.startAnimation(animation);
//        RelativeLayout.LayoutParams remod = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        mCbirSites_Itemmain.setLayoutParams(remod);
    }

    private void onItemMainsdown() {
        int heightPx = DensityUtils.dip2px(this,0);
        Animation animation = new ViewSizeChangeAnimation(mCbirSites_Itemmain,heightPx);
        animation.setDuration(500);
        mCbirSites_Itemmain.startAnimation(animation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 100:
                if (resultCode == RESULT_OK && data != null){
//                    String uri = data.getDataString();
                    Uri selectedImageUri = data.getData();
                    String imagePath = getRealPathFromUri(this, selectedImageUri);
                    //判断文件是否存在
                    if (imagePath != null) {

//                        Uri imageUri = Uri.fromFile(new File(imagePath));
//                        filePath = imageUri.getPath();
//                        mBlurview.setVisibility(View.VISIBLE);
//                        mImg_Upload.setImageURI(imageUri);

                        File imageFile = new File(imagePath);
                        Uri imageUri = Uri.fromFile(imageFile);
                        filePath = imageUri.getPath();
                        mBlurview.setVisibility(View.VISIBLE);
                        if (imageFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
                            mImg_Upload.setImageBitmap(bitmap);
//                            mImg_Upload.setImageURI(imageUri);

                            Glide.with(this)
                                    .load(bitmap)
                                    .centerCrop()
                                    .into(mBg_upload_img);
                        }
//                        // 检查是否已经获得了读取外部存储的权限
//                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                                != PackageManager.PERMISSION_GRANTED) {
//                            // 请求读取外部存储的权限
//                            ActivityCompat.requestPermissions(this,
//                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                                    1);
//                        } else {
//                            // 已经获得了读取外部存储的权限，可以继续操作
//                            // 在这里加载图片
//
//                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //处理图片数据 得到图片PATH
    public String getRealPathFromUri(Context context, Uri uri) {
        String realPath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            realPath = cursor.getString(columnIndex);
            cursor.close();
        }
        return realPath;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if (mCbirSites_Itemmain.getHeight() > 0){
                    Log.e("text", "监听返回时间 ok" );
                    onItemMainsdown();
                    return true;
                }
                Log.e("text", "监听返回时间 "+ keyCode );
        }
        return super.onKeyUp(keyCode, event);
    }

    public interface SetInto{
        void onYandexFinish(String imageurl,String size,String title,String titleurl,String domain,String description);
    }
}
