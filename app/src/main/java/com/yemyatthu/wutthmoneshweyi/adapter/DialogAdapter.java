package com.yemyatthu.wutthmoneshweyi.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yemyatthu on 4/21/15.
 */
public class DialogAdapter extends PagerAdapter {
  List<String> mPhotoUrls = new ArrayList<>();
  private PhotoViewAttacher mAttacher;
  public DialogAdapter() {

  }

  public void replaceAll(List<String> photoUrls){
     mPhotoUrls = photoUrls;
  }
  @Override public int getCount() {
    return mPhotoUrls.size();
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    ImageView imageView = new ImageView(container.getContext());
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    imageView.setScaleType(ImageView.ScaleType.CENTER);
    imageView.setLayoutParams(layoutParams);
    mAttacher = new PhotoViewAttacher(imageView);
    container.addView(imageView);
    Glide.with(container.getContext())
        .load(mPhotoUrls.get(position))
        .into(imageView);
    return imageView;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((ImageView)object);
  }
}
