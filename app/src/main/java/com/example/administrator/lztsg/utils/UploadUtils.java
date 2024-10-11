package com.example.administrator.lztsg.utils;

import android.util.Log;

import com.example.administrator.lztsg.httpjson.HtmlServiceOkHttp;
import com.example.administrator.lztsg.httpjson.HttpCallbackListener;
import com.example.administrator.lztsg.httpjson.HttpLoadDataResolution;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class UploadUtils {
    private static final int TINE_OUT = 10 * 1000;
    private static final String CHARSET = "UTF-8";
    private static String path;
    private static String videourl;

    public static void uploadFile(File file, String requestURL, final HttpLoadDataResolution resolution) {
//        if (file != null && file.exists()) {
//            Log.d("test", "upload start");
//            try {
//                // 创建文件输入流
//                FileInputStream fis = new FileInputStream(file);
//                // 创建字节数组输出流
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                // 创建缓冲区
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                // 从文件输入流读取数据，并写入字节数组输出流
//                while ((bytesRead = fis.read(buffer)) != -1) {
//                    bos.write(buffer, 0, bytesRead);
//                }
//                // 关闭输入流和输出流
//                fis.close();
//                bos.close();
//                // 获取字节数组
//                byte[] bytes = bos.toByteArray();
//
//                // 创建请求体
//                RequestBody body = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("image", file.getName(),
//                                RequestBody.create(MediaType.parse("image/*"), bytes))
//                        .build();
//
//                HtmlServiceOkHttp.postHtml(requestURL, body, new HttpCallbackListener() {
//                    @Override
//                    public void onFinish(String response) {
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            String loadurl = json.getString("url");
//                            if (loadurl.contains("preview")) {
//                                loadurl = loadurl.replace("preview", "orig");
//                            }
//                            Log.d("test", "upload end");
//                            path = "https://yandex.com/images/search?rpt=imageview&url=" + loadurl;
//                            Log.d("test", "upload: " + response);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        getData(resolution);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TINE_OUT);//设置连接超时为10s
            connection.setReadTimeout(TINE_OUT);//设置读取操作超时为10s
            connection.setDoInput(true);//允许输入流
            connection.setDoOutput(true);//允许输出流
            connection.setUseCaches(false);//是否缓存
            connection.setRequestMethod("POST");//设置请求方式为POST
            connection.setRequestProperty("Charset", CHARSET);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-type", "image/jpeg");

            if (file != null) {
                Log.d("test", "upload start");
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                InputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(bytes)) != -1) {
                    dataOutputStream.write(bytes, 0, len);
                    Log.d("test", "upload bytes: " + len);
                }
                inputStream.close();
                dataOutputStream.flush();
                Log.d("test", "upload end");
                int responsCode = connection.getResponseCode();
                if (responsCode == 200) {
                    InputStream inputStream1 = connection.getInputStream();
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((len = inputStream1.read(bytes)) != -1) {
                        stringBuilder.append(new String(bytes, 0, len, "UTF-8"));
                    }
                    JSONObject json = new JSONObject(stringBuilder.toString());
                    String loadurl = json.getString("url");
                    if (loadurl.contains("preview")) {
                        loadurl = loadurl.replace("preview", "orig");
                    }
                    path = "https://yandex.com/images/search?rpt=imageview&url=" + loadurl;
                    Log.d("test", "upload: " + stringBuilder.toString());
                    getData(resolution);
                } else {
                    Log.d("test", "upload fail");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getData(final HttpLoadDataResolution resolution) {
        int GOingUrl = 1;
        HtmlServiceOkHttp.getHtml(path, "", GOingUrl, new HttpCallbackListener() {
            @Override
            public String onFinish(String response) {
                //生成遍历
                Document document = Jsoup.parse(response, path);
                Elements itemthumb = document.select("div.CbirSites-ItemThumb a[href]");
                Elements itemthumb_mark = document.select("div.Thumb-Mark");
                Elements itemtitle = document.select("div.CbirSites-ItemTitle a[href]");
                Elements itemdomain = document.select("div.CbirSites-ItemInfo>a[href]");
                Elements itemdescription = document.select("div.CbirSites-ItemDescription");
                Elements similar_images = document.select("div.MMImage_type_cover");
                if (itemthumb != null) {
                    ArrayList<String> imgList = new ArrayList<String>();
                    ArrayList<String> imgsizeList = new ArrayList<String>();
                    ArrayList<String> titleList = new ArrayList<String>();
                    ArrayList<String> titleurlList = new ArrayList<String>();
                    ArrayList<String> domainList = new ArrayList<String>();
                    ArrayList<String> descriptionList = new ArrayList<String>();
                    //判断获取筛选标签
                    for (int size = 0; size < itemthumb.size(); size++) {
                        //录入所有链接到videoList里面
                        imgList.add(itemthumb.get(size).attr("abs:href")); //图片
                        imgsizeList.add(itemthumb_mark.get(size).text()); //图片尺寸
                        titleList.add(itemtitle.get(size).text()); //标题
                        titleurlList.add(itemtitle.get(size).attr("abs:href")); //跳转链接
                        domainList.add(itemdomain.get(size).text()); //网址信息
                        descriptionList.add(itemdescription.get(size).text()); //其他信息
                    }

                    //输出数据
                    if (imgList.size() == itemthumb.size() && resolution != null) {
                        int loging = 0;
                        while (loging <= (imgList.size() - 1)) {
                            String imageurl = imgList.get(loging);
                            String size = imgsizeList.get(loging);
                            String title = titleList.get(loging);
                            String titleurl = titleurlList.get(loging);
                            String domain = domainList.get(loging);
                            String description = descriptionList.get(loging);
                            //回调onFinish方法
                            resolution.onFinish(imageurl, size, title, titleurl, domain, description);
                            loging++;
                        }
                    }
//                    videourl = path + "&cbir_page=similar";
//                    indata(videourl,resolution);
                }
                return response;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

//    private static void indata(final String videourl,final HttpLoadDataResolution resolution) {
//        int GOingUrl = 2;
//        HtmlService.getHtml(path, videourl, GOingUrl, new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) {
//                //成功回调
//                Document doc = Jsoup.parse(response);
//                //选择器选择
//                Elements similar_images = doc.select("img.serp-item__thumb");
//                if (similar_images != null){
//                    ArrayList<String> similar_imgList = new ArrayList<String>();
//                    for (int size= 0;size<similar_images.size();size++){
//                        similar_imgList.add("https:"+similar_images.get(size).attr("src"));
//
//                        //输出相似图片数据
//                        resolution.onSimilarimgFinish(similar_imgList.get(size));
//                    }
//                }
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//    }
}
