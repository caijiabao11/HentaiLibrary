package com.example.administrator.lztsg.httpjson;

import android.content.Context;
import android.util.Log;

import com.example.administrator.lztsg.MyApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HpoiHttpJson {
    public static String path;
    public static ArrayList<String> Allname = new ArrayList<>(),Allimg = new ArrayList<>(),Allvideourl = new ArrayList<>(),Allduration = new ArrayList<>();
    public static String datas = null;
    public static int deurl;//失效链接数量
    private static Context context = MyApplication.getContext();


    public static void getData(String selecttopage,final HttpHpoiDataResolution resolution){
        //热门浏览界面链接
        path = "https://www.hpoi.net/hobby/all?order=hits7Day&r18=-1&workers=&view=4&category=100" + selecttopage;

        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,"",GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {

                //生成遍历
                Document document = Jsoup.parse(response,path);
                Elements divUrls = document.select("div#content")
                        .select(".row div a[href]");
                Elements divTit = document.select("div#content")
                        .select(".row div>p");
                Elements divImg = document.select("div#content")
                        .select(".row div img[src]");

                if (divUrls != null){
                    //判断获取筛选标签
                    for (int size=0;size<divUrls.size();size++){

                        String tit = divTit.get(size).text();
                        String img = divImg.get(size).attr("src");
                        String url = divUrls.get(size).attr("abs:href");
                        resolution.onFinish(tit,img,url);
                    }
                }

                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void indata(final String path, final HttpHpoiDataResolution resolution) {
        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,"",GOingUrl,new HttpCallbackListener() {
            private HpoiHttpJson httpJson;

            @Override
            public String onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                //选择器选择scriprt
                Elements item_name = doc.select("div.hpoi-infoList-item>span");
                Elements item_value = doc.select("div.hpoi-infoList-item>p");

                Elements item_userpic = doc.select("ul.navbar-nav li").get(1).select("a[href]");
                //判断获取筛选标签
                if (item_name != null){

                    String userpic = item_userpic.attr("abs:href");
                    ArrayList<String> item_nameList = new ArrayList<String>();
                    ArrayList<String> item_tagList = new ArrayList<String>();

                    for (int size = 0; size < item_name.size(); size++) {
                        //录入所有链接到videoList里面
                        item_nameList.add(item_name.get(size).text());

                        if (item_name.get(size).text().equals("外部链接")){
                            item_tagList.add(item_value.get(size).attr("abs:href"));
                        }else {
                            item_tagList.add(item_value.get(size).text());
                        }
                        resolution.onInitData(item_nameList.get(size),item_tagList.get(size));
                    }
                    inituserpic(userpic,resolution);
                }
                return response;
            }

            @Override
            public void onError(Exception e) {
                //错误回调

            }
        });
    }

    public static void inituserpic(String userpicurl,final HttpHpoiDataResolution resolution){
        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(userpicurl, "", GOingUrl, new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                //选择器选择scriprt
                Elements item_userpic = doc.select("div.av-masonry-container img");
                Elements item_value = doc.select("li.dropdown ul.dropdown-menu>li>a[href]");


                for (int size = 0; size < item_userpic.size(); size++) {
                    //录入所有链接到videoList里面
//                    item_nameList.add(item_name.get(size).text());

//                    String link = "https://www.hpoi.net/album/new?itemType=hobby&itemId=8655316";
                    String link = item_value.get(0).attr("abs:href");
                    String pattern = "itemId=(\\d+)";

                    Pattern regexPattern = Pattern.compile(pattern);
                    Matcher matcher = regexPattern.matcher(link);
                    String number = null;
                    if (matcher.find()) {
                        number = matcher.group(1);
                        Log.i("Extracted number: ", "" + number);
                    } else {
                        Log.i("No match found","number");
                    }
                    String img = item_userpic.get(size).attr("src");
                    resolution.onFinish("",img,number);
                }
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void loadtopic(final String path, final HttpHpoiDataResolution resolution) {
        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,"",GOingUrl,new HttpCallbackListener() {
            private HpoiHttpJson httpJson;

            @Override
            public String onFinish(String response) {
                //成功回调
                Document doc = Jsoup.parse(response);
                //选择器选择scriprt
                Elements item_userpic = doc.select("div.av-masonry-image-container img");

                //判断获取筛选标签
                if (item_userpic != null){

                    for (int size = 0; size < item_userpic.size(); size++) {
                        //录入所有链接到videoList里面
                        String img = item_userpic.get(size).attr("src");
                        resolution.onFinish("",img,"");
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

    public static void getPage(String path,final HttpHpoiDataResolution resolution){

        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,"",GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {

                //生成遍历
                Document document = Jsoup.parse(response,path);
                Elements page = document.select("ul.hpoi-pagination li");

                if (page != null && page.size() >0){
                    //判断获取筛选标签
                    String total = page.get(page.size()-2).text();
                    Integer totalint = Integer.parseInt(total);
                    resolution.onPages(totalint);
                }
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}

