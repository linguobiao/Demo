package com.guoou.sdk.demo;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guoou.sdk.bean.DeviceBean;
import com.guoou.sdk.demo.R;
import com.guoou.sdk.util.ByteUtils;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;



public class PullToRefreshAdapter extends BaseQuickAdapter<SearchResult, BaseViewHolder> {
    public PullToRefreshAdapter(List<SearchResult> list) {
        super( R.layout.item_device, list);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, SearchResult item) {
        DeviceBean deviceBean = ByteUtils.getDevice(item);
        viewHolder.setText(R.id.tv_device_name, deviceBean.getName())
                .setText(R.id.tv_device_mac, deviceBean.getMac())
                .setText(R.id.tv_device_rssi, item.rssi + "dbm");
    }
}
