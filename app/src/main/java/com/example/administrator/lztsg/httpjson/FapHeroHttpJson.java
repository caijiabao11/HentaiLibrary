package com.example.administrator.lztsg.httpjson;

import android.content.Context;
import android.util.Log;

import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.items.MultipleItem;
import com.example.administrator.lztsg.items.TestholeItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FapHeroHttpJson {
    public static String path;
    public static String videourl;
    public static ArrayList<String> Allname = new ArrayList<>(),Allimg = new ArrayList<>(),Allvideourl = new ArrayList<>(),Allduration = new ArrayList<>();
    public static String datas = null;
    public static FileOutputStream fos;
    public static FileInputStream fis;
    public static  int pagination = 1;
    public static int deurl;//失效链接数量
    private static Context context = MyApplication.getContext();

    public static void getData(final HttpJsonResolution resolution){
        //主页链接
//        path = "https://spankbang.com/profile/jiongge";
        path = "https://spankbang.com/profile/fuckme6974/videos?o=new&page=" + pagination;
        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
//                FileIOStream fileIOStream = new FileIOStream(context);
//                File fileDir = context.getFilesDir(); // 或者使用 getCacheDir() 获取缓存目录
//                File outputFile = new File(fileDir, "FapHeroHtml.txt");// 本地保存的文件路径和名称
//
//                String sit = fileIOStream.showInput(outputFile);
//                if(!outputFile.exists() || outputFile.exists() && response.length() > sit.length()){
//                    //内部存储源码
//                    fileIOStream.showOutput(outputFile,response);
//                    sit = response;
//                }

                //生成遍历
                Document document = Jsoup.parse(response,path);
                Elements divVideo = document.select("nav.nav_quick_top + div.video-list")
                        .select("div.video-item a[href]").select(".thumb");
                Elements divImg = document.select("nav.nav_quick_top + div.video-list")
                        .select("div.video-item a")
                        .select(".thumb > picture > img[src]");
                Elements divDuration = document.select("nav.nav_quick_top + div.video-list")
                        .select("div.video-item a")
                        .select(".thumb > p.t > span.l");
                if (divVideo != null){
                    ArrayList<MultipleItem> videoList = new ArrayList<>();
                    //判断获取筛选标签
                    for (int size=0;size<divVideo.size();size++){
                        //录入所有链接到videoList里面
                        String alt = divImg.get(size).attr("alt");
                        String img = divImg.get(size).attr("abs:data-src");
                        String url = divVideo.get(size).attr("abs:href");
                        String durantion = divDuration.get(size).text();
                        videoList.add(new TestholeItem(alt,img,url,durantion));

                        resolution.onFinish(alt,img,url,durantion);
                    }
                }
//                return sit;
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void getVideosPage() {
        path = "https://spankbang.com/profile/fuckme6974/videos";

        HtmlServiceOkHttp.getHtml(path, videourl, 1, new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                Document document = Jsoup.parse(response,path);
                Elements page = document.select("div.pagination > ul > li");
                int index = page.size() - 2;
                if (index > 1){
                    FapHeroHttpJson.pagination++;
                }
                return String.valueOf(index);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void indata(final String videourl, final HttpJsonResolution resolution, final ArrayList<String> allvideoList) {
        int GOingUrl = 2;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {

            @Override
            public String onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                //选择器选择scriprt type="application/ld+json"
                Element scriptEle = doc.select("script[type=\"application/ld+json\"]").first();
                    try{
                        JSONObject json = new JSONObject(scriptEle.data());

                        String title = json.getString("name");
                        String imageurl = json.getString("thumbnailUrl");
                        String videourl = json.getString("contentUrl");
                        String duration = json.getString("duration");
                        resolution.onFinish(title, imageurl, videourl, duration);

                        Log.e("text数据","text数据"+scriptEle.data());

                    } catch (JSONException e){
                        e.printStackTrace();
                        Log.e("视频链接失效", "run: "+ e );
                        deurl++;
                    }
                return response;
            }

            @Override
            public void onError(Exception e) {
                //错误回调

            }
        });
    }
}

