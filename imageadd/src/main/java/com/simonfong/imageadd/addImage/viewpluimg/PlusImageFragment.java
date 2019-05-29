package com.simonfong.imageadd.addImage.viewpluimg;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simonfong.imageadd.R;
import com.simonfong.imageadd.addImage.loader.ImageLoaderInterface;

import java.util.ArrayList;

public class PlusImageFragment extends Fragment  {
    public static final int RESULT_CODE_VIEW_IMG = 11; //查看大图页面的结果码
    private static ImageLoaderInterface imageLoader;
    private ViewPager viewPager; //展示图片的ViewPager
    private TextView positionTv; //图片的位置，第几张图片
    private ArrayList<String> imgList; //图片的数据源
    private int mPosition; //第几张图片
    private ViewPagerAdapter mAdapter;
    private boolean mShowDelect;//是否显示删除按钮

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View inflate = inflater.inflate(R.layout.fragment_plus_image, container);
//        viewPager = (ViewPager) inflate.findViewById(R.id.viewPager);
//        positionTv = (TextView) inflate.findViewById(R.id.position_tv);
//        inflate.findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                back();
//            }
//        });
//        inflate.findViewById(R.id.delete_iv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //删除图片
//                deletePic();
//            }
//        });
//        inflate.findViewById(R.id.delete_iv).setVisibility(mShowDelect ? View.VISIBLE : View.GONE);
//        return inflate;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        imgList = getArguments().getStringArrayList(MainConstant.IMG_LIST);
//        mPosition = getArguments().getInt(MainConstant.POSITION, 0);
//        mShowDelect = getArguments().getBoolean(MainConstant.SHOW_DELECT, false);
//        initView();
//    }
//
//    private void initView() {
//
//
//        viewPager.addOnPageChangeListener(this);
//
//        mAdapter = new ViewPagerAdapter(getActivity(), imgList);
//        mAdapter.setImageLoader(imageLoader);
//        viewPager.setAdapter(mAdapter);
//        positionTv.setText(mPosition + 1 + "/" + imgList.size());
//        viewPager.setCurrentItem(mPosition);
//    }
//
//    //删除图片
//    private void deletePic() {
//        CancelOrOkDialog dialog = new CancelOrOkDialog(getActivity(), "要删除这张图片吗?") {
//            @Override
//            public void ok() {
//                super.ok();
//                imgList.remove(mPosition); //从数据源移除删除的图片
//                if (imgList.size() == 0) {
//                    back();
//                }
//                setPosition();
//                dismiss();
//            }
//        };
//        dialog.show();
//    }
//
//    //设置当前位置
//    private void setPosition() {
//        positionTv.setText(mPosition + 1 + "/" + imgList.size());
//        viewPager.setCurrentItem(mPosition);
//        mAdapter.notifyDataSetChanged();
//    }
//
//    //返回上一个页面
//    private void back() {
//        Intent intent = getActivity().getIntent();
//        intent.putStringArrayListExtra(MainConstant.IMG_LIST, imgList);
//        getActivity().setResult(RESULT_CODE_VIEW_IMG, intent);
//        getActivity().finish();
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        mPosition = position;
//        positionTv.setText(position + 1 + "/" + imgList.size());
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            //按下了返回键
//            back();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
