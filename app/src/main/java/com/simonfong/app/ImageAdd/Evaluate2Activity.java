package com.simonfong.app.ImageAdd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.simonfong.app.R;
import com.simonfong.imageadd.addImage.ui.AddPicView;
import com.simonfong.imageadd.addImage.ui.RatingBar;
import com.simonfong.imageadd.addImage.viewpluimg.MainConstant;
import com.simonfong.imageadd.addImage.viewpluimg.PlusImageActivity;

import java.util.ArrayList;
import java.util.List;


public class Evaluate2Activity extends AppCompatActivity implements View.OnClickListener {

    AddPicView mApvSelectPic;
    private int IMAGE_PICKER = 0;
    private int MAX_COUNT = 9;
    private int max_count;
    private ImagePicker mImagePicker;
    private RatingBar mPingjiaStarView;
    private TextView mPingjiaPointTv;
    private Button mOkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_circle);
        mApvSelectPic = findViewById(R.id.apv_select_pic);
        mPingjiaStarView = findViewById(R.id.pingjia_star);
        mPingjiaPointTv = findViewById(R.id.pingjia_point_tv);
        mOkBtn = findViewById(R.id.btn_ok);
        mOkBtn.setOnClickListener(this);
        initView();
        initImagePicker();
    }

    private void initView() {
        max_count = MAX_COUNT;
        mApvSelectPic.setImageLoader(new AddImageGlideImageLoader());
        mApvSelectPic.setOnAddClickListener(new AddPicView.OnAddClickListener() {
            @Override
            public void addClick(View view) {
                mImagePicker.setSelectLimit(max_count);    //选中数量限制
                Intent picture = new Intent(Evaluate2Activity.this, ImageGridActivity.class);
                startActivityForResult(picture, IMAGE_PICKER);
            }

            @Override
            public void picClick(View view, int position) {
                ArrayList<String> data = (ArrayList<String>) mApvSelectPic.getData();
                viewPluImg(position, data);
            }

            @Override
            public void deleteClick(View view) {
                max_count = max_count + 1;
            }


        });

        mPingjiaStarView.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                mPingjiaPointTv.setText(ratingCount + "");
            }
        });
        mPingjiaStarView.setStar(2f);
    }

    /**
     * 跳转到展示大图片
     *
     * @param position
     * @param data
     */
    private void viewPluImg(int position, ArrayList<String> data) {
        Intent newIntent = PlusImageActivity.getNewIntent(data, position, true, new AddImageGlideImageLoader());
        Intent newIntent1 = PlusImageFragmentActivity.getNewIntent(this, data, position, true);
        startActivityForResult(newIntent1, MainConstant.REQUEST_CODE_MAIN);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null) {
                List<ImageItem> imageItems = (ArrayList<ImageItem>) data.getSerializableExtra(
                        ImagePicker.EXTRA_RESULT_ITEMS);
                max_count = max_count - imageItems.size();
                for (int i = 0; i < imageItems.size(); i++) {
                    mApvSelectPic.addData(imageItems.get(i).path);
                }
            }
        }

        if (requestCode == MainConstant.REQUEST_CODE_MAIN && resultCode == MainConstant.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(MainConstant.IMG_LIST); //要删除的图片的集合
            //            mPicList.clear();
            //            mPicList.addAll(toDeletePicList);
            mApvSelectPic.setNewData(toDeletePicList);
            max_count = MAX_COUNT - toDeletePicList.size();
            //            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }

    private void initImagePicker() {
        mImagePicker = ImagePicker.getInstance();
        mImagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        mImagePicker.setShowCamera(true);  //显示拍照按钮
        mImagePicker.setCrop(false);        //允许裁剪（单选才有效）
        mImagePicker.setSaveRectangle(true); //是否按矩形区域保存
        mImagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        mImagePicker.setFocusWidth(600);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        mImagePicker.setFocusHeight(600);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        mImagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        mImagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                List<String> data = mApvSelectPic.getData();
                Toast.makeText(Evaluate2Activity.this, "获取图片数量：" + data.size(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < data.size(); i++) {
                    Log.e("TAG", "data:" + data.get(i));
                }
                break;
            default:
        }
    }
}
