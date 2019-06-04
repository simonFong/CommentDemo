package com.simonfong.app.ImageAdd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.simonfong.app.R;
import com.simonfong.imageadd.addImage.viewpluimg.CancelOrOkDialog;
import com.simonfong.imageadd.addImage.viewpluimg.MainConstant;
import com.simonfong.imageadd.addImage.viewpluimg.PlusImageFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.simonfong.imageadd.addImage.viewpluimg.MainConstant.RESULT_CODE_VIEW_IMG;

/**
 * @author simonfong
 * 使用fragment例子,需要自己写逻辑
 */
public class PlusImageFragmentActivity extends AppCompatActivity {

    @BindView(R.id.back_iv)
    ImageView mBackIv;
    @BindView(R.id.position_tv)
    TextView mPositionTv;
    @BindView(R.id.delete_iv)
    ImageView mDeleteIv;
    @BindView(R.id.fl_layout)
    FrameLayout mFlLayout;
    private ContentFrameLayout contentFrameLayout;
    private ArrayList<String> imgList;
    private int mPosition;
    private PlusImageFragment mPlusImageFragment;

    /**
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
        setContentView(R.layout.activity_plus_image_fragment);
        ButterKnife.bind(this);
        imgList = getIntent().getStringArrayListExtra(MainConstant.IMG_LIST);
        mPosition = getIntent().getIntExtra(MainConstant.POSITION, 0);
        initFragment();
    }

    private void initFragment() {
        mPlusImageFragment = PlusImageFragment.getInstance(imgList, mPosition, new AddImageGlideImageLoader());
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_layout, mPlusImageFragment).commit();
        mPlusImageFragment.setPlusImageListener(new PlusImageFragment.PlusImageListener() {
            @Override
            public void change(int currPosition, ArrayList<String> data) {
                imgList = data;
                if (data.size() == 0) {
                    back();
                    return;
                }
                mPositionTv.setText(currPosition + 1 + "/" + data.size());
            }
        });
    }

    @OnClick({R.id.back_iv, R.id.delete_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                back();
                break;
            case R.id.delete_iv:
                CancelOrOkDialog dialog = new CancelOrOkDialog(this, "要删除这张图片吗?") {
                    @Override
                    public void ok() {
                        super.ok();
                        //删除当前图片
                        mPlusImageFragment.delete();
                        dismiss();
                    }
                };
                dialog.show();
                break;
            default:
        }
    }

    //返回上一个页面
    private void back() {
        Intent intent = getIntent();
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, imgList);
        setResult(RESULT_CODE_VIEW_IMG, intent);
        finish();
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
