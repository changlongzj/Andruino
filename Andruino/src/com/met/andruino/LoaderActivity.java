package com.met.andruino;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoaderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loader);
	}
	
	//TODO Provisional
	public void PassarAlMenu()
	{
		Intent intent = new Intent(LoaderActivity.this, MenuActivity.class);
		startActivity(intent);
		finish();
	}

}
