package com.example.administrator.lztsg.httpjson;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

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

public class HentaiJoiHttpJson {
    private static HttpJsonResolution mResolution;
    public static String path;
    public static String videourl;
    public static ArrayList<String> Allname = new ArrayList<>(),Allimg = new ArrayList<>(),Allvideourl = new ArrayList<>(),Allduration = new ArrayList<>();
    public static String datas = null;
    public static String sethtml;
    public static FileOutputStream fos = null;
    public static FileInputStream fis = null;
    public static int deurl;//失效链接数量
    private static Context context = MyApplication.getContext();

//    public static void setmResolution(HttpJsonResolution resolution) {
//        mResolution = resolution;
//    }

    @JavascriptInterface
    @SuppressWarnings("unused")
    public void processHTML(String html){
    // 注意啦，此处就是执行了js以后 的网页源码
        Log.d("源码"+html,"processHTML "+html);

        this.sethtml = html;
    }

    public static void getData(final HttpJsonResolution resolution){
        //主页链接
        path = "https://www.xvideos.com/channels/wutfaced#_tabVideos";
        int GOingUrl = 1;
        HtmlService.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            private HentaiJoiHttpJson httpJson;


            @Override
            public void onFinish(String response) {
                //生成遍历
                String sit = showInput();

                if ((sit != null && fis != null && sethtml.length() > sit.length() ) ||  fis == null){
                    //内部存储源码
                    showOutput(sethtml);
                    sit = sethtml;
                }else if (sit == null){
                    showOutput(sethtml);
                    sit = sethtml;
                }

                Document document = Jsoup.parse(sit, path);
                Elements divVideo = document.select("div.mozaique")
                        .select("div.thumb-block")
                        .select("div.thumb-inside")
                        .select("div.thumb>a[href]");
                if (divVideo != null) {
                    ArrayList<String> videoList = new ArrayList<String>();
                    //判断获取筛选标签
                    for (int size = 0; size < divVideo.size(); size++) {
                        //录入所有链接到videoList里面
                        videoList.add(divVideo.get(size).attr("abs:href"));

                    }
                    //遍历videoList
                    for (String urlinto:videoList){
                        //第二详细页视频链接
                        videourl = urlinto;
                        if (urlinto.contains("verification_video")) {
                            videoList.remove(urlinto);
                        }else {
                            indata(videourl, resolution,videoList);
                        }
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                //错误回调
                Log.e("hentai请求错误回调", "run: "+ e );
            }
        });
    }

    public static void indata(final String videourl, final HttpJsonResolution resolution, final ArrayList<String> allvideoList) {
        int GOingUrl = 2;
        HtmlService.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            private HentaiJoiHttpJson httpJson;

            @Override
            public void onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                //选择器选择scriprt type="application/ld+json"
                Element scriptEle = doc.select("script[type=\"application/ld+json\"]").first();
                    try{
                        JSONObject json = new JSONObject(scriptEle.data());
                        httpJson.Allname.add(json.getString("name"));
                        httpJson.Allimg.add(json.getJSONArray("thumbnailUrl").getString(0));
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

    public static void showOutput(String res) {
        //写入内部存储文件

        try {
            fos = context.openFileOutput("HenTaiHtml.txt", Context.MODE_PRIVATE);
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
            fis = context.openFileInput("HenTaiHtml.txt");

            int len = 0;
            byte[] buf = new byte[1024];
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
