package com.gxdingo.sg.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.RegexUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.ArticleListAdapter;
import com.gxdingo.sg.bean.WebBean;
import com.gxdingo.sg.biz.OnContentListener;
import com.gxdingo.sg.biz.WebContract;
import com.gxdingo.sg.dialog.ClientCallPhoneDialog;
import com.gxdingo.sg.presenter.WebPresenter;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.List;

import butterknife.BindView;

import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/22
 * @page:
 */
public class ArticleListActivity extends BaseMvpActivity<WebContract.WebPresenter> implements WebContract.WebListener, OnItemClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    private ArticleListAdapter mAdapter;

    private int articleId;

    private String identifier;

    @Override
    protected WebContract.WebPresenter createPresenter() {
        return new WebPresenter();
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
        return R.layout.module_include_refresh;
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
        mAdapter = new ArticleListAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        mAdapter.setOnItemClickListener(this);

        articleId = getIntent().getIntExtra(Constant.SERIALIZABLE + 0, 0);

        identifier = getIntent().getStringExtra(Constant.SERIALIZABLE + 1);
    }

    @Override
    protected void initData() {
        getP().loadData(articleId, identifier);
    }

    @Override
    public void loadWebUrl(WebBean webBean) {

    }

    @Override
    public void onArticleListResult(List<WebBean> webBeans) {
        mAdapter.setList(webBeans);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        WebBean item = (WebBean) adapter.getItem(position);
        if (RegexUtils.isMobileExact(item.getIdentifier())){
            new XPopup.Builder(reference.get())
                    .isDarkTheme(false)
                    .asCustom(new ClientCallPhoneDialog(reference.get(), item.getIdentifier(), new OnContentListener() {
                        @Override
                        public void onConfirm(BasePopupView popupView, String content) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + content));
                            startActivity(intent);
                        }
                    }))
                    .show();
            return;
        }
        goToPagePutSerializable(reference.get(), WebActivity.class, getIntentEntityMap(new Object[]{true, 0, item.getIdentifier()}));
    }
}
