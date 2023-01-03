package com.example.administrator.lztsg;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

import androidx.annotation.Nullable;

public class LongRunningService extends Service {
    private AlarmManager manager;
    private PendingIntent pendingIntent;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("service","service");
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //设置时间
        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalender = Calendar.getInstance();

        mCalender.setTimeInMillis(System.currentTimeMillis()); //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalender.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalender.set(Calendar.HOUR_OF_DAY,23); //设置在几点提醒  设置的为23点
        mCalender.set(Calendar.MINUTE,25); //设置在几分提醒  设置的为25分
        mCalender.set(Calendar.SECOND,0); //设置在几秒提醒
        mCalender.set(Calendar.MILLISECOND,0); //设置在几毫秒提醒

        //上面设置的是23点25分的时间点

        long systemTime = System.currentTimeMillis(); //获取当前系统毫秒数
        long selectTime = mCalender.getTimeInMillis(); //获取设置的时间点毫秒值
        long IntervalTime = 24 * 60 * 60 * 1000;
        if (systemTime > selectTime){
            //如果当前大于设置的时间，则从第二天开始
            mCalender.add(Calendar.DAY_OF_MONTH,1);
        }

        //发送广播
        Intent i = new Intent(this,AlarmReceiver.class);
        i.setAction("com.lztsg.alarmreceiver_start");

        //参数：上下文环境，请求码，Intent，标记
        pendingIntent = PendingIntent.getBroadcast(this,0,i,0);

        //设置闹钟，参数：  int type: 闹钟的类型，long triggerAtMillis: 开始时间，long intervalMillis: 时间间隔，PendingIntent pendingIntent: 动作
//        manager.set(AlarmManager.RTC_WAKEUP,mCalender.getTimeInMillis(),pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),1000,pendingIntent);
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),IntervalTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在Service结束后关闭AlarmManager
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean key = sp.getBoolean("daka_switch",false);
        if (key == true){
            Intent i = new Intent(this,AlarmReceiver.class);
            i.setAction("com.lztsg.alarmreceiver_stop");
            //参数：上下文环境，请求码，Intent，标记
            pendingIntent = PendingIntent.getBroadcast(this,0,i,0);
            sendBroadcast(i);

        } else if (key == false){
            Log.e("测试通知onDestroy","测试通知onDestroy");
            manager.cancel(pendingIntent);
        }
    }
}
