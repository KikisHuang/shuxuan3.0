package com.gxdingo.sg.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alibaba.idst.nui.Constants;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.AliASRBean;
import com.gxdingo.sg.bean.gen.DraftBeanDao;
import com.gxdingo.sg.db.CommonDaoUtils;
import com.gxdingo.sg.db.DaoManager;
import com.gxdingo.sg.db.DaoUtilsStore;
import com.gxdingo.sg.db.bean.DraftBean;
import com.gxdingo.sg.utils.LocalConstant;
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
import com.kikis.commnlibrary.utils.GsonUtil;
import com.kikis.commnlibrary.utils.RxUtil;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.BaseSubscriber;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.RECORD_AUDIO;
import static com.blankj.utilcode.util.ArrayUtils.copy;
import static com.blankj.utilcode.util.StringUtils.getString;
import static com.gxdingo.sg.utils.ClientLocalConstant.RECORD_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.AAC;
import static com.gxdingo.sg.utils.LocalConstant.TAG;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_BAIDU_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_GAODE_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.PN_TENCENT_MAP;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToBaiduActivity;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToGaoDeMap;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.goToTencentMap;
import static com.gxdingo.sg.utils.ThirdPartyMapsGuide.isAvilible;
import static com.kikis.commnlibrary.utils.CommonUtils.getPath;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.isDebug;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;
import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class IMChatPresenter extends BaseMvpPresenter<BasicsListener, IMChatContract.IMChatListener> implements IMChatContract.IMChatPresenter, NetWorkListener {
    private NetworkModel networkModel;
    private WebSocketModel mWebSocketModel;

    private ClientNetworkModel clientNetworkModel;

    private AudioModel mAudioModel;

    private CommonModel commonModel;

    private CommonDaoUtils<DraftBean> mDraftUtils;

    private Disposable mDownloadDisp;

    private boolean isIdentify = false;

    public IMChatPresenter() {
        clientNetworkModel = new ClientNetworkModel(this);
        networkModel = new NetworkModel(this);
        mWebSocketModel = new WebSocketModel(this);

        mAudioModel = new AudioModel();

        commonModel = new CommonModel();

        DaoUtilsStore mStore = DaoUtilsStore.getInstance();

        mDraftUtils = mStore.getDratfUtils();
        //???????????????
        DaoManager.getInstance().closeConnection();

    }

    @Override
    public void onSucceed(int type) {
        if (isBViewAttached()) {

            //??????????????????
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
     * ????????????????????????
     */
    @Override
    public void getChatHistoryList(String shareUuid, String otherId, int otherRole) {

        mWebSocketModel.getChatHistoryList(getContext(), shareUuid, otherId, otherRole, new CustomResultListener<IMChatHistoryListBean>() {
            @Override
            public void onResult(IMChatHistoryListBean imChatHistoryListBean) {
                getV().onChatHistoryList(imChatHistoryListBean);
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param shareUuid
     * @param otherId
     * @param otherRole
     */
    @Override
    public void refreshHistoryList(String shareUuid, String otherId, int otherRole) {

        mWebSocketModel.refreshChatHistoryList(getContext(), shareUuid, otherId, otherRole, (CustomResultListener<IMChatHistoryListBean>) imChatHistoryListBean -> {
            try {
                if (isViewAttached()) {
                    if (isViewAttached() && imChatHistoryListBean != null && imChatHistoryListBean.getList() != null) {
                        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                            //????????????????????????,?????????????????????????????????????????????
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
                                BaseLogUtils.i("????????????????????????????????????");
                                getV().onAddNewChatHistoryList(newData);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                LogUtils.e("onAddNewChatHistoryList === " + e);
                //???????????????????????????bugly
                CrashReport.postCatchedException(e);
            }
        });
    }


    /**
     * ??????????????????
     *
     * @param text ????????????
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
     * ??????????????????
     *
     * @param url ??????URL
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
     * ??????????????????
     *
     * @param url ??????URL
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
     * ??????????????????
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
     * ????????????
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
            SendIMMessageBean sendIMMessageBean = new SendIMMessageBean(shareUuid, type, content, voiceDuration, params);
            mWebSocketModel.sendMessage(getContext(), sendIMMessageBean, receiveIMMessageBean -> getV().onSendMessageSuccess(receiveIMMessageBean));
        }
    }

    /**
     * ??????????????????(???)
     *
     * @param sendIMMessageBean ??????????????????
     */
    public void sendMessage(SendIMMessageBean sendIMMessageBean) {
        mWebSocketModel.sendMessage(getContext(), sendIMMessageBean, new CustomResultListener<ReceiveIMMessageBean>() {
            @Override
            public void onResult(ReceiveIMMessageBean receiveIMMessageBean) {
                getV().onSendMessageSuccess(receiveIMMessageBean);
            }
        });
    }

    /**
     * ??????????????????(???)
     *
     * @param sendIMMessageBean ??????????????????
     */
    public void sendMessageResultPos(SendIMMessageBean sendIMMessageBean, int pos) {
        mWebSocketModel.sendMessage(getContext(), sendIMMessageBean, new CustomResultListener<ReceiveIMMessageBean>() {
            @Override
            public void onResult(ReceiveIMMessageBean receiveIMMessageBean) {
                getV().onSendMessageSuccessResultPos(receiveIMMessageBean, pos);
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param pos 0 ?????????1 ??????
     */
    @Override
    public void photoSourceClick(int pos) {
        boolean gallery = pos == 0;
        PictureSelector selector = PictureSelector.create((Activity) getContext());
        PictureSelectionModel model = gallery ? selector.openGallery(ofImage()) : selector.openCamera(ofImage());

        if (pos == 1)
            model.selectionMode(PictureConfig.SINGLE);
        else
            model.selectionMode(PictureConfig.MULTIPLE);
        model.
                loadImageEngine(GlideEngine.createGlideEngine())// ?????????????????? ?????? implements ImageEngine??????
                .compress(true)//????????????
                .showCropFrame(false)// ?????????????????????????????? ???????????????????????????false
                .showCropGrid(false)//?????????????????????????????? ???????????????????????????false
                .rotateEnabled(false)//???????????????????????????
                .scaleEnabled(true)//?????????????????????????????????
                .minimumCompressSize(500)//??????1024kb?????????
                .synOrAsy(true)//????????????or????????????
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
//                        String url = getPhotoUrl(result.get(0));

                        for (LocalMedia localMedia : result) {
                            String url = getPhotoUrl(localMedia);
                            if (isViewAttached())
                                getV().onUploadImageUrl(url);
                        }
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
     * ????????????
     */
    @Override
    public void startRecorder() {
        if (mAudioModel != null)
            mAudioModel.startRecorder(this, (LifecycleProvider) getContext());
    }

    /**
     * ??????????????????
     */
    @Override
    public void stopRecorder() {
        if (mAudioModel != null && mAudioModel.isRecording()) {
            mAudioModel.stopRecorder();

            if (!isBViewAttached() || !isViewAttached())
                return;

            if (mAudioModel.getDuration() > 1) {
                onStarts();
                //??????????????????????????????
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

                }, 0);

            } else {
                getBV().onMessage("??????????????????");
                if (mAudioModel != null)
                    mAudioModel.delRecord();
            }
        }
    }

    /**
     * ??????????????????
     */
    @Override
    public void cancelRecorder() {
        if (mAudioModel != null && mAudioModel.isRecording()) {
            mAudioModel.stopRecorder();
            mAudioModel.delRecord();
        }
    }

    /**
     * ??????????????????
     *
     * @param rxPermissions
     */
    @Override
    public void checkRecordPermissions(RxPermissions rxPermissions) {
        if (commonModel != null)
            commonModel.checkPermission(rxPermissions, new String[]{RECORD_AUDIO}, new PermissionsListener() {
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
     * ????????????
     *
     * @param content
     */
    @Override
    public void playVoice(String content) {
        if (mAudioModel != null) {
            mAudioModel.audioPlayer(getContext(), content, new AudioModelListener() {
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
     * ??????????????????
     */
    @Override
    public void stopVoice() {
        if (mAudioModel != null && mAudioModel.isPlaying())
            mAudioModel.audioPause();
    }

    /**
     * ??????????????????
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
     * ????????????
     *
     * @param position
     * @param id
     */
    @Override
    public void getTransfer(int position, long id) {

        if (mWebSocketModel != null)
            mWebSocketModel.getTransfer(getContext(), id, data -> {

                //????????????????????????ID?????????????????????socket
                Map<String, Object> map = new HashMap<>();
                map.put("id", data);

                sendMessage(getV().getShareUUID(), 21, "", 0, map);
                if (isViewAttached())
                    getV().getTransFerSucceed(position);

            });

    }

    /**
     * ??????????????????
     */
    @Override
    public void getAddressList() {

        if (clientNetworkModel != null)
            clientNetworkModel.getAddressList(getContext(), true, this);

    }


    /**
     * ????????????
     *
     * @param id
     * @param position
     */
    @Override
    public void revocationMessage(long id, int position) {

        if (mWebSocketModel != null)
            mWebSocketModel.revocationMessage(getContext(), id, t -> {
                getV().onMessageRevocation(position);

            });

    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        if (mDownloadDisp != null) {
            mDownloadDisp.dispose();
            mDownloadDisp = null;
        }
        if (mAudioModel != null)
            mAudioModel.delAudioFile();

        super.onMvpDetachView(retainInstance);
    }


    /**
     * ????????????
     */
    @Override
    public void saveDraft() {
        if (isViewAttached() && !isEmpty(getV().getShareUUID()) && getV().getMessageEdttext() != null && !isEmpty(getV().getSendIdentifier())) {

            DraftBean draftBean = new DraftBean();

            draftBean.uuid = getV().getShareUUID();

            draftBean.draft = getV().getMessageEdttext().getText().toString();

            draftBean.sendIdentifier = getV().getSendIdentifier();


            DraftBean db = mDraftUtils.queryByQueryBuilderUnique(DraftBeanDao.Properties.Uuid.eq(getV().getShareUUID()));

            if (db != null) {

                //?????????????????????
                draftBean.id = db.id;
                new Thread(() -> DaoUtilsStore.getInstance().getDratfUtils().update(draftBean)).start();

            } else {
                //???????????????????????????????????????????????????
                if (!isEmpty(getV().getMessageEdttext().getText().toString()))
                    new Thread(() -> DaoUtilsStore.getInstance().getDratfUtils().insert(draftBean)).start();

            }
            EventBus.getDefault().post(LocalConstant.NOTIFY_MSG_LIST_ADAPTER);

            //???????????????
            DaoManager.getInstance().closeConnection();
        }
    }


    /**
     * ????????????
     */
    @Override
    public void checkDraft() {
        if (isViewAttached() && !isEmpty(getV().getShareUUID()) && getV().getMessageEdttext() != null) {

            DraftBean db = mDraftUtils.queryByQueryBuilderUnique(DraftBeanDao.Properties.Uuid.eq(getV().getShareUUID()));

            if (db != null && !isEmpty(db.draft)) {

                if (isDebug)
                    LogUtils.w(" draft id === " + db.id);

                getV().getMessageEdttext().setText(db.draft);
                //???????????????
                DaoManager.getInstance().closeConnection();
            }
        }
    }

    /**
     * ???????????????
     *
     * @param content
     * @param position
     */
    @Override
    public void voiceToText(String content, int position) {
        if (isIdentify) {
            getBV().onMessage("???????????????");
            return;
        }

        if (networkModel != null) {

            String filePath = getPath() + File.separator + "audio" + File.separator;

            String fileName = UUID.randomUUID().toString() + AAC;

            isIdentify = true;
            networkModel.downloadFile(content, new DownloadProgressCallBack() {
                @Override
                public void onStart() {
                    onStarts();
                }

                @Override
                public void onError(ApiException e) {
                    if (isBViewAttached())
                        onMessage(e.getMessage());
                    isIdentify = false;
                    onAfters();
                }

                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    int progress = (int) (bytesRead * 100 / contentLength);

                    BaseLogUtils.w("downLoad progress === " + progress);
                }

                @Override
                public void onComplete(String path) {

                    BaseLogUtils.w("downLoad path === " + path);

                    if (clientNetworkModel != null)
                        clientNetworkModel.getVoiceToken(getContext(), token -> {
//                            token = "5f8b1fcf1d5744abbb72ea1b813f46bb";
                            if (token == null) {
                                isIdentify = false;
                                onAfters();
                                return;
                            }

                            if (mAudioModel != null) {
                                mAudioModel.initAliyunNui(getContext(), (String) token, (event, resultCode, finish, asrResult, taskId) -> {

                                    if (event == Constants.NuiEvent.EVENT_FILE_TRANS_UPLOADED) {
                                        //???????????????????????????...
                                    } else if (event == Constants.NuiEvent.EVENT_FILE_TRANS_RESULT) {
                                        //???????????? asrResult.asrResult
                                        LogUtils.i("onFileTransEventCallback === " + asrResult.asrResult);

                                        AliASRBean aliASRBean = GsonUtil.GsonToBean(asrResult.asrResult, AliASRBean.class);
                                        Message msg = new Message();

                                        if (aliASRBean.getFlash_result() != null && aliASRBean.getFlash_result().getSentences() != null && aliASRBean.getFlash_result().getSentences().size() > 0) {

                                            if (mAudioModel != null) {
                                                msg.what = 100;
                                                mAudioModel.getASRText(getContext(), aliASRBean.getFlash_result().getSentences(), text -> {
                                                    msg.obj = text;
                                                    msg.arg1 = position;
                                                });
                                            }

                                        } else {
                                            LogUtils.i(TAG, "???????????????????????????");
                                            msg.obj = gets(R.string.speech_recognition_failure);
                                        }
                                        mHandler.sendMessage(msg);
                                    } else if (event == Constants.NuiEvent.EVENT_ASR_ERROR) {
                                        //240075 ???????????????????????????????????????
                                        //????????????asrResult.asrResult
                                        LogUtils.i(TAG, "error happened: " + resultCode);

                                        Message msg = new Message();
                                        msg.obj = gets(R.string.speech_recognition_failure);
                                        mHandler.sendMessage(msg);
                                    }

                                });

                                if (mAudioModel != null)
                                    mAudioModel.aliYunStartFileTranscriber(filePath + fileName);
                            }
                        });

                }
            }, mDownloadDisp, filePath, fileName);
        }
    }

    @Override
    public void delMessage(long id, int position) {

        if (mWebSocketModel != null)
            mWebSocketModel.messageDel(getContext(), String.valueOf(id), t -> {
                getV().onMessageDelete(position);
            });
    }

    @Override
    public void copyText(String content, String toast) {
        //??????
        ClipboardUtils.copyText(content);
        if (isBViewAttached())
            getBV().onMessage(toast);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                if (isBViewAttached())
                    getV().onIdentifiedContentResult(msg.obj, msg.arg1);
            } else {
                onMessage((String) msg.obj);
            }
            isIdentify = false;
            onAfters();
        }
    };

    @Override
    public void onMvpDestroy() {
        super.onMvpDestroy();

        if (mAudioModel != null)
            mAudioModel.destroy();

        if (mHandler != null)
            mHandler = null;
    }
}
