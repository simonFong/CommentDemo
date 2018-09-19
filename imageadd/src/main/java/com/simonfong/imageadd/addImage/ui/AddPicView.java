package com.simonfong.imageadd.addImage.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.simonfong.imageadd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengzimin  on  2018/07/18.
 * interface by
 */
public class AddPicView extends LinearLayout {
    private Context mContext;
    private int mSingleLineShowNum;
    private int mMaxNum;
    private AddPicAdapter mAddPicAdapter;
    private OnAddClickListener mOnAddClickListener;
    private boolean mShowDelectPic;


    public AddPicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddPicView);
        //能加入的最大数量，默认9个
        mMaxNum = typedArray.getInteger(R.styleable.AddPicView_max_num, 9);
        //每一行显示的最大数量，默认3个
        mSingleLineShowNum = typedArray.getInteger(R.styleable.AddPicView_single_line_show_num, 3);
        //是否显示删除按钮
        mShowDelectPic = typedArray.getBoolean(R.styleable.AddPicView_show_delect_pic, true);
        mContext = context;
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_add_pic, this, true);
        RecyclerView recyclerview = inflate.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(mContext, mSingleLineShowNum));
        mAddPicAdapter = new AddPicAdapter(mContext);
        recyclerview.setAdapter(mAddPicAdapter);
    }

    /**
     * 设置是否显示删除按钮
     *
     * @param isShow
     */
    public void setShowDelectPic(boolean isShow) {
        mShowDelectPic = isShow;
    }

    /**
     * 设置单行显示的数量
     *
     * @param singleLineShowNum
     */
    public void setSingleLineShowNum(int singleLineShowNum) {
        mSingleLineShowNum = singleLineShowNum;
    }

    /**
     * 设置最大数量
     *
     * @param maxNum
     */
    public void setMaxNum(int maxNum) {
        mMaxNum = maxNum;
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setNewData(List<String> data) {
        mAddPicAdapter.setNewData(data);
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(String data) {
        mAddPicAdapter.addData(data);
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<String> getData() {
        return mAddPicAdapter.getData();
    }


    public interface OnAddClickListener {
        void addClick();//点击添加

        void picClick(int position);//点击图片,一般进行图片放大，或者删除操作

        void delectClick();//点击删除后回调，用于设置选择图片的最大选择数
    }

    public void setOnAddClickListener(OnAddClickListener mOnAddClickListener) {
        this.mOnAddClickListener = mOnAddClickListener;
    }

    class AddPicAdapter extends RecyclerView.Adapter<AddPicAdapter.CommonHolder> {

        private Context mContext;
        private List<String> mData = new ArrayList<>();

        /**
         * 设置数据
         *
         * @param data
         */
        public void setNewData(List<String> data) {
            mData = data;
            notifyDataSetChanged();
        }

        /**
         * 添加数据
         *
         * @param data
         */
        public void addData(String data) {
            mData.add(data);
            notifyDataSetChanged();
        }

        /**
         * 删除某个数据
         *
         * @param data
         */
        public void delectData(String data) {
            mData.remove(data);
            notifyDataSetChanged();
        }

        /**
         * 清空数据
         */
        public void clearData() {
            mData.clear();
            notifyDataSetChanged();
        }

        public List<String> getData() {
            return mData;
        }

        public AddPicAdapter(Context context) {
            mContext = context;
        }

        @Override
        public AddPicAdapter.CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_pic, parent, false);
            return new CommonHolder(view);
        }

        public class CommonHolder extends RecyclerView.ViewHolder {

            private final ImageView mImage;
            private final ImageView mCloseIv;

            public CommonHolder(View itemView) {
                super(itemView);
                mImage = itemView.findViewById(R.id.iv_pic);
                mCloseIv = itemView.findViewById(R.id.iv_close);
            }

        }

        @Override
        public void onBindViewHolder(CommonHolder holder, final int position) {
            ImageView image = holder.mImage;
            ImageView closeIv = holder.mCloseIv;
            if (mData.size() < mMaxNum && position == mData.size()) {//判断是否是显示加号的情况
                //                image.setBackgroundResource();
                Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new CenterCrop())).load(R.mipmap.ic_add_img).into(image);
                closeIv.setVisibility(GONE);
            } else {
                String item = mData.get(position);
                Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap
                                .ic_launcher)
                        .transform(new CenterCrop())).load(item).into(image);

                closeIv.setVisibility(mShowDelectPic ? VISIBLE : GONE);
            }

            image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mData.size() < mMaxNum && position == mData.size()) {
                        mOnAddClickListener.addClick();
                    } else {
                        mOnAddClickListener.picClick(position);
                    }
                }
            });

            closeIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.remove(position);
                    notifyDataSetChanged();
                    mOnAddClickListener.delectClick();
                }
            });
        }


        @Override
        public int getItemCount() {
            if (mData == null || mData.size() == 0) {
                return 1;
            }
            if (mData != null && mData.size() < mMaxNum) {
                return mData.size() + 1;
            }
            return mData.size();
        }

    }


    /**
     * 自定义控件 正方形显示 高度适应宽度
     * 不知道为什么使用报错：ClassNotFoundException
     */
    public static class MyImageView extends android.support.v7.widget.AppCompatImageView {
        public MyImageView(Context context) {
            super(context);
        }

        public MyImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        //传入参数widthMeasureSpec、heightMeasureSpec
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }
}
