package com.guoou.sdk.demo;

import com.guoou.sdk.bean.CarLiveDataBean;
import com.guoou.sdk.bean.CarMeasureOrderBean;
import com.guoou.sdk.bean.CarSyncDataBean;
import com.guoou.sdk.bean.InfoDataBean;
import com.guoou.sdk.bean.LiveDataBean;
import com.guoou.sdk.bean.SdkPart;
import com.guoou.sdk.bean.SyncDataBean;
import com.guoou.sdk.bean.SyncSetBean;
import com.guoou.sdk.util.DecimalUtil;
import com.guoou.sdk.util.ParserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LGB on 2018/5/11.
 */

public class ShowLogUtil {

    public static void buildLiveLog(LiveDataBean bean, SyncSetBean setBean, StringBuilder stringBuilder){
        buildLine(stringBuilder);
        build(stringBuilder, "-->>收到实时数据...");
        if (bean != null) {
            int unit = 0;
            if (setBean != null) {
                unit = setBean.getUnit();
            }
            build(stringBuilder, "dataCnt:" + bean.getDataCnt());
            build(stringBuilder, "value:" + ParserUtils.buildValue(bean.getValue(), unit));
            build(stringBuilder, "type:" + bean.getType());
        }
        buildLine(stringBuilder);
    }

    public static void buildSyncDataLog(SyncDataBean bean, SyncSetBean setBean, StringBuilder stringBuilder){
        buildLine(stringBuilder);
        build(stringBuilder, "-->>收到同步数据...");
        if (bean != null) {
            int unit = 0;
            if (setBean != null) {
                unit = setBean.getUnit();
            }

            build(stringBuilder, "loLimit:" + ParserUtils.buildValue(bean.getLoLimit(), unit));
            build(stringBuilder, "hiLimit:" + ParserUtils.buildValue(bean.getHiLimit(), unit));
            build(stringBuilder, "cnt:" + bean.getDataCnt());
            build(stringBuilder, "average:" + ParserUtils.buildValue(bean.getAllAverage(), unit));
            build(stringBuilder, "min:" + ParserUtils.buildValue(bean.getAllMin(), unit));
            build(stringBuilder, "max:" + ParserUtils.buildValue(bean.getAllMax(), unit));
            build(stringBuilder, "stDev:" + ParserUtils.buildValue(bean.getAllStDev(), unit));
            build(stringBuilder, "cv:" + DecimalUtil.df_0_0().format(bean.getAllCV()) + "%");
            List<Float> dataList = bean.getDataList();
            if (dataList != null && dataList.size() > 0) {
                String dataListStr = "数据组：";
                for (float data : dataList) {
                    dataListStr = dataListStr + ParserUtils.buildValue(data, unit) + "   ";
                }
                build(stringBuilder, dataListStr);
            }
        }
        buildLine(stringBuilder);
    }

    public static void buildSetLog(SyncSetBean bean, StringBuilder stringBuilder){
        buildLine(stringBuilder);
        build(stringBuilder, "-->>收到设置数据...");
        if (bean != null) {
            build(stringBuilder, "group name:" + bean.getName());
            build(stringBuilder, "unit:" + bean.getUnit());
            build(stringBuilder, "alarm:" + bean.isAlarm());
            build(stringBuilder, "type:" + (bean.getType() == 1 ? "车辆模式打开" : "车辆模式关闭"));
        }
        buildLine(stringBuilder);
    }

    public static void buildInfoLog(InfoDataBean bean, StringBuilder stringBuilder){
        buildLine(stringBuilder);
        build(stringBuilder, "-->>收到设备信息...");
        if (bean != null) {
            build(stringBuilder, "MD:" + bean.getMD());
            build(stringBuilder, "SN:" + bean.getSN());
            build(stringBuilder, "HV:" + bean.getHV());
            build(stringBuilder, "SV:" + bean.getSV());
        }
        buildLine(stringBuilder);
    }

    public static void buildCarLiveLog(CarLiveDataBean bean, SyncSetBean setBean, StringBuilder stringBuilder){
        buildLine(stringBuilder);
        build(stringBuilder, "-->>收到车辆模式实时数据...");
        if (bean != null) {
            int unit = 0;
            if (setBean != null) {
                unit = setBean.getUnit();
            }
            build(stringBuilder, "车辆编号:" + bean.getCarId());
            build(stringBuilder, "检测部位:" + ParserUtils.getParkByCode(bean.getPartCode()).getName());
            build(stringBuilder, "部位总次数:" + bean.getPartCount());
            build(stringBuilder, "部位当前序号:" + bean.getPartCurrent());
            build(stringBuilder, "测量值:" + ParserUtils.buildValue(bean.getValue(), unit));
        }
        buildLine(stringBuilder);
    }

    public static void buildCarMeasureOrderLog(CarMeasureOrderBean bean, StringBuilder stringBuilder){
        buildLine(stringBuilder);
        build(stringBuilder, "-->>收到车辆模式测量顺序...");
        if (bean != null) {
            ArrayList<SdkPart> sdkParts = bean.getSdkParts();
            if (sdkParts != null && sdkParts.size() > 0) {
                String orderStr = "测量顺序：";
                for (SdkPart part : sdkParts) {
                    orderStr = orderStr + part.getName() + "   ";
                }
                build(stringBuilder, orderStr);
            }
        }
        buildLine(stringBuilder);
    }

    public static void buildCarSyncDataLog(CarSyncDataBean bean, SyncSetBean setBean, StringBuilder stringBuilder){
        buildLine(stringBuilder);
        build(stringBuilder, "-->>收到车辆模式整车数据...");
        if (bean != null) {
            int unit = 0;
            if (setBean != null) {
                unit = setBean.getUnit();
            }
            ArrayList<CarSyncDataBean.DataBean> dataBeanList = bean.getDataBeanList();
            if (dataBeanList != null && dataBeanList.size() > 0) {
                for (CarSyncDataBean.DataBean dataBean : dataBeanList) {

                    if (dataBean.getSdkPart() != null) {
                        String str = dataBean.getSdkPart().getName() + ":";
                        ArrayList<Float> valueList = dataBean.getValueList();
                        if (valueList != null && valueList.size() > 0) {
                            for (float value : valueList) {
                                if (value == -1) {
                                    str = str + "--" + "   ";
                                } else {
                                    str = str + ParserUtils.buildValue(value, unit) + "   ";
                                }
                            }
                        }
                        build(stringBuilder, str);
                    }
                }
            }
        }
        buildLine(stringBuilder);
    }
    
    private static void buildLine(StringBuilder stringBuilder) {
        build(stringBuilder, "------------------------------------------------------------------");
    }

    private static void build(StringBuilder stringBuilder, String str) {
        stringBuilder.append("\n" + str);
    }
}
