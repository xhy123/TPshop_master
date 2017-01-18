package com.soubao.tpshop.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.dao.SPPersonDao;
import com.soubao.tpshop.model.person.SPRegionModel;

import java.util.List;

/**
 * Created by admin on 2016/7/9.
 */
public class SPCityUtil {

    private Context context;
    private Handler handler;
    public SPCityUtil(final Context context,final Handler handler) {
        this.context = context ;
        this.handler = handler;
    }

    /***
     * 初始化省份
     */
    public  void initProvince() {
        new Thread() {
            @Override
            public void run() {
                List<SPRegionModel> regions = SPPersonDao.getInstance(context).queryRegionByLevel(SPPersonDao.LEVEL_PROVINCE);//省份
                Message message = handler.obtainMessage(SPPersonDao.LEVEL_PROVINCE);
                message.obj = regions;
                handler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 初始化城市
     */
    public void initChildrenRegion(final String parentID ,final int level) {
        new Thread() {
            @Override
            public void run() {
                try {
                    List<SPRegionModel> regions = SPPersonDao.getInstance(context).queryRegionByParentID(parentID);//省份
                    Message message = handler.obtainMessage(level);
                    message.obj = regions;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
