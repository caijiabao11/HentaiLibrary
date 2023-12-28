package com.example.administrator.lztsg.httpjson;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetaTvHttpJson {
    private static HttpJsonResolution mResolution;
    public static String path;
    public static String videourl;
    public static ArrayList<String> Allname = new ArrayList<>(),Allimg = new ArrayList<>(),Allvideourl = new ArrayList<>(),Allduration = new ArrayList<>();
    public static String datas = null,imgsrc,name;


    public static void getData(final HttpJsonResolution resolution){
        //主页链接
        int page = getPageTotal();
        path = "https://www.2cyavcut.cfd/index.php/vodtype/43-"+ page +".html";
        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //生成遍历
                Document document = Jsoup.parse(response,path);
                Elements divVideourl = document.select("div.margin-fix")
                        .select(".item>a[href]");

                Elements divImg = divVideourl.select("div.img>.thumb");

                if (divVideourl != null){
                    ArrayList<String> itemList = new ArrayList<String>();
                    ArrayList<String> titList = new ArrayList<String>();
                    ArrayList<String> thumbList = new ArrayList<String>();
                    //判断获取筛选标签
                    for (int size=0;size<divVideourl.size();size++){
                        //录入所有链接到videoList里面
                        itemList.add(divVideourl.get(size).attr("abs:href"));
                        titList.add(divVideourl.get(size).attr("title"));
                        thumbList.add(divImg.get(size).attr("src"));
                    }

                    int random = getRandomVideo();
                    //第二详细页视频链接
                    if (itemList.size() >= random){
                        videourl = itemList.get(random);
                        name = titList.get(random);
                        imgsrc = thumbList.get(random);
                        indata(videourl,resolution);
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void indata(final String videourl, final HttpJsonResolution resolution) {
        int GOingUrl = 2;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            private BetaTvHttpJson httpJson;

            @Override
            public void onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                //选择器选择scriprt type="text/javascript"
                Element scriptEle = doc.select("script[type=\"text/javascript\"]").get(6);

                Element tit = doc.select("div.headline>h1").first();
                //标题
//                name = tit.text();

                String javascriptCode = scriptEle.data();
                String playerDataString = javascriptCode.substring(javascriptCode.indexOf("{"), javascriptCode.lastIndexOf("}") + 1);
                try{
                    JSONObject json = new JSONObject(playerDataString);
                    String url = json.getString("url");
                    String videourl = "https://www.2cyavcut.cfd/addons/dplayer/?url=" + url;
                    indataoen(videourl,resolution);
                    Log.d("url", "onFinish: "+url);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                //错误回调
            }
        });
    }
    public static void indataoen(final String videourl, final HttpJsonResolution resolution){
        int GOingUrl = 2;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Document doc = Jsoup.parse(response);
                Element videom3u8 = doc.select("script[type=\"text/javascript\"]").get(2);



//                Elements videom3u8 = doc.select("video source[src]");
//                Elements img = doc.select("video");
                if(videom3u8 != null){
                    String title = name;
                    String imageurl = imgsrc;
                    String videourl = "";
                    String duration = "";

                    Pattern pattern = Pattern.compile("var urls = \"(.*?)\";");
                    Matcher matcher = pattern.matcher(videom3u8.data());
                    if (matcher.find()) {
                        videourl = matcher.group(1);
                    }
                    Log.d("Video is ok !!!", videourl);
                    resolution.onFinish(title,imageurl,videourl,duration);

                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static int getPageTotal(){
        //随机范围1-13
        Random random = new Random();
        int randomNumber = random.nextInt(13) + 1;
        return randomNumber;
    }

    public static int getRandomVideo(){
        //随机范围0-36
        Random random = new Random();
        int randomNumber = random.nextInt(36);
        return randomNumber;
    }
}
