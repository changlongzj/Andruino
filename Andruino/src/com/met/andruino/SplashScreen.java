package com.met.andruino;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

public class SplashScreen extends Activity {

	private static int SPLASH_TIME_OUT = 500; // 3 seconds TODO

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		Log.d("SplashScreen::", "OnCreate");

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // Full screen splash
		//A comment 
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("SplashScreen::", "post delay");
				Intent intent = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(intent);
				Log.d("SplashScreen::", "After StartActivity");
				finish();
			}

		}, SPLASH_TIME_OUT);
	}

}
