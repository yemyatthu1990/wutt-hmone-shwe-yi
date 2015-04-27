package com.yemyatthu.wutthmoneshweyi.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.getbase.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
  private FloatingActionButton mFab;
  private FrameLayout mCurrentView;
  public DialogAdapter(FloatingActionButton fb) {
    mFab = fb;
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

  @Override public Object instantiateItem(final ViewGroup container, final int position) {
    //FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
    //layoutParams2.gravity= Gravity.BOTTOM|Gravity.END;
    //int dp = dpToPx(16,container.getContext());
    //layoutParams2.setMargins(dp,dp,dp,dp);
    //FloatingActionButton fb = (FloatingActionButton) LayoutInflater.from(container.getContext()).inflate(R.layout.floating_button,null);
    //fb.setLayoutParams(layoutParams2);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    final FrameLayout frameLayout = new FrameLayout(container.getContext());
    frameLayout.setLayoutParams(layoutParams);
    final ImageView imageView = new ImageView(container.getContext());
    FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
    layoutParams1.gravity= Gravity.CENTER;
    imageView.setLayoutParams(layoutParams1);
    Glide.with(container.getContext())
        .load(mPhotoUrls.get(position))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView);
    mAttacher = new PhotoViewAttacher(imageView);
    mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
      @Override public void onPhotoTap(View view, float x, float y) {
        if (mFab.isShown()) {
          mFab.animate()
              .translationY(mFab.getHeight())
              .setDuration(300)
              .alpha(0.0f)
              .setListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                  super.onAnimationEnd(animation);
                  mFab.setVisibility(View.INVISIBLE);
                }
              });
        } else {
          mFab.animate()
              .translationY(0)
              .setDuration(300)
              .alpha(1.0f)
              .setListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                  super.onAnimationEnd(animation);
                  mFab.setVisibility(View.VISIBLE);
                }
              });
        }
      }
    });
    mAttacher.setOnDoubleTapListener(new OnDoubleTapPhotoView(mAttacher));
    mAttacher.setScaleType(ImageView.ScaleType.CENTER);
    mAttacher.update();
    frameLayout.addView(imageView,0);
    //frameLayout.addView(fb);
    container.addView(frameLayout);
    frameLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (mFab.isShown()) {
          mFab.animate()
              .translationY(mFab.getHeight())
              .setDuration(300)
              .alpha(0.0f)
              .setListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                  super.onAnimationEnd(animation);
                  mFab.setVisibility(View.INVISIBLE);
                }
              });
        } else {
          mFab.animate()
              .translationY(0)
              .setDuration(300)
              .alpha(1.0f)
              .setListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                  super.onAnimationEnd(animation);
                  mFab.setVisibility(View.VISIBLE);
                }
              });
        }
      }
    });
    mFab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Uri bmpUri = getLocalBitmapUri();
        if (bmpUri != null) {
          // Construct a ShareIntent with link to image
          Intent shareIntent = new Intent();
          shareIntent.setAction(Intent.ACTION_SEND);
          shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
          shareIntent.setType("image/*");
          // Launch sharing dialog for image
          container.getContext().startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
          // ...sharing failed, handle error
          Log.e("Share Activity", "share failed");
        }
      }
    });

    return frameLayout;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((FrameLayout) object);
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
      mFab.animate().translationY(mFab.getHeight())
          .alpha(0.0f)
          .setDuration(300)
          .setListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
              super.onAnimationEnd(animation);
              mFab.setVisibility(View.INVISIBLE);
            }
          });
      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT);
      mPhotoViewAttacher.getImageView().setLayoutParams(layoutParams);
      return super.onDoubleTap(ev);
    }
  }
  public int dpToPx(int dp,Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return px;
  }

  @Override public void setPrimaryItem(ViewGroup container, int position, Object object) {
    super.setPrimaryItem(container, position, object);
    mCurrentView = (FrameLayout) object;
  }

  public Uri getLocalBitmapUri() {

    ImageView imageView = (ImageView) mCurrentView.getChildAt(0);
    GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable) imageView.getDrawable();
    Bitmap bmp = bitmapDrawable.getBitmap();
    // Store image to default external storage directory
    Uri bmpUri = null;
    try {
      File file =  new File(
          Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "wutthmoneshweyi" + System.currentTimeMillis() + ".png");
      file.getParentFile().mkdirs();
      FileOutputStream out = new FileOutputStream(file);
      bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
      out.close();
      bmpUri = Uri.fromFile(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bmpUri;
  }
}
