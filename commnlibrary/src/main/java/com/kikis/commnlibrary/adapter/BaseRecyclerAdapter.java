package com.kikis.commnlibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kikis.commnlibrary.utils.KikisUitls;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;

/**
 * Created by lian on 2018/3/1.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final String TAG = getTAG(BaseRecyclerAdapter.class);
    //list集合
    protected final List<T> mData;

    protected final Context mContext;
    //上下文
    protected LayoutInflater mInflater;
    //点击item监听
    protected OnItemClickListener mClickListener;
    //长按item监听
    protected OnItemLongClickListener mLongClickListener;

    //item点击间隔开关
    private boolean intervalSwitch = true;

    //最后点击的时间
    private long lastClickTime = 0;
    //点击时间间隔
    private int clickInterval = 300;

    /**
     * 构造方法
     *
     * @param list
     */
    public BaseRecyclerAdapter(List<T> list) {
        mData = (list != null) ? list : new ArrayList<T>();
        //有时候需要传入context不能用getApplicationContext
        mContext = KikisUitls.getContext();
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 构造方法
     * 有时需要传入Activity的上下文，否则使用MyBaseApplication.getContext()会有问题。
     *
     * @param list
     */
    public BaseRecyclerAdapter(List<T> list, Context context) {
        mData = (list != null) ? list : new ArrayList<T>();
        //有时候需要传入context不能用getApplicationContext
        mContext = context == null ? KikisUitls.getContext() : context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void clear() {
        if (mData != null) {
            this.mData.clear();
            notifyDataSetChanged();
        }
    }


    /**
     * 方法中主要是引入xml布局文件,并且给item点击事件和item长按事件赋值
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        try {
            int type = getItemLayoutId(viewType);
            View view = mInflater.inflate(type, parent, false);


            final RecyclerViewHolder holder = new RecyclerViewHolder(mContext, view);

            if (mClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intervalSwitch) {
                            if (System.currentTimeMillis() - lastClickTime > clickInterval) {
                                lastClickTime = System.currentTimeMillis();
                                mClickListener.onItemClick(holder.itemView, holder.getPosition());
                            }
                        } else
                            mClickListener.onItemClick(holder.itemView, holder.getPosition());

                    }
                });
            }
            if (mLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mLongClickListener.onItemLongClick(holder.itemView, holder.getPosition());
                        return true;
                    }
                });
            }
            return holder;
        } catch (Exception e) {
            Log.e(TAG, "BaseRecyclerAdapter Error === " + e);
        }
        return null;
    }


    /**
     * onBindViewHolder这个方法主要是给子项赋值数据的
     * 这子类重写此方法后bindData将不执行。
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        bindData(holder, position, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * add方法是添加item方法
     *
     * @param pos
     * @param item
     */
    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    /**
     * addll方法是添加item方法
     *
     * @param data
     */
    public void addDataAll(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 获取数据集
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * addll方法是添加item方法
     *
     * @param data
     */
    public void addData(T data, int pos) {
        mData.add(data);
        notifyItemChanged(pos);
    }


    /**
     * delete方法是删除item方法
     *
     * @param pos
     */
    public void delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    /**
     * item点击事件set方法
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    /**
     * item点击事件get方法
     */
    public OnItemClickListener getOnItemClickListener() {
        return mClickListener;
    }

    /**
     * item长按事件get方法
     */
    public OnItemLongClickListener getOnItemLongClickListener() {
        return mLongClickListener;
    }

    /**
     * item长按事件set方法
     *
     * @param listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    /**
     * item中xml布局文件方法
     *
     * @param viewType
     * @return
     */
    abstract public int getItemLayoutId(int viewType);

    /**
     * 赋值数据方法
     *
     * @param holder
     * @param position
     * @param item
     */
    abstract public void bindData(RecyclerViewHolder holder, int position, T item);


    /**
     * item点击事件接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }


    /**
     * item长按事件接口
     */
    public interface OnItemLongClickListener {
        public void onItemLongClick(View itemView, int pos);
    }

    /**
     * 关闭item点击间隔 (默认开启)
     */
    public void closeItemClickInterval() {
        intervalSwitch = false;
    }

    /**
     * 设置点击间隔时间（单位毫秒）
     */
    public void setInterval(int value) {
        clickInterval = value;
    }
}