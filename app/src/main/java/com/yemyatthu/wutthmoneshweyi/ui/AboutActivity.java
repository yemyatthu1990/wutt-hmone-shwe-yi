package com.yemyatthu.wutthmoneshweyi.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.yemyatthu.wutthmoneshweyi.BuildConfig;
import com.yemyatthu.wutthmoneshweyi.R;

/**
 * Created by yemyatthu on 4/16/15.
 */
public class AboutActivity extends AppCompatActivity{
  @InjectView(R.id.about_text) TextView mAboutText;
  @InjectView(R.id.about_web) WebView mAboutWeb;
  @InjectView(R.id.version_text) TextView mVersionText;
  private ActionBar mActionBar;
  private boolean expand = false;
  private String licenceText;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);
    ButterKnife.inject(this);
    mActionBar = getSupportActionBar();
    mActionBar.setDisplayHomeAsUpEnabled(true);
    String aboutText = "<h2><strong>Disclaimer</strong></h2>\n"
        + "\n"
        + "<p>\t\t\t\tThis is a fan-made <strong>unofficial</strong> android app for&nbsp;Wutt Hmone Shwe Yi. All the photos, informations and datas in this app are collected over the internet, and copyrighted to their respective owners. If you think I&#39;m violating your copyright, please contact me at thu.yemyat@gmail.com. I will remove the content asap.</p>"
        + "\n"
        + "<p>Icons used in this app are from www.flaticon.com. Gmail, Facebook and Instagram Logos are taken&nbsp;from the respective websites. &nbsp;</p>";

    mAboutText.setText(Html.fromHtml(aboutText));
    mAboutWeb.loadUrl("file:///android_asset/about.html");
    mVersionText.setText("Version: " + BuildConfig.VERSION_NAME);
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
}
