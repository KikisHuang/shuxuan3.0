package com.gxdingo.sg.bean.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.gxdingo.sg.db.bean.DraftBean;

import com.gxdingo.sg.bean.gen.DraftBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig draftBeanDaoConfig;

    private final DraftBeanDao draftBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        draftBeanDaoConfig = daoConfigMap.get(DraftBeanDao.class).clone();
        draftBeanDaoConfig.initIdentityScope(type);

        draftBeanDao = new DraftBeanDao(draftBeanDaoConfig, this);

        registerDao(DraftBean.class, draftBeanDao);
    }
    
    public void clear() {
        draftBeanDaoConfig.clearIdentityScope();
    }

    public DraftBeanDao getDraftBeanDao() {
        return draftBeanDao;
    }

}
