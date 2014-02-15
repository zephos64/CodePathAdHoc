package com.codepath.adhoc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class EventList extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getMoreListData(10);
	}
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent,
			Bundle savedInstanceState) {
		//View view = inf.inflate(R.layout.fragment_list, parent, false);
		
		setupListeners();

		return getView();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	abstract void getMoreListData(int numNewItems);
	
	private void setupListeners() {
		
	}
	
	
}
