package com.gxdingo.sg.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.IMComplaintContentItemAdapter;
import com.gxdingo.sg.bean.UpLoadBean;
import com.gxdingo.sg.bean.WebBean;
import com.gxdingo.sg.biz.IMComplaintContract;
import com.gxdingo.sg.dialog.EnterPaymentPasswordPopupView;
import com.gxdingo.sg.dialog.OneSentenceHintPopupView;
import com.gxdingo.sg.presenter.IMComplaintPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.KeyboardHeightObserver;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.GridPictureEditing;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gxdingo.sg.utils.LocalConstant.COMPLAINT_SUCCEED;

/**
 * IM-投诉举报内容
 *
 * @author JM
 */
public class IMComplaintContentActivity extends BaseMvpActivity<IMComplaintContract.IMComplaintPresenter> implements IMComplaintContract.IMComplaintListener, KeyboardHeightObserver {
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;


    IMComplaintContentItemAdapter mAdapter;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.gpe_picture)
    GridPictureEditing gpePicture;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    int mMaxCount = 4;//最大投诉图片数
    int mSpanCount = 4;//每行多少列
    private String reason;


    @Override
    protected IMComplaintContract.IMComplaintPresenter createPresenter() {
        return new IMComplaintPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
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
        return R.layout.module_activity_im_complaint_content;
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

        reason = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);

        String sendIdentifier = getIntent().getStringExtra(Constant.SERIALIZABLE + 1);

        titleLayout.setTitleText("");
        initGpeImages();
        btnSubmit.setOnClickListener(v -> {
            getP().complaint(reason, etContent.getText().toString(), gpePicture.getValues(),sendIdentifier);
            /*    new XPopup.Builder(reference.get())
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .isDarkTheme(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(new OneSentenceHintPopupView(reference.get(), "已收到您的反馈，我们会严肃处理")).show();*/
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

    }

    /**
     * 初始化GridPictureEditing
     */
    private void initGpeImages() {
        View addButtonView = LayoutInflater.from(this).inflate(R.layout.module_view_add_complaint_picture_button, null);//添加图片
        gpePicture.init(this, mSpanCount, mMaxCount, addButtonView
                , 0
                , 0, new GridPictureEditing.OnGridPictureEditingClickListener() {
                    @Override
                    public void onItem(View view, int position, GridPictureEditing.PictureValue pictureValue) {
                        ToastUtils.showLong("onItem=" + position);
                    }

                    @Override
                    public void onAddButton() {
                        int num = mMaxCount - gpePicture.getValueCount();
                        getP().addPhoto(num);
                    }
                });
        //设置自定义的ViewHolder，不用默认的
        gpePicture.setCustomGridPictureEditViewHolder(new GridPictureEditing.CustomGridPictureEditViewHolder() {

            @Override
            public int getViewId() {
                return R.layout.module_item_complaint_picture;
            }

            @Override
            public void bindView(RecyclerView.ViewHolder viewHolder, int position, GridPictureEditing.PictureValue pictureValue) {
                ImageView image = viewHolder.itemView.findViewById(R.id.image);
                ImageView delete = viewHolder.itemView.findViewById(R.id.delete);

                RequestOptions options = new RequestOptions();
                options.placeholder(com.kikis.commnlibrary.R.mipmap.news_default_icon);    //加载成功之前占位图
                options.error(com.kikis.commnlibrary.R.mipmap.news_default_icon);    //加载错误之后的错误图

                //判断是本地图片/视频还是网络图片/视频
                if (pictureValue.type == 0) {
                    Glide.with(IMComplaintContentActivity.this).load(pictureValue.thumbnailUrl).apply(options).into(image);
                } else {
                    //本地文件
                    File file = new File(pictureValue.thumbnailUrl);
                    Glide.with(IMComplaintContentActivity.this).load(file).apply(options).into(image);
                }

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gpePicture.removeValue(position);
                    }
                });
            }
        });
    }

    /**
     * 获取投诉照片数据
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

    @Override
    public void onArticleListResult(List<WebBean> list) {

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type==COMPLAINT_SUCCEED)
            finish();
    }
}
