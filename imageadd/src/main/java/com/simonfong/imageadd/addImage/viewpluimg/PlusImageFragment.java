package com.simonfong.imageadd.addImage.viewpluimg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simonfong.imageadd.R;
import com.simonfong.imageadd.addImage.loader.ImageLoaderInterface;

import java.util.ArrayList;

public class PlusImageFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private static ImageLoaderInterface imageLoader;
    private ViewPager viewPager; //展示图片的ViewPager
    private ArrayList<String> imgList; //图片的数据源
    private int mPosition; //第几张图片
    private ViewPagerAdapter mAdapter;
    private PlusImageListener mListener;


    /**
     * @param data        图片数据
     * @param position    选择的下标
     * @param imageLoader 显示图片的加载器
     */
    public static PlusImageFragment getInstance(ArrayList<String> data, int position, ImageLoaderInterface imageLoader) {
        PlusImageFragment plusImageFragment = new PlusImageFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(MainConstant.IMG_LIST, data);
        bundle.putInt(MainConstant.POSITION, position);
        PlusImageFragment.imageLoader = imageLoader;
        plusImageFragment.setArguments(bundle);
        return plusImageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_plus_image, null);
        viewPager = (ViewPager) inflate.findViewById(R.id.viewPager);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgList = getArguments().getStringArrayList(MainConstant.IMG_LIST);
        mPosition = getArguments().getInt(MainConstant.POSITION, 0);
        initView();
    }

    private void initView() {
        viewPager.addOnPageChangeListener(this);
        mAdapter = new ViewPagerAdapter(getActivity(), imgList);
        mAdapter.setImageLoader(imageLoader);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mPosition);
    }


    public interface PlusImageListener {
        /**
         * @param currPosition 当前position
         * @param data         当前数据源
         */
        void change(int currPosition, ArrayList<String> data);
    }


    public void setPlusImageListener(PlusImageListener listener) {
        mListener = listener;
    }

    /**
     * 删除当前图片
     */
    public void delete() {
        imgList.remove(mPosition);
        mListener.change(mPosition, imgList);
        setPosition();
    }

    /**
     * 设置当前位置
     */
    private void setPosition() {
        viewPager.setCurrentItem(mPosition);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        if (mListener != null) {
            mListener.change(mPosition, imgList);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
