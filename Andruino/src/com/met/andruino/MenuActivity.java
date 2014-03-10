package com.met.andruino;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.met.andruino.adapters.ModesListAdapter;

public class MenuActivity extends Activity {
	public ListView listV;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		listV = (ListView) findViewById(R.id.main_menuList);

		String [] strEvents = {"Robot autònom", "Buscador de pas", "Torito", "Seguidor de línies", "Pendent", "Laberint",
				"Detecció de moviment", "Control de motors", "Telepresència"}; //Hard-coded array: better solution: xml
		List<String> events = Arrays.asList(strEvents);
		Log.d("MainActivity - List", events.toString());
		
		ModesListAdapter mAdapter = new ModesListAdapter(this, events);
		Log.d("MainActivity - List", "After new ModesListAdapter::: Count : " + mAdapter.getCount());
		
		if (listV == null) Log.d("MainActivity - List", "ListView NULL!!!");
		
		listV.setAdapter(mAdapter);
		Log.d("MainActivity - List", "After setting Adapter");

		listV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Object obj = listV.getItemAtPosition(arg2);
				
				/*Toast.makeText(mView.getContext(), "You've clicked " + ((MyEvents)obj).title, 
						Toast.LENGTH_SHORT).show();*/
			}
		});
		
		
	}
}
