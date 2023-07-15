package com.example.administrator.lztsg.httpjson;

import android.content.Context;
import android.util.Log;

import com.example.administrator.lztsg.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CunzhiHellHttpJson {
    private static HttpJsonResolution mResolution;
    public static String path;
    public static String videourl;
    public static ArrayList<String> Allname = new ArrayList<>(),Allimg = new ArrayList<>(),Allvideourl = new ArrayList<>(),Allduration = new ArrayList<>();
    public static String datas = null;
    public static FileOutputStream fos;
    public static FileInputStream fis;
    public static int deurl;//失效链接数量
    public static int okvidos;//另一个网站成功链接数量
    private static Context context = MyApplication.getContext();

//    public static void setmResolution(HttpJsonResolution resolution) {
//        mResolution = resolution;
//    }

    public static void getData(final HttpJsonResolution resolution){
        //主页链接
        path = "https://spankbang.com/5nzsi/video/porn";

        int GOingUrl = 1;
        HtmlService.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //生成遍历
                String sit = showInput();

                if ((fis != null && response.length() > sit.length() ) ||  fis == null){

                    //内部存储源码
                    showOutput(response);
                    sit = response;
                }

                Document document = Jsoup.parse(sit,path);
                Elements divVideo = document.select("div.video-list")
                        .select(".video-rotate").get(1)
                        .select("div.video-item>a[href]")
                        .select(".thumb");
                if (divVideo != null){
                    ArrayList<String> videoList = new ArrayList<String>();
                    //判断获取筛选标签
                    for (int size=0;size<divVideo.size();size++){
                        //录入所有链接到videoList里面
                        videoList.add(divVideo.get(size).attr("abs:href"));
                    }
                    videoList.add("https://sharesome.com/api/videos?user=694125&limit=12&page=2");
                    videoList.add("https://spankbang.com/5nzsi/video/");
                    for (String urlinto:videoList){
                        //第二详细页视频链接
                        videourl = urlinto;
                        indata(videourl,resolution,videoList);
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void indata(final String videourl, final HttpJsonResolution resolution, final ArrayList<String> allvideoList) {
        int GOingUrl = 2;
        HtmlService.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            private CunzhiHellHttpJson httpJson;
            private String oenUrl = "https://spankbang.com/5nzsi/video/";
            private String toUrl = "https://sharesome.com/api/videos?user=694125&limit=12&page=2";
            @Override
            public void onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                if (videourl.equals(toUrl)){
                    try {
                        int loging = 0;
                        while (loging <= 5){
                            JSONObject json = new JSONObject(response).getJSONArray("data").getJSONObject(loging);
                            httpJson.Allname.add(json.getString("title"));
                            httpJson.Allimg.add("https:"+json.getString("thumb"));
                            httpJson.Allvideourl.add("https:"+json.getString("mp4_url"));
                            httpJson.Allduration.add(json.getString("duration"));

                            int towloging = Allname.size() - 1;
                            Log.e("text数据"+Allname.get(towloging),"text数据"+json);

                            okvidos++;
                            if(okvidos == 6){
                                okvidos--;
                            }
//                            String title = Allname.get(towloging);
//                            String imageurl = Allimg.get(towloging);
//                            String videourl = Allvideourl.get(towloging);
//                            String duration = Allduration.get(towloging);
//                            //回调onFinish方法
//                            resolution.onFinish(title, imageurl, videourl, duration);
                            loging ++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("视频链接失效", "run: "+ e );
                        deurl++;
                    }

                }else{
                    //选择器选择scriprt type="application/ld+json"
                    Element scriptEle = doc.select("script[type=\"application/ld+json\"]").first();
                    try{
                        JSONObject json = new JSONObject(scriptEle.data());
                        httpJson.Allname.add(json.getString("name"));
                        httpJson.Allimg.add(json.getString("thumbnailUrl"));
                        httpJson.Allvideourl.add(json.getString("contentUrl"));
                        httpJson.Allduration.add(json.getString("duration"));

                        Log.e("text数据","text数据"+scriptEle.data());

                    } catch (JSONException e){
                        e.printStackTrace();
                        Log.e("视频链接失效", "run: "+ e );
                        deurl++;
                    }
                }

                //Allname.size() % 10== 0 &&  10数量的余0
                if (Allname.size() == allvideoList.size() - deurl + okvidos && resolution!=null) {
                    int loging = 0;
                    while (loging <= (Allname.size()-1)) {
                        String title = Allname.get(loging);
                        String imageurl = Allimg.get(loging);
                        String videourl = Allvideourl.get(loging);
                        String duration = Allduration.get(loging);
                        //回调onFinish方法
                        resolution.onFinish(title, imageurl, videourl, duration);
                        loging ++;
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                //错误回调
            }
        });
    }

    public static void showOutput(String res) {
        //写入内部存储文件
        try {
            fos = context.openFileOutput("CunZhiHtml.txt", Context.MODE_PRIVATE);
            fos.write(res.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String showInput() {
        //读取内部存储文件
        try {
            fis = context.openFileInput("CunZhiHtml.txt");

            int len = 0;
            byte[] buf = new byte[1024];
//            StringBuilder sb = new StringBuilder();//动态拼接
            String line = null;
            while ((len = fis.read(buf)) != -1) {
                line += new String(buf, 0, len);
            }

            return line;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
