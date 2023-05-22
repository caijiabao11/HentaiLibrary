package com.example.administrator.lztsg;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.example.administrator.lztsg.activity.TestholekilnActivity;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager manager;
    private Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.lztsg.alarmreceiver_start")){
//            Toast.makeText(context,"接收到了广播",Toast.LENGTH_SHORT).show();
            //设置通知内容并在onReceive()这个函数执行时开启
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("leo","测试",
                        NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            //创建意图 setContentIntent的参数PendingIntent
            Intent intent1 = new Intent(context, TestholekilnActivity.class);
            //参数：上下文环境，请求码，Intent，标记
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,0);

            //创建Notification
            notification = new NotificationCompat.Builder(MyApplication.getContext(),"leo")
                    .setContentTitle("试炼洞窑") //设置标题(必须设置)
                    .setContentText("图书姬：勇者Sama，还要挑战一次吗？") //设置文本内容(必须设置)
                    .setSmallIcon(R.mipmap.ic_launcher_lizhitusuguang_icon) //设置小图标(必须设置)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.longrunning_largeicon_1)) //设置通知的大图标
                    .setColor(Color.parseColor("#FFFFFFFF")) //设置小图标的颜色
                    .setContentIntent(pendingIntent) //设置点击通知后的跳转意图
                    .setAutoCancel(true) //设置点击通知后自动清除通知
                    .build();
            manager.notify(1,notification);

        }

        if (intent.getAction().equals("com.lztsg.alarmreceiver_stop")){
            Log.e("关闭广播","关闭广播");
            context.startService(new Intent(context,LongRunningService.class));
        }
    }
}
