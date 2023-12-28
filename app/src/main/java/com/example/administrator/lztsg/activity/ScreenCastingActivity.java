package com.example.administrator.lztsg.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.lztsg.MyToast;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.CastingItem;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.avtransport.callback.GetMediaInfo;
import org.fourthline.cling.support.avtransport.callback.GetPositionInfo;
import org.fourthline.cling.support.avtransport.callback.Pause;
import org.fourthline.cling.support.avtransport.callback.Play;
import org.fourthline.cling.support.avtransport.callback.Seek;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.MediaInfo;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.ProtocolInfo;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.item.AudioItem;
import org.fourthline.cling.support.model.item.ImageItem;
import org.fourthline.cling.support.model.item.VideoItem;
import org.fourthline.cling.support.renderingcontrol.callback.GetVolume;
import org.fourthline.cling.support.renderingcontrol.callback.SetVolume;
import org.seamless.util.MimeType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScreenCastingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private SelectCastingAdapter mSelectCastingAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Button mbtn_start,mbtn_fast_rewind,mbtn_fast_forward,mbtn_volume_up,mbtn_volume_down;
    private TextView current,total;
    private SeekBar mseekbar;
    private List<CastingItem> mData;
    public static int mCurrentVolume;

    // Upnp服务
    private static AndroidUpnpService mUpnpService = null;
    // Upnp控制点
    private static ControlPoint mControlPoint;

    private static Device mCurrentDevice;

    // 协议头
    private static final String DIDL_LITE_FOOTER = "</DIDL-Lite>";
    // 协议尾
    private static final String DIDL_LITE_HEADER = "<?xml version=\"1.0\"?>" +
            "<DIDL-Lite " + "xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" " +
            "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " + "xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" " +
            "xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\">";

    // 类型定义
    public static final int VIDEO_TYPE = 1;
    public static final int AUDIO_TYPE = 2;
    public static final int IMAGE_TYPE = 3;

    // 投屏前需要确保 mUpnpService != null || mCurrentDevice != null || mControlPoint != null

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_casting);
        init();
        initData();
        LinearRecyclerView();
        // 启动 Upnp服务
        Intent intent = new Intent(this, AndroidUpnpServiceImpl.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void init() {
        mRecyclerView = findViewById(R.id.run_main);
        mbtn_start = findViewById(R.id.btn_start);
        mbtn_fast_rewind = findViewById(R.id.btn_fast_rewind);
        mbtn_fast_forward = findViewById(R.id.btn_fast_forward);
        mbtn_volume_up = findViewById(R.id.btn_volume_up);
        mbtn_volume_down = findViewById(R.id.btn_volume_down);

        current = findViewById(R.id.current);
        total = findViewById(R.id.total);
        mseekbar = findViewById(R.id.bottom_seek_progress);
    }

    private void initData() {
        this.mData = new ArrayList<>();
        mbtn_start.setOnClickListener(this);
        mbtn_volume_up.setOnClickListener(this);
        mbtn_volume_down.setOnClickListener(this);
    }

    private void LinearRecyclerView() {
        //初始化线性布局管理器
        mLinearLayoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSelectCastingAdapter = new SelectCastingAdapter(mData, new SelectCastingAdapter.OnItemClickListener() {
            @Override
            public void itemHoldersonClick(int position) {

                // 投屏视频
                //nana
                String url = "https://t22.cdn2020.com/video/m3u8/2023/08/28/efe34610/index.m3u8";
//                String url = "https://hls-uranus.sb-cd.com/hls/1/1/11321829-,720p,.mp4.urlset/master.m3u8?secure=KJQhwZcAPK0Ms1WWtJqgSw,1694024946&m=18&d=5&_tid=11321829";
//                String url = "https://vdownload-18.sb-cd.com/1/1/11321829-720p.mp4?secure=KJQhwZcAPK0Ms1WWtJqgSw,1694024946&m=18&d=5&_tid=11321829";
//                //hentai
//                String url = "https://214.freeaav.online/upload/2021012005.mp4";
                setAVTransportURI(url, VIDEO_TYPE);
//                // 投屏音频
//                String mp3 = "http://111.230.29.242:8080/cheshi.mp3";
//                setAVTransportURI(mp3, AUDIO_TYPE);
//                // 投屏图片
//                String image = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597923118159&di=5d0add2fc13243068afbf07e2ce2842a&imgtype=0&src=http%3A%2F%2Fwww.anqu.com%2Fuploads%2Fallimg%2F1208%2F39-120R3093301.jpg";
//                setAVTransportURI(image, IMAGE_TYPE);

            }
        });
        mRecyclerView.setAdapter(mSelectCastingAdapter);
    }

    // 服务连接
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mUpnpService = (AndroidUpnpService) iBinder;

            // 服务回调
            mUpnpService.getRegistry().addListener(new DefaultRegistryListener() {

                @Override
                public void deviceAdded(Registry registry, final Device device) {
                    super.deviceAdded(registry, device);

                    Log.e("wush", "找到设备：" + device.getDisplayString());
                    // 加入到发现的设备列表
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            deviceAdapter.addDevice(device);
                            mCurrentDevice = device;
                            mData.add(new CastingItem(device.getDetails().getFriendlyName()));
                            mSelectCastingAdapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void deviceRemoved(Registry registry, final Device device) {
                    super.deviceRemoved(registry, device);

                    Log.e("wush", "移除设备：" + device.getDisplayString());
                    // 从发现的设备列表中移除
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            deviceAdapter.removeDevice(device);
                            String elementToRemove = device.getDetails().getFriendlyName();
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).equals(elementToRemove)) {
                                    mData.remove(i);
                                    i--; // 由于删除了一个元素，需要将索引减1
                                }
                            }
                            mSelectCastingAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            mControlPoint = mUpnpService.getControlPoint();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };

    /**
     * 媒体投屏（视频，音频，图片）
     */
    private void setAVTransportURI(String url, int mediaType) {

        // mCurrentDevice 为在步骤1 搜索到的设备中的一个
        Service service = mCurrentDevice.findService(new UDAServiceType("AVTransport"));

        String metadata = pushMediaToRender(url, "id", "name", "0", mediaType);
        mControlPoint.execute(new SetAVTransportURI(service, url, metadata) {

            @Override
            public void success(ActionInvocation invocation) {
                super.success(invocation);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyToast.makeText(ScreenCastingActivity.this,"投屏成功",
                                1000, Gravity.FILL_HORIZONTAL | Gravity.BOTTOM).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getMediaInfo();
                                getPositionInfo();
                            }
                        },1000 * 10);
                    }
                });
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                Log.e("wush", "投屏失败");
            }
        });
    }
    private void getMediaInfo(){
        //获取媒体信息
        Service service = mCurrentDevice.findService(new UDAServiceType("AVTransport"));
        mControlPoint.execute(new GetMediaInfo(service) {

            @Override
            public void success(ActionInvocation invocation) {
                super.success(invocation);
                Log.e("wush", "获取媒体信息成功");
            }

            @Override
            public void received(ActionInvocation invocation, final MediaInfo mediaInfo) {
                // 媒体信息封装在 mediaInfo 对象当中
                // 包括总时长，播放地址等等

                Log.d("received", "received: "+mediaInfo.getMediaDuration());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        total.setText(mediaInfo.getMediaDuration());
                    }
                });
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                Log.e("wush", "获取媒体信息失败");
            }
        });
    }
    private void getPositionInfo(){
        //获取进度
        Service service = mCurrentDevice.findService(new UDAServiceType("AVTransport"));
        mControlPoint.execute(new GetPositionInfo(service) {

            @Override
            public void success(ActionInvocation invocation) {
                super.success(invocation);
                Log.e("wush", "获取进度成功");
            }

            @Override
            public void received(ActionInvocation invocation, PositionInfo positionInfo) {
                Log.e("wush", positionInfo.toString());
                // 进度 positionInfo.getTrackDuration()
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 后台任务逻辑
//                        while (invocation) {
//                            // 计算进度
//                            String position = positionInfo.getRelTime();
//                            int duration = calculateDuration();
//
//                            // 发送消息到UI线程
//                            Message message = handler.obtainMessage(UPDATE_TEXT_VIEW, positionInfo);
//                            handler.sendMessage(message);
//
//                            // 休眠一秒钟
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        total.setText(positionInfo.getTrackDuration());
                    }
                });
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                Log.e("wush", "获取进度失败");
            }
        });
    }
    private void setSeek(String progress){
        Service service = mCurrentDevice.findService(new UDAServiceType("AVTransport"));
        // progress 为 "00:00:00"，String 类型
        mControlPoint.execute(new Seek(service, progress) {

            @Override
            public void success(ActionInvocation invocation) {
                super.success(invocation);
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {

            }
        });
    }
    private void getVolume(VolumeCallback callback){
        //获取音量
        Service service1 = mCurrentDevice.findService(new UDAServiceType("RenderingControl"));
        mControlPoint.execute(new GetVolume(service1) {

            @Override
            public void success(ActionInvocation invocation) {
                super.success(invocation);
                Log.e("wush", "获取音量成功");
            }

            @Override
            public void received(ActionInvocation actionInvocation, int currentVolume) {
                Log.e("wush", "当前音量：" + currentVolume);
                callback.onVolumeReceived(currentVolume);

            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                Log.e("wush", "获取音量失败");
                callback.onVolumeFailure();
            }
        });
    }

    private void setVolume(long volume){
        Service service = mCurrentDevice.findService(new UDAServiceType("RenderingControl"));
        // volume 0 - 100 （小米盒子，其他设备没有测试具体范围）
        mControlPoint.execute(new SetVolume(service, volume) {

            @Override
            public void success(ActionInvocation invocation) {
                super.success(invocation);
                Log.e("wush", "设置音量成功");
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                Log.e("wush", "设置音量失败");
            }
        });
    }

    /**
     * 创建媒体协议信息
     */
    private String pushMediaToRender(String url, String id, String name, String duration, int mediaType) {

        long size = 0;
        long bitrate = 0;
        Res res = new Res(new MimeType(ProtocolInfo.WILDCARD, ProtocolInfo.WILDCARD), size, url);

        String creator = "unknow";
        String resolution = "unknow";

        String metadata = "";

        if (VIDEO_TYPE == mediaType) {
            VideoItem videoItem = new VideoItem(id, "0", name, creator, res);
            metadata = createItemMetadata(videoItem);
        } else if (AUDIO_TYPE == mediaType) {
            AudioItem audioItem = new AudioItem(id, "0", name, creator, res);
            metadata = createItemMetadata(audioItem);
        } else if (IMAGE_TYPE == mediaType) {
            ImageItem imageItem = new ImageItem(id, "0", name, creator, res);
            metadata = createItemMetadata(imageItem);
        }

        Log.e("wush", "metadata: " + metadata);
        return metadata;
    }

    /**
     * 协议封装
     */
    private String createItemMetadata(DIDLObject item) {

        StringBuilder metadata = new StringBuilder();
        metadata.append(DIDL_LITE_HEADER);

        metadata.append(String.format("<item id=\"%s\" parentID=\"%s\" restricted=\"%s\">", item.getId(), item.getParentID(), item.isRestricted() ? "1" : "0"));

        metadata.append(String.format("<dc:title>%s</dc:title>", item.getTitle()));
        String creator = item.getCreator();
        if (creator != null) {
            creator = creator.replaceAll("<", "_");
            creator = creator.replaceAll(">", "_");
        }
        metadata.append(String.format("<upnp:artist>%s</upnp:artist>", creator));

        metadata.append(String.format("<upnp:class>%s</upnp:class>", item.getClazz().getValue()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date now = new Date();
        String time = sdf.format(now);
        metadata.append(String.format("<dc:date>%s</dc:date>", time));

        Res res = item.getFirstResource();
        if (res != null) {
            // protocol info
            String protocolInfo = "";
            ProtocolInfo pi = res.getProtocolInfo();
            if (pi != null) {
                protocolInfo = String.format("protocolInfo=\"%s:%s:%s:%s\"", pi.getProtocol(), pi.getNetwork(), pi.getContentFormatMimeType(), pi
                        .getAdditionalInfo());
            }
            Log.e("wush", "protocolInfo: " + protocolInfo);

            // resolution, extra info, not adding yet
            String resolution = "";
            if (res.getResolution() != null && res.getResolution().length() > 0) {
                resolution = String.format("resolution=\"%s\"", res.getResolution());
            }

            // duration
            String duration = "";
            if (res.getDuration() != null && res.getDuration().length() > 0) {
                duration = String.format("duration=\"%s\"", res.getDuration());
            }

            // res begin
            // metadata.append(String.format("<res %s>", protocolInfo)); // no resolution & duration yet
            metadata.append(String.format("<res %s %s %s>", protocolInfo, resolution, duration));

            // url
            String url = res.getValue();
            metadata.append(url);

            // res end
            metadata.append("</res>");
        }
        metadata.append("</item>");

        metadata.append(DIDL_LITE_FOOTER);

        return metadata.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                Service service = mCurrentDevice.findService(new UDAServiceType("AVTransport"));
                Drawable background = mbtn_start.getBackground();
                Drawable desiredBackground = getResources().getDrawable(R.drawable.ic_pause);
                if (background.getConstantState() == desiredBackground.getConstantState()) {
                    // 背景资源相同
                    mControlPoint.execute(new Pause(service) {

                        @Override
                        public void success(ActionInvocation invocation) {
                            super.success(invocation);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mbtn_start.setBackground(getResources().getDrawable(R.drawable.ic_play_white));
                                    MyToast.makeText(ScreenCastingActivity.this,"暂停播放",
                                            1000, Gravity.FILL_HORIZONTAL | Gravity.BOTTOM).show();
                                }
                            });
                        }

                        @Override
                        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                            Log.e("wush", "暂停失败");
                        }
                    });
                }else{
                    mControlPoint.execute(new Play(service) {

                        @Override
                        public void success(ActionInvocation invocation) {
                            super.success(invocation);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mbtn_start.setBackground(getResources().getDrawable(R.drawable.ic_pause));
                                    MyToast.makeText(ScreenCastingActivity.this,"恢复播放",
                                            1000, Gravity.FILL_HORIZONTAL | Gravity.BOTTOM).show();
                                }
                            });
                        }

                        @Override
                        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                            Log.e("wush", "恢复失败");
                        }
                    });
                }
                break;
            case R.id.btn_fast_rewind:
                break;
            case R.id.btn_fast_forward:
                break;
            case R.id.btn_volume_up:
                getVolume(new VolumeCallback() {
                    @Override
                    public void onVolumeReceived(int volume) {
                        // 处理返回的音量值
                        long volumeup = volume + 1;
                        Log.d("volume_up", "onClick: "+ volumeup);
                        setVolume(volumeup);
                    }

                    @Override
                    public void onVolumeFailure() {
                        // 处理获取音量失败的情况
                    }
                });
                break;
            case R.id.btn_volume_down:
                getVolume(new VolumeCallback() {
                    @Override
                    public void onVolumeReceived(int volume) {
                        // 处理返回的音量值
                        long volumedown = volume - 1;
                        Log.d("volume_down", "onClick: "+ volumedown);
                        setVolume(volumedown);
                    }

                    @Override
                    public void onVolumeFailure() {
                        // 处理获取音量失败的情况
                    }
                });
                break;
        }
    }
    public interface VolumeCallback {
        void onVolumeReceived(int volume);
        void onVolumeFailure();
    }
}
    class SelectCastingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<CastingItem> castingItems;
    private OnItemClickListener mListener;

    SelectCastingAdapter(List<CastingItem> castingItems,OnItemClickListener listener) {
        this.castingItems = castingItems;
        this.mListener = listener;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CastingItem item = castingItems.get(position);
        //设置Item文字
        CastingHolder castingHolder = (CastingHolder) holder;
        castingHolder.textView.setText(item.getTitle());
        castingHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemHoldersonClick(position);
            }
        });

        // 设置下划线的可见性
        if (position == getItemCount() - 1) {
            castingHolder.divider.setVisibility(View.GONE); // 最后一项不显示下划线
        } else {
            castingHolder.divider.setVisibility(View.VISIBLE); // 其他项显示下划线
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_casting_item,parent,false);
        return new CastingHolder(view);
    }

    @Override
    public int getItemCount() {
        return castingItems.size();
    }

    //手办图鉴详细页item
    class CastingHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View divider;

        CastingHolder(View item) {
            super(item);
            textView = item.findViewById(R.id.title);
            divider = item.findViewById(R.id.divider);
        }
    }

    public interface OnItemClickListener {
        void itemHoldersonClick(int position);
    }
}
