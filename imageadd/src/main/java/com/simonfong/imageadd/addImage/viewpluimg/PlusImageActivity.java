package com.simonfong.imageadd.addImage.viewpluimg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.simonfong.imageadd.R;

import java.util.ArrayList;


/**
 * Created by fengzimin  on  2018/07/18.
 * 大图显示预览，带有删除按钮
 */

public class PlusImageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final int RESULT_CODE_VIEW_IMG = 11; //查看大图页面的结果码
    private ViewPager viewPager; //展示图片的ViewPager
    private TextView positionTv; //图片的位置，第几张图片
    private ArrayList<String> imgList; //图片的数据源
    private int mPosition; //第几张图片
    private ViewPagerAdapter mAdapter;
    private boolean mShowDelect;//是否显示删除按钮


    /**
     * @param context
     * @param data       图片的数据源
     * @param position   第几张图片
     * @param showDelect 是否显示删除按钮
     * @return
     */
    public static Intent getNewIntent(Context context, ArrayList<String> data, int position, boolean
            showDelect) {
        Intent intent = new Intent(context, PlusImageActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, data);
        intent.putExtra(MainConstant.POSITION, position);
        intent.putExtra(MainConstant.SHOW_DELECT, showDelect);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_image);
        imgList = getIntent().getStringArrayListExtra(MainConstant.IMG_LIST);
        mPosition = getIntent().getIntExtra(MainConstant.POSITION, 0);
        mShowDelect = getIntent().getBooleanExtra(MainConstant.SHOW_DELECT, false);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        positionTv = (TextView) findViewById(R.id.position_tv);
        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        findViewById(R.id.delete_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除图片
                deletePic();
            }
        });
        findViewById(R.id.delete_iv).setVisibility(mShowDelect ? View.VISIBLE : View.GONE);

        viewPager.setOnPageChangeListener(this);

        mAdapter = new ViewPagerAdapter(this, imgList);
        viewPager.setAdapter(mAdapter);
        positionTv.setText(mPosition + 1 + "/" + imgList.size());
        viewPager.setCurrentItem(mPosition);
    }

    //删除图片
    private void deletePic() {
        CancelOrOkDialog dialog = new CancelOrOkDialog(this, "要删除这张图片吗?") {
            @Override
            public void ok() {
                super.ok();
                imgList.remove(mPosition); //从数据源移除删除的图片
                setPosition();
                dismiss();
            }
        };
        dialog.show();
    }

    //设置当前位置
    private void setPosition() {
        positionTv.setText(mPosition + 1 + "/" + imgList.size());
        viewPager.setCurrentItem(mPosition);
        mAdapter.notifyDataSetChanged();
    }

    //返回上一个页面
    private void back() {
        Intent intent = getIntent();
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, imgList);
        setResult(RESULT_CODE_VIEW_IMG, intent);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        positionTv.setText(position + 1 + "/" + imgList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //按下了返回键
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
