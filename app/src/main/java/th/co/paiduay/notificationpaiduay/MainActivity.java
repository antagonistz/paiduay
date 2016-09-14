package th.co.paiduay.notificationpaiduay;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity {

    ScrollView scrollView;
    Bitmap headerImage, driverImage;
    int[] mainImagesName = {R.drawable.landing1, R.drawable.landing2};
    int[] imagesName = {R.drawable.img1, R.drawable.img2 , R.drawable.img3,
            R.drawable.img4, R.drawable.img5};
    int[] planImagesName = {R.drawable.plan, R.drawable.plan1 , R.drawable.plan2};
    Bitmap chatImage, mainImage,  arrivalBitmap, planImage;
    ImageView chatView, headerView, mainView, loadingView, driverImageView;
    ImageButton sendButton;
    TextInputEditText editText;
    ViewGroup loadingMessagesBar;
    public static int currentPosition = 0;
    public static int countOnMainClicked = 0, countOnPlanClicked = 0;
    public static Boolean isAccepted = true, isPlan = false;

    Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadingMessagesBar.setVisibility(View.GONE);
            chatImage.recycle();
            chatImage = BitmapFactory.decodeResource(getResources(), imagesName[((++currentPosition) % imagesName.length)]);
            chatView.setImageBitmap(chatImage);
            chatView.requestFocus();
        }
    };

    void bindChatViews(){
        scrollView = ((ScrollView) findViewById(R.id.middle));
        chatView = ((ImageView)findViewById(R.id.pic));
        sendButton = ((ImageButton)findViewById(R.id.send));
        headerView = ((ImageView) findViewById(R.id.header));
        editText = (TextInputEditText) findViewById(R.id.et);
        loadingMessagesBar = (RelativeLayout) findViewById(R.id.loadingLayout);
        loadingView = (ImageView) findViewById(R.id.loadingProgress);
        driverImageView = (ImageView) findViewById(R.id.yourFriendProfilePicture);
    }

    void initChatInstance(){
        chatImage = BitmapFactory.decodeResource(getResources(), imagesName[0]);
        headerImage = BitmapFactory.decodeResource(getResources(), R.drawable.header);
        chatView.setImageBitmap(chatImage);
        headerView.setImageBitmap(headerImage);
        scrollView.setSmoothScrollingEnabled(false);
        Glide.with(this).load(R.drawable.loading)
                .asGif().crossFade().override(64, 64).into(loadingView);
        Glide.with(this).load(R.drawable.driver_icon_loading).bitmapTransform(new CropCircleTransformation(this)).into(driverImageView);

    }

    void initChatListener(){

        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                },200);
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                },200);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = new AlphaAnimation(0.3f,1.0f);
                animation.setDuration(300);
                view.startAnimation(animation);
                editText.setText("");
                editText.clearFocus();
                chatImage.recycle();
                chatImage = BitmapFactory.decodeResource(getResources(), imagesName[((++currentPosition) % imagesName.length)]);
                chatView.setImageBitmap(chatImage);
                chatView.requestFocus();


                loadingMessagesBar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingMessagesBar.setVisibility(View.VISIBLE);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        }   );
                        mHandler.removeCallbacks(runnable);
                        mHandler.postDelayed(runnable, 5000);
                    }
                },2000);

                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                },200);

            }
        });

    }

    Notification notification;
    void startNotification(){
        //real notifications
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(isAccepted ? "Maleewan accepted your ride request for tomorrow(23-08-2016) at 7:30 am."
                : "Meleewan will pick you up in 15 minutes." );


        if(Build.VERSION.SDK_INT >= 21) {
            notification = new Notification.Builder(this)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setLargeIcon(driverImage)
                    .setSmallIcon(R.drawable.icon_small)
                    .setCategory(Notification.CATEGORY_EVENT)
                    .setContentTitle("Paiduay")
                    .setContentText(stringBuilder.toString())
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[] { 500, 200, 100, 200, 100 })
                    .build();
        }else {
            notification = new Notification.Builder(this)
                    .setContentTitle("Paiduay")
                    .setContentText(stringBuilder.toString())
                    .setSmallIcon(R.drawable.icon_small)
                    .setLargeIcon(driverImage)
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[] { 500, 200, 100, 200, 100 })
                    .build();
        }


        Handler mHandler = new Handler();
        mHandler.removeCallbacks(runnableNotification);
        mHandler.postDelayed(runnableNotification, 20000);
    }
    Runnable runnableNotification = new Runnable() {
        @Override
        public void run() {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1234, notification);

            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
            wakeLock.acquire();
        }
    };
    ViewGroup arrival, accepted, plan;
    void bindMainViews(){
        mainView = (ImageView) findViewById(R.id.landingPage);
        arrivalImageView = (ImageView)findViewById(R.id.arrivalImg);
        planImageView = (ImageView) findViewById(R.id.planImg);
        arrival = (RelativeLayout) findViewById(R.id.arrivalLayout);
        accepted = (RelativeLayout) findViewById(R.id.acceptedLayout);
        plan = (RelativeLayout) findViewById(R.id.planLayout);
    }

    void initMainListener(){
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(countOnMainClicked >= 1){
                    setContentView(R.layout.chat_layout);
                    bindChatViews();
                    initChatInstance();
                    initChatListener();
                }else{
                    mainImage = BitmapFactory.decodeResource(getResources(), mainImagesName[1]);
                    mainView.setImageBitmap(mainImage);
                    countOnMainClicked++;

        /*            Glide.with(MainActivity.this).load(mainImagesName[1]).centerCrop().into(mainView);
                    countOnMainClicked++;*/
                }
            }
        });

        planImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planImage = BitmapFactory.decodeResource(getResources(), planImagesName[((++countOnPlanClicked) % planImagesName.length)]);
                planImageView.setImageBitmap(planImage);
                if(countOnPlanClicked >= 3) countOnPlanClicked = 0;


            }
        });
    }

    void initMainInstance(){
        driverImage = BitmapFactory.decodeResource(getResources(), R.drawable.driver_icon);
/*        mainImage = BitmapFactory.decodeResource(getResources(), mainImagesName[0]);
        mainView.setImageBitmap(mainImage);*/
        Glide.with(MainActivity.this).load(mainImagesName[0]).centerCrop().into(mainView);

        arrivalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrival);
        arrivalImageView.setImageBitmap(arrivalBitmap);

        Glide.with(MainActivity.this).load(R.drawable.plan).centerCrop().into(planImageView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPlan) {
            plan.setVisibility(View.VISIBLE);
            accepted.setVisibility(View.GONE);
            arrival.setVisibility(View.GONE);

/*            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            if(actionBar!=null)
            actionBar.hide();*/
        }else{
            if (isAccepted) {
                accepted.setVisibility(View.VISIBLE);
                arrival.setVisibility(View.GONE);
                plan.setVisibility(View.GONE);
            } else {
                plan.setVisibility(View.GONE);
                accepted.setVisibility(View.GONE);
                arrival.setVisibility(View.VISIBLE);
            }
        }
    }

    ImageView arrivalImageView, planImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*            setContentView(R.layout.activity_main);
            bindMainViews();
            initMainListener();
            initMainInstance();*/

        setContentView(R.layout.mainlayout);
        bindMainViews();
        initMainListener();
        initMainInstance();


        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.selectplan) {
                    isPlan = !isPlan;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(isPlan ? "plan activated!" : "plan not activated");
                    Snackbar.make(mainView, stringBuilder.toString(), 5000).show();
                }else{
                    isAccepted = !isAccepted;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(isAccepted ? "accepted notify activated!" : "arrival notify activated");
                    Snackbar.make(mainView, stringBuilder.toString(), 5000).show();

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });





    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isPlan)
        startNotification();
    }
}
