package com.example.mynotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;

import com.example.mynotification.service.NotificationService;
import com.example.mynotification.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mynotification.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    static public int notificationCount = 1;
    static public int notificationId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // set service
        Intent serviceIntent = new Intent(this, MyBackgroundService.class);
        // start service
        startService(serviceIntent);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        Log.d("ttest", "main activity created");
        createNotificationChannel();
    }
    public void replaceFragmentWithBackStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.nav_host_fragment_activity_main, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }
    public void popIfStandingInNestedChildFragment(){
        if(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main) != null){
            getSupportFragmentManager().popBackStackImmediate();
            getSupportFragmentManager().popBackStackImmediate();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        // listen extra value
        String menuFragment = intent.getStringExtra("NotificationMessage");
        Log.d("ttest", "on new intent " + menuFragment);
        if (menuFragment != null) {
            if (menuFragment.equals("Notification Fragment")) {
                navController.popBackStack();
                navController.navigate(R.id.navigation_notifications);
                Log.d("ttest", "go to notification");
            }
        }
        super.onNewIntent(intent);
    }
    public void pushNotification(String notificationTitle,String notificationText) {
        NotificationService notiInstance = NotificationService.getInstance();
        notiInstance.pushNotification(this, getString(R.string.channel_id),notificationTitle, notificationText, notificationId, notificationCount);
        MainActivity.notificationId += 1;
        MainActivity.notificationCount +=1;
    }

    public  void createNotificationChannel() {
        NotificationService notiInstance = NotificationService.getInstance();
        notiInstance.setNotification(this, getString(R.string.channel_id), getString(R.string.channel_name), getString(R.string.channel_description));
    }

}