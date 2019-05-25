package com.simonfong.imageadd.addImage.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.simonfong.imageadd.R;
import com.simonfong.imageadd.addImage.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.Collections;
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
    private int mCloseDrawableRes;
    private final boolean mCanDrag;
    private View mInflate;
    private int mDefaultAddDrawableRes;
    private ImageLoaderInterface imageLoader;


    public AddPicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddPicView);
        //能加入的最大数量，默认9个
        mMaxNum = typedArray.getInteger(R.styleable.AddPicView_max_num, 9);
        //每一行显示的最大数量，默认3个
        mSingleLineShowNum = typedArray.getInteger(R.styleable.AddPicView_single_line_show_num, 3);
        //是否显示删除按钮
        mShowDelectPic = typedArray.getBoolean(R.styleable.AddPicView_show_delete_pic, true);
        //设置删除按钮资源文件
        mCloseDrawableRes = typedArray.getResourceId(R.styleable.AddPicView_close_drawable_res, R.drawable.img_delect);
        //设置是否可拖动，默认可以
        mCanDrag = typedArray.getBoolean(R.styleable.AddPicView_can_drag, false);
        //设置默认添加资源文件
        mDefaultAddDrawableRes = typedArray.getResourceId(R.styleable.AddPicView_default_add_drawable_res, R.mipmap.ic_add_img);
        mContext = context;
        initView();
    }

    private void initView() {
        mInflate = LayoutInflater.from(mContext).inflate(R.layout.layout_add_pic, this, true);
        RecyclerView recyclerview = mInflate.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(mContext, mSingleLineShowNum));
        mAddPicAdapter = new AddPicAdapter(mContext);
        recyclerview.setAdapter(mAddPicAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchHelperCallBack());
        itemTouchHelper.attachToRecyclerView(recyclerview);
    }


    class MyItemTouchHelperCallBack extends ItemTouchHelper.Callback {

        /**
         * 声明不同状态下可以移动的方向（idle, swiping, dragging）
         *
         * @param recyclerView
         * @param viewHolder
         * @return
         */
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFrlg = 0;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return makeMovementFlags(dragFrlg, 0);

        }

        /**
         * 拖动的项目从旧位置移动到新位置时调用
         *
         * @param recyclerView
         * @param viewHolder
         * @param target
         * @return
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //得到拖拽前的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mAddPicAdapter.getData(), i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mAddPicAdapter.getData(), i, i - 1);
                }
            }
            mAddPicAdapter.notifyItemMoved(fromPosition, toPosition);

            return true;
        }

        /**
         * 是否可以把拖动的ViewHolder拖动到目标ViewHolder之上
         *
         * @param recyclerView
         * @param current
         * @param target
         * @return
         */
        @Override
        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder
                target) {
            //得到当拖拽的viewHolder的Position
            int fromPosition = current.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();
            if (mAddPicAdapter.getData().size() != mMaxNum) {//达到最大数量，mData数量等于adapter的item数量
                if (toPosition == mAddPicAdapter.getItemCount() - 1) {
                    return false;
                }
            }
            return super.canDropOver(recyclerView, current, target);

        }

        /**
         * 滑动到消失后的调用
         *
         * @param viewHolder
         * @param direction
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        /**
         * 返回值决定是否有拖动操作
         *
         * @return
         */
        @Override
        public boolean isLongPressDragEnabled() {
            return mCanDrag;
        }

        /**
         * 长按选中Item的时候开始调用
         * 长按高亮
         *
         * @param viewHolder
         * @param actionState
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            //                                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            //                                    viewHolder.itemView.setBackgroundColor(Color.RED);
            //                                    //获取系统震动服务//震动70毫秒
            //                                    Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            //                                    vib.vibrate(70);
            //                                }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原高亮
         *
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //                viewHolder.itemView.setBackgroundColor(0);
            mAddPicAdapter.notifyDataSetChanged();//完成拖动后刷新适配器，这样拖动后删除就不会错乱
        }
    }

    /**
     * 设置是否显示删除按钮
     *
     * @param isShow
     */
    public void setShowDelectPic(boolean isShow, int drawableRes) {
        mShowDelectPic = isShow;
        mCloseDrawableRes = drawableRes;
    }

    /**
     * 设置默认添加按钮的资源文件
     *
     * @param drawableRes
     */
    public void setDefaultAddDrawableRes(int drawableRes) {
        mDefaultAddDrawableRes = drawableRes;
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
     * 获取最大数量
     *
     * @return
     */
    public int getMaxNum() {
        return mMaxNum;
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
        mAddPicAdapter.notifyDataSetChanged();
        return mAddPicAdapter.getData();
    }


    public interface OnAddClickListener {
        void addClick(View view);//点击添加

        void picClick(View view, int position);//点击图片,一般进行图片放大，或者删除操作

        void deleteClick(View view);//点击删除后回调，用于设置选择图片的最大选择数
    }

    public void setOnAddClickListener(OnAddClickListener mOnAddClickListener) {
        this.mOnAddClickListener = mOnAddClickListener;
    }

    public void setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
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

        @SuppressLint("CheckResult")
        @Override
        public void onBindViewHolder(CommonHolder holder, final int position) {
            ImageView image = holder.mImage;
            ImageView closeIv = holder.mCloseIv;
            closeIv.setBackgroundResource(mCloseDrawableRes);


            if (imageLoader == null) {
                throw new NullPointerException("请先设置AddImageLoader");
            }
            if (!isInEditMode()) {
                if (mData.size() < mMaxNum && position == mData.size()) {//判断是否是显示加号的情况
                    imageLoader.displayImage(mContext, mDefaultAddDrawableRes, image);
                    closeIv.setVisibility(GONE);

                } else {
                    String item = mData.get(position);
                    imageLoader.displayImage(mContext, item, image);
                    closeIv.setVisibility(mShowDelectPic ? VISIBLE : GONE);
                }

                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnAddClickListener != null) {
                            if (mData.size() < mMaxNum && position == mData.size()) {
                                mOnAddClickListener.addClick(mInflate);
                            } else {
                                mOnAddClickListener.picClick(mInflate, position);
                            }
                        }

                    }
                });
            }

            closeIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.remove(position);
                    notifyDataSetChanged();
                    if (mOnAddClickListener != null) {
                        mOnAddClickListener.deleteClick(mInflate);

                    }
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

}
