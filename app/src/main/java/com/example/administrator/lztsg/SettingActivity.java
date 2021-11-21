package com.example.administrator.lztsg;

import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class SettingActivity extends AppCompatActivity {
    private ImageButton mImgButton;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bindViews();
        ImageButtonOnClick();
    }

    private void bindViews(){
        mImgButton = findViewById(R.id.imgbutton);
    }

    //返回箭头点击事件
    public void ImageButtonOnClick(){
        mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public static class SettingsFragment extends PreferenceFragment {
        private Preference mSummary;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            getCaheSize();
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if ("setting_cleandate".equals(preference.getKey())){
                Toast.makeText(getActivity(),"嗯，整洁了不少呢,,,帮大忙了！！", Toast.LENGTH_SHORT).show();
                cleanCache();
                mSummary.setSummary(getCaheSize());
            }
            return super.onPreferenceTreeClick(preference);
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {

        }

        //获取缓存大小
        private String getCaheSize(){
            String str="";
            try{
                str = DataCleanManager.getTotalCacheSize(getActivity());
                mSummary = findPreference("setting_cleandate");
                mSummary.setSummary(str);
            }catch (Exception e){
                e.printStackTrace();
            }
            return str;
        }

        //清除缓存
        private void cleanCache(){
            DataCleanManager.clearAllCache(getActivity());
        }
    }

}
