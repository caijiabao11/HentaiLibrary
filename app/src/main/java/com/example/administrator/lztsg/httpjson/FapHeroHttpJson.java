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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FapHeroHttpJson {
    public static String path;
    public static String videourl;
    public static ArrayList<String> Allname = new ArrayList<>(),Allimg = new ArrayList<>(),Allvideourl = new ArrayList<>(),Allduration = new ArrayList<>();
    public static String datas = null;
    public static FileOutputStream fos;
    public static FileInputStream fis;
    public static int deurl;//失效链接数量
    private static Context context = MyApplication.getContext();

    public static void getData(final HttpJsonResolution resolution){
        //主页链接
        path = "https://spankbang.com/profile/jiongge";
        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                FileIOStream fileIOStream = new FileIOStream(context);
                File fileDir = context.getFilesDir(); // 或者使用 getCacheDir() 获取缓存目录
                File outputFile = new File(fileDir, "FapHeroHtml.txt");// 本地保存的文件路径和名称

                String sit = fileIOStream.showInput(outputFile);
                if(!outputFile.exists() || outputFile.exists() && response.length() > sit.length()){
                    //内部存储源码
                    fileIOStream.showOutput(outputFile,response);
                    sit = response;
                }

                //生成遍历
                Document document = Jsoup.parse(sit,path);
                Elements divVideo = document.select("div.video-list")
                        .select(".video-rotate").get(1)
                        .select("div.video-item a[href]")
                        .select(".thumb");
                if (divVideo != null){
                    ArrayList<String> videoList = new ArrayList<String>();
                    //判断获取筛选标签
                    for (int size=0;size<divVideo.size();size++){
                        //录入所有链接到videoList里面
                        videoList.add(divVideo.get(size).attr("abs:href"));
                    }
//                    videoList.add("https://spankbang.com/5jh6d/video/fap+hero+marcurial+2+reupload");
                    videoList.add("https://spankbang.com/417hp-cr1e4w/playlist/fap+hero+w+similar");
//                    videoList.add("https://spankbang.com/78yfh/video/simple+beat+training+06+full+by+martoise");
                    videoList.add("https://spankbang.com/3zejh-3nfmp0/playlist/favorites");
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
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            private FapHeroHttpJson httpJson;

            @Override
            public void onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
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

                //Allname.size() % 10== 0 &&  10数量的余0
                if (Allname.size() == allvideoList.size() - deurl && resolution!=null) {
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
}

