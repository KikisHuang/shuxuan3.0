package com.gxdingo.sg.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.IMOtherFunctionsAdapter;
import com.kikis.commnlibrary.view.recycler_view.PullDividerItemDecoration;
import com.kikis.commnlibrary.view.recycler_view.PullGridLayoutManager;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IM其他功能Fragment
 *
 * @author JM
 */
public class IMOtherFunctionsFragment extends Fragment {
    @BindView(R.id.prv_other_functions)
    PullRecyclerView prvOtherFunctions;
    Activity mActivity;
    Context mContext;
    IMOtherFunctionsAdapter mIMOtherFunctionsAdapter;//其他功能适配器，例如相册、拍照等


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity) mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_fragment_im_other_functions, null);
        ButterKnife.bind(this, view);
        mIMOtherFunctionsAdapter = new IMOtherFunctionsAdapter(mContext, IMOtherFunctionsAdapter.TYPE_USER);
        prvOtherFunctions.addItemDecoration(new PullDividerItemDecoration(mContext, (int) getResources().getDimension(R.dimen.dp20), 0));
        prvOtherFunctions.setLayoutManager(new PullGridLayoutManager(mContext, 4));
        prvOtherFunctions.setPullAdapter(mIMOtherFunctionsAdapter);
        prvOtherFunctions.setOnItemClickListener(new PullRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().post(new FunctionsItem(IMOtherFunctionsAdapter.TYPE_USER, position, mIMOtherFunctionsAdapter.getTitleItems()[position]));
            }
        });
        return view;
    }

    public class FunctionsItem {
        public int type;
        public int position;
        public String name;

        public FunctionsItem(int type, int position, String name) {
            this.type = type;
            this.position = position;
            this.name = name;
        }
    }
}
