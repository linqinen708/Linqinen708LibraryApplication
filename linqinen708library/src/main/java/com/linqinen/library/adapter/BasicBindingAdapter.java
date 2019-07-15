package com.linqinen.library.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linqinen.library.R;
import com.linqinen.library.utils.LogT;


/**
 * Created by 林 on 2017/9/27.
 * BasicBindingAdapter2 更改了adapter的刷新机制，所有刷新都只是普通的notifyDataSetChanged();
 * BasicBindingAdapter3增加了HeaderView
 */

public abstract class BasicBindingAdapter<M, B extends ViewDataBinding> extends RecyclerView.Adapter {
    private Context mContext;
    protected ObservableArrayList<M> items;
    protected ListChangedCallback itemsChangeCallback;

    public Context getContext() {
        return mContext;
    }

    private boolean isShowFootView = false;

    /**
     * 每次改变底部footView，则刷新adapter
     */
    public void setShowFootView(boolean showFootView) {
        isShowFootView = showFootView;
        if (showFootView) {
            notifyDataSetChanged();
        }
    }

    /**
     * 判断viewType 是不是FooterView
     */
    public static final int TYPE_FOOTER_VIEW = 1;
    /**
     * 判断viewType 是不是HeaderView
     */
    public static final int TYPE_HEADER_VIEW = 2;

    public BasicBindingAdapter(Context context) {
        this.mContext = context;
        this.items = new ObservableArrayList<>();
        this.itemsChangeCallback = new ListChangedCallback();
    }

    public ObservableArrayList<M> getItems() {
        return items;
    }

    public static class BaseBindingViewHolder extends RecyclerView.ViewHolder {
        public BaseBindingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;

    }

    /**重写id可以使刷新不闪烁*/
    @Override
    public long getItemId(int position) {
        if (getHeaderViewLayoutResId() != 0) {
            if (position == 0) {
                return getHeaderViewLayoutResId();
            } else {
                position--;
            }
        }
        if (isShowFootView) {
            if (position == items.size()) {
                return R.layout.adapter_foot_view;
            }else{
                position--;
            }
        }
        /*当已经出现footer的时候，如果又上划到顶部
         * itemPosition 会出现-1的情况，应该修正为0
         * 也就是item的第一个值
         * */
        if(position < 0){
            position = 0;
        }
        if(position >= items.size()){
            LogT.e("越界了，position："+position +",mList.size():"+items.size());
            return System.currentTimeMillis();
        }
        return items.get(position).hashCode();
    }

    /**
     * 每次让items + 1 多一个footView
     */
    @Override
    public int getItemCount() {
        int count = items.size();

        /*如果有HeaderView,这数量+1*/
        if (getHeaderViewLayoutResId() != 0) {
            count++;
        }
        /*如果有FooterView,数量+1*/
        if (isShowFootView) {
            return ++count;
        } else {
            return count;
        }
    }

    @Override
    public int getItemViewType(int position) {

        /*如果是第一个item，则判断是否是headerView*/
        if (position == 0 && getHeaderViewLayoutResId() != 0) {
            return TYPE_HEADER_VIEW;
        }

        if (getHeaderViewLayoutResId() != 0 && position > 0) {
            position--;
        }

//        LogT.i("getItemViewType:"+position );
        if (isShowFootView) {
            if (position  == items.size()) {
                return TYPE_FOOTER_VIEW;
            }
            /*super.getItemViewType(position) 实际上默认返回值就是0*/
            return super.getItemViewType(position);
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER_VIEW) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_foot_view, parent, false);
            return new FootViewHolder(view);
        } else if (viewType == TYPE_HEADER_VIEW && getHeaderViewLayoutResId() != 0) {
//            final View view = LayoutInflater.from(mContext).inflate(getHeaderViewLayoutResId(), parent, false);
            return new HeaderViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), getHeaderViewLayoutResId(), parent, false).getRoot());
        } else {
            B binding = DataBindingUtil.inflate(LayoutInflater.from(this.mContext), this.getLayoutResId(viewType), parent, false);

            return new BaseBindingViewHolder(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        try {
            if (holder != null && holder instanceof BaseBindingViewHolder) {

                if (getHeaderViewLayoutResId() != 0 && position > 0) {
                    position--;
                }

                B binding = DataBindingUtil.getBinding(holder.itemView);
                if (binding != null) {
                    if (mOnItemClickListener != null) {
                        binding.getRoot().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                            }
                        });
                    }
                }
                this.onBindItem(binding, this.items.get(position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.items.addOnListChangedCallback(itemsChangeCallback);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.items.removeOnListChangedCallback(itemsChangeCallback);
    }


    protected void resetItems(ObservableArrayList<M> newItems) {
        this.items = newItems;
    }

    //endregion
    @LayoutRes
    protected abstract int getLayoutResId(int viewType);

    /**
     * 使用HeaderView，默认为0，表示没有，如果有，则重写该方法，返回R.layout.xxx
     */
    @LayoutRes
    protected int getHeaderViewLayoutResId() {
        return 0;
    }


    protected abstract void onBindItem(B binding, M mBean);

    private class ListChangedCallback extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<M>> {
        @Override
        public void onChanged(ObservableArrayList<M> newItems) {
            LogT.i("onChanged:");
            resetItems(newItems);
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableArrayList<M> newItems, int i, int i1) {
            LogT.i("onItemRangeChanged:");
            resetItems(newItems);
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(ObservableArrayList<M> newItems, int i, int i1) {
//            LogT.i("onItemRangeInserted:" + i + "--" + i1);
            resetItems(newItems);
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeMoved(ObservableArrayList<M> newItems, int i, int i1, int i2) {
            LogT.i("onItemRangeMoved:");
            resetItems(newItems);
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeRemoved(ObservableArrayList<M> newItems, int positionStart, int itemCount) {
            LogT.i("onItemRangeRemoved:");
            resetItems(newItems);
            notifyDataSetChanged();
        }
    }
}
