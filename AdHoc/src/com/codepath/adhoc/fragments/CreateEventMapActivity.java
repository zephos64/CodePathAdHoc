package com.codepath.adhoc.fragments;

import com.codepath.adhoc.R;
import com.codepath.adhoc.parsemodels.Events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreateEventMapActivity extends CreateEventFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inf.inflate(R.layout.fragment_create_event_map, parent, false);
		return view;
	}
	
	@Override
	public void checkData() {
		
	}

	@Override
	public Events getEvent() {
		// TODO Auto-generated method stub
		return null;
	}
}
