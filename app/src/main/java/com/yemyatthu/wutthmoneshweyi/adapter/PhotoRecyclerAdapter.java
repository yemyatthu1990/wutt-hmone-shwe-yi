package com.yemyatthu.wutthmoneshweyi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yemyatthu.wutthmoneshweyi.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yemyatthu on 4/16/15.
 */
public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.RecyclerHolder> {
  ClickListener mItemClickListener;
  List<String> mUrls = new ArrayList<>();
  public PhotoRecyclerAdapter(){
  }
  public void setItems(List<String> urls){
    mUrls = urls;
    notifyDataSetChanged();
  }
  public void SetOnItemClickListener(final ClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }
  @Override public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ImageView imageView = (ImageView) inflater.inflate(R.layout.recycler_image, parent, false);
    return new RecyclerHolder(imageView);
  }

  @Override public void onBindViewHolder(RecyclerHolder holder, int position) {
    Glide.with(holder.mImageView.getContext())
        .load(mUrls.get(position))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .crossFade()
        .into(holder.mImageView);
  }

  @Override public int getItemCount() {
    return mUrls.size();
  }

  public interface ClickListener {
    public void onItemClick(View view,int position);
  }

  public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView mImageView;
    public RecyclerHolder(ImageView imageView) {
      super(imageView);
      mImageView = imageView;
      mImageView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if(mItemClickListener!=null){
        mItemClickListener.onItemClick(view,getAdapterPosition());
      }
    }

  }




}
