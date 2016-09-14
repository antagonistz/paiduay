package th.co.paiduay.notificationpaiduay;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by android on 31/8/2559.
 */
public class Application  extends android.app.Application {
    final static String NOTIFY_ACTION = "notifyAction";

    private static BroadcastReceiver receiver;
    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new NotificationReceiver();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(receiver, new IntentFilter(NOTIFY_ACTION));

    //    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Application.NOTIFY_ACTION));
    }
}
