package com.codepath.adhoc.activities;

import com.codepath.adhoc.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreateEventMapActivity extends Fragment {
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
}
