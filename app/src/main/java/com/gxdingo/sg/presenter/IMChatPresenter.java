package com.gxdingo.sg.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.R;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.AddressListBean;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.AudioModelListener;
import com.gxdingo.sg.biz.PermissionsListener;
import com.gxdingo.sg.biz.UpLoadImageListener;
import com.gxdingo.sg.model.AudioModel;
import com.gxdingo.sg.model.ClientNetworkModel;
import com.gxdingo.sg.model.CommonModel;
import com.kikis.commnlibrary.activitiy.BaseActivity;
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
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.kikis.commnlibrary.utils.RxUtil;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.zhouyou.http.subsciber.BaseSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.gxdingo.sg.utils.ClientLocalConstant.RECORD_SUCCEED;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_BAIDU_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_GAODE_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_TENCENT_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToBaiduActivity;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToGaoDeMap;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToTencentMap;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.isAvilible;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class IMChatPresenter extends BaseMvpPresenter<BasicsListener, IMChatContract.IMChatListener> implements IMChatContract.IMChatPresenter, NetWorkListener {
    private NetworkModel networkModel;
    private WebSocketModel mWebSocketModel;
    private long mStartSendTime;//发送消息间隔
    private long mEndSendTime;

    private ClientNetworkModel clientNetworkModel;

    private AudioModel mAudioModel;
    private CommonModel commonModel;

    public IMChatPresenter() {
        clientNetworkModel = new ClientNetworkModel(this);
        networkModel = new NetworkModel(this);
        mWebSocketModel = new WebSocketModel(this);
        mAudioModel = AudioModel.getInstance();

        commonModel = new CommonModel();
    }

    @Override
    public void onSucceed(int type) {
        if (isBViewAttached()) {

            //语音录制结束
            if (type == RECORD_SUCCEED) {
                stopRecorder();
            }

        }
    }

    @Override
    public void onMvpPause() {
        super.onMvpPause();

        if (mAudioModel != null && mAudioModel.isPlaying())
            mAudioModel.audioPause();
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

        if (o instanceof AddressListBean) {
            AddressListBean addressListBean = (AddressListBean) o;
            if (addressListBean.getList() != null && addressListBean.getList().size() > 2) {

                RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                    List<AddressBean> addresses = new ArrayList<>(2);

                    for (int i = 0; i < 2; i++) {
                        addresses.add(addressListBean.getList().get(i));
                    }

                    e.onNext(addresses);
                    e.onComplete();
                }), ((BaseActivity) getContext())).subscribe(data -> {

                    if (isViewAttached())
                        getV().showSelectAddressDialog((List<AddressBean>) data);

                });
            } else {
                if (isViewAttached())
                    getV().showSelectAddressDialog(addressListBean.getList());
            }

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
        addDisposable(subscriber);
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
     * 刷新聊天记录
     *
     * @param shareUuid
     * @param otherId
     * @param otherRole
     */
    @Override
    public void refreshHistoryList(String shareUuid, int otherId, int otherRole) {

        mWebSocketModel.refreshChatHistoryList(getContext(), shareUuid, otherId, otherRole, (CustomResultListener<IMChatHistoryListBean>) imChatHistoryListBean -> {

            if (isViewAttached()) {

                    RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                        //判断是否有新消息,有的话重新计数翻页，设置新数据
                        for (ReceiveIMMessageBean ndata : imChatHistoryListBean.getList()) {
                            boolean newMesssage = true;
                            LinkedList<ReceiveIMMessageBean> oldData = getV().getNowChatHistoryList();
                            for (int i = 0; i < getV().getNowChatHistoryList().size(); i++) {
                                if (ndata.getId() == oldData.get(i).getId()) {
                                    newMesssage = false;
                                    continue;
                                }
                            }
                            if (newMesssage) {
                                break;
                            }
                        }
                        e.onNext(imChatHistoryListBean.getList());
                        e.onComplete();
                    }), (BaseActivity) getContext()).subscribe(o -> {

                        ArrayList<ReceiveIMMessageBean> newData = (ArrayList<ReceiveIMMessageBean>) o;

                        if (isViewAttached() && newData.size() > 0) {
                            BaseLogUtils.i("有新消息，添加到消息列表");

                            getV().onAddNewChatHistoryList(newData);
                        }
                    });
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
        if (mWebSocketModel != null) {
            mEndSendTime = System.currentTimeMillis();
            if (mEndSendTime - mStartSendTime > 500) {
                SendIMMessageBean sendIMMessageBean = new SendIMMessageBean(shareUuid, type, content, voiceDuration, params);
                mWebSocketModel.sendMessage(getContext(), sendIMMessageBean, receiveIMMessageBean -> getV().onSendMessageSuccess(receiveIMMessageBean));
            } else {
                onMessage("发送过于频繁");
            }
            mStartSendTime = mEndSendTime;
        }
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
    public void goOutSideNavigation(int pos, AddressBean mAddress) {
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

    /**
     * 开始录制
     */
    @Override
    public void startRecorder() {
        if (mAudioModel != null)
            mAudioModel.startRecorder(this, (LifecycleProvider) getContext());
    }

    /**
     * 停止录制语音
     */
    @Override
    public void stopRecorder() {
        if (mAudioModel != null && mAudioModel.isRecording()) {
            mAudioModel.stopRecorder();

            if (!isBViewAttached() || !isViewAttached())
                return;

            if (mAudioModel.getDuration() > 1) {
                onStarts();
                //停止录制后，上传语音
                networkModel.upLoadImage(getContext(), mAudioModel.getRecordPath(), new UpLoadImageListener() {
                    @Override
                    public void loadSucceed(String path) {
                        sendMessage(getV().getShareUUID(), 11, path, mAudioModel.getDuration(), null);
                        onAfters();
                    }

                    @Override
                    public void loadSucceed(UpLoadBean upLoadBean) {
                        onAfters();
                    }

                });

            } else {
                getBV().onMessage("录制时间太短");
                mAudioModel.removeRecord();
            }
        }
    }

    /**
     * 取消语音录制
     */
    @Override
    public void cancelRecorder() {
        if (mAudioModel != null && mAudioModel.isRecording()) {
            mAudioModel.stopRecorder();
            mAudioModel.removeRecord();
        }
    }

    /**
     * 获取录音权限
     *
     * @param rxPermissions
     */
    @Override
    public void checkRecordPermissions(RxPermissions rxPermissions) {
        if (commonModel != null)
            commonModel.checkPermission(rxPermissions, new String[]{RECORD_AUDIO, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, new PermissionsListener() {
                @Override
                public void onNext(boolean value) {

                    if (isBViewAttached()) {
                        if (!value)
                            getBV().onMessage(gets(R.string.please_get_recording_permissions));

                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

    /**
     * 语音播放
     *
     * @param content
     */
    @Override
    public void playVoice(String content) {
        if (mAudioModel != null) {
            mAudioModel.audioPlayer(content, new AudioModelListener() {
                @Override
                public void onAudioMessage(String msg) {
                    if (isBViewAttached())
                        getBV().onMessage(msg);
                }

                @Override
                public void onAudioError(String ermsg) {
                    if (mAudioModel != null && mAudioModel.isPlaying())
                        mAudioModel.audioPause();
                }

                @Override
                public void onRecorderComplete() {

                }
            });

        }
    }

    /**
     * 停止语音播放
     */
    @Override
    public void stopVoice() {
        if (mAudioModel != null && mAudioModel.isPlaying())
            mAudioModel.audioPause();
    }

    /**
     * 清除语音未读
     *
     * @param position
     * @param id
     */
    @Override
    public void clearMessageUnread(int position, long id) {
        if (mWebSocketModel != null) {
            mWebSocketModel.messageRead(getContext(), id, data -> {
                if (isViewAttached())
                    getV().readAudioMsg(position, id);

            });
        }

    }

    /**
     * 领取转账
     *
     * @param position
     * @param id
     */
    @Override
    public void getTransfer(int position, long id) {

        if (mWebSocketModel != null)
            mWebSocketModel.getTransfer(getContext(), id, data -> {

                //领取成功后使用该ID发送一条消息到socket
                Map<String, Object> map = new HashMap<>();
                map.put("id", data);

                sendMessage(getV().getShareUUID(), 21, "", 0, map);
                if (isViewAttached())
                    getV().getTransFerSucceed(position);

            });

    }

    /**
     * 获取地址列表
     */
    @Override
    public void getAddressList() {

        if (clientNetworkModel != null)
            clientNetworkModel.getAddressList(getContext(), true, this);

    }

    /**
     * 获取默认地址
     */
    @Override
    public void getCacheAddress() {
        if (commonModel != null)
            getV().onAddressResult(commonModel.getCacheDefaultAddress());

    }

}
