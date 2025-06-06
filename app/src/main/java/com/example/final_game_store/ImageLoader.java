package com.example.final_game_store;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoader {
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_placeholder) // изображение во время загрузки
                        .error(R.drawable.ic_error_image))      // изображение при ошибке
                .into(imageView);
    }
}
