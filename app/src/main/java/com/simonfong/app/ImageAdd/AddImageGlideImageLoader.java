package com.simonfong.app.ImageAdd;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.simonfong.imageadd.addImage.loader.AddImageLoader;

/**
 * Created  on  2019/05/25.
 * interface by
 *
 * @author $USER_NAME
 */
public class AddImageGlideImageLoader extends AddImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transforms(new CenterCrop(), new RoundedCorners(10)))
                .into(imageView);
    }
}
