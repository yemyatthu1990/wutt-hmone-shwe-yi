package com.yemyatthu.wutthmoneshweyi.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.yemyatthu.wutthmoneshweyi.BuildConfig;
import com.yemyatthu.wutthmoneshweyi.R;
import com.yemyatthu.wutthmoneshweyi.adapter.VideoRecyclerAdapter;
import com.yemyatthu.wutthmoneshweyi.util.EndlessRecyclerOnScrollListener;
import com.yemyatthu.wutthmoneshweyi.util.JsonService;
import com.yemyatthu.wutthmoneshweyi.util.NetUtils;
import com.yemyatthu.wutthmoneshweyi.util.TinyDB;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yemyatthu on 4/16/15.
 */
public class VideoActivity extends AppCompatActivity {

  @InjectView(R.id.video_recycler_view) RecyclerView mVideoRecyclerView;
  @InjectView(R.id.progress_bar) ProgressBar mProgressBar;
  private static final String CACHE = "vd_caches";
  private ActionBar mActionBar;
  private YouTube mYouTube;
  private GridLayoutManager mLayoutManager;
  private VideoRecyclerAdapter mVideoRecyclerAdapter;
  private YouTube.Search.List search;
  private List<SearchResult> listResponses;
  private SearchListResponse mSearchListResponse;
  private boolean firstTimeLoad;
  private TinyDB mTinyDB;
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video);
    ButterKnife.inject(this);
    mActionBar = getSupportActionBar();
    mActionBar.setDisplayHomeAsUpEnabled(true);
    firstTimeLoad = true;
    mTinyDB = new TinyDB(getApplicationContext());
    mLayoutManager = new GridLayoutManager(VideoActivity.this, 2);

    mVideoRecyclerView.setLayoutManager(mLayoutManager);

    mVideoRecyclerView.setHasFixedSize(true);
    mVideoRecyclerAdapter = new VideoRecyclerAdapter();
    mVideoRecyclerView.setAdapter(mVideoRecyclerAdapter);
    if(mTinyDB.getString(CACHE)!=null && mTinyDB.getString(CACHE).length()>0){
    new ReadCacheAsync().execute();
    }
    mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        switch(mVideoRecyclerAdapter.getItemViewType(position)){
          case 100:
            return 1;
          case 101:
            return 2; //number of columns of the grid
          default:
            return -1;
        }
      }
    });
    mYouTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
      @Override public void initialize(HttpRequest request) throws IOException {

      }
    }).setApplicationName("wutt_hmone_shwe_yi").build();
    if(NetUtils.isConnected(this)) {
      try {
        search = mYouTube.search().list("id,snippet");
        search.setKey(BuildConfig.YOUTUBE_API_KEY);
        search.setQ("Wutt Hmone Shwe Yi");
        search.setType("video");
        search.setMaxResults((long) 24);
        search.setFields(
            "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/medium/url),nextPageToken,prevPageToken");
        new SearchAsyncTask().execute(search);
        mVideoRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
          @Override public void onLoadMore(int current_page) {
            if(NetUtils.isConnected(VideoActivity.this)) {
              if (mSearchListResponse != null &&
                  mSearchListResponse.getNextPageToken() != null &&
                  mSearchListResponse.getNextPageToken().length() > 0) {
                search.setPageToken(mSearchListResponse.getNextPageToken());
                new SearchAsyncTask().execute(search);
              }
            }else{
              Toast.makeText(VideoActivity.this, "No internet connection.", Toast.LENGTH_LONG).show();
            }
          }
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }else{
      Toast.makeText(this, "No internet connection.", Toast.LENGTH_LONG).show();
    }
  }
  class SearchAsyncTask extends AsyncTask<YouTube.Search.List,Void,SearchListResponse>{

    @Override protected SearchListResponse doInBackground(YouTube.Search.List... lists) {
      try {
        return lists[0].execute();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override protected void onPostExecute(SearchListResponse searchListResponse) {
      super.onPostExecute(searchListResponse);
      listResponses = searchListResponse.getItems();
      mSearchListResponse = searchListResponse;

      if(listResponses.size()>0){
        if(mProgressBar.isShown()){
          mProgressBar.setVisibility(View.GONE);
        }
        final VideoRecyclerAdapter currentAdapter = ((VideoRecyclerAdapter) mVideoRecyclerView.getAdapter());
        if(firstTimeLoad){
          currentAdapter.addAll(listResponses);
          new Handler().post(new Runnable() {
            @Override public void run() {
              String saveResults = JsonService.convertToJson(listResponses);
              mTinyDB.putString(CACHE,saveResults);
            }
          });
          firstTimeLoad = false;
        }else{
          currentAdapter.appendAll(listResponses);
        }
        setOnItemClickOnAdapter();
      }
    }
  }
  private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

    System.out.println("\n=============================================================");
    System.out.println(
        "   First " + 25 + " videos for search on \"" + query + "\".");
    System.out.println("=============================================================\n");

    if (!iteratorSearchResults.hasNext()) {
      System.out.println(" There aren't any results for your query.");
    }

    while (iteratorSearchResults.hasNext()) {

      SearchResult singleVideo = iteratorSearchResults.next();
      ResourceId rId = singleVideo.getId();

      // Confirm that the result represents a video. Otherwise, the
      // item will not contain a video ID.
      if (rId.getKind().equals("youtube#video")) {
        Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

        System.out.println(" Video Id" + rId.getVideoId());
        System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
        System.out.println(" Thumbnail: " + thumbnail.getUrl());
        System.out.println("\n-------------------------------------------------------------\n");
      }
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void setOnItemClickOnAdapter(){
    mVideoRecyclerAdapter.SetOnItemClickListener(new VideoRecyclerAdapter.ClickListener() {
      @Override public void onItemClick(View view, int position) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(VideoActivity.this,
            BuildConfig.YOUTUBE_API_KEY, listResponses.get(position).getId().getVideoId());
        startActivity(intent);
      }
    });
  }

  class ReadCacheAsync extends AsyncTask<Void,Void,List<SearchResult>>{

    @Override protected List<SearchResult> doInBackground(Void... voids) {
      return JsonService.convertToJava(mTinyDB.getString(CACHE));
    }

    @Override protected void onPostExecute(List<SearchResult> s) {
      super.onPostExecute(s);
      if(s.size()>0){
        mVideoRecyclerAdapter.addAll(s);
        mProgressBar.setVisibility(View.GONE);
      }
    }
  }
}
