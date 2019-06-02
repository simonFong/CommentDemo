package com.simonfong.app.ImageAdd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.View;

import com.simonfong.app.R;
import com.simonfong.imageadd.addImage.loader.ImageLoaderInterface;
import com.simonfong.imageadd.addImage.viewpluimg.MainConstant;
import com.simonfong.imageadd.addImage.viewpluimg.PlusImageActivity;
import com.simonfong.imageadd.addImage.viewpluimg.PlusImageFragment;

import java.util.ArrayList;

public class PlusImageFragmentActivity extends AppCompatActivity {

    private ContentFrameLayout contentFrameLayout;
    private ArrayList<String> imgList;
    private int mPosition;

    /**
     *
     * @param context
     * @param data       图片的数据源
     * @param position   第几张图片
     * @param showDelect 是否显示删除按钮
     * @return
     */
    public static Intent getNewIntent(Context context, ArrayList<String> data, int position, boolean
            showDelect) {
        Intent intent = new Intent(context, PlusImageFragmentActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, data);
        intent.putExtra(MainConstant.POSITION, position);
        intent.putExtra(MainConstant.SHOW_DELECT, showDelect);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_image__fragment);
        imgList = getIntent().getStringArrayListExtra(MainConstant.IMG_LIST);
        mPosition = getIntent().getIntExtra(MainConstant.POSITION, 0);
        initFragment();
    }

    private void initFragment() {
        PlusImageFragment instance = PlusImageFragment.getInstance(imgList, mPosition, new AddImageGlideImageLoader());
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_layout, instance).commit();
        instance.setPlusImageBackListener(new PlusImageFragment.PlusImageBackListener() {
            @Override
            public void back(ArrayList<String> imgList) {
                Log.e("dslfkaj","imgList="+imgList.size());
            }
        });
    }
}
