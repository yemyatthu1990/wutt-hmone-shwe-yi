package com.yemyatthu.wutthmoneshweyi.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.yemyatthu.wutthmoneshweyi.R;
import com.yemyatthu.wutthmoneshweyi.WHSY;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yemyatthu on 4/16/15.
 */
public class BioActivity extends ActionBarActivity {
  @InjectView(R.id.native_name) TextView mNativeName;
  @InjectView(R.id.profile_image) ImageView mProfileImage;
  @InjectView(R.id.wiki_card) CardView mWikiCard;
  private ActionBar mActionBar;
  private PhotoViewAttacher mAttacher;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bio);
    ButterKnife.inject(this);
    mActionBar = getSupportActionBar();
    mActionBar.setDisplayHomeAsUpEnabled(true);
    setTypeFace(mNativeName);
  }

  @OnClick(R.id.wiki_card)
  public void onClickWikiCard(){
    ((WHSY)getApplication()).sendTracker("go to Wiki");
    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://en.wikipedia.org/wiki/Wut_Hmone_Shwe_Yee"));
    Intent.createChooser(i,"Open With");
    startActivity(i);
  }
  @OnClick(R.id.profile_image)
  public  void onClickProfile(){
    Dialog dialog = new Dialog(BioActivity.this, R.style.ImageDialogAnimation);
    ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.dialog_image, null);
    dialog.setContentView(imageView);

    dialog.show();
    Glide.with(BioActivity.this)
        .load(R.drawable.wutt_hmone_shwe_yi)
        .crossFade().into(imageView);

    mAttacher = new PhotoViewAttacher(imageView);
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

  void setTypeFace(TextView... textViews){
    for(TextView tv:textViews){
      tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pyidaungsu-1.2.ttf"));
    }
  }
}
