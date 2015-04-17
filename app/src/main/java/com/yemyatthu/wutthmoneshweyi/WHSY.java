package com.yemyatthu.wutthmoneshweyi;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import java.util.HashMap;

/**
 * Created by yemyatthu on 4/17/15.
 */
public class WHSY extends Application {
  @Override public void onCreate() {
    super.onCreate();
  }

  /**
   * Enum used to identify the tracker that needs to be used for tracking.
   *
   * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
   * storing them all in Application object helps ensure that they are created only once per
   * application instance.
   */
  public enum TrackerName {
    APP_TRACKER, // Tracker used only in this app.
    GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
    ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
  }

  HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
  synchronized Tracker getTracker(TrackerName trackerId) {
    if (!mTrackers.containsKey(trackerId)) {

      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.app_tracker)
          : null;
      mTrackers.put(trackerId, t);

    }
    return mTrackers.get(trackerId);
  }

  public void sendTracker(String screenName){
    // Get tracker.
    Tracker t = getTracker(
        TrackerName.APP_TRACKER);

    // Set screen name.
    t.setScreenName(screenName);

    // Send a screen view.
    t.send(new HitBuilders.ScreenViewBuilder().build());
  }
}
