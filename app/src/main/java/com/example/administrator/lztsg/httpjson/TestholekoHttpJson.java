package com.example.administrator.lztsg.httpjson;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class TestholekoHttpJson {
    private static HttpJsonResolution mResolution;
    public static String path;
    public static String videourl;
    public static ArrayList<String> Allname = new ArrayList<>(),Allimg = new ArrayList<>();
    public static String datas = null;

//    public static void setmResolution(HttpJsonResolution resolution) {
//        mResolution = resolution;
//    }

    public static void getData(final HttpJsonResolution resolution){
        //主页链接
        path = "https://spankbang.com/profile/jiongge";
        HtmlService.getHtml(path,videourl,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //生成遍历
                Document document = Jsoup.parse(response,path);
                Elements divVideo = document.select("div.video-list")
                        .select(".video-rotate")
                        .select(".video-list-with-ads").get(0)
                        .select("div.video-item a[href]")
                        .select(".thumb");
                if (divVideo != null){
                    ArrayList<String> videoList = new ArrayList<String>();
                    //判断获取筛选标签
                    for (int size=0;size<divVideo.size();size++){
                        //录入所有链接到videoList里面
                        videoList.add(divVideo.get(size).attr("abs:href"));
                        //第二详细页视频链接
                        videourl = videoList.get(size);
                        indata(videourl,resolution,divVideo,size);
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void indata(final String videourl, final HttpJsonResolution resolution, final Elements divVideo, final int size) {
        HtmlService.getHtml(path,videourl,new HttpCallbackListener() {
            private TestholekoHttpJson httpJson;

            @Override
            public void onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                //选择器选择scriprt type="application/ld+json"
                Element scriptEle = doc.select("script[type=\"application/ld+json\"]").first();
//                StringBuilder sb = new StringBuilder();


//                if (divVideo.size() == (size+1)){
//                    datas = sb.toString();
                    try{
                        JSONObject json = new JSONObject(scriptEle.data());
                        httpJson.Allname.add((String)json.get("name"));
                        httpJson.Allimg.add((String) json.get("thumbnailUrl"));

                        String videourl = (String) json.get("contentUrl");
                        Log.e("text数据","text数据"+scriptEle.data());
                        if (Allname.size() % 10== 0 && resolution!=null) {
                                int loging = 0;
                            while (loging <= (Allname.size()-1)) {
                                String text = Allname.get(loging);
                                String imageurl = Allimg.get(loging);
                                //回调onFinish方法
                                resolution.onFinish(text, imageurl, videourl);
                                loging += 1;
                            }
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
            }

            @Override
            public void onError(Exception e) {
                //错误回调
            }
        });
    }
}
