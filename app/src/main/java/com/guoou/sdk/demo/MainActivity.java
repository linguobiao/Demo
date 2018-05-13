package com.guoou.sdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.guoou.sdk.api.BleListener;
import com.guoou.sdk.bean.CarLiveDataBean;
import com.guoou.sdk.bean.CarMeasureOrderBean;
import com.guoou.sdk.bean.CarSyncDataBean;
import com.guoou.sdk.bean.Event;
import com.guoou.sdk.bean.InfoDataBean;
import com.guoou.sdk.bean.LiveDataBean;
import com.guoou.sdk.bean.SdkPart;
import com.guoou.sdk.bean.SyncDataBean;
import com.guoou.sdk.bean.SyncSetBean;
import com.guoou.sdk.global.SdkManager;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_log;
    private ScrollView scrollView;
    private String mac;//要连接的设备的MAC地址

    private LiveDataBean liveDataBean;  //普通模式-实时数据
    private SyncDataBean syncDataBean;  //普通模式-同步数据
    private SyncSetBean syncSetBean;    //普通模式-设置数据
    private InfoDataBean infoDataBean;  //普通模式-设备信息
    private CarLiveDataBean carLiveDataBean;        //车辆模式-实时数据
    private CarMeasureOrderBean carMeasureOrderBean;//车辆模式-测量顺序
    private CarSyncDataBean carSyncDataBean;        //车辆模式-整车数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);//要在onCreate执行这句代码，不然会收不到设备返回的数据。还要在onDestroy()里面执行注销：EventBus.getDefault().unregister(this);
        tv_log = (TextView) findViewById(R.id.tv_log);
        findViewById(R.id.bt_connect).setOnClickListener(this);
        findViewById(R.id.bt_sync).setOnClickListener(this);
        findViewById(R.id.bt_sync_info).setOnClickListener(this);
        findViewById(R.id.bt_car_clean).setOnClickListener(this);
        findViewById(R.id.bt_car_sync_data).setOnClickListener(this);
        findViewById(R.id.bt_car_get_measure_order).setOnClickListener(this);
        findViewById(R.id.bt_car_set_measure_order).setOnClickListener(this);
        findViewById(R.id.bt_car_change_part).setOnClickListener(this);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
    }

    @Override
    public void onClick(View v) {
        if (R.id.bt_connect == v.getId()) {//点击 连接设备
            if (!TextUtils.isEmpty(mac)) {
                //断开设备连接
                SdkManager.getInstance().getClient().disconnect(mac);
            }
            //跳转到搜索设备界面
            startActivityForResult(new Intent(MainActivity.this, DeviceActivity.class), 10001);
            return;
        }
        //判断是否连接设备
        if (!SdkManager.getInstance().isConnect(mac)) {
            Toast.makeText(MainActivity.this, "还未连接设备", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.bt_sync:                  //点击 普通模式-同步数据
                showLog("-->>正在同步数据...");
                SdkManager.getInstance().sync(mac, null);
                break;
            case R.id.bt_sync_info:             //点击 普通模式-获取设备信息
                showLog("-->>正在同步设备信息...");
                SdkManager.getInstance().syncInfo(mac);
                break;
            case R.id.bt_car_sync_data:         //点击 车辆模式-同步整车数据
                showLog("-->>车辆模式，正在同步整车数据...");
                SdkManager.getInstance().syncCarData(mac);
                break;
            case R.id.bt_car_change_part:       //点击 车辆模式-切换测量位置
                showLog("-->>车辆模式，切换测量位置...");
                SdkManager.getInstance().syncCarChangePart(mac, SdkPart.PART_DLF);
                break;
            case R.id.bt_car_get_measure_order: //点击 车辆模式-获取测量顺序
                showLog("-->>车辆模式，获取测量顺序...");
                SdkManager.getInstance().syncCarGetMeasureOrder(mac);
                break;
            case R.id.bt_car_set_measure_order: //点击 车辆模式-设置测量顺序
                showLog("-->>车辆模式，设置测量顺序...");
                ArrayList<SdkPart> sdkParts = new ArrayList<>();

                sdkParts.add(SdkPart.PART_CEF);
                sdkParts.add(SdkPart.PART_PRF);
                sdkParts.add(SdkPart.PART_SRA);
                sdkParts.add(SdkPart.PART_DRF);
                sdkParts.add(SdkPart.PART_SRB);
                sdkParts.add(SdkPart.PART_DRB);
                sdkParts.add(SdkPart.PART_SRC);
                sdkParts.add(SdkPart.PART_PRB);
                sdkParts.add(SdkPart.PART_TKB);
                sdkParts.add(SdkPart.PART_PLB);
                sdkParts.add(SdkPart.PART_SLC);
                sdkParts.add(SdkPart.PART_DLB);
                sdkParts.add(SdkPart.PART_SLB);
                sdkParts.add(SdkPart.PART_DLF);
                sdkParts.add(SdkPart.PART_SLA);
                sdkParts.add(SdkPart.PART_PLF);
                sdkParts.add(SdkPart.PART_TOP);
                sdkParts.add(SdkPart.PART_SLD);
                sdkParts.add(SdkPart.PART_SRD);

                SdkManager.getInstance().syncCarSetMeasureOrder(mac, sdkParts);
                break;
            case R.id.bt_car_clean:                 //点击 车辆模式-清空整车数据
                showLog("-->>车辆模式，清空整车数据...");
                SdkManager.getInstance().syncCarClean(mac);
                break;
            default:break;
        }
    }

    /**
     * 连接设备
     */
    private void connect() {
        logBuilder.setLength(0);//清空log
        tv_log.setText(null);//清空log
        showLog("-->>正在连接[" + mac + "]...");
        SdkManager.getInstance().connectDevice(mac, new BleListener() {
            @Override
            public void onResult(boolean isSuccess, boolean isShowFail) {
                if (isSuccess) showLog("-->>连接成功[" + mac + "]");
                else showLog("-->>连接失败[" + mac + "]");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001 && resultCode == RESULT_OK) {
            mac = data.getStringExtra("mac");
            //设置设备连接状态监听器
            SdkManager.getInstance().getClient().registerConnectStatusListener(mac, bleConnectStatusListener);
            //连接设备
            connect();
        }
    }

    /**
     * 设备连接状态监听器
     */
    private BleConnectStatusListener bleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == Constants.STATUS_CONNECTED) {}
            else if (status == Constants.STATUS_DISCONNECTED) {
                showLog("-->>设备连接断开...");
            }
        }
    };

    @Subscribe
    public void onEventMainThread(Event event) {
        if (event.getLiveDataBean() != null) {          //收到实时数据
            liveDataBean = event.getLiveDataBean();
            ShowLogUtil.buildLiveLog(liveDataBean, syncSetBean, logBuilder);
            tv_log.setText(logBuilder.toString());
        } else if (event.getSyncDataBean() != null) {   //收到同步数据
            syncDataBean = event.getSyncDataBean();
            ShowLogUtil.buildSyncDataLog(syncDataBean, syncSetBean, logBuilder);
            tv_log.setText(logBuilder.toString());
        } else if (event.getSyncSetBean() != null) {    //收到设置数据
            syncSetBean = event.getSyncSetBean();
            ShowLogUtil.buildSetLog(syncSetBean, logBuilder);
            tv_log.setText(logBuilder.toString());
        } else if (event.getInfoDataBean() != null) {   //收到设备信息
            infoDataBean = event.getInfoDataBean();
            ShowLogUtil.buildInfoLog(infoDataBean, logBuilder);
            tv_log.setText(logBuilder.toString());
        } else if (event.getCarLiveDataBean() != null) {//收到车辆模式实时数据
            carLiveDataBean = event.getCarLiveDataBean();
            ShowLogUtil.buildCarLiveLog(carLiveDataBean, syncSetBean, logBuilder);
            tv_log.setText(logBuilder.toString());
        } else if (event.getCarMeasureOrderBean() != null) {//收到车辆模式测量顺序
            carMeasureOrderBean = event.getCarMeasureOrderBean();
            ShowLogUtil.buildCarMeasureOrderLog(carMeasureOrderBean, logBuilder);
            tv_log.setText(logBuilder.toString());
        } else if (event.getCarSyncDataBean() != null) {//收到车辆模式整车数据
            carSyncDataBean = event.getCarSyncDataBean();
            ShowLogUtil.buildCarSyncDataLog(carSyncDataBean, syncSetBean, logBuilder);
            tv_log.setText(logBuilder.toString());
        }
        scrollDown();
    }

    private StringBuilder logBuilder = new StringBuilder();

    private void showLog(String log) {
        logBuilder.append("\n" + log);
        tv_log.setText(logBuilder.toString());
        scrollDown();
    }

    private void scrollDown() {
        scrollView.post(new Runnable() {
            @Override public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);//注销EventBus
        SdkManager.getInstance().getClient().unregisterConnectStatusListener(mac, bleConnectStatusListener);//注销设备连接状态监听器
        SdkManager.getInstance().getClient().disconnect(mac);//断开设备连接
        SdkManager.getInstance().finish();//注销SDK
        super.onDestroy();
    }
}
