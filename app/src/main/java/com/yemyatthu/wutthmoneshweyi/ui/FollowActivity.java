package com.yemyatthu.wutthmoneshweyi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.yemyatthu.wutthmoneshweyi.R;
import com.yemyatthu.wutthmoneshweyi.WHSY;

/**
 * Created by yemyatthu on 4/16/15.
 */
public class FollowActivity extends ActionBarActivity {
  @InjectView(R.id.facebook) ImageView mFacebook;
  @InjectView(R.id.instagram) ImageView mInstagram;
  @InjectView(R.id.gmail) ImageView mGmail;

  private ActionBar mActionBar;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_follow);
    ButterKnife.inject(this);
    mActionBar = getSupportActionBar();
    mActionBar.setDisplayHomeAsUpEnabled(true);
  }

  @OnClick(R.id.facebook)
  public void onClickFb(){
    ((WHSY)getApplication()).sendTracker("Go to fb");
    Intent i = getOpenFacebookIntent(FollowActivity.this);
    startActivity(i);
  }

  @OnClick(R.id.instagram)
  public void onClickInstagram(){
    ((WHSY)getApplication()).sendTracker("Go to insta");
    Intent i = newInstagramProfileIntent(getPackageManager(),
        "https://instagram.com/wutthmoneshweyi/");
    startActivity(i);
  }

  @OnClick(R.id.gmail)
  public void onClickEmail(){
    ((WHSY)getApplication()).sendTracker("Go to email");
    Intent i = new Intent(Intent.ACTION_SEND);
    i.setType("message/rfc822");
    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"welovewutthmonesy@gmail.com"});
    i.putExtra(Intent.EXTRA_SUBJECT, "Hi, Wutt Hmone!");
    try {
      startActivity(Intent.createChooser(i, "Send mail..."));
    } catch (android.content.ActivityNotFoundException ex) {
      Toast.makeText(FollowActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
