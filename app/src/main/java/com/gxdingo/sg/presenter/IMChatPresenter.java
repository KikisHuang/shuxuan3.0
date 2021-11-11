package com.gxdingo.sg.presenter;

import android.app.Activity;

import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.gxdingo.sg.bean.SendIMMessageBean;
import com.gxdingo.sg.biz.IMChatContract;
import com.gxdingo.sg.biz.NetWorkListener;
import com.gxdingo.sg.model.NetworkModel;
import com.gxdingo.sg.model.WebSocketModel;
import com.gxdingo.sg.utils.GlideEngine;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.presenter.BaseMvpPresenter;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_BAIDU_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_GAODE_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_TENCENT_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToBaiduActivity;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToGaoDeMap;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToTencentMap;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.isAvilible;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class IMChatPresenter extends BaseMvpPresenter<BasicsListener, IMChatContract.IMChatListener> implements IMChatContract.IMChatPresenter, NetWorkListener {
    private NetworkModel networkModel;
    private WebSocketModel mWebSocketModel;
    private long mStartSendTime;//发送消息间隔
    private long mEndSendTime;

    public IMChatPresenter() {
        networkModel = new NetworkModel(this);
        mWebSocketModel = new WebSocketModel(this);
    }

    @Override
    public void onSucceed(int type) {
        if (isBViewAttached()) {

        }
    }

    @Override
    public void onMessage(String msg) {
        if (isBViewAttached())
            getBV().onMessage(msg);
    }

    @Override
    public void noData() {
        if (isBViewAttached())
            getBV().noData();
    }

    @Override
    public void onData(boolean refresh, Object o) {
        if (isBViewAttached()) {

        }
    }

    @Override
    public void haveData() {
        if (isBViewAttached())
            getBV().haveData();
    }

    @Override
    public void finishLoadmoreWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishLoadmoreWithNoMoreData();
    }

    @Override
    public void finishRefreshWithNoMoreData() {
        if (isBViewAttached())
            getBV().finishRefreshWithNoMoreData();
    }

    @Override
    public void onRequestComplete() {
        if (isBViewAttached())
            getBV().onRequestComplete();
    }

    @Override
    public void resetNoMoreData() {
        if (isBViewAttached())
            getBV().resetNoMoreData();
    }

    @Override
    public void finishRefresh(boolean success) {
        if (isBViewAttached())
            getBV().finishRefresh(success);
    }

    @Override
    public void finishLoadmore(boolean success) {
        if (isBViewAttached())
            getBV().finishLoadmore(success);
    }

    @Override
    public void onAfters() {
        if (isBViewAttached())
            getBV().onAfters();
    }

    @Override
    public void onStarts() {
        if (isBViewAttached())
            getBV().onStarts();
    }

    @Override
    public void onDisposable(BaseSubscriber subscriber) {

    }

    /**
     * 获取聊天记录列表
     */
    @Override
    public void getChatHistoryList(String shareUuid, int otherId, int otherRole) {

        mWebSocketModel.getChatHistoryList(getContext(), shareUuid, otherId, otherRole, new CustomResultListener<IMChatHistoryListBean>() {
            @Override
            public void onResult(IMChatHistoryListBean imChatHistoryListBean) {
                getV().onChatHistoryList(imChatHistoryListBean);
            }
        });
    }


    /**
     * 发送文本消息
     *
     * @param text 文本消息
     */
    @Override
    public void sendTextMessage(String shareUuid, String text) {
        SendIMMessageBean sendIMMessageBean = new SendIMMessageBean();
        sendIMMessageBean.setShareUuid(shareUuid);
        sendIMMessageBean.setContent(text);
        sendIMMessageBean.setType(0);

        sendMessage(sendIMMessageBean);
    }

    /**
     * 发送图片消息
     *
     * @param url 图片URL
     * @param pos
     */
    @Override
    public void sendPictureMessage(String shareUuid, String url, int pos) {
        SendIMMessageBean sendIMMessageBean = new SendIMMessageBean();
        sendIMMessageBean.setShareUuid(shareUuid);
        sendIMMessageBean.setContent(url);
        sendIMMessageBean.setType(10);

        sendMessageResultPos(sendIMMessageBean, pos);
    }

    /**
     * 发送语音消息
     *
     * @param url 语音URL
     */
    @Override
    public void sendVoiceMessage(String shareUuid, String url, int voiceDuration) {
        SendIMMessageBean sendIMMessageBean = new SendIMMessageBean();
        sendIMMessageBean.setShareUuid(shareUuid);
        sendIMMessageBean.setContent(url);
        sendIMMessageBean.setVoiceDuration(voiceDuration);
        sendIMMessageBean.setType(11);

        sendMessage(sendIMMessageBean);
    }

    /**
     * 发送转账消息
     *
     * @param id
     */
    @Override
    public void sendTransferAccountsMessage(String shareUuid, String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        SendIMMessageBean sendIMMessageBean = new SendIMMessageBean();
        sendIMMessageBean.setShareUuid(shareUuid);
        sendIMMessageBean.setParams(params);
        sendIMMessageBean.setType(20);

        sendMessage(sendIMMessageBean);
    }

    /**
     * 发送消息
     *
     * @param shareUuid
     * @param type
     * @param content
     * @param voiceDuration
     * @param params
     */
    @Override
    public void sendMessage(String shareUuid, int type, String content, int voiceDuration, Map<String, Object> params) {
        mEndSendTime = System.currentTimeMillis();
        if (mEndSendTime - mStartSendTime > 500) {
            SendIMMessageBean sendIMMessageBean = new SendIMMessageBean(shareUuid, type, content, voiceDuration, params);
            mWebSocketModel.sendMessage(getContext(), sendIMMessageBean, receiveIMMessageBean -> getV().onSendMessageSuccess(receiveIMMessageBean));
        } else {
            onMessage("发送过于频繁");
        }
        mStartSendTime = mEndSendTime;
    }

    /**
     * 发送一条消息(旧)
     *
     * @param sendIMMessageBean 发送消息对象
     */
    public void sendMessage(SendIMMessageBean sendIMMessageBean) {
        mEndSendTime = System.currentTimeMillis();
        if (mEndSendTime - mStartSendTime > 500) {
            mWebSocketModel.sendMessage(getContext(), sendIMMessageBean, new CustomResultListener<ReceiveIMMessageBean>() {
                @Override
                public void onResult(ReceiveIMMessageBean receiveIMMessageBean) {
                    getV().onSendMessageSuccess(receiveIMMessageBean);
                }
            });
        } else {
            onMessage("发送过于频繁");
        }
        mStartSendTime = mEndSendTime;
    }

    /**
     * 发送一条消息(旧)
     *
     * @param sendIMMessageBean 发送消息对象
     */
    public void sendMessageResultPos(SendIMMessageBean sendIMMessageBean, int pos) {
        mEndSendTime = System.currentTimeMillis();
        if (mEndSendTime - mStartSendTime > 500) {
            mWebSocketModel.sendMessage(getContext(), sendIMMessageBean, new CustomResultListener<ReceiveIMMessageBean>() {
                @Override
                public void onResult(ReceiveIMMessageBean receiveIMMessageBean) {
                    getV().onSendMessageSuccessResultPos(receiveIMMessageBean, pos);
                }
            });
        } else {
            onMessage("发送过于频繁");
        }
        mStartSendTime = mEndSendTime;
    }

    /**
     * 照片来源点击
     *
     * @param pos 0 相册，1 相机
     */
    @Override
    public void photoSourceClick(int pos) {
        boolean gallery = pos == 0;
        PictureSelector selector = PictureSelector.create((Activity) getContext());
        PictureSelectionModel model = gallery ? selector.openGallery(ofImage()) : selector.openCamera(ofImage());
        model.selectionMode(PictureConfig.SINGLE).
                loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .compress(true)//是否压缩
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .rotateEnabled(false)//裁剪是否可旋转图片
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .minimumCompressSize(500)//小于1024kb不压缩
                .synOrAsy(true)//开启同步or异步压缩
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        getBV().onStarts();
//                        String url = getPhotoUrl(result.get(0));
                        String url = !isEmpty(result.get(0).getCompressPath()) ? result.get(0).getCompressPath() : result.get(0).getPath();
                        getV().onUploadImageUrl(url);
                        /*    networkModel.upLoadImage(getContext(), url, new UpLoadImageListener() {
                            @Override
                            public void loadSucceed(String path) {
                                getV().onUploadImageUrl(path);
                                getBV().onAfters();
                            }

                            @Override
                            public void loadSucceed(UpLoadBean upLoadBean) {
                                System.out.println();
                            }
                        });*/
                    }

                    @Override
                    public void onCancel() {


                    }
                });
    }

    @Override
    public void goOutSideNavigation(int pos, ReceiveIMMessageBean.MsgAddress mAddress) {
        if (!isBViewAttached() || mAddress == null)
            return;

        switch (pos) {
            case 0:
                if (isAvilible(getContext(), PN_GAODE_MAP)) {
                    goToGaoDeMap(getContext(), mAddress.getStreet(), mAddress.getLongitude(), mAddress.getLatitude());
                } else
                    getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.gaode_map)));
                break;
            case 1:
                if (isAvilible(getContext(), PN_BAIDU_MAP))
                    goToBaiduActivity(getContext(), mAddress.getStreet(), mAddress.getLongitude(), mAddress.getLatitude());
                else
                    getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.baidu_map)));

                break;
            case 2:
                if (isAvilible(getContext(), PN_TENCENT_MAP)) {
                    goToTencentMap(getContext(), mAddress.getStreet(), mAddress.getLongitude(), mAddress.getLatitude());
                } else
                    getBV().onMessage(String.format(getString(R.string.uninstall_app), gets(R.string.tencent_map)));
                break;

        }
    }
}
