package com.example.administrator.lztsg.httpjson;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileIOStream {
    private static FileOutputStream fos = null;
    private static FileInputStream fis = null;
    private static FapHeroHttpJson fapHeroHttpJsonl;
    private static Class<CunzhiHellHttpJson> cunzhiHellHttpJson = CunzhiHellHttpJson.class;
    private static Class<HentaiJoiHttpJson> hentaiJoiHttpJson = HentaiJoiHttpJson.class;
    private static Class<FapHeroHttpJson> fapHeroHttpJson = FapHeroHttpJson.class;
    private static String cunzhi,hentai,faphero;
    private static Context context;


    public static void showOutput(String res) {
        //写入内部存储文件

        try {
            cunzhi = cunzhiHellHttpJson.getSimpleName();
            hentai = hentaiJoiHttpJson.getSimpleName();
            faphero = fapHeroHttpJson.getSimpleName();

            Log.e("faphero", "showOutput: "+ faphero );
            Log.e("showOutput", "showOutput: "+ context.getClass().getName() );
            if ((context.getClass().getSimpleName()).equals(cunzhi)) {

                fos = context.openFileOutput("CunZhiHtml.txt", Context.MODE_PRIVATE);

            } else if ((context.getClass().getSimpleName()).equals(hentai)) {

                fos = context.openFileOutput("HenTaiHtml.txt", Context.MODE_PRIVATE);

            } else if ((context.getClass().getSimpleName()).equals(faphero)) {

                fos = context.openFileOutput("FapHeroHtml.txt", Context.MODE_PRIVATE);
                Log.e("FapHeroHtml", "showOutput: "+ "FapHeroHtml" );
            }
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
            if ((context.getClass().getSimpleName()).equals(cunzhi)) {

                fis = context.openFileInput("CunZhiHtml.txt");

            } else if ((context.getClass().getSimpleName()).equals(hentai)) {

                fis = context.openFileInput("HenTaiHtml.txt");

            } else if ((context.getClass().getSimpleName()).equals(faphero)) {

                fis = context.openFileInput("FapHeroHtml.txt");
            }

            int len = 0;
            byte[] buf = new byte[1024];
//            StringBuilder sb = new StringBuilder();//动态拼接
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
