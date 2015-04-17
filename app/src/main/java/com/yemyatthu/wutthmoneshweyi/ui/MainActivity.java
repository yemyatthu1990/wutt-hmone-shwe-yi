package com.yemyatthu.wutthmoneshweyi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.yemyatthu.wutthmoneshweyi.R;
import com.yemyatthu.wutthmoneshweyi.WHSY;

/**
 * Created by yemyatthu on 4/15/15.
 */
public class MainActivity extends ActionBarActivity{
  @InjectView(R.id.title_text) TextView mTitleText;
  @InjectView(R.id.photo_image) ImageView mPhotoImage;
  @InjectView(R.id.photo_text) TextView mPhotoText;
  @InjectView(R.id.bio_image) ImageView mBioImage;
  @InjectView(R.id.bio_text) TextView mBioText;
  @InjectView(R.id.follow_image) ImageView mFollowImage;
  @InjectView(R.id.about_image) ImageView mAboutImage;
  @InjectView(R.id.photo_card) CardView mPhotoCard;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    String titleText="<font color=#E91E63>Wut </font> <font color=#43A047>Hmone</font> <br> <font color=#3D5AFE>Shwe </font> <font color=#EF6C00>Yi</font> ";
    mTitleText.setText(Html.fromHtml(titleText));
    mPhotoImage.setColorFilter(Color.WHITE);
    mBioImage.setColorFilter(Color.WHITE);
    mFollowImage.setColorFilter(Color.WHITE);
    mAboutImage.setColorFilter(Color.WHITE);
    ((WHSY)getApplication()).sendTracker("Main Screen");
    setTypeFace(mTitleText);
  }

  void setTypeFace(TextView... textViews){
    for(TextView tv:textViews){
      tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/anabelle_script.ttf"));
    }
  }
  @OnClick(R.id.photo_card)
  public void onClickPhotoCard(){
    Intent i = new Intent(MainActivity.this,PhotosActivity.class);
    startActivity(i);
  }
  @OnClick(R.id.bio_card)
  public void onClickBioCard(){
    Intent i = new Intent(MainActivity.this,BioActivity.class);
    startActivity(i);
  }

  @OnClick(R.id.follow_card)
  public void onClickFollowCard(){
    Intent i = new Intent(MainActivity.this,FollowActivity.class);
    startActivity(i);
  }
  @OnClick(R.id.about_card)
  public void onClickAboutCard(){
    Intent i = new Intent(MainActivity.this,AboutActivity.class);
    startActivity(i);
  }
}
