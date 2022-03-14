package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.StoreBusinessScopeAdapter;
import com.gxdingo.sg.bean.BusinessScopeEvent;
import com.gxdingo.sg.bean.StoreBusinessScopeBean;
import com.gxdingo.sg.bean.StoreCategoryBean;
import com.gxdingo.sg.biz.StoreCertificationContract;
import com.gxdingo.sg.dialog.UpLoadLicencePopupView;
import com.gxdingo.sg.presenter.StoreCertificationPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.biz.CustomResultListener;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;

/**
 * 商家经营范围
 *
 * @author: Weaving
 * @date: 2021/6/2
 * @page:
 */
public class StoreBusinessScopeActivity extends BaseMvpActivity<StoreCertificationContract.StoreCertificationPresenter>
        implements StoreCertificationContract.StoreCertificationListener, OnItemClickListener {


    @BindView(R.id.rv_business_scope)
    public RecyclerView rv_business_scope;
    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.tv_right_button)
    TextView tvRightButton;
    @BindView(R.id.tv_right_image_button)
    ImageView tvRightImageButton;

    private StoreBusinessScopeAdapter mAdapter;

    private BasePopupView popupView;

    private List<StoreCategoryBean> tempLicenceMap;
    private LocalMedia tempLicenceUrl;


    @Override
    protected StoreCertificationContract.StoreCertificationPresenter createPresenter() {
        return new StoreCertificationPresenter();
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
        return R.layout.module_activity_store_business_scope;
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
        titleLayout.setTitleText("经营范围");
        tempLicenceMap = new ArrayList<>();
        tvRightButton.setVisibility(View.VISIBLE);
        tvRightButton.setOnClickListener(v -> {
            getP().confirmBusinessScope(mAdapter.getData(), tempLicenceMap);
        });

        mAdapter = new StoreBusinessScopeAdapter();
        rv_business_scope.setLayoutManager(new LinearLayoutManager(reference.get()));
        rv_business_scope.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        getP().getCategory();
    }

    @Override
    public void uploadImage(String url) {
        if (popupView != null && popupView.isShow())
            ((UpLoadLicencePopupView) popupView).setLicenceImg(url);
    }

    @Override
    public void onBusinessScopeResult(List<StoreBusinessScopeBean.ListBean> businessScopes) {
        mAdapter.setList(businessScopes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeBusinessScope(BusinessScopeEvent businessScopeEvent) {
        sendEvent(businessScopeEvent);
        finish();
    }

    @Override
    public void certificationPassed() {

    }

    @Override
    public void onReview() {

    }

    @Override
    public void rejected(String rejectReason) {

    }

    @Override
    public void showActivityTypeLayout(int type) {

    }

    @Override
    public void setOssSpecialQualificationsImg(int position, String path) {
        //将网络oss图片赋值
        if (position <= tempLicenceMap.size() - 1)
            tempLicenceMap.get(position).setProve(path);

        tempLicenceMap.add(new StoreCategoryBean(mAdapter.getData().get(position).getId(), path));
        mAdapter.getData().get(position).setSelect(true);
        mAdapter.notifyDataSetChanged();

        tempLicenceUrl = null;
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        List<StoreBusinessScopeBean.ListBean> data = mAdapter.getData();

        //反选还是选中
        boolean isSelect = !data.get(position).isSelect();


        if (isSelect && tempLicenceMap.size() >= 2) {
            onMessage("最多选2个品类");
            return;
        }

        if (!isSelect) {
            //反选删除
            for (int j = 0; j < tempLicenceMap.size(); j++) {
                if (data.get(position).getId() == tempLicenceMap.get(j).getCategoryId())
                    tempLicenceMap.remove(j);
            }

            data.get(position).setSelect(false);
            mAdapter.notifyDataSetChanged();
            return;
        }

        if (data.get(position).getType() == 1) {
            //特殊品类需要添加经营许可证
            if (popupView != null) {
                popupView.onDestroy();
                popupView = null;
            }

            popupView = new XPopup.Builder(reference.get())
                    .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
                    .isDarkTheme(false)
                    .asCustom(new UpLoadLicencePopupView(reference.get(), "您的选择经营范围需要提交\n" + data.get(position).licenceName, (CustomResultListener<Integer>) integer -> {
                        if (integer != null && integer == 1) {
                            //上传食品经营许可证
                            getP().selectedLicence(o -> {
                                tempLicenceUrl = (LocalMedia) o;
                                //先用本地图片显示给用户看
                                uploadImage(getPhotoUrl(tempLicenceUrl));
                            });
                        } else if (integer == 0) {
                            if (tempLicenceUrl != null)
                                //点击确定后上传资质图片到oss
                                getP().uploadOss(position, getPhotoUrl(tempLicenceUrl));
                            else
                                onMessage("请上传特殊资质图片");
                        }

                    })).show();


        } else {
            tempLicenceMap.add(new StoreCategoryBean(data.get(position).getId(), ""));
            data.get(position).setSelect(true);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupView != null) {
            popupView.onDestroy();
            popupView = null;
        }

    }
}
