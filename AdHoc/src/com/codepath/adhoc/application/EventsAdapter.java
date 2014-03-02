package com.codepath.adhoc.application;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.adhoc.AdHocUtils;
import com.codepath.adhoc.R;
import com.codepath.adhoc.animations.AnimatorHelper;
import com.codepath.adhoc.parsemodels.Events;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.parse.ParseUser;

public class EventsAdapter extends ArrayAdapter<Events> {
	
	private List<Events> mEvents;
	protected String str = "";
	private int mPreviousPosition = -1;
	private final float mAnimX = 140;
    private final float mAnimY = 140;
    private ArrayList<Animator> mAnimatorList = new ArrayList<Animator>();

	public EventsAdapter(Context context, List<Events> objects) {
		super(context, com.codepath.adhoc.R.layout.fragment_item_event, objects);
		this.mEvents = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(com.codepath.adhoc.R.layout.fragment_item_event, null);
		}
		
		final Events events = mEvents.get(position);
		
		Log.d("DEBUG", "Position getView at " + position);
		
		TextView tvLoginTitle = (TextView) convertView.findViewById(R.id.tvLoginTitle);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tvStartTime);
		TextView tvRemainingSpots = (TextView) convertView.findViewById(R.id.tvListMaxAttendees);
		TextView tvHostedOrJoined = (TextView) convertView.findViewById(R.id.tvHostedOrJoined);
		ProgressBar pbAttendeesAmount = (ProgressBar) convertView.findViewById(R.id.pbAttendeeBar);
		ImageView ivStatus = (ImageView) convertView.findViewById(R.id.ivStatus);
		
		tvLoginTitle.setText(events.getEventName());
		
		pbAttendeesAmount.setMax(events.getMaxAttendees());
		pbAttendeesAmount.setProgress(events.getAttendanceCount());
		
		if(events.getMaxAttendees() == events.getAttendanceCount()) {
			tvRemainingSpots.setText("FULL");
			tvRemainingSpots.setTextColor(Color.parseColor("#FF0000"));
		} else {
			tvRemainingSpots.setText(String.valueOf(events.getAttendanceCount()));
			tvRemainingSpots.setTextColor(Color.parseColor("#00A7CF"));
		}
		tvTime.setText(AdHocUtils.getTime(events.getEventTime()));
		
		
		if(events.getHostUserId().equals(ParseUser.getCurrentUser().getObjectId())) {
			tvHostedOrJoined.setText("HOST");
			
			setImage("@drawable/ic_star", ivStatus, convertView);
			
		} else if(events.getJoinedUserIds().contains(ParseUser.getCurrentUser())) {
			tvHostedOrJoined.setText("JOINED");
			setImage("@drawable/ic_user_joined", ivStatus, convertView);
		} else {
			tvHostedOrJoined.setText("JOINED");
			tvHostedOrJoined.setVisibility(View.INVISIBLE);
			ivStatus.setVisibility(View.INVISIBLE);
		}
		
		/*Animation animation = new AlphaAnimation(0.0f,1.0f);  
        animation.setFillAfter(true);  
        animation.setDuration(100);  
        animation.setStartOffset(position * 100);  
        convertView.startAnimation(animation);  */
		animatePostIcs(position, convertView);
		mPreviousPosition = position;
		
		return convertView;
	}

	public void setImage(String uri, ImageView ivImage, View convertView) {
		int imageResource = convertView.getResources().getIdentifier(uri, null, convertView.getContext().getPackageName());
		Drawable res = convertView.getResources().getDrawable(imageResource);
		ivImage.setImageDrawable(res);
	}
	
	private void animatePostIcs(int position, View view) {
        float translationX;
        float translationY;

        if (mPreviousPosition < position) {
            translationX = mAnimX;
            translationY = mAnimY;
        } else {
            translationX = -mAnimX;
            translationY = -mAnimY;
        }

        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(view, "translationX", translationX, 0f);
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(view, "translationY", translationY, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationXAnimator, translationYAnimator);
        animatorSet.setDuration(AnimatorHelper.DURATION_LONG);
        animatorSet.addListener(new AnimatorWithLayerListener(view));

        mAnimatorList.add(animatorSet);

        animatorSet.start();
    }
	class AnimatorWithLayerListener implements Animator.AnimatorListener {

        private final View mView;

        public AnimatorWithLayerListener(View view) {
            mView = view;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            ViewCompat.setHasTransientState(mView, true);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ViewCompat.setHasTransientState(mView, false);
            mAnimatorList.remove(animation);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        	ViewHelper.setTranslationX(mView, 0f);
        	ViewHelper.setTranslationY(mView, 0f);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
