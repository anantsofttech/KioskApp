package com.anantkiosk.kioskapp.Home.slider;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.anantkiosk.kioskapp.Model.AdvSign;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.demono.adapter.InfinitePagerAdapter;

import java.util.List;

public class AdvertiseImageSliderAdapter extends InfinitePagerAdapter {

    Context context;

    List<AdvSign> imageDetalList;

    public AdvertiseImageSliderAdapter(Context context, List<AdvSign> imageDetalList) {
        this.context = context;
        this.imageDetalList = imageDetalList;

    }

    @Override
    public int getItemCount() {
        if (imageDetalList == null) {
            return 0;
        } else if (this.imageDetalList.size() > 0) {
            return this.imageDetalList.size();
        } else {
            return 0;
        }

    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup container) {
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = layoutInflater.inflate(R.layout.rawpager, container, false);
            AdvSign model = imageDetalList.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imgbanner);
            String url = model.getUrl();
            if(model.getId().equalsIgnoreCase("-1")){
                if(url.contains("dummy1")){
                    Glide.with(context)
                            .asBitmap()
                            .load(R.drawable.adv1)
                            .into(new BitmapImageViewTarget(imageView) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCornerRadius(24);
                                    imageView.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }else if(url.contains("dummy2")){
                    Glide.with(context)
                            .asBitmap()
                            .load(R.drawable.adv2)
                            .into(new BitmapImageViewTarget(imageView) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCornerRadius(24);
                                    imageView.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }else if(url.contains("dummy3")){
                    Glide.with(context)
                            .asBitmap()
                            .load(R.drawable.adv3)
                            .into(new BitmapImageViewTarget(imageView) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCornerRadius(24);
                                    imageView.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }else if(url.contains("no image")){
                    Glide.with(context)
                            .asBitmap()
                            .load(R.drawable.noproductimageavailable)
                            .into(new BitmapImageViewTarget(imageView) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCornerRadius(24);
                                    imageView.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }
            }else {

                Log.e("", "getItemView: 123" + url );
                try {
                    RequestOptions requestOptions = new RequestOptions()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .apply(new RequestOptions().override(800, 1200))
                            .apply(new RequestOptions().centerCrop());

                    Glide.with(context)
                            .load(url)
                            .apply(requestOptions)
                            .into(imageView);

                } catch (Exception e) {
                }
            }
            UtilsGlobal.dismissProgressBar();
            return itemView;
        }
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    public void setData(List<AdvSign> imageDetalList) {
        this.imageDetalList = imageDetalList;
        notifyDataSetChanged();
    }
}
