package com.example.weatherapp.binding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class PicassoBindingAdapters {
    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }
}
