package com.example.listviewtest;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

	private PullUpLayout mLayout;
	private ArrayList<String> list;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLayout = (PullUpLayout) findViewById(R.id.layout);
		// ------------------------------------------------------
		TextView view = new TextView(this);
		view.setText("I am the HeaderView!");
		view.setBackgroundColor(0x22FF0000);
		view.setPadding(50, 10, 50, 10);
		mLayout.setHeaderView(view);
		//
		list = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			list.add("TEST:" + i);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		// ------------------------------------------------------
		mLayout.setAdapter(adapter);
	}
}
