package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.CustomJZMp3;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.httpjson.AsmrHttpJson;
import com.example.administrator.lztsg.httpjson.CunzhiHellHttpJson;
import com.example.administrator.lztsg.httpjson.FapHeroHttpJson;
import com.example.administrator.lztsg.httpjson.HentaiJoiHttpJson;
import com.example.administrator.lztsg.httpjson.HpoiHttpJson;
import com.example.administrator.lztsg.httpjson.HttpAsmrJsonResolution;
import com.example.administrator.lztsg.httpjson.HttpHpoiDataResolution;
import com.example.administrator.lztsg.httpjson.HttpJsonResolution;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    //获取TAG的fragment名称
    protected final String TAG = this.getClass().getSimpleName();
    private static SetInto mSetInto;
    private static final String KEY_POSITION = "position";
    private int position;
    private boolean isFirstLoad = true; //是否第一次加载
    public WhiteNoiseActivity whiteNoiseActivity;
    private CustomJZMp3 customJZMp3;
    private List<String> loadpage;
    private boolean oneload = true;
    private EditText mSearch;
    public static String data;
    public Context context;

    public static void setInto(SetInto setInto) {
        mSetInto = setInto;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(ctx);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(initLyout(), container, true);
        initView(rootview);
        initData(context);
        return rootview;
    }

    public abstract int setContentLayout();

    protected abstract int initLyout();//初始化布局

    protected abstract void initView(View view);//控件获取

    protected abstract void initData(Context context);//数据结构

    public void onDataLoad() {
        //数据加载
        Handler handler = new Handler();

        position = getArguments().getInt(KEY_POSITION);
        Log.d("当前页面", "当前页面" + position);
        switch (position) {
            //地狱寸止fragment页面 ==0
            case 0:
                CunzhiHellHttpJson.getData(new HttpJsonResolution() {
                    @Override
                    public void onFinish(String title, String imageurl, String videourl, String duration) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mSetInto.onCunzhiHellFinish(title, imageurl, videourl, duration);
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                break;

            //颅内高潮fragment页面 ==1
            case 1:
                HentaiJoiHttpJson.getData(new HttpJsonResolution() {
                    @Override
                    public void onFinish(String title, String imageurl, String videourl, String duration) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mSetInto.onHentaiFinish(title, imageurl, videourl, duration);
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
                break;

            //节奏大师fragment页面 ==2
            case 2:
                FapHeroHttpJson.getData(new HttpJsonResolution() {
                    @Override
                    public void onFinish(final String title, final String imageurl, final String videourl, final String duration) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mSetInto.onFapHeroFinish(title, imageurl, videourl, duration);
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                break;
        }
    }

    public void onDataLoadStop() {
        //停止数据加载
    }

    public void onDataLoadNoise() {
        position = getArguments().getInt(KEY_POSITION);

        TextView textView = getActivity().findViewById(R.id.tit);
        customJZMp3 = getActivity().findViewById(R.id.noise_mp3);
        ImageView imageView = getActivity().findViewById(R.id.img_noise);
        switch (position) {
            case 0:
                textView.setText("【白噪音】2小时_在太空舱里安静的休息");
                customJZMp3.setUp("https://img.maycode.top/cai/noise/data/01.mp3", "");
                loadimg(imageView, "https://img.maycode.top/cai/noise/img/01.jpg");
                break;
            case 1:
                textView.setText("【老歌向/环境音】3小时 汤姆躺沙发上熟睡 氛围声");
                customJZMp3.setUp("https://img.maycode.top/cai/noise/data/02.mp3", "");
                loadimg(imageView, "https://img.maycode.top/cai/noise/img/02.jpg");
                break;
            case 2:
                textView.setText("【白噪音/环境音】4小时 女朋友在你枕旁熟睡 氛围声");
                customJZMp3.setUp("https://img.maycode.top/cai/noise/data/03.mp3", "");
                loadimg(imageView, "https://img.maycode.top/cai/noise/img/03.jpg");
                break;
        }
        //播放器设置
        customJZMp3.startVideo();
        if (whiteNoiseActivity.play_or_pause) {
            customJZMp3.goOnPlayOnPause();
        } else {
            customJZMp3.goOnPlayOnResume();
        }

    }

    public void onDataLoadHpoi() {
        //数据加载
        Handler handler = new Handler();
        loadpage = new ArrayList<>();
        mSearch = HpoiActivity.mSearch;

        position = getArguments().getInt(KEY_POSITION);
        Log.d("当前页面", "当前页面" + position);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void afterTextChanged(Editable s) {
                //这是文本框改变之后 会执行的动作
                data = mSearch.getText().toString();
//                if (data != null && data != "") {
//                    data = "&keyword=" + data;
//                }
            }
        });

        String page = String.valueOf(position + 1);
        if (data == null){
            data = "";
        }
        String selecttopage = data + "&page=" + page;

        if (loadpage.size() != 0 && !(loadpage.get(0).equals(page)) || oneload == true && data == "") {

            oneload = false;
            if (loadpage.size() != 0){
               loadpage.clear();
            }
            loadpage.add(page);
            HpoiHttpJson.getData(selecttopage, new HttpHpoiDataResolution() {
                @Override
                public void onFinish(String title, String imageurl, String url) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSetInto.onHpoiFinish(title, imageurl, url);
                        }
                    });
                }

                @Override
                public void onInitData(String name, String value) {

                }

                @Override
                public void onPages(int total) {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        } else if (loadpage.size() != 0 && !(loadpage.get(0).equals(page)) || oneload == true && data != "") {
            oneload = false;
            if (loadpage.size() != 0){
                loadpage.clear();
            }
            loadpage.add(page);

            data = "&keyword=" + data;
            selecttopage = data + "&page=" + page;
            //热门浏览界面链接
            HpoiHttpJson.getData(selecttopage, new HttpHpoiDataResolution() {
                @Override
                public void onFinish(String title, String imageurl, String url) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSetInto.onHpoiFinish(title, imageurl, url);
                        }
                    });
                }

                @Override
                public void onInitData(String name, String value) {

                }

                @Override
                public void onPages(int total) {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

    }

    public void onDataLoadAsmr(){
        Handler handler = new Handler();
        loadpage = new ArrayList<>();
        mSearch = AsmrActivity.mSearch;

        position = getArguments().getInt(KEY_POSITION);
        String page = String.valueOf(position + 1);
        Log.d("当前页面", "当前页面" + position);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void afterTextChanged(Editable s) {
                //这是文本框改变之后 会执行的动作
                data = mSearch.getText().toString();
            }
        });

        if (!loadpage.isEmpty() && !(loadpage.get(0).equals(page)) || oneload && AsmrActivity.shouldExecuteTextWatcher){
            oneload = false;
            if (!loadpage.isEmpty()){
                loadpage.clear();
            }
            loadpage.add(page);
            AsmrHttpJson.getData(data,(position + 1),new HttpAsmrJsonResolution() {
                @Override
                public void onFinish(String id, String title, String name, String release, JSONArray vas, JSONArray tags, String CoverUrl) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSetInto.onAsmrFinish(id,title,name,release,vas,tags,CoverUrl);
                        }
                    });
                }

                @Override
                public void onTracksFinish(JSONArray trackslist) {

                }

                @Override
                public void onPages(int total) {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }


    public void loadimg(View view, String imgurl) {
        Glide.with(context)
                .load(imgurl)
                .centerCrop()
                .into((ImageView) view);
    }

    @Override
    public void onResume() {
        super.onResume();
        //懒加载
        if (isFirstLoad && TAG.equals("PagerFragment")) {
            Log.d(TAG, "onResume");
            onDataLoad();
            isFirstLoad = false;
        } else if (TAG.equals("NoiseFragment")) {
            Log.d(TAG, "onResume");
            onDataLoadNoise();
        } else if (TAG.equals("HpoiFragment")) {
            Log.d(TAG, "onResume");
            onDataLoadHpoi();
        } else if (TAG.equals("AsmrFragment")) {
            Log.d(TAG, "onResume");
            onDataLoadAsmr();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public abstract void onDestroyFragment();

    public interface SetInto {
        void onCunzhiHellFinish(String title, String imageurl, String videourl, String duration);

        void onHentaiFinish(String title, String imageurl, String videourl, String duration);

        void onFapHeroFinish(String title, String imageurl, String videourl, String duration);

        //手办图鉴的
        void onHpoiFinish(String title, String imageurl, String url);

        //amsr的
        void onAsmrFinish(String id, String title, String name, String release, JSONArray vas, JSONArray tags, String CoverUrl);
    }
}