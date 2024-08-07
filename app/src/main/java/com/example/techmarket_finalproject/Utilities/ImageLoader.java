package com.example.techmarket_finalproject.Utilities;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.techmarket_finalproject.R;

public class ImageLoader {

    public static void loadImage(ImageView imageView, Object imageResource) {
        if (imageResource instanceof String) {
            Glide.with(imageView.getContext())
                    .load((String) imageResource)
                    .placeholder(R.drawable.default_image)
                    .into(imageView);
        } else if (imageResource instanceof Integer) {
            Glide.with(imageView.getContext())
                    .load((Integer) imageResource)
                    .placeholder(R.drawable.default_image)
                    .into(imageView);
        }
    }
}