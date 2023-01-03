package com.example.administrator.lztsg.httpjson;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetaTvHttpJson {
    private static HttpJsonResolution mResolution;
    public static String path;
    public static String videourl;
    public static ArrayList<String> Allname = new ArrayList<>(),Allimg = new ArrayList<>(),Allvideourl = new ArrayList<>(),Allduration = new ArrayList<>();
    public static String datas = null,imgsrc;


    public static void getData(final HttpJsonResolution resolution){
        //主页链接
//        path = "https://hanime1.me/search?query=&genre=&sort=%E4%BB%96%E5%80%91%E5%9C%A8%E7%9C%8B&year=&month=&duration=";
        path = "https://www.underhentai.net/random/?nc";
        int GOingUrl = 1;
        HtmlService.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            private BetaTvHttpJson httpJson;


            @Override
            public void onFinish(String response) {
                //生成遍历
                Document document = Jsoup.parse(response, path);
//                Elements divVideo = document.select("div.row").select(".no-gutter")
//                        .select("div.col-xs-6")
//                        .select("div.hover-lighter a[href]");
                Elements divVideo = document.select("div.content-table table tbody>tr").eq(0)
                        .select("td.c8 a[href]");
                Elements imgSrc = document.select("div.loading")
                        .select("img.img-responsive").eq(0);
                if (divVideo != null) {
                    ArrayList<String> videoList = new ArrayList<String>();
                    //判断获取筛选标签
                    for (int size = 0; size < divVideo.size(); size++) {
                        //录入所有链接到videoList里面
                        videoList.add(divVideo.get(size).attr("abs:href" )+ "#ustream");
                    }
                    //遍历videoList
                    for (String urlinto:videoList){
                        //第二详细页视频链接
                        videourl = urlinto;
                        indata(videourl, resolution,videoList);
                    }
                    imgsrc =imgSrc.attr("src");
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
            private BetaTvHttpJson httpJson;

            @Override
            public void onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                //选择器选择scriprt type="application/ld+json"
//                Element scriptEle = doc.select("script[type=\"application/ld+json\"]").first();
                Elements scriptEle = doc.select("script");
                String pattern = "(?<=(\\$\\(\"#ustream\"\\)\\.append.{1,20}src=\\\\\")).*?(?=(\\\\\"))";
                String str = String.valueOf(scriptEle.get(14));
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(str);
                if(m.find()) {
                    Log.d("获取的数据为","获取的数据为"+m.group(0));
                    String videourl = m.group(0) + "?ads=false";
                    indataoen(videourl, resolution);
                } else {
                    Log.d("没找到","没找到");
                }
//                    try{
////                        scriptEle.data();
////                        JSONObject json = new JSONObject(scriptEle.data());
////                        httpJson.Allname.add(json.getString("name"));
////                        httpJson.Allimg.add(json.getJSONArray("thumbnailUrl").getString(0));
////                        httpJson.Allvideourl.add(json.getString("contentUrl"));
////                        httpJson.Allduration.add(json.getString("uploadDate"));
////
////                        Log.e("text数据","text数据"+scriptEle.data());
//                        //Allname.size() % 10== 0 &&  10数量的余0
//                        if (Allname.size() == allvideoList.size() && resolution!=null) {
//                                int loging = 0;
//                            while (loging <= (Allname.size()-1)) {
//                                String title = Allname.get(loging);
//                                String imageurl = Allimg.get(loging);
//                                String videourl = Allvideourl.get(loging);
//                                String duration = Allduration.get(loging);
//                                //回调onFinish方法
//                                resolution.onFinish(title, imageurl, videourl, duration);
//                                loging ++;
//                            }
//                        }
//                    } catch (JSONException e){
//                        e.printStackTrace();
//                    }
            }

            @Override
            public void onError(Exception e) {
                //错误回调
            }
        });
    }
    public static void indataoen(final String videourl, final HttpJsonResolution resolution){
        int GOingUrl = 2;
        HtmlService.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Document doc = Jsoup.parse(response);
                Elements videom3u8 = doc.select("video source[src]");
                Elements img = doc.select("video");
                if(videom3u8 != null){
                    String title = doc.title();
                    String imageurl = imgsrc;
                    String videourl = videom3u8.attr("src");
                    String duration = "";

                    Log.d("Video is ok !!!", videourl);
                    resolution.onFinish(title,imageurl,videourl,duration);

                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
