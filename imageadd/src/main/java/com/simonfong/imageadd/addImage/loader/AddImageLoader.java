package com.simonfong.imageadd.addImage.loader;

import android.content.Context;
import android.widget.ImageView;


public abstract class AddImageLoader implements ImageLoaderInterface<ImageView> {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

}
