package com.gxdingo.sg.activity;

import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;

import butterknife.OnClick;

import static com.gxdingo.sg.utils.RSAEncrypt.decryptByPrivateKey;
import static com.gxdingo.sg.utils.RSAUtils.getPrivateKeyPath;


/**
 * @author: Kikis
 * @date: 2022/1/7
 * @page:
 */
public class TestActivity extends BaseActivity {


    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return 0;
    }

    @Override
    protected boolean ImmersionBar() {
        return false;
    }

    @Override
    protected int StatusBarColors() {
        return 0;
    }

    @Override
    protected int NavigationBarColor() {
        return 0;
    }

    @Override
    protected int activityBottomLayout() {
        return 0;
    }

    @Override
    protected View noDataLayout() {
        return null;
    }

    @Override
    protected View refreshLayout() {
        return null;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return 0;
    }

    @Override
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }


    /**
     * 输入开启的线程数量
     */
    private static final int MAX_THREAD = 10;
    private static final String ip = "180.101.45.141";

    @Override
    protected void init() {


    }


    @Override
    protected void initData()  {

        String data = "KGwMXhHIoAq2RoaCT8iY2jk87DEtZA/7IRC8rnk49dJXVCjMmL1D3uI97TVjwJ2CkGFrzR20fPh0\\r\\niCNoudTh5WDpfwb0360h6L+soPu8W8+H7PT97yiGAQatQbPKgdtmcco6xRD9hTbu9Kd8/kqnfBJi\\r\\nOor6SeCKyjwQzv8xtzl+qI+6hyBrecaFXn/P6eKjeUr1hD1f+GQ1+Y2gLYGeWuvM6ZI2wPBFJKII\\r\\nHE6KxcGLGoyACFJa9Y8oP26DIVGV3DcBvjqKXD2twLsS0RTWNfj4Wrvz2oMfyy6neFgjCaNr+r6X\\r\\nfJU07eAuYcjqiC8l2OaydMEQOdeCO/c5NImVyA==";

        String decodeData = null;
        try {
            decodeData = decryptByPrivateKey(data,getPrivateKeyPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.i("Test decodeData === " + decodeData);


    }


    @Override
    protected void onBaseCreate() {
        super.onBaseCreate();
    }

    @OnClick({})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {

        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
    }
}
