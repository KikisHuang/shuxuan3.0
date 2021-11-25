package com.gxdingo.sg.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.biz.StoreBusinessDistrictReleaseContract;
import com.gxdingo.sg.dialog.SgConfirm2ButtonPopupView;
import com.gxdingo.sg.presenter.StoreBusinessDistrictReleasePresenter;
import com.gxdingo.sg.utils.StoreLocalConstant;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.kikis.commnlibrary.view.GridPictureEditing;
import com.kikis.commnlibrary.view.RoundAngleImageView;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * 商家端商圈发布
 *
 * @author JM
 */
public class StoreBusinessDistrictReleaseActivity extends BaseMvpActivity<StoreBusinessDistrictReleaseContract.StoreBusinessDistrictReleasePresenter>
        implements StoreBusinessDistrictReleaseContract.StoreBusinessDistrictReleaseListener {
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.tv_right_button)
    TextView tvRightButton;
    @BindView(R.id.tv_right_image_button)
    ImageView tvRightImageButton;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.gpe_picture)
    GridPictureEditing gpePicture;

    int mMaxCount = 9;//最大图片数
    int mSpanCount = 4;//每行多少列

    @Override
    protected StoreBusinessDistrictReleaseContract.StoreBusinessDistrictReleasePresenter createPresenter() {
        return new StoreBusinessDistrictReleasePresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_new_custom_title;
    }

    @Override
    protected boolean ImmersionBar() {
        return true;
    }

    @Override
    protected int StatusBarColors() {
        return R.color.white;
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
        return R.layout.module_activity_store_business_district_release;
    }

    @Override
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    protected void init() {
        titleLayout.setTitleTextSize(16);
        titleLayout.setBackImg(R.drawable.module_svg_back_delete_icon_8900);
        titleLayout.setTitleText("发布");
        tvRightButton.setText("发布");
        tvRightButton.setTextColor(Color.parseColor("#ffffff"));
        tvRightButton.setVisibility(View.VISIBLE);
        tvRightButton.setBackgroundResource(R.drawable.module_bg_business_district_release_button);
        ScreenUtils.init(this);
        int lr = ScreenUtils.dip2px(getResources().getDimension(R.dimen.dp6));
        int tb = ScreenUtils.dip2px(getResources().getDimension(R.dimen.dp2));
        tvRightButton.setPadding(lr, tb, lr, tb);

        initGpeImages();
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.tv_right_button)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            onMessage("请输入内容！");
            etContent.requestFocus();
            return;
        }/* else if (gpePicture.getValueCount() == 0) {
            onMessage("请上传图片！");
            return;
        }*/
        ArrayList<GridPictureEditing.PictureValue> tempPictureValues = gpePicture.getValues();
        if (tempPictureValues != null) {
            ArrayList<String> images = new ArrayList<>();
            for (int i = 0; i < tempPictureValues.size(); i++) {
                images.add(tempPictureValues.get(i).url);
            }
            getP().releaseBusinessDistrict(etContent.getText().toString(), images);
        }
    }

    /**
     * 初始化GridPictureEditing
     */
    private void initGpeImages() {
        int dp6 = (int) getResources().getDimension(R.dimen.dp6);
        View addButtonView = LayoutInflater.from(this).inflate(R.layout.module_view_add_business_district_picture_button, null);//添加图片
        gpePicture.init(this, mSpanCount, mMaxCount, addButtonView
                , dp6
                , dp6, new GridPictureEditing.OnGridPictureEditingClickListener() {
                    @Override
                    public void onItem(View view, int position, GridPictureEditing.PictureValue pictureValue) {
                        deletePictureDialog(position);
                    }

                    @Override
                    public void onAddButton() {
                        uploadImage();
                    }
                });
        //设置自定义的ViewHolder（圆角），不用默认的
        gpePicture.setCustomGridPictureEditViewHolder(new GridPictureEditing.CustomGridPictureEditViewHolder() {

            @Override
            public int getViewId() {
                return R.layout.module_item_business_district_release_picture;
            }

            @Override
            public void bindView(RecyclerView.ViewHolder viewHolder, int position, GridPictureEditing.PictureValue pictureValue) {
                RoundAngleImageView ivPicture = viewHolder.itemView.findViewById(R.id.iv_picture);

                RequestOptions options = new RequestOptions();
                options.placeholder(com.kikis.commnlibrary.R.mipmap.news_default_icon);    //加载成功之前占位图
                options.error(com.kikis.commnlibrary.R.mipmap.news_default_icon);    //加载错误之后的错误图

                //判断是本地图片/视频还是网络图片/视频
                if (pictureValue.type == 0) {
                    Glide.with(StoreBusinessDistrictReleaseActivity.this).load(pictureValue.thumbnailUrl).apply(options).into(ivPicture);
                } else {
                    //本地文件
                    File file = new File(pictureValue.thumbnailUrl);
                    Glide.with(StoreBusinessDistrictReleaseActivity.this).load(file).apply(options).into(ivPicture);
                }
            }
        });
    }

    /**
     * 删除图片
     */
    private void deletePictureDialog(int position) {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .dismissOnTouchOutside(false)
                .asCustom(new SgConfirm2ButtonPopupView(reference.get(), "确定删除图片？", "", () -> {
                    gpePicture.removeValue(position);
                }))
                .show();
    }

    /**
     * 上传图片
     */
    private void uploadImage() {
        new XPopup.Builder(reference.get())
                .isDarkTheme(false)
                .isDestroyOnDismiss(true)
                .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.photo_album), gets(R.string.photo_graph)).setItemClickListener((itemv, pos) -> {
                    int num = mMaxCount - gpePicture.getValueCount();
                    getP().addPhoto(pos, num);
                })).show();
    }

    /**
     * 获取图片数据
     *
     * @param upLoadBean 服务器返回上传图片的ULR
     */
    @Override
    public void getPhotoDataList(UpLoadBean upLoadBean) {
        if (upLoadBean != null) {
            List<String> listUrls = upLoadBean.urls;
            if (listUrls != null) {
                for (String url : listUrls) {
                    //给控件添加图片即可显示
                    gpePicture.addValue(GridPictureEditing.TYPE_NETWORK_FILE, GridPictureEditing.FILE_TYPE_PICTURE, url, url);
                }
            }
        }
    }

    /**
     * 发布商圈信息成功
     */
    @Override
    public void releaseBusinessDistrictSuccess(String msg) {
        //发送事件告知刷新商圈列表
        sendEvent(StoreLocalConstant.SOTRE_REFRESH_BUSINESS_DISTRICT_LIST);
        onMessage(msg);
        finish();
    }
}
