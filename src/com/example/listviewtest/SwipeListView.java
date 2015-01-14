package com.example.listviewtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

@SuppressLint("ClickableViewAccessibility")
public class SwipeListView extends ListView {
	private OnListViewScrollListener mOnListViewScrollListener;
	private float lastY;
	private float tempY;
	private int distanceY;
	private boolean mListViewFocus;
	private State mState;
	private BaseAdapter mCustomAdapter;

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (mCustomAdapter == null || mCustomAdapter != adapter) {
			mCustomAdapter = (BaseAdapter) adapter;
		}
		super.setAdapter(adapter);
	}

	public void setOnListViewScrollListener(OnListViewScrollListener onListViewScrollListener) {
		this.mOnListViewScrollListener = onListViewScrollListener;
	}

	public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SwipeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SwipeListView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mListViewFocus) {
			return super.onTouchEvent(ev);
		} else {
			return onCustomEvent(ev);
		}
	}

	@SuppressLint("NewApi")
	private boolean onCustomEvent(MotionEvent event) {
		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			lastY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			tempY = event.getRawY();
			distanceY = (int) (tempY - lastY);
			lastY = tempY;
			if (mOnListViewScrollListener != null) {
				mState = mOnListViewScrollListener.onScroll(distanceY);
				if (mState != State.NORMAL) {
					getHandler().post(new Runnable() {
						@Override
						public void run() {
							setSelection(0);
						}
					});
					mListViewFocus = (mState == State.TOP);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mOnListViewScrollListener != null) {
				mOnListViewScrollListener.onScrollCheck();
			}
			lastY = 0.f;
			tempY = 0.f;
			distanceY = 0;
			break;
		default:
			break;
		}
		return true;
	}

	public int getCustomScrollY() {
		View c = getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = getFirstVisiblePosition();
		int top = c.getTop();
		return -top + firstVisiblePosition * c.getHeight();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if (clampedY) {
			if (getFirstVisiblePosition() < 2) {
				if (mState == State.TOP) {
					mListViewFocus = false;
				}
			}
			if (getLastVisiblePosition() >= (getAdapter().getCount() - 1)) {
				if (mState == State.BOTTOM) {
					mListViewFocus = false;
				}
			}
		}
	}

	public static interface OnListViewScrollListener {
		State onScroll(int y);

		void onScrollCheck();
	}

	public static enum State {
		TOP, BOTTOM, NORMAL
	}
}
