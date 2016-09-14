package th.co.paiduay.notificationpaiduay;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by android on 31/8/2559.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentService = new Intent(context, NotificationService.class);
                context.startService(intentService);
            }
        }, 10000);




    }
}
