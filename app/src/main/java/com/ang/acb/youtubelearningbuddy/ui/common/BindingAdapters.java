package com.ang.acb.youtubelearningbuddy.ui.common;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.utils.GlideApp;
import com.ang.acb.youtubelearningbuddy.utils.UiUtils;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

/**
 * Binding adapters are responsible for making the appropriate framework calls to set values.
 *
 * See: https://developer.android.com/topic/libraries/data-binding/binding-adapters
 * See: https://github.com/googlesamples/android-sunflower
 */
public class BindingAdapters {

    @BindingAdapter("toggleVisibility")
    public static void toggleVisibility(View view, Boolean isVisible) {
        if (isVisible) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }

    @BindingAdapter({"imageUrl"})
    public static void bindImage(ImageView imageView, String imageUrl) {
        // Bind sandwich image
        GlideApp.with(imageView.getContext())
                .load(imageUrl)
                .transform(new CenterCrop(), new RoundedCorners((int)
                           UiUtils.dipToPixels(imageView.getContext(), 4)))
                .placeholder(R.color.colorImagePlaceholder)
                .into(imageView);
    }
}