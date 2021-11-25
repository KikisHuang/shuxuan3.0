package com.gxdingo.sg.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.ClientPickerDateListener;
import com.gxdingo.sg.view.PickerLayoutManager;
import com.lxj.xpopup.core.BottomPopupView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/10/21
 * @page:
 */
public class SelectDateDialog extends BottomPopupView implements PickerLayoutManager.OnPickerListener {

    @BindView(R.id.rv_picker_year)
    public RecyclerView rv_picker_year;

    @BindView(R.id.rv_picker_month)
    public RecyclerView rv_picker_month;

    private ClientPickerDateListener onListener;

    private final int mStartYear = Calendar.getInstance().get(Calendar.YEAR)-10 ;
    private final int mEndYear = Calendar.getInstance().get(Calendar.YEAR);

    private  PickerLayoutManager mYearManager;
    private  PickerLayoutManager mMonthManager;

    private PickerAdapter mYearAdapter;
    private PickerAdapter mMonthAdapter;

    public SelectDateDialog(@NonNull Context context,ClientPickerDateListener listener) {
        super(context);
        this.onListener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_date_select;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

        ButterKnife.bind(this, bottomPopupContainer);

        mYearAdapter = new PickerAdapter();
        mMonthAdapter = new PickerAdapter();

        // 生产年份
        ArrayList<String> yearData = new ArrayList<>(10);
        for (int i = mStartYear; i <= mEndYear; i++) {
            yearData.add(i + " " + gets(R.string.common_year));
        }

        // 生产月份
        ArrayList<String> monthData = new ArrayList<>(12);
        for (int i = 1; i <= 12; i++) {
            monthData.add(i + " " + gets(R.string.common_month));
        }

        mYearAdapter.setList(yearData);
        mMonthAdapter.setList(monthData);

        mYearManager = new PickerLayoutManager.Builder(getContext())
                .build();
        mMonthManager = new PickerLayoutManager.Builder(getContext())
                .build();

        rv_picker_year.setLayoutManager(mYearManager);
        rv_picker_month.setLayoutManager(mMonthManager);

        rv_picker_year.setAdapter(mYearAdapter);
        rv_picker_month.setAdapter(mMonthAdapter);

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);

        mYearManager.setOnPickerListener(this);
    }

    private void setYear(int year){
        int index = year - mStartYear;
        if (index < 0) {
            index = 0;
        } else if (index > mYearAdapter.getItemCount() - 1) {
            index = mYearAdapter.getItemCount() - 1;
        }
        rv_picker_year.scrollToPosition(index);
    }

    private void setMonth(int month){
        int index = month - 1;
        if (index < 0) {
            index = 0;
        } else if (index > mMonthAdapter.getItemCount() - 1) {
            index = mMonthAdapter.getItemCount() - 1;
        }
        rv_picker_month.scrollToPosition(index);
    }

    @OnClick(R.id.btn_done)
    public void onClickView(){
        if (onListener!=null)
            onListener.onSelected(this,mStartYear + mYearManager.getPickedPosition(),mMonthManager.getPickedPosition() + 1);
    }

    @Override
    public void onPicked(RecyclerView recyclerView, int position) {

    }


    private static final class PickerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public PickerAdapter() {
            super(R.layout.module_dialog_time_picker_item);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            baseViewHolder.setText(R.id.tv_picker_name, s);
        }
    }

}
