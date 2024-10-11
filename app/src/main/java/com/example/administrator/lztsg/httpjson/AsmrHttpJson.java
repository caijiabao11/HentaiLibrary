package com.example.administrator.lztsg.httpjson;

import android.util.Log;

import com.example.administrator.lztsg.activity.AsmrActivity;
import com.example.administrator.lztsg.activity.AsmrMoreFragment;
import com.example.administrator.lztsg.activity.AsmrSearchFragment;
import com.example.administrator.lztsg.items.Asmr_chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AsmrHttpJson {
    private static HttpJsonResolution mResolution;
    public static String path;
    public static String videourl;
    public static ArrayList<String> Allname = new ArrayList<>(),Allimg = new ArrayList<>(),Allvideourl = new ArrayList<>(),Allduration = new ArrayList<>();
    public static String datas = null,imgsrc,name;


    public static void getData(String data,int page, final HttpAsmrJsonResolution resolution){
        if (AsmrActivity.currentFragment instanceof AsmrMoreFragment){
            //主页链接
            path = "https://api.asmr-300.com/api/works?order=create_date&sort=desc&page="+page+"&subtitle=0";
        }else if (AsmrActivity.currentFragment instanceof AsmrSearchFragment){
            //搜索链接
            path = "https://api.asmr.one/api/search/"+data+"?order=create_date&sort=desc&page="+page+"&subtitle=0";
        }

        int GOingUrl = 1;

        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                //生成遍历
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArray = json.getJSONArray("works");
                    for (int i=0;i<=jsonArray.length();i++){
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        JSONArray vas = new JSONArray(),
                                tags = new JSONArray();

                        String id = obj.getString("id");
                        String title = obj.getString("title");
                        String name = obj.getString("name");
                        String release = obj.getString("release");
                        vas = obj.getJSONArray("vas");
                        tags = obj.getJSONArray("tags");
                        String CoverUrl = obj.getString("mainCoverUrl");
                        resolution.onFinish(id,title,name,release,vas,tags,CoverUrl);
                    }
                    Log.d("jsonArray", "onFinish: "+jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public static void indata(final String videourl, final HttpAsmrJsonResolution resolution) {
        int GOingUrl = 2;
        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {

            @Override
            public String onFinish(String response) {
                //成功回调
                try {
                    JSONArray json = new JSONArray(response);
                    resolution.onTracksFinish(json);
                    Log.d("jsonArray", "onFinish: "+json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void onError(Exception e) {
                //错误回调
            }
        });
    }
    //获取amsr首页页面总数
    public static void getPage(String path,final HttpAsmrJsonResolution resolution){

        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path,"",GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {

                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject obj = json.getJSONObject("pagination");

                    double totalCount = Double.parseDouble(obj.getString("totalCount"));
                    //20，代表一页显示20个
                    int totalSeconds = (int) Math.round(totalCount / 20); // 将持续时间四舍五入为最接近的整数秒数
                    resolution.onPages(totalSeconds);

                    Log.d("jsonArray", "onFinish: "+json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    //获取amsr_mass数据
    public static void getAsmr_massData(final HttpAsmr_ChipJsonResolution resolution){
        //社团列表链接
        path = "https://api.asmr.one/api/circles/";
        int GOingUrl = 1;

        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                //生成遍历
                try {
//                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<Asmr_chip> asmr_masschip = new ArrayList<>();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        String id = obj.getString("id");
                        String name = obj.getString("name");
                        String count = obj.getString("count");
//                        resolution.onFinish(id,title,name,release,vas,tags,CoverUrl);
                        asmr_masschip.add(new Asmr_chip(id,name,count));
                    }
                    resolution.onFinish(asmr_masschip);
                    Log.d("jsonArray", "onFinish: "+jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    //获取amsr_tag数据
    public static void getAsmr_tagData(final HttpAsmr_ChipJsonResolution resolution){
        //标签列表链接
        path = "https://api.asmr.one/api/tags/";
        int GOingUrl = 1;

        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                //生成遍历
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<Asmr_chip> asmr_tagchip = new ArrayList<>();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        String id = obj.getString("id");
                        String name = obj.getString("name");
                        String count = obj.getString("count");
                        asmr_tagchip.add(new Asmr_chip(id,name,count));
                    }
                    resolution.onFinish(asmr_tagchip);
                    Log.d("jsonArray", "onFinish: "+jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    //获取amsr_vas数据
    public static void getAsmr_vasData(final HttpAsmr_ChipJsonResolution resolution){
        //女优列表链接
        path = "https://api.asmr.one/api/vas/";
        int GOingUrl = 1;

        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                //生成遍历
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<Asmr_chip> asmr_vaschip = new ArrayList<>();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        String id = obj.getString("id");
                        String name = obj.getString("name");
                        String count = obj.getString("count");
                        asmr_vaschip.add(new Asmr_chip(id,name,count));
                    }
                    resolution.onFinish(asmr_vaschip);
                    Log.d("jsonArray", "onFinish: "+jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    //获取amsr_random数据
    public static void getAsmr_randomData(final HttpAsmrJsonResolution resolution){
        //随身听链接
        path = "https://api.asmr.one/api/works?order=betterRandom";
        int GOingUrl = 1;

        HtmlServiceOkHttp.getHtml(path,videourl,GOingUrl,new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                //生成遍历
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArray = json.getJSONArray("works");
                    for (int i=0;i<=jsonArray.length();i++){
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        JSONArray vas = new JSONArray(),
                                tags = new JSONArray();

                        String id = obj.getString("id");
                        String title = obj.getString("title");
                        String name = obj.getString("name");
                        String release = obj.getString("release");
                        vas = obj.getJSONArray("vas");
                        tags = obj.getJSONArray("tags");
                        String CoverUrl = obj.getString("mainCoverUrl");
                        resolution.onFinish(id,title,name,release,vas,tags,CoverUrl);
                    }
                    Log.d("jsonArray", "onFinish: "+jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
