package th.co.paiduay.notificationpaiduay;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by android on 31/8/2559.
 */
public class NotificationService extends Service {

    View mView;
/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("this is from NotificationService");
                handler.postDelayed(this, 500);
            }
        };

        handler.postDelayed(runnable, 500);
        return super.onStartCommand(intent, flags, startId);
    }
*/


    @Override
    public void onCreate() {
        super.onCreate();

        // instance of WindowManager
        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater mInflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate required layout file
        mView = mInflater.inflate(R.layout.notification_layout, null);

        // attach OnClickListener
        mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // you can fire an Intent accordingly - to deal with the click event
                // stop the service - this also removes `mView` from the window
                // because onDestroy() is called - that's where we remove `mView`
              //  stopSelf();
                Intent notificationIntent = new Intent(NotificationService.this, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notificationIntent);

                stopSelf();

            }
        });

        // the LayoutParams for `mView`
        // main attraction here is `TYPE_SYSTEM_ERROR`
        // as you noted above, `TYPE_SYSTEM_ALERT` does not work on the lockscreen
        // `TYPE_SYSTEM_OVERLAY` works very well but is focusable - no click events
        // `TYPE_SYSTEM_ERROR` supports all these requirements
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.RGBA_8888);

        // finally, add the view to window
        mWindowManager.addView(mView, mLayoutParams);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        final Notification builder = new NotificationCompat.Builder(this)
                .setContentText("Cats are calling you!!")
                .setContentTitle("Feed us human!!")
                .setTicker("New chat_layout from your cats")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon)
                .setVisibility(View.VISIBLE)
                .setVibrate(new long[] { 500, 200, 100, 200, 100 })
                .addAction(android.R.drawable.ic_menu_view, "feed them", contentIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(999, builder);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // remove `mView` from the window
        removeNow();
    }

    // Removes `mView` from the window
    public void removeNow() {
        if (mView != null) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(mView);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
