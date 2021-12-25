package com.gxdingo.sg.db;


import com.gxdingo.sg.bean.gen.DraftBeanDao;
import com.gxdingo.sg.db.bean.DraftBean;

/**
 * 存放DaoUtils
 */
public class DaoUtilsStore {

    private volatile static DaoUtilsStore instance = new DaoUtilsStore();

    //消息列表
    private CommonDaoUtils<DraftBean> mDratfUtils;

    public static DaoUtilsStore getInstance() {
        return instance;
    }

    private DaoUtilsStore() {
        DaoManager mManager = DaoManager.getInstance();

        DraftBeanDao chatDao = mManager.getDaoSession().getDraftBeanDao();
        mDratfUtils = new CommonDaoUtils(DraftBean.class, chatDao);

    }

    public CommonDaoUtils<DraftBean> getDratfUtils() {
        return mDratfUtils;
    }

}