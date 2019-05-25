package com.simonfong.imageadd.addImage.viewpluimg;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.simonfong.imageadd.R;
import com.simonfong.imageadd.addImage.loader.ImageLoaderInterface;

import java.util.List;


/**
 * 图片浏览的适配器
 * <p>
 * 作者： 周旭 on 2017年7月30日 0030.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> imgList; //图片的数据源
    private ImageLoaderInterface imageLoader;

    public ViewPagerAdapter(Context context, List<String> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    public void setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    //判断当前的View 和 我们想要的Object(值为View) 是否一样;返回 true/false
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    //instantiateItem()：将当前view添加到ViewGroup中，并返回当前View
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = getItemView(R.layout.view_pager_img);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_iv);
//        Glide.with(context).load(imgList.get(position)).into(imageView);
        imageLoader.displayImage(context, imgList.get(position), imageView);
        container.addView(itemView);
        return itemView;
    }

    //destroyItem()：删除当前的View;  
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private View getItemView(int layoutId) {
        View itemView = LayoutInflater.from(this.context).inflate(layoutId, null, false);
        return itemView;
    }
}
