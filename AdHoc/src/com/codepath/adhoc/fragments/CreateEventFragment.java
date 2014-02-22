package com.codepath.adhoc.fragments;

import android.support.v4.app.Fragment;

import com.codepath.adhoc.parsemodels.Events;

public abstract class CreateEventFragment extends Fragment {
	public abstract boolean checkData();
	public abstract Events getEvent();
}
