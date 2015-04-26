package com.yemyatthu.wutthmoneshweyi.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.DefaultOnDoubleTapListener;
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
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    FrameLayout frameLayout = new FrameLayout(container.getContext());
    frameLayout.setLayoutParams(layoutParams);
    ImageView imageView = new ImageView(container.getContext());
    FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
    layoutParams1.gravity= Gravity.CENTER;
    imageView.setLayoutParams(layoutParams1);
    Glide.with(container.getContext())
        .load(mPhotoUrls.get(position)).into(imageView);
    mAttacher = new PhotoViewAttacher(imageView);
    mAttacher.setOnDoubleTapListener(new OnDoubleTapPhotoView(mAttacher));
    mAttacher.setScaleType(ImageView.ScaleType.CENTER);
    mAttacher.update();
    frameLayout.addView(imageView);
    container.addView(frameLayout);
    return frameLayout;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((FrameLayout
        ) object);
  }
  class OnDoubleTapPhotoView extends DefaultOnDoubleTapListener{
    PhotoViewAttacher mPhotoViewAttacher;
    /**
     * Default constructor
     *
     * @param photoViewAttacher PhotoViewAttacher to bind to
     */
    public OnDoubleTapPhotoView(PhotoViewAttacher photoViewAttacher) {
      super(photoViewAttacher);
      mPhotoViewAttacher = photoViewAttacher;
    }

    @Override public boolean onDoubleTap(MotionEvent ev) {
      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT);
      mPhotoViewAttacher.getImageView().setLayoutParams(layoutParams);
      return super.onDoubleTap(ev);
    }
  }
}
