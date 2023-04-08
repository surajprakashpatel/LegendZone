package com.srsoft.legendzone.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.srsoft.legendzone.R;
import com.srsoft.legendzone.ui.common.BaseActivity;

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initialization();
    }

   private void initialization(){

       FirebaseApp.initializeApp(this);
       FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
       firebaseAppCheck.installAppCheckProviderFactory(
               PlayIntegrityAppCheckProviderFactory.getInstance());

       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               if(user!=null){
                   if(user.getDisplayName().matches("")){
                       Intent intent = new Intent(SplashActivity.this,UpdateProfileActivity.class);
                       startActivity(intent);
                       finish();
                   }else{
                       Toast.makeText(SplashActivity.this, user.getDisplayName()+"", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(SplashActivity.this,DashboardActivity.class);
                       startActivity(intent);
                       finish();
                   }
               }else{
                   Intent intent = new Intent(SplashActivity.this,WelcomeActivity.class);
                   startActivity(intent);
                   finish();
               }
           }
       },2000);


   }
}