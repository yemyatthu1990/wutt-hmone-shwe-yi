package com.yemyatthu.wutthmoneshweyi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.api.services.youtube.model.SearchResult;
import com.yemyatthu.wutthmoneshweyi.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yemyatthu on 5/1/15.
 */
public class VideoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  VideoRecyclerAdapter.ClickListener mItemClickListener;
  List<SearchResult> mSearchListResponses;
  Context mContext;
  public final int ITEM_VIEW = 100;
  public final int FOOTER_VIEW = 101;
  public VideoRecyclerAdapter(){
    mSearchListResponses = new ArrayList<>();
  }
  public void addAll(List<SearchResult> searchListResponses){
    mSearchListResponses = searchListResponses;
    notifyDataSetChanged();
  }
  public void appendAll(List<SearchResult> responses){
    mSearchListResponses.addAll(responses);
    notifyDataSetChanged();
  }

  public List<SearchResult> getAll(){
    return mSearchListResponses;
  }
  public interface ClickListener {
    public void onItemClick(View view,int position);
  }

  public void SetOnItemClickListener(final ClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }
  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

    mContext = viewGroup.getContext();
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    if(viewType == ITEM_VIEW){
    View view = inflater.inflate(R.layout.video_item, viewGroup, false);
    return new RecyclerHolder(view);}
    else{
      View view = inflater.inflate(R.layout.progress_layout,viewGroup,false);
      return new ProgressHolder(view);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder recyclerHolder, int i) {
    if(recyclerHolder instanceof RecyclerHolder){
      if(mSearchListResponses.size()==0){
        return;
      }else {
        TextView titleTv = ((RecyclerHolder) recyclerHolder).mTitle;

        ImageView mThumbnailTv = ((RecyclerHolder) recyclerHolder).mImageView;
        SearchResult searchResult = mSearchListResponses.get(i);
        if (searchResult.getId().getKind().equals("youtube#video")) {
          titleTv.setText(searchResult.getSnippet().getTitle());
          Glide.with(mContext)
              .load(searchResult.getSnippet().getThumbnails().getMedium().getUrl())
              .into(mThumbnailTv);
        }
      }
    }else{
      if(mSearchListResponses.size()==0){
        ((ProgressHolder)recyclerHolder).mProgressBar.setVisibility(View.GONE);
      }else{
        ((ProgressHolder)recyclerHolder).mProgressBar.setVisibility(View.VISIBLE);
      }
      ((ProgressHolder) recyclerHolder).mProgressBar.setIndeterminate(true);
    }

  }

  @Override public int getItemViewType(int position) {
    return position<mSearchListResponses.size()? ITEM_VIEW:FOOTER_VIEW;
  }

  @Override public int getItemCount() {
    return mSearchListResponses.size()+1;
  }

  public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView mImageView;
    public TextView mTitle;

    public RecyclerHolder(View view) {
      super(view);
      mImageView = (ImageView) view.findViewById(R.id.thumbnail_view);
      mTitle = (TextView) view.findViewById(R.id.title);
      view.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (mItemClickListener != null) {
        mItemClickListener.onItemClick(view, getAdapterPosition());
      }
    }
  }

  public class ProgressHolder extends RecyclerView.ViewHolder{
    public ProgressBar mProgressBar;
    public ProgressHolder(View itemView) {
      super(itemView);
      mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
    }
  }
}

