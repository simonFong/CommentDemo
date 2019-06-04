先上图:
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181112173232800.gif)![在这里插入图片描述](https://img-blog.csdnimg.cn/20181112173247498.gif)

https://github.com/simonFong/CommentDemo
想用的直接到github下载就可以了，星星控件和添加图片的控件在imageadd的lib里

使用方法：
1.下载lib，将imageadd导入自己的工程，并在项目的build.gradle文件添加
```
dependencies {
          ...
          implementation project(':imageadd')
 }
```

2.星星控件
直接在自己的布局文件里添加
```
 <com.simonfong.imageadd.addImage.ui.RatingBar
            android:id="@+id/pingjia_star"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:layout_marginLeft="15dp"
            android:layout_toRightOf="@+id/textView"
            android:gravity="center"
            app:starCount="5"
            app:starEmpty="@drawable/rating_small_empty"
            app:starFill="@drawable/rating_small_full"
            app:starHalf="@drawable/rating_small_half"
            app:starImageSize="35dp"
            app:starPadding="1dp"
            app:stepSize="Half"/>
```
属性：
```
 <declare-styleable name="RatingBar">
        <!--尺寸值-->
        <attr name="starImageSize" format="dimension" />
        <!--星星间距-->
        <attr name="starPadding" format="dimension" />
        <!--星星总数-->
        <attr name="starCount" format="integer" />
        <!--空白的星星资源文件值-->
        <attr name="starEmpty" format="reference" />
        <!--满星资源文件值-->
        <attr name="starFill" format="reference" />
        <!--半星资源文件值-->
        <attr name="starHalf" format="reference" />
        <!--是否可点击boolean值-->
        <attr name="clickable" format="boolean" />
        <!--当前进度float值-->
        <attr name="starStep" format="float" />
        <!--每次进度方式的值，整星还是半星-->
        <attr name="stepSize">
            <enum name="Half" value="0" />
            <enum name="Full" value="1" />
        </attr>
    </declare-styleable>
  ```
简单使用:

```
mPingjiaStarView = findViewById(R.id.pingjia_star);
mPingjiaStarView.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
    @Override
    public void onRatingChange(float ratingCount) {
          mPingjiaPointTv.setText(ratingCount + "");
    }
});
mPingjiaStarView.setStar(2f);
```

  3.添加图片控件
  demo里使用的是imagepicker作为获取图片源的第三方库，这里出现了一个问题
  ```
  1.添加图片选择器 jeasonlzy/ImagePicker
版本为'com.android.support:appcompat-v7:27.1.1'出现两个问题
        
        a.会报多个不同版本错误，需要统一版本

        Error:Execution failed for task ':app:preDebugBuild'.
        > Android dependency 'com.android.support:appcompat-v7' has different version for the compile (27.0.2) and runtime (27.1.1) classpath. You should manually set the same version via DependencyResolution
        解决方式：
        需要在项目的build.gradle添加代码，统一版本：
        configurations.all {
                 resolutionStrategy.eachDependency { DependencyResolveDetails details ->
                     def requested = details.requested
                     if (requested.group == 'com.android.support') {
                         if (!requested.name.startsWith("multidex")) {
                             details.useVersion '27.1.1'
                         }
                     }
                 }
             }
             
             
        b.compileSdkVersion 版本 27 及以上，大图返回列表时数据空了
        java.lang.RuntimeException: Unable to resume activity {cn.dlc.zizhuyinliaoji.myapplication/com.lzy.imagepicker.ui.ImageGridActivity}: java.lang.IndexOutOfBoundsException
        解决方式：
        1.把版本讲到27以下，或者27版本以上的27.0.2/27.0.3，这两个测试可用，其他没有测试
        2.使用修改后的imagepicker-library，即在ImageDataSource类里onLoadFinished方法imageFolders.clear();前面加上代码
        if ((activity.getLifecycle().getCurrentState() == STARTED || activity.getLifecycle().getCurrentState() == RESUMED) && imageFolders.size() > 0) {
        return;
        }
        再导入工程，解决(修改后的库在demo也有提供)
  ```
  添加图片控件直接在布局文件里添加：
  ```
   <com.simonfong.imageadd.addImage.ui.AddPicView
        android:id="@+id/apv_select_pic"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:max_num="9"
        app:show_delete_pic="true"
        app:can_drag="true"
        app:single_line_show_num="3"/>
  ```
  属性：
  ```
  <declare-styleable name="AddPicView">
       <!--允许添加的最大数量-->
        <attr name="max_num" format="integer"/>
        <!--一行显示的最大数量-->
        <attr name="single_line_show_num" format="integer"/>
        <!--是否显示删除按钮-->
        <attr name="show_delete_pic" format="boolean"/>
        <!--删除按钮的资源文件-->
        <attr name="close_drawable_res" format="reference"/>
        <!--图片是否可拖动-->
        <attr name="can_drag" format="boolean"/>
        <!--默认图片资源-->
        <attr name="default_add_drawable_res" format="reference"/>

    </declare-styleable>
  ```
  简单使用：
  

```
//设置AddImageLoader
 mApvSelectPic.setImageLoader(new AddImageGlideImageLoader());
//设置新的数据
mApvSelectPic.setNewData(toDeletePicList);
//添加数据
mApvSelectPic.addData(imageItems.get(i).path);
//点击监听
 mApvSelectPic.setOnAddClickListener(new AddPicView.OnAddClickListener() {
            @Override
            public void addClick(View view) {//点击添加按钮
                mImagePicker.setSelectLimit(max_count);    //选中数量限制
                Intent picture = new Intent(EvaluateActivity.this, ImageGridActivity.class);
                startActivityForResult(picture, IMAGE_PICKER);
            }

            @Override
            public void picClick(View view, int position) {//点击图片
                ArrayList<String> data = (ArrayList<String>) mApvSelectPic.getData();
                viewPluImg(position, data);
            }

            @Override
            public void deleteClick(View view) {//点击删除按钮
                max_count = max_count + 1;
            }


        });
```
必须设置AddImageLoader，这里以Glide作为例子
```
public class AddImageGlideImageLoader extends AddImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transforms(new CenterCrop(), new RoundedCorners(30)))
                .into(imageView);
    }
}
```

2019年6月3日20:49:38修改
因为有同事提出说查看大图的界面跟项目原本风格不一致，特意添加一个查看大图的fragment，方便自定义界面。

使用例子
1.首先自定义一个展示activity
activity.xml
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.LinearLayoutCompat
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <android.support.v7.widget.Toolbar
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="#373b3e">

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <ImageView
                android:id="@+id/back_iv"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerVertical="true"
                android:contentDescription="@null"
                android:scaleType="centerCrop"
                android:src="@mipmap/back"/>

            <!--图片位置-->
            <TextView
                android:id="@+id/position_tv"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerVertical="true"
                android:layout_marginStart="15dp"
                android:layout_marginLeft="15dp"
                android:layout_toEndOf="@id/back_iv"
                android:layout_toRightOf="@id/back_iv"
                android:textColor="#ffffff"
                android:textSize="18sp"
                tools:text="1/1"/>

            <ImageView
                android:id="@+id/delete_iv"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentEnd="true"
                android:layout_alignParentRight="true"
                android:layout_centerVertical="true"
                android:layout_marginEnd="16dp"
                android:layout_marginRight="16dp"
                android:contentDescription="@null"
                android:padding="10dp"
                android:scaleType="centerCrop"
                android:src="@mipmap/shanchu"/>
        </RelativeLayout>

    </android.support.v7.widget.Toolbar>

    <FrameLayout
        android:id="@+id/fl_layout"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">
    </FrameLayout>

</android.support.v7.widget.LinearLayoutCompat>
```
2.在activity中加载fragment

```
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
```
3.写逻辑（以下是activity全部代码，demo中PlusImageFragmentActivity）

```
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
```
完成。
当然，也可以直接使用模板activity，使用隐私跳转

```
 /**
     * 跳转到展示大图片
     *
     * @param position
     * @param data
     */
    private void viewPluImg(int position, ArrayList<String> data) {
        Intent newIntent = PlusImageActivity.getNewIntent(data, position, true, new AddImageGlideImageLoader());
        startActivityForResult(newIntent, MainConstant.REQUEST_CODE_MAIN);

    }
```
修改数据后在onActivityResult中获取回调数据
```
  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ...
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
```
