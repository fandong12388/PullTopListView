package com.example.listviewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.example.listviewtest.SwipeListView.OnListViewScrollListener;

public class PullUpLayout extends RelativeLayout implements OnListViewScrollListener {
	private Context mContext;
	private RelativeLayout mHeaderContent;
	private SwipeListView mListView;
	private int mDistance;

	public PullUpLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initUI();
	}

	public PullUpLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initUI();
	}

	public PullUpLayout(Context context) {
		super(context);
		mContext = context;
		initUI();
	}

	private void initUI() {
		mDistance = Math.round(mContext.getResources().getDimension(R.dimen.distance));
		LayoutInflater.from(mContext).inflate(R.layout.vw_pull_up_layout, this);
		mHeaderContent = (RelativeLayout) findViewById(R.id.layout_header);
		mListView = (SwipeListView) findViewById(R.id.layout_listview);
		mListView.setOnListViewScrollListener(this);
	}

	public void setAdapter(BaseAdapter adapter) {
		mListView.setAdapter(adapter);
	}

	public void setHeaderView(View view) {
		mHeaderContent.addView(view);
	}

	@Override
	public SwipeListView.State onScroll(int y) {
		RelativeLayout.LayoutParams params = (LayoutParams) getLayoutParams();
		int oldTopMargin = params.topMargin;
		int newTopMargin = params.topMargin + y;
		if (newTopMargin <= 0) {
			if (oldTopMargin != 0) {
				params.topMargin = 0;
				setLayoutParams(params);
			}
			return SwipeListView.State.TOP;
		} else if (newTopMargin > mDistance) {
			if (oldTopMargin != mDistance) {
				newTopMargin = mDistance;
				params.topMargin = newTopMargin;
				setLayoutParams(params);
			}
			return SwipeListView.State.BOTTOM;
		} else {
			params.topMargin = newTopMargin;
			setLayoutParams(params);
			return SwipeListView.State.NORMAL;
		}
	}

	@Override
	protected boolean awakenScrollBars() {
		return false;
	}

	@Override
	public void onScrollCheck() {
		RelativeLayout.LayoutParams params = (LayoutParams) getLayoutParams();
		int margin = params.topMargin;
		if ((margin > 0 && margin <= mDistance / 2) || (margin < 0)) {
			params.topMargin = 0;
			setLayoutParams(params);
		} else if (margin > mDistance || (margin >= mDistance / 2 && margin < mDistance)) {
			params.topMargin = mDistance;
			setLayoutParams(params);
		}
	}
}
