package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.OnBusinessTimeListener;
import com.gxdingo.sg.view.PickerLayoutManager;
import com.lxj.xpopup.core.BottomPopupView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Weaving
 * @date: 2021/6/4
 * @page:
 */
public class BusinessTimePopupView extends BottomPopupView implements View.OnClickListener {

    private TextView btn_done;


    private RecyclerView mStartHourView;
    private RecyclerView mStartMinuteView;
    private RecyclerView mEndHourView;
    private RecyclerView mEndMinuteView;

    private PickerLayoutManager mStartHourManager;
    private PickerLayoutManager mStartMinuteManager;
    private PickerLayoutManager mEndHourManager;
    private PickerLayoutManager mEndMinuteManager;

    private PickerAdapter mStartHourAdapter;
    private PickerAdapter mStartMinuteAdapter;
    private PickerAdapter mEndHourAdapter;
    private PickerAdapter mEndMinuteAdapter;

    private OnBusinessTimeListener onBusinessTimeListener;

    public BusinessTimePopupView(@NonNull Context context, OnBusinessTimeListener onBusinessTimeListener) {
        super(context);
        this.onBusinessTimeListener = onBusinessTimeListener;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_business_time;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        btn_done = findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);


        mStartHourView = findViewById(R.id.rv_start_time_hour);
        mStartMinuteView = findViewById(R.id.rv_start_time_minute);
        mEndHourView = findViewById(R.id.rv_end_time_hour);
        mEndMinuteView = findViewById(R.id.rv_end_time_minute);

        mStartHourAdapter = new PickerAdapter();
        mStartMinuteAdapter = new PickerAdapter();
        mEndHourAdapter = new PickerAdapter();
        mEndMinuteAdapter = new PickerAdapter();

        // 生产小时
        ArrayList<String> hourData = new ArrayList<>(24);
        for (int i = 0; i <= 23; i++) {
            hourData.add((i < 10 ? "0" : "") + i + " " + gets(R.string.common_hour));
        }
        mStartHourAdapter.setList(hourData);
        mEndHourAdapter.setList(hourData);

        // 生产分钟
        ArrayList<String> minuteData = new ArrayList<>(60);
        for (int i = 0; i <= 59; i++) {
            minuteData.add((i < 10 ? "0" : "") + i + " " + gets(R.string.common_minute));
        }
        mStartMinuteAdapter.setList(minuteData);
        mEndMinuteAdapter.setList(minuteData);

        mStartHourManager = new PickerLayoutManager.Builder(getContext()).build();
        mStartMinuteManager = new PickerLayoutManager.Builder(getContext()).build();
        mEndHourManager = new PickerLayoutManager.Builder(getContext()).build();
        mEndMinuteManager = new PickerLayoutManager.Builder(getContext()).build();

        mStartHourView.setLayoutManager(mStartHourManager);
        mStartMinuteView.setLayoutManager(mStartMinuteManager);
        mEndHourView.setLayoutManager(mEndHourManager);
        mEndMinuteView.setLayoutManager(mEndMinuteManager);

        mStartHourView.setAdapter(mStartHourAdapter);
        mStartMinuteView.setAdapter(mStartMinuteAdapter);
        mEndHourView.setAdapter(mEndHourAdapter);
        mEndMinuteView.setAdapter(mEndMinuteAdapter);

        Calendar calendar = Calendar.getInstance();

        //设置当前时间为起始营业时间
        setHour(calendar.get(Calendar.HOUR_OF_DAY));
        setMinute(calendar.get(Calendar.MINUTE));
    }

    public BusinessTimePopupView setHour(String hour) {
        return setHour(Integer.parseInt(hour));
    }

    public BusinessTimePopupView setHour(int hour) {
        int index = hour;
        if (index < 0 || hour == 24) {
            index = 0;
        } else if (index > mStartHourAdapter.getItemCount() - 1) {
            index = mStartHourAdapter.getItemCount() - 1;
        }
        mStartHourView.scrollToPosition(index);
        return this;
    }

    public BusinessTimePopupView setMinute(String minute) {
        return setMinute(Integer.parseInt(minute));
    }

    public BusinessTimePopupView setMinute(int minute) {
        int index = minute;
        if (index < 0) {
            index = 0;
        } else if (index > mStartMinuteAdapter.getItemCount() - 1) {
            index = mStartMinuteAdapter.getItemCount() - 1;
        }
        mStartMinuteView.scrollToPosition(index);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_done:
                if (onBusinessTimeListener != null) {
                    onBusinessTimeListener.onSelected(this, mStartHourManager.getPickedPosition(),
                            mStartMinuteManager.getPickedPosition(), mEndHourManager.getPickedPosition(), mEndMinuteManager.getPickedPosition());
                }
                break;
        }
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
