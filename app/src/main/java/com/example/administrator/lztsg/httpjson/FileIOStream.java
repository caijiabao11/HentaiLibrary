package com.example.administrator.lztsg.httpjson;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileIOStream {
    private static Context context;

    public FileIOStream(Context context){
        this.context = context;
    }


    public void showOutput(File outputFile,String res) {
        //写入内部存储文件
        Log.e("showOutput", "showOutput: "+ context.getClass().getName() );
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(res.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String showInput(File outputFile) {
        //读取内部存储文件
        try {
            FileInputStream fis = new FileInputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            StringBuilder stringBuilder = new StringBuilder();

            while ((length = fis.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, length));
            }

            String fileContent = stringBuilder.toString();
            fis.close();

            // 处理文件内容
            return fileContent;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
