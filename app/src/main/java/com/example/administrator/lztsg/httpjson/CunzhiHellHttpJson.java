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


    public static void getData(final HttpJsonResolution resolution){
        //主页链接
        path = "https://jp.spankbang.com/5nzsi/video/";

        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                //生成遍历
                FileIOStream fileIOStream = new FileIOStream(context);
                File fileDir = context.getFilesDir(); // 或者使用 getCacheDir() 获取缓存目录
                File outputFile = new File(fileDir, "CunZhiHtml.txt");// 本地保存的文件路径和名称

                String sit = fileIOStream.showInput(outputFile);
                if(!outputFile.exists() || outputFile.exists() && response.length() > sit.length()){
                    //内部存储源码
                    fileIOStream.showOutput(outputFile,response);
                    sit = response;
                }
                Document document = Jsoup.parse(sit,path);
                Elements divVideo = document.select("section.user_uploads > div.video-list")
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
                return sit;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void indata(final String videourl, final HttpJsonResolution resolution, final ArrayList<String> allvideoList) {
        int GOingUrl = 2;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            private CunzhiHellHttpJson httpJson;
            private String oenUrl = "https://spankbang.com/5nzsi/video/";
            private String toUrl = "https://sharesome.com/api/videos?user=694125&limit=12&page=2";
            @Override
            public String onFinish(String response) {
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
                return response;
            }

            @Override
            public void onError(Exception e) {
                //错误回调
            }
        });
    }
}
