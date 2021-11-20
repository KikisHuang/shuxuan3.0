package com.gxdingo.sg.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.biz.StoreWalletContract;
import com.gxdingo.sg.model.CommonModel;
import com.gxdingo.sg.presenter.StoreWalletPresenter;

import com.gxdingo.sg.utils.BitMapUtil;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.EnumMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.STORAGE;
import static com.gxdingo.sg.utils.StoreLocalConstant.DEVICE_PHOTO_REQUEST;
import static com.gxdingo.sg.utils.StoreLocalConstant.REQUEST_CODE_SCAN;

/**
 * @author: Weaving
 * @date: 2021/11/13
 * @page:
 */
public class CustomCaptureActivity extends BaseMvpActivity<StoreWalletContract.StoreWalletPresenter> {

//    private CaptureManager capture;
//
    @BindView(R.id.container_fl)
    public FrameLayout container_fl;

    @Override
    protected StoreWalletContract.StoreWalletPresenter createPresenter() {
        return new StoreWalletPresenter();
    }

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
        return R.layout.module_activity_custom_capture;
    }

    @Override
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @OnClick({R.id.close_capture_iv,R.id.select_picture_iv})
    public void OnClickViews(View v){
        switch (v.getId()){
            case R.id.close_capture_iv:
                finish();
                break;
            case R.id.select_picture_iv:
                new CommonModel().checkPermission(getRxPermissions(), new String[]{READ_EXTERNAL_STORAGE}, new PermissionsListener() {
                    @Override
                    public void onNext(boolean value) {
                        if (value){
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, DEVICE_PHOTO_REQUEST);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                break;
        }

    }

    @Override
    protected void init() {
//        capture = new CaptureManager(this,barcodeScannerView);
//        capture.initializeFromIntent(getIntent(),savedInstanceState);
//        capture.decode();
        initScan();
    }

    private void initScan() {
        try {
            /**
             * 二维码解析回调函数
             */
            CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
                @Override
                public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                    if (!TextUtils.isEmpty(result)) {
                        setResult(RESULT_OK,getIntent().putExtra("success_result",result));
                        finish();
                    }
                }

                @Override
                public void onAnalyzeFailed() {
//                    setResult(SCAN_FAIL,getIntent().putExtra("fail_result","扫描失败"));
                    finish();
                }
            };

            /**
             * 执行扫面Fragment的初始化操作
             */
            CaptureFragment captureFragment = new CaptureFragment();
            // 为二维码扫描界面设置定制化界面
            CodeUtils.setFragmentArgs(captureFragment, R.layout.module_fragment_my_camera);
            captureFragment.setAnalyzeCallback(analyzeCallback);
            /**
             * 替换我们的扫描控件
             */
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fl, captureFragment).commit();
        } catch (Exception e) {

        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DEVICE_PHOTO_REQUEST) {

            if (data != null) {
                Uri uri = data.getData();
                String imagePath = BitMapUtil.getPicturePathFromUri(CustomCaptureActivity.this, uri);
                //对获取到的二维码照片进行压缩
                Bitmap bitmap = BitMapUtil.compressPicture(imagePath);
                Result result = setZxingResult(bitmap);
                if (result == null) {
                    ToastUtils.showLong("识别失败，请试试其它二维码");
                } else {
//                    LogUtils.d(result.getText());
                    getP().scanCode(result.getText());
                }
            }
        }
    }

    private static Result setZxingResult(Bitmap bitmap) {
        if (bitmap == null) return null;
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        int[] pix = new int[picWidth * picHeight];
        //Log.e(TAG, "decodeFromPicture:图片大小： " + bitmap.getByteCount() / 1024 / 1024 + "M");
        bitmap.getPixels(pix, 0, picWidth, 0, 0, picWidth, picHeight);
        //构造LuminanceSource对象
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(picWidth
                , picHeight, pix);
        BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //因为解析的条码类型是二维码，所以这边用QRCodeReader最合适。
        QRCodeReader qrCodeReader = new QRCodeReader();
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, true);
        Result result;
        try {
            result = qrCodeReader.decode(bb, hints);
            return result;
        } catch (NotFoundException | ChecksumException | FormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
