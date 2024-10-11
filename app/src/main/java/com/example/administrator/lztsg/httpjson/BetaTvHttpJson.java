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
//        int page = getPageTotal();
        path = "https://hentaigasm.com";
        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                //生成遍历
                Document document = Jsoup.parse(response,path);
                Elements divVideourl = document.select("div.widget-posts > ul > li")
                        .select(".thumb > a[href]");
                Elements divImg = divVideourl.select("span.clip > img[src]");

                if (divVideourl != null){
                    ArrayList<String> itemList = new ArrayList<String>();
                    ArrayList<String> titList = new ArrayList<String>();
                    ArrayList<String> thumbList = new ArrayList<String>();
                    //判断获取筛选标签
                    for (int size=0;size<divVideourl.size();size++){
                        //录入所有链接到videoList里面
                        String url = divVideourl.get(size).attr("abs:href");
                        String alt = divVideourl.get(size).attr("title");
                        String img = divImg.get(size).attr("src");

//                        String durantion = divDuration.get(size).text();

                        resolution.onFinish(alt,img,url,"");
                    }

//                    int random = getRandomVideo();
                    //第二详细页视频链接
//                    if (itemList.size() >= random){
//                        videourl = itemList.get(random);
//                        name = titList.get(random);
//                        imgsrc = thumbList.get(random);
//                        indata(videourl,resolution);
//                    }
                }
                return response;
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
            public String onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                //选择器选择scriprt type="text/javascript"
                Element scriptEle = doc.select("div#player_01 + script[type=\"text/javascript\"]").first();

                try{
                    Pattern pattern = Pattern.compile("\\{([^{}]+)\\}");
                    Matcher matcher = pattern.matcher(scriptEle.data());

                    if (matcher.find()) {
                        String extractedContent = matcher.group(1);
                        String formattedContent = "{" + extractedContent.replaceAll("\\s*([,{}])\\s*", "$1").replaceAll("(\\w+):\\s*\"(.*?)\"", "\"$1\": \"$2\"") + "}";
                        // 去除末尾逗号
                        formattedContent = formattedContent.replaceAll(",\\s*\\}", "}");

                        JSONObject json = new JSONObject(formattedContent);

                        String imageurl = json.getString("image");
                        String videourl = json.getString("file");
                        resolution.onFinish("", imageurl, videourl, "");

                        Log.e("text数据","text数据"+scriptEle.data());
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Log.e("视频链接失效", "run: "+ e );
//                    deurl++;
                }
                return response;
            }

            @Override
            public void onError(Exception e) {
                //错误回调
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
