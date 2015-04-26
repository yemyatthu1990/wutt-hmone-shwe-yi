package com.yemyatthu.wutthmoneshweyi.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.yemyatthu.wutthmoneshweyi.R;

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


}
