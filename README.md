先上图:
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181112173232800.gif)![在这里插入图片描述](https://img-blog.csdnimg.cn/20181112173247498.gif)

https://github.com/simonFong/CommentDemo
想用的直接到github下载就可以了，星星控件和添加图片的控件在imageadd的lib里

使用方法：
1.下载lib，导入自己的工程
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
        <!--图片圆角-->
        <attr name="rounded_corner" format="dimension"/>

    </declare-styleable>
  ```
  简单使用：


```
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

