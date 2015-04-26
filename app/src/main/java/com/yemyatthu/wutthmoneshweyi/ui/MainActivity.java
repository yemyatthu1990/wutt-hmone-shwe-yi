package com.yemyatthu.wutthmoneshweyi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.yemyatthu.wutthmoneshweyi.R;
import com.yemyatthu.wutthmoneshweyi.WHSY;

/**
 * Created by yemyatthu on 4/15/15.
 */
public class MainActivity extends ActionBarActivity {
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
    String titleText =
        "<font color=#E91E63>Wut </font> <font color=#43A047>Hmone</font> <br> <font color=#3D5AFE>Shwe </font> <font color=#EF6C00>Yi</font> ";
    mTitleText.setText(Html.fromHtml(titleText));
    mPhotoImage.setColorFilter(Color.WHITE);
    mBioImage.setColorFilter(Color.WHITE);
    mFollowImage.setColorFilter(Color.WHITE);
    mAboutImage.setColorFilter(Color.WHITE);
    ((WHSY) getApplication()).sendTracker("Main Screen");
    setTypeFace(mTitleText);
  }

  void setTypeFace(TextView... textViews) {
    for (TextView tv : textViews) {
      tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/anabelle_script.ttf"));
    }
  }

  @OnClick(R.id.photo_card) public void onClickPhotoCard() {
    Intent i = new Intent(MainActivity.this, PhotosActivity.class);
    startActivity(i);
  }

  @OnClick(R.id.bio_card) public void onClickBioCard() {
    Intent i = new Intent(MainActivity.this, BioActivity.class);
    startActivity(i);
  }

  @OnClick(R.id.follow_card) public void onClickFollowCard() {
    Intent i = new Intent(MainActivity.this, FollowActivity.class);
    startActivity(i);
  }

  @OnClick(R.id.about_card) public void onClickAboutCard() {
    Intent i = new Intent(MainActivity.this, AboutActivity.class);
    startActivity(i);
  }
  @OnClick(R.id.fb_logo)
  public void onClickFb(){
    ((WHSY)getApplication()).sendTracker("Go to fb");
    Intent i = getOpenFacebookIntent(MainActivity.this);
    startActivity(i);
  }

  @OnClick(R.id.insta_logo)
  public void onClickInstagram(){
    ((WHSY)getApplication()).sendTracker("Go to insta");
    Intent i = newInstagramProfileIntent(getPackageManager(),
        "https://instagram.com/wutthmoneshweyi/");
    startActivity(i);
  }

  @OnClick(R.id.email)
  public void onClickEmail(){
    ((WHSY)getApplication()).sendTracker("Go to email");
    Intent i = new Intent(Intent.ACTION_SEND);
    i.setType("message/rfc822");
    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"welovewutthmonesy@gmail.com"});
    i.putExtra(Intent.EXTRA_SUBJECT, "Hi, Wutt Hmone!");
    try {
      startActivity(Intent.createChooser(i, "Send mail..."));
    } catch (android.content.ActivityNotFoundException ex) {
      Toast.makeText(MainActivity.this, "There are no email clients installed.",
          Toast.LENGTH_SHORT).show();
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

  public Intent getOpenFacebookIntent(Context context) {

    try {
      context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
      return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/240382364361"));
    } catch (Exception e) {
      return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/wutthmoneshweyi"));
    }
  }

  public Intent newInstagramProfileIntent(PackageManager pm, String url) {
    final Intent intent = new Intent(Intent.ACTION_VIEW);
    try {
      if (pm.getPackageInfo("com.instagram.android", 0) != null) {
        if (url.endsWith("/")) {
          url = url.substring(0, url.length() - 1);
        }
        final String username = url.substring(url.lastIndexOf("/") + 1);
        // http://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
        intent.setData(Uri.parse("http://instagram.com/_u/" + username));
        intent.setPackage("com.instagram.android");
        return intent;
      }
    } catch (PackageManager.NameNotFoundException ignored) {
    }
    intent.setData(Uri.parse(url));
    return intent;
  }
}
