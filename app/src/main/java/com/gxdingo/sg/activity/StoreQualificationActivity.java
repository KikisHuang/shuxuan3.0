package com.gxdingo.sg.activity;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.QualificationAdapter;
import com.gxdingo.sg.bean.StoreAuthInfoBean;
import com.gxdingo.sg.biz.ClientStoreContract;
import com.gxdingo.sg.presenter.ClientStorePresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.gxdingo.sg.utils.LocalConstant.ADD;
import static com.gxdingo.sg.utils.PhotoUtils.getPhotoUrl;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2022/3/11
 * @page:店铺资质页面
 */
public class StoreQualificationActivity extends BaseMvpActivity<ClientStoreContract.ClientStorePresenter> {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    private QualificationAdapter mAdapter;

    private String imageUrl;

    private List<String> data;

    private String id;

    @Override
    protected ClientStoreContract.ClientStorePresenter createPresenter() {
        return new ClientStorePresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_custom_title;
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
        return R.layout.module_activity_store_qualifications;
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
        data = new ArrayList<>();
        imageUrl = getIntent().getStringExtra(Constant.PARAMAS + 0);
        id = getIntent().getStringExtra(Constant.PARAMAS + 1);

        title_layout.setTitleText(getString(R.string.shop_qualification));
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        mAdapter = new QualificationAdapter((int) (ScreenUtils.getScreenWidth() * 1.15 / 2));
        recyclerView.setAdapter(mAdapter);
        data.add(imageUrl);
        mAdapter.setList(data);
        mAdapter.setOnItemClickListener((a,v,pos) -> {
            getP().viewHdImage(mAdapter.getData().get(pos));
        });
    }

    @Override
    protected void initData() {
        getP().getStoreQualifications(id, false);

    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof StoreAuthInfoBean) {
            StoreAuthInfoBean authInfoBean = (StoreAuthInfoBean) object;
            if (authInfoBean.getCategoryList() != null && authInfoBean.getCategoryList().size() > 0) {
                data.clear();
                data.add(imageUrl);
                RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
                    for (StoreAuthInfoBean.CategoryListBean categoryListBean : authInfoBean.getCategoryList()) {
                        if (!isEmpty(categoryListBean.getProve()))
                            data.add(categoryListBean.getProve());
                    }
                    e.onNext(data);
                    e.onComplete();
                }), reference.get()).subscribe(o -> {
                    mAdapter.setList(data);
                });
            }
        }

    }
}
